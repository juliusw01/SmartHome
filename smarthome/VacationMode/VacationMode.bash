#!/bin/bash

# Definiere die URL des POST-Endpunkts
URL="http://localhost:8080/light/device/12310fa9d1604f89b7ae363466f98c5c/switchState"

# API-Key für die Authentifizierung
API_KEY="API_KEY_SECRET"

# Definiere minimale und maximale Wartezeit in Sekunden
MIN_WAIT=600    # minimale Wartezeit (z.B. 5 Sekunden)
MAX_WAIT=3600   # maximale Wartezeit (z.B. 60 Sekunden)

COUNTER=0

while true; do
    # Hole die aktuelle Stunde im 24-Stunden-Format
    CURRENT_HOUR=$(date +%H)

    # Prüfe, ob die aktuelle Stunde zwischen 18 und 24 Uhr liegt
    if (( CURRENT_HOUR >= 18 && CURRENT_HOUR < 24 )) || (( COUNTER % 2 != 0 )); then
        # Erzeuge eine zufällige Wartezeit zwischen MIN_WAIT und MAX_WAIT
        WAIT_TIME=$((RANDOM % (MAX_WAIT - MIN_WAIT + 1) + MIN_WAIT))

        # Ausgabe der Wartezeit
        echo "Warte $WAIT_TIME Sekunden, bevor der POST-Request gesendet wird..."

        # Warten für die zufällige Zeit
        sleep $WAIT_TIME

        # POST-Request senden
        curl -s -X POST \
             -H "Content-Type: application/json" \
             -H "X-API-KEY: $API_KEY" \
             $URL > /dev/null

        # Erhöhe den Zähler und gebe den aktuellen Wert aus
        COUNTER=$((COUNTER + 1))
        echo "POST-Request an $URL gesendet um $(date). Anzahl der Aufrufe: $COUNTER"
    else
        # Außerhalb des Zeitfensters, 5 Minuten warten, bevor erneut geprüft wird
        echo "Aktuelle Zeit $(date +%H:%M). Skript pausiert bis 18 Uhr."
        sleep 300  # 300 Sekunden (5 Minuten) warten
    fi
done
