package org.zapto.bensmith.midi.form;

import java.util.Properties;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.zapto.bensmith.midi.pitch.PitchGenerator;
import org.zapto.bensmith.midi.rhythm.RhythmGenerator;
import org.zapto.bensmith.midi.vel.VelocityGenerator;

public class TickBasedGenerator extends FormGenerator {

	
	PitchGenerator pitchGenerator;
	RhythmGenerator rhythmGenerator;
	VelocityGenerator velocityGenerator;
	Spinner numTicks;
	int numChannels = 1;
	long lastStartTick = 0L;
	
	public TickBasedGenerator(Composite parent)
	{
		super(parent, SWT.NONE);
		addLabel("This form generater takes a number of ticks a form of type A. 12 ticks = one quarter note. In common time, 48 ticks = one measure.");
		setLayout(new GridLayout(1, true));
		addLabel("Number of Ticks: ");
		numTicks = new Spinner (this, SWT.BORDER);
		numTicks.setMinimum(0);
		numTicks.setMaximum(10000);
		numTicks.setSelection(48);
		pack();
	}
	
	public void addLabel(String text)
	{
		Label addedLabel = new Label(this, SWT.BORDER);
		addedLabel.setText(text);
	}
	
	public VelocityGenerator getVelocityGenerator() {
		return velocityGenerator;
	}

	public void setVelocityGenerator(VelocityGenerator velocityGenerator) {
		this.velocityGenerator = velocityGenerator;
	}

	public int getNumChannels() {
		return numChannels;
	}

	public void setNumChannels(int numChannels) {
		this.numChannels = numChannels;
	}

	public PitchGenerator getPitchGenerator() {
		return pitchGenerator;
	}

	public void setPitchGenerator(PitchGenerator pitchGenerator) {
		this.pitchGenerator = pitchGenerator;
	}

	public RhythmGenerator getRhythmGenerator() {
		return rhythmGenerator;
	}

	public void setRhythmGenerator(RhythmGenerator rhythmGenerator) {
		this.rhythmGenerator = rhythmGenerator;
	}
	
	public int getNextChannel()
	{
		return 0;
	}
	
	public int getNextPitch()
	{
		return pitchGenerator.nextPitch();
	}
	
	public int getNextVelocity()
	{
		return velocityGenerator.nextVelocity();
	}
	
	public long getNextStartTick()
	{
		lastStartTick = rhythmGenerator.nextStart();
		return lastStartTick;
	}
	
	public long getNextLength()
	{
		return rhythmGenerator.nextLength();
	}
	
	public void restart()
	{
		rhythmGenerator.restart();
		pitchGenerator.restart();
		velocityGenerator.restart();
		lastStartTick = 0L;
	}
	
	public boolean isDone()
	{
		return (lastStartTick >= numTicks.getSelection());
	}
	
	public void next()
	{
		pitchGenerator.next();
		rhythmGenerator.next();
		velocityGenerator.next();
	}
	
	public Properties getSettings()
	{
		Properties p = new Properties();
		p.setProperty("basicFormGenerator.numNotes", numTicks.getSelection() + "");
		return p;
	}
	
	public void setSettings(Properties p)
	{
		numTicks.setSelection(Integer.parseInt(p.getProperty("basicFormGenerator.numNotes")));
	}
}
