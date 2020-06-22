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

window.onload = function() {
    loginStatus();
    drawChart();
}

/**
 * Adds a random greeting to the page.
 */
function addRandomSaying() {
  const sayings =
      ['Real Sneaky Beaky Like', 'Forever Cozy', 'That\'s craaaazy', 'Bet'];

  const quote = sayings[Math.floor(Math.random() * sayings.length)];

  const quoteContainer = document.getElementById('quote-container');
  quoteContainer.innerText = quote;
}


var elements = document.getElementsByClassName("column");

function showFullWidthImage() {
    for (var i = 0; i < elements.length; i++) {
    elements[i].style.msFlex = "100%";
    elements[i].style.flex = "100%";
  }
}

function showTwoImages() {
  for (var i = 0; i < elements.length; i++) {
    elements[i].style.msFlex = "35%";
    elements[i].style.flex = "35%";
  }
}

function showFourImages() {
    for (var i = 0; i < elements.length; i++) {
    elements[i].style.msFlex = "15%";
    elements[i].style.flex = "15%";
  }
}

function retrieveComments() {
    fetch('/data?' + 'comment-count=' + document.getElementById("comment-count").value).then(response => response.json()).then(commentList => {
    addComments(commentList);
  });
}

function addComments(commentList) {
    const commentListElement = document.getElementById("comment-container");
    while (commentListElement.firstChild) {
        commentListElement.removeChild(commentListElement.firstChild);
    }

    for (comment of commentList) {
        commentListElement.appendChild(createCommentElement(comment));
    }   
}

function createCommentElement(comment) {
    let commentElement = document.createElement('div');
    commentElement.classList.add("comment");

    let userInfo = document.createElement('div');
    userInfo.classList.add("textrow");
    commentElement.appendChild(userInfo);

    let userEmail = document.createElement('p');
    userEmail.classList.add("textcolumn");
    userEmail.innerText = comment.email;
    userInfo.appendChild(userEmail);

    let userName = document.createElement('p');
    userName.classList.add("textcolumn");
    userName.innerText = comment.name;
    userInfo.appendChild(userName);

    let commentText = document.createElement('p');
    commentText.innerText = comment.text;
    commentElement.appendChild(commentText);

    let timeStamp = document.createElement('p');
    timeStamp.classList.add("right")
    timeStamp.innerText = comment.date;
    commentElement.appendChild(timeStamp);

    return commentElement;
}

function deleteComments() {
    const request = new Request("/delete-data", {method: "POST"});
    const fetchDelete = fetch(request);
    fetchDelete.then(retrieveComments);
}

function loginStatus() {
    console.log("Checking login status");

    fetch("/loginStatus").then(response => response.json()).then(result => {

        let loginButton = document.getElementById("login-button");
        let logoutButton = document.getElementById("logout-button");
        let commentField = document.getElementById("comment-field");
        let commentPrompt = document.getElementById("comment-prompt");

        if(result.loggedIn) {
            console.log("logged in");

            loginButton.classList.add("hidden");
            commentPrompt.classList.add("hidden");

            let logoutURL = document.getElementById("logoutURL");
            logoutURL.href = result.redirect;
            logoutButton.classList.remove("hidden");
            commentField.classList.remove("hidden");

        } else {
            console.log("not logged in");

            logoutButton.classList.add("hidden");
            commentField.classList.add("hidden");

            let loginURL = document.getElementById("loginURL");
            loginURL.href = result.redirect;
            loginButton.classList.remove("hidden");
            commentPrompt.classList.remove("hidden");

        } 
    });
}

/**
 * Opens side menu
 */
function openNav() {
    document.getElementById('nav-bar').style.display = "inline-flex";
    document.getElementById('nav-tab').style.display = "hidden";
}

/**
 * Closes side menu
 */
function closeNav() {
    document.getElementById('nav-bar').style.display = "none";
}


google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawChart);

/** Creates a chart and adds it to the page. */
function drawChart() {
  const data = new google.visualization.arrayToDataTable([
        ['Color', 'Votes', { role: 'style' } ],
          ['Red', 10, '#ff0000'],
          ['Orange', 5, '#ffa500'],
          ['Yellow', 15, '#ffff00'],
          ['Green', 25, '#008000'],
          ['Blue', 45, '#0000ff'],
          ['Indigo', 8, '#4b0082'],
          ['Violet', 15, '#ee82ee'],
        ]);

  const options = {
    'title': 'Favorite Colors of the Rainbow',
    'width':500,
    'height':500
  };

  const view = new google.visualization.DataView(data);

  const chart = new google.visualization.ColumnChart(
      document.getElementById("favorite-color-container"));
  chart.draw(view, options);
}