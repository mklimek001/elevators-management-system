# Elevators managemet system

# EN
## Technology
The algorithm was developed in the Java language, with two main classes: Elevator (representing a single elevator) and Building (controlling all elevators in the building). To enable interaction with other applications, a simple REST API was implemented using the Spring framework.   
The API allows for:
* Starting and stopping the simulation
* Retrieving the status of all elevators in the building
* Calling an elevator to a specific floor
* Changing the state of a selected elevator

The application is built using Gradle.

I failed while preparing Docker containers, so the most convenient way to run the application is to use an IDE (such as IntelliJ IDEA) with Java JDK 17 installed.

There are also JUnit tests attached to the project.

For demonstration purposes, I have created a simple web application. It allows for interaction with the elevators in the building. It can be run with the following commands:

```
cd elevator-frontend
npm install
npm start
```

## Algorithm
In the simulation, elevators move up and down between the maximum and minimum requested floors. For example, an elevator located on the floor **5** and requested for floors **[7, 2, 8, 3, 2]** will move as follows: **5 -> 7 -> 8 -> 3 -> 2**.

The floors, at which the elevator stops can be dynamically changed during the program's execution (for example, by calling the elevator from a panel on a floor or  inside the elevator).

When an elevator is called from a floor panel, the algorithm checks if it's possible to send an empty elevator to this floor. It is allowed when
1. There is at least one available elevator, and no other elevator will stop on that floor while moving in this direction.
2. More than half of the elevators are available, and at most one other elevator will stop on that floor while moving in this direction.

In such cases, the nearest available elevator is selected to go to that floor.

If there are no available elevators and no moving elevator will stop on that floor while moving in the requested direction, the algorithm chooses the elevator that will reach that floor earliest. It prefers elevators that will continue the route in the requested direction after reaching that floor.

# PL
## Technologia

Algorytm przygotowany został w języku Java. Dwie główne, odpowiedzialne za jego poprawność klasy to Elevator (pojedyncza winda) i Building (centrum sterowania wszystkimi windami w budynku). Aby umożliwić interakcję z innymi aplikacjami, z wykorzystaniem bibliotek Spring, przygotowane zostało proste  REST API, które umożliwia:
* uruchomienie i zatrzymanie symulacji
* pobranie statusu wszystkich wind w budynku
* przywołanie windy na określone piętro
* zmianę stanu wybranej windy

Aplikacja jest budowana z użyciem Gradle.

Próby przygotowane kontenery Docker nie działają poprawnie, więc najwygodniejszym sposobem na uruchomienie aplikacji pozostaje skorzystanie z IDE (np Intellij IDEA) z zainstalowanym JDK Java 17. 

Do aplikacji zostały również przygotowane testy.

Do celów prezentacyjnych przygotowana została również prosta aplikacja webowa , Pozwala ona na wchodzenie w interakcję z windami w budynku. Można ją uruchomić następującymi komendami:
```
cd elevator-frontend
npm install
npm start
```

# Algorytm

Windy w symulacji poruszają się w górę i w dół, pomiędzy skrajnymi z pięter, na których się zatrzymują. Na przykład winda, która znajduje się na **5** piętrze i dostanie za zadanie dojechać na piętra **[7, 2, 8, 3, 2]**, poruszy się w następujący sposób: **5 -> 7 -> 8 -> 3 -> 2**.

Piętra na których winda się zatrzymuje mogą być dynamicznie zmieniane w czasie działania programu (na przykład przez przywołanie widny z poziomu panelu na piętrze lub w windzie).

W przypadku przywołania windy z poziomu panelu na określonym piętrze, algorytm sprawdza, czy możliwe jest przesłanie pustej windy. Dzieje się tak w dwóch przypadkach:
1. istnieje co najmniej jedna wolna winda i żadna inna winda nie zatrzyma się dążąc w tym kierunku
2. więcej niż połowa wind jest wolna i co najwyżej jedna inna winda zatrzyma się dążąc w tym kierunku

Wtedy znajdowana jest najbliższa wolna winda, która otrzymuje polecenie udania się na to piętro.

Jeśli nie ma dostępnych wolnych wind i żadna poruszająca się winda nie zatrzymuje się na tym piętrze, dążąc w zadanym kierunku, wybierana jest winda, która dotrze do niego najwcześniej. Preferowane są przy tym windy, które będą udawały się w dalszą drogę w zadanym kierunku.


