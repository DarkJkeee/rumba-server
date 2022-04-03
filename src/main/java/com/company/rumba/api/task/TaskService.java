package com.company.rumba.api.task;

import com.company.rumba.api.event.EventRepository;
import com.company.rumba.errors.CustomErrorException;
import com.company.rumba.utils.UserProvider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskService {
    private final UserProvider userProvider;
    private final TaskRepository taskRepository;
    private final EventRepository eventRepository;

    public void addTask(Task task, Long eventId) {
        eventRepository
                .findById(eventId)
                .map(event -> {
                    if (!event.getCreator().getAccountId().equals(userProvider.getCurrentUserID())) {
                        throw CustomErrorException.forbiddenError("User is not a creator of the event");
                    }

                    event.getTasks().add(task);
                    return eventRepository.save(event);
                })
                .orElseThrow(() -> CustomErrorException.eventNotExistError);
    }

    public void changeTask(Task newTask, Long id) {
        taskRepository
                .findById(id)
                .map(task -> {
                    if (!eventRepository.findEventByTask(task).getCreator().getAccountId().equals(userProvider.getCurrentUserID())) {
                        throw CustomErrorException.forbiddenError("User is not a creator of the event");
                    }

                    newTask.setTaskId(id);
                    return taskRepository.save(newTask);
                })
                .orElseThrow(() -> CustomErrorException.taskNotExistError);
    }

    public void deleteTask(Long id) {
        taskRepository
                .findById(id)
                .map(task -> {
                    var event = eventRepository.findEventByTask(task);
                    if (!event.getCreator().getAccountId().equals(userProvider.getCurrentUserID())) {
                        throw CustomErrorException.forbiddenError("User is not a creator of the event");
                    }

                    event
                            .getTasks()
                            .removeIf(task1 -> task1.getTaskId().equals(id));
                    return eventRepository.save(event);
                })
                .orElseThrow(() -> CustomErrorException.taskNotExistError);
    }

    public List<Task> getMyTasks(Long id) {
        return eventRepository
                .findById(id)
                .map(event -> event
                        .getTasks()
                        .stream()
                        .filter(task -> task
                                .getMembers()
                                .stream()
                                .anyMatch(member -> member.getMember().getAccountId().equals(userProvider.getCurrentUserID()))
                        )
                        .collect(Collectors.toList())
                )
                .orElseThrow(() -> CustomErrorException.eventNotExistError);
    }
}
