# Args: orion-ip email tipoPersona 

curl $1:1026/v2/entities -s -S --header 'Content-Type: application/json' --header 'fiware-service: sc_thinkingcity' --header 'fiware-servicepath: /SmartParking' -d @- <<EOF
{
    "type": "Persona",
    "id": "11111",
    "Abril": {
        "value": "000000000000",
        "type": "string"
    },
    "AceptaCondiciones": {
        "value": "SI",
        "type": "string"
    },
    "Agosto": {
        "value": "000000000000",
        "type": "string"
    },
    "Antiguedad": {
        "value": "null",
        "type": "string"
    },
    "Aparcamiento": {
        "value": "null",
        "type": "string"
    },
    "AparcamientoNombre": {
        "value": "null",
        "type": "string"
    },
    "Apellido1": {
        "value": "G",
        "type": "string"
    },
    "Apellido2": {
        "value": "C",
        "type": "string"
    },
    "Apellidos": {
        "value": "GC",
        "type": "string"
    },
    "Area": {
        "value": "null",
        "type": "string"
    },
    "AsignacionesRealizadas": {
        "value": 0,             
        "type": "int"
    },
    "Bonus": {
        "value": "null",
        "type": "string"
    },
    "ColorCoche1": {
        "value": "null",
        "type": "string"
    },
    "ColorCoche2": {
        "value": "null",
        "type": "string"
    },
    "ColorCoche3": {
        "value": "null",
        "type": "string"
    },
    "DNI": {
        "value": "1234567XY",
        "type": "string"
    },
    "Desempenio": {
        "value": "Desempeniador",
        "type": "string"
    },
    "Diciembre": {
        "value": "000000000000",
        "type": "string"
    },
    "Edificio": {
        "value": "O1",
        "type": "string"
    },
    "EdificioNombre": {
        "value": "DCW01",
        "type": "string"
    },
    "Email": {
        "value": "$2",
        "type": "string"
    },
    "Empresa": {
        "value": "tesa",
        "type": "string"
    },
    "EmpresaContratada": {
        "value": "tid",
        "type": "string"
    },
    "Enero": {
        "value": "000000000000",
        "type": "string"
    },
    "Febrero": {
        "value": "000000000000",
        "type": "string"
    },
    "FechaAceptaCondiciones": {
        "value": "2017-05-22T09:12:44Z",
        "type": "DateTime"
    },
    "FechaIncorporacionEmpresaActual": {
        "value": "2017-05-22T09:12:44Z",
        "type": "DateTime"
    },
    "Frecuencia": {
        "value": "0",
        "type": "string"
    },
    "Idioma": {
        "value": "eng",
        "type": "string"
    },
    "InicioAnio": {
        "value": "0",
        "type": "string"
    },
    "Julio": {
        "value": "000000000000",
        "type": "string"
    },
    "Junio": {
        "value": "000000000000",
        "type": "string"
    },
    "MarcaCoche1": {
        "value": "null",
        "type": "string"
    },
    "MarcaCoche2": {
        "value": "null",
        "type": "string"
    },
    "MarcaCoche3": {
        "value": "null",
        "type": "string"
    },
    "Marzo": {
        "value": "000000000000",
        "type": "string"
    },
    "Matricula": {
        "value": "cx11111",
        "type": "string"
    },
    "MatriculaCoche1": {
        "value": "null",
        "type": "string"
    },
    "MatriculaCoche2": {
        "value": "null",
        "type": "string"
    },
    "MatriculaCoche3": {
        "value": "null",
        "type": "string"
    },
    "Mayo": {
        "value": "000000000000",
        "type": "string"
    },
    "Migrado": {
        "value": "SI",
        "type": "string"
    },
    "Miscelanea": {
        "value": "null",
        "type": "string"
    },
    "ModeloCoche1": {
        "value": "null",
        "type": "string"
    },
    "ModeloCoche2": {
        "value": "null",
        "type": "string"
    },
    "ModeloCoche3": {
        "value": "null",
        "type": "string"
    },
    "Movil": {
        "value": "null",
        "type": "string"
    },
    "Nivel": {
        "value": "null",
        "type": "string"
    },
    "Nombre": {
        "value": "G",
        "type": "string"
    },
    "Noviembre": {
        "value": "000000000000",
        "type": "string"
    },
    "Octubre": {
        "value": "000000000000",
        "type": "string"
    },
    "Proximidad": {
        "value": 0,
        "type": "int"
    },
    "PuestoDeTrabajo": {
        "value": "null",
        "type": "string"
    },
    "RankingCesionDinamica": {
        "value": 0,
        "type": "int"
    },
    "RankingCesionInicial": {
        "value": 0,
        "type": "int"
    },
    "Septiembre": {
        "value": "000000000000",
        "type": "string"
    },
    "SolicitaParking": {
        "value": "Activo",
        "type": "string"
    },
    "TipoPerfilUsuarioParkingDT": {
        "value": "null",
        "type": "string"
    },
    "TipoPersona": {
        "value": "$3",
        "type": "string"
    },
    "UID": {
        "value": "cx11111@TESA",
        "type": "string"
    },
    "applicationID": {
        "value": "TSP_SCT_7",
        "type": "string"
    },
    "timeInstant": {
        "value": "2017-05-22T09:12:44Z",
        "type": "DateTime"
    }
}