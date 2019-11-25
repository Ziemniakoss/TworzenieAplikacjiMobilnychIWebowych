package com.ziemniak.webserv.repositories;

import com.ziemniak.webserv.filestorage.File;
import com.ziemniak.webserv.filestorage.FileDoesNotExist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class FileInfoRepository {
	private final Logger log = LoggerFactory.getLogger(FileInfoRepository.class);
	private static final String PREFIX = "file:";


	public File getFile(String username, String id) throws FileDoesNotExist {
		Jedis j = new Jedis();
		Map<String, String> fileParams = j.hgetAll(getKey(username, id));
		if (fileParams.isEmpty()) {
			throw new FileDoesNotExist();
		}
		File f = new File();
		f.setName(fileParams.get("name"));
		f.setId(fileParams.get("id"));
		j.close();
		return f;
	}

	public File storeFile(String username, File f) {
		if (username == null) {
			log.error("Tried to store file without specifying username");
			throw new NullPointerException("Null name");
		}
		if (f.getName() == null) {
			f.setName("Name Undefined");
		}
		Jedis j = new Jedis();
		String id = "" + j.incr(getIdKey(username));
		String key = getKey(username, id);
		j.hset(key, "id", id);
		j.hset(key, "name", f.getName());
		j.close();
		f.setId(id);
		return f;
	}


	public List<File> getAllFiles(String username) {
		Jedis j = new Jedis();
		Set<String> fileKeys = j.keys(getKeyPattern(username));
		List<File> files = new ArrayList<>();
		for (String key : fileKeys) {
			File f = new File();
			Map<String, String> fileParams = j.hgetAll(key);
			if (!fileParams.isEmpty()) {
				f.setId(fileParams.get("id"));
				f.setName(fileParams.get("name"));
			}
			files.add(f);
		}
		j.close();
		return files;
	}

	/**
	 * Zwraca klucz jaki odpowiada w bazie Redis danemu plikowi
	 *
	 * @param username właściciel pliku
	 * @param id       id pliku
	 * @return klucz pliku w bazie Redis
	 */
	private String getKey(String username, String id) {
		return PREFIX + username + ':' + id;
	}

	private String getKeyPattern(String username) {
		return PREFIX + username + ":*";
	}

	private String getIdKey(String username) {
		return PREFIX + username + "_id";
	}
}
