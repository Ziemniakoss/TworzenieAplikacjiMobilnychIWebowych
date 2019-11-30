package com.ziemniak.webserv.filestorage;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface StorageService {

	void addUser(String user);

	void store(String username, MultipartFile file, String filename) throws StorageException;

	/**
	 * Usuwa plik z bazy plików
	 *
	 * @param username właściciel pliku
	 * @param fileId   id pliku
	 */
	void delete(String username, String fileId) throws FileDoesNotExist;

	/**
	 * Zwraca plik z bazy plików
	 *
	 * @param username
	 * @param fileId
	 * @return
	 */
	File get(String username, String fileId) throws StorageException, IOException;
}
