package daviddorohd.dk.techexam2023q6q7.config;

import daviddorohd.dk.techexam2023q6q7.entity.TestUser;
import daviddorohd.dk.techexam2023q6q7.repository.TestUserRepository;
import daviddorohd.dk.techexam2023q6q7.security.TestUserType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Setup implements CommandLineRunner {

    private final TestUserRepository testUserRepository;

    @Override
    public void run(String... args) throws Exception {
        var hash = BCrypt.hashpw("test", BCrypt.gensalt(12));

        var user1 = TestUser.builder()
                .testUsername("David")
                .testUserPassword(hash)
                .testUserType(TestUserType.ADMIN)
                .build();
        var user2 = TestUser.builder()
                .testUsername("Lasse")
                .testUserPassword(hash)
                .testUserType(TestUserType.USER)
                .build();

        this.testUserRepository.save(user1);
        this.testUserRepository.save(user2);
    }
}
