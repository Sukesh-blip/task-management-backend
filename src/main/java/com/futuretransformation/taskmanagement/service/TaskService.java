package com.futuretransformation.taskmanagement.service;

import com.futuretransformation.taskmanagement.dto.TaskRequest;
import com.futuretransformation.taskmanagement.dto.TaskResponse;
import com.futuretransformation.taskmanagement.exception.ResourceNotFoundException;
import com.futuretransformation.taskmanagement.model.Task;
import com.futuretransformation.taskmanagement.model.User;
import com.futuretransformation.taskmanagement.repository.TaskRepository;
import com.futuretransformation.taskmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    // Create Task
    public TaskResponse createTask(TaskRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = Task.builder()
                .user(user)
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .status(request.getStatus())
                .dueDate(request.getDueDate())
                .build();

        return mapToResponse(taskRepository.save(task));
    }

    // Get All Tasks for User
    public List<TaskResponse> getAllTasks(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return taskRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Get Task by ID
    public TaskResponse getTaskById(Long id, String email) {
        Task task = getTaskForUser(id, email);
        return mapToResponse(task);
    }

    // Update Task
    public TaskResponse updateTask(Long id, TaskRequest request, String email) {
        Task task = getTaskForUser(id, email);

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setStatus(request.getStatus());
        task.setDueDate(request.getDueDate());

        return mapToResponse(taskRepository.save(task));
    }

    // Delete Task
    public String deleteTask(Long id, String email) {
        Task task = getTaskForUser(id, email);
        taskRepository.delete(task);
        return "Task deleted successfully";
    }

    // Helper — verify task belongs to user
    private Task getTaskForUser(Long id, String email) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        if (!task.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized access to task");
        }

        return task;
    }

    // Helper — map Task to TaskResponse
    private TaskResponse mapToResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setPriority(task.getPriority());
        response.setStatus(task.getStatus());
        response.setDueDate(task.getDueDate());
        response.setCreatedAt(task.getCreatedAt());
        return response;
    }
}