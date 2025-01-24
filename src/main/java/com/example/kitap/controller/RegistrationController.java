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

@Controller
public class RegistrationController {

    private final CustomerService customerService;

    @Autowired
    public RegistrationController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("customer", new CustomerEntity());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute CustomerEntity customer,
                               BindingResult result,
                               Model model) {
        try {
            // Basic validation
            if (customer.getEmail() == null || customer.getEmail().trim().isEmpty()) {
                result.rejectValue("email", "error.email", "Email is required");
                return "register";
            }

            if (customer.getPassword() == null || customer.getPassword().trim().isEmpty()) {
                result.rejectValue("password", "error.password", "Password is required");
                return "register";
            }

            // Check for existing user
            if (customerService.findByEmail(customer.getEmail()).isPresent()) {
                result.rejectValue("email", "error.email", "This email is already registered");
                return "register";
            }

            // Set default role as USER
            customer.setRole(Role.USER);

            // Save the customer
            customerService.saveCustomer(customer);

            return "redirect:/login?registered=true";

        } catch (Exception e) {
            result.rejectValue("global", "error.global", "Registration failed. Please try again.");
            return "register";
        }
    }
}