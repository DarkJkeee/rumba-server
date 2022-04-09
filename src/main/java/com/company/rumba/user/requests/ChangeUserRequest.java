package com.company.rumba.user.requests;

import lombok.Data;

@Data
public class ChangeUserRequest {
    private String email;
    private String firstName;
    private String lastName;
}
