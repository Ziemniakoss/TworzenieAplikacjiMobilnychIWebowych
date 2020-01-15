# Projektowanie aplikacji mobilnych i webowych

Aplikacia do zarządzania bibliografią. Umożliwia:
- dodawanie plików 
- pobieranie dodanych plików
- dodawanie pozycji bibliograficznych
- usuwanie pozycji bibliograficznych
- pokazywanie plików przypiętych do pozycji bibliograficznych

## Backend aplikacji

Przechowuje dane o użytkownikach, ich plkach i pozycjach bibliograficznych. Umożliwia autoryzację
przez JWT. JWT musi być przesyłany w nagłówku Authorization wg schematu:
```
Bearer jwt
```
Backend aplikacji działą pod tym [adresem](https://ziemback.herokuapp.com)

## Strona interentowa

Strona internetowa aktualnie pozwala na:
- utworzenie nowego użytkownika
- logowanie
- udostępnianie plików
- wyświetlanie inormacji o posiadnych plikach

## Aplkacja mobilna

Aplikacja mobilna umożliwia logowanie oraz listowanie bibliografii i plików

## Uruchamianie lokalne

W celu uruchomienia aplikacji lokalnie należy w głównym folderze
projektu uruchomić skrypt

```bash
start.sh
```

Po wykonaniu tych komend strona powinna działać pod tym  [adresem](http://localhost:8080)

## Swagger

Serwer aplikacji jest udokumentowany w Swaggerze. 
Dokumentacja jest dostępna pod [adresem](https://ziemback.herokuapp.com)

## Co jest dodane ponad wymagania
- Strona internetowa działająca nawet w przeglądarkach niebługujących js
- Strona internetowa wykrywa rozszerzenie pliku i jeżeli jest to np pdf to podaje pełną nazwę rozszerzenia. 
Aktualnie obsługiwane są
   - pdf
   - dokumenty Worda
   - OpenDocument(arkusze, dokumenty)
   
## Spełnionen wymagania na ochrone danych
- Wszystkie dane wejściowe są walidowane
- Hasła przechowywane w bazie są hashowane za pomocą 
[bcrypt](https://en.wikipedia.org/wiki/Bcrypt) o mocy 12(2<sup>12</sup> iteracji).
- Spowolnienie przetwarzania prośby o logowanie (poprzez hashowanie z dużą ilością iteracji)
- Kontrola siły hasła. Hasło musi mieć entropię przynajmniej 
15 i musi spełniac poniższe warunki:
    - Musi mieć przynajmniej 6 znaków
    - Musi zawierać przynajmniej jedną wielką literę
    - Musi zawierać przynajmniej jedną małą literę
    - Musi zawierać przynajmniej jedną cyfrę
    - nie może zawierać specjalnych znaków takich jak /<>{}'
- Po niepoprawnej próbie logowania podawana jest jedynie inforamcja, że dane nie były poprawne ale nie
jest stwierdzane czy to nazwa użytkownika była niepoprawna czy hasło  
