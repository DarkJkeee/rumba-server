package com.company.rumba.api.member;

import com.company.rumba.api.event.EventRepository;
import com.company.rumba.api.task.Task;
import com.company.rumba.api.task.TaskRepository;
import com.company.rumba.errors.CustomErrorException;
import com.company.rumba.errors.ErrorType;
import com.company.rumba.utils.UserProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
@AllArgsConstructor
public class MemberService {
    private final String invalidDatesErrorMsg = "Start date must be less than end date";
    private final String invalidMemberDatesErrorMsg =
            "Start and end dates must be between start and end dates of the task";

    private final TaskRepository taskRepository;
    private final EventRepository eventRepository;
    private final UserProvider userProvider;

    public void addMember(Long eventId) {
        eventRepository
                .findById(eventId)
                .map(event -> {
                    if (event.getMembers().stream().anyMatch(user -> user.getAccountId().equals(userProvider.getCurrentUserID()))) {
                        throw new CustomErrorException(
                                HttpStatus.BAD_REQUEST,
                                ErrorType.MEMBER_ALREADY_EXIST,
                                "The user is already a member of the event"
                        );
                    }

                    event.getMembers().add(userProvider.getCurrentAppUser());
                    event.setEditedAt(ZonedDateTime.now());
                    return eventRepository.save(event);
                })
                .orElseThrow(() -> CustomErrorException.eventNotExistError);
    }

    public boolean tryAssignMember(Member member, Long taskId) {
        return taskRepository
                .findById(taskId)
                .map(task -> checkAvailabilityForMember(task, member.getStartDate(), member.getEndDate()))
                .orElseThrow(() -> CustomErrorException.taskNotExistError);
    }

    public void assignMember(Member member, Long taskId) {
        taskRepository
                .findById(taskId)
                .map(task -> {
                    var event = eventRepository.findEventByTask(task);
                    var eventMemberNotExist = event
                            .getMembers()
                            .stream()
                            .noneMatch(user -> user.getAccountId().equals(userProvider.getCurrentUserID()));

                    if (eventMemberNotExist) {
                        throw new CustomErrorException(
                                HttpStatus.BAD_REQUEST,
                                ErrorType.MEMBER_NOT_FOUND,
                                "The user isn't a member of the event"
                        );
                    }

                    if (member.getStartDate().isAfter(member.getEndDate())) {
                        throw CustomErrorException.invalidDatesError(invalidDatesErrorMsg);
                    }

                    if (member.getStartDate().isBefore(task.getStartDate())
                            || member.getEndDate().isAfter(task.getEndDate())) {
                        throw CustomErrorException.invalidDatesError(invalidMemberDatesErrorMsg);
                    }

                    if (!checkAvailabilityForMember(task, member.getStartDate(), member.getEndDate())) {
                        throw new CustomErrorException(
                                HttpStatus.BAD_REQUEST,
                                ErrorType.VALIDATION_ERROR,
                                "There are full of members on this interval"
                        );
                    }

                    event
                            .getTasks()
                            .forEach(t -> t
                                    .getMembers()
                                    .removeIf(m -> m
                                            .getMember()
                                            .getAccountId().equals(userProvider.getCurrentUserID())
                                    )
                            );

                    member.setMember(userProvider.getCurrentAppUser());
                    task.getMembers().add(member);
                    task.setEditedAt(ZonedDateTime.now());
                    return taskRepository.save(task);
                })
                .orElseThrow(() -> CustomErrorException.taskNotExistError);
    }

    public void unassignMember(Long taskId) {
        taskRepository
                .findById(taskId)
                .map(task -> {
                    var userNotMember = task
                            .getMembers()
                            .stream()
                            .noneMatch(member -> member.getMember().getAccountId().equals(userProvider.getCurrentUserID()));
                    if (userNotMember) {
                        throw CustomErrorException.memberNotExistError;
                    }

                    task
                            .getMembers()
                            .removeIf(member -> member.getMember().getAccountId().equals(userProvider.getCurrentUserID()));
                    task.setEditedAt(ZonedDateTime.now());
                    return taskRepository.save(task);
                })
                .orElseThrow(() -> CustomErrorException.taskNotExistError);
    }

    public void deleteMember(Long eventId) {
        eventRepository
                .findById(eventId)
                .map(event -> {

                    // TODO: Check if curr user is creator or member of event.
                    if (event.getMembers().removeIf(user -> user.getAccountId().equals(userProvider.getCurrentUserID()))) {
                        event
                                .getTasks()
                                .forEach(task -> task
                                        .getMembers()
                                        .removeIf(member -> member
                                                .getMember()
                                                .getAccountId().equals(userProvider.getCurrentUserID())
                                        )
                                );

                        event.setEditedAt(ZonedDateTime.now());
                        return eventRepository.save(event);
                    } else {
                        throw CustomErrorException.memberNotExistError;
                    }
                })
                .orElseThrow(() -> CustomErrorException.eventNotExistError);
    }

    private boolean checkAvailabilityForMember(Task task, ZonedDateTime startDate, ZonedDateTime endDate) {
        var membersOnInterval = task
                .getMembers()
                .stream()
                .filter(mem -> (!endDate.isAfter(mem.getEndDate())
                                && !endDate.isBefore(mem.getStartDate()))
                                || (!startDate.isAfter(mem.getEndDate())
                                && !startDate.isBefore(mem.getStartDate()))
                )
                .count();

        if (startDate.isAfter(endDate)) {
            return false;
        }

        if (startDate.isBefore(task.getStartDate())
                || endDate.isAfter(task.getEndDate())) {
            return false;
        }
        return membersOnInterval < task.getMembersCount();
    }
}
