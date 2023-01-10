package daviddorohd.dk.techexam2023q6q7.repository;

import daviddorohd.dk.techexam2023q6q7.entity.TestSession;
import daviddorohd.dk.techexam2023q6q7.entity.TestUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestSessionRepository extends JpaRepository<TestSession, Integer> {

    Optional<TestSession> findBySessionId(String sessionId);
}
