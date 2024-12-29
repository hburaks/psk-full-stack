package com.hburak_dev.psk_full_stack.email;

import lombok.Getter;

@Getter
public enum EmailTemplateName {

    ACTIVATE_ACCOUNT("activate_account"),
    SESSION_REMINDER("session_reminder");


    private final String name;

    EmailTemplateName(String name) {
        this.name = name;
    }
}
