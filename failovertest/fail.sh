#!/bin/bash

s1=${1:-90}

iptables -t filter -A INPUT -p tcp -m tcp --dport 22 -j ACCEPT
iptables -t filter -A INPUT -j DROP
iptables -t filter -A OUTPUT -p tcp -m tcp --sport 22 -j ACCEPT
iptables -t filter -A OUTPUT -j DROP

echo Killed, waiting ${s1} seconds...
sleep ${s1}

echo Resuming...

iptables -F INPUT
iptables -F OUTPUT

echo Done.