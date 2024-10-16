package br.com.rfs.tasksmanager.services.impl;

import br.com.rfs.tasksmanager.domain.dto.request.CreateUserRequest;
import br.com.rfs.tasksmanager.domain.dto.response.UserListResponse;
import br.com.rfs.tasksmanager.domain.dto.response.UserResponse;
import br.com.rfs.tasksmanager.domain.entity.Role;
import br.com.rfs.tasksmanager.domain.entity.User;
import br.com.rfs.tasksmanager.repositories.RoleRepository;
import br.com.rfs.tasksmanager.repositories.UserRepository;
import br.com.rfs.tasksmanager.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void newUser(CreateUserRequest request) {
        log.info("New user request: {}", request.username());
        var basicRole = roleRepository.findByName(Role.Values.BASIC.name());

        var userFromDb = userRepository.findByUsername(request.username());

        if (userFromDb.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        var user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRoles(Set.of(basicRole));

        userRepository.save(user);

    }

    @Override
    public UserListResponse findAll(int page, int size) {
        var pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        var users = userRepository.findAll(pageRequest)
                .map(user ->
                        new UserResponse(
                                user.getId(),
                                user.getUsername(),
                                user.getCreatedAt(),
                                user.getUpdatedAt())
                );
        return new UserListResponse(users.getContent(), page, size, users.getTotalPages(), users.getTotalElements());
    }


}
