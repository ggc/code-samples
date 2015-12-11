#!/bin/bash

n=1

#Function that writes 1 - 10
function f1 {
    n=$1
    while [[ $n -gt 0 ]]
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

f1&
f2&
echo "Bien"
