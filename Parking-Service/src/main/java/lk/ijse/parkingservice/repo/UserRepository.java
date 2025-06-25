package lk.ijse.parkingservice.repo;

import lk.ijse.parkingservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByEmail(String email);

    boolean existsUserByEmail(String email);
}
