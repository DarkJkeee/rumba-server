package com.company.rumba.api.member;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

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

    @PostMapping("/try/assign")
    public ResponseEntity<?> tryAssignMember(@RequestParam("task_id") Long taskId, @Valid @RequestBody Member member) {
        return ResponseEntity.ok().body(memberService.tryAssignMember(member, taskId));
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
