;Muestra por pantalla una P si el primer numero apilado es primo
push 17
dup
store 0
push 2
dup
store 1
div
store 2
load 0
dup
load 1
div
load 1
mul
eq
not
load 1
load 2
le
and
rbf 6
load 1
push 1
add
store 1
rjump -17
load 1
load 2
le
rbt 4
push 80
out
rjump 5
push 78
out
end

