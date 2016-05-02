#include <iostream>
#include <climits>

using namespace std;


const int MP = 20; // Num max de paises
const int MC = 20; // Num max de colores

void colorearPaises(int nPaises, int pais, int nColores, int paises[MP], int colores[MC], bool fronteras[MP][MP], bool paisesColoreados[MP], bool coloresUsados[MC], int coste)
{
  if(pais<nPaises && paisesColoreados[pais] == false) //Si el pais actual no esta coloreado
      for(int i = 0;i<nColores;i++){ //Escoge un color
	cout << "Arranca iteracion pais " << pais << " con coste "<< coste << endl;
	if(!coloresUsados[i]){ //Si el color buscado no esta usado, marcas pais y color
	  cout << "COSTE: " << coste << endl;
	  cout << "pais: " << pais << endl;
	  cout << "color: " << i << endl;
	  //Marcar
	  paisesColoreados[pais] = true;
	  coloresUsados[i] = true;
	  coste = coste + paises[pais]*colores[i];
	  for(int j = 0;j<nPaises;j++) //Buscar mas paises sin fronteras
	    if(fronteras[pais][j]==0 && i!=j && !paisesColoreados[j]){
	      cout << "pais " << j << "tambien" << endl;
	      paisesColoreados[j]=true; //Se marcan
	      coste = coste + paises[j]*colores[i];
	    }
	  cout << "COSTE "<< coste << endl;
	  //Fin marcar
	  colorearPaises(nPaises, pais+1, nColores, paises, colores, fronteras, paisesColoreados, coloresUsados, coste); //Con lo marcado, se prueba con sig. pais
	  //Desmarcar
	  paisesColoreados[pais] = false;
	  coloresUsados[i] = false;
	  coste = coste - paises[pais]*colores[i];
	  for(int j = 0;j<nPaises;j++) //Buscar mas paises sin fronteras para desmarcar
	    if(fronteras[pais][j]==0 && i!=j && !paisesColoreados[j]){
	      paisesColoreados[j]=false; //Se desmarcan
	      coste = coste - paises[j]*colores[i];
	    }
	  //Fin desmarcar
	  cout << "Fin iteracion pais " << pais << " con coste "<< coste << endl;
	}
      }
  
  else{
    cout << "Coste: " << coste << endl;
    coste=0;
  }
}
  
int main() {
  int nmapas;    // num de mapas a colorear

  int nPaises;   // num de paises del mapa actual
  int nColores;  // num de colores para el mapa actual

  int paises[MP];  // superficie de cada pais en el mapa actual
  int colores[MC]; // coste para colorear una unidad de superficie usando cada color

  // fronteras[i][j] indica si existe frontera entre los paises i y j en el mapa
  // Es una matriz simetrica (fronteras[i][j] == fronteras[j][i])
  bool fronteras[MP][MP];

  // Leer numero de mapas del caso de pruebas
  cin >> nmapas;
  for (int i=0; i<nmapas; i++) {
    // Leer numero de paises y colores para el mapa actual
    cin >> nPaises;
    cin >> nColores;

    // Leer superficie de cada pais
    for (int j=0; j<nPaises; j++)
      cin >> paises[j];

    // Leer coste de cada color (por unidad de superficie)
    for (int j=0; j<nColores; j++)
      cin >> colores[j];

    // Leer matriz de fronteras
    for (int j=0; j<nPaises; j++) {
      for (int k=0; k<nPaises; k++) {
	cin >> fronteras[j][k];
      }
    }

    int coste = 0;

    /* Llama aquí a tu procedimiento y calcula el valor de mejorcoste */

    bool coloresUsados[MC];
    for (int j=0; j<nColores; j++)
      coloresUsados[j] = false;
    bool paisesColoreados[MP];
    for (int j=0; j<nPaises; j++)
      paisesColoreados[j] = false;
		
    colorearPaises(nPaises, 0, nColores, paises, colores, fronteras, paisesColoreados, coloresUsados, coste); //Con lo marcado, se prueba con sig. pais
		
    /* .... */

    //cout << mejorcoste << endl;
  }

  return 0;
}
