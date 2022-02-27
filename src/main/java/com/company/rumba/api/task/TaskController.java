package com.company.rumba.api.task;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/tasks")
public class TaskController {
    private final TaskService taskService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createTask(@RequestBody Task task, @RequestParam("event_id") Long event_id) {
        taskService.createTask(task, event_id);
    }
}
