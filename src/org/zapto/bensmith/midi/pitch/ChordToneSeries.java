package org.zapto.bensmith.midi.pitch;

import java.util.ArrayList;

public class ChordToneSeries {

	private int myTone = 0;
	private ArrayList<Integer> availableTones = new ArrayList<Integer>();
	private ArrayList<Integer> scaleSteps= new ArrayList<Integer>();
	private static ArrayList<Integer> majorTriad = new ArrayList<Integer>();
	private static ArrayList<String> predefinedChordList;
	private static ArrayList<int[]> predefinedChords;
	

	private int tonePosition = 0;
	
	public ChordToneSeries()
	{
		//default constructor gives C major triad
		myTone = 60;
		majorTriad.add(4);
		majorTriad.add(3);
		majorTriad.add(5);
		
		setScaleSteps(majorTriad);
		initializeToneSeries();
		loadPredefinedChords();
	}
	
	public ChordToneSeries(int startingPitch, ArrayList<Integer> rSS)
	{
		setRootPitch(startingPitch);
		setScaleSteps(rSS);
		initializeToneSeries();
		loadPredefinedChords();
	}
	
	
	public ChordToneSeries(int startingPitch)
	{
		//default constructor gives C major scale
		setRootPitch(startingPitch);
		majorTriad.add(4);
		majorTriad.add(3);
		majorTriad.add(5);
		
		setScaleSteps(majorTriad);
		initializeToneSeries();
		loadPredefinedChords();
	}
	
	
	public  ArrayList<String> getPredefinedChordList() {
		return predefinedChordList;
	}

	public  ArrayList<int[]> getPredefinedChords() {
		return predefinedChords;
	}
	
	private void loadPredefinedChords()
	{
		predefinedChordList = new ArrayList<String>();
		predefinedChords = new ArrayList<int[]>();
		predefinedChordList.add("Minor Triad");
		int[] minorTriad = {3,4,5};
		predefinedChords.add(minorTriad);
		
		predefinedChordList.add("Major Triad");
		int[] majorTriad = {4,3,5};
		predefinedChords.add(majorTriad);
		
		predefinedChordList.add("Diminished Triad");
		int[] dimTriad = {3,3,6};
		predefinedChords.add(dimTriad);
		
		predefinedChordList.add("Augmented Triad");
		int[] augTriad = {4,4,4};
		predefinedChords.add(augTriad);
		
		predefinedChordList.add("Major 7");
		int[] maj7 = {4,3,4,1};
		predefinedChords.add(maj7);
		
		predefinedChordList.add("Minor 7");
		int[] min7 = {3,4,3,2};
		predefinedChords.add(min7);
		
		predefinedChordList.add("Major 6");
		int[] maj6 = {4,3,2,3};
		predefinedChords.add(maj6);
		
		predefinedChordList.add("Minor 6");
		int[] min6 = {3,4,2,3};
		predefinedChords.add(min6);
		
		predefinedChordList.add("Dominant 7");
		int[] dom7 = {4,3,3,2};
		predefinedChords.add(dom7);
		
		predefinedChordList.add("Diminished 7");
		int[] dim7 = {3,3,3,3};
		predefinedChords.add(dim7);
		
		predefinedChordList.add("Half-diminished 7");
		int[] hd7 = {3,3,4,2};
		predefinedChords.add(hd7);
		
		predefinedChordList.add("Minor-major 7");
		int[] mm7 = {3,4,4,1};
		predefinedChords.add(mm7);
	}
	
	public void setRootPitch(int pitch)
	{
		myTone = pitch;
	}
	
	public void setScaleSteps(ArrayList<Integer> rSS)
	{
		scaleSteps = rSS;
	}
	
	public void initializeToneSeries()
	{
		int tone = myTone;
		int scaleStep = 0;
		availableTones.clear();
		availableTones.add(myTone);
		while (tone < 127)
		{
			tone += scaleSteps.get(scaleStep);
			availableTones.add(tone);
			scaleStep++;
			if (scaleStep > (scaleSteps.size()-1))
				scaleStep = 0;
		}
		tone = myTone;
		scaleStep = scaleSteps.size()-1;
		while (tone > 0)
		{
			tone -= scaleSteps.get(scaleStep);
			availableTones.add(0, tone);
			scaleStep--;
			if (scaleStep < 0)
				scaleStep = (scaleSteps.size()-1);
			
		}
		goHome();
	}
	
	public void setTonePosition(int x)
	{
		tonePosition = x;
	}
	
	public ArrayList<Integer> getToneSeries()
	{
		return availableTones;
	}
	
	public int thisTone()
	{
		return availableTones.get(tonePosition);
	}
	
	public int forward(int distance)
	{
		tonePosition += distance;
		if (tonePosition >= size())
			tonePosition -= distance;
		return thisTone();
	}
	
	public int size()
	{
		return availableTones.size();
	}
	
	public int forward()
	{
		return forward(1);
	}
	
	
	public int back(int distance)
	{
		tonePosition -= distance;
		if (tonePosition < 0)
			tonePosition += distance;
		
		return thisTone();
	}
	
	public int back()
	{
		return back(1);
	}
	
	public void goHome()
	{
		tonePosition = availableTones.indexOf(myTone);
	}
	
	public int getHomePosition()
	{
		return availableTones.indexOf(myTone);
	}
}
