package com.chu.community.community.controller;

import com.chu.community.community.dto.QuestionDTO;
import com.chu.community.community.mapper.QuestionMapper;
import com.chu.community.community.mapper.UserMapper;
import com.chu.community.community.model.Question;
import com.chu.community.community.model.User;
import com.chu.community.community.service.QuestionService;
import com.chu.community.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    //编辑
    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Integer id,
                       Model model){
        QuestionDTO question= questionService.getById( id );
        model.addAttribute( "title", question.getTitle() );
        model.addAttribute( "description", question.getDescription() );
        model.addAttribute( "tag", question.getTag() );
        model.addAttribute( "id",question.getId() );
        return "publish";
    }

    @GetMapping("/publish")
    public String publish() {
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam(value = "title",required = false) String title,
            @RequestParam(value = "description",required = false) String description,
            @RequestParam(value = "tag",required = false) String tag,
            @RequestParam(value = "id",required = false) Integer id,
            HttpServletRequest request,
            Model model
    ) {
        model.addAttribute( "title", title );
        model.addAttribute( "description", description );
        model.addAttribute( "tag", tag );

        if (title == null || title == "") {
            model.addAttribute( "error", "标题不能为空！" );
            return "publish";
        }
        if (description == null || description == "") {
            model.addAttribute( "error", "内容不能为空！" );
            return "publish";
        }
        if (tag == null || tag == "") {
            model.addAttribute( "error", "标签不能为空！" );
            return "publish";
        }

        Cookie[] cookies = request.getCookies();
        User user = null;
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals( "token" )) {
                    String token = cookie.getValue();
                    user = userMapper.findByToken( token );
                    if (user != null) {
                        request.getSession().setAttribute( "user", user );
                    }
                    break;
                }
            }
        }
        if (user == null) {
            model.addAttribute( "error", "用户未登录" );
            return "publish";
        }

        Question question = new Question();
        question.setTitle( title );
        question.setDescription( description );
        question.setTag( tag );
        question.setCreator( user.getId() );

        question.setId( id );
        questionService.createOrUpdate(question);
        //questionMapper.create( question );
        return "redirect:/";
    }
}

