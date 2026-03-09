package com.guilherme.encurtador_url.url;

import jakarta.persistence.Index;
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

    public Long decode(String url) {
        Long result = 0L;

        for (int i = 0;i < url.length();i++){
            result = result * 62 + alfabeto.indexOf(url.charAt(i));
        }

        return result;
    }
}
