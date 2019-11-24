package com.ziemniak.webserv;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.ziemniak.webserv.repositories.BlacklistedJwtRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenManager {
	private final Logger log = LoggerFactory.getLogger(TokenManager.class);
	String secret = "hadghhghalgsalahahf";

	@Autowired
	private BlacklistedJwtRepository blackList;

	/**
	 * Sprawdza czy JWT jest aktualny i czy nie ma go na czarnej liście
	 *
	 * @param jwt JWT
	 * @return {@code true} jeżeli JWT jest poprawny
	 */
	public boolean verify(String jwt) {
		boolean verified = false;
		try {
			Algorithm alg = Algorithm.HMAC256(secret);
			JWTVerifier ver = JWT.require(alg)
					.withIssuer("Ziemniak")
					.build();
			ver.verify(JWT.decode(jwt));
			verified = true;
		} catch (TokenExpiredException tee) {
			log.warn("Expired token used(" + jwt + "");
		} catch (SignatureVerificationException sve) {
			log.error("Token with invalid signature used(" + jwt + ")");
		} catch (AlgorithmMismatchException ame) {
			log.error("Token with wrong algorithm used on jwt(" + jwt + ")");
		} catch (JWTDecodeException jde) {
			log.error("Non-parsable token(" + jwt + ")");
		}
		log.info("Token verified");
		return verified;
	}

	/**
	 * Generuje JWT
	 *
	 * @param username       nazwa użytkownika dla którego należy wygenerować JWT
	 * @param expirationDate data ważności JWT
	 * @return String reprezentujący JWT albo pustego Stringa jeżeli nastąpił błąd przy tworzniu
	 */
	public String create(String username, Date expirationDate) {
		if (username == null) {
			throw new NullPointerException("username is null");
		}
		if (expirationDate == null) {
			throw new NullPointerException("expirationDate is null");
		}
		try {
			Algorithm alg = Algorithm.HMAC256(secret);
			return JWT.create()
					.withClaim("u", username)
					.withExpiresAt(expirationDate)
					.withIssuer("Ziemniak")
					.sign(alg);
		} catch (JWTCreationException jce) {
			log.error("Error while creating JWT: " + jce.getMessage());
		}
		return "";
	}

	public boolean isBlacklisted(String jwt){
		return blackList.isBlacklisted(jwt);
	}

	public void addToBlacklist(String jwt){
		blackList.add(jwt);
	}

}
