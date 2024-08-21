package com.crypto07.SpringSecurityMain.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @GetMapping("/adminUrl")
    public String adminUrl(){

        return "This is the secured end point - only admin can access this url";

    }

}
