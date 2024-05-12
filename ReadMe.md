# DodgeBall

A feladat egy labdás kidobós játék szimulációja. A játék egy rögzített méretű, 5x5-ös teremben történik, ahol 10 játékos van, és egy labda. A játékosok feladata, hogy a labda segítségével kidobják egymást. Akit kidobtak, az kiesik a játékból. A végén csak egy játékos maradhat, ő lesz a győztes. Az alapfeladatban elég, ha az egész termet zároljuk, amikor abban a játékosokat vagy a labdát mozgatni szetenénk.

A megvalósításhoz a következő osztályokat használd.

---

`Game`: ez az osztály felelős a szimuláció lefuttatásáért.

- Tartalmazza a terem dimenzióit és a játékosok számát tartalmazó konstansokat, valamit egy további konstanst, amely meghatározza, hogy mennyit kell várni két kirajzolás között.
  _(Ez legyen 50 ms.)_
- Ezen kívül tartalmaz egy `main()` metódust, ami az alábbiakat teszi:
  1. Létrehozza a termet.
  2. Létrehozza a labdát, elhelyezve azt a terem bármely pontján.
  3. Létrehozza és elindítja a játékosokat. (**‘A’**-tól kezdve legyenek elnevezve!)
  4. Amíg több, mint egy játékos van játékban, megfelelő időközönként _(emlékeztetőül: 50 ms)_ kirajzoltatja a terem állapotát.
  5. Amint csak egy játékos marad, kiírja a győztes betűjelét, majd őt is leállítja.

---

`Room`: a játékosok és a labda helyzetét ábrázolja.

- Tartalmazza a szélességét és a hosszúságát, amit a konstruktor paramétereként kell átadni.
- Tárol egy objektumokból álló mátrixot, amelyben azt tartjuk nyilván, hogy az adott pozíción játékos van, labda, vagy semmi sincs.
- Tárolja a pályán levő játékosok számát.
- A konstruktora inicializálja a mátrixot, és feltölti üres helyet ábrázoló objektumokkal.
- Van egy lekérdező metódusa a szélességének és a hosszúságának a lekérdezésére.
- Van egy kirajzoló metódusa, amely minden egyes pozícióra vagy szóközt ír ki (üres hely), vagy egy o karaktert, ha ott labda van, vagy pedig egy játékos betűjelét.
  - A látványosság érdekében érdemes az összes kiírás előtt a képernyőt törölni egy `"\033[H\033[2J"` karakterlánc kiírásával, majd minden kiírás előtt a kurzort a bal felső sarokba mozgatni a `"\u001B[0;0H"` kiírásával.
- Le lehet kérdezni egy adott pozíción található objektumot.
- Le lehet kérdezni az aktív játékosok számát.
- Le lehet kérdezni a labda pozícióját.
- El lehet helyezni egy objektumot egy adott pozícióra. (Nem ennek az osztálynak a feladata annak ellenőrzése, hogy az adott pozíció üres-e.) Ha az objektum játékos, akkor növekszik az aktív játékosok száma.
- Át lehet mozgatni egy objektumot egyik pozícióról egy másikra. (Nem ennek az osztálynak a feladata annak ellenőrzése, hogy az új pozíció üres-e.)
- El lehet távolítani egy objektumot egy adott pozícióról. Ha az objektum játékos, akkor csökken az aktív játékosok száma.

---

`Player`: egy játékos viselkedését szimuláló szál.

- Tartalmaz egy konstanst, amely meghatározza, hogy két mozgás között mennyit kell várnia.
  _(Ez legyen 100 ms.)_
- Tárol egy referenciát a teremre.
- Tárolja, hogy aktív-e a játékos, vagy már kiesett.
- A konstruktora átvesz egy nevet (karakterlánc) és a termet, és beállítja őket.
- A játékos viselkedésének lényege a `run()` metódusban van:
  - Tartalmazza a játékos koordinátáit, amelyeket az elején véletlenszerűen sorsol ki úgy, hogy az adott ponton még ne legyen játékos, és el is helyezi magát a teremben.
  - Amíg a játékos **`aktív`**:
    - Vár meghatározott ideig. _(Emlékeztető: 100 ms)_
    - Megpróbál elmozdulni a teremben valamelyik irányba a 8 közül egy lépést. (Semmiképp ne maradjon helyben!)
    - Ha nem sikerül elmozdulnia, mert az adott helyen fal van, akkor újrakezdi a várakozást.
    - Szintén újrakezdi a várakozást, ha azért nem sikerül elmozdulnia, mert az adott pozíció nem szabad.
    - Miután elmozdult, megnézi, hogy valamelyik irányban tőle (most elég csak a 4 fő irányt vizsgálni) található-e labda, és az nem mozog-e.
    - Ha talált labdát, ami nem mozog, akkor eldobja a lehetséges 7 irány közül az egyikbe. (A 8. irányba, vagyis saját maga felé nem dobja el.)
      Dobás után ha tud, lép még egyet a dobás irányával ellentétben.
    - Ha a játékos már nem aktív (kidobták, vagy győzött), akkor távolítsa el magát a teremből.
- Van egy metódus, amelyet a labda hív meg, amikor kidobják a játékost, valamint ugyanezt használhatja a főprogram is, hogy a győztest leállítsa.
- Érdemes `toString()` metódust is átírni, hogy a játékos nevét írja ki, megkönnyítve a terem kirajzoló metódusát.

---

`Ball`: a labda mozgást szimuláló szál.

- Tartalmaz egy konstanst, amely meghatározza, hogy két lépés között mennyit kell várnia.
  _(Ez legyen 50 ms.)_

- Tárol egy referenciát a teremre.
- Tartalmazza a labda koordinátáit, amelyeknek kezdeti értékét a konstruktor állítja be.
- Tartalmazza a mozgás irányát (pl. szintén két koordináta segítségével).
- A konstruktora beállítja a termet és a kezdeti koordinátákat.
- A `run()` metódusa:
  - Amíg a teremben több, mint egy játékos van:
    - Vár meghatározott ideig. _(Emlékeztető: 50 ms)_
    - Ha a labda mozgásban van, de elérte a falat, akkor megáll a mozgása.
    - Ha a labda mozgásban van, akkor megvizsgálja, hogy az irányában van-e játékos.
    - Ha van játékos, akkor kidobja, és megáll a mozgása.
    - Ellenkező esetben a labda elmozdul a teremben.
- Van egy metódus, aminek segítségével el lehet dobni a labdát egy adott irányba. Ez a metódus annyit csinál, hogy beállítja a mozgás irányát jelző adattagokat.
- Egy másik metódussal lekérdezhető, hogy mozgásban van-e a labda.
- Érdemes `toString()` metódust is átírni, hogy egy o karaktert írjon ki, megkönnyítve a terem kirajzoló metódusát.

---

`Empty`: üres mező

- Ez az osztály csak azért jobb, mint a sima `Object()`, mert át tudjuk írni a `toString()` metódusát, hogy egy szóközt írjon ki.
