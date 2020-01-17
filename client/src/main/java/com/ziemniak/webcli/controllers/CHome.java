package com.ziemniak.webcli.controllers;

import com.ziemniak.webcli.ClientApplication;
import com.ziemniak.webcli.File;
import com.ziemniak.webcli.dto.FileUploadPositiveResponseDTO;
import com.ziemniak.webcli.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

@Controller
@RequestMapping("/home")
public class CHome {
	private final Logger log = LoggerFactory.getLogger(CHome.class);

	@GetMapping(value = "/home/fetchfile/{id}", produces = "multipart/mixed")
	public String fetchFile(@CookieValue(value = "jwt", defaultValue = "") String jwt, @PathVariable(value = "id") String id, HttpServletResponse resp, @RequestHeader String host) throws IOException {
		resp.sendRedirect(ClientApplication.URL_TO_SERVER+"/files/"+id);

		System.out.println(host);
		if ("".equals(jwt)) {
			log.warn("Rejected file fetch request du to lack of JWT");
			resp.sendRedirect("/login");
		}
		String url = ClientApplication.URL_TO_SERVER+"/files/get/" +id;
		log.info("Redirecting request for file "+id+" to "+url);
		resp.sendRedirect(url);

		RestTemplate rt = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + jwt);
		return "redirect:/myfiles";
	}


	@GetMapping()
	public String home(@CookieValue(value = "jwt", defaultValue = "") String jwt, Model model) {
		if ("".equals(jwt)) {
			return "redirect:/login";
		}
		//pobranie listy publikacji
		try {
			File[] files = fetchFileList(jwt);
			model.addAttribute("fileList", files);
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
				log.warn("Error while fetching files due to expired or invalid JWT");
				return "redirect:/login";
			}
		} catch (HttpServerErrorException e) {
			log.error("Server experienced error while processing file list get request");
			return "redirect:/error";
		}
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
		Path pathToTemp = FileUtils.saveTemp(file);
		FileSystemResource resource = new FileSystemResource(pathToTemp) {
			@Override
			public String getFilename() {
				return file.getName();
			}
		};

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.set("Authorization", "Bearer " + jwt);


		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file", resource);
		body.add("fileName", file.getOriginalFilename());
		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);
		String url = ClientApplication.URL_TO_SERVER + "/files/add";
		RestTemplate restTemplate = new RestTemplate();
		try {
			restTemplate.postForObject(url, httpEntity, FileUploadPositiveResponseDTO.class);
			log.info("Successfully send file");
		} catch (HttpStatusCodeException e) {
			switch (e.getStatusCode()) {
				case BAD_REQUEST:
					log.error("Bad request received while trying to send file to server: " + e.getMessage());
					new java.io.File(pathToTemp.toUri()).delete();
					log.info("Deleted temporary file " + pathToTemp.getFileName());
					return "redirect:/error";
				case UNAUTHORIZED:
					new java.io.File(pathToTemp.toUri()).delete();
					log.info("Deleted temporary file " + pathToTemp.getFileName());
					return "redirect:/login";
				default:
					log.error("Unknown error occured with code " + e.getStatusCode() + ':' + e.getMessage());
					new java.io.File(pathToTemp.toUri()).delete();
					log.info("Deleted temporary file " + pathToTemp.getFileName());

					return "redirect:/error";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		new java.io.File(pathToTemp.toUri()).delete();
		log.info("Deleted temporary file " + pathToTemp.getFileName());
		return "redirect:/home";
	}

	/**
	 * Zapisuje plik tymczasowo na dysku
	 *
	 * @param file plik do zapisania
	 * @return ścieżka do pliku
	 */
	private Path saveTemp(MultipartFile file) {
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

	/**
	 * Pobiera listę plików z serwera
	 *
	 * @param jwt JWT utożsamiający użytkownika
	 * @return lista plików należących do posiadacza tokenu
	 * @throws HttpServerErrorException kiedy server napotka problem przy przetwarzaniu zapytania
	 * @throws HttpClientErrorException kiedy token autoryzujący użytkownika jest niepoprawny
	 *                                  lub nieaktualny
	 */
	private File[] fetchFileList(String jwt) throws HttpClientErrorException, HttpServerErrorException {
		RestTemplate rt = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + jwt);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity entity = new HttpEntity<>("", headers);
		String url = ClientApplication.URL_TO_SERVER + "/files/getall";
		ResponseEntity<File[]> response = rt.exchange(url, HttpMethod.GET, entity, com.ziemniak.webcli.File[].class, new HashMap<>());
		return response.getBody();
	}
}
