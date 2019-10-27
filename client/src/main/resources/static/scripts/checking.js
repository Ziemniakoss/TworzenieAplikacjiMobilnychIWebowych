/**
 * Script listening to input changes. It will try to check automatically
 * if given username is available and if two passwords given by user
 * are the same
 */
var serverUrl = "http://172.18.0.22:42069";
// EventListener for username input changes
var usernameInput = document.getElementById("username");
console.log(usernameInput);
if(usernameInput != null){
    usernameInput.addEventListener("keyup", function(){
        //send http request and check if it is valid username
        console.log(usernameInput.value);

        var url = serverUrl+"/check/username/" + usernameInput.value;
        console.log(url);
        var xhr = new XMLHttpRequest();
        xhr.onload = function(){
        	var body = JSON.parse(xhr.responseText);
        	console.log(body["available"]);
        	if(body["available"] === true){
        		usernameInput.style.color = "#00cc00";
        	}else{
        		usernameInput.style.color ="#ff0000";
        	}
        }
        xhr.open("GET", url, true);
        xhr.send();
        console.log("Getting")
    });
}

// Checking if two passwords are the same

var passIn = document.getElementById("password");
var passInVal = document.getElementById("validatePassword");


function checkPasswords(){
	var eq = passIn.value == passInVal.value;
	console.log(eq);
	if(eq == true){
		passInVal.style.color = "#00ff00";
		passIn.style.color ="#00ff00";
	}else{
		passInVal.style.color = "#ff0000";
    	passIn.style.color ="#ff0000";
	}

}
passInVal.addEventListener("keyup", checkPasswords)
passIn.addEventListener("keyup", checkPasswords)