package com.company.rumba.user.requests;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChangePasswordRequest {
    @NotNull(message = "Must be an old password")
    private String oldPassword;
    @NotNull(message ="Must be a new password")
    private String newPassword;
}
