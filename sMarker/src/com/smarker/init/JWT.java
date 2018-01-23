package com.smarker.init;

import java.security.Key;
import java.util.Map;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

public class JWT {

	// static Key key;

	public static void generateKey() {
		//MacProvide.generateKey() is deactivated because otherwise we would need to login in the app everytime the server restarts,
		//but on a live app this would be the best approach
		// key = MacProvider.generateKey();
	}

	// public Key getKey() {
	// return key;
	// }

	public static String key = "chaveexemplo";

	public static String getKey() {
		return key;
	}

	//Generates the jwt that is sent to the user
	public static String GenerateJWT(Map claim) {

		String compactJws = Jwts.builder().setClaims(claim).setIssuer("sMarker").signWith(SignatureAlgorithm.HS512, key)
				.compact();

		return compactJws;

	}

	//Reverts the token to users informations
	public static String ReverseToken(String token, String field) {
		String data = (String) Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().get(field);
		return data;
	}

}
