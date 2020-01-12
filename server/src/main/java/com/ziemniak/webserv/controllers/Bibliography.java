package com.ziemniak.webserv.controllers;

import com.ziemniak.webserv.dto.BibliographyCreationRequestDTO;
import com.ziemniak.webserv.repositories.bibliographies.BibliographiesRepository;
import com.ziemniak.webserv.repositories.bibliographies.BibliographyDoesNotExistsException;
import com.ziemniak.webserv.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

@RestController
@Api(description = "Zarządzanie bibliografiami(ale nie ich plikami)")
public class Bibliography {
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private BibliographiesRepository bibliographiesRepository;

	@GetMapping("/bibliography/{id}")
	@ApiOperation(value = "Pobierz inforamcje o bibliografii z danym id")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Pobrano dane", response = com.ziemniak.webserv.repositories.bibliographies.Bibliography.class),
			@ApiResponse(code = 404, message = "Bibliografia nie istnieje"),
			@ApiResponse(code = 401, message = "Nie posiadasz uprawnień do tej bibliografii"),
			@ApiResponse(code = 403, message = "Brak naglówka autoryzującego")
	})
	public ResponseEntity<?> getBibliographyInfo(@PathVariable int id, HttpServletRequest req) {
		String username = jwtUtils.getUsername(req.getHeader("Authorization").substring(7));
		if (!bibliographiesRepository.hasAccess(id, username)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		try {
			return ResponseEntity.status(HttpStatus.OK).body(bibliographiesRepository.get(id));
		} catch (BibliographyDoesNotExistsException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@GetMapping("/bibliography/getall")
	@ApiOperation(value = "Pobiera wszystkie bibliografie należące do użytkownika")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Udało sie pobrać"),//todo response
			@ApiResponse(code = 403, message = "Brak naglówka autoryzującego"),
			@ApiResponse(code = 404, message = "Użytkownik nie istnieje")}
	)
	public ResponseEntity<?> getAllBibliographies(HttpServletRequest req) {
		String username = jwtUtils.getUsername(req.getHeader("Authorization").substring(7));
		Collection<com.ziemniak.webserv.repositories.bibliographies.Bibliography> all = null;
		try {
			all = bibliographiesRepository.getAll(username);
			return ResponseEntity.status(HttpStatus.OK).body(all);
		} catch (UsernameNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Użytkownik nie istnieje");
		}

	}

	@PostMapping("/bibliography/add")
	@ApiOperation(value = "Tworzy nową bibliografię")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Udało się dodać bibliografię"),
			@ApiResponse(code = 404, message = "Nie ma takiego uytkownika"),
			@ApiResponse(code = 403, message = "Brak naglówka autoryzującego")
	})
	public ResponseEntity<?> createBibliography(@RequestBody BibliographyCreationRequestDTO creationReq, HttpServletRequest req) {
		String username = jwtUtils.getUsername(req.getHeader("Authorization").substring(7));
		try {
			bibliographiesRepository.create(username, creationReq.getName());
			return ResponseEntity.ok("Dodano");
		} catch (UsernameNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Użytkownik nie istnieje");
		}
	}

	@DeleteMapping("/bibliography/{id}")
	@ApiOperation(value = "Usuwa bibliografię o podanym id")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Udało się usunąć bibliografię"),
			@ApiResponse(code = 401, message = "Użytkownik nie ma uprawnień do bibliografii"),
			@ApiResponse(code = 403, message = "Brak naglówka autoryzującego"),
			@ApiResponse(code = 404, message = "Bibliografia nie istnieje")
	})
	public ResponseEntity deleteBibliography(@PathVariable int id, HttpServletRequest req) {
		String username = jwtUtils.getUsername(req.getHeader("Authorization").substring(7));
		if (!bibliographiesRepository.hasAccess(id, username)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		try {
			bibliographiesRepository.delete(id);
			return ResponseEntity.ok().build();
		} catch (BibliographyDoesNotExistsException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

}
