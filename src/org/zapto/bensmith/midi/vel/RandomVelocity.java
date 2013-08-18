package org.zapto.bensmith.midi.vel;

import java.util.Properties;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;
public class RandomVelocity extends VelocityGenerator {
	
	Spinner velocityMin, velocityMax;
	Random rng = new Random();
	int velocityRange = 0;
	public RandomVelocity(Composite parent)
	{
		super(parent, SWT.NONE);
		setLayout(new RowLayout());
		addLabel("Velocity Minimum: ");
		velocityMin = new Spinner (this, SWT.BORDER);
		velocityMin.setMinimum(0);
		velocityMin.setMaximum(126);
		velocityMin.setSelection(20);
		
		addLabel("Velocity Maximum: ");
		velocityMax = new Spinner (this, SWT.BORDER);
		velocityMax.setMinimum(0);
		velocityMax.setMaximum(126);
		velocityMax.setSelection(100);
		
		pack();
	}
	
	public void addLabel(String text)
	{
		Label addedLabel = new Label(this, SWT.BORDER);
		addedLabel.setText(text);
	}
	
	public RandomVelocity()
	{
		super(new Shell(), SWT.NONE);
		
	}
	
	public int nextVelocity()
	{
		return rng.nextInt(velocityRange) + velocityMin.getSelection();
	}
	
	public void restart()
	{
		velocityRange = velocityMax.getSelection() - velocityMin.getSelection();
	}
	
	
	public Properties getSettings()
	{
		Properties mySettings = new Properties();
		mySettings.put("randomVelocity.velocityMin", ""+velocityMin.getSelection());
		mySettings.put("randomVelocity.velocityMax", ""+velocityMax.getSelection());
		
		return mySettings;
	}
	
	public void setSettings(Properties p)
	{
		velocityMin.setSelection(Integer.parseInt(p.getProperty("randomVelocity.velocityMin")));
		velocityMax.setSelection(Integer.parseInt(p.getProperty("randomVelocity.velocityMac")));
	}
}
