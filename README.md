# Projektowanie aplikacji mobilnych i webowych

Aplikacia do zarządzania bibliografią. 

## Uruchamianie

W celu uruchomienia aplikacji należy w głównym folderze
projektu uruchomić skrypt

```bash
build_n_run.sh
```

Po wykonaniu tych komend strona powinna działać pod tym  [adresem](http://localhost:8080)

Jeżeli na komputerze zainstalowany jest python3 to automatycznie po 
wystartowaniu kontenerów uruchomi się przeglądarka otwarta [stronie aplikacji](http://localhost:8080)(czasem trzeba
odświerzyć stronę)

## Zatrzymywanie i usuwanie kontenerów i ich obrazy

Aby zatrzymać i usunąć kontenery i ich obrazy należy uruchomić skrypt 

```bash
clean.sh
```

## Swagger

Backend aplikacji jest udokumentowany w Swaggerze i dokumentacja powinna być dostęna po uruchomieniu serwera jao
- [Swagger gui](http://localhost:42069/swagger-ui.html)
- [JSON](http://localhost:42069/v2/api-docs)