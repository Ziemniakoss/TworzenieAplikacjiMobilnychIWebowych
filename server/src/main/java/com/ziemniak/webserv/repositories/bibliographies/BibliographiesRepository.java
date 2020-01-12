package com.ziemniak.webserv.repositories.bibliographies;

import com.ziemniak.webserv.repositories.files.FileDoesNotExistException;
import com.ziemniak.webserv.repositories.files.FileInfo;
import com.ziemniak.webserv.repositories.files.FileRepository;
import com.ziemniak.webserv.repositories.files.PermissionDeniedException;
import com.ziemniak.webserv.repositories.users.UserRepository;
import com.ziemniak.webserv.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public class BibliographiesRepository {
	private Logger log = LoggerFactory.getLogger(BibliographiesRepository.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FileRepository fileRepository;
	@Autowired
	private JwtUtils jwtUtils;

	private final RowMapper<Bibliography> mapper =
			(rs, rn) -> new Bibliography(rs.getInt("id"), rs.getString("name"));


	public Bibliography get(int id) throws BibliographyDoesNotExistsException {
		try {
			return jdbcTemplate.queryForObject(
					"SELECT id, name FROM bibliographies WHERE id = ?", new Object[]{id}, mapper);
		} catch (IncorrectResultSizeDataAccessException e) {
			throw new BibliographyDoesNotExistsException(id);
		}
	}

	public void delete(int id) throws BibliographyDoesNotExistsException {
		if (exists(id)) {
			jdbcTemplate.update("DELETE FROM bibliographies WHERE id = ?", id);
		} else {
			throw new BibliographyDoesNotExistsException(id);
		}
	}

	/**
	 * Tworzy nową bibliografię dla użytkownika
	 *
	 * @param username         Użytkownik dla którego należy stworzyć bibiografię
	 * @param bibliographyName nazwa nowej bibliografii
	 * @throws org.springframework.security.core.userdetails.UsernameNotFoundException użytkownik nie istnieje
	 */
	public void create(String username, String bibliographyName) {
		int id = userRepository.getUserId(username);
		jdbcTemplate.update(
				"INSERT INTO bibliographies (owner, name) VALUES (?,?)",
				id, bibliographyName);
	}

	public void addFileToBibliography(int bibliographyId, int fileId, String username)
			throws BibliographyDoesNotExistsException, FileDoesNotExistException, PermissionDeniedException {
		if (!fileRepository.exists(fileId)) {
			throw new FileDoesNotExistException(fileId);
		}

		if (!fileRepository.isOwner(fileId, username)) {
			throw new PermissionDeniedException();
		}
		if (!exists(bibliographyId)) {
			throw new BibliographyDoesNotExistsException(bibliographyId);
		}
		try {
			jdbcTemplate.update(
					"INSERT INTO bibliography_files (bibliography_id, file_id) VALUES (?,?);",
					bibliographyId, fileId);
		} catch (DataAccessException d) {
			//todo lepsze łapanie
			//prawdopodobnie już jest taki rekord
			log.error(String.join("", "Error(",
					d.getClass().toString(), ") occured while adding file ",
					String.valueOf(fileId),
					" to bibliography ",
					String.valueOf(bibliographyId)),
					d.getMessage());
			d.printStackTrace();
		}
	}

	/**
	 * Usuwa plik z bibliografii
	 *
	 * @param bibliographyId id bibliografii
	 * @param fileId         id pliku
	 * @return czy udało się usunąć
	 * @throws FileDoesNotExistException          podany plik nie istnieje
	 * @throws BibliographyDoesNotExistsException podana bibliografia nie istnieje
	 */
	public boolean deleteFileFromBibliography(int bibliographyId, int fileId) throws FileDoesNotExistException, BibliographyDoesNotExistsException {
		if (!fileRepository.exists(fileId)) {
			throw new FileDoesNotExistException(fileId);
		}
		if (!exists(bibliographyId)) {
			throw new BibliographyDoesNotExistsException(bibliographyId);
		}
		int i = jdbcTemplate.update(
				"DELETE FROM bibliography_files WHERE file_id =? AND bibliography_id = ?;",
				fileId, bibliographyId);
		if (i != 0) {
			log.info(String.format("Deleted file %d from bibliography %d", fileId, bibliographyId));
		}
		return i != 0;
	}


	/**
	 * @param username
	 * @return
	 * @throws org.springframework.security.core.userdetails.UsernameNotFoundException
	 */
	public Collection<Bibliography> getAll(String username) {
		int id = userRepository.getUserId(username);
		return jdbcTemplate.query(
				"SELECT id, name FROM bibliographies WHERE owner = ?", new Object[]{userRepository.getUserId(username)}, mapper);
	}

	public boolean hasAccess(int bibliographyId, String username) {
		return jdbcTemplate.queryForObject(
				"SELECT EXISTS(SELECT  id FROM bibliographies WHERE id = ? AND owner = ?);",
				Boolean.class, bibliographyId, userRepository.getUserId(username));
	}

	public boolean exists(int bibliographyId) {
		return jdbcTemplate.queryForObject(
				"SELECT EXISTS(SELECT id FROM bibliographies WHERE id = ?);",
				Boolean.class, bibliographyId);
	}

	public Collection<FileInfo> getAllFiles(int bibliographyId){
		String sql = "SELECT id,name,creation_date " +
				" FROM bibliography_files " +
				" JOIN files f ON bibliography_files.file_id = f.id " +
				" WHERE bibliography_id = ?;";
		return jdbcTemplate.query(sql,new Object[]{bibliographyId},
				(rs, rn)-> new FileInfo(rs.getInt("id"),rs.getString("name")));
	}
}
