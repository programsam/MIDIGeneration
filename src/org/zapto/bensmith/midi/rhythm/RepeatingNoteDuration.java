package org.zapto.bensmith.midi.rhythm;

import java.util.Properties;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;
public class RepeatingNoteDuration extends RhythmGenerator {
	
	Spinner noteLength;
	Random rng = new Random();

	long startingTick = 0;

	public RepeatingNoteDuration(Composite parent)
	{
		super(parent, SWT.NONE);
		setLayout(new GridLayout());
		addLabel("12 ticks is a quarter note. Select the note length:");
		noteLength = new Spinner (this, SWT.BORDER);
		noteLength.setMinimum(0);
		noteLength.setMaximum(200);
		noteLength.setSelection(4);
		
		pack();
	}
	
	
	public void addLabel(String text)
	{
		Label addedLabel = new Label(this, SWT.BORDER);
		addedLabel.setText(text);
	}
	
	
	public long nextStart()
	{
		return startingTick;
	}
	
	public void next()
	{
		startingTick += noteLength.getSelection();
	}
	
	public long nextLength()
	{
		return noteLength.getSelection();
	}
	
	public void restart()
	{
		startingTick = 0L - noteLength.getSelection();
	}
	
	public Properties getSettings()
	{
		Properties mySettings = new Properties();
		mySettings.put("repeatingNoteDuration.duration", ""+noteLength.getSelection());
		return mySettings;
	}
	
	public void setSettings(Properties p)
	{
		noteLength.setSelection(Integer.parseInt(p.getProperty("repeatingNoteDuration.duration")));
	}
}
