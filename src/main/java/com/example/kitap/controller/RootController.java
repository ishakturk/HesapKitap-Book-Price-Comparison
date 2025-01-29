package com.example.kitap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

    @GetMapping("/")
    public String redirectToBooks() {
        return "redirect:/books";
    }
}
