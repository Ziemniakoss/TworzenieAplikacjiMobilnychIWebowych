package com.ziemniak.webcli.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileUtils {
	private static final Logger log = LoggerFactory.getLogger(FileUtils.class);
	/**
	 * Zapisuje plik tymczasowo na dysku
	 *
	 * @param file plik do zapisania
	 * @return ścieżka do pliku
	 */
	public static Path saveTemp(MultipartFile file) {
		new java.io.File("temp").mkdir();
		java.io.File f = null;
		int i = 1;
		while (true) {
			f = new java.io.File("temp" + java.io.File.separator + i);
			if (f.exists()) {
				i += 1;
			} else {
				break;
			}
		}
		log.info("Saving " + file.getName() + " temporarily on disc as " + f.toPath());
		try {
			InputStream inputStream = file.getInputStream();
			Files.copy(inputStream, f.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f.toPath();
	}

}
