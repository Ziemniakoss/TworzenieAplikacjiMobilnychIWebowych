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
   
## Zabezpieczenia na zaliczenie ochrone danych(backend)
- Wszystkie dane wejściowe są walidowane
- Hasła przechowywane w bazie są hashowane za pomocą 
[bcrypt](https://en.wikipedia.org/wiki/Bcrypt) o mocy 15(2<sup>15</sup> iteracji).
- Spowolnienie przetwarzania prośby o logowanie (poprzez hashowanie z dużą ilością iteracji)
- Kontrola siły hasła. Hasło musi mieć entropię przynajmniej 
15 i musi spełniac poniższe warunki:
    - Musi mieć przynajmniej 8 znaków
    - Musi zawierać przynajmniej jedną wielką literę
    - Musi zawierać przynajmniej jedną małą literę
    - Musi zawierać przynajmniej jedną cyfrę
    - Musi zawierać przynajmniej jeden znak specjalny
- Podczas podania niepopeawnych danych logowania nie jest podana dokładna
przyczyna odrzucenia żądania. 
   
  
