package br.exitus.api.repository;

import br.exitus.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    UserDetails findDetailsByEmail(String email);

    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
