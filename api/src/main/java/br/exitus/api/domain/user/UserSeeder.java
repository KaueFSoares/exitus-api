package br.exitus.api.domain.user;

import br.exitus.api.domain.role.RoleEnum;
import br.exitus.api.repository.RoleRepository;
import br.exitus.api.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@DependsOn("roleSeeder")
public class UserSeeder {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserSeeder(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void seed() {
        if (userRepository.count() == 0) {

            for (var role : RoleEnum.values()) {
                userRepository.save(createUser(role));
            }

            System.out.println("-----UserSeeder: users created-----");

        } else {
            System.out.println("-----UserSeeder: users already created-----");
        }
    }

    private User createUser(RoleEnum role) {
        var userRole = roleRepository.findByName(role);

        var type = role.name().toLowerCase();

        var user = new User();

        user.setEmail(type + "@" + type + ".com");
        user.setPassword(passwordEncoder.encode(type));
        user.setName(type);
        user.getRoles().add(userRole);

        return user;
    }


}
