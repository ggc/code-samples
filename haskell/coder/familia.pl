%% Relaciones familiares como programa lógico %%


hombre(javier).
hombre(pedro).
hombre(jorge).
hombre(alfonso).
hombre(juan).
mujer(maria).
mujer(carmen).
mujer(teresa).
mujer(alicia).

progenitor(javier,pedro).
progenitor(javier,teresa).
progenitor(maria,pedro).
progenitor(maria,teresa).
progenitor(pedro,alfonso).
progenitor(pedro,juan).
progenitor(carmen,juan).
progenitor(carmen,alfonso).
progenitor(jorge,alicia).
progenitor(teresa,alicia).

padre(X,Y) :-
  progenitor(X,Y),
  hombre(X).
madre(X,Y) :-
  progenitor(X,Y),
  mujer(X).

hijo(X,Y) :-
  progenitor(Y,X),
  hombre(X).

abuelo(X,Y) :-
  progenitor(Z,Y),
  padre(X,Z).

hermano(X,Y) :-
  progenitor(Z,X),
  progenitor(Z,Y).

tio(X,Y) :-
  progenitor(Z,Y),
  hermano(Z,X).

descendiente(X,Y) :-
  progenitor(Y,X).
descendiente(X,Y) :-
  progenitor(Y,Z),
  descendiente(X,Z).


% ¿Qué pasa con el objetivo hermano(pedro,pedro)?
hermano1(X,Y) :-
  progenitor(Z,X),
  progenitor(Z,Y),
  distinto(X,Y).

distinto(X,Y) :- X \= Y.


















