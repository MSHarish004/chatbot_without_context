
package com.example.ChatBot.controller;

import com.example.ChatBot.service.GrokService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ChatController {

    @Autowired
    private GrokService grokService;

    @GetMapping("/")
    public String index() {
        return "index"; // Return Thymeleaf view named index.html
    }

    @PostMapping("/chat")
    @ResponseBody
    public String chat(@RequestParam("message") String message) {
        try {
            return grokService.getChatResponse(message);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
