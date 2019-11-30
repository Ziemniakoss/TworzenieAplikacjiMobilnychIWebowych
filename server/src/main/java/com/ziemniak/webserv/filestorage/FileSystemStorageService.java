package com.ziemniak.webserv.filestorage;

import com.ziemniak.webserv.repositories.FileInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

@Service
public class FileSystemStorageService implements StorageService {
	private final Logger log = LoggerFactory.getLogger(FileSystemStorageService.class);
	private final static String FILE_SYSTEM_ROOT = "webServerFiles";
	@Autowired
	private FileInfoRepository fileInfoRepository;


	@Override
	public void addUser(String user) {
		log.info("Creating directory for \"" + user + "\"'s files");
		new File(getPath(user)).mkdir();
		log.info("Created directory for user \"" + user + "\"");
	}

	@Override
	public void store(String username, MultipartFile file, String fileName) throws StorageException {
		com.ziemniak.webserv.filestorage.File f = new com.ziemniak.webserv.filestorage.File();
		f.setName(fileName);
		if (file.isEmpty()) {
			log.warn("\"" + username + "\" tried to save empty file");
			throw new EmptyFile();
		}
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		if (filename.contains("..")) {
			log.warn('"' + username + "\" tried to save file with relative path outside current directory");
			throw new StorageException();
		}
		try {
			new File(getPath(username)).mkdirs();
			f.setCreationDate(LocalDateTime.now());
			InputStream inputStream = file.getInputStream();
			fileInfoRepository.storeFile(username, f);
			Path path = Paths.get(getPath(username, f.getId()));
			Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void delete(String username, String fileId) throws FileDoesNotExist {
		File f = new File(getPath(username, fileId));
		if (!f.exists()) {
			throw new FileDoesNotExist();
		}
		boolean result = f.delete();
	}

	/**
	 * Pobiera z bazy plików żądany plik. W celu zapewnienia, że nazwa pliku i jego rozszerzenie
	 * odpowiada orginałowi, najpierw jest ten zasób kopiowany do foleru temp/{randomInt}/{orginalna nazwa pliku}
	 * i dopiero z tego skopiowanego jest pobierany.
	 *
	 * <b>Uwaga</b>
	 * Pliki  nie są automatycznie usuwane, więc po skorzystaniu z zasobu należy ręcznie usunąć plik
	 *
	 * @param username właściciel pliku
	 * @param fileId   id pliku
	 * @return
	 * @throws StorageException
	 * @throws IOException      jeżeli nastąpi błąd przy kopiowaniu pliku
	 */
	@Override
	public File get(String username, String fileId) throws StorageException, IOException {
		try {
			String p = getPath(username, fileId);
			File src = new File(p);
			if (!src.exists()) {
				throw new FileDoesNotExist();
			}
			com.ziemniak.webserv.filestorage.File fileInfo = fileInfoRepository.getFile(username, fileId);
			//Szukam pierwszej wolnej nazwy dla tymczasowego pliku
			File dest = null;
			int i = 0;
			while (true) {
				dest = new File("temp" + File.separator + ++i);
				if (!dest.exists()) {
					dest.mkdirs();
					dest = new File("temp/" + i + '/' + fileInfo.getName());
					break;
				}
			}
			log.info("Coping file from " + src.toURI() + " to " + dest.toURI());
			Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
			return dest;
		} catch (MalformedURLException e) {
			log.error("Failed to read file: " + e.getMessage());
			throw new StorageException();
		}
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
