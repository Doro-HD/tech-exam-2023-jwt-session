package daviddorohd.dk.techexam2023q6q7.entity;

import daviddorohd.dk.techexam2023q6q7.security.TestUserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String testUsername;
    private String testUserPassword;
    @Enumerated(EnumType.STRING)
    private TestUserType testUserType;
}
