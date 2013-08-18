package org.zapto.bensmith.midi.vel;

import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public abstract class VelocityGenerator extends Composite {
	
	public VelocityGenerator(Composite parent)
	{
		super(parent, SWT.NONE);
		
	}
	
	public VelocityGenerator(Composite parent, int style)
	{
		super(parent, style);
		
	}
	
	public VelocityGenerator()
	{
		super(null, SWT.NONE);
	}
	
	public int nextVelocity() 
	{
		return 1;
	}
	
	public void restart()
	{
		
	}
	
	public void next()
	{
		
	}
	
	public Properties getSettings()
	{
		return new Properties();
	}
	
	public void setSettings(Properties p)
	{
		
	}
}
