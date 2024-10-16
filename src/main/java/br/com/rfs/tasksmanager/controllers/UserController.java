package br.com.rfs.tasksmanager.controllers;

import br.com.rfs.tasksmanager.domain.dto.request.CreateUserRequest;
import br.com.rfs.tasksmanager.domain.dto.response.TodoListResponse;
import br.com.rfs.tasksmanager.domain.dto.response.UserListResponse;
import br.com.rfs.tasksmanager.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
@Tag(name = "Users API")
public class UserController {

    private final UserService userService;

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New user created with success"),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "resource not found",
                    content = @Content) })
    @Operation(summary = "Create new User API")
    public ResponseEntity<Void> newUser(@RequestBody CreateUserRequest request) {
        userService.newUser(request);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Results are ok", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TodoListResponse.class))
            }),
            @ApiResponse(responseCode = "401", description = "No authentication"),
            @ApiResponse(responseCode = "403", description = "Without authorization"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Resource not found")
    })
    @Parameters(value = {
            @Parameter(name = "page", in = ParameterIn.QUERY),
            @Parameter(name = "size", in = ParameterIn.QUERY)
    })
    @Operation(summary = "List all users with pagination API")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<UserListResponse> listUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                                      @RequestParam(value = "size", defaultValue = "10") int size) {
        var userListReponse = userService.findAll(page, size);
        return ResponseEntity.ok(userListReponse);
    }
}
