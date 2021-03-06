package com.ziemniak.webserv.controllers;

import com.ziemniak.webserv.dto.*;
import com.ziemniak.webserv.repositories.files.FileDoesNotExistException;
import com.ziemniak.webserv.repositories.files.FileInfo;
import com.ziemniak.webserv.repositories.files.FileRepository;
import com.ziemniak.webserv.repositories.files.PermissionDeniedException;
import com.ziemniak.webserv.repositories.users.UserDoesNotExistException;
import com.ziemniak.webserv.repositories.users.UserRepository;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@Api(description = "Pozwala na pobieranie i udostępnianie plików")
public class FilesAccess {
	private final JwtUtils jwtUtils;
	private final FileRepository fileRepository;


	private final Logger log = LoggerFactory.getLogger(FilesAccess.class);

	@Autowired
	public FilesAccess(JwtUtils jwtUtils, FileRepository fileRepository) {
		this.jwtUtils = jwtUtils;
		this.fileRepository = fileRepository;
	}

	@PostMapping(value = "/files/add", produces = "application/json")
	@ApiOperation(value = "Tworzy nowy plik w bazie danych")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Plik został zapisany", response = FileUploadPositiveResponseDTO.class),
			@ApiResponse(code = 403, message = "Żądanie nie zawierało nagłówka z jwt")
	})
	public ResponseEntity add(@RequestBody FileUploadDTO file,  HttpServletRequest req) {
		String username = jwtUtils.getUsername(req.getHeader("Authorization").substring(7));
		log.info('"' + username + "\" is adding file " + file.getName());
		fileRepository.saveFile(username, file.getFile(), file.getName());
		return new ResponseEntity<>("ok", HttpStatus.OK);
	}

	@GetMapping(value = "/files/get/{id}")
	@ApiOperation(value = "Pobierz plik o podanym id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Plik został przesłany"),
			@ApiResponse(code = 401, message = "Nie masz dostępu do tego pliku"),
			@ApiResponse(code = 403, message = "Żądanie nie zawierało nagłówka z jwt"),
			@ApiResponse(code = 404, message = "Plik nie istnieje")
	})
	public ResponseEntity<?> getFile(@PathVariable int id, HttpServletRequest req, HttpServletResponse resp) {
		String username = jwtUtils.extractUsername(req);
		log.info("User " + username + " is trying to download file " + id);
		try {
			byte[] file = fileRepository.getFile(id, username);
			resp.setHeader("Content-disposition", "attachment; filename="+fileRepository.getFileName(id));//todo faktyczna nazwa
			log.info("Sending file " + id + " to " + username);
			return ResponseEntity.ok(file);
		} catch (FileDoesNotExistException e) {
			log.warn("User " + username + " tried to download non existing file " + id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (PermissionDeniedException e) {
			log.warn("USer " + username + " tried to access  file " + id + " to which he has no permissions");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	@GetMapping(value = "/files/getall", consumes = "application/json", produces = "application/json")
	@ApiOperation(value = "Get info about all  uploaded by user", consumes = "application/json", produces = "application/json")
	@ApiResponses({
			@ApiResponse(code = 200, message = "User was authorized and server returned all user's files info"),
			@ApiResponse(code = 401, message = "jwt cookie contained invalid JWT")
	})
	private ResponseEntity getAll(HttpServletRequest httpServletRequest) {
		String username = jwtUtils.extractUsername(httpServletRequest);
		List<FileInfo> fileInfos = null;
		try {
			fileInfos = fileRepository.getAllOwnedFilesInfo(username);
		} catch (UserDoesNotExistException e) {
			//todo
		}
		log.info("Returning fileList(" + fileInfos.size() + " items)");
		return new ResponseEntity<>(fileInfos, HttpStatus.OK);
	}

	@PostMapping("/files/share")
	public ResponseEntity<?> grantAccessToFile(@RequestBody FileShareRequestDTO fileShareRequest, HttpServletRequest req) {
		String username = jwtUtils.extractUsername(req);
		try {
			fileRepository.grantAccess(fileShareRequest.getFileId(), username, fileShareRequest.getUsername());
			return ResponseEntity.ok("Ok");
		} catch (PermissionDeniedException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nie masz dostępu do tego pliku");
		} catch (UsernameNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Użytkownik nie istnieje");
		} catch (FileDoesNotExistException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plik nie istnieje");
		}
	}

	@PostMapping("/files/revokeaccess")
	public ResponseEntity<?> revokeAccessToFile(@RequestBody RevokeAccessToFileRequestDTO revokeRequest, HttpServletRequest req) {
		String owner = jwtUtils.extractUsername(req);
		System.out.println(revokeRequest);
		try {
			fileRepository.revokeAccess(revokeRequest.getFileId(), owner, revokeRequest.getUsername());
		} catch (FileDoesNotExistException | UsernameNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return null;
	}

	@GetMapping("/files/shared/mine")
	@ApiOperation(value = "Zwraca listę udostępninoych przez użytkownika plkiów razem z listą osób dla których są te pliki udostępnione")
	public ResponseEntity getAllUsersSharedFiles(HttpServletRequest req) {
		List<FileSharedByUserDTO> res = fileRepository.getAllUsersShardFiles(jwtUtils.extractUsername(req));
		return ResponseEntity.ok(res);
	}

	@GetMapping("/files/shared/forme")
	@ApiOperation(value = "Zwraca listęp plików udostępnionych dla użytkownika przez innych")
	public ResponseEntity getAllFilesSharedForUser(HttpServletRequest req) {
		return ResponseEntity.ok(fileRepository.getAllFilesSharedForUser(jwtUtils.extractUsername(req)));
	}
}
