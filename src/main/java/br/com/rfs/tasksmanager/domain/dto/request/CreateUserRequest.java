package br.com.rfs.tasksmanager.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

public record CreateUserRequest(@NotEmpty
                                @Schema(description = "Nome do usuário", example = "João")
                                String username,
                                @NotEmpty
                                @Schema(description = "Senha do usuário", example = "Teste@123")
                                String password) {
}
