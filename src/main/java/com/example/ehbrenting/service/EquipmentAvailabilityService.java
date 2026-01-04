package com.example.ehbrenting.service;

import com.example.ehbrenting.dto.AvailabilityDTO;
import com.example.ehbrenting.dto.EquipmentDTO;
import com.example.ehbrenting.exceptions.InsufficientAvailabilityException;
import com.example.ehbrenting.exceptions.InvalidRentalPeriodException;
import com.example.ehbrenting.model.Equipment;
import com.example.ehbrenting.model.Rental;
import com.example.ehbrenting.repository.EquipmentRepository;
import com.example.ehbrenting.repository.RentalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EquipmentAvailabilityService {

    private static final int MAX_DAYS_AHEAD = 30;

    private final EquipmentRepository equipmentRepository;
    private final RentalRepository rentalRepository;

    public EquipmentAvailabilityService(
            EquipmentRepository equipmentRepository,
            RentalRepository rentalRepository
    ) {
        this.equipmentRepository = equipmentRepository;
        this.rentalRepository = rentalRepository;
    }


    public void validateRentalRequest(
            Long equipmentId,
            LocalDate startDate,
            LocalDate endDate,
            int requestedQuantity
    ) {
        validateDates(startDate, endDate);
        validateAvailability(equipmentId, startDate, endDate, requestedQuantity);
    }

    public List<AvailabilityDTO> calculateAvailability(
            Long equipmentId,
            LocalDate from,
            LocalDate to
    ) {
        Equipment equipment = getEquipment(equipmentId);
        int totalQuantity = equipment.getQuantity();

        List<Rental> rentals =
                rentalRepository.findRentalsForEquipmentInDateRange(
                        equipmentId, from, to
                );

        List<AvailabilityDTO> availability = new ArrayList<>();

        for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {
            int rented = calculateRentedQuantityForDate(rentals, date);
            availability.add(new AvailabilityDTO(date, totalQuantity - rented));
        }

        return availability;
    }


    private void validateDates(LocalDate start, LocalDate end) {
        LocalDate today = LocalDate.now();
        LocalDate maxDate = today.plusDays(MAX_DAYS_AHEAD);

        if (start.isBefore(today)) {
            throw new InvalidRentalPeriodException(
                    "Je kan niet in het verleden huren"
            );
        }

        if (end.isBefore(start)) {
            throw new InvalidRentalPeriodException(
                    "Einddatum moet na de startdatum liggen"
            );
        }

        if (end.isAfter(maxDate)) {
            throw new InvalidRentalPeriodException(
                    "Je kan maximum 30 dagen vooruit huren"
            );
        }
    }

    private void validateAvailability(
            Long equipmentId,
            LocalDate start,
            LocalDate end,
            int requestedQuantity
    ) {
        Equipment equipment = getEquipment(equipmentId);
        int totalQuantity = equipment.getQuantity();

        List<Rental> rentals =
                rentalRepository.findRentalsForEquipmentInDateRange(
                        equipmentId, start, end
                );

        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            int rented = calculateRentedQuantityForDate(rentals, date);
            int available = totalQuantity - rented;

            if (available < requestedQuantity) {
                throw new InsufficientAvailabilityException(
                        "Onvoldoende beschikbaarheid op " + date
                );
            }
        }
    }


    private Equipment getEquipment(Long id) {
        return equipmentRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Equipment not found")
                );
    }

    private int calculateRentedQuantityForDate(
            List<Rental> rentals,
            LocalDate date
    ) {
        return rentals.stream()
                .filter(r ->
                        !date.isBefore(r.getStartDate()) &&
                                !date.isAfter(r.getEndDate())
                )
                .mapToInt(Rental::getQuantity)
                .sum();
    }

    public boolean isEquipmentAvailable(
            Long equipmentId,
            EquipmentDTO equipment,
            LocalDate from,
            LocalDate to
    ) {
        if (!equipment.isActive()) {
            return false;
        }

        return calculateAvailability(equipmentId, from, to)
                .stream()
                .anyMatch(a -> a.getAvailable() > 0);
    }

}
