package daviddorohd.dk.techexam2023q6q7.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

@Getter
@Setter
//Modificeret fra obligatorisk projekt af Coderbois. Projektnavn fra obligatorisk opgave er Adventure-Party.
public class JWTHandler {

    private static final String secretEnvName = "JWT_SECRET";
    private static final String issuerEnvName = "JWT_ISSUER";

    private String accessToken;
    private String username;

    public void sign(String username, TestUserType testUserType) {

        //Set expiration date
        LocalDateTime localDateTime = LocalDateTime.now().plusHours(2);
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date expirationDate = Date.from(instant);

        //Create jwt token
        try {
            Algorithm algorithm = Algorithm.HMAC256(System.getenv(secretEnvName));
            this.accessToken = JWT.create()
                    .withIssuer(System.getenv(issuerEnvName))
                    .withClaim("username", username)
                    .withClaim("type", testUserType.toString())
                    .withExpiresAt(expirationDate)
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "jwt configuration error");
        }
    }

    public JWTPayload decode(String jwt) {

        HashMap<String, String> hashMap = new HashMap<>();

        try {
            Algorithm algorithm = Algorithm.HMAC256(System.getenv(secretEnvName));
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(System.getenv(issuerEnvName))
                    .build(); //Reusable verifier instance
            //Will throw error here if not valid
            DecodedJWT decodedJWT = verifier.verify(jwt);

            Claim usernameClaim = decodedJWT.getClaim("username");
            Claim typeClaim = decodedJWT.getClaim("type");
            this.username = usernameClaim.asString();

            hashMap.put("username", usernameClaim.asString());
            hashMap.put("type", typeClaim.asString());

        } catch (SignatureVerificationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "JWT verification error");
        }

        return new JWTPayload(hashMap);
    }
}

