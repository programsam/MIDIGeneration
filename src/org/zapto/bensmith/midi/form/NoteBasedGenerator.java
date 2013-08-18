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

public class NoteBasedGenerator extends FormGenerator {

	
	PitchGenerator pitchGenerator;
	RhythmGenerator rhythmGenerator;
	VelocityGenerator velocityGenerator;
	Spinner numNotes;
	int numChannels = 1;
	int noteNum = 0;
	
	public NoteBasedGenerator(Composite parent)
	{
		super(parent, SWT.NONE);
		addLabel("This form generater takes a number of notes (MIDI events) and produces a form of type A.  The length will vary depending on the number of notes.");
		setLayout(new GridLayout(1, true));
		addLabel("Number of Notes: ");
		numNotes = new Spinner (this, SWT.BORDER);
		numNotes.setMinimum(0);
		numNotes.setMaximum(1000);
		numNotes.setSelection(40);
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
		return rhythmGenerator.nextStart();
	}
	
	public long getNextLength()
	{
		return rhythmGenerator.nextLength();
	}
	
	public void restart()
	{
		noteNum = 0;
		rhythmGenerator.restart();
		pitchGenerator.restart();
		velocityGenerator.restart();
	}
	
	public boolean isDone()
	{
		return (noteNum >= numNotes.getSelection());
	}
	
	public void next()
	{
		noteNum++;
		pitchGenerator.next();
		rhythmGenerator.next();
		velocityGenerator.next();
	}
	
	public Properties getSettings()
	{
		Properties p = new Properties();
		p.setProperty("basicFormGenerator.numNotes", numNotes.getSelection() + "");
		return p;
	}
	
	public void setSettings(Properties p)
	{
		numNotes.setSelection(Integer.parseInt(p.getProperty("basicFormGenerator.numNotes")));
	}
}
