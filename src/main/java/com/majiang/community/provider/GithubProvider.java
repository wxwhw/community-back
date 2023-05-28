package com.majiang.community.provider;

import com.alibaba.fastjson2.JSON;
import com.majiang.community.dto.AccessTokenDTO;
import com.majiang.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Author wxh
 * 2023/2/8 17:25
 */
@Component
public class GithubProvider {

    public static final MediaType mediaType = MediaType.get("application/json; charset=utf-8");

    public String getAccessToken(AccessTokenDTO accessTokenDTO) {

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON.toJSONString(accessTokenDTO),mediaType);
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "fail to getAccessToken!!!";
    }

    public GithubUser getUser(String access_token) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                .header("Authorization", "Bearer " + access_token)
                .build();

        try (Response response = client.newCall(request).execute()) {
            /*将返回的Json转为对象*/
            String JsonString = response.body().string();
            GithubUser githubUser = JSON.parseObject(JsonString, GithubUser.class);
            return githubUser;
        } catch (IOException e) {
        }
        return null;
    }
}




