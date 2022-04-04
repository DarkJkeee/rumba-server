package com.company.rumba.api.event;

import com.company.rumba.api.dto.ListEvent;
import com.company.rumba.api.task.Task;
import com.company.rumba.errors.CustomErrorException;
import com.company.rumba.errors.ErrorType;
import com.company.rumba.utils.UserProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class EventService {
    private final ModelMapper modelMapper;
    private final EventRepository eventRepository;
    private final UserProvider userProvider;

    public void createEvent(Event event) {
        if (event.getTasks().stream().anyMatch(task -> task.getTaskId() != null)) {
            throw new CustomErrorException(
                    HttpStatus.BAD_REQUEST,
                    ErrorType.TASK_NOT_FOUND,
                    "Tasks cannot have ids"
            );
        }

        if (event.getStartDate().isAfter(event.getEndDate())) {
            throw CustomErrorException.invalidStartAndEndDates;
        }

        event.getTasks().forEach(task -> setUpTask(event, task));

        if (event.getIsOnline()) {
            event.setLatitude(null);
            event.setLongitude(null);
        }

        event.setCreatedAt(ZonedDateTime.now());
        event.setEditedAt(ZonedDateTime.now());
        event.setCreator(userProvider.getCurrentAppUser());
        eventRepository.save(event);
    }

    public void changeEvent(Event newEvent, Long id) {
        eventRepository
                .findById(id)
                .map(event -> {
                    if (!event.getCreator().getAccountId().equals(userProvider.getCurrentUserID())) {
                        throw CustomErrorException.forbiddenError("User is not a creator of the event");
                    }

                    if (event.getStartDate().isAfter(event.getEndDate())) {
                        throw CustomErrorException.invalidStartAndEndDates;
                    }

                    newEvent.setMembers(event.getMembers());
                    newEvent.setTasks(event.getTasks());
                    newEvent.setCreator(userProvider.getCurrentAppUser());
                    newEvent.setEventId(id);
                    newEvent.setEditedAt(ZonedDateTime.now());
                    return eventRepository.save(newEvent);
                })
                .orElseThrow(() -> CustomErrorException.eventNotExistError);
    }

    public List<ListEvent> getCreatedEvents() {
        return eventRepository
                .findAllCreatedBy(userProvider.getCurrentUserID())
                .stream()
                .map(event -> {
                    var listEvent = modelMapper.map(event, ListEvent.class);
                    listEvent.setIsActionsRequired(event.getTasks().isEmpty());
                    return listEvent;
                })
                .collect(Collectors.toList());
    }

    public List<ListEvent> getParticipatedEvents() {
        return eventRepository
                .findAllParticipatedBy(userProvider.getCurrentAppUser())
                .stream()
                .map(event -> {
                    var listEvent = modelMapper.map(event, ListEvent.class);
                    listEvent.setIsActionsRequired(event
                            .getTasks()
                            .stream()
                            .noneMatch(task -> task
                                    .getMembers()
                                    .stream()
                                    .anyMatch(member -> member.getMember().getAccountId().equals(userProvider.getCurrentUserID()))
                            )
                    );
                    return listEvent;
                })
                .collect(Collectors.toList());
    }

    public Event getEvent(Long id) {
        return eventRepository
                .findById(id)
                .orElseThrow(() -> CustomErrorException.eventNotExistError);
    }

    public void setUpTask(Event event, @Valid Task task) {
        if (task.getStartDate().isBefore(event.getStartDate()) || task.getEndDate().isAfter(event.getEndDate())) {
            throw CustomErrorException.invalidDatesOfTask;
        }

        if (task.getStartDate().isAfter(task.getEndDate())) {
            throw CustomErrorException.invalidStartAndEndDates;
        }

        task.setCreatedAt(ZonedDateTime.now());
        task.setEditedAt(ZonedDateTime.now());
    }
}
