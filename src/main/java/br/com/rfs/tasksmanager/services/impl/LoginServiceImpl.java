package br.com.rfs.tasksmanager.services.impl;

import br.com.rfs.tasksmanager.domain.dto.request.LoginRequest;
import br.com.rfs.tasksmanager.domain.dto.response.LoginResponse;
import br.com.rfs.tasksmanager.repositories.UserRepository;
import br.com.rfs.tasksmanager.services.LoginService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Long EXPIRATION_TIME = 3600L;
    private static final String ISSUER = "task-manager.com";

    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("Processando login do usuario: {}", request.username());
        var user = userRepository.findByUsername(request.username());

        if (user.isEmpty() || !user.get().isLoginCorret(request, passwordEncoder)) {
            throw new BadCredentialsException("user or password is invalid");
        }

        var now = Instant.now();
        var scopes = user.get().getRoles()
                .stream()
                .map(r -> r.getName().toUpperCase())
                .collect(Collectors.joining(" "));
        var claims = JwtClaimsSet.builder()
                .issuer(ISSUER)
                .subject(user.get().getId().toString())
                .expiresAt(now.plusSeconds(EXPIRATION_TIME))
                .issuedAt(now)
                .claim("scope", scopes)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new LoginResponse(jwtValue, now.plusSeconds(EXPIRATION_TIME));
    }
}
