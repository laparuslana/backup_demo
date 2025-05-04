package com.example.mainapp.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContentController {
    
    @GetMapping("/req/login")
    public String login(){
        return "login";
    }
    
    @GetMapping("/req/signup")
    public String signup(){
        return "signup";
    }

    @GetMapping("/user")
    public String home(){
        return "user";
    }

    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }

    @GetMapping("/history")
    public String history(){
        return "history";
    }
}
