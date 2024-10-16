package br.com.rfs.tasksmanager.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

public record CreateTodoRequest(@NotEmpty @Schema(description = "Title of task", example = "New Task") String title,
                                @NotEmpty @Schema(description = "Description of task") String description) {
}
