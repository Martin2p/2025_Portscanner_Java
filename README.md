# Java Portscanner

Ein einfacher Portscanner in JavaFX zur Erkennung erreichbarer Geräte im lokalen Netzwerk über geöffnete Ports – ohne ICMP-Ping.  
Erkennt u. a. Smartphones, Drucker oder Smart-TVs über gängige Ports (22, 80, 443, 5555, 62078 etc.).

## Features
- Scan eines IP-Subnets (z. B. 192.168.0.0/24)
- Erkennt Geräte durch Port-Verbindung statt Ping
- Fortschrittsanzeige mit `ProgressIndicator` (JavaFX)
- Ausgabe erreichbarer Hosts in einer `TextArea`
- Multithreaded (JavaFX `Task`-basierter Hintergrundscan)

## Technologien
- Java 17+
- JavaFX
- SceneBuilder (für GUI)

## Installation & Ausführung
1. JavaFX im Projekt einbinden
2. SceneBuilder verwenden oder manuell `FXML` anpassen
3. `Main`-Klasse starten


## License / Lizenz

**English:**  
This software is for **private, non-commercial use only**.  
Any commercial use, modification, distribution, or publication without prior written permission is prohibited.  
See [`LICENSE.txt`](LICENSE.txt) for details.

**Deutsch:**  
Diese Software darf **nur privat und nicht kommerziell** verwendet werden.  
Kommerzielle Nutzung, Modifikation oder Weitergabe ist ohne schriftliche Erlaubnis nicht gestattet.  
Details siehe [`LICENSE.txt`](LICENSE.txt).

---

## ✉️ Contact / Kontakt
Fragen, Feedback oder Interesse an einer Lizenz für kommerzielle Nutzung?

📧 E-Mail: `martin.tastler@posteo.de`

---

## Status
Projekt ist abgeschlossen.
Project is finished.
