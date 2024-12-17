package com.hburak_dev.psk_full_stack.session;

import lombok.Getter;

@Getter
public enum SessionStatusType {

    AWAITING_PSYCHOLOGIST_APPROVAL("Terapist Onayı Bekleniyor"),
    APPOINTMENT_SCHEDULED("Randevu Alındı"),
    COMPLETED("Gerçekleşti"),
    CANCELED("İptal"),
    UNAVAILABLE("Müsait Değil");

    private final String description;

    SessionStatusType(String description) {
        this.description = description;
    }
}
