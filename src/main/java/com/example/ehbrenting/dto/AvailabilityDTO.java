package com.example.ehbrenting.dto;

import java.time.LocalDate;

public class AvailabilityDTO {
    private LocalDate date;
    private int available;

    public AvailabilityDTO(LocalDate date, int available) {
        this.date = date;
        this.available = available;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }
}
