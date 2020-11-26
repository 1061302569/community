package com.chu.community.community.controller;

import com.chu.community.community.dto.AccessTokenDTO;
import com.chu.community.community.dto.GitHubUser;
import com.chu.community.community.provider.GitHubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthorzeController {
    @Autowired
    private GitHubProvider gitHubProvider;

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
        GitHubUser user = gitHubProvider.getUser( accessToken );
        System.out.println(user.getName());
        if(user != null){
            //登录成功,写cookie和session
            request.getSession().setAttribute( "user",user );
            return "redirect:/";
        }else{
            return "redirect:/";
        }
        //return "index";
    }
}
