# Projektowanie aplikacji mobilnych i webowych

Aplikacia do zarządzania bibliografią. 

## Klient

Klient to frontend pozwalający  na rejstrowanie, logowanie i wylogowywanie się użythowników. Za 
pomocą klienta powinno byc też możliwe dodawanie nowych pozycji w bibliografi jako pliki pdf jak
także ich usuwanie. Klient powinien także odpinać i przypinać pozycje bibliograficzne do dokumentów

## Server

Serwer przechouje dane o użytkownikach, ich bibliografiach a także informacje o aktywnych sesjach użythowników dzięki czemu może 
sprawdzać czy dane żądanie(np o plik pdf) może zostać obsłużone.

Serwer także po dostaniu pliku pdf musi z niego wyodrębnić dane takie jak autor i tytuł. Następnie 
po przetworzeniu pdf serwer wysyła powiadomienie do każdej zalogowanej sesji o nowej publikacji.

Do przechowywania danych wykorzystywana jest baza danych Redis

## Uruchamianie

Aby uruchomić tą aplikację wystarczy uruchomic dwa pliki jar: client.jar i server.jar. W celu uruchomienia ich jako dockery 
należy uruchomić skrypt(zakładając że użytkownik ma root a i zainstalowanego dockera):

```bash
build.sh
run.sh
```

Po wykonaniu tych dwoch komend na localhost:8080 powinna sie wyswietlić przepiękna strona tytułowa.


