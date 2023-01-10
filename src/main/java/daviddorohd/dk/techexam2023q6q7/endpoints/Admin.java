package daviddorohd.dk.techexam2023q6q7.endpoints;

import daviddorohd.dk.techexam2023q6q7.dto.TestUserRequest;
import daviddorohd.dk.techexam2023q6q7.repository.TestUserRepository;
import daviddorohd.dk.techexam2023q6q7.security.AuthService;
import daviddorohd.dk.techexam2023q6q7.security.TestUserType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/admin")
@RequiredArgsConstructor
public class Admin {

    private final AuthService authService;
    private final TestUserRepository testUserRepository;

    @GetMapping("/jwt")
    public HashMap<String, String> hello(@RequestHeader("Authorization") String authHeader) {
        var jwtPayload = this.authService.authorize(authHeader, TestUserType.ADMIN);

        return new HashMap<>(Map.of("username", jwtPayload.getClaim("username")));
    }

    @PostMapping("/jwt")
    public HashMap<String, String> create(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody TestUserRequest testUserRequest
    ) {
        var jwtPayload = this.authService.authorize(authHeader, TestUserType.ADMIN);

        testUserRequest.setPassword(BCrypt.hashpw(testUserRequest.getPassword(), BCrypt.gensalt(12)));

        this.testUserRepository.save(testUserRequest.asEntity());

        return new HashMap<>(Map.of("username", jwtPayload.getClaim("username")));
    }

    @DeleteMapping("/jwt/{username}")
    public HashMap<String, String> delete(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("username") String username
    ) {
        this.authService.authorize(authHeader, TestUserType.ADMIN);

        this.testUserRepository.deleteByTestUsername(username);

        return new HashMap<>(Map.of("message", "success"));
    }

    @GetMapping("/session")
    public String helloSession(@CookieValue("session") String sessionId) {
        var testUser = this.authService.authorizeSession(sessionId, TestUserType.ADMIN);

        return "Hello admin: " + testUser.getTestUsername();
    }
}
