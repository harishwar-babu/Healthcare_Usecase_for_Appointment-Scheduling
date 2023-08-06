package com.appointmentservice.util;
import com.appointmentservice.exception.JWTNotValidException;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
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