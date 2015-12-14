#!/bin/bash

#Function that writes 1 - 10
function f1 {
    n=$1
    while [[ $n -gt  0 ]]
    do
	echo "add $n" > /proc/modlist
	sleep .5
	n=$(( n-1 ))
    done
}

#Function that removes 1 - 10
function f2 {
    n=$1
    sleep 2
    while [[ $n -gt 0 ]]
    do
	echo "remove $n" > /proc/modlist
	
	sleep .5
	n=$(( n-1 ))
    done
}

function modcat {
    n=$1
    while [[ $n -gt 0 ]]
    do
	clear
	echo "Modlist contiene: "
	cat /proc/modlist
	
	sleep .5
	n=$(( n-1 ))
    done
}

clear
modcat $1 &
echo "add 999" > /proc/modlist
f1 $1 &
f1 $1 &
f1 $1 &
f2 $1 &
echo "Bien"
