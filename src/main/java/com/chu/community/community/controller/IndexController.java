package com.chu.community.community.controller;

import com.chu.community.community.dto.PaginlationDTO;
import com.chu.community.community.dto.QuestionDTO;
import com.chu.community.community.mapper.QuestionMapper;
import com.chu.community.community.mapper.UserMapper;
import com.chu.community.community.model.User;
import com.chu.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * made in 2020年11月22日
 */
@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(
            HttpServletRequest request,
            Model model,
            @RequestParam(value="page",defaultValue = "1") Integer page,
            @RequestParam(value="size",defaultValue = "2") Integer size
    ) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals( "token" )) {
                    String token = cookie.getValue();
                    User user = userMapper.findByToken( token );
                    if (user != null) {
                        request.getSession().setAttribute( "user", user );
                    }
                    break;
                }
            }
        }
        PaginlationDTO paginlation = questionService.list(page,size);
/*        for (QuestionDTO questionDTO : questionList) {
            questionDTO.setDescription( "修改" );
        }*/
        model.addAttribute( "paginlation",paginlation );
        return "index";
    }
}
