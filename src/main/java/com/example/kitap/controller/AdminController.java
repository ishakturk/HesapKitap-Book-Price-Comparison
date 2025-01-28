package com.example.kitap.controller;

import com.example.kitap.entity.CustomerEntity;
import com.example.kitap.service.dbservice.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final CustomerService customerService;

    @Autowired
    public AdminController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public String adminDashboard(Model model) {
        model.addAttribute("users", customerService.getAllCustomers());
        return "admin";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        CustomerEntity user = customerService.findCustomerById(id)
                .orElseThrow(() -> new IllegalArgumentException("Geçersiz kullanıcı ID: " + id));
        model.addAttribute("user", user);
        return "edit-user";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Long id,
                             @ModelAttribute CustomerEntity user,
                             BindingResult result) {
        if (result.hasErrors()) {
            return "edit-user";
        }
        customerService.updateCustomerBoolean(user);
        return "redirect:/admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        customerService.deleteCustomerById(id);
        return "redirect:/admin";
    }
}