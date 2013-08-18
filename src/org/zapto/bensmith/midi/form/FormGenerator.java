package org.zapto.bensmith.midi.form;

import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.zapto.bensmith.midi.pitch.PitchGenerator;
import org.zapto.bensmith.midi.rhythm.RhythmGenerator;
import org.zapto.bensmith.midi.vel.VelocityGenerator;

public abstract class FormGenerator extends Composite {

	
	public FormGenerator(Composite parent)
	{
		super(parent, SWT.NONE);
		
	}
	
	public FormGenerator(Composite parent, int style)
	{
		super(parent, style);
		
	}
	
	public VelocityGenerator getVelocityGenerator() {
		return null;
	}

	public void setVelocityGenerator(VelocityGenerator velocityGenerator) {
		
	}

	public int getNumChannels() {
		return 0;
	}

	public void setNumChannels(int numChannels) {
		
	}

	public PitchGenerator getPitchGenerator() {
		return null;
	}

	public void setPitchGenerator(PitchGenerator pitchGenerator) {
	}

	public RhythmGenerator getRhythmGenerator() {
		return null;
	}

	public void setRhythmGenerator(RhythmGenerator rhythmGenerator) {
		
	}
	
	public int getNextChannel()
	{
		return 0;
	}
	
	public int getNextPitch()
	{
		return 0;
	}
	
	public int getNextVelocity()
	{
		return 0;
	}
	
	public long getNextStartTick()
	{
		return 0;
	}
	
	public long getNextLength()
	{
		return 0;
	}
	
	public void restart()
	{

	}
	
	public boolean isDone()
	{
		return true;
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
