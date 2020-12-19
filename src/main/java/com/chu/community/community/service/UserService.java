package com.chu.community.community.service;

import com.chu.community.community.mapper.UserMapper;
import com.chu.community.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    //判断是否登录用户是否存在数据库
    public void createOrUpdate(User user) {
        User dbUser = userMapper.findByAccountId(user.getAccountId());
        if(dbUser == null){
            //插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert( user );
        }else{
            //更新
            dbUser.setGmtModified( System.currentTimeMillis() );
            dbUser.setAvatarUrl( user.getAvatarUrl() );
            dbUser.setName(user.getName());
            dbUser.setToken( user.getToken() );
            userMapper.update(dbUser);
        }
    }
}
