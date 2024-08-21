package com.crypto07.SpringSecurityMain.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member")
public class MemberController {

    @GetMapping("/memberUrl")
    public String memberUrl(){

        return "This is the secuired end point - only members can access this url";

    }

}
