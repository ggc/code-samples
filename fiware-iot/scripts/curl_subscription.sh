# Args: cygnus-ip

curl $1:1026/v2/entities -s -S --header 'Content-Type: application/json' --header 'fiware-service: sc_thinkingcity' --header 'fiware-servicepath: /SmartParking' -d @- <<EOF
{
    "description": "Suscripcion de Persona",
    "subject": {
        "entities": [
            {
                "idPattern": ".*",
                "type": "Persona"
            }
        ],
        "condition": {
            "attrs": [ "TipoPersona", "SolicitaParking" ]
        }
    },
    "notification": {
        "http": {
            "url": "http://$1:5050/notify"
        },
        "attrs": [ ],
        "attrsFormat": "legacy"
    }
}