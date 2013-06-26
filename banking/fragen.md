1. Wie hoch ist der Aufwand in Eiffel, um Zusicherungen im Programmcode zu formulieren?

Sehr niedrig, da man mit require ein Vorbedingung, mit ensure eine Nachbedingung und mit inveriant eine Invariante erstellen kann.


2. Wie stark wirkt sich die Überprüfung von Zusicherungen auf die Laufzeit aus?

Die Überprüfungen wirken sich auf die Laufzeit aus, da jede Bedingung geprüft werden muss´, deshalb wird empfohlen Zusicherungen nicht in Produktiv Umgebungen zu verwenden. 
http://docs.eiffel.com/book/method/et-design-contract-tm-assertions-and-exceptions#Run-time_assertion_monitoring


3. Vorbedingungen dürfen im Untertyp nicht stärker und Nachbedingungen nicht schwächer werden um Ersetzbarkeit zu garantieren. Der Eiffel-Compiler überprüft diese Bedingungen. Ist es (trotz eingeschalteter Überprüfung von Zusicherungen) möglich diese Bedingungen zu umgehen? Wenn ja, wie?

In Eiffel wird dies dadurch garantiert, dass man beim Überschreiben einer Methode eines Untertyps nur die Vorbedingung durch require else (alte Vorbedingung oder neue Vorbedingung) abschwächen kann und die Nachbedingung nur durch ensure then (alte Nachbedingung und neue Nachbedingung)verstärken kann.

Das ganze kann umgehen wenn man Konstanten für die Bedingungen verwendet und im Untertypen andere Werte setzt, sodass die Bedingungen abgeschwächt bzw. gestärkt werden.

4. Eiffel erlaubt kovariante Eingangsparametertypen. Unter welchen Bedingungen führt das zu Problemen, und wie äußern sich diese? 
Können Sie ein Programm schreiben, in dem die Verwendung kovarianter Eingangsparametertypen zu einer Exception führt?

http://dev.eiffel.com/Covariance_through_renaming


5. Vereinfachen kovariante Eingangsparametertypen die Programmierung? Unter welchen Bedingungen ist das so?