package com.ziemniak.webserv;

import com.ziemniak.webserv.dto.FileShareRequestDTO;
import com.ziemniak.webserv.dto.FileUploadPositiveResponseDTO;
import com.ziemniak.webserv.dto.RevokeAccessToFileRequestDTO;
import com.ziemniak.webserv.filestorage.File;
import com.ziemniak.webserv.filestorage.FileDoesNotExist;
import com.ziemniak.webserv.filestorage.StorageException;
import com.ziemniak.webserv.filestorage.StorageService;
import com.ziemniak.webserv.repositories.FileInfoRepository;
import com.ziemniak.webserv.repositories.files.FileRepository;
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
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@Api(description = "Allows to access files stored on server")
public class FilesREST {
	@Autowired
	private StorageService storage;
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private FileInfoRepository fileInfoRepository;
	@Autowired
	private FileRepository fileRepository;

	private final Logger log = LoggerFactory.getLogger(FilesREST.class);

	@PostMapping(value = "/files/add", produces = "application/json")
	@ApiOperation(value = "Creates new file in database")
	@ApiResponses({
			@ApiResponse(code = 200, message = "User was authorized and file was created successfully", response = FileUploadPositiveResponseDTO.class),
			@ApiResponse(code = 401, message = "User used invalid or expired JWT", response = String.class)
	})
	public ResponseEntity add(@CookieValue(value = "jwt", defaultValue = "") String jwt, @RequestParam("file") MultipartFile file, @RequestParam("fileName") String filename) {
		System.err.println(file.getOriginalFilename());
		if (!jwtUtils.verify(jwt)) {
			log.warn("Someone tried to add file without valid JWT");
			return new ResponseEntity<>("JWT is not valid", HttpStatus.UNAUTHORIZED);
		}
		String username = jwtUtils.getUsername(jwt);
		log.info('"' + username + "\" is adding file " + file.getName());
		try {
			storage.store(username, file, filename);
			log.info(username + " added  file " + file.getName());
			return new ResponseEntity<>(new FileUploadPositiveResponseDTO(file.getName()), HttpStatus.OK);
		} catch (StorageException e) {
			log.error("Failed to save file " + file.getName() + " for user " + username);
			e.printStackTrace();
		}
		return null;//todo
	}

	@GetMapping(value = "/files/get/{id}", produces = "multipart/mixed")
	public ResponseEntity getFile(HttpServletResponse response, @PathVariable String id, @CookieValue(value = "jwt", defaultValue = "") String jwt) {
		if (!jwtUtils.verify(jwt)) {
			log.warn("Someone tried to download file without valid JWT(" + jwt + ')');
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		String username = jwtUtils.getUsername(jwt);
		try {
			File f = fileInfoRepository.getFile(username, id);
			response.setHeader("Content-disposition", "attachment; filename=" + f.getName());
			OutputStream out = response.getOutputStream();
			Path path = Paths.get(storage.get(username, id).toURI());
			log.info("Sending file " + path.toUri());
			Files.copy(path, out);
			out.close();


			//usuwam plik tymczasowy
			log.info("Deleting temporarty file: " + path);
			Files.delete(path);
			Files.delete(path.getParent());
		} catch (FileDoesNotExist fileDoesNotExist) {
			ResponseEntity r = new ResponseEntity(HttpStatus.NOT_FOUND);
			fileDoesNotExist.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (StorageException e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok().build();
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
		List<File> files = fileInfoRepository.getAllFiles(username);
		log.info("Returning fileList(" + files.size() + " items)");
		return new ResponseEntity<>(files, HttpStatus.OK);
	}

	@PostMapping("/files/share")
	public ResponseEntity<?> shareFile(@RequestBody FileShareRequestDTO fileShareRequest, HttpServletRequest req){

	}

	@PostMapping("/files/revokeaccess")
	public ResponseEntity<?> revokeAccessToFile(@RequestBody RevokeAccessToFileRequestDTO revokeRequest, HttpServletRequest req){

	}
}
