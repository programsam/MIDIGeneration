package org.zapto.bensmith.midi.pitch;

import java.util.Properties;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;
import org.zapto.bensmith.midi.custom.NoteSpinner;
public class RepeatingNote extends PitchGenerator {
	
	NoteSpinner pitch;
	Random rnd = new Random();

	public RepeatingNote(Composite parent)
	{
		super(parent, SWT.NONE);
		setLayout(new GridLayout());

		addLabel("Pitch: ");
		pitch = new NoteSpinner (this, SWT.BORDER);
		pitch.setMinimum(0);
		pitch.setMaximum(126);
		pitch.setSelection(40);
		pack();
	}
	
	public void addLabel(String text)
	{
		Label addedLabel = new Label(this, SWT.BORDER);
		addedLabel.setText(text);
	}
	
	public RepeatingNote()
	{
		super(new Shell(), SWT.NONE);
		
	}
	
	public int nextPitch()
	{
		return pitch.getSelection();
	}
	
	
	public void restart()
	{
	}
	
	public void next()
	{
	}
	
	
	public Properties getSettings()
	{
		Properties p = new Properties();
		p.setProperty("repeatingNote.pitch", pitch.getSelection() + "");
		return p;
	}
	
	public void setSettings(Properties p)
	{
		pitch.setSelection(Integer.parseInt(p.get("repeatingNote.pitch").toString()));
	}
}
