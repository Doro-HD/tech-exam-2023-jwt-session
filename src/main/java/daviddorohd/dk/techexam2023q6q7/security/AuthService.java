package daviddorohd.dk.techexam2023q6q7.security;



import daviddorohd.dk.techexam2023q6q7.entity.TestSession;
import daviddorohd.dk.techexam2023q6q7.entity.TestUser;
import daviddorohd.dk.techexam2023q6q7.repository.TestSessionRepository;
import daviddorohd.dk.techexam2023q6q7.repository.TestUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TestUserRepository testUserRepository;
    private final TestSessionRepository testSessionRepository;

    public String signIn(TestUser testUserSignIn) {
        String token;

        Optional<TestUser> testUserOptional = this.testUserRepository.findByTestUsername(testUserSignIn.getTestUsername());
        if (testUserOptional.isPresent()) {
            var testUser = testUserOptional.get();

            if (BCrypt.checkpw(testUserSignIn.getTestUserPassword(), testUser.getTestUserPassword())) {
                var jwtHandler = new JWTHandler();
                jwtHandler.sign(testUser.getTestUsername(), testUser.getTestUserType());

                token = jwtHandler.getAccessToken();
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return token;
    }

    public String signInSession(TestUser testUserSignIn) {
        Optional<TestUser> testUserOptional = this.testUserRepository.findByTestUsername(testUserSignIn.getTestUsername());
        if (testUserOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user");
        }

        var testUser = testUserOptional.get();
        if (!BCrypt.checkpw(testUserSignIn.getTestUserPassword(), testUser.getTestUserPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect password");
        }

        var testSession = TestSession.builder()
                .testUser(testUser)
                .sessionId(UUID.randomUUID().toString())
                .logDate(LocalDate.now())
                .build();
        this.testSessionRepository.save(testSession);

        return testSession.getSessionId();
    }

    public JWTPayload authorize(String bearerToken) {
        return this.authorize(bearerToken, TestUserType.USER);
    }

    public JWTPayload authorize(String bearerToken, TestUserType requiredTestUserType) {
        bearerToken = bearerToken.replace("Bearer ", "");

        JWTHandler jwtHandler = new JWTHandler();

        var payload = jwtHandler.decode(bearerToken);

        var userTyper = payload.getClaim("type");
        if (TestUserType.valueOf(userTyper).ordinal() > requiredTestUserType.ordinal()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unauthorized operation");
        }

        return payload;
    }

    public TestUser authorizeSession(String sessionId) {
        return authorizeSession(sessionId, TestUserType.USER);
    }

    public TestUser authorizeSession(String sessionId, TestUserType requiredType) {
        var testSessionOptional = this.testSessionRepository.findBySessionId(sessionId);
        if (testSessionOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find session");
        }
        var testSession = testSessionOptional.get();

        var testUser = testSession.getTestUser();
        if (testUser.getTestUserType().ordinal() > requiredType.ordinal()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unauthorized operation");
        }

        return testUser;
    }
}

