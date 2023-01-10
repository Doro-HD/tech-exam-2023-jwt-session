package daviddorohd.dk.techexam2023q6q7.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TestSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "test_user_id")
    @OneToOne(targetEntity = TestUser.class, fetch = FetchType.LAZY)
    private TestUser testUser;
    @Column(name = "log_date")
    private LocalDate logDate;
    @Column(name = "session_id")
    private String sessionId;
}
