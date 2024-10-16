package br.com.rfs.tasksmanager.services;

import br.com.rfs.tasksmanager.domain.dto.request.CreateUserRequest;
import br.com.rfs.tasksmanager.domain.dto.response.UserListResponse;
import br.com.rfs.tasksmanager.domain.entity.Role;
import br.com.rfs.tasksmanager.domain.entity.User;
import br.com.rfs.tasksmanager.repositories.RoleRepository;
import br.com.rfs.tasksmanager.repositories.UserRepository;
import br.com.rfs.tasksmanager.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void newUser_ShouldCreateUser_WhenUserDoesNotExist() {
        CreateUserRequest request = new CreateUserRequest("newUser", "password123");
        Role basicRole = new Role(Role.Values.BASIC.name());
        
        when(roleRepository.findByName(Role.Values.BASIC.name())).thenReturn(basicRole);
        when(userRepository.findByUsername(request.username())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");

        userService.newUser(request);

        verify(userRepository).save(argThat(user ->
            user.getUsername().equals(request.username()) &&
            user.getPassword().equals("encodedPassword") &&
            user.getRoles().contains(basicRole)
        ));
    }

    @Test
    void newUser_ShouldThrowResponseStatusException_WhenUserAlreadyExists() {
        CreateUserRequest request = new CreateUserRequest("existingUser", "password123");
        
        when(userRepository.findByUsername(request.username())).thenReturn(Optional.of(new User()));

        assertThatThrownBy(() -> userService.newUser(request))
            .isInstanceOf(ResponseStatusException.class)
            .hasFieldOrPropertyWithValue("status", HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    void findAll_ShouldReturnPaginatedUsers() {
        int page = 0;
        int size = 10;
        User user = new User();
        user.setId(1L);
        user.setUsername("user1");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        Page<User> userPage = new PageImpl<>(List.of(user), PageRequest.of(page, size), 1);

        when(userRepository.findAll(any(PageRequest.class))).thenReturn(userPage);

        UserListResponse response = userService.findAll(page, size);

        assertThat(response).isNotNull();
        assertThat(response.users()).hasSize(1);
        assertThat(response.users().get(0).username()).isEqualTo("user1");
        assertThat(response.page()).isEqualTo(page);
        assertThat(response.size()).isEqualTo(size);
        assertThat(response.totalPages()).isEqualTo(1);
        assertThat(response.totalElements()).isEqualTo(1);
    }
}