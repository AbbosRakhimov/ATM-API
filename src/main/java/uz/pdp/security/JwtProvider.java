package uz.pdp.security;

import java.util.Date;
import java.util.Set;



import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import uz.pdp.entity.Role;

@Component
public class JwtProvider {

	private static final long expireTime=1000*60*60*24;
	private static final String secretKey= "This is a secret key that no one knows";
			Date expireDate= new Date(System.currentTimeMillis()+expireTime);
	public String generateToken(String username, Set<Role> roles) {
		
		String token = Jwts
				.builder()
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(expireDate)
				.claim("roles", roles)
				.signWith(SignatureAlgorithm.HS512,secretKey)
				.compact();
		return token;
	}
	
	public String getIbanFromToken(String token) {
		try {
			String iban = Jwts.
					parser()
					.setSigningKey(secretKey)
					.parseClaimsJws(token)
					.getBody()
					.getSubject();
			return iban;
		} catch (Exception e) {
			return null;
		}
	}
}
