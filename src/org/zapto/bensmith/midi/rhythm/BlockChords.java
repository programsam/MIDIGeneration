package org.zapto.bensmith.midi.rhythm;

import java.util.Properties;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;
public class BlockChords extends RhythmGenerator {
	
	Spinner noteLength;
	Random rng = new Random();

	long startingTick = 1;
	int numNotes = 0;

	public BlockChords(Composite parent)
	{
		super(parent, SWT.NONE);
		setLayout(new GridLayout());
		addLabel("Will combine every group of three pitches into a pitch of desired length note");
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
	
	public BlockChords()
	{
		super(new Shell(), SWT.NONE);
	}
	
	public long nextStart()
	{
		return startingTick;
	}
	
	public long nextLength()
	{
		return noteLength.getSelection();
	}
	
	public void next()
	{
		if (numNotes > 2)
		{
			numNotes = 0;
			startingTick += noteLength.getSelection();
		}
		
		numNotes++;
		
	}
	
	public void restart()
	{
		startingTick = 1;
		numNotes = 0;
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
