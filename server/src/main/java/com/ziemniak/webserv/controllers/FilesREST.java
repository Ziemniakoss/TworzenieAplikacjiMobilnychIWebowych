package com.ziemniak.webserv.controllers;

import com.ziemniak.webserv.repositories.files.FileDoesNotExistException;
import com.ziemniak.webserv.repositories.files.FileInfo;
import com.ziemniak.webserv.dto.FileShareRequestDTO;
import com.ziemniak.webserv.dto.FileUploadPositiveResponseDTO;
import com.ziemniak.webserv.dto.RevokeAccessToFileRequestDTO;
import com.ziemniak.webserv.repositories.files.FileRepository;
import com.ziemniak.webserv.repositories.users.UserDoesNotExistException;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@Api(description = "Allows to access files stored on server")
public class FilesREST {
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private FileRepository fileRepository;

	private final Logger log = LoggerFactory.getLogger(FilesREST.class);

	@PostMapping(value = "/files/add", produces = "application/json")
	@ApiOperation(value = "Creates new file in database")
	@ApiResponses({
			@ApiResponse(code = 200, message = "User was authorized and file was created successfully", response = FileUploadPositiveResponseDTO.class),
			@ApiResponse(code = 401, message = "User used invalid or expired JWT", response = String.class)
	})
	public ResponseEntity add(@RequestParam("file") MultipartFile file, @RequestParam("fileName") String filename, HttpServletRequest req) {
		String username = jwtUtils.getUsername(req.getHeader("Authorization").substring(7));
		log.info('"' + username + "\" is adding file " + file.getName());
		fileRepository.saveFile(username, file);
		log.info(username + " added  file " + file.getName());
		return new ResponseEntity<>(new FileUploadPositiveResponseDTO(file.getName()), HttpStatus.OK);
	}

	@GetMapping(value = "/files/get/{id}")// TODO: 1/8/20 swagger 
	public ResponseEntity<?> getFile(@PathVariable int id, HttpServletRequest req, HttpServletResponse resp) {
		String username = getUsernameFromJwt(req);
		if (!fileRepository.hasAccess(id, username)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		try {
			byte[] file = fileRepository.getFile(id);
			resp.setHeader("Content-disposition", "attachment; filename=test");//todo faktyczna nazwa
			return ResponseEntity.ok(file);
		} catch (FileDoesNotExistException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@GetMapping(value = "/files/getall", consumes = "application/json", produces = "application/json")
	@ApiOperation(value = "Get info about all  uploaded by user", consumes = "application/json", produces = "application/json")
	@ApiResponses({
			@ApiResponse(code = 200, message = "User was authorized and server returned all user's files info"),
			@ApiResponse(code = 401, message = "jwt cookie contained invalid JWT")
	})
	private ResponseEntity getAll(@CookieValue(value = "jwt", defaultValue = "") String jwt, HttpServletRequest httpServletRequest) {
		if (!jwtUtils.verify(jwt)) {
			log.warn("Someone used invalid JWT(" + jwt + ")");
			return new ResponseEntity<>("JWT is not valid", HttpStatus.UNAUTHORIZED);
		}
		String username = jwtUtils.getUsername(jwt);
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
	public ResponseEntity<?> shareFile(@RequestBody FileShareRequestDTO fileShareRequest, HttpServletRequest req) {
		String jwt = req.getHeader("Authorization");
		//todo
		return null;
	}

	@PostMapping("/files/revokeaccess")
	public ResponseEntity<?> revokeAccessToFile(@RequestBody RevokeAccessToFileRequestDTO revokeRequest, HttpServletRequest req) {
		String owner = getUsernameFromJwt(req);

		return null;
	}

	private String getUsernameFromJwt(HttpServletRequest req) {
		return jwtUtils.getUsername(req.getHeader("Authorization").substring(7));
	}
}
