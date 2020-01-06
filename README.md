# Projektowanie aplikacji mobilnych i webowych

Aplikacia do zarządzania bibliografią. Umożliwia:
- dodawanie plików 
- pobieranie dodanych plików
- dodawanie pozycji bibliograficznych(TODO)
- usuwanie pozycji bibliograficznych(TODO)
- pokazywanie plików przypiętych do pozycji bibliograficznych

## Backend aplikacji

Przechowuje dane o użytkownikach, ich plkach i pozycjach bibliograficznych. Umożliwia autoryzację
przez JWT. JWT musi być przesyłany w nagłówku Authorization

Backend aplikacji działą pod tym [adresem](https://ziemback.herokuapp.com)

### Zabezpieczenia
#### Hashowanie haseł
Wszsytkie hasła wprowadzane do bazy danych są hashowane algorytmem BCyptr

## Strona interentowa

TODO


## Aplkacja mobilna
TODO
Aplikacja mobilna umożliwia logowanie i rejstracje oraz wyświetlanie,
dodawanie, usuwanie pozycji bibliograficznych oraz podpinanie i odpinanie pod 
te pozycje plików.

## Uruchamianie lokalne

W celu uruchomienia aplikacji lokalnie należy w głównym folderze
projektu uruchomić skrypt

```bash
start.sh
```

Po wykonaniu tych komend strona powinna działać pod tym  [adresem](http://localhost:8080)

## Swagger

Backend aplikacji jest udokumentowany w Swaggerze i dokumentacja powinna być dostęna po lokalnym uruchomieniu serwera jako
- [Swagger gui](http://localhost:42069/swagger-ui.html)
- [JSON](http://localhost:42069/v2/api-docs)

## Jak sprawdzić czy js sie wykonuje asynchronicznie

Podczas wpisywania nazwy użytkownika przy rejstracji aplikacja będzie na przy każdym wciśnięciu klawisza w polu użytkownik wysyłała
zapytanie do serwera w celu sprawdzenia czy dana nazwa użytkownika jest dostępna:
- jeżeli nie jest to nazwa użytkownika podświetli się na czerwono
- w przeciwnym wypadku będzie zielona

## Co jest dodane ponad wymagania
- Strona internetowa działająca nawet w przeglądarkach niebługujących js
- Strona internetowa wykrywa rozszerzenie pliku i jeżeli jest to np pdf to podaje pełną nazwę rozszerzenia. 
Aktualnie obsługiwane są
   - pdf
   - dokumenty Worda
   - OpenDocument(arkusze, dokumenty)
   
  
