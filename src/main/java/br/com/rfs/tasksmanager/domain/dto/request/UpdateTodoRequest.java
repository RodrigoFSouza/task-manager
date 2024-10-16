package br.com.rfs.tasksmanager.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateTodoRequest(@Schema(description = "Title of task", example = "New Task") String title,
                                @Schema(description = "Description of task") String description,
                                @Schema(description = "Status of task") String status) {
}
