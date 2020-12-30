package com.chu.community.community.controller;

import com.chu.community.community.dto.CommentCreateDTO;
import com.chu.community.community.dto.ResultDTO;
import com.chu.community.community.exception.CustomizeErrorCode;
import com.chu.community.community.model.Comment;
import com.chu.community.community.model.User;
import com.chu.community.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CommentController  {
    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){
        User user = (User)request.getSession().getAttribute( "user" );
        if(user == null){
            return ResultDTO.errorOf( CustomizeErrorCode.NO_LOGIN );
        }
        Comment comment = new Comment();
        comment.setParentId( commentCreateDTO.getParentId());
        comment.setContent( commentCreateDTO.getContent() );
        comment.setType( commentCreateDTO.getType() );
        comment.setGmtModified( System.currentTimeMillis() );
        comment.setGmtCreate( System.currentTimeMillis() );
        comment.setCommentator( user.getId() );
        comment.setLikeCount( 0L );
        commentService.inset( comment );
        return ResultDTO.okOf();
    }
}
