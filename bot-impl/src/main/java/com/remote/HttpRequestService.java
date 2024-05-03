package com.remote;

import com.google.gson.Gson;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;

public class HttpRequestService<T,R>  {

    public R sendRequest(String path, T content, HttpMethod method, Class<R> returnType){
        try {
            WebClient builder = WebClient.builder().build();
            String response = builder
                    .method(method)
                    .uri(URI.create(path))
                    .header("Content-Type", "application/json")
                    .bodyValue(content)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return new Gson().fromJson(response, returnType);
        } catch (Exception e){
            System.out.println("Error: could not fetch remote bot decision");
            e.printStackTrace();
        }
        return null;
    }
}