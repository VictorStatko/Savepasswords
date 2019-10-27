package com.statkovit.authorizationservice.services.impl;

import com.statkovit.authorizationservice.services.CookieService;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Service
public class CookieServiceImpl implements CookieService {

    @Override
    public void addCookies(Map<String, String> cookies, HttpServletResponse response) {
        cookies.forEach((key, value) -> {
            response.addCookie(
                    createHttpOnlyCookie(key, value, 24 * 60 * 60) // expires in 1 day
            );
        });
    }

    @Override
    public void deleteCookies(Map<String, String> cookies, HttpServletResponse response) {
        cookies.forEach((key, value) -> {
            response.addCookie(
                    createHttpOnlyCookie(key, value, 0) // expires now
            );
        });
    }

    private Cookie createHttpOnlyCookie(String key, String value, int cookieTime) {
        Cookie cookie = new Cookie(key, value);
        //TODO set true for production
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(cookieTime);
        cookie.setPath("/");
        return cookie;
    }
}
