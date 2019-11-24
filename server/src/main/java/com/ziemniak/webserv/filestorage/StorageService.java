package com.ziemniak.webserv.filestorage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface StorageService {

	void addUser(String user);

	void store(String username, MultipartFile file);

	/**
	 * Usuwa plik z bazy plików
	 *
	 * @param username właściciel pliku
	 * @param fileId   id pliku
	 */
	void delete(String username, String fileId) throws FileDoesNotExist;

	/**
	 * Zwraca plik z bazy plików
	 * @param username
	 * @param fileId
	 * @return
	 */
	Resource get(String username, String fileId);


	/**
	 * Znajduje wszystkie pliki danego użytkownika
	 *
	 * @param username Użytkownik którego pliki należy znaleźć
	 * @return Mapę gdzie kluczami są id plików a wartościami faktyczne nazwy plików
	 */
	Map<String, String> getAllFileNames(String username);

}
