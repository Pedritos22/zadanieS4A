## Status zadania

![Build Status](https://img.shields.io/badge/Tests-14%20Passed-brightgreen)
![Language](https://img.shields.io/badge/Language-Java-orange)

Wszystkie zaimplementowane unit testy kończą się sukcesem.
Skrypt zwraca kod wyjścia `0` przy pełnym powodzeniu operacji.

## Podejscie do rozwiazania

Rozwiazanie zostalo napisane z naciskiem na wydajnosc czasowa i pamieciowa, poniewaz ograniczenia wejscia sa bardzo duze.

- do obslugi aktualizacji pojedynczej trasy i zapytan o sume na przedziale zostaly uzyte dwa drzewa `Binary Indexed Tree`
- jedno drzewo przechowuje aktualne pojemnosci aktywnych tras, a drugie przechowuje skumulowany offset potrzebny do obliczenia wyniku w chwili `t`
- kazda operacja `P`, `C`, `A` i `Q` jest obslugiwana w `O(log n)`
- poczatkowa inicjalizacja drzewa pojemnosci jest wykonywana w `O(n)`

## Struktura kodu

Kod zostal podzielony na kilka prostych klas pomocniczych:

Wszystkie klasy zrodlowe znajduja sie w pakiecie `s4a`.

- `Main.java` zawiera glowna logike rozwiazania i obsluge operacji `P`, `C`, `A`, `Q`
- `BinaryIndexedTree.java` odpowiada za aktualizacje punktowe i sumy prefiksowe/przedzialowe
- `FastScanner.java` odpowiada za szybki odczyt danych wejsciowych
- `FastOutput.java` odpowiada za buforowane wypisywanie wynikow

## Dlaczego kod jest napisany w ten sposob

Przy ograniczeniach `n, q <= 10^7` samo poprawne rozwiazanie nie wystarcza. Duze znaczenie ma koszt wejscia/wyjscia, rozmiar struktur danych i brak zbednego narzutu runtime.

- parser wejscia jest oparty o bufor bajtowy, aby uniknac wolniejszych mechanizmow standardowej biblioteki przy bardzo duzej liczbie tokenow
- wypisywanie wynikow jest buforowane, zeby ograniczyc liczbe operacji na `stdout`
- pojemnosci tras sa przechowywane w `short[]`, bo z tresci zadania wynika `0 <= p_i <= 1000`, wiec nie ma potrzeby uzywania wiekszego typu
- rozwiazanie korzysta z prostych tablic i struktur bez dodatkowych obiektow, zeby ograniczyc zuzycie pamieci

## Co warto zauwazyc

- zapytania sa uporzadkowane chronologicznie, wiec stan tras mozna aktualizowac na biezaco
- operacja `C i t` zawiera czas w formacie wejscia, ale samo wycofanie opiera sie na zapamietanym stanie danej trasy
- przy tak duzych danych trzeba uwazac nie tylko na zlozonosc asymptotyczna, ale tez na stale czasowe i koszt I/O

## Dlaczego nie zostal uzyty `BigInteger`

Nie zastosowalem `BigInteger`, poniewaz w tym zadaniu bardzo mocno pogorszylby wydajnosc i zuzycie pamieci.

- operacje na drzewach wykonywane sa nawet dziesiatki milionow razy, a `BigInteger` jest wielokrotnie ciezszy od `long`
- nie wystarczyloby uzyc `BigInteger` tylko przy koncowym wypisywaniu wyniku, bo wartosci posrednie sa przechowywane w drzewach i tablicach
- przejscie calej struktury na `BigInteger` najprawdopodobniej uczyniloby rozwiazanie zbyt wolnym i zbyt pamieciozernym

Obecna wersja uzywa `long`, co jest naturalnym kompromisem. Oznacza to jednoczesnie, ze poprawne dzialanie wymaga, aby oczekiwane wyniki miescily sie w zakresie typu `long`.

## Matematyka rozwiazania

Dla jednej aktywnej trasy o pojemnosci `p`, ktora zaczela byc liczona od chwili `s`, liczba dostepnych miejsc do chwili `t` wynosi:

```text
p * (t - s)
```

Po przeksztalceniu:

```text
p * t - p * s
```

To jest kluczowa obserwacja. Wklad jednej trasy mozna rozdzielic na dwie czesci:

- czesc zalezna od chwili zapytania: `p * t`
- czesc stala dla aktualnego stanu trasy: `-p * s`

Dla przedzialu tras `[i, j]` dostajemy wiec:

```text
wynik(i, j, t) = t * sumaPojemnosci(i, j) + sumaOffsetow(i, j)
```

gdzie:

- `sumaPojemnosci(i, j)` to suma aktualnych pojemnosci aktywnych tras
- `sumaOffsetow(i, j)` to suma wartosci `-p * s` dla aktywnych tras

Wlasnie dlatego w rozwiazaniu utrzymywane sa dwa drzewa `Binary Indexed Tree`:

- jedno dla pojemnosci
- jedno dla offsetow

## Sens poszczegolnych operacji

`Q i j t`

Zapytanie odczytuje obie sumy na przedziale i podstawia je do wzoru:

```text
offsetSum + t * capacitySum
```

`A i p t`

Po przypisaniu nowego samolotu od chwili `t` jego wklad ma byc rowny:

```text
p * (czas - t) = p * czas + (-p * t)
```

Dlatego:

- pojemnosc ustawiana jest na `p`
- offset ustawiany jest na `-p * t`

`P i p t`

Zmiana pojemnosci w chwili `t` musi zachowac poprawna historie do chwili zmiany i zaczac nowe naliczanie od tego momentu. Jesli stara pojemnosc to `old`, a nowa to `p`, to korekta offsetu wynosi:

```text
(old - p) * t
```

Po tej zmianie wzor dalej dziala poprawnie dla dowolnego przyszlego zapytania.

`C i t`

Wycofanie oznacza, ze trasa ma od tej chwili przestac wnosic jakikolwiek wklad do wyniku. W praktyce usuwany jest wiec aktualny wklad tej trasy z obu drzew:

- jej aktualna pojemnosc
- jej zapamietany offset

## Co to oznacza?

Kazda modyfikacja dotyczy tylko jednej trasy, a kazde zapytanie potrzebuje tylko sumy na przedziale.
Uznałem, że pasować pod to będzie Binary Indexed Tree (inaczej drzewo Fenwicka):

- aktualizacja punktowa kosztuje `O(log n)`
- suma na przedziale kosztuje `O(log n)`

Nie trzeba przeliczac calego przedzialu od zera dla kazdego zapytania, bo cala historia jest zakodowana w aktualnej pojemnosci i offsetcie kazdej trasy.

## Wymagania

Do uruchomienia projektu potrzebne sa:

- Apache Maven `3.9.9` lub nowszy
- Java Development Kit `21` lub nowszy

## Uruchamianie lokalnie

### Build projektu

Podstawowa komenda budowania:

```bash
mvn clean package
```

Po poprawnym buildzie powstaje wykonywalny plik:

```bash
target/zadanie-s4a.jar
```

### Uruchomienie testow

```bash
mvn test
```

### Uruchomienie programu

Program czyta dane z `stdin`:

```bash
java -jar target/zadanie-s4a.jar < input.txt
```