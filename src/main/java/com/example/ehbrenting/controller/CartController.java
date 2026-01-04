package com.example.ehbrenting.controller;

import com.example.ehbrenting.dto.CartItemDTO;
import com.example.ehbrenting.model.Rental;
import com.example.ehbrenting.model.User;
import com.example.ehbrenting.service.CartService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /* ===================== ADD TO CART ===================== */

    @PostMapping("/add")
    public String addToCart(
            @Valid CartItemDTO cartItemDto,
            @AuthenticationPrincipal User user,
            RedirectAttributes redirectAttributes
    ) {
        if (user == null) {
            return "redirect:/login";
        }

        try {
            cartService.addToCart(user, cartItemDto);
            redirectAttributes.addFlashAttribute(
                    "success",
                    "Item toegevoegd aan winkelwagen"
            );
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/equipment/" + cartItemDto.getEquipmentId();
    }

    /* ===================== VIEW CART ===================== */

    @GetMapping
    public String showCart(
            @AuthenticationPrincipal User user,
            Model model
    ) {
        if (user == null) {
            return "redirect:/login";
        }

        List<Rental> cartItems = cartService.getCartItems(user);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("title", "Winkelwagen");

        return "cart/cart";
    }

    /* ===================== CONFIRM ORDER ===================== */

    @PostMapping("/confirm")
    public String confirmOrder(
            @AuthenticationPrincipal User user,
            RedirectAttributes redirectAttributes
    ) {
        if (user == null) {
            return "redirect:/login";
        }

        cartService.confirmOrder(user);

        redirectAttributes.addFlashAttribute(
                "success",
                "Bestelling succesvol bevestigd"
        );

        return "redirect:/profile/rentals";
    }
}
