package org.zapto.bensmith.midi.pitch;

import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public abstract class PitchGenerator extends Composite {
	
	public PitchGenerator(Composite parent)
	{
		super(parent, SWT.NONE);
		
	}
	
	public PitchGenerator(Composite parent, int style)
	{
		super(parent, style);
		
	}
	
	public PitchGenerator()
	{
		super(null, SWT.NONE);
	}
	
	public int nextPitch() 
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
		return null;
	}
	
	public void setSettings(Properties p)
	{
		
	}
}
