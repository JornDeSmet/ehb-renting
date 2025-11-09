package com.example.ehbrenting.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentDTO {

    private Long id;

    @NotEmpty(message = "Naam mag niet leeg zijn")
    private String name;

    private String description;

    @NotEmpty(message = "Categorie mag niet leeg zijn")
    private String category;

    @NotNull(message = "Prijs per dag mag niet leeg zijn")
    @DecimalMin(value = "0.0", inclusive = false, message = "Prijs moet groter dan 0 zijn")
    private BigDecimal pricePerDay;

    @NotNull(message = "Aantal mag niet leeg zijn")
    @Min(value = 0, message = "Aantal kan niet negatief zijn")
    private Integer quantity;

    private String imageUrl;

    private boolean active = true;
}
