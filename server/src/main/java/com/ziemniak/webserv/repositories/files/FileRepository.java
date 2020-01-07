package com.ziemniak.webserv.repositories.files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
public class FileRepository {
	private final Logger log = LoggerFactory.getLogger(FileRepository.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public boolean hasAccess(int fileId, String username) {

	}

	public void revokeAccess(int fileId, String owner, String user) throws PermissionDeniedException {
		if (!isOwner(fileId, owner)) {
			throw new PermissionDeniedException();
		}
		// TODO: 07.01.2020

	}

	public void grantAccess(int fileId, String owner, String user) throws PermissionDeniedException {
		if (!isOwner(fileId, owner)) {
			throw new PermissionDeniedException();
		}
		//todo
	}

	public int saveFile(String owner, MultipartFile file) {

	}

	public void deleteFile(int id, String owner) throws PermissionDeniedException {
		if (!isOwner(id, owner)) {
			throw new PermissionDeniedException();
		}
		String sql = "DELETE FROM files WHERE id = ?;";
		jdbcTemplate.update(sql, id);
		log.info("Deleted file " + id);
	}

	public boolean isOwner(int fileId, String username) {
		if(username == null){
			return false;
		}
		//todo sprawdzic
		String sql = "SELECT username FROM files f" +
				" JOIN users u on u.id = fileId.owner " +
				" WHERE f.id = ?;";
		String owner= jdbcTemplate.queryForObject(sql, new Object[]{username}, (rs, rw) -> rs.getString("username"));
		return username.equals(owner);
	}
}
