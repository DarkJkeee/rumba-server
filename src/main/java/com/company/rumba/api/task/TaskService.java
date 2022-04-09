package com.company.rumba.api.task;

import com.company.rumba.api.event.EventRepository;
import com.company.rumba.api.event.EventService;
import com.company.rumba.errors.CustomErrorException;
import com.company.rumba.utils.UserProvider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
@AllArgsConstructor
public class TaskService {
    private final String invalidDatesErrorMsg = "Start date must be less than end date";
    private final String invalidTaskDatesErrorMsg =
            "Start and end dates of task must be between start and end dates of event";
    private final String userIsNotCreatorErrorMsg = "User is not a creator of the event";

    private final UserProvider userProvider;
    private final TaskRepository taskRepository;
    private final EventRepository eventRepository;

    private final EventService eventService;

    public void addTask(Task task, Long eventId) {
        eventRepository
                .findById(eventId)
                .map(event -> {
                    if (!event.getCreator().getAccountId().equals(userProvider.getCurrentUserID())) {
                        throw CustomErrorException.forbiddenError(userIsNotCreatorErrorMsg);
                    }

                    eventService.setUpTask(event, task);
                    event.getTasks().add(task);
                    return eventRepository.save(event);
                })
                .orElseThrow(() -> CustomErrorException.eventNotExistError);
    }

    public void changeTask(Task newTask, Long id) {
        taskRepository
                .findById(id)
                .map(task -> {
                    var event = eventRepository.findEventByTask(task);

                    if (!event.getCreator().getAccountId().equals(userProvider.getCurrentUserID())) {
                        throw CustomErrorException.forbiddenError(userIsNotCreatorErrorMsg);
                    }

                    if (!task.getStartDate().isAfter(event.getStartDate())
                            || !task.getEndDate().isBefore(event.getEndDate())) {
                        throw CustomErrorException.invalidDatesError(
                                invalidTaskDatesErrorMsg
                        );
                    }

                    if (task.getStartDate().isAfter(task.getEndDate())) {
                        throw CustomErrorException.invalidDatesError(invalidDatesErrorMsg);
                    }

                    newTask.setEditedAt(ZonedDateTime.now());
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
                        throw CustomErrorException.forbiddenError(userIsNotCreatorErrorMsg);
                    }

                    event
                            .getTasks()
                            .removeIf(task1 -> task1.getTaskId().equals(id));
                    return eventRepository.save(event);
                })
                .orElseThrow(() -> CustomErrorException.taskNotExistError);
    }

    public Task getMyTask(Long id) {
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
                        .findFirst()
                        .orElseThrow(() -> CustomErrorException.taskNotExistError)
                )
                .orElseThrow(() -> CustomErrorException.eventNotExistError);
    }
}
