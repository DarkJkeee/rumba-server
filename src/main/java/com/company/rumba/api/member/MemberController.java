package com.company.rumba.api.member;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMember(@RequestParam("event_id") Long eventId) {
        memberService.deleteMember(eventId);
    }
}
