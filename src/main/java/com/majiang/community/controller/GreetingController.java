package com.majiang.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Author wxh
 * 2023/2/7 13:55
 */
@Controller
public class GreetingController {

    /*首页*/
    @GetMapping("/index")
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {

        model.addAttribute("name", name);
        return "index";
    }
}
