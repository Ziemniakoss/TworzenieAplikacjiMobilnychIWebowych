package com.ziemniak.webcli;

import com.ziemniak.webcli.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;

@RestController
public class CHomeFileFetcher {
	private final Logger log = LoggerFactory.getLogger(CHomeFileFetcher.class);

	@GetMapping(value = "/home/fetchfile/{id}", produces = "multipart/mixed")
	public ResponseEntity fetchFile(@CookieValue(value = "jwt", defaultValue = "") String jwt, @PathVariable(value = "id") String id, HttpServletResponse resp) throws IOException {
		if ("".equals(jwt)) {
			log.warn("Rejected file fetch request due to lack of JWT");
			resp.sendRedirect("/login");
			return null;
		}
		String url = ClientApplication.URL_TO_SERVER + "/files/get/" + id;
		RestTemplate rt = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Cookie", "jwt=" + jwt);

		HttpEntity httpEntity = new HttpEntity("", headers);
		Resource res = rt.exchange(url, HttpMethod.GET, httpEntity, Resource.class).getBody();
		Path path = FileUtils.saveTemp(res);

		res = new FileSystemResource(path);
		return ResponseEntity.ok(res);
	}

}
