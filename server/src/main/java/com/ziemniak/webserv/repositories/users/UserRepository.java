package com.ziemniak.webserv.repositories.users;

import com.ziemniak.webserv.utils.PasswordUtils;
import com.ziemniak.webserv.utils.PasswordValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private PasswordUtils passwordUtils;

	public void add(String username, String passwordPlain) throws PasswordValidationException, UserAlreadyExistsException {
		if (userExists(username)) {
			throw new UserAlreadyExistsException();
		}
		passwordUtils.validate(passwordPlain);
		String hash = passwordEncoder.encode(passwordPlain);
		String sql = "INSERT INTO users (username, password_hash) VALUES (?,?);";
		jdbcTemplate.update(sql, username, hash);
	}

	public void changePassword(User u, String oldPassword, String newPassword, String validateNewPassword) throws PasswordValidationException {
		List<String> errors = new ArrayList<>();
		if(!passwordEncoder.matches(oldPassword,u.getPassword())){
			errors.add("Stare hasło jest niepoprawne");
		}
		if(!newPassword.equals(validateNewPassword)){
			errors.add("Hasła się nie zgadzają");
		}
		try{
			passwordUtils.validate(newPassword);
		}catch (PasswordValidationException e){
			errors.addAll(e.getErrors());
		}
		if(!errors.isEmpty()){
			throw new PasswordValidationException(errors);
		}
		jdbcTemplate.query("SELECT '' FROM change_password(?,?)",new Object[]{u.getUsername(),passwordEncoder.encode(newPassword)},(rs,rn)->null);
	}

	public User getUser(String username) throws UsernameNotFoundException {
		//todo refactor
		if (!userExists(username)) {
			throw new UsernameNotFoundException(username);
		}
		String sql = "select username, password_hash as password FROM users WHERE username = ?;";
		return jdbcTemplate.queryForObject(sql, new Object[]{username},
				(rs, rn) -> new User(rs.getString("username"), rs.getString("password"), new ArrayList<>()));
	}

	public boolean userExists(String username) {
		return jdbcTemplate.queryForObject("SELECT EXISTS(SELECT id FROM users WHERE username = ?)", Boolean.class, username);
	}

	/**
	 * znajduje id użytkownika w bazie danych
	 *
	 * @param username nazwa użytkownika
	 * @return id użytkownika
	 * @throws UsernameNotFoundException jak nie ma takiego użytkownika
	 */
	public int getUserId(String username) {
		Integer id = jdbcTemplate.queryForObject(
				"SELECT id FROM users WHERE username = ?;", Integer.class, username);
		if (id == null) {
			throw new UsernameNotFoundException(username);
		}
		return id;
	}

}
