# beQuietApp

Aplikacja na androida napisana w języku Kotlin z użyciem bazy danych SQLLite.

Aplikacja ma na celu sterowanie profilem głośności telefonu na podstawie jego lokalizacji. Użytkownik wybiera lokalizację, 
promień działania od określonego miejsca oraz poziom głośności dla owego miejsca.

Aplikacja do działania wymaga włączonej lokalizacji. Po wkroczeniu w dany obszar miejsca, ustawiana jest odpowiednia głośność,
która jest przywracana do poprzedniego stanu po wyjściu spoza obszaru działania.

Aplikacja działa również w tle, co pozwala użytkownikowi nie martwić się o każdorazowe uruchamianie aplikacji.
