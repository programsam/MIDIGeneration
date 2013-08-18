package org.zapto.bensmith.midi.pitch;

import java.util.Properties;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;
import org.zapto.bensmith.midi.custom.NoteSpinner;
public class StepwiseRandom extends PitchGenerator {
	
	NoteSpinner startPitch,maxPitch, minPitch;
	Spinner  upRange, downRange;
	Random rnd = new Random();
	int noteNum = 0;
	int prevNote = 0;
	Button allowZero;
	
	public StepwiseRandom(Composite parent)
	{
		super(parent, SWT.NONE);
		setLayout(new RowLayout());
		allowZero = new Button(this, SWT.CHECK);
		allowZero.setText("Check to require pitch movement");
		
		addLabel("Starting Pitch: ");
		startPitch = new NoteSpinner (this, SWT.BORDER);
		startPitch.setMinimum(0);
		startPitch.setMaximum(126);
		startPitch.setSelection(64);
		
		addLabel("Up Range (semitones): ");
		upRange = new Spinner (this, SWT.BORDER);
		upRange.setMinimum(0);
		upRange.setMaximum(10);
		upRange.setSelection(1);
		
		addLabel("Down Range (semitones): ");
		downRange = new Spinner (this, SWT.BORDER);
		downRange.setMinimum(0);
		downRange.setMaximum(10);
		downRange.setSelection(1);
		
		addLabel("Max Pitch: ");
		maxPitch = new NoteSpinner (this, SWT.BORDER);
		maxPitch.setMinimum(0);
		maxPitch.setMaximum(126);
		maxPitch.setSelection(100);
		
		addLabel("Min Pitch: ");
		minPitch = new NoteSpinner (this, SWT.BORDER);
		minPitch.setMinimum(0);
		minPitch.setMaximum(126);
		minPitch.setSelection(40);
		
		
		pack();
	}
	
	public void addLabel(String text)
	{
		Label addedLabel = new Label(this, SWT.BORDER);
		addedLabel.setText(text);
	}
	
	public StepwiseRandom()
	{
		super(new Shell(), SWT.NONE);
		
	}
	
	public int nextPitch()
	{
		if (noteNum == 1)
		{
			prevNote = startPitch.getSelection();
			return startPitch.getSelection();
		}
		else
		{
			int minimum = 0;
			if (allowZero.getSelection())
				minimum = 1;
			if (rnd.nextBoolean()) //do we go up?
			{
				int toRet = prevNote + rnd.nextInt(upRange.getSelection()) + minimum;
				if (toRet >= maxPitch.getSelection())
				{
					prevNote = maxPitch.getSelection();
					return maxPitch.getSelection();
				}
				else
				{
					prevNote = toRet;
					return toRet;
				}
			}
			else
			{
				int toRet = prevNote - rnd.nextInt(upRange.getSelection()) - minimum;
				if (toRet < minPitch.getSelection())
				{
					prevNote = minPitch.getSelection();
					return minPitch.getSelection();
				}
				else
				{
					prevNote = toRet;
					return toRet;
				}
			}
		}
	}
	
	
	public void restart()
	{
		noteNum = 0;
	}
	
	public void next()
	{
		noteNum++;
	}
	
	
	public Properties getSettings()
	{
		Properties p = new Properties();
		p.setProperty("stepwiseRandom.startPitch", startPitch.getSelection() + "");
		p.setProperty("stepwiseRandom.upRange", upRange.getSelection() + "");
		p.setProperty("stepwiseRandom.downRange", downRange.getSelection() + "");
		p.setProperty("stepwiseRandom.maxPitch", maxPitch.getSelection() + "");
		p.setProperty("stepwiseRandom.minPitch", minPitch.getSelection() + "");
		p.setProperty("stepwiseRandom.allowZero", allowZero.getSelection() + "");
		
		return p;
	}
	
	public void setSettings(Properties p)
	{
		startPitch.setSelection(Integer.parseInt(p.get("stepwiseRandom.startPitch").toString()));
		upRange.setSelection(Integer.parseInt(p.get("stepwiseRandom.upRange").toString()));
		downRange.setSelection(Integer.parseInt(p.get("stepwiseRandom.downRange").toString()));
		maxPitch.setSelection(Integer.parseInt(p.get("stepwiseRandom.maxPitch").toString()));
		minPitch.setSelection(Integer.parseInt(p.get("stepwiseRandom.minPitch").toString()));
		allowZero.setSelection(Boolean.parseBoolean(p.get("stepwiseRandom.allowZero").toString()));
		
	}
}
