package org.zapto.bensmith.midi.rhythm;

import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public abstract class RhythmGenerator extends Composite {
	
	public RhythmGenerator(Composite parent)
	{
		super(parent, SWT.NONE);
		
	}
	
	public RhythmGenerator(Composite parent, int style)
	{
		super(parent, style);
		
	}
	
	public RhythmGenerator()
	{
		super(null, SWT.NONE);
	}
	
	public long nextStart() 
	{
		return 1;
	}
	
	public long nextLength() 
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
