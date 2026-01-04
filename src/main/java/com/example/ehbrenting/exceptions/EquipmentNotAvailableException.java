package com.example.ehbrenting.exceptions;

public class EquipmentNotAvailableException extends RuntimeException {

    public EquipmentNotAvailableException() {
        super("Dit materiaal is niet beschikbaar voor de gekozen periode.");
    }

    public EquipmentNotAvailableException(String message) {
        super(message);
    }
}
