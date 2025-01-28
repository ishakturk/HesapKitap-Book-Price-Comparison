package com.example.kitap.controller;

import com.example.kitap.entity.CustomerEntity;
import com.example.kitap.service.dbservice.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/profile")
@PreAuthorize("hasRole('USER')")
public class ProfileController {

    private final CustomerService customerService;

    @Autowired
    public ProfileController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public String showProfile(Principal principal, Model model) {
        CustomerEntity user = customerService.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı"));
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/update")
    public String updateProfile(Principal principal,
                                @RequestParam(required = false) String newPassword) {
        String email = principal.getName();
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            customerService.updateCustomerInfo(email, newPassword);
        }
        return "redirect:/profile?success=true";
    }
}
