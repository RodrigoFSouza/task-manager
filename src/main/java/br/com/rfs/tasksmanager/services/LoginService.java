package br.com.rfs.tasksmanager.services;

import br.com.rfs.tasksmanager.domain.dto.request.LoginRequest;
import br.com.rfs.tasksmanager.domain.dto.response.LoginResponse;

public interface LoginService {
    LoginResponse login(LoginRequest request);
}
