package com.ziemniak.webcli.controllers;

import com.ziemniak.webcli.ClientApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class FilesFetcherController {
	private final Logger log = LoggerFactory.getLogger(FilesFetcherController.class);

	@GetMapping("/myfiles/download/{id}")
	public ResponseEntity<?> fetchFile(@PathVariable int id, @CookieValue(defaultValue = "", value = "jwt") String jwt,
									   HttpServletResponse response) throws IOException {
		if ("".equals(jwt)) {
			response.sendRedirect("/login");
			return null;
		}
//		try {
		log.info("Downloading file " + id);
		ResponseEntity<byte[]> file = getFileFromServer(id, jwt);
		log.info("Downloaded file");
		HttpHeaders headers = new HttpHeaders();
		ResponseEntity re = new ResponseEntity(file.getBody(), file.getHeaders(), HttpStatus.OK);
		return re;//todo a moze zwraca od razu?
//		} catch (HttpClientErrorException e) {
//			log.warn("Failed to download file(" + e.getStatusCode() + "): " + e.getMessage());
//			String url = null;
//			switch (e.getStatusCode()) {
//				case FORBIDDEN:
//					url = "/login";
//					break;
//				case NOT_FOUND:
//					url = "/error/404";
//					break;
//				case UNAUTHORIZED:
//					url = "/error/401";
//					break;
//				default:
//					url = "/error/client";
//					log.error("Unkonwn client error " + e.getRawStatusCode() + "while fetching file: " + e.getMessage());
//			}
//			return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
//					.header(HttpHeaders.LOCATION, url).build();
//		} catch (RestClientException e) {
//			log.error("Unknown error occured while downloading file from server: " + e.getMessage());
//			return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
//					.header(HttpHeaders.LOCATION, "/error/server").build();
//		}
	}

	private ResponseEntity<byte[]> getFileFromServer(int id, String jwt) {
		RestTemplate rt = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + jwt);
		HttpEntity e = new HttpEntity(headers);
		log.info("DOwnloading file " + id);
		String url = ClientApplication.URL_TO_SERVER + "/files/get/" + id;
		return rt.exchange(url, HttpMethod.GET, e, byte[].class);
	}

}
