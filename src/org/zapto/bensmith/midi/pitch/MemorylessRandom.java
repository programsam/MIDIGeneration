package org.zapto.bensmith.midi.pitch;

import java.util.HashMap;
import java.util.Properties;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;
import org.zapto.bensmith.midi.custom.NoteSpinner;
public class MemorylessRandom extends PitchGenerator {
	
	NoteSpinner minPitch, maxPitch;
	int pitchRange = 0;
	Random rnd = new Random();
	int noteNum = 0;

	public MemorylessRandom(Composite parent)
	{
		super(parent, SWT.NONE);
		setLayout(new RowLayout());
		
		addLabel("Minimum Pitch: ");
		minPitch = new NoteSpinner (this, SWT.BORDER);
		minPitch.setMinimum(0);
		minPitch.setMaximum(126);
		minPitch.setSelection(54);
		
		addLabel("Maximum Pitch: ");
		maxPitch = new NoteSpinner (this, SWT.BORDER);
		maxPitch.setMinimum(0);
		maxPitch.setMaximum(126);
		maxPitch.setSelection(89);
		
		pack();
	}
	
	public void addLabel(String text)
	{
		Label addedLabel = new Label(this, SWT.BORDER);
		addedLabel.setText(text);
	}
	
	public MemorylessRandom()
	{
		super(new Shell(), SWT.NONE);
		
	}
	
	public int nextPitch()
	{
		return rnd.nextInt(pitchRange) + minPitch.getSelection();
	}
	
	
	public void restart()
	{
		noteNum = 0;
		pitchRange = maxPitch.getSelection() - minPitch.getSelection(); 
	}
	
	public void next()
	{
		noteNum++;
	}
	
	
	public Properties getSettings()
	{
		Properties mySettings = new Properties();
		mySettings.put("memorylessRandom.minPitch", ""+minPitch.getSelection());
		mySettings.put("memorylessRandom.maxPitch", ""+maxPitch.getSelection());

		return mySettings;
	}
	
	public void setSettings(Properties p)
	{
		minPitch.setSelection(Integer.parseInt(p.getProperty("memorylessRandom.minPitch")));
		maxPitch.setSelection(Integer.parseInt(p.getProperty("memorylessRandom.maxPitch")));
	}
}
