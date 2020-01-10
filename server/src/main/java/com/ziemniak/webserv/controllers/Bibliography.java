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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

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
			@ApiResponse(code = 401, message = "Brak nagłówka autoryzacyjnego albo nie posiadasz uprawnień do tej bibliografii")
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
	public ResponseEntity<Collection<com.ziemniak.webserv.repositories.bibliographies.Bibliography>> getAllBibliographies(HttpServletRequest req) {
		String username = jwtUtils.getUsername(req.getHeader("Authorization").substring(7));
		return ResponseEntity.status(HttpStatus.OK).body(bibliographiesRepository.getAll(username));
	}

	@PostMapping("/bibliography/add")
	public void createBibliography(@RequestBody BibliographyCreationRequestDTO creationReq, HttpServletRequest req) {
		String username = jwtUtils.getUsername(req.getHeader("Authorization").substring(7));
		bibliographiesRepository.create(username, creationReq.getName());
	}

	@DeleteMapping("/bibliography/{id}")
	@ApiOperation(value = "Usuwa bibliografię o podanym id")
	@ApiResponses({
			@ApiResponse(code = 200,message = "Udało się usunąć bibliografię"),
			@ApiResponse(code = 404, message = "Bibliografia nie istnieje"),
			@ApiResponse(code = 401,
					message = "W żądaniu nie było poprawnego nagłówka autoryzującego zawierającego " +
							"jwt albo użytkownik nie ma uprawnień do bibliografii")
	})
	public ResponseEntity deleteBibliography(@PathVariable int id, HttpServletRequest req){
		String username = jwtUtils.getUsername(req.getHeader("Authorization").substring(7));
		if(!bibliographiesRepository.hasAccess(id,username)){
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
