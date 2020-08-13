package com.google.alpollo;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.google.gson.Gson;

@WebServlet("/auth")
public class AuthenticationServlet extends HttpServlet {
    private final Gson gson = new Gson();

    private class LoggedInInfo {
        final boolean loggedIn;
        final String loginUrl;

        public LoggedInInfo(boolean loggedIn, String loginUrl) {
            this.loggedIn = loggedIn;
            this.loginUrl = loginUrl;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        UserService userService = UserServiceFactory.getUserService();

        boolean loggedIn = userService.isUserLoggedIn();
        String loginUrl = userService.createLoginURL("/");
        response.getWriter().println(gson.toJson(new LoggedInInfo(loggedIn, loginUrl)));
    }
}
