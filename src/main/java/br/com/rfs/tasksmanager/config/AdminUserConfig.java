package br.com.rfs.tasksmanager.config;

import br.com.rfs.tasksmanager.domain.entity.Role;
import br.com.rfs.tasksmanager.domain.entity.User;
import br.com.rfs.tasksmanager.repositories.RoleRepository;
import br.com.rfs.tasksmanager.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AdminUserConfig  implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        var roleAdmin = roleRepository.findByName(Role.Values.ADMIN.name());

        var userAdmin = userRepository.findByUsername("admin");

        userAdmin.ifPresentOrElse(
                user -> {
                    log.info("Admin já está cadastrado!");
                },
                () -> {
                    var user = new User();
                    user.setUsername("admin");
                    user.setPassword(passwordEncoder.encode("123"));
                    user.setRoles(Set.of(roleAdmin));

                    userRepository.save(user);
                }
        );
    }
}
