#!/bin/bash

echo Fixing the file modification times ...
`dirname $0`/sbt.sh --no-jrebel "$@" run
