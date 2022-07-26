# Záróvizsga - Iskolák api

A feladatok megoldásához az IDEA fejlesztőeszközt használd!
Bármely régebbi osztályt megnyithatsz.

A meglévő exams repositoryba dolgozz!
Ezen könyvtár tartalmát nyugodtan át lehet másolni (`pom.xml`, tesztek). GroupId: `org.training360`,
artifactId: `sv2-jvjbf-zarovizsga`. Csomagnév: `org.training360.sv2jvjbfzarovizsga`.

Először másold át magadhoz a `pom.xml`-t és a teszteseteket, majd commitolj azonnal!
A vizsga végéig bárhányszor commitolhatsz.

Csak a vizsga vége előtt 15 perccel push-olhatsz először, utána push-olhatsz a vizsga végéig bármennyiszer.

Ha letelik az idő, mindenképp pusholj, akkor is, ha nem vagy kész!

## Alkalmazás

Hozz létre egy alkalmazást, amivel iskolákat és tanulókat lehet adminisztrálni.

## Nem-funkcionális követelmények

Klasszikus háromrétegű alkalmazás, MariaDB adatbázissal, Java Spring backenddel, REST webszolgáltatásokkal.

Követelmények tételesen:

* SQL adatbázis kezelő réteg megvalósítása Spring Data JPA-val (`Repository`)
* Flyway
* Üzleti logika réteg megvalósítása `@Service` osztályokkal
* Controller réteg megvalósítása, RESTful API implementálására.
* Hibakezelés, validáció
* Hozz létre egy `Dockerfile`-t!


## Általános követelmények (12 pont)

- Alkalmazás szerkezeti felépítése, következetes mappa és package szerkezet - 2 pont
- A három réteg létrehozása megfelelő, indítható, működő Spring Boot alkalmazás - 3 pont
- Dockerfile és migrációs fájlok megléte, helyessége - 2 pont
- Clean code - 5 pont


## Az adatbázisréteg elkészítése  (12 pont)

A feladatban két entitást kell elkészítened egyiket `School` a másikat `Student` néven. Fontos, hogy egy iskolának
több tanulója is lehet, de egy tanuló csak egy iskolába járhat. A kapcsolat kétirányú legyen! <br>

Iskola adatai:

* id (Long)
* schoolName (String)
* address (Address, lásd később)

Tanuló adatai:

* id (Long)
* name (String)
* dateOfBirth (LocalDate)
* schoolAgeStatus (SchoolAgeStatus enum: SCHOOL_AGED, NOT_SCHOOL_AGED; azt mutatja, hogy egy tanuló tanköteles korban van-e vagy nem.)

Address (nem entitás):
* postalCode (String)
* city (String)
* street (String)
* houseNumber (int)

Adatbázist indíthatsz a következő Docker paranccsal:

```shell
docker run -d -e MYSQL_DATABASE=schooladministration -e MYSQL_USER=school -e MYSQL_PASSWORD=school -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -p 3306:3306 --name schooladministration-mariadb mariadb
```

### Részpontszámok

- Az entitások létrehozása helyes `JPA` szabványnak megfelelő - 8 pont
- Tábla és oszlopnevek megfelelők - 4 pont

## Iskola és Tanuló mentése (30 pont)

### `POST /api/schools`

A HTTP kérés törzsében egy iskola nevét és címét (egy cím minden adatát külön) várjuk. Az azonosítót az adatbázis osztja ki, míg a tanulók lista
kezdetben üres.<br>
Validálás:

- A név nem lehet üres és nem tartalmazhat csak whitespace karaktereket

A kérésben beérkező adatokat a fenti feltételek alapján validáld le, és hiba esetén küldj vissza hibaüzenetet (a pontos
hibaüzeneteket megtalálhatod a vonatkozó teszteseteknél), valamint 400-as hibakódot!

Sikeres mentés esetén küldd vissza az elmentett iskola összes adatát (id-val és tanulókkal együtt), és 201-es
kódot!

### Részpontok

* A beérkező HTTP kérést az alkalmazás kezeli (controller) - 3 pont
* Az adatok elmentésre kerülnek - 3 pont
* Validálás és hibakezelés - 3 pont
* A válasz tartalmazza a megfelelő adatokat - 3 pont

### `POST api/schools/{id}/students`

Az iskola azonosítója az URL-ben érkezik.

A HTTP kérés törzse:

- név (nem lehet üres)
- születési dátum (csak múltbéli dátumot tartalmazhat)

A kérésben beérkező adatokat a fenti feltételek alapján validáld le, és hiba esetén küldj vissza hibaüzenetet
(a pontos hibaüzeneteket megtalálhatod a vonatkozó teszteseteknél), valamint 400-as hibakódot!

Ha nem megfelelő az iskola azonosítója, 404-es státuszkóddal térj vissza és megfelelő hibaüzenettel.

A SchoolAgeStatus a tanuló korától függ. Minden tanuló 16 éves koráig tanköteles, ez azt jelenti, hogyha a tanuló betöltötte a 16. életévét, akkor
a nem tanköteles státuszba kerül. Két dátum közötti különbséget a `ChronoUnit.YEARS.between(date1, date2)` metódussal tudsz számolni,
ami `long`-gal tér vissza.

Sikeres mentés esetén küldd vissza az iskola adatait (id-val és tanulókkal együtt) és 201-es státuszkódot!

### Részpontok

* A beérkező HTTP kérést az alkalmazás kezeli - 3 pont
* Az adatok elmentésre kerülnek, ha megfelelők - 3 pont
* Egyszerű mezők validálása és hibakezelése - 4 pont
* Nem létező iskola hibájának kezelése - 4 pont
* A tankötelezettség helyes beállítása - 2 pont
* A válasz tartalmazza a megfelelő adatokat - 2 pont

## Iskolák lekérdezése (12 pont)

### `GET /api/schools`

Lehessen az összes iskolát lekérdezni a végponton.
Opcionálisan query stringként lehessen megadni egy várost, ebben az esetben csak az abban a városban
található iskolákat kérdezzük le.

### Részpontok

* A beérkező HTTP kérést az alkalmazás kezeli (controller) - 3 pont
* Query paraméter nélkül az összes iskola lekérdezésre kerül - 3 pont
* A query paraméter esetén megfelelő szűrés történik - 3 pont
* A válasz tartalmazza a megfelelő adatokat - 3 pont

## Tanuló kirúgása (14 pont)

### `PUT /api/schools/{id}/students/{stdId}`

Egy tanulót ki lehet rúgni egy iskolából. Ekkor adatai az adatbázisban maradnak, csak nem lesz hozzá iskola rendelve.

Ha az iskola vagy a tanuló a kérés alapján nem található, küldjünk vissza 404-es hibakódot. Figyeljünk arra,
hogy a lekért tanulónak az iskola tanulói között kell szerepelnie! Ha nem így lenne, akkor is 404 hibakód megy vissza.

Sikeres frissítés után küldjük vissza az iskola összes adatát, természetesen a kirúgott tanuló már ne szerepeljen a tanulók listájában!

### Részpontok

- A beérkező HTTP kérést az alkalmazás kezeli - 3 pont
- Megfelelő hibakezelés - 4 pont
- Megfelelő frissítés  - 4 pont
- A válasz tartalmazza a megfelelő adatokat - 3 pont