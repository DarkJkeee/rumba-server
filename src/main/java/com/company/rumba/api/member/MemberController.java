package com.company.rumba.api.member;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("api/members")
public class MemberController {
    private final MemberService memberService;

    @GetMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addMember(@RequestParam("event_id") Long eventId) {
        memberService.addMember(eventId);
    }

    @PostMapping("/assign")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void assignMember(@Valid @RequestBody Member member, @RequestParam("task_id") Long taskId) {
        memberService.assignMember(member, taskId);
    }

    @DeleteMapping("/unassign")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unassignMember(@RequestParam("task_id") Long taskId) {
        memberService.unassignMember(taskId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMember(@RequestParam("event_id") Long eventId) {
        memberService.deleteMember(eventId);
    }
}
