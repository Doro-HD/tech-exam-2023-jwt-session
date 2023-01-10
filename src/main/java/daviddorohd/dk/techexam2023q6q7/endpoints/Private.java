package daviddorohd.dk.techexam2023q6q7.endpoints;

import daviddorohd.dk.techexam2023q6q7.security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/private")
@RequiredArgsConstructor
public class Private {

    private final AuthService authService;

    @GetMapping("/jwt")
    public HashMap<String, String> hello(@RequestHeader("Authorization") String bearerToken) {
        var jwtPayload = this.authService.authorize(bearerToken);

        return new HashMap<>(Map.of("username", jwtPayload.getClaim("username")));
    }

    @GetMapping("/session")
    public String helloSession(@CookieValue("session") String sessionId) {
        var testUser = this.authService.authorizeSession(sessionId);

        return "Hello user: " + testUser.getTestUsername();
    }
}
