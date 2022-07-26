# Képesítővizsga - Sportteljesítmények api

A feladatok megoldásához az IDEA fejlesztőeszközt használd!
Bármely régebbi osztályt megnyithatsz.

A vizsga repository-ba dolgozz, a projekt neve legyen `sv2-jvjbf-kepesitovizsga`!
Ezen könyvtár tartalmát nyugodtan át lehet másolni (`pom.xml`, tesztek). GroupId: `training360`,
artifactId: `sv2-jvjbf-kepesitovizsga`. Csomagnév: `training360`.

Először másold át magadhoz a `pom.xml`-t és a teszteseteket, majd commitolj azonnal!
A vizsga végéig bárhányszor commitolhatsz.

Csak a vizsga vége előtt 15 perccel push-olhatsz először, utána push-olhatsz a vizsga végéig bármennyiszer.

Ha letelik az idő, mindenképp pusholj, akkor is, ha nem vagy kész!

## Alkalmazás

Hozz létre egy alkalmazást, amivel sprtolókat és eredményeket lehet kezelni!

## Nem-funkcionális követelmények

Klasszikus háromrétegű alkalmazás, MariaDB adatbázissal, Java Spring Boot backenddel, REST webszolgáltatásokkal.

Követelmények tételesen:

* SQL adatbázis kezelő réteg megvalósítása Spring Data JPA-val (`Repository`)
* Flyway
* Üzleti logika réteg megvalósítása `@Service` osztályokkal
* Controller réteg megvalósítása, RESTful API implementálására.
* Hibakezelés, validáció
* Hozz létre egy `Dockerfile`-t!

## Az alkalmazás szerkezeti felépítése  (20 pont)

A feladatban két entitást kell elkészítened, egyiket `Athlete`, a másikat `Result` néven. Fontos, hogy egy sportolónak
több eredménye is lehet, de egy eredmény csak egy sportolóhoz tartozhat. A kapcsolat kétirányú legyen! <br>

Sportoló adatai:

* id (Long)
* name (String)
* sex (Sex enum: MALE, FEMALE)

Eredmény adatai:

* id (Long)
* resultDate (LocalDate)
* sportType (SportType enum: SWIMMING, RUNNING, POLE_VAULT, HAMMER_THROWING)
* measure (double)
* measureUnit(char)


`SportType` enum:
Minden sport típusnak legyen egy `char measureUnit` adattagja, ami a sportág eredményének mértékenységét mutatja.

* Úszás, Futás: 's'
* Rúdugrás, Kalapácsvetés: 'm'

Az eredmény esetében a mértékegységet a sport típus segítségével kell beállítani!<br>

Háromrétegű Spring Boot webalkalmazást készíts!

Adatbázist indíthatsz a következő Docker paranccsal:

```shell
docker run -d -e MYSQL_DATABASE=sportresults -e MYSQL_USER=sportresults -e MYSQL_PASSWORD=sportresults -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -p 3306:3306 --name sportresults-mariadb mariadb
```

A feladatleírást olvasd el részletesen, és nézd meg az egyes részfeladatokhoz tartozó teszteseteket is, hogy milyen
inputra mi az elvárt viselkedése az alkalmazásnak!

### Részpontszámok

- Alkalmazás szerkezeti felépítése, következetes mappa és package szerkezet - 2 pont
- Az entitások létrehozása helyes, `JPA` szabványnak megfelelő - 6 pont
- A három réteg létrehozása megfelelő, indítható, működő Spring Boot alkalmazás - 3 pont
- Dockerfile és migrációs fájlok megléte, helyessége - 4 pont
- Clean code - 5 pont

## Sportoló mentése (12 pont)

### `POST /api/athletes`

A HTTP kérés törzsében egy sportoló nevét és nemét várja. Az azonosítót az adatbázis osztja ki, míg az eredmények lista
kezdetben üres.<br>
Validálás:

- A név nem lehet üres és nem tartalmazhat csak whitespace karaktereket

A kérésben beérkező adatokat a fenti feltételek alapján validáld le, és hiba esetén küldj vissza hibaüzenetet (a pontos
hibaüzeneteket megtalálhatod a vonatkozó teszteseteknél), valamint 400-as hibakódot!

Sikeres mentés esetén küldd vissza az elmentett sportoló összes adatát (id-val és eredményekkel együtt), és 201-es
kódot!

### Részpontok

* A beérkező HTTP kérést az alkalmazás kezeli (controller) - 3 pont
* Az adatok elmentésre kerülnek - 3 pont
* Validálás és hibakezelés - 3 pont
* A válasz tartalmazza a megfelelő adatokat - 3 pont

## Eredmény mentése (17 pont)

### `POST /api/athletes/{id}/results`

A sportoló azonosítója az URL-ben érkezik.

A HTTP kérés törzse:

- hely (nem lehet üres)
- eredmény dátuma(Csak múlt vagy jelen idejű dátumot tartalmazhat)
- sport típusa
- mérték (csak pozitív lehet)

A kérésben beérkező adatokat a fenti feltételek alapján validáld le, és hiba esetén küldj vissza hibaüzenetet
(a pontos hibaüzeneteket megtalálhatod a vonatkozó teszteseteknél), valamint 400-as hibakódot!

Ha nem megfelelő a sportoló azonosítója, 404-es státuszkóddal térj vissza és megfelelő hibaüzenettel!

Sikeres mentés esetén küldd vissza a sportoló adatait (id-val és eredményekkel együtt) és 201-es státuszkódot!

### Részpontok

* A beérkező HTTP kérést az alkalmazás kezeli - 3 pont
* Az adatok elmentésre kerülnek - 3 pont
* Egyszerű mezők validálása és hibakezelése - 4 pont
* Nem létező sportoló hibájának kezelése - 4 pont
* A válasz tartalmazza a megfelelő adatokat - 3 pont

## Eredmény frissítése (14 pont)

### `PUT /api/results/{id}`

Előfordulhat, hogy eredményt megóvnak, vagy később büntetés vagy valami hiba miatt változtatják az eredmény mértékét. A
kérés URL-ben érkezik be az eredmény azonosítója és a törzsben pedig egy új mérték.<br>

Ha nem megfelelő az `id` akkor 404-es státuszkóddal térj vissza és egy megfelelő hibaüzenettel! Egyébként frissítsd
az eredményt a megfelelő értékre és térj vissza a frissített eredménnyel!

### Részpontok

* A beérkező HTTP kérést az alkalmazás kezeli (controller) - 3 pont
* Nem létező eredmény kezelése - 3 pont
* Az adatok frissülnek - 3 pont
* Validálás és hibakezelés - 2 pont
* A válasz tartalmazza a megfelelő adatokat - 3 pont

## Eredmények rangsorolt lekérdezése (17 pont)

### `GET /api/results`

A kérés alapértelmeztten visszaadja az összes eredményt, ami az adatbázisban található.<br>

A kérésnél query paraméterként beérkezhet egy `SportType` ezzel csak a megfelelő sport eredményeit adjuk vissza
eredmények sorrendjében. Figyelj arra, hogyha a mértékegység másodperc, akkor a legjobb eredmény a legkisebb, ha méter,
akkor a legjobb eredmény a legnagyobb!
A válaszban egy eredmény következő adatait várjuk:

* eredmény azonosítója
* sportoló neve
* eredmény helyszíne
* eredmény dátuma
* eredmény mértéke

### Részpontok

- A beérkező HTTP kérést az alkalmazás kezeli - 3 pont
- A query paraméter megfelelően van beállítva és kezelve - 3 pont
- Query paraméter hiánya esetén megfelelő adatokat küldd vissza- 3 pont
- Query paraméter megléte esetén megfelelő adatokat küldd vissza - 4 pont
- Query paraméter megléte esetén megfelelő sorrendben küldi vissza az adatokat - 4 pont




