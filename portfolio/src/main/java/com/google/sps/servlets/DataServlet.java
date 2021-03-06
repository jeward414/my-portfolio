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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import com.google.sps.data.Comment;

@WebServlet("/data")
public class DataServlet extends HttpServlet {
  private static final Gson gson = new Gson();
  public static final String COMMENT_FIELD = "comment-field";
  private static final String NAME_FIELD = "name-field";
  private static final String DATE_FIELD = "timestampValue";
  private static final String EMAIL_FIELD = "email-field";
  public static final String COMMENT = "Comment";
  public static final String COMMENT_COUNT = "comment-count";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    final Query allComments = new Query(COMMENT).addSort(DATE_FIELD, SortDirection.DESCENDING);
    DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = dataStore.prepare(allComments);

    int numOfComments = Integer.parseInt(request.getParameter(COMMENT_COUNT));
    ArrayList<Comment> commentList = new ArrayList<Comment>();
    for (Entity entity : results.asIterable(FetchOptions.Builder.withLimit(numOfComments))) {
        final String name = (String) entity.getProperty(NAME_FIELD);
        final String text = (String) entity.getProperty(COMMENT_FIELD);
        final String email = (String) entity.getProperty(EMAIL_FIELD);
        final Date date = (Date) entity.getProperty(DATE_FIELD);

        Comment comment = new Comment(name, text, email, date);

        commentList.add(comment);
    }

    response.setContentType("application/json;");
    response.getWriter().println(convertToJsonUsingGson(commentList));
  }

  private String convertToJsonUsingGson(List<Comment> jsonData) {
      return gson.toJson(jsonData);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
      UserService userService = UserServiceFactory.getUserService();

      String name = getParameter(request, NAME_FIELD, "");
      Date timestamp = new Date();

      String comment = request.getParameter(COMMENT_FIELD);
      String email = userService.getCurrentUser().getEmail();
      Entity taskEntity = new Entity(COMMENT);
      taskEntity.setProperty(NAME_FIELD, name);
      taskEntity.setProperty(COMMENT_FIELD, comment);
      taskEntity.setProperty(EMAIL_FIELD, email);
      taskEntity.setProperty(DATE_FIELD, timestamp);
      DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
      dataStore.put(taskEntity);
      
      response.sendRedirect("/comments.html");
  }

  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}
