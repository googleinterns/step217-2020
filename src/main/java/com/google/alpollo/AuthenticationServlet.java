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
        private final boolean loggedIn;
        private final String authUrl;

        public LoggedInInfo(boolean loggedIn, String authUrl) {
            this.loggedIn = loggedIn;
            this.authUrl = authUrl;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        UserService userService = UserServiceFactory.getUserService();

        boolean loggedIn = userService.isUserLoggedIn();
        String authUrl = userService.createLoginURL("/");
        response.getWriter().println(gson.toJson(new LoggedInInfo(loggedIn, authUrl)));
    }
}
