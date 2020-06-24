# Changelog (German)

28.08.2019
 * Refakturierung und implementation Namespaces bzw. packages.
 * Version auf 0.9 und suffix auf beta gesetzt.
 * Service auf Start_sticky gesetzt.
 * Added Boot Receiver & StartupFgService

29.08.2019
 * kleine Refakturierung
 * Möglichkeit den Alarm zu disabeln eingebaut.
 * Gebuildet wird jetzt alles ausser legacy.
 * SMSReceiver deregistriert.
 * MainActivity entsorgt.
 * SMS Listener und zugehöriges Interface nach Legacy gezügelt.

03.09.2019
 * Rework der Gui Instanzierung. Gui wird bei Alarm neu instanziert, die alten geschlossen.
 * App hat nun einen Broadcaster der Informationen weiter geben kann.
 * Settings angepasst.

25.09.2019
 * Services durch Receiver ersetzt das Android Services gelegentlich als idle abmurkst
 * Gui Rework angepasst. Die alten werden sofern geöffnet überlagert, da die Timestamps nicht richtig übergeben werden

26.09.2019
 * Addressparser ist nun Interface und kann erweitert werden.
 * Diverse Refakturierungen

05.10.2019
 * GitHub Initial commit

07.10.2019
 * Branch mit Map-Integration hinzugefügt.

25.10.2019
 * Im MapIntegration Branch Ringtone Picker eingebaut.
 * Contact Picker Eingebaut

23.06.2020
 * Berechtigungs Handling überarbeitet.
 * Version ist jetzt 1.1
 * Das Handling von Dialogfenstern wurde angepasst.
 * Das Backand für das Einstellungshandling wurde angepasst.
 * Do Not Disturb ist nun standardmässig ausgeschaltet.
 * Viel Refakturieren
 * Legacy Items entfernt
 
24.06.2020
 * Version ist jetzt 1.2 public