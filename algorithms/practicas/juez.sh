program=$(find ./$1 -maxdepth 1 -name "*-exe")
echo $program
$program < $1/sample.in > $1/sample.return
diff $1/sample.return $1/sample.out >> /dev/null
if [ $? -eq 0 ]
then
    echo "Correcto!"
else
    echo "Respuestas incorrectas."
fi
echo $(diff $1/sample.return $1/sample.out)
