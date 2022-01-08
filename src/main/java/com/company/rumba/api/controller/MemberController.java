package com.company.rumba.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/member")
public class MemberController {
    @GetMapping("/add")
    public String addMember() {
        return "added";
    }
}
