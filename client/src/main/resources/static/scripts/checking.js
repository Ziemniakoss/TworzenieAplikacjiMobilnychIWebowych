var xhr = new XMLHttpRequest();
xhr.open('GET', 'http://localhost:42069/auth/regiser', true)
//add body
xhr.send();
xhr.onload = function(){
    if(xhr.status == 200){

    }else{

    }
}