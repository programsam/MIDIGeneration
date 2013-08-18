package org.zapto.bensmith.midi.pitch;

import java.util.Properties;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;
import org.zapto.bensmith.midi.custom.NoteSpinner;
public class MorphingTriads extends PitchGenerator {
	
	NoteSpinner tonic;
	Random rnd = new Random();
	int noteNum = 0;
	int[] pitches = {60,64,67}; //C major triad

	public MorphingTriads(Composite parent)
	{
		super(parent, SWT.NONE);
		setLayout(new GridLayout());

		addLabel("Root Note: ");
		tonic = new NoteSpinner (this, SWT.BORDER);
		tonic.setMinimum(0);
		tonic.setMaximum(126);
		tonic.setSelection(60);
		
		pack();
	}
	
	public void addLabel(String text)
	{
		Label addedLabel = new Label(this, SWT.BORDER);
		addedLabel.setText(text);
	}
	
	public MorphingTriads()
	{
		super(new Shell(), SWT.NONE);
		
	}
	
	public int nextPitch()
	{
//		System.out.println("---------------");
//		System.out.println("Note num: " + noteNum);
//		printPitches();
//		System.out.println("So, returning: " + pitches[noteNum]);
		return pitches[noteNum];
	}
	
	
	public void restart()
	{
		noteNum = -1;
		
		pitches[0] = tonic.getSelection();
		pitches[1] = tonic.getSelection() + 4;
		pitches[2] = tonic.getSelection() + 7;
	}
	
	private void changeAPitch()
	{
		int toChange = rnd.nextInt(2);
		int deltaChange = rnd.nextInt(5);
		boolean goUp = rnd.nextBoolean();
		if (goUp)
			pitches[toChange]+=deltaChange;
		else
			pitches[toChange]-=deltaChange;
	}
	
	private boolean halfSteps()
	{
		if (pitches[0]+1 == pitches[1] || pitches[0]-1 == pitches[1])
			return true;
		else if (pitches[1]+1 == pitches[2] || pitches[1]-1 == pitches[2])
			return true;
		else if (pitches[2]+1 == pitches[0] || pitches[2]-1 == pitches[0])
			return true;
		else
			return false;
	}
	
	private boolean samePitches()
	{
		if (pitches[0] == pitches[1])
			return true;
		else if (pitches[1] == pitches[2])
			return true;
		else if (pitches[2] == pitches[0])
			return true;
		else
			return false;
	}
	
	public void next()
	{
		noteNum++;
		if (noteNum > 2)
		{
			noteNum = 0;
			if (rnd.nextBoolean())
			{
				changeAPitch();
				while (samePitches() || halfSteps())
					changeAPitch();
				
			}
		}
	}
	
	private void printPitches()
	{
		System.out.print("[");
		System.out.print(pitches[0] + ", ");
		System.out.print(pitches[1] + ", ");
		System.out.print(pitches[2]);
		System.out.println("]");
	}
	
	
	public Properties getSettings()
	{
		Properties p = new Properties();
		p.setProperty("morphingTriads.tonic", tonic.getSelection() + "");
		return p;
	}
	
	public void setSettings(Properties p)
	{
		tonic.setSelection(Integer.parseInt(p.get("morphingTriads.tonic").toString()));
	}
}
