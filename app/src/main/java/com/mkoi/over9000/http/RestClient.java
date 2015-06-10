package com.mkoi.over9000.http;

import com.mkoi.over9000.message.response.LoginResponse;

import org.androidannotations.annotations.rest.Accept;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.MediaType;
import org.androidannotations.api.rest.RestClientErrorHandling;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

/**
 * Wrapper na REST Client ze Springa, zrobiony z Android Annotations
 * @author Bartłomiej Borucki
 */
@Rest(rootUrl = "http://over9000-cryptosync.rhcloud.com", converters = {MappingJacksonHttpMessageConverter.class})
//@Rest(rootUrl = "http://192.168.0.4:3000", converters = {MappingJacksonHttpMessageConverter.class})
public interface RestClient extends RestClientErrorHandling {

    @Get("/login/{nick}")
    @Accept(MediaType.APPLICATION_JSON)
    LoginResponse userLogin(String nick);
}
