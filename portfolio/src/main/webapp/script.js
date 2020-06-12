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
    fetch('/data?' + 'comment-count=' + document.getElementById("comment-count").value).then(response => response.text()).then((message) => {
    document.getElementById('comment-container').innerText = message;
  });
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

        if(result.loggedIn) {
            console.log("logged in");

            loginButton.classList.add("hidden");

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