package br.com.rfs.tasksmanager.domain.dto.response;

import java.util.List;

public record TodoListResponse(List<TodoResponse> todoItens,
                               int page,
                               int pageSize,
                               int totalPages,
                               long totalElements) {
}
