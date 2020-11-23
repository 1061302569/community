# 这是一款论坛，基于SpringBoot
``` java
package com.chu.community.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *  made in 2020年11月22日
 */
@Controller
public class HelloController {
    @GetMapping("/hello")
    public String hello(@RequestParam( name = "name") String name, Model model){
        model.addAttribute("name",name);
        return "hello";
    }
}

```