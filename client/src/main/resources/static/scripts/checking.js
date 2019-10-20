/**
 * Script listening to input changes. It will try to check automatically
 * if given username is available and if two passwords given by user
 * are the same
 */

// EventListener for username input changes
var usernameInput = document.getElementById("username");
if(username != null){
    usernameInput.addEventListener("keypress", function(){
        //send http request and check if it is valid username
        var url = "localhost:42069/check/username/" + usernameInput.value;
    });
}

// Checking if two passwords are the same




function checkUserName(userName){
    var url = "localhost:42069/check/username/"+userName;
    console.log(url);
    var xhr = new XMLHttpRequest();
    xhr.open("GET", url, true)
    xhr.onload = function(){
        console.log(xhr.responseText)
    }
    xhr.send();
}