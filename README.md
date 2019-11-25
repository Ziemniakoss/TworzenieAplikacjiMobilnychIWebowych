# Projektowanie aplikacji mobilnych i webowych

Aplikacia do zarządzania bibliografią. 

## Uruchamianie

W celu uruchomienia aplikacji należy w głównym folderze
projektu uruchomić skrypt

```bash
start.sh
```

Po wykonaniu tych komend strona powinna działać pod tym  [adresem](http://localhost:8080)

## Swagger

Backend aplikacji jest udokumentowany w Swaggerze i dokumentacja powinna być dostęna po uruchomieniu serwera jako
- [Swagger gui](http://localhost:42069/swagger-ui.html)
- [JSON](http://localhost:42069/v2/api-docs)

Dokumentacja jest też dostępna na [github pages](https://ziemniakoss.github.io/TworzenieAplikacjiMobilnychIWebowych/)

##Jak sprawdzić czy js sie wykonuje asynchronicznie

Podczas wpisywania nazwy użytkownika przy rejstracji aplikacja będzie na przy każdym wciśnięciu klawisza w polu użytkownik wysyłała
zapytanie do serwera w celu sprawdzenia czy dana nazwa użytkownika jest dostępna:
- jeżeli nie jest to nazwa użytkownika podświetli się na czerwono
- w przeciwnym wypadku będzie zielona