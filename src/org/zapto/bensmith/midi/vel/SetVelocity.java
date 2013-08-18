package org.zapto.bensmith.midi.vel;

import java.util.Properties;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;
public class SetVelocity extends VelocityGenerator {
	
	Spinner velocity;
	Random rng = new Random();

	long startingTick = 1;

	public SetVelocity(Composite parent)
	{
		super(parent, SWT.NONE);
		setLayout(new GridLayout());
		addLabel("Select the velocity:");
		velocity = new Spinner (this, SWT.BORDER);
		velocity.setMinimum(0);
		velocity.setMaximum(126);
		velocity.setSelection(48);
		pack();
	}
	
	public void addLabel(String text)
	{
		Label addedLabel = new Label(this, SWT.BORDER);
		addedLabel.setText(text);
	}
	
	public SetVelocity()
	{
		super(new Shell(), SWT.NONE);
	}
	
	public int nextVelocity()
	{
		return velocity.getSelection();
	}
	
	public Properties getSettings()
	{
		Properties mySettings = new Properties();
		mySettings.put("setVelocity.velocity", ""+velocity.getSelection());
		return mySettings;
	}
	
	public void setSettings(Properties p)
	{
		velocity.setSelection(Integer.parseInt(p.getProperty("setVelocity.velocity")));
	}
}
