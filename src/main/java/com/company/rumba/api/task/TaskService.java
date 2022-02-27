package com.company.rumba.api.task;

import com.company.rumba.api.event.EventRepository;
import com.company.rumba.errors.CustomErrorException;
import com.company.rumba.errors.ErrorType;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final EventRepository eventRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

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
}
