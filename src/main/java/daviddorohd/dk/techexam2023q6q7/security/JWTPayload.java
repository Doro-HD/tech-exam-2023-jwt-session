package daviddorohd.dk.techexam2023q6q7.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;

public class JWTPayload {

    private final HashMap<String, String> payload;

    public JWTPayload(HashMap<String, String> payload) {
        this.payload = payload;
    }

    public String getClaim(String claim) {

        return this.payload.get(claim);
    }
}

