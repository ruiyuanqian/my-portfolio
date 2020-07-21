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

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['Hello world!', '¡Hola Mundo!', '你好，世界！', 'Bonjour le monde!'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

function AddARandomMeme(){
    
  fetch('/data?aRandomOne=true').then(response => response.json()).then((resJson) => {
    
    let container = document.getElementById('aRandomMeme-container');
    container.innerHTML = '';

    let commentUser = document.createElement('p');
    commentUser.innerText = "User: " + resJson.userEmail;

    let commentElement = document.createElement('p');
    commentElement.innerText = "Comment: " + resJson.comment;

    let imgElement = document.createElement('img');
    imgElement.src = resJson.url;

    container.appendChild(commentUser);
    container.appendChild(commentElement);
    container.appendChild(imgElement);

  });

}

function onloadHandler(){
    checkLoginStatus();
}

function checkLoginStatus(){
    
    fetch("/login").then(response => response.json()).then(
        ( resJson )=>{
            
            let container = document.getElementById('login-status');
            container.innerHTML = '';
            
            let commentHeader = document.createElement('h3');
            commentHeader.style = "background:black;color:white;";

            let commentP = document.createElement('p');
            let commentA = document.createElement('a');

            if(resJson.isLoggedIn == false)
            {
                commentHeader.innerText = 'Hello "Anonymous",';
                
                commentP.innerText = "Please login before uploading memes.";
                
                commentA.innerText = "Login"
                commentA.href = resJson.logInURL;

                document.getElementById('meme-upload-fs').disabled = true;
            }
            else
            {
                commentHeader.innerText = 'Hello "' + resJson.userEmail + '",';

                commentP.innerText = "You can upload memes now!";
                
                commentA.innerText = "Logout"
                commentA.href = resJson.logOutURL;

                document.getElementById('meme-upload-fs').disabled = false;
            }

            container.appendChild(commentHeader);
            container.appendChild(commentP);
            container.appendChild(commentA);
        }
    )

    /*
    fetch('/login').then(response => response.text()).then(
        (resText) => {
            document.getElementById('login-status').innerHTML = resText;
        }
    )
    */
    
    /*
      private bool isLoggedIn;
      private String logInURL;
      private String logOutURL;
      private String userEmail;
      */
}

window.onload = onloadHandler;

/*
function getServerData(){
    
  fetch('/data').then(response => response.text()).then((resText) => {
    document.getElementById('resText-container').innerText = resText;
  });

}

function getServerJson(){
    
  fetch('/data').then(response => response.json()).then((resJson) => {
    document.getElementById('resText-container').innerText = resJson.status;
  });
}
*/
