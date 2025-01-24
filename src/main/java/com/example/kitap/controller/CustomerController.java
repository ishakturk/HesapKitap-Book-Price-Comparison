package com.example.kitap.controller;

import com.example.kitap.entity.CustomerEntity;
import com.example.kitap.service.dbservice.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public String listCustomers(Model model) {
        model.addAttribute("customers", customerService.getAllCustomers());
        return "customers/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("customer", new CustomerEntity());
        return "customers/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        CustomerEntity customer = customerService.findCustomerById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
        model.addAttribute("customer", customer);
        return "customers/form";
    }

    @PostMapping("/save")
    public String saveCustomer(@ModelAttribute CustomerEntity customer) {
        if (customer.getId() == null) {
            // For new customers, ensure password is provided
            if (customer.getPassword() == null || customer.getPassword().trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required for new customers");
            }
            customerService.saveCustomer(customer);
        } else {
            // For existing customers, only update if password is provided
            CustomerEntity existingCustomer = customerService.findCustomerById(customer.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

            if (customer.getPassword() == null || customer.getPassword().trim().isEmpty()) {
                // Keep existing password if no new password provided
                customer.setPassword(existingCustomer.getPassword());
            }
            customerService.updateCustomer(customer);
        }
        return "redirect:/customers";
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomerById(id);
        return "redirect:/customers";
    }

    @PostMapping("/update-password")
    public String updatePassword(@RequestParam("newPassword") String newPassword, Principal principal) {
        String currentEmail = principal.getName(); // Retrieve the logged-in user's email
        boolean isUpdated = customerService.updateCustomerInfo(currentEmail, newPassword);

        if (!isUpdated) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        }

        return "redirect:/customers/profile"; // Adjust the redirect as per your application's flow
    }


    @PostMapping("/admin/update-customer")
    public ResponseEntity<Void> updateAnyCustomer(@RequestBody CustomerEntity customer) {
        if (customer.getId() == null) {
            return ResponseEntity.badRequest().build(); // Ensure the ID is provided for updates
        }

        boolean isUpdated = customerService.updateCustomerBoolean(customer);

        return isUpdated ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }



}