package com.ziemniak.webserv;

import com.ziemniak.webserv.dto.FileNegativeResponseDto;
import com.ziemniak.webserv.dto.FileRequestDto;
import com.ziemniak.webserv.dto.FileUploadPositiveResponseDTO;
import com.ziemniak.webserv.filestorage.File;
import com.ziemniak.webserv.filestorage.StorageException;
import com.ziemniak.webserv.filestorage.StorageService;
import com.ziemniak.webserv.repositories.FileInfoRepository;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api(description = "Allows to access files stored on server")
public class FileAccessREST {
	@Autowired
	private StorageService storage;
	@Autowired
	private TokenManager tokenManager;
	@Autowired
	private FileInfoRepository fileInfoRepository;

	private final Logger log = LoggerFactory.getLogger(FileAccessREST.class);

	@PostMapping(value = "/files/add")
	public ResponseEntity add(@CookieValue(value = "jwt", defaultValue = "") String jwt, @RequestParam("file") MultipartFile file) {

		if (!tokenManager.verify(jwt)) {
			log.warn("Someone tried to add file without valid JWT");
			return new ResponseEntity<>("JWT is not valid", HttpStatus.UNAUTHORIZED);
		}
		String username = tokenManager.getUsername(jwt);
		log.info('"'+username+"\" is adding file "+file.getName());
		try {
			storage.store(username, file);
			log.info(username+" added  file "+file.getName());
			return new ResponseEntity<>(new FileUploadPositiveResponseDTO(file.getName()),HttpStatus.OK);
		} catch (StorageException e) {
			log.error("Failed to save file " + file.getName() + " for user " + username);
			e.printStackTrace();
		}
		return null;//todo
	}

	@GetMapping(value = "/files/get", consumes = "application/json", produces = "application/json")
	public ResponseEntity getFile(@CookieValue(value = "jwt", defaultValue = "") String jwt, @Valid @RequestBody FileRequestDto req, Errors errors) {
		if (errors.hasErrors()) {
			log.error("Request for single file had " + errors.getErrorCount() + " errors: " + errors.getAllErrors());
			return new ResponseEntity<>(new FileNegativeResponseDto(errors.getAllErrors().toString()), HttpStatus.BAD_REQUEST);
		}
		String username = tokenManager.getUsername(jwt);

		try {
			Resource resource = storage.get(username, "" + req.getId());
			return new ResponseEntity<>(resource, HttpStatus.OK);
		} catch (StorageException e) {
			log.error("Failed to get resource " + req.getId() + "for user " + username);
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping(value = "/files/getall", produces = "application/json", consumes = "application/json")
	private ResponseEntity getAll(@CookieValue(value = "jwt", defaultValue = "") String jwt) {
		if (!tokenManager.verify(jwt)) {
			return new ResponseEntity<>("JWT is not valid", HttpStatus.UNAUTHORIZED);
		}
		String username = tokenManager.getUsername(jwt);
		List<File> files = fileInfoRepository.getAllFiles(username);
		return new ResponseEntity<>(files, HttpStatus.OK);
	}
}
