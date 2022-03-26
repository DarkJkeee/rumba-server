package com.company.rumba.api.member;

import com.company.rumba.api.event.EventRepository;
import com.company.rumba.api.task.TaskRepository;
import com.company.rumba.errors.CustomErrorException;
import com.company.rumba.errors.ErrorType;
import com.company.rumba.utils.UserProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MemberService {
    private final TaskRepository taskRepository;
    private final MemberRepository memberRepository;
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
                    return eventRepository.save(event);
                })
                .orElseThrow(() -> CustomErrorException.eventNotExistError);
    }

    public void assignMember(Member member, Long taskId) {
        taskRepository
                .findById(taskId)
                .map(task -> {
                    var eventNotExist = eventRepository
                            .findEventByTask(task)
                            .getMembers()
                            .stream()
                            .noneMatch(user -> user.getAccountId().equals(userProvider.getCurrentUserID()));
                    var memberAlreadyAssigned = memberRepository
                            .findByTaskAndMember(task, userProvider.getCurrentAppUser())
                            .isPresent();
                    if (eventNotExist) {
                        throw new CustomErrorException(
                                HttpStatus.BAD_REQUEST,
                                ErrorType.MEMBER_NOT_FOUND,
                                "The user isn't a member of the event"
                        );
                    }
                    if (memberAlreadyAssigned) {
                        throw new CustomErrorException(
                                HttpStatus.BAD_REQUEST,
                                ErrorType.MEMBER_ALREADY_ASSIGNED,
                                "The user has already assigned to the task"
                        );
                    }

                    member.setTask(task);
                    member.setMember(userProvider.getCurrentAppUser());
                    return memberRepository.save(member);
                })
                .orElseThrow(() -> CustomErrorException.taskNotExistError);
    }

    public void unassignMember(Long taskId) {
        var task = taskRepository
                .findById(taskId)
                .orElseThrow(() -> CustomErrorException.taskNotExistError);
        memberRepository
                .findByTaskAndMember(task, userProvider.getCurrentAppUser())
                .ifPresentOrElse(memberRepository::delete, () -> {
                    throw new CustomErrorException(
                            HttpStatus.BAD_REQUEST,
                            ErrorType.MEMBER_ALREADY_UNASSIGNED,
                            "The user has already unassigned"
                    );
                });
    }

    public void deleteMember(Long eventId) {
        eventRepository
                .findById(eventId)
                .map(event -> {
                    if (event.getMembers().removeIf(user -> user.getAccountId().equals(userProvider.getCurrentUserID()))) {
                        event
                                .getTasks()
                                .forEach(task -> memberRepository
                                        .findByTaskAndMember(task, userProvider.getCurrentAppUser())
                                        .ifPresent(memberRepository::delete));
                        return eventRepository.save(event);
                    } else {
                        throw CustomErrorException.memberNotExistError;
                    }
                })
                .orElseThrow(() -> CustomErrorException.eventNotExistError);
    }
}
