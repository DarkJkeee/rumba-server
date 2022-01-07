package com.company.rumba.auth.email;

public interface EmailSender {
    void send(String to, String email);
}
