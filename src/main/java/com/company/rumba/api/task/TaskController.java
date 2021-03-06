package com.company.rumba.api.task;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("api/tasks")
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addTask(@Valid @RequestBody Task task, @RequestParam("event_id") Long eventId) {
        taskService.addTask(task, eventId);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeTask(@Valid @RequestBody Task task, @PathVariable Long id) {
        taskService.changeTask(task, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id) { taskService.deleteTask(id); }

    @GetMapping
    public ResponseEntity<?> getMyTask(@RequestParam("event_id") Long eventId) {
        return ResponseEntity.ok(taskService.getMyTask(eventId));
    }
}
