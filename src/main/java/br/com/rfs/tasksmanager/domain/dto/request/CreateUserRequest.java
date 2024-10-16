package br.com.rfs.tasksmanager.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

public record CreateUserRequest(@NotBlank(message = "Informe o nome do usuário")
                                @Schema(description = "Nome do usuário", example = "João")
                                String username,
                                @NotBlank(message = "Informe a senha do usuário")
                                @Schema(description = "Senha do usuário", example = "Teste@123")
                                String password) {
}
