package com.chu.community.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {
    QUESTION_NOT_FOUND(2001,"你找的问题不存在，要不换个试试"),
    TARGET_PARAM_NOT_FOUND(2002,"你找的问题不存在，要不换个试试"),
    NO_LOGIN(2003,"当前操作需要登录，请登录后再试"),
    SYSTEM_ERROR(2004,"服务器冒烟了，要不您稍后试试"),
    TYPE_PARAM_WARNING(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"您回复的评论不存在了");

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    private Integer code;
    private String message;

    CustomizeErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
