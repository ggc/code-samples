--Programacion declarativa. Practica obligatoria
import Control.Monad
import Data.Char
--Para introducir el texto codificado se usara
--el comando cat de unix.
coder = [['a','1'],['e','2'],['i','3'],['o','4'],['u','5']];

main = forever $ do
   --putStr "write here: "
   l <- getLine -- Lee un caracter de la entrada
   putStrLn $ map decoChar l -- Llama la func decod con dicho char


decoChar::Char -> Char
decoChar c
   | not (validChar c) = 'X' --Rrescindible
   | null (digievoluciona c) = c --Si no hay codificacion, se mantiene
   | otherwise =  head (digievoluciona c)
          
-- Funciones auxiliares--
digievoluciona::Char -> [Char]
digievoluciona c = [last x | x <- coder, (head x) == c]

validChar::Char -> Bool
validChar c = 
   or (map (c ==) (['a'..'z']++['A'..'Z']++['.', ',', ' ', '\n']))
