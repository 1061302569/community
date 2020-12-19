package com.chu.community.community.controller;

import com.chu.community.community.dto.AccessTokenDTO;
import com.chu.community.community.dto.GitHubUser;
import com.chu.community.community.mapper.UserMapper;
import com.chu.community.community.model.User;
import com.chu.community.community.provider.GitHubProvider;
import com.chu.community.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

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

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletResponse response
                            ){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String accessToken = gitHubProvider.getAccessTokenDTO( accessTokenDTO );
        GitHubUser gitHubProviderUser = gitHubProvider.getUser( accessToken );
        //System.out.println(gitHubProviderUser.getName());
        if(gitHubProviderUser != null){
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken( token );
            user.setName(gitHubProviderUser.getName());
            user.setAccountId( String.valueOf(gitHubProviderUser.getId()));
            user.setAvatarUrl( gitHubProviderUser.getAvatar_url() );
            userService.createOrUpdate(user);
            response.addCookie(new Cookie( "token",token ) );
            /*//登录成功,写cookie和session
            request.getSession().setAttribute( "user",gitHubProviderUser );*/
            return "redirect:/";
        }else{
            return "redirect:/";
        }
        //return "index";
    }

    //logout退出登录
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response){
        //移除user
        request.getSession().removeAttribute( "user" );
        //删除cookie
        Cookie cookie = new Cookie( "token", null );
        cookie.setMaxAge( 0 );
        response.addCookie( cookie );
        return "redirect:/";
    }
}

