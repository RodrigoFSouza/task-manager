package br.com.rfs.tasksmanager.services.impl;

import br.com.rfs.tasksmanager.domain.constant.TodoStatus;
import br.com.rfs.tasksmanager.domain.dto.request.CreateTodoRequest;
import br.com.rfs.tasksmanager.domain.dto.request.UpdateTodoRequest;
import br.com.rfs.tasksmanager.domain.dto.response.TodoListResponse;
import br.com.rfs.tasksmanager.domain.dto.response.TodoResponse;
import br.com.rfs.tasksmanager.domain.entity.Todo;
import br.com.rfs.tasksmanager.repositories.TodoRepository;
import br.com.rfs.tasksmanager.services.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;

    @Override
    public TodoListResponse find(int page, int size, String status) {
        var pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        var todos = todoRepository.findByStatus(EnumUtils.getEnum(TodoStatus.class, status), pageRequest)
                .map(todo ->
                        new TodoResponse(
                                todo.getId(),
                                todo.getTitle(),
                                todo.getDescription(),
                                todo.getStatus().toString(),
                                todo.getCreatedAt(),
                                todo.getUpdatedAt())
                );
        return new TodoListResponse(todos.getContent(), page, size, todos.getTotalPages(), todos.getTotalElements());
    }

    @Override
    public TodoResponse add(CreateTodoRequest request) {
        Todo todo = new Todo();
        todo.setTitle(request.title());
        todo.setDescription(request.description());
        todo.setStatus(TodoStatus.NEW);
        Todo todoFromDb = todoRepository.save(todo);
        return new TodoResponse(todoFromDb.getId(), todoFromDb.getTitle(), todoFromDb.getDescription(),
                todoFromDb.getStatus().toString(), todoFromDb.getCreatedAt(), todoFromDb.getUpdatedAt());
    }

    @Override
    public void update(Long id, UpdateTodoRequest request) {
        var todoFromDb = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo não encontrado"));

        todoFromDb.setTitle(request.title());
        todoFromDb.setDescription(request.description());
        todoFromDb.setStatus(EnumUtils.getEnum(TodoStatus.class, request.status()));

        todoRepository.save(todoFromDb);
    }

    @Override
    public void delete(Long id) {
        var todoFromDb = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo não encontrado"));

        todoRepository.delete(todoFromDb);
    }
}
