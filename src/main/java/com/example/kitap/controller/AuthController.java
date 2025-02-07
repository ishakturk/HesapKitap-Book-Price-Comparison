package com.example.kitap.controller;

import com.example.kitap.entity.CustomerEntity;
import com.example.kitap.entity.Role;
import com.example.kitap.service.dbservice.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final CustomerService customerService;

    @Autowired
    public AuthController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error,
                                Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Geçersiz kimlik bilgileri!");
        }
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("customer", new CustomerEntity());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute CustomerEntity customer,
                               BindingResult result) {
        try {
            if (customer.getEmail() == null || customer.getEmail().trim().isEmpty()) {
                result.rejectValue("email", "error.email", "Email gereklidir");
                return "register";
            }

            if (customer.getPassword() == null || customer.getPassword().trim().isEmpty()) {
                result.rejectValue("password", "error.password", "Şifre gereklidir");
                return "register";
            }

            if (customerService.findByEmail(customer.getEmail()).isPresent()) {
                result.rejectValue("email", "error.email", "Bu email zaten kayıtlı");
                return "register";
            }

            customer.setRole(Role.USER);
            customerService.saveCustomer(customer);
            return "redirect:/login?registered=true";

        } catch (Exception e) {
            result.rejectValue("global", "error.global", "Kayıt başarısız. Lütfen tekrar deneyin.");
            return "register";
        }
    }
}
