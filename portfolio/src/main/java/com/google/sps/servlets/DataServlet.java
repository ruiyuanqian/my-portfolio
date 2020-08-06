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

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

import java.util.Date;
import java.text.DateFormat;
import java.lang.Boolean;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.google.gson.Gson;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.google.sps.servlets.FTLSingleton;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private static final UserService userService = UserServiceFactory.getUserService();
  private static final Gson aGson = new Gson();

  class MemeRecord {
      String url;
      String comment;
      String userEmail;
      long timestamp; 
      double randomIndex;
      
      public MemeRecord(String nUrl, String nComment, String nUserEmail, long nTimestamp, double nRandomIndex){
          this.url = nUrl;
          this.comment = nComment;
          this.userEmail = nUserEmail;
          this.timestamp = nTimestamp;
          this.randomIndex = nRandomIndex;
      }
  }

  private void printJsonWithOneMeme(HttpServletResponse response, PreparedQuery results) throws IOException {
    response.setContentType("application/json;");

    List<Entity> memeList = new ArrayList<Entity>();
    results.asIterable().forEach(memeList::add);
    Collections.shuffle(memeList);
    
    memeList.stream().findFirst().ifPresent(
        ( aMeme ) -> {
            
            try
            {
                MemeRecord aMemeRecord = new MemeRecord(
                    "" + aMeme.getProperty("url"),
                    "" + aMeme.getProperty("comment"),
                    "" + aMeme.getProperty("userEmail"),
                    Long.parseLong( "" + aMeme.getProperty("timestamp") ),
                    Double.parseDouble( "" + aMeme.getProperty("randomIndex") )
                );

                //Gson aGson = new Gson();
                String json = aGson.toJson( aMemeRecord );

                response.getWriter().println(json);
            }
            catch( IOException e )
            {
                e.printStackTrace();
            }

        }
    );
    
  }

  private void printHTMLWithAllMemes(HttpServletResponse response, PreparedQuery results) throws IOException {
    response.setContentType("text/html;");
    // deprecated render function:
    // FTLSingleton.getInstance().render_allMemes_header( response.getWriter() );

    FTLSingleton.getInstance().render_staticPart( response.getWriter() , "allMemes" , "header" );
    for (Entity entity : results.asIterable()) 
    {
        MemeRecord tmpMeme = new MemeRecord(
            "" + entity.getProperty("url"),
            "" + entity.getProperty("comment"),
            "" + entity.getProperty("userEmail"),
            Long.parseLong( "" + entity.getProperty("timestamp") ),
            Double.parseDouble( "" + entity.getProperty("randomIndex") )
            );
        
        FTLSingleton.getInstance().render_allMemes_aMeme( response.getWriter() , tmpMeme );
    }
    FTLSingleton.getInstance().render_staticPart( response.getWriter() , "allMemes" , "footer" );

    /*
    response.getWriter().println("<a href=\"/\">Home Page</a>");
    
    for (Entity entity : results.asIterable()) {
      long id = entity.getKey().getId();
      String argURL = (String) entity.getProperty("url");
      String argComment = (String) entity.getProperty("comment");
      long timestamp = (long) entity.getProperty("timestamp");

      String userEmail = (String) entity.getProperty("userEmail");
      
      response.getWriter().println("\n <p> ### ### ### </p>\n");
      response.getWriter().println("\n <p> ID: " + id + "</p>");
      response.getWriter().println("\n <p> User: " + userEmail + "</p>");
      response.getWriter().println("\n <p> Timestamp: " + DateFormat.getDateTimeInstance().format(timestamp) + "</p>");
      response.getWriter().println("\n <p> Comment: " + argComment + "</p>");
      response.getWriter().println("\n <img src=\"" + argURL + "\"></img>");
    }
    */
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//    response.setContentType("text/html;");
//    response.getWriter().println("<h1>Hello world!</h1>");
//    response.getWriter().println("<h1>Hello [Ruiyuan Qian]</h1>");
//        String json = "{";
//            json += "\"status\": ";
//            json += 12345;
//            json += "}";
//        response.setContentType("application/json;");
//        response.getWriter().println(json);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query = new Query("Memes").addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);

    Boolean shouldPickARandomOne = Boolean.parseBoolean( request.getParameter("aRandomOne") );
    if(shouldPickARandomOne)
    {
        printJsonWithOneMeme(response, results);
    }
    else
    {
        printHTMLWithAllMemes(response, results);
    }

  }

  enum InputValidationStatus {
      VALID,
      ILLEGAL_URL,
      ILLEGAL_COMMENT,
  }

  InputValidationStatus checkUnallowedInput(String argURL, String argComment)
  {
      if( !Pattern.matches("^http:\\/\\/(.+)\\.sinaimg\\.cn\\/(large|mw600)\\/(.+)\\.(jpg|png|gif)$",argURL) )
      {
          return InputValidationStatus.ILLEGAL_URL;
      }
      
      if( !Pattern.matches("^(.+)$",argComment) )
      {
          return InputValidationStatus.ILLEGAL_COMMENT;
      }

      //0 means input is allowed
      return InputValidationStatus.VALID;
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
//    String text = request.getParameter("text-input");
//    response.setContentType("text/html;");
//    response.getWriter().println("echo : " + text);
    
    //UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn())
    {
        response.setContentType("text/html;");
        response.getWriter().println("<h1>Please login before uploading memes.</h1>");
        response.getWriter().println("<a href=\"/\">Home Page</a>");
        return ;
    }

    String unsafeArgURL = request.getParameter("url-input");
    String unsafeArgComment = request.getParameter("comment-input");

    String argURL = Jsoup.clean( unsafeArgURL , Whitelist.basic() );
    String argComment = Jsoup.clean( unsafeArgComment , Whitelist.basic() );

    InputValidationStatus unallowedInputType = checkUnallowedInput( argURL , argComment );
    if( unallowedInputType != InputValidationStatus.VALID )
    {
        response.setContentType("text/html;");
        if( unallowedInputType == InputValidationStatus.ILLEGAL_URL )
        {
            response.getWriter().println("<h1>Unallowed URL</h1>");
        }
        else
        {
            response.getWriter().println("<h1>Unallowed Comment</h1>");
        }
        response.getWriter().println("<a href=\"/\">Home Page</a>");
        return ;
    }

    long timestamp = System.currentTimeMillis();

    Entity memeEntity = new Entity("Memes");
    memeEntity.setProperty("url", argURL);
    memeEntity.setProperty("comment", argComment);
    memeEntity.setProperty("timestamp", timestamp);

    memeEntity.setProperty("randomIndex", Math.random());

    memeEntity.setProperty("userEmail", userService.getCurrentUser().getEmail());
    
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(memeEntity);
    
//    response.sendRedirect("/data");
//    response.sendRedirect("/index.html");

    response.setContentType("text/html;");
    response.getWriter().println("<h1>Uploaded successfully</h1>");
    response.getWriter().println("<a href=\"/\">Home Page</a>");
    return ;

  }
}
