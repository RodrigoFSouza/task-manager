package br.com.rfs.tasksmanager.services;

import br.com.rfs.tasksmanager.domain.constant.TodoStatus;
import br.com.rfs.tasksmanager.domain.dto.request.CreateTodoRequest;
import br.com.rfs.tasksmanager.domain.dto.request.UpdateTodoRequest;
import br.com.rfs.tasksmanager.domain.dto.response.TodoListResponse;
import br.com.rfs.tasksmanager.domain.dto.response.TodoResponse;
import br.com.rfs.tasksmanager.domain.entity.Todo;
import br.com.rfs.tasksmanager.repositories.TodoRepository;
import br.com.rfs.tasksmanager.services.impl.TodoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TodoServiceTest {

    @InjectMocks
    private TodoServiceImpl todoService;

    @Mock
    private TodoRepository todoRepository;

    private Todo todo;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Test Todo");
        todo.setDescription("This is a test");
        todo.setStatus(TodoStatus.NEW);  // Supondo que você tenha um Enum TodoStatus
        todo.setCreatedAt(LocalDateTime.now());
        todo.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    public void testFind() {
        Page<Todo> todosPage = new PageImpl<>(Arrays.asList(todo));
        when(todoRepository.findByStatus(any(), any(Pageable.class))).thenReturn(todosPage);

        TodoListResponse response = todoService.find(0, 10, "NEW");

        assertFalse(response.todoItens().isEmpty());
        assertEquals(1, response.todoItens().size());
        assertEquals(todo.getId(), response.todoItens().get(0).getId());
        verify(todoRepository).findByStatus(any(), any(Pageable.class));
    }

    @Test
    public void testAdd() {
        CreateTodoRequest request = new CreateTodoRequest("Test Todo", "This is a test");
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        TodoResponse response = todoService.add(request);

        assertNotNull(response);
        assertEquals("Test Todo", response.getTitle());
        assertEquals("This is a test", response.getDescription());
        verify(todoRepository).save(any(Todo.class));
    }

    @Test
    public void testUpdate() {
        UpdateTodoRequest request = new UpdateTodoRequest("Updated Title", "Updated Description", "IN_PROGRESS");
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        todoService.update(1L, request);

        assertEquals("Updated Title", todo.getTitle());
        assertEquals("Updated Description", todo.getDescription());
        assertEquals(TodoStatus.IN_PROGRESS, todo.getStatus());  // verificando se o status foi alterado
        verify(todoRepository).findById(1L);
        verify(todoRepository).save(todo);
    }

    @Test
    public void testDelete() {
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        todoService.delete(1L);

        verify(todoRepository).delete(todo);
    }

    @Test
    public void testDeleteTodoNotFound() {
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            todoService.delete(1L);
        });

        assertEquals("Todo não encontrado", exception.getMessage());
    }
}