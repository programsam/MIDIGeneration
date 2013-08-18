#!/bin/sh
echo Removing old MIDIGen files...
rm -rf build/MIDIGen.app/Contents/MacOS/org
echo Copying over new MIDIGen files
cp -R bin/org build/MIDIGen.app/Contents/MacOS/org
echo Copying images
cp -R img build/MIDIGen.app/Contents/MacOS/
echo Exporting MIDIGen App
rm -rf ~/Downloads/MIDIGen.app
cp -R build/MIDIGen.app ~/Downloads/MIDIGen.app
