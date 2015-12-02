--Programacion declarativa - Practica 1

{-Apartado 1-}
f :: Float -> Float -> Float
f x y = 2*x - y*x

g :: Float -> Float
g x   = f (f 2 x) (f x 1)

h :: Float -> Float -> Float
h x y = 
 if x>=y && y>0 then x - y else
 if y>x && x>0  then 0
 else y - x
 
i :: Float -> Float -> Float -> Float
--el primer arg = 0 
i x y z = f (f (x + 2*y) (g 3)) (5 - (g z) - y)

{-Apartado 2-}

{-Apartado 3-}
not True = False
not False = True
--True && y = y
--False && y = False


{-Apartado 4-}
perm :: Integer -> Integer
perm m = 
 if m == 1 then m else
 if m > 1 then m*perm (m-1)
 else -1

var :: Integer -> Integer -> Integer
var n m = m^n

comb :: Integer -> Integer -> Integer
comb n m = div (perm n) ((perm m)*perm (n-m))

{-Apartado 5-}
multiplo35 :: Integer -> Bool
multiplo35 n = multiplo3 n || multiplo5 n
multiplo3 n = 
 if (mod n 3)==0 then True
 else False
multiplo5 n =
 if (mod n 5)==0 then True
 else False

digEntero n = 