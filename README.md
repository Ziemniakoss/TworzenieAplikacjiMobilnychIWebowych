#Projektowanie aplikacji mobilnych i webowych

##Klient

Klient ma pozwalać na rejstrowanie, logowanie i wylogowywanie się użythowników. Za 
pomocą klienta powinnoo byc też możliwe dodawanie nowych pozycji w bibliografi jako pliki pdf jak
także ich usuwanie. Klient powinien także odpinać i przypinać pozycje bibliograficzne do dokumentów

##Server

Serwer przechowuje dane o użythownikach i ich danych takich jak:
- pozycje w bibligrafii
- nick
- hasło

Każda pozycja bibliograficzna posiada:
- unikalne id
- nazwę
- autora

Serwer przechouje także informacje o aktywnych sesjach użythowników dzięki czemu może 
sprawdzać czy dane żądanie(np o plik pdf) może zostać obsłużone.

Serwer także po dostaniu pliku pdf musi z niego wyodrębnić dane takie jak autor i tytuł. Następnie 
po przetworzeniu pdf serwer wysyła powiadomienie do każdej zalogowanej sesji o nowej publikacji.

###Dockerfile

Dockerfile jest bazowany na dostępnym na licencji GPL-2.0 [oficjalnym obrazie dockera dla MySQL Community Server ](https://github.com/docker-library/mysql/tree/6659750146b7a6b91a96c786729b4d482cf49fe6)