package com.guilherme.encurtador_url.url;

import org.springframework.stereotype.Component;

@Component
public class UrlMath {

    private final String alfabeto = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public String encode(Long id){

        StringBuilder s = new StringBuilder();

        while (id > 0){

            int value = (int) (id % 62);

            s.append(alfabeto.charAt(value));

            id /= 62;
        }

        s.reverse();

        return s.toString();
    }
}
