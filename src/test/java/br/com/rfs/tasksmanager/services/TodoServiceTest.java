package br.com.rfs.tasksmanager.services;

import br.com.rfs.tasksmanager.domain.constant.TodoStatus;
import br.com.rfs.tasksmanager.domain.dto.request.CreateTodoRequest;
import br.com.rfs.tasksmanager.domain.dto.request.UpdateTodoRequest;
import br.com.rfs.tasksmanager.domain.dto.response.TodoListResponse;
import br.com.rfs.tasksmanager.domain.dto.response.TodoResponse;
import br.com.rfs.tasksmanager.domain.entity.Role;
import br.com.rfs.tasksmanager.domain.entity.Todo;
import br.com.rfs.tasksmanager.domain.entity.User;
import br.com.rfs.tasksmanager.repositories.TodoRepository;
import br.com.rfs.tasksmanager.repositories.UserRepository;
import br.com.rfs.tasksmanager.services.impl.TodoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TodoServiceTest {

    @InjectMocks
    private TodoServiceImpl todoService;

    @Mock
    private TodoRepository todoRepository;
    @Mock
    private UserRepository userRepository;

    private Todo todo;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Test Todo");
        todo.setDescription("This is a test");
        todo.setStatus(TodoStatus.NEW);
        todo.setUser(User.builder().id(1L).username("teste").roles(Set.of(new Role(Role.Values.ADMIN.name()))).build());
        todo.setCreatedAt(LocalDateTime.now());
        todo.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Test find all")
    public void testFind() {
        Page<Todo> todosPage = new PageImpl<>(Arrays.asList(todo));
        when(todoRepository.findByStatus(any(), any(Pageable.class))).thenReturn(todosPage);

        TodoListResponse response = todoService.find(0, 10, "NEW");

        assertFalse(response.todoItens().isEmpty());
        assertEquals(1, response.todoItens().size());
        assertEquals(todo.getId(), response.todoItens().get(0).id());
        verify(todoRepository).findByStatus(any(), any(Pageable.class));
    }

    @Test
    @DisplayName("Test add todo")
    public void testAdd() {
        CreateTodoRequest request = new CreateTodoRequest("Test Todo", "This is a test");
        Long userId = 1L;
        User userFromDb = User.builder().id(userId).username("teste").roles(Set.of(new Role(Role.Values.ADMIN.name()))).build();
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userFromDb));

        TodoResponse response = todoService.add(request, userId);

        assertNotNull(response);
        assertEquals("Test Todo", response.title());
        assertEquals("This is a test", response.description());
        verify(todoRepository).save(any(Todo.class));
    }

    @Test
    @DisplayName("Test update todo")
    public void testUpdate() {
        UpdateTodoRequest request = new UpdateTodoRequest("Updated Title", "Updated Description", "IN_PROGRESS");
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        todoService.update(1L, request);

        assertEquals("Updated Title", todo.getTitle());
        assertEquals("Updated Description", todo.getDescription());
        assertEquals(TodoStatus.IN_PROGRESS, todo.getStatus());
        verify(todoRepository).findById(1L);
        verify(todoRepository).save(todo);
    }

    @Test
    @DisplayName("Test delete todo")
    public void testDelete() {
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        todoService.delete(1L);

        verify(todoRepository).delete(todo);
    }

    @Test
    @DisplayName("Test delete todo not found")
    public void testDeleteTodoNotFound() {
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            todoService.delete(1L);
        });

        assertEquals("Todo não encontrado", exception.getMessage());
    }
}