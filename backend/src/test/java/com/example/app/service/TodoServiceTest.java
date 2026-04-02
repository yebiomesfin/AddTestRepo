package com.example.app.service;

import com.example.app.model.Todo;
import com.example.app.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    private Todo todo;

    @BeforeEach
    void setUp() {
        todo = new Todo("Write tests", false);
        todo.setId(1L);
    }

    @Test
    void findAll_returnsList() {
        when(todoRepository.findAll()).thenReturn(List.of(todo));
        List<Todo> result = todoService.findAll();
        assertThat(result).hasSize(1).contains(todo);
    }

    @Test
    void findById_found() {
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
        Todo result = todoService.findById(1L);
        assertThat(result.getTitle()).isEqualTo("Write tests");
    }

    @Test
    void findById_notFound_throws() {
        when(todoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> todoService.findById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    void create_savesTodo() {
        when(todoRepository.save(todo)).thenReturn(todo);
        Todo saved = todoService.create(todo);
        assertThat(saved).isEqualTo(todo);
        verify(todoRepository).save(todo);
    }

    @Test
    void delete_callsRepositoryDelete() {
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
        todoService.delete(1L);
        verify(todoRepository).delete(todo);
    }
}
