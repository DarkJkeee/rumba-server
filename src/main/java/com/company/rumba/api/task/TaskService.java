package com.company.rumba.api.task;

import com.company.rumba.api.event.EventRepository;
import com.company.rumba.errors.CustomErrorException;
import com.company.rumba.errors.ErrorType;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final EventRepository eventRepository;

    public void createTask(Task task, Long eventId) {
        eventRepository
                .findById(eventId)
                .map(event -> {
                    event.getTasks().add(task);
                    return eventRepository.save(event);
                })
                .orElseThrow(() -> new CustomErrorException(
                        HttpStatus.NOT_FOUND,
                        ErrorType.EVENT_NOT_FOUND,
                        "Event doesn't exist"
                ));
    }

    public Task changeTask(Task newTask, Long id) {
        return taskRepository
                .findById(id)
                .map(task -> {
                    if (newTask.getTitle() != null) task.setTitle(newTask.getTitle());
                    if (newTask.getDescription() != null) task.setDescription(newTask.getDescription());
                    if (newTask.getStartDate() != null) task.setStartDate(newTask.getStartDate());
                    if (newTask.getEndDate() != null) task.setEndDate(newTask.getEndDate());
                    return taskRepository.save(task);
                })
                .orElseThrow(() -> new CustomErrorException(
                        HttpStatus.NOT_FOUND,
                        ErrorType.TASK_NOT_FOUND,
                        "Task doesn't exist"
                ));
    }
}
