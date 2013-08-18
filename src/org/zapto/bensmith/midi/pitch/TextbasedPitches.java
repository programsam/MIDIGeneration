package org.zapto.bensmith.midi.pitch;

import java.util.Properties;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;
public class TextbasedPitches extends PitchGenerator {
	
	Random rnd = new Random();
	int noteNum = 0;
	int pitchClass = 0;
	int pitch = 0;
	Text myText;

	public TextbasedPitches(Composite parent)
	{
		super(parent, SWT.NONE);
		setLayout(new FillLayout());
		addLabel("Enter text here to generate some notes: ");
		myText = new Text(this, SWT.MULTI | SWT.V_SCROLL);
		
		
		pack();
	}
	
	public void addLabel(String text)
	{
		Label addedLabel = new Label(this, SWT.BORDER);
		addedLabel.setText(text);
	}
	
	public TextbasedPitches()
	{
		super(new Shell(), SWT.NONE);
		
	}
	
	public int nextPitch()
	{
		String txt = myText.getText();
		if (noteNum >= txt.length())
			noteNum = 0;
		if (txt.charAt(noteNum) > 127)
			return 6;
		return txt.charAt(noteNum);
	}
	
	
	public void restart()
	{
		noteNum = 0;
	}
	
	public void next()
	{
		noteNum++;
	}
	
	public Properties getSettings()
	{
		Properties p = new Properties();
		p.setProperty("textbasedPitches.text", myText.getText());
		return p;
	}
	
	public void setSettings(Properties p)
	{
		myText.setText(p.getProperty("textbasedPitches.text"));
	}
}
