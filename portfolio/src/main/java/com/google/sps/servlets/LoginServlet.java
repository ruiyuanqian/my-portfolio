// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

////////////////////////////////
////  new packages imported
////////////////////////////////

import com.google.gson.Gson;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

  private class LoginStatus{

      private boolean isLoggedIn;
      private String logInURL;
      private String logOutURL;
      private String userEmail;

      public LoginStatus(boolean nIsLoggedIn, String logInURL,String logOutURL,String userEmail){
          this.isLoggedIn = nIsLoggedIn;
          this.logInURL = logInURL;
          this.logOutURL = logOutURL;
          this.userEmail = userEmail;
      }
  }

  private static final Gson aGson = new Gson();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    response.setContentType("application/json;");
    UserService userService = UserServiceFactory.getUserService();
    
    String json;

    if (userService.isUserLoggedIn()) 
    {
        LoginStatus aLoginStatus = new LoginStatus(
            true,
            "",
            userService.createLogoutURL("/"),
            userService.getCurrentUser().getEmail()
        );
        json = aGson.toJson( aLoginStatus );
    }
    else
    {
        LoginStatus aLoginStatus = new LoginStatus(
            false,
            userService.createLoginURL("/"),
            "",
            ""
        );
        json = aGson.toJson( aLoginStatus );
    }
    response.getWriter().println(json);

    /*
    response.setContentType("text/html");

    UserService userService = UserServiceFactory.getUserService();
    if (userService.isUserLoggedIn()) {
      String userEmail = userService.getCurrentUser().getEmail();
      String urlToRedirectToAfterUserLogsOut = "/";
      String logoutUrl = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);

      response.getWriter().println("<p>Hello " + userEmail + "!</p>");
      response.getWriter().println("<p>Logout <a href=\"" + logoutUrl + "\">here</a>.</p>");
    } else {
      String urlToRedirectToAfterUserLogsIn = "/";
      String loginUrl = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);

      response.getWriter().println("<p>Hello stranger.</p>");
      response.getWriter().println("<p>Login <a href=\"" + loginUrl + "\">here</a>.</p>");
    }
    */

  }
}
