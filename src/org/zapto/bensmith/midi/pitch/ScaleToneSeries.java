package org.zapto.bensmith.midi.pitch;

import java.util.ArrayList;

public class ScaleToneSeries {

	private int myTone = 0;
	private ArrayList<Integer> availableTones = new ArrayList<Integer>();
	private ArrayList<Integer> scaleSteps= new ArrayList<Integer>();
	private static ArrayList<Integer> majorScalee = new ArrayList<Integer>();
	private static ArrayList<int[]> predefinedScales = new ArrayList<int[]>();
	private static ArrayList<String> predefinedScaleList = new ArrayList<String>();
	private int tonePosition = 0;
	
	public ScaleToneSeries()
	{
		//default constructor gives C major scale
		myTone = 60;
		majorScalee.add(2);
		majorScalee.add(2);
		majorScalee.add(1);
		majorScalee.add(2);
		majorScalee.add(2);
		majorScalee.add(2);
		majorScalee.add(1);
		setScaleSteps(majorScalee);
		loadPredefinedScales();
		initializeToneSeries();
	}
	
	public void loadPredefinedScales()
	{
		predefinedScaleList.clear();
		predefinedScales.clear();
		predefinedScaleList.add("Major");
		int[] majorScale = {2,2,1,2,2,2,1};
		predefinedScales.add(majorScale);
		
		predefinedScaleList.add("Minor");
		int[] minorScale = {2,1,2,2,1,2,2};
		predefinedScales.add(minorScale);
		
		predefinedScaleList.add("Mixolydian");
		int[] mixolydian = {2,2,1,2,2,1,2};
		predefinedScales.add(mixolydian);
		
		predefinedScaleList.add("Lydian");
		int[] lydian = {2,2,2,1,2,2,1};
		predefinedScales.add(lydian);
		
		predefinedScaleList.add("Blues");
		int[] bluesScale = {3,2,1,1,3,2};
		predefinedScales.add(bluesScale);
		
		predefinedScaleList.add("Half/Whole Diminished");
		int[] hwd = {1,2,1,2,1,2,1,2};
		predefinedScales.add(hwd);
		
		predefinedScaleList.add("Whole/Half Diminished");
		int[] whd = {2,1,2,1,2,1,2,1};
		predefinedScales.add(whd);
		
		predefinedScaleList.add("Augmented Scale");
		int[] aug = {3,1,3,1,3,1};
		predefinedScales.add(aug);
		
		predefinedScaleList.add("Whole Tone");
		int[] wt = {2,2,2,2,2,2};
		predefinedScales.add(wt);
		
		predefinedScaleList.add("Chromatic");
		int[] cm = {1,1,1,1,1,1,1,1,1,1,1,1};
		predefinedScales.add(cm);
	}
	
	public ArrayList<String> getPredefinedScaleList()
	{
		return predefinedScaleList;
	}
	
	public ArrayList<int[]> getPredefinedScales()
	{
		return predefinedScales;
	}
	
	public ScaleToneSeries(int startingPitch, ArrayList<Integer> rSS)
	{
		setRootPitch(startingPitch);
		setScaleSteps(rSS);
		initializeToneSeries();
		loadPredefinedScales();
	}
	
	public ScaleToneSeries(int startingPitch)
	{
		//default constructor gives C major scale
		setRootPitch(startingPitch);
		majorScalee.add(2);
		majorScalee.add(2);
		majorScalee.add(1);
		majorScalee.add(2);
		majorScalee.add(2);
		majorScalee.add(2);
		majorScalee.add(1);
		setScaleSteps(majorScalee);
		initializeToneSeries();
		loadPredefinedScales();
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
