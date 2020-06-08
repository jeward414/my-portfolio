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

@WebServlet("/data")
public class DataServlet extends HttpServlet {
  private final ArrayList<String> comments = new ArrayList<String>();
  private static final Gson gson = new Gson();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    final Query allComments = new Query("Comment");
    DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = dataStore.prepare(allComments);

    ArrayList<String> commentList = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
        final String comment = (String) entity.getProperty("comment-field");

        commentList.add(comment);
    }

    response.setContentType("application/json;");
    response.getWriter().println(convertToJsonUsingGson(commentList));
  }

  private String convertToJsonUsingGson(ArrayList<String> jsonData) {
      return gson.toJson(jsonData);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
      StringBuffer sb = new StringBuffer();
      String text = getParameter(request, "comment-field", "");

      String[] words = text.split(" ");
      
      for (String txt:words) {
          comments.add(txt);
      }

      for (String s : comments) {
          sb.append(s);
          sb.append(" ");
      }

      String comment = request.getParameter("comment-field");
      Entity taskEntity = new Entity("Comment");
      taskEntity.setProperty("comment-field", comment);
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
