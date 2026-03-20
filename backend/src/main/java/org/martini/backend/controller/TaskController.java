package org.martini.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.martini.backend.model.dao.Role;
import org.martini.backend.model.dto.CreateTaskDto;
import org.martini.backend.model.dto.TaskDto;
import org.martini.backend.model.dto.UpdateTaskDto;
import org.martini.backend.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public Page<TaskDto> getAllTasks(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "5") int size,
                                     Authentication authentication) {
        return taskService.findAll(PageRequest.of(page, size), authentication);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('" + Role.Constants.ADMIN + "') or @taskService.isOwner(#id, authentication.name)")
    public TaskDto getTaskById(@PathVariable Long id) {
        return taskService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTask(@RequestBody @Valid CreateTaskDto taskDto, Authentication authentication) {
        taskService.create(taskDto, authentication);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('" + Role.Constants.ADMIN + "') or @taskService.isOwner(#id, authentication.name)")
    public void updateTask(@PathVariable Long id, @RequestBody @Valid UpdateTaskDto taskDetails,
                           Authentication authentication) {
        taskService.update(id, taskDetails, authentication);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('" + Role.Constants.ADMIN + "') or @taskService.isOwner(#id, authentication.name)")
    public void deleteTask(@PathVariable Long id) {
        taskService.delete(id);
    }
}
