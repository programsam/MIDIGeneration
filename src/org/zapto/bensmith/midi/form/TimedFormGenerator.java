package org.zapto.bensmith.midi.form;

import java.util.Properties;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.zapto.bensmith.midi.MIDIGen;
import org.zapto.bensmith.midi.pitch.PitchGenerator;
import org.zapto.bensmith.midi.rhythm.RhythmGenerator;
import org.zapto.bensmith.midi.vel.VelocityGenerator;

public class TimedFormGenerator extends FormGenerator {

	
	PitchGenerator pitchGenerator;
	RhythmGenerator rhythmGenerator;
	VelocityGenerator velocityGenerator;
	Spinner minutes, seconds;
	int numChannels = 1;
	long lastTick;
	long maximumTick;
	
	public TimedFormGenerator(Composite parent)
	{
		super(parent, SWT.NONE);
		addLabel("This form generator form of type A that is X seconds long.");
		setLayout(new FillLayout());
		Composite c = new Composite(this, SWT.BORDER);
		c.setLayout(new RowLayout());
		minutes = new Spinner (c, SWT.BORDER);
		minutes.setMinimum(0);
		minutes.setMaximum(30);
		minutes.setSelection(0);
		Label addedLabel = new Label(c, SWT.BORDER);
		addedLabel.setText(":");
		seconds = new Spinner (c, SWT.BORDER);
		seconds.setMinimum(0);
		seconds.setMaximum(59);
		seconds.setSelection(30);
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
		lastTick = rhythmGenerator.nextStart();
		return rhythmGenerator.nextStart();
	}
	
	public long getNextLength()
	{
		return rhythmGenerator.nextLength();
	}
	
	public void restart()
	{
		lastTick = 0L;
		/*
		 * 500000.0 milliseconds per quarter note
		 * or, each quarter note is 0.5 seconds.
		 * 
		 * There are 12 ticks per quarter note.
		 * 
		 * So there are 12 / 0.5 = 24 ticks per second.
		 */
		
		int mins = minutes.getSelection();
		int secs = seconds.getSelection();
		secs += (mins*60);
		maximumTick = secs*24;
		lastTick = 0L;
		MIDIGen.log("The maximum tick is: " + maximumTick);
		MIDIGen.log("And the current tick is: " + lastTick);
		rhythmGenerator.restart();
		pitchGenerator.restart();
		velocityGenerator.restart();
	}
	
	public boolean isDone()
	{
		return (lastTick >= maximumTick);
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
		return p;
	}
	
	public void setSettings(Properties p)
	{
	}
}
