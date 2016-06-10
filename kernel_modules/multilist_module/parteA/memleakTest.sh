#!/bin/bash

# listTest nTimes
function listTest {
    n=$1
    while [[ $n -gt  0 ]]
    do
	echo "add $n" > /proc/modmultilist/default
	#sleep .1
	n=$(( n-1 ))
    done

    n=$1
    while [[ $n -gt  0 ]]
    do
	echo "remove $n" > /proc/modmultilist/default
	#sleep .1
	n=$(( n-1 ))
    done
}

# entryTest nTimes
function entryTest {
    n=$1
    while [[ $n -gt 0 ]]
    do
	echo "create entry$n" > /proc/modmultilist/control
	sleep .1
	n=$(( n-1 ))
    done
    n=$1
    while [[ $n -gt 0 ]]
    do
	echo "delete entry$n" > /proc/modmultilist/control
	sleep .1
	n=$(( n-1 ))
    done
}


# memleakTest.sh function nTimes
clear
if [ "$1" == "list" ]
then
  listTest $2
fi

if [ "$1" == "entry" ]
then
  entryTest $2
fi

echo "Fin"
