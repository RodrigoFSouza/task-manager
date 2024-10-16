package br.com.rfs.tasksmanager.services;

import br.com.rfs.tasksmanager.domain.dto.request.LoginRequest;
import br.com.rfs.tasksmanager.domain.entity.User;
import br.com.rfs.tasksmanager.repositories.UserRepository;
import br.com.rfs.tasksmanager.services.impl.LoginServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class LoginServiceTest {

    @Mock
    private JwtEncoder jwtEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginServiceImpl loginService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_ShouldThrowBadCredentialsException_WhenUserNotFound() {
        LoginRequest loginRequest = new LoginRequest("user", "password");

        when(userRepository.findByUsername(loginRequest.username())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loginService.login(loginRequest))
            .isInstanceOf(BadCredentialsException.class)
            .hasMessage("user or password is invalid");
    }

    @Test
    void login_ShouldThrowBadCredentialsException_WhenPasswordIsIncorrect() {
        LoginRequest loginRequest = new LoginRequest("user", "password");
        User user = mock(User.class);

        when(userRepository.findByUsername(loginRequest.username())).thenReturn(Optional.of(user));
        when(user.isLoginCorret(any(LoginRequest.class), any(PasswordEncoder.class))).thenReturn(false);

        assertThatThrownBy(() -> loginService.login(loginRequest))
            .isInstanceOf(BadCredentialsException.class)
            .hasMessage("user or password is invalid");
    }
}