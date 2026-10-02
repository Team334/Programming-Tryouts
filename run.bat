#!/bin/bash

# Create the output directory if it doesn't exist
mkdir -p out

# Find all .java files in src and lib and compile them
find src lib -name "*.java" > sources.txt
javac -d out @sources.txt
rm sources.txt

# Run the compiled Robot class
java -cp out src.Robot
