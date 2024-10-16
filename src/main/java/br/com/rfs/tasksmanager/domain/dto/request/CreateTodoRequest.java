package br.com.rfs.tasksmanager.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record CreateTodoRequest(@NotBlank(message = "Informe o título da tarefa")
                                @Schema(description = "Title of task", example = "New Task") String title,
                                @NotBlank(message = "Informe a descrição da tarefa")
                                @Schema(description = "Description of task") String description) {
}
