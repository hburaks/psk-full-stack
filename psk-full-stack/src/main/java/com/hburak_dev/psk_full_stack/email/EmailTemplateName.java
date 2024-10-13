package com.hburak_dev.psk_full_stack.email;

import lombok.Getter;

@Getter
public enum EmailTemplateName {

    ACTIVATE_ACCOUNT("activate_account");


    private final String name;

    EmailTemplateName(String name) {
        this.name = name;
    }
}
