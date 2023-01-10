package daviddorohd.dk.techexam2023q6q7.dto;

import daviddorohd.dk.techexam2023q6q7.entity.TestUser;
import daviddorohd.dk.techexam2023q6q7.security.TestUserType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestUserRequest {

    private String username;
    private String password;
    private TestUserType testUserType;

    public TestUser asEntity() {
        return TestUser.builder()
                .testUsername(this.username)
                .testUserPassword(this.password)
                .testUserType(this.testUserType)
                .build();
    }
}
