package com.ziemniak.webcli.controllers;

import com.ziemniak.webcli.ClientApplication;
import com.ziemniak.webcli.dto.FileUploadPositiveResponseDTO;
import com.ziemniak.webcli.dto.GetAllFilesResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/home")
public class CHome {
	private final Logger log = LoggerFactory.getLogger(CHome.class);

	@GetMapping()
	public String home(@CookieValue(value = "jwt", defaultValue = "") String jwt, Model model) {
		if ("".equals(jwt)) {
			return "redirect:/login";
		}
		//pobranie listy publikacji
//		RestTemplate rt = new RestTemplate();
//		HttpHeaders headers = new HttpHeaders();
//		headers.set("Cookie", "jwt=" + jwt);
//		headers.setContentType(MediaType.APPLICATION_JSON);
//		HttpEntity entity = new HttpEntity("", headers);
//		Map<String, String > uriVariables = new HashMap<>();
//		uriVariables.put("jwt", jwt);
//
//		String url = ClientApplication.URL_TO_SERVER + "/files/getall";
//		System.out.println(jwt);
//
//		ResponseEntity<GetAllFilesResponseDTO> response = rt.exchange(url, HttpMethod.GET, entity, GetAllFilesResponseDTO.class,new HashMap<>());
//		List<com.ziemniak.webcli.File> files = response.getBody().getFiles();
//		log.info("Got fileList");
//		System.out.println(files);
//		model.addAttribute("files", files);
		return "home";
	}

	@PostMapping()
	public String postFile(@CookieValue(value = "jwt", defaultValue = "") String jwt, @RequestParam("file") MultipartFile file, Model model) throws IOException {
		if ("".equals(jwt)) {
			return "redirect:/login";
		}
		if (file.isEmpty()) {
			log.warn("Tried to upload empty file");
			model.addAttribute("errorreason", "File was empty");
			return "redirect:/home";
		}
		Path pathToTemp = save(file);
		FileSystemResource resource = new FileSystemResource(pathToTemp) {
			@Override
			public String getFilename() {
				return file.getName();
			}
		};

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.set("Cookie", "jwt=" + jwt);


		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file", resource);
		body.add("filename", "test");
		body.add("name", "test");
		System.out.println(body.get("file").isEmpty());
		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);
		String url = ClientApplication.URL_TO_SERVER + "/files/add";
		RestTemplate restTemplate = new RestTemplate();
		try {
			restTemplate.postForObject(url, httpEntity, FileUploadPositiveResponseDTO.class);
			log.info("Dont u worry it works xd");
		} catch (Exception e) {
			e.printStackTrace();
		}
		new File(pathToTemp.toUri()).delete();
		log.info("Deleted temporary file " + pathToTemp.getFileName());

		return "redirect:/home";
	}

	/**
	 * Zapisuje plik tymczasowo na dysku
	 *
	 * @param file plik do zapisania
	 * @return ścieżka do pliku
	 */
	private Path save(MultipartFile file) {
		new File("temp").mkdir();
		//Szukamy wolnej ścieżki
		log.info("Findint teporary path for file");
		File f = null;
		int i = 1;
//		while (true) {
//			f = new File("temp" + File.separator + i);
//			if (!f.exists()) {
//				break;
//			}
//		}
		f = new File("temp/" + file.getName());
		log.info("Saving " + file.getName() + " temporarily on disc as temp/" + i);
		try {
			InputStream inputStream = file.getInputStream();
			Files.copy(inputStream, f.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f.toPath();
	}
}
