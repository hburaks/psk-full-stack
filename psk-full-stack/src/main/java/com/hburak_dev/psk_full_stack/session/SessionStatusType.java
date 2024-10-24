package com.hburak_dev.psk_full_stack.session;

import lombok.Getter;

@Getter
public enum SessionStatusType {

    AWAITING_PSYCHOLOGIST_APPROVAL("Terapist Onayı Bekleniyor"),
    AWAITING_PAYMENT("Ödeme Bekleniyor"),
    AWAITING_PAYMENT_CONFIRMATION("Ödeme Onayı Bekleniyor"),
    APPOINTMENT_SCHEDULED("Randevu Alındı"),
    COMPLETED("Gerçekleşti"),
    CANCELED("İptal");

    private final String description;

    SessionStatusType(String description) {
        this.description = description;
    }
}
