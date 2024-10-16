package br.com.rfs.tasksmanager.services;

import br.com.rfs.tasksmanager.domain.dto.request.CreateTodoRequest;
import br.com.rfs.tasksmanager.domain.dto.request.UpdateTodoRequest;
import br.com.rfs.tasksmanager.domain.dto.response.TodoListResponse;
import br.com.rfs.tasksmanager.domain.dto.response.TodoResponse;

public interface TodoService {
    TodoListResponse find(int page, int size, String status);
    TodoResponse add(CreateTodoRequest request);
    void update(Long id, UpdateTodoRequest request);
    void delete(Long id);
}
