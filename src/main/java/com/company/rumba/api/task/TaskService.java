package com.company.rumba.api.task;

import com.company.rumba.api.event.EventRepository;
import com.company.rumba.errors.CustomErrorException;
import lombok.AllArgsConstructor;
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
                .orElseThrow(() -> CustomErrorException.eventNotExistError);
    }

    public void changeTask(Task newTask, Long id) {
        if (!taskRepository.existsById(id)) {
            throw CustomErrorException.taskNotExistError;
        }

        newTask.setTaskId(id);
        taskRepository.save(newTask);
    }
}
