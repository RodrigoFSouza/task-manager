package br.com.rfs.tasksmanager.controllers;

import br.com.rfs.tasksmanager.domain.dto.request.LoginRequest;
import br.com.rfs.tasksmanager.domain.dto.response.LoginResponse;
import br.com.rfs.tasksmanager.services.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/login")
@Tag(name = "Login API")
public class LoginController {
    private final LoginService loginService;

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login user with success"),
            @ApiResponse(responseCode = "401", description = "No authentication"),
            @ApiResponse(responseCode = "403", description = "Without authorization"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Resource not found")
    })
    @Operation(summary = "Login API")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        var loginResponse = loginService.login(loginRequest);

        return ResponseEntity.ok(loginResponse);
    }
}