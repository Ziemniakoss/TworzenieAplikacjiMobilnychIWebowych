package com.ziemniak.webcli.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
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
	public static Path saveTemp(MultipartFile file) throws IOException {
		File f = findLocation(file.getOriginalFilename());
		log.info("Saving " + file.getName() + " temporarily on disc as " + f.toPath());
		InputStream inputStream = file.getInputStream();
		FileOutputStream out = new FileOutputStream(f);
		Files.copy(inputStream, f.toPath(),StandardCopyOption.REPLACE_EXISTING);
		return f.toPath();
	}

	public static Path saveTemp(Resource res) throws IOException {
		File output = findLocation(res.getFilename());
		log.info("Saving resource " + res.getFilename() + " temporarily on disc as " + output.toURI());
		System.out.println(res.getFilename());

		InputStream inputStream = res.getInputStream();
		Files.copy(inputStream, output.toPath(), StandardCopyOption.REPLACE_EXISTING);
		return output.toPath();
	}

	private static File findLocation(String filename) {
		new File("temp").mkdir();
		int i = 1;
		File f;
		while (true) {
			f = new File("temp" + File.separator + i);
			if (f.exists()) {
				i++;
			} else {
				break;
			}
		}
		f.mkdirs();
		System.out.println(f);
		f = new File(f + File.separator + filename);
		log.info("Found new location to teporarily save: " + f.toURI());
		return f;
	}
}
