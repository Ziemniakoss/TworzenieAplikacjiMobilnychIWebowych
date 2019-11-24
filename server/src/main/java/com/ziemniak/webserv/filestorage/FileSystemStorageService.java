package com.ziemniak.webserv.filestorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;

@Service
public class FileSystemStorageService implements StorageService {
	private final Logger log = LoggerFactory.getLogger(FileSystemStorageService.class);
	private final static String FILE_SYSTEM_ROOT = "webServerFiles";


	@Override
	public void addUser(String user) {
		log.info("Creating directory for \"" + user + "\"'s files");
		new File(getPath(user)).mkdir();
		log.info("Created directory for user \"" + user + "\"");
	}

	@Override
	public void store(String username, MultipartFile file) {


	}

	@Override
	public void delete(String username, String fileId) throws FileDoesNotExist {
		File f = new File(getPath(username,fileId));
		if(!f.exists()){
			throw new FileDoesNotExist();
		}
		boolean result = f.delete();
	}

	@Override
	public Resource get(String username, String fileId) {
		return null;
	}

	@Override
	public Map<String, String> getAllFileNames(String username) {
		return null;
	}

	private boolean exists(String user) {
		return new File(getPath(user)).exists();
	}

	private String getPath(String username) {
		return FILE_SYSTEM_ROOT + File.separator + username;
	}

	private String getPath(String username, String fileId) {
		return FILE_SYSTEM_ROOT + File.separator + username + File.separator + fileId;
	}
}
