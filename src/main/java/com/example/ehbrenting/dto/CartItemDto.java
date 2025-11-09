package com.example.ehbrenting.dto;

import java.time.LocalDate;

public class CartItemDto {

    private Long equipmentId;
    private int quantity;
    private LocalDate startDate;
    private LocalDate endDate;

    public CartItemDto() {
    }

    public CartItemDto(Long equipmentId, int quantity, LocalDate startDate, LocalDate endDate) {
        this.equipmentId = equipmentId;
        this.quantity = quantity;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }



    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
