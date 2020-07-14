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

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

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

    Query query = new Query("Memes").addSort("timestamp", SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    response.setContentType("text/html;");

    for (Entity entity : results.asIterable()) {
      long id = entity.getKey().getId();
      String argURL = (String) entity.getProperty("url");
      String argComment = (String) entity.getProperty("comment");
      long timestamp = (long) entity.getProperty("timestamp");
      
      response.getWriter().println("\n <p> ### ### ### </p>\n");
      response.getWriter().println("\n <p> ID: " + id + "</p>");
      response.getWriter().println("\n <p> Timestamp: " + DateFormat.getDateTimeInstance().format(timestamp) + "</p>");
      response.getWriter().println("\n <p> Comment: " + argComment + "</p>");
      response.getWriter().println("\n <img src=\"" + argURL + "\"></img>");
    }
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
//    String text = request.getParameter("text-input");
//    response.setContentType("text/html;");
//    response.getWriter().println("echo : " + text);

    String argURL = request.getParameter("url-input");
    String argComment = request.getParameter("comment-input");
    long timestamp = System.currentTimeMillis();

    Entity memeEntity = new Entity("Memes");
    memeEntity.setProperty("url", argURL);
    memeEntity.setProperty("comment", argComment);
    memeEntity.setProperty("timestamp", timestamp);
    
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(memeEntity);
    
//    response.sendRedirect("/data");
    response.sendRedirect("/index.html");
  }
}
