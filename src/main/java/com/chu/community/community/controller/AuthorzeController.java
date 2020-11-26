package com.chu.community.community.controller;

import com.chu.community.community.dto.AccessTokenDTO;
import com.chu.community.community.dto.GitHubUser;
import com.chu.community.community.mapper.UserMapper;
import com.chu.community.community.model.User;
import com.chu.community.community.provider.GitHubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthorzeController {
    @Autowired
    private GitHubProvider gitHubProvider;

    @Autowired
    private UserMapper userMapper;

    @Value( "${github.client.id}" )
    private String clientId;

    @Value( "${github.client.secret}" )
    private String clientSecret;

    @Value( "${github.redirect.uri}" )
    private String redirectUri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletRequest request
                            ){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String accessToken = gitHubProvider.getAccessTokenDTO( accessTokenDTO );
        GitHubUser gitHubProviderUser = gitHubProvider.getUser( accessToken );
        System.out.println(gitHubProviderUser.getName());
        if(gitHubProviderUser != null){
            User user = new User();
            user.getToken( UUID.randomUUID().toString() );
            user.getName(gitHubProviderUser.getName());
            user.getAccountId(gitHubProviderUser.getId());
            user.getGmtCreate(System.currentTimeMillis());
            user.getGmtModified(user.getGmtCreate( System.currentTimeMillis() ));
            userMapper.insert( user );
            //登录成功,写cookie和session
            request.getSession().setAttribute( "user",gitHubProviderUser );
            return "redirect:/";
        }else{
            return "redirect:/";
        }
        //return "index";
    }
}

