package com.patientservice.util;
import com.patientservice.exception.JWTNotValidException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

@Service
public class JwtUtil {

	public static String secret = "Hacktivators";
	
	// copy it in the MicroServices
	public void verify(String authorization)
	{
		try
		{
		Jwts.parser().setSigningKey(secret).parseClaimsJws(authorization);
		}
		catch (Exception e) {
			System.out.println("exception occurrred");
			e.printStackTrace();
			throw new JWTNotValidException();
		}
	}
}