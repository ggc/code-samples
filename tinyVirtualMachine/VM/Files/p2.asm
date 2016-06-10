;Elimina el char indicado
in
dup
push -1
eq
bt 11
dup
push 97 ;Entero del ASCII a eliminar
eq
bt 0
out
jump 0