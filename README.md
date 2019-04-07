# Projekt Eszközök beadandó

[![Build Status](https://travis-ci.com/madam97/projekteszk.svg?branch=master)](https://travis-ci.com/madam97/projekteszk)

## Mappa tartalma

- docs: ebben a mappában vannak az oldalhoz szükséges dokumentumok
- images: képek, melyek megjelennek az oldalon
- src/backend: backend működéséhez szükséges fájlok (Java, SQL)
- src/frontend: frontend működéséhez szükséges fájlok (HTML, CSS, TypeScript)
- src/test: teszt fájlok

## Rövid leírás

Az API egy horgász webshophoz tartozó termékek adatait fogja kezelni. Ezen felül pedig lehetőség lesz területi jegy váltására is, így a horgászcikkek mellett a jegyvásárláshoz szükséges adatokat is kezelni fogjuk. Ezek a horgászvízek és a horgászegyesületek adatai. Az API mindezeken túl "hozzáfér" a MOHOSZ adatbázisához, ahol a horgászok adatait tudja majd lekérni, bizonyos ellenőrzések céljából.
A termékeket lehet belépés nélkül is böngészni, viszont a vásárláshoz regisztráció szükséges.

## Szerepkörök

- *regisztrálatlan felhasználó (ROLE_GUEST):* tud böngészni a termékek között, de a vásárláshoz regisztrálnie kell
- *regisztrált felhasználó (ROLE_USER):* tud termékeket vásárolni, illetve területi jegyet is, ha megadta a horgászkártyája számát és a rendszer ellenőrizte annak hitelességét a MOHOSZ adazbázisban; továbbá megtekintheti és törölheti a rendeléseit
- *admin felhasználó (ROLE_ADMIN):* olyan felhasználó, aki látja, módosíthatja és törölheti a termékeket, illetve azok adatait, továbbá felhasználót is törölhet

## Funkcionális követelmények


### Vásárlás / Horgászat menete

Regisztráció után a felhasználónak lehetősége van horgászcikkek és területi jegyek vásárlására.
Horgászcikk vásárlásakor kereshet terméknévre, illetve lehetősége van a termékek szűrésére típus, valamint gyártó szerint.
Egy termék vásárlása ugyanúgy történik, mint egy átlagos webshopnál.

A területi jegy vásárlása már kicsit érdekesebb.
Először is ahhoz, hogy egy ember horgászhasson, két dolgot tehet. 

Az első, hogy leteszi a horgászvizsgát, ezek után pedig
egy horgászegyesület tagjának kell lennie (akinek éves díjat fizet). Minden évben meg kell vennie az állami horgászjegyet,
amihez kap egy fogási naplót is. Ezek után vehet területi jegyet az adott vízre és horgászhat. Ha kifogott egy halat
és el is viszi azt, akkor azt be kell írnia a fogási naplóba. Minden év január végéig le kell adnia a legközelebbi horgászegyesületnél az előző évi fogási naplót, különben nem veheti meg a következő állami horgászjegyet.

A második, hogy egy internetes vizsga kitöltése után a személy jogosult ún. turista állami horgászjegy váltására.
Ez a jegy a váltás időpontjától számítva 90 napig érvényes és a horgász emellé is kap egy fogási naplót, amit vezetnie kell
és a turista állami horgászjegy lejártától számított 30 napon belül be kell küldenie a NÉBIH-nek.

Tehát területi jegy vásárlásakor a kártyaszám megadása után a rendszer a következőket ellenőrzi:
- a megadott kártyaszám megtalálható-e a MOHOSZ adatbázisában
- ha igen, akkor a megadott kártya érvényes-e még
- van-e a horgásznak érvényes állami horgászjegye (fishing_license_num not null)
- van-e a horgásznak valami kihágása, ami miatt az adott évben nem horgászhat (is_disabled)

Ha mindezeket rendben találta, akkor a felhasználó jogosult területi jegy vásárlására.


### SQL táblák

![SQL Tables](https://github.com/madam97/projekteszk/blob/master/images/documentation/db.jpg "SQL táblák")

#### Products

Ebben a táblában tároljuk a horgász termékeket.
Itt érdemes szót ejteni a a típusról, ami a következők lehetnek:
bot, csali, horog, műcsali, orsó, zsinór, ruha, egyéb kiegészítő

| Oszlopnév        | Típus           | Leírás              |
| ---------------- |:---------------:| -------------------:|
| id               | int             | elsődleges kulcs    |
| name             | varchar         | a termék neve       |
| type             | varchar         | a termék típusa     |
| manufacturer     | varchar         | a termék gyártója   |
| price            | int             | a termék ára        |
| desc             | varchar         | a termék leírása    |


#### Order

Ebben a táblában tároljuk egy felhasználó rendeléseit.
Külön van választva a horgászcikk vásárlás és a területi jegy vásárlás, mert a területi jegy vásárláshoz a regisztráció során megadott alapadatokon kívül meg kell adni a horgászkártya számát is. Ugyanis 2019-től a hagyományos papír alapú horgászihazolványt felváltotta a horgászkártya. A kártyaszám megadása után a rendszer leellenőrzi a MOHOSZ adatbázisában, (Mohosz tábla) hogy a megadott kártyaszám regisztrálva van-e a rendszerbe, illetve, hogy minden adott-e ahhoz, hogy a horgász felhasználó területi jegyet vehessen.

| Oszlopnév        | Típus           | Leírás                                      |
| ---------------- |:---------------:| -------------------------------------------:|
| id               | int             | elsődleges kulcs                            |
| user_id          | int             | a felhasználó ID-je                         |
| product_id       | int             | a megvásárolni kívánt termék ID-je          |
| ticket_id        | int             | a megvásárolni kívánt területi jegy ID-je   |
| start            | date            | területi jegy érvényességének kezdete       |
| end              | date            | területi jegy érvényességének vége          |


#### Tickets

Ebben a táblában tároljuk a területi jegyeket és azok adatait.
Minden területi jegy egy adott vízterülethez tartozik és a típusa határozza meg az érvényességi idejét.
Vannak napijegyek, hetijegyek, illetve éves jegyek. Mindezekből pedig van általános és turista.
Vásárláskor a felhasználó megadhatja a kezdődátumot, hogy mikortól kezdődjön a jegy érvényessége.


| Oszlopnév        | Típus           | Leírás                                    |
| ---------------- |:---------------:| -----------------------------------------:|
| id               | int             | elsődleges kulcs                          |
| spot_id          | int             | a vízterület ID-je, ahova szól a jegy     |
| type             | varchar         | a jegy típusa                             |
| price            | int             | a jegy ára                                |


#### User

Ebben a táblában tároljuk a felhasználó adatait.
Regisztrációkor a felhasználónak kötelezően meg kell adnia a nevét, e-mail címét (ez lesz a felhasználóneve) és egy jelszót.
Amikor terméket akar vásárolni, akkor szükséges lesz még megadnia a telefonszámát és a címét.
Továbbá ha területi jegyet is szeretne vásárolni, akkor meg kell adnia a horgászkártyájának a számát is.
Ezeket az adatokat nem kötelező a regisztrációkor megadni, de a vásárláshoz elengedhetetlenül szükségesek.

| Oszlopnév        | Típus           | Leírás                                      |
| ---------------- |:---------------:| -------------------------------------------:|
| id               | int             | elsődleges kulcs                            |
| card_id          | int             | horgászkáryta ID-je (száma)                 |
| name             | varchar         | a felhasználó születési neve                |
| email            | varchar         | a felhasználó e-mail címe (felhasználóneve) |
| pass             | varchar         | a belépéshez szükséges jelszó               |
| role             | enum            | regisztrálatlan felhasználó (ROLE_GUEST), regisztrált felhasználó (ROLE_USER), vagy admin felhasználó (ROLE_ADMIN)             |
| phone            | varchar         | a felhasználó telefonszáma                  |
| address          | varchar         | a felhasználó lakcíme                       |


#### Spots

Ebben a táblában tároljuk a vízterületeket és azok adatait.
Területi jegy vásárlásakor a felhasználó engedélyt kap az adott vízterületen történő horgászatra meghatározott időre.
Minden egyes vízterület egy horgászegyesület birtokában van, ők tartjűk fenn azt (terület gondozása, halak betelepítése, stb.).

| Oszlopnév        | Típus           | Leírás                                    |
| ---------------- |:---------------:| -----------------------------------------:|
| id               | int             | elsődleges kulcs (víztérkód)              |
| club_id          | int             | a horgászegyesület ID-je (tulajdonos)     |
| name             | varchar         | a vízterület neve                         |
| desc             | varchar         | a vízterület leírása                      |


#### Clubs

Ebben a táblában tároljuk a horgászegyesületeket és azok adatait.
Minden horgászegyesületnek van egy neve és egy megye, ahol az egyesület megtalálható.
A horgászvízekhez nem rendeltünk külön megye adattagot, mert a szimuláció során azt feltételezzük,
hogy a horgászegyesület a saját megyéjén belül birtokol csak horgászvízeket.

| Oszlopnév        | Típus           | Leírás                                   |
| ---------------- |:---------------:| ----------------------------------------:|
| id               | int             | elsődleges kulcs                         |
| name             | varchar         | a horgászegyesület neve                  |
| shire            | varchar         | a megye, ahol az egyesület található     |


#### Mohosz

Ez a tábla szimulálja az együttműködést a Magyar Országos Horgász Szövetséggel (MOHOSZ).
Ők tárolják az összes horgász legfontosabb adatait. Úgy mint a horgász nevét, kártyaszámát,
a kártya kiállításának és érvényességének dátumát, az állami, vagy turista horgászjegy számát,
illetve hogy volt-e valamilyen kihágása, ami miatt nem vehet állami vagy turista horgászjegyet,
illetve területi jegyet. Ez egy "beégetett adatokkal" rendelkező tábla lesz, az API-ban csak lekérdezni
tudjuk ezen adatokat, módosítani nem, hisz az adatkezelő nem az admin felhasználó, hanem a MOHOSZ.

| Oszlopnév              | Típus           | Leírás                                                   |
| ---------------------- |:---------------:| --------------------------------------------------------:|
| card_id                | int             | elsődleges kulcs, a horgászkártya ID-je                  |
| club_id                | int             | a horgászegyesület ID-je, aminek tagja a horgász         |
| name                   | varchar         | a horgász neve                                           |
| card_issue             | date            | a horgászhoz tartozó horgászkártya kiállításának ideje   |
| card_expiration        | date            | a horgászhoz tartozó horgászkártya lejáratának ideje     |
| fishing_license_numb   | int             | állami vagy turista horgászjegy száma                    |
| is_disabled            | bool            | van-e a horgásznak kihágása                              |


### API végpontok

Minden API végpont alakja **/api/{táblanév-kicsi-betűkkel}/** szöveg, melyet után a lenti táblázatok *Végpont* oszlopában lévő szövegek követnek. A *Szükséges engedély* oszlopban az adott végponthoz szükséges minimális jogosultság van megjelölve.

#### Products

| Végpont           | Típus  | Szükséges engedély | Leírás                                                   | 
| ----------------- |:------:| ------------------ | -------------------------------------------------------- |
| *semmi*           | GET    | ROLE_GUEST         | Visszaadja az összes terméket                            |
| *semmi*           | POST   | ROLE_ADMIN         | A megadott terméket felviszi az adatbázisba              |
| {id}              | DELETE | ROLE_ADMIN         | Kitörli a megadott id-jű terméket                        |

#### Order

| Végpont           | Típus  | Szükséges engedély | Leírás                                                   | 
| ----------------- |:------:| ------------------ | -------------------------------------------------------- |
| *semmi*           | GET    | ROLE_ADMIN         | Visszaadja az összes rendelést                           |
| {user}            | GET    | ROLE_USER          | Visszaadja a felhasználó rendeléseitű                    |
| *semmi*           | POST   | ROLE_USER          | A megadott rendelést felviszi az adatbázisba             |
| {id}              | DELETE | ROLE_USER          | Kitörli a megadott id-jű rendelést                       |

#### Tickets

| Végpont           | Típus  | Szükséges engedély | Leírás                                                   | 
| ----------------- |:------:| ------------------ | -------------------------------------------------------- |
| *semmi*           | GET    | ROLE_USER          | Visszaadja az összes jegyet                              |
| *semmi*           | POST   | ROLE_ADMIN         | A megadott jegyet felviszi az adatbázisba                |
| {id}              | DELETE | ROLE_ADMIN         | Kitörli a megadott id-jű jegyet                          |

#### User

| Végpont           | Típus  | Szükséges engedély | Leírás                                                   | 
| ----------------- |:------:| ------------------ | -------------------------------------------------------- |
| {id}              | GET    | ROLE_GUEST         | Visszaadja az adott id-jű felhasználót                   |
| *semmi*           | GET    | ROLE_ADMIN         | Visszaadja az összes felhasználót                        |
| register          | POST   | ROLE_ADMIN         | A megadott felhasználót felviszi az adatbázisba          |
| login             | POST   | ROLE_ADMIN         | A megadott felhasználót belépteti                        |
| {id}/to-admin     | PATCH  | ROLE_ADMIN         | A felhasználót adminná teszi                             |
| {id}              | PUT    | ROLE_ADMIN         | A felhasználó adatait frissíti a megadott adatokkal      |
| {id}              | DELETE | ROLE_ADMIN         | Kitörli a megadott id-jű felhasználót                    |

#### Spots

| Végpont           | Típus  | Szükséges engedély | Leírás                                                   | 
| ----------------- |:------:| ------------------ | -------------------------------------------------------- |
| {id}              | GET    | ROLE_GUEST         | Visszaadja az adott id-jű vízterületet                   |
| {name}            | GET    | ROLE_QUEST         | Visszaadja az adott NEVŰ vízterületet                    |
| *semmi*           | GET    | ROLE_ADMIN         | Visszaadja az összes vízterületet                        |
| *semmi*           | POST   | ROLE_ADMIN         | A megadott vízterületet felviszi az adatbázisba          |
| {id}              | PUT    | ROLE_ADMIN         | A vízterület adatait frissíti a megadott adatokkal       |
| {id}              | DELETE | ROLE_ADMIN         | Kitörli a megadott id-jű vízterületet                    |

#### Clubs

| Végpont           | Típus  | Szükséges engedély | Leírás                                                   | 
| ----------------- |:------:| ------------------ | -------------------------------------------------------- |
| {id}              | GET    | ROLE_GUEST         | Visszaadja az adott id-jű horgászegyesületet             |
| {name}            | GET    | ROLE_QUEST         | Visszaadja az adott NEVŰ horgászegyesület                |
| *semmi*           | GET    | ROLE_ADMIN         | Visszaadja az összes horgászegyesület                    |
| *semmi*           | POST   | ROLE_ADMIN         | A megadott horgászegyesületet felviszi az adatbázisba    |
| {id}              | PUT    | ROLE_ADMIN         | A horgászegyesület adatait frissíti a megadott adatokkal |
| {id}              | DELETE | ROLE_ADMIN         | Kitörli a megadott id-jű horgászegyesületet              |

#### Mohosz

| Végpont           | Típus  | Szükséges engedély | Leírás                                                   | 
| ----------------- |:------:| ------------------ | -------------------------------------------------------- |
| {id}              | GET    | ROLE_USER          | Visszaadja az adott id-jű Mohosz felhasználó adatot      |
| *semmi*           | GET    | ROLE_ADMIN         | Visszaadja az összes felhasználó adatot a Mohosztól      |
