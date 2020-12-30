package com.chu.community.community.dto;

import com.chu.community.community.exception.CustomizeErrorCode;
import com.chu.community.community.exception.CustomizeException;
import com.chu.community.community.model.User;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;

@Data
public class ResultDTO {
    private Integer code;
    private String message;

    public static ResultDTO errorOf(Integer code,String message){

        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode( code );
        resultDTO.setMessage( message );
        return resultDTO;
    }


    public static ResultDTO errorOf(CustomizeErrorCode ex) {
        return errorOf( ex.getCode(),ex.getMessage() );
    }

    public static ResultDTO errorOf(CustomizeException errorCode) {
        return errorOf( errorCode.getCode(),errorCode.getMessage() );
    }

    public static ResultDTO okOf(){
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode( 200 );
        resultDTO.setMessage( "登录成功" );
        return resultDTO;
    }

}
