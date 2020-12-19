package com.chu.community.community.controller;

import com.chu.community.community.dto.PaginlationDTO;
import com.chu.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * made in 2020年11月22日
 */
@Controller
public class IndexController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(
            Model model,
            @RequestParam(value="page",defaultValue = "1") Integer page,
            @RequestParam(value="size",defaultValue = "5") Integer size
    ) {

        PaginlationDTO paginlation = questionService.list(page,size);
/*        for (QuestionDTO questionDTO : questionList) {
            questionDTO.setDescription( "修改" );
        }*/
        model.addAttribute( "paginlation",paginlation );
        return "index";
    }
}
