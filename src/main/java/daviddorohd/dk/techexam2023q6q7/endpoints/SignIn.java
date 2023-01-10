package daviddorohd.dk.techexam2023q6q7.endpoints;

import daviddorohd.dk.techexam2023q6q7.dto.TestUserRequest;
import daviddorohd.dk.techexam2023q6q7.security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/sign-in")
@RequiredArgsConstructor
public class SignIn {

    private final AuthService authService;

    @PostMapping("/jwt")
    public HashMap<String, String> signIn(@RequestBody TestUserRequest testUserRequest) {
        var jwtToken = this.authService.signIn(testUserRequest.asEntity());

        return new HashMap<>(Map.of("jwt", jwtToken));
    }

    @PostMapping("/session")
    public ResponseEntity<Object> signInSession(@RequestBody TestUserRequest testUserRequest) {
        var sessionId = this.authService.signInSession(testUserRequest.asEntity());

        var sessionCookie = ResponseCookie.from("session", sessionId)
                .path("/")
                .maxAge(60  * 60 * 6)
                .httpOnly(true)
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, sessionCookie.toString())
                .build();
    }
}
