#!/bin/bash

#TODO: Enhance the script and choose randomly between all devices/lightsources

# Definiere die URL des POST-Endpunkts
URL="http://localhost:8080/light/device/12310fa9d1604f89b7ae363466f98c5c/switchState"


# Definiere minimale und maximale Wartezeit in Sekunden
MIN_WAIT=600    # minimale Wartezeit (z.B. 600 Sekunden)
MAX_WAIT=3600   # maximale Wartezeit (z.B. 3600 Sekunden)

while true; do
    # Erzeuge eine zufällige Wartezeit zwischen MIN_WAIT und MAX_WAIT
    WAIT_TIME=$((RANDOM % (MAX_WAIT - MIN_WAIT + 1) + MIN_WAIT))

    # Ausgabe der Wartezeit
    echo "Warte $WAIT_TIME Sekunden, bevor der POST-Request gesendet wird..."

    # Warten für die zufällige Zeit
    sleep $WAIT_TIME

    # POST-Request senden
    curl -s -X POST -H "Content-Type: application/json" $URL > /dev/null

    # Ausgabe, dass die URL aufgerufen wurde
    echo "POST-Request an $URL gesendet um $(date)"
done
