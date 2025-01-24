package com.example.kitap.service.dbservice;

import com.example.kitap.entity.Role;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.kitap.entity.CustomerEntity;
import com.example.kitap.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<CustomerEntity> findCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public Optional<CustomerEntity> findCustomerById(long id) {
        return customerRepository.findById(id);
    }

    public void saveCustomer(CustomerEntity customer) {


        try {
            // Encode the password before saving
            String encodedPassword = passwordEncoder.encode(customer.getPassword());
            customer.setPassword(encodedPassword);

            // Ensure role is set for new customers
            if (customer.getRole() == null) {
                customer.setRole(Role.USER);
            }

            CustomerEntity savedCustomer = customerRepository.save(customer);
        } catch (Exception e) {
            throw e;
        }
    }

    public CustomerEntity updateCustomer(CustomerEntity customerEntity) {
        return customerRepository.save(customerEntity);
    }

    public void deleteCustomer(CustomerEntity customerEntity) {
        customerRepository.delete(customerEntity);
    }

    public void deleteCustomerById(long id) {
        customerRepository.deleteById(id);
    }

    public List<CustomerEntity> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<CustomerEntity> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Transactional
    public boolean updateCustomerInfo(String email, String newPassword) {
        CustomerEntity existingCustomer = customerRepository.findByEmailCustom(email);

        if (existingCustomer == null) {
            return false; // Customer not found
        }

        if (newPassword != null && !newPassword.trim().isEmpty()) {
            existingCustomer.setPassword(newPassword);
            customerRepository.save(existingCustomer); // Persist changes
        }

        return true;
    }

    @Transactional
    public boolean updateCustomerBoolean(CustomerEntity customer) {
        Optional<CustomerEntity> existingCustomer = customerRepository.findById(customer.getId());

        if (existingCustomer.isEmpty()) {
            return false; // Customer not found
        }

        CustomerEntity customerToUpdate = existingCustomer.get();
        customerToUpdate.setEmail(customer.getEmail() != null ? customer.getEmail() : customerToUpdate.getEmail());
        customerToUpdate.setPassword(customer.getPassword() != null ? customer.getPassword() : customerToUpdate.getPassword());
        customerToUpdate.setRole(customer.getRole() != null ? customer.getRole() : customerToUpdate.getRole());

        customerRepository.save(customerToUpdate);
        return true;
    }

}

