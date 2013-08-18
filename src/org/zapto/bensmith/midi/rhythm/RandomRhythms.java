package org.zapto.bensmith.midi.rhythm;

import java.util.Properties;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;
public class RandomRhythms extends RhythmGenerator {
	
	Spinner startRange, lengthMin, lengthMax;
	Random rng = new Random();

	long startingTick = 0;
	int lengthRange = 0;
	final Button sequential = new Button(this, SWT.CHECK);

	public RandomRhythms(Composite parent)
	{
		super(parent, SWT.NONE);
		setLayout(new RowLayout());
		addLabel("Note Length Maximum: ");
		lengthMax = new Spinner (this, SWT.BORDER);
		lengthMax.setMinimum(0);
		lengthMax.setMaximum(96);
		lengthMax.setSelection(48);
		
		addLabel("Note Length Minimum: ");
		lengthMin = new Spinner (this, SWT.BORDER);
		lengthMin.setMinimum(0);
		lengthMin.setMaximum(96);
		lengthMin.setSelection(1);
		
		addLabel("Note can start between zero ticks and: ");
		startRange = new Spinner (this, SWT.BORDER);
		startRange.setMinimum(0);
		startRange.setMaximum(5000);
		startRange.setSelection(64);
		
		
		sequential.setText("Sequential");
		
		pack();
		
	}
	
	public void addLabel(String text)
	{
		Label addedLabel = new Label(this, SWT.BORDER);
		addedLabel.setText(text);
	}
	
	public RandomRhythms()
	{
		super(new Shell(), SWT.NONE);
	}
	
	public long nextStart()
	{
		if (sequential.getSelection())
		{
			return (long) rng.nextInt(startRange.getSelection()) + startingTick;
		}
		else
		{
			return (long) rng.nextInt(startRange.getSelection()) + 1;
		}
	}
	
	public long nextLength()
	{
		if (sequential.getSelection())
		{
			long toRet = (long) rng.nextInt(lengthRange) + lengthMin.getSelection();
			startingTick += toRet;
			return toRet;
		}
		else
		{
			return (long) rng.nextInt(lengthRange) + lengthMin.getSelection();
		}
	}
	
	public void restart()
	{
		startingTick = 0;
		lengthRange = lengthMax.getSelection() - lengthMin.getSelection();
	}
	
	public Properties getSettings()
	{
		Properties mySettings = new Properties();
		mySettings.put("randomRhythms.startRange", ""+startRange.getSelection());
		mySettings.put("randomRhythms.sequential", ""+sequential.getSelection());
		mySettings.put("randomRhythms.lengthMin", ""+lengthMin.getSelection());
		mySettings.put("randomRhythms.lengthMax", ""+lengthMax.getSelection());
		
		return mySettings;
	}
	
	public void setSettings(Properties p)
	{
		startRange.setSelection(Integer.parseInt(p.getProperty("randomRhythms.startRange")));
		lengthMin.setSelection(Integer.parseInt(p.getProperty("randomRhythms.lengthMin")));
		lengthMax.setSelection(Integer.parseInt(p.getProperty("randomRhythms.lengthMax")));
		sequential.setSelection(Boolean.parseBoolean(p.getProperty("randomRhythms.sequential")));
	}
}
