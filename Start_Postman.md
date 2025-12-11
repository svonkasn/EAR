# Rychlý start - Postman

## Krok 1: Spuštění aplikace

``` bash
mvn spring-boot:run
```

Aplikace bude dostupná na adrese: `http://localhost:8080`

## Krok 2: Import kolekce

1.  Otevřete Postman
2.  Klikněte na `Import`
3.  Vyberte soubor `Online-System-Tests.postman_collection.json`

## Krok 3: Spuštění základních scénářů

### Scénář 1: Plný testovací cyklus s autogradem

1.  **Login as Student (alice)** - přihlášení studenta
2.  **Create Attempt** - vytvoření pokusu (userId: 3, testId: 1)
3.  **Submit Attempt** - odeslání pokusu (autograding se spustí
    automaticky)\
4.  **Get Attempt by ID** - kontrola výsledku

### Scénář 2: Ruční hodnocení

1.  **Login as Teacher (teacher1)** - přihlášení učitele
2.  **Grade Open Answer** - ohodnocení otevřené otázky (answerId: 1,
    points: 3)
3.  **Finalize Attempt** - finalizace pokusu
4.  **Get Attempt by ID** - kontrola finálního stavu

### Scénář 3: Statistika

1.  **Login as Teacher (teacher1)** - přihlášení učitele
2.  **Get Test Statistics** - získání statistiky testu (testId: 1)
3.  **Get Top N Leaderboard** - top-10 výsledků
4.  **Get Low N Leaderboard** - bottom-5 výsledků

## Testovací data

### Uživatelé:

-   **admin** `admin123` (ADMIN)
-   **teacher1** `teacher123` (TEACHER)
-   **alice**  `alice123` (STUDENT, ID: 3)
-   **bob** `bob123` (STUDENT, ID: 4)

### Testy:

-   **Test ID: 1** - "Java Test 1"
  -   Question 1: OpenQuestion (3 body)
  -   Question 2: MultipleChoice (4 body)
  -   Question 3: OpenQuestion (3 body)

### Existující pokusy:

-   **Attempt ID: 1** - alice, IN_PROGRESS
-   **Attempt ID: 2** - bob, EVALUATED, score: 7

## Důležité poznámky

1.  **Autentizace:** Po přihlášení Postman automaticky ukládá cookies.
    Ujistěte se, že jste provedli login před dalšími požadavky.

2.  **Proměnné:** Kolekce používá proměnné:

  -   `{{baseUrl}}` - základní URL (výchozí: http://localhost:8080)
  -   `{{attemptId}}` - ID pokusu (výchozí: 1)
  -   `{{scenarioAttemptId}}` - ID pokusu ze scénáře (nastavuje se
      automaticky)

3.  **Přístupová práva:**

  -   STUDENT může pracovat pouze se svými pokusy
  -   TEACHER může zobrazovat všechny pokusy a hodnotit
  -   ADMIN má plný přístup

4.  **Autograding:** Při odeslání pokusu (`submit`) se automaticky
    hodnotí všechny uzavřené otázky (MultipleChoice).

5.  **Ruční hodnocení:** Otevřené otázky vyžadují ruční hodnocení
    učitelem.

## Troubleshooting

-   **401 Unauthorized:** Proveďte login před požadavkem
-   **403 Forbidden:** Zkontrolujte uživatelskou roli
-   **404 Not Found:** Ověřte, že ID existuje v databázi
-   **409 Conflict:** Zkontrolujte logiku aplikace (stav pokusu atd.)

## Příklady použití

### Vytvoření nového pokusu:

``` json
POST /rest/attempts
{
    "userId": 3,
    "testId": 1
}
```

### Odeslání pokusu:

``` json
POST /rest/attempts/{attemptId}/submit
{
    "userId": 3
}
```

### Hodnocení otevřené otázky:

``` json
POST /rest/answers/{answerId}/grade
{
    "points": 3,
    "teacherId": 2,
    "note": "Good explanation"
}
```

### Získání statistiky:

    GET /rest/tests/{testId}/stats
