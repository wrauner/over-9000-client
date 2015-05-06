package com.mkoi.over9000.http;

import com.mkoi.over9000.message.LoginMessage;
import com.mkoi.over9000.message.response.FriendResponse;
import com.mkoi.over9000.message.response.LoginResponse;
import com.mkoi.over9000.message.response.RegisterResponse;
import com.mkoi.over9000.message.response.SearchResponse;
import com.mkoi.over9000.message.response.ServerResponse;
import com.mkoi.over9000.model.User;

import org.androidannotations.annotations.rest.Accept;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Put;
import org.androidannotations.annotations.rest.RequiresHeader;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.MediaType;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

/**
 * Wrapper na REST Client ze Springa, zrobiony z Android Annotations
 * @Author Bartłomiej Borucki
 */
@Rest(rootUrl = "http://over9000-cryptosync.rhcloud.com", converters = {MappingJacksonHttpMessageConverter.class})
public interface RestClient {

    @Post("/login")
    @Accept(MediaType.APPLICATION_JSON)
    LoginResponse userLogin(LoginMessage loginMessage);

    @Post("/register")
    @Accept(MediaType.APPLICATION_JSON)
    RegisterResponse userRegister(User user);

    @Get("/search/{email}")
    @Accept(MediaType.APPLICATION_JSON)
    SearchResponse searchUsers(String email);

    @Get("/friends")
    @Accept(MediaType.APPLICATION_JSON)
    @RequiresHeader("Authorization")
    FriendResponse getFriends();

    @Put("/friends/requests/{email}")
    @Accept(MediaType.APPLICATION_JSON)
    @RequiresHeader("Authorization")
    ServerResponse friendRequest(String email);

    void setHeader(String name, String value);
    String getHeader(String name);

}
