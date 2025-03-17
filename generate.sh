#!/bin/bash

SOURCE="src/main/java/advent/of/code/dayN/DayN.java"
DEST="src/main/java/advent/of/code/day$1/Day$1.java"
mkdir -p "$(dirname $DEST)"
cp "$SOURCE" "$DEST"
sed -i "s/DayN/Day$1/g" "$DEST"
sed -i "s/dayN/day$1/g" "$DEST"

mkdir -p "src/main/resources/advent/of/code/day$1/"
mkdir -p "src/test/resources/advent/of/code/day$1/"

touch "src/test/resources/advent/of/code/day$1/example.txt"

SOURCE="src/test/java/advent/of/code/dayN/DayNTest.java"
DEST="src/test/java/advent/of/code/day$1/Day$1Test.java"
mkdir -p "$(dirname $DEST)"
cp "$SOURCE" "$DEST"
sed -i "s/DayN/Day$1/g" "$DEST"
sed -i "s/dayN/day$1/g" "$DEST"
