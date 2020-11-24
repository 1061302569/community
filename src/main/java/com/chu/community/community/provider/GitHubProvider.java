package com.chu.community.community.provider;

import com.alibaba.fastjson.JSON;
import com.chu.community.community.dto.AccessTokenDTO;
import com.chu.community.community.dto.GitHubUser;

import okhttp3.*;
import org.springframework.stereotype.Component;


import java.io.IOException;

import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;

@Component
public class GitHubProvider {
    public String getAccessTokenDTO(AccessTokenDTO accessTokenDTO){
         MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, com.alibaba.fastjson.JSON.toJSONString( accessTokenDTO ) );
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post( (okhttp3.RequestBody) body )
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            System.out.println(string);
            String[] split = string.split( "&" );
            String tokenStr = split[0];
            String token = tokenStr.split( "=" )[1];
            System.out.println(token);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public GitHubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                //.url("https://api.github.com/user?access_token="+accessToken)
                .url("https://api.github.com/user?access_token=3b56077099ce9d2ee7e29d443cf4aeb0b45a9feb")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GitHubUser gitHubUser = com.alibaba.fastjson.JSON.parseObject( string, GitHubUser.class );
            return gitHubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
