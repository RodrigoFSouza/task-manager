package br.com.rfs.tasksmanager.services;

import br.com.rfs.tasksmanager.domain.dto.request.CreateUserRequest;
import br.com.rfs.tasksmanager.domain.dto.response.UserListResponse;

public interface UserService {
    void newUser(CreateUserRequest request);
    UserListResponse findAll(int page, int size);
}
