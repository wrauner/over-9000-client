package com.mkoi.over9000.http;

import com.mkoi.over9000.message.LoginMessage;
import com.mkoi.over9000.message.response.LoginResponse;
import com.mkoi.over9000.message.response.RegisterResponse;
import com.mkoi.over9000.model.User;

import org.androidannotations.annotations.rest.Accept;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.MediaType;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

/**
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
}
