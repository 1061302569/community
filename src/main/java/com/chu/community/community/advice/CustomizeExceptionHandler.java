package com.chu.community.community.advice;

import com.alibaba.fastjson.JSON;
import com.chu.community.community.dto.ResultDTO;
import com.chu.community.community.exception.CustomizeErrorCode;
import com.chu.community.community.exception.CustomizeException;
import jdk.nashorn.internal.ir.RuntimeNode;
import org.apache.catalina.webresources.WarResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    Object handle(Throwable ex, Model model, HttpServletRequest request, HttpServletResponse response) {
        String contentType = request.getContentType();
        if("application/json".equals( contentType )){
            ResultDTO resultDTO = null;
            //返回json
            if(ex instanceof CustomizeException){
                resultDTO =  ResultDTO.errorOf( (CustomizeException) ex );
            }else {
                resultDTO = resultDTO.errorOf( CustomizeErrorCode.SYSTEM_ERROR );
            }
            try {
                response.setContentType( "application/json" );
                response.setStatus( 200 );
                response.setCharacterEncoding( "UTF-8" );
                PrintWriter writer = response.getWriter();
                writer.write( JSON.toJSONString(resultDTO) );
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }else{
            //错误页面跳转
            if(ex instanceof CustomizeException){
                model.addAttribute( "message", ex.getMessage() );
            }else {
                model.addAttribute( "message","服务冒烟了，要不您稍后试试！！！" );
            }
            return new ModelAndView( "error" );
        }

    }
}
