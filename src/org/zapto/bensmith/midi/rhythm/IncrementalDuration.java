package org.zapto.bensmith.midi.rhythm;

import java.util.Properties;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;
public class IncrementalDuration extends RhythmGenerator {
	
	Spinner minLength, maxLength;
	
	Button goUp;
	Random rng = new Random();

	long startingTick = 1;
	long currentLen = 1L;

	public IncrementalDuration(Composite parent)
	{
		super(parent, SWT.NONE);
		setLayout(new GridLayout());
		addLabel("Select minimum note duration (12 ticks = quarter note):");
		minLength = new Spinner (this, SWT.BORDER);
		minLength.setMinimum(0);
		minLength.setMaximum(40);
		minLength.setSelection(1);
		addLabel("Select maximum note duration (12 ticks = quarter note):");
		
		maxLength = new Spinner (this, SWT.BORDER);
		maxLength.setMinimum(0);
		maxLength.setMaximum(40);
		maxLength.setSelection(20);
		
		addLabel("Checked=>min to max, Unchecked=>max to min");
		goUp = new Button(this, SWT.CHECK);
		
		pack();
	}
	
	public void addLabel(String text)
	{
		Label addedLabel = new Label(this, SWT.BORDER);
		addedLabel.setText(text);
	}
	
	public IncrementalDuration()
	{
		super(new Shell(), SWT.NONE);
	}
	
	public long nextStart()
	{
		long toRet = startingTick;
		startingTick+=currentLen;
		return toRet;
	}
	
	public long nextLength()
	{
		return currentLen;
	}
	
	public void restart()
	{
		startingTick = 1;
		if (goUp.getSelection())
			currentLen = minLength.getSelection();
		else
			currentLen = maxLength.getSelection();
	}
	
	public void next()
	{
		if (goUp.getSelection())
		{
			currentLen++;
			if (currentLen >= maxLength.getSelection())
				currentLen = minLength.getSelection();
		}
		else
		{
			currentLen--;
			if (currentLen <= minLength.getSelection())
				currentLen = maxLength.getSelection();
		}
	}
	
	public Properties getSettings()
	{
		Properties mySettings = new Properties();
		mySettings.put("incrementalDuration.maxLength", ""+maxLength.getSelection());
		mySettings.put("incrementalDuration.minLength", ""+minLength.getSelection());
		mySettings.put("incrementalDuration.goUp", ""+goUp.getSelection());

		return mySettings;
	}
	
	public void setSettings(Properties p)
	{
		maxLength.setSelection(Integer.parseInt(p.getProperty("incrementalDuration.maxLength")));
		minLength.setSelection(Integer.parseInt(p.getProperty("incrementalDuration.minLength")));
		goUp.setSelection(Boolean.parseBoolean(p.getProperty("incrementalDuration.goUp")));
	}
}
