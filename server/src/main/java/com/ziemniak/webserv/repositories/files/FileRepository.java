package com.ziemniak.webserv.repositories.files;

import com.ziemniak.webserv.repositories.users.UserDoesNotExistException;
import com.ziemniak.webserv.repositories.users.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class FileRepository {
	private final Logger log = LoggerFactory.getLogger(FileRepository.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private UserRepository userRepository;

	public boolean hasAccess(int fileId, String username) {
		if (isOwner(fileId, username)) {
			return true;
		}
		return jdbcTemplate.queryForObject(
				"SELECT EXISTS(SELECT FROM shared_files WHERE file_id = ? " +
						"AND shared_to = (SELECT id FROM users WHERE username = ?))", Boolean.class, fileId, username);
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
		if (owner.equals(user)) {
			return;//Już ma uprawnienia
		}
		//todo
	}

	public void saveFile(String owner, MultipartFile file) {
		String sql = "\n" +
				"INSERT INTO files (owner_id, name, file) VALUES " +
				"((SELECT id FROM users WHERE username = ?), ?, ?)";
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql);
				ps.setString(1, owner);
				ps.setString(2, file.getName());
				try {
					ps.setBinaryStream(3, file.getInputStream());
				} catch (IOException e) {
					throw new SQLException("Creation of stream failed: " + e.getMessage());
				}
				return ps;
			}
		};
		jdbcTemplate.update(creator);
		log.info(owner + " added new file " + file.getName());
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
		if (username == null) {
			return false;
		}
		String sql = "SELECT ((SELECT owner_id FROM files WHERE id = ?) = (SELECT id FROM users WHERE username = ?))";
		return jdbcTemplate.queryForObject(sql, new Object[]{fileId,username}, Boolean.class);
	}

	public byte[] getFile(int id) throws FileDoesNotExistException {
		if (!exists(id)) {
			throw new FileDoesNotExistException(id);
		}
		return jdbcTemplate.queryForObject("SELECT file FROM files WHERE id = ?", (rs, rw) -> rs.getBytes("file"), id);
	}

	public boolean exists(int fileId) {
		return jdbcTemplate.queryForObject("SELECT EXISTS(SELECT id FROM files WHERE id = ?)",
				(rs, rw) -> rs.getBoolean(1), fileId);
	}

	public List<FileInfo> getAllOwnedFilesInfo(String username) throws UserDoesNotExistException {
		boolean userExists = jdbcTemplate.queryForObject("SELECT EXISTS(SELCT id FROM users WHERE username = ?)", new Object[]{username}, Boolean.class);
		if (!userExists) {
			throw new UserDoesNotExistException();
		}
		return jdbcTemplate.query("SELECT id, name, creation_date " +
						"FROM files " +
						"WHERE owner_id = (SELECT id FROM users WHERE username = ?)", new Object[]{username},
				(resultSet, i) -> {
					FileInfo file = new FileInfo();
					file.setId(resultSet.getInt("id"));
					file.setName(resultSet.getString("name"));
					file.setCreationDate(resultSet.getTimestamp("creation_date"));
					return file;
				});
	}

	public List<FileInfo> getAllSharedFilesInfo(String username){
		int id = userRepository.getUserId(username);
		String sql= "SELECT f.name as \"name\" f.id as \"id\" " +
				" FROM shared_files sf " +
				" JOIN files f on f.id = sf.file_id " +
				" WHERE owner = ?";
		return jdbcTemplate.query(sql, new Object[]{id},
				(resultSet, i) -> new FileInfo(resultSet.getInt("id"),resultSet.getString("name")));
	}
}
