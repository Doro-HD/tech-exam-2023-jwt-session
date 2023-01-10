package daviddorohd.dk.techexam2023q6q7.repository;

import daviddorohd.dk.techexam2023q6q7.entity.TestUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface TestUserRepository extends JpaRepository<TestUser, Integer> {

    Optional<TestUser> findByTestUsername(String username);
    @Transactional
    void deleteByTestUsername(String username);
}
