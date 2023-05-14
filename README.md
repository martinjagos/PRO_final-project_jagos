# PrintEvidence (PRO_final-project_jagos)
## I. Úvod
Desktopová aplikace **PrintEvidence** napsaná v jazyce Java, která vznikla jako záverečný projekt II. ročníku v předmětu programování na škole Obchodní Akademie Uherské Hradiště, slouží k evidenci objednávek 3D tisku a ke kontrole stavu filamentů. Aplikace umožňuje spravovat objednávky, sledovat stav zásob materiálu, vypočítání minimálních nákladů objednávky a další. Projekt vznikl i také díky mému koníčku - 3D tisk a modelování 3D modelů.

Autorem projektu je **Martin Jagoš**.
## II. Požadavky na spoštění aplikace
Aplikaci můžete spustit jako JAR soubor nebo jako projekt Java. Pro spuětění jak JAR souboru, tak i samotného kódu, je potřeba verze JDK 20. Komprimovanou složku se souborem JAR naleznete [zde](https://github.com/martinjagos/PRO_final-project_jagos/releases). Aplikace vyžaduje externí balíček Flatlaf 3.0, který naleznete v kódu aplikace.
## III. Instalace souboru JAR
1. [Zde](https://github.com/martinjagos/PRO_final-project_jagos/releases) stáhněte ZIP soubor.
2. Soubor ZIP extrahujte.
3. Spusťte soubor JAR.
## IV. Používání aplikace
Když otevřete aplikaci, tak nahoře uvidíte lištu s kartami - *Objednávky*, *Filamenty* a *Ostatní*. Vámi zvolenou kartu otevřete jednoduchým kliknutím na ni.
### A) Karta Objednávky
* Zde uvidíte 2 části karty - *Nová objednávka* a *Historie Objednávek*.
#### a) Sekce Nová objednávka
* V první části *Nová objednávka* můžete přidat novou objednávku. Můžete zvolit filament, datum ve formátu YYYY-MM-DD, spotřebu filamentu v gramech, spotřebovanou energii ve Wh a jestli jste vytvořili vlastní model pro objednávku.
* Pro správný výpočet nákladů vyplňte pole na kartě *Ostatní* v sekci *Nastavení*.
* Pro potvrzení a vytvoření nové objednávky stiskněte tlačítko *Odeslat*, které zapíše do souboru */settings/historieObjednavekDB.txt* objednávku, vypočítá náklady ze spotřeby filamentu a spotřeby energie a také odečte z vybraného filamentu hmotnost.
#### b) Sekce Historie objednávek
* Zde se vykreslují zapsané objednávky ze souboru */settings/historieObjednavekDB.txt*. Nalezneme zde ID objednávky neboli pořadové číslo objednávky, použitý filament, datum objednávky, spotřebu filamentu, spotřebu energie, jestli jsme k objednávce použili vlastní model a vypočtěné náklady.
### B) Karta Filamenty
* Na kartě *Filamenty* se vykreslují jednotlivé informace o nešem filamentu a obrázek filamentu. Zobrazují se zde informace - Název, Barva, Materiál, Hmotnost, ID neboli pořadové číslo a Cena.
* Filamenty jsou pomocí GridLayout a BoxLayoaut zobrazovaní ve 3 sloupcích. Sloupce jsou dynamické, takže je možno vykreslovat několik filamentů. Panel, na kterém jsou filamenty vykreslené, se bude zvětšovat. Poté si můžeme listovat přes kolečko myši nebo posuvníkem na pravé straně.

### C) Karta Ostatní
#### a) Sekce Přidat nový filament
* V této sekci můžeme přidávat nové filamenty do seznamu našich filamentu. Po vyplnění všech polí a vybrání obrázku zmáčkneme tlačítko *Odeslat* a informace se uloží do souboru */settings/filamentDB.txt*, přidají se do seznamu filamentů, ihned se vykreslí na kartě *Filamenty* a nahraje se obrázek do složky */images/*

* **POZOR!** je možno jen přidávat obrázky s koncovkou .png.
#### b) Sekce Upravit filament
* Zde můžeme upravovat a mazat informace o jednotlivých filamentech.
* Pro upravení filamentu zadejte do pole *Upravit filament s ID* - číslo ID, které můžete zjistit na kratě *Filamenty*. Po vyplnění klikněte na tlačítko vedle pole - *Načíst*.
* Vypíšou se vám všechny uložené informace o zvoleném filamentu. Upravit můžete všechny až na *Cenu* a *Cenu za 1 g*.
* Po upravení klikněte na tlačítko *Odeslat*. Změny se zapíšou do souboru */settings/filamentDB.txt*. 
* Pokud chcte filament odstranit, načtěte filament jako v postupu pro upravení filamentu výše a stiskněte tlačítko *Odstranit*. Poté se vymažou informace o vámi zvolené filamentu, ID dalších filamentů (pořadové číslo) se změní a smaže se obrázek filamentu ve složce */images/*.
#### c) Sekce Nastavení
* V této sekci můžeme přidávat cenu za energie.
* Pro přidání ceny za energie vyplňte pole *Cena za energie* v v Korunách českých na kWh a odešlete tlačítkem *Potvrdit*.
## V. Omezení aplikace
Požívání aplikace omezuje licence [GNU General Public License v2.0](https://github.com/martinjagos/PRO_final-project_jagos/blob/master/LICENSE).
## VI. Externí knihovny
* Knihovna FlatLaf - zajišťuje moderní vzhled aplikace © Copyright 2023 JFormDesigner podle licence *Apache-2.0 license*
