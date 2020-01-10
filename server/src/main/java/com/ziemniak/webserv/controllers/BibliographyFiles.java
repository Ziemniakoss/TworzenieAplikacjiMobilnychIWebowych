package com.ziemniak.webserv.controllers;

import com.ziemniak.webserv.repositories.bibliographies.BibliographiesRepository;
import com.ziemniak.webserv.repositories.bibliographies.BibliographyDoesNotExistsException;
import com.ziemniak.webserv.repositories.files.FileDoesNotExistException;
import com.ziemniak.webserv.repositories.files.PermissionDeniedException;
import com.ziemniak.webserv.utils.JwtUtils;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(description = "Zarządzanie plikami w bibliografii")
public class BibliographyFiles {
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private BibliographiesRepository bibliographiesRepository;
	private final Logger log = LoggerFactory.getLogger(BibliographiesRepository.class);

	@GetMapping("/bibliography/{bibliographyId}/files")
	public void getAllFilesInBibliography() {

	}

	@PostMapping("/bibliography/{bibliographyId}/files/{fileId}")
	public ResponseEntity addFileToBibliography(@PathVariable int bibliographyId, @PathVariable int fileId, HttpServletRequest req) {
		String username = jwtUtils.extractUsername(req);
		if (!bibliographiesRepository.hasAccess(bibliographyId, username)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nie masz uprawnień do bibliografii");
		}
		try {
			bibliographiesRepository.addFileToBibliography(bibliographyId, fileId,username);
			log.info(String.format("Added file %d to bibliography %d", fileId, bibliographyId));
			return ResponseEntity.ok().build();
		} catch (BibliographyDoesNotExistsException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bibliografia nie istnieje");
		} catch (FileDoesNotExistException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plik nie istnieje");
		} catch (PermissionDeniedException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nie masz uprawnień do pliku");
		}
	}

	@DeleteMapping("/bibliography/{bibliographyId}/files/{fileId}")
	public ResponseEntity deleteFileFromBibliography(@PathVariable int bibliographyId, @PathVariable int fileId, HttpServletRequest req) {
		String username = jwtUtils.extractUsername(req);
		if (!bibliographiesRepository.hasAccess(bibliographyId, username)) {
			log.warn(String.format("User '%s' tried to delete file %d from bibliography %d to which he has no access",
					username, fileId, bibliographyId));
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nie masz uprawnień do tej bibliografii");
		}
		try {
			if (bibliographiesRepository.deleteFileFromBibliography(bibliographyId, fileId)) {
				return ResponseEntity.ok("ok");
			}else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plik nie jest w bibliografii");
			}
		} catch (FileDoesNotExistException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plik nie istnieje");
		} catch (BibliographyDoesNotExistsException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bibliografia nie istnieje");
		}
	}
}
