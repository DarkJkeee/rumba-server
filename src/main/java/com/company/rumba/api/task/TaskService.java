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

    public void addTask(Task task, Long eventId) {
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

    public void changeTask(Task newTask, Long id) {
        if (!taskRepository.existsById(id)) {
            throw new CustomErrorException(
                    HttpStatus.NOT_FOUND,
                    ErrorType.TASK_NOT_FOUND,
                    "Task doesn't exist"
            );
        }

        newTask.setTaskId(id);
        taskRepository.save(newTask);
    }
}
