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
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
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
import com.google.sps.commentdata.Comment;

@WebServlet("/data")
public class DataServlet extends HttpServlet {
  private static final Gson gson = new Gson();
  private static final String COMMENT_FIELD = "comment-field";
  private static final String NAME_FIELD = "name-field";
  private static final String DATE_FIELD = "timestampValue";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    final Query allComments = new Query("Comment");
    DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = dataStore.prepare(allComments);

    List<Comment> commentList = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
        final String name = (String) entity.getProperty(NAME_FIELD);
        final String text = (String) entity.getProperty(COMMENT_FIELD);
        final Date date = (Date) entity.getProperty(DATE_FIELD);

        Comment comment = new Comment(name, text, date);

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
      StringBuffer sb = new StringBuffer();
      String name = getParameter(request, NAME_FIELD, "");
      String text = getParameter(request, COMMENT_FIELD, "");
      Date timestamp = new Date();

      String comment = request.getParameter("comment-field");
      Entity taskEntity = new Entity("Comment");
      taskEntity.setProperty(NAME_FIELD, name);
      taskEntity.setProperty(COMMENT_FIELD, comment);
      taskEntity.setProperty(DATE_FIELD, timestamp);
      DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
      dataStore.put(taskEntity);
      
      response.sendRedirect("/index.html");
  }

  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}
