package com.statkovit.authorizationservice.services;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface CookieService {
    void addCookies(Map<String, String> cookies, HttpServletResponse response);

    void deleteCookies(Map<String, String> cookies, HttpServletResponse response);
}
