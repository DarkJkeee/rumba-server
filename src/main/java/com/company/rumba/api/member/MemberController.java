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

    @PostMapping("/assign/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void assignMember(@Valid @RequestBody Member member, @PathVariable Long id) {
        memberService.assignMember(member, id);
    }

    @DeleteMapping("/unassign/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unassignMember(@PathVariable Long id) {
        memberService.unassignMember(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMember(@RequestParam("event_id") Long eventId) {
        memberService.deleteMember(eventId);
    }
}
