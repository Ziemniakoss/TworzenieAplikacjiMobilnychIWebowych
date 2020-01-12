package com.ziemniak.webserv.controllers;

import com.ziemniak.webserv.repositories.bibliographies.BibliographiesRepository;
import com.ziemniak.webserv.repositories.bibliographies.BibliographyDoesNotExistsException;
import com.ziemniak.webserv.repositories.files.FileDoesNotExistException;
import com.ziemniak.webserv.repositories.files.FileInfo;
import com.ziemniak.webserv.repositories.files.PermissionDeniedException;
import com.ziemniak.webserv.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
	@ApiOperation(value = "Pobiera wszystkie pliki z bibliografii")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Pobrano listę plików w bibliografii", response = FileInfo[].class),
			@ApiResponse(code = 401, message = "NIe masz upranień do tej bibliografii"),
			@ApiResponse(code = 403, message = "W żądaniu nie było nagłówka z jwt"),
			@ApiResponse(code = 404,message = "Bibliografia nie istnieje")
	})
	public ResponseEntity<?> getAllFilesInBibliography(@PathVariable int bibliographyId, HttpServletRequest req) {
		String username = jwtUtils.extractUsername(req);
		if(!bibliographiesRepository.hasAccess(bibliographyId,username)){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nie masz uprawnień do tej bibliografii");
		}
		if(!bibliographiesRepository.exists(bibliographyId)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bibliografia nie istnieje");
		}
		try{
			return ResponseEntity.ok(bibliographiesRepository.getAllFiles(bibliographyId));
		}catch (UsernameNotFoundException e){
			log.error("Illegal jwt used: signed for user that don't exist");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@PostMapping("/bibliography/{bibliographyId}/files/{fileId}")
	@ApiOperation(value = "Dodaje plik do bibliografii")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Udało sie dodać plik do bibliografii"),
			@ApiResponse(code = 401, message = "Nie masz uprawnień do bibliografii albo pliku"),
			@ApiResponse(code = 403, message = "W żądaniu nie było nagłówka z jwt"),
			@ApiResponse(code = 404,message = "Plik albo bibliografia nie istnieje")
	})
	public ResponseEntity addFileToBibliography(@PathVariable int bibliographyId, @PathVariable int fileId, HttpServletRequest req) {
		String username = jwtUtils.extractUsername(req);
		if (!bibliographiesRepository.hasAccess(bibliographyId, username)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nie masz uprawnień do bibliografii");
		}
		try {
			bibliographiesRepository.addFileToBibliography(bibliographyId, fileId, username);
			log.info(String.format("Added file %d to bibliography %d", fileId, bibliographyId));
			return ResponseEntity.ok().build();
		} catch (BibliographyDoesNotExistsException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bibliografia nie istnieje");
		} catch (FileDoesNotExistException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plik nie istnieje");
		} catch (PermissionDeniedException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nie jesteś właścicielem pliku");
		}
	}

	@DeleteMapping("/bibliography/{bibliographyId}/files/{fileId}")
	@ApiOperation(value = "usuwa plik z bibliografii")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Udało się usunąć plik z bibliogafii"),
			@ApiResponse(code = 401, message = "Nie masz dostępu do danej bibliografii"),
			@ApiResponse(code = 403, message = "Żądanie nie zawierało nagłówka z jwt"),
			@ApiResponse(code = 404, message = "Plik nie istnieje albo bibliografia nie istnieje albo plik nie jest w bibliografii")
	})
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
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plik nie jest w bibliografii");
			}
		} catch (FileDoesNotExistException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plik nie istnieje");
		} catch (BibliographyDoesNotExistsException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bibliografia nie istnieje");
		}
	}
}
