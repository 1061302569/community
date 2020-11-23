## 我的社区

## 资料
https://spring.io/guides
https://spring.io/guides/gs/serving-web-content/

## 工具
<<<<<<< HEAD
- IDEA
- Git
- MyBaties
=======
>>>>>>> 16be387b80bd7e59575beb6427eeabe67cd76e60

## 这是一款论坛，基于SpringBoot
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