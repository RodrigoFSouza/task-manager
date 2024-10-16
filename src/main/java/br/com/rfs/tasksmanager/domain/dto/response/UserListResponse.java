package br.com.rfs.tasksmanager.domain.dto.response;

import java.util.List;

public record UserListResponse(List<UserResponse> users,
                               int page,
                               int size,
                               int totalPages,
                               long totalElements) {
}
