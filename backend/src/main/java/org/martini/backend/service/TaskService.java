package org.martini.backend.service;

import lombok.RequiredArgsConstructor;
import org.martini.backend.exception.ResourceNotFoundException;
import org.martini.backend.model.dao.Role;
import org.martini.backend.model.dao.Task;
import org.martini.backend.model.dao.User;
import org.martini.backend.model.dto.CreateTaskDto;
import org.martini.backend.model.dto.TaskDto;
import org.martini.backend.model.dto.UpdateTaskDto;
import org.martini.backend.repository.TaskRepository;
import org.martini.backend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public Page<TaskDto> findAll(PageRequest pageRequest, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .filter(grantedAuthority -> grantedAuthority.getAuthority() != null)
                .anyMatch(a -> a.getAuthority().equals(Role.ROLE_ADMIN.name()));
        if (isAdmin) {
            return taskRepository.findAll(pageRequest)
                    .map(this::convertToDto);
        } else {
            return taskRepository.findAllByUserUsername(authentication.getName(), pageRequest)
                    .map(this::convertToDto);
        }
    }

    public TaskDto findById(Long id) {
        return taskRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id));
    }

    @Transactional
    public void create(CreateTaskDto taskDto, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("no such user exists"));

        Task task = Task.builder()
                .title(taskDto.title())
                .description(taskDto.description())
                .done(false)
                .user(user)
                .build();

        taskRepository.save(task);
    }

    @Transactional
    public void update(Long id, UpdateTaskDto taskDetails, Authentication authentication) {

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("no such user exists"));

        Task updatedTask = taskRepository.findById(id)
                .map(task -> {
                    task.setTitle(taskDetails.title());
                    task.setDescription(taskDetails.description());
                    task.setDone(taskDetails.done());
                    task.setUser(user);
                    return task;
                }).orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id));

        taskRepository.save(updatedTask);
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    public boolean isOwner(Long taskId, String username) {
        return taskRepository.findById(taskId)
                .map(task -> task.getUser().getUsername().equals(username))
                .orElse(false);
    }

    private TaskDto convertToDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .done(task.isDone())
                .userId(task.getUser() != null ? task.getUser().getId() : null)
                .build();
    }
}
