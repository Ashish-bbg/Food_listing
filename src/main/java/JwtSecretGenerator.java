import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

public class JwtSecretGenerator {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		String secret = Encoders.BASE64URL.encode(
				Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded());
		
		System.out.println(secret);
	}
	
}
