package com.example.ehbrenting.exceptions;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidPasswordException.class)
    public String handleInvalidPassword(
            InvalidPasswordException ex,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/profile";
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public String handlePasswordMismatch(
            PasswordMismatchException ex,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/profile";
    }

    @ExceptionHandler(EquipmentNotAvailableException.class)
    public String handleEquipmentNotAvailable(
            EquipmentNotAvailableException ex,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/catalog";
    }
}
