package org.zapto.bensmith.midi.vel;

import java.util.Properties;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;
public class IncrementalVelocity extends VelocityGenerator {
	
	Spinner minVelocity, maxVelocity;
	Button goUp;
	
	int myVelocity;

	public IncrementalVelocity(Composite parent)
	{
		super(parent, SWT.NONE);
		setLayout(new GridLayout());
		addLabel("Minimum Velocity:");
		minVelocity = new Spinner (this, SWT.BORDER);
		minVelocity.setMinimum(0);
		minVelocity.setMaximum(126);
		minVelocity.setSelection(20);
		
		addLabel("Maximum Velocity:");
		maxVelocity = new Spinner (this, SWT.BORDER);
		maxVelocity.setMinimum(0);
		maxVelocity.setMaximum(126);
		maxVelocity.setSelection(100);
		
		addLabel("Checked=>min to max, Unchecked=>max to min");
		goUp = new Button(this, SWT.CHECK);
		
		pack();
	}
	
	public void addLabel(String text)
	{
		Label addedLabel = new Label(this, SWT.BORDER);
		addedLabel.setText(text);
	}
	
	public IncrementalVelocity()
	{
		super(new Shell(), SWT.NONE);
	}
	
	public void restart()
	{
		if (goUp.getSelection())
			myVelocity = minVelocity.getSelection();
		else
			myVelocity = maxVelocity.getSelection();
	}
	
	public int nextVelocity()
	{		
		if (goUp.getSelection())
		{
			myVelocity++;
			if (myVelocity >= 126)
				myVelocity = minVelocity.getSelection();
			return myVelocity;
		}
		else
		{
			myVelocity--;
			if (myVelocity <= 0)
				myVelocity = maxVelocity.getSelection();
			return myVelocity;
		}
	}
	
	public Properties getSettings()
	{
		Properties mySettings = new Properties();
		mySettings.put("incrementalVelocity.minVelocity", ""+minVelocity.getSelection());
		mySettings.put("incrementalVelocity.maxVelocity", ""+maxVelocity.getSelection());
		mySettings.put("incrementalVelocity.goUp", ""+goUp.getSelection());
		
		return mySettings;
	}
	
	public void setSettings(Properties p)
	{
		minVelocity.setSelection(Integer.parseInt(p.getProperty("incrementalVelocity.minVelocity")));
		maxVelocity.setSelection(Integer.parseInt(p.getProperty("incrementalVelocity.maxVelocity")));
		goUp.setSelection(Boolean.parseBoolean(p.getProperty("incrementalVelocity.goUp")));
	}
}
