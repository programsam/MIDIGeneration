package org.zapto.bensmith.midi.form;

import java.util.ArrayList;
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

public class AABAFormGenerator extends FormGenerator {

	
	PitchGenerator pitchGenerator;
	RhythmGenerator rhythmGenerator;
	VelocityGenerator velocityGenerator;
	Spinner partLength;
	int numChannels = 1;
	int formPartNum = 0;
	int mmeNum = -1;
	long offset = 0;
	int totalEvents = 0;
	int currentEvent = 0;
	
	private ArrayList<MyMIDIEvents> aPart;
	private ArrayList<MyMIDIEvents> bPart;
	
	private ArrayList<MyMIDIEvents> currentPart;
	
	public AABAFormGenerator(Composite parent)
	{
		super(parent, SWT.NONE);
		addLabel("This form generater takes a form part length (48 ticks = 1 measure) and generates a song of form AABA.");
		setLayout(new GridLayout(2, true));
		addLabel("Length of Form Part: ");
		partLength = new Spinner (this, SWT.BORDER);
		partLength.setMinimum(0);
		partLength.setMaximum(1536);
		partLength.setSelection(192);
		aPart = new ArrayList<MyMIDIEvents>();
		bPart = new ArrayList<MyMIDIEvents>();
		
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
		return currentPart.get(mmeNum).getPitch();
	}
	
	public int getNextVelocity()
	{
		return currentPart.get(mmeNum).getVelocity();
	}
	
	public long getNextStartTick()
	{
		return currentPart.get(mmeNum).getStart() + offset;
	}
	
	public long getNextLength()
	{
		return currentPart.get(mmeNum).getLength();
	}
	
	public void restart()
	{
		mmeNum = -1;
		rhythmGenerator.restart();
		pitchGenerator.restart();
		velocityGenerator.restart();
		aPart.clear();
		bPart.clear();
		formPartNum = 0;
		offset = 0;
		long start = -1;
		 while (start < partLength.getSelection())
		 {
			pitchGenerator.next();
			int pitch = pitchGenerator.nextPitch();
			velocityGenerator.next();
			int vel = velocityGenerator.nextVelocity();
			rhythmGenerator.next();
			start = rhythmGenerator.nextStart();
			long length = rhythmGenerator.nextLength();
			MyMIDIEvents mme = new MyMIDIEvents();
			mme.setLength(length);
			mme.setStart(start);
			mme.setPitch(pitch);
			mme.setVelocity(vel);
			if ((start+length) > partLength.getSelection())
				continue;
			if (start > partLength.getSelection())
				continue;
			aPart.add(mme);
	     }
		 
		 rhythmGenerator.restart();
			pitchGenerator.restart();
			velocityGenerator.restart();
			start = -1;
			 while (start < partLength.getSelection())
			 {
				pitchGenerator.next();
				int pitch = pitchGenerator.nextPitch();
				velocityGenerator.next();
				int vel = velocityGenerator.nextVelocity();
				rhythmGenerator.next();
				start = rhythmGenerator.nextStart();
				long length = rhythmGenerator.nextLength();
				MyMIDIEvents mme = new MyMIDIEvents();
				mme.setLength(length);
				mme.setStart(start);
				mme.setPitch(pitch);
				mme.setVelocity(vel);
				if ((start+length) > partLength.getSelection())
					continue;
				if (start > partLength.getSelection())
					continue;
				bPart.add(mme);
			 }
			 
			 currentEvent = 0;
			 currentPart = aPart;
			 totalEvents = aPart.size() + aPart.size() + bPart.size() + aPart.size();
	}
	
	public boolean isDone()
	{
		return currentEvent >= totalEvents;
	}
	
	public void next()
	{
		currentEvent++;
		if (mmeNum >= (currentPart.size()-1))
		{
			mmeNum = -1;
			formPartNum++;
			offset = (partLength.getSelection()) * formPartNum;
		}
		
		if (formPartNum == 0 || formPartNum == 1 || formPartNum == 3)
		{
			currentPart = aPart;
		}
		else if (formPartNum == 2)
		{
			currentPart = bPart;
		}
		else
		{
			currentPart = null;
		}
		
		mmeNum++;
	}
	
	public Properties getSettings()
	{
		Properties p = new Properties();
		p.setProperty("aabaFormGenerator.partLength", partLength.getSelection() + "");
		return p;
	}
	
	public void setSettings(Properties p)
	{
		partLength.setSelection(Integer.parseInt(p.getProperty("aabaFormGenerator.partLength")));
	}
}
