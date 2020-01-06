package com.ziemniak.webserv.utils;

import com.ziemniak.webserv.repositories.WeakPasswordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PasswordUtils {
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private WeakPasswordsRepository weakPasswordsRepository;
	private String specialCharacters = "~`!@#$%^&*()_+{}[];':\"\\|<>,.?/";

	public String encode(String plain) {
		if (plain == null) {
			plain = "";
		}
		return encoder.encode(plain);
	}

	/**
	 * Sprawdza czy podane hasło jest w rejestrze najpopularniejszych
	 * haseł
	 *
	 * @param password hasło do sprawdzenia
	 * @return true jeżeli jest w rejestrze najpopularniejszych haseł
	 * @see WeakPasswordsRepository
	 */
	public boolean isWeak(String password) {
		return weakPasswordsRepository.isRegistered(password);
	}

	/**
	 * Oblicza entropię ciągu znaków hasła na podstawie wzoru Shannona.
	 *
	 * @param password Hasło dla którego należy obliczyć entropię
	 * @return ilość bitów entropii
	 */
	private static double calculateEntropy(String password) {
		if (password == null) {
			return 0;
		}
		Map<Character, Integer> occurances = new HashMap<>();

		for (char c : password.toCharArray()) {
			if (occurances.containsKey(c)) {
				occurances.put(c, occurances.get(c) + 1);
			} else {
				occurances.put(c, 1);
			}
		}
		double e = 0;
		for (int x : occurances.values()) {
			double prob = 1.0 * x / password.length();
			e += prob * Math.log(prob) / Math.log(2);
		}
		return -e * password.length();
	}

	/**
	 * Waliduje czy hasło może być użyte. Poprawne hasło:
	 * <ul>
	 *     <li>ma przynajmniej 8 znaków</li>
	 *     <li>posiada przynajmniej jedną wielką litere</li>
	 *     <li>posiada przynajmniej jendą małą literę</li>
	 *     <li>posiada przynajmniej jedną cyfrę</li>
	 *     <li>posiada przynajmniej jeden znak specjalny</li>
	 *     <li>nie jest w rejestrze haseł słabych</li>
	 *     <li>ma etropię większą niż 15</li>
	 * </ul>
	 * @param password hasło do walidacji. Jeżeli jest to null, hasło będzie zamienione na
	 *                 pusty ciąg znaków
	 * @throws PasswordValidationException jeżeli hasło jest nieprawidłowe
	 */
	public void getValidationErrors(String password) throws PasswordValidationException {
		if(password == null){
			password = "";
		}
		List<String> errors = new ArrayList<>();
		if(password.length() < 8){
			errors.add("Hasło jest za krótkie");
		}
		boolean containsUpperCase = false;
		boolean containsLowerCase = false;
		boolean containsNumber = false;
		boolean containsSpecial = false;
		for(char c : password.toCharArray()){
			if(Character.isDigit(c)){
				containsNumber = true;
			}
			if(Character.isUpperCase(c)){
				containsUpperCase = true;
			}
			if(Character.isLowerCase(c)){
				containsLowerCase = true;
			}
			if(specialCharacters.indexOf(c) != -1){
				containsSpecial = true;
			}
		}
		if(!containsLowerCase){
			errors.add("Hasło nie zawiera małej litery");
		}
		if(!containsUpperCase){
			errors.add("Hasło nie zawiera wielkiej litery");
		}
		if(!containsNumber){
			errors.add("Hasło nie zawiera liczby");
		}
		if(!containsSpecial){
			errors.add("Hasło nie zawiera zanku specjalnego");
		}
		if(isWeak(password)){
			errors.add("Hasło jest w rejestrze haseł słabych");
		}
		double entropy = calculateEntropy(password);
		if(entropy < 15){
			errors.add("Hasło ma zdecydowanie za małą entropię");
		}

		if(!errors.isEmpty()){
			throw new PasswordValidationException(entropy,errors);
		}
	}
}
