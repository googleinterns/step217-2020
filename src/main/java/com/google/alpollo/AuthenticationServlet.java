package com.google.alpollo;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.google.gson.Gson;

/** Servlet that returns an auth info that includes status whether the user logged in and link to auth. */
@WebServlet("/auth")
public class AuthenticationServlet extends HttpServlet {
    private final Gson gson = new Gson();

    /** Class that provides info about logged in status of the user and link to the authentication. */
    private class LoggedInInfo {
        private final boolean isLoggedIn;
        private final String authUrl;

        public LoggedInInfo(boolean isLoggedIn, String authUrl) {
            this.isLoggedIn = isLoggedIn;
            this.authUrl = authUrl;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        UserService userService = UserServiceFactory.getUserService();

        boolean isLoggedIn = userService.isUserLoggedIn();
        String authUrl = userService.createLoginURL("/");
        response.getWriter().println(gson.toJson(new LoggedInInfo(isLoggedIn, authUrl)));
    }
}
