package com.example.ehbrenting.controller;

import com.example.ehbrenting.dto.CartItemDTO;
import com.example.ehbrenting.model.Rental;
import com.example.ehbrenting.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public String addToCart(CartItemDTO cartItemDto, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        cartService.addToCart(principal.getName(), cartItemDto);
        return "redirect:/cart";
    }

    @GetMapping
    public String showCart(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        List<Rental> cartItems = cartService.getCartItems(principal.getName());
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("title", "Winkelwagen");
        return "cart/cart";
    }

    @PostMapping("/confirm")
    public String confirmOrder(Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        cartService.confirmOrder(principal.getName());
        return "redirect:/profile";
    }
}
