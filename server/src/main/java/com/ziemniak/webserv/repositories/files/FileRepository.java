package com.ziemniak.webserv.repositories.files;

import com.ziemniak.webserv.dto.FileSharedByUserDTO;
import com.ziemniak.webserv.dto.FileSharedForUserDTO;
import com.ziemniak.webserv.repositories.users.UserDoesNotExistException;
import com.ziemniak.webserv.repositories.users.UserRepository;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.TypeMismatchDataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FileRepository {
	private final Logger log = LoggerFactory.getLogger(FileRepository.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private UserRepository userRepository;

	public boolean hasAccess(int fileId, String username) {//todo zamienic na procedurę
		return jdbcTemplate.queryForObject(
				"SELECT EXISTS(SELECT file_id FROM shared_files WHERE file_id = ? " +
						"AND shared_to = (SELECT id FROM users WHERE username = ?))", Boolean.class, fileId, username);
	}

	public void revokeAccess(int fileId, String owner, String user) throws FileDoesNotExistException,UsernameNotFoundException {
		try {
			String sql = "SELECT revoke_access(?,?,?);";
			jdbcTemplate.queryForObject(sql, new Object[]{fileId, owner, user}, (rs, rn) -> null);
			log.info(String.format("%s revoked access to file %d for user %s", owner, fileId, user));
		} catch (UncategorizedSQLException e) {
			String message = e.getMostSpecificCause().getMessage();
			System.err.println(message);
			if (message.contains("File does not exists")) {
				throw new FileDoesNotExistException(fileId);
			} else if (message.contains("User does not exists")) {
				throw new UsernameNotFoundException("owner");
			} else if (message.contains("User to revoke does not exists")) {
				throw new UsernameNotFoundException("revoker");
			}
		} catch (TypeMismatchDataAccessException ignored) {
		}
	}

	public void grantAccess(int fileId, String owner, String user) throws PermissionDeniedException, FileDoesNotExistException {
		try {
			String sql = "Select grant_access(?,?,?)";
			jdbcTemplate.query(sql, new Object[]{fileId, owner, user}, (rs, rn) -> null);
		} catch (UncategorizedSQLException e) {
			String message = e.getMostSpecificCause().getMessage();
			if (message.contains("File does not exits")) {
				throw new FileDoesNotExistException(fileId);
			} else if (message.contains("User does not exists")) {
				throw new UsernameNotFoundException("owner");
			} else if (message.contains("User to revoke does not exists")) {
				throw new UsernameNotFoundException("revoker");
			} else if (message.contains("Not a owner")) {
				throw new PermissionDeniedException();
			}
			log.error(message);
		}
	}

	public void saveFile(String owner, MultipartFile file) {
		String sql = "\n" +
				"INSERT INTO files (owner, name, file) VALUES " +
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
		String sql = "SELECT EXISTS (SELECT id FROM files WHERE id = ? AND owner = (SELECT id FROM users WHERE username = ?))";
		return jdbcTemplate.queryForObject(sql, new Object[]{fileId, username}, Boolean.class);
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
		boolean userExists = jdbcTemplate.queryForObject("SELECT EXISTS(SELECT id FROM users WHERE username = ?)", new Object[]{username}, Boolean.class);
		if (!userExists) {
			throw new UserDoesNotExistException();
		}
		return jdbcTemplate.query("SELECT id, name, creation_date " +
						"FROM files " +
						"WHERE files.owner = (SELECT id FROM users WHERE username = ?)", new Object[]{username},
				(resultSet, i) -> {
					FileInfo file = new FileInfo();
					file.setId(resultSet.getInt("id"));
					file.setName(resultSet.getString("name"));
					file.setCreationDate(resultSet.getTimestamp("creation_date"));
					return file;
				});
	}

	public List<FileSharedByUserDTO> getAllUsersShardFiles(String username){
		try{
			return jdbcTemplate.query("SELECT * FROM get_all_shared_files(?);",new Object[]{username},(rs, rowNum) -> {
				FileSharedByUserDTO file = new FileSharedByUserDTO();
				file.setId(rs.getInt("id"));
				file.setName(rs.getString("name"));
				JSONArray jsonArray = new JSONArray(rs.getString("shared_to"));
				List<String> s = new ArrayList<>();
				for(int i = 0; i < jsonArray.length(); i++){
					s.add(jsonArray.getString(i));
				}
				file.setSharedTo(s);
				return file;
			});
		}catch (UncategorizedSQLException e){
			//Nie ma użytkonwnika
			throw new UsernameNotFoundException(username);
		}
	}

	public List<FileSharedForUserDTO> getAllFilesSharedForUser(String username){
		return jdbcTemplate.query("SELECT * FROM get_all_shared_to(?)",
				new Object[]{username}, (rs, rowNum) -> {
					FileSharedForUserDTO file = new FileSharedForUserDTO();
					file.setName(rs.getString("name"));
					file.setId(rs.getInt("id"));
					//todo data
					file.setOwner(rs.getString("owner"));
					return file;
				});
	}

}
