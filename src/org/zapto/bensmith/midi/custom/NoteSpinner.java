package org.zapto.bensmith.midi.custom;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

public class NoteSpinner extends Composite {
	
	
	Text myText;
	Button up, dn;
	int myValue = 60;
	int minimum, maximum;
	
	private final String[] noteNamesFlat = {"C", "Db", "D", "Eb", "E", "F",
			"Gb", "G", "Ab", "A", "Bb", "B"};
	
	private final String[] noteNamesSharp = {"C", "C#", "D", "D#", "E", "F",
			"F#", "G", "G#", "A", "A#", "B"};
	
	public NoteSpinner(Composite parent, int style) {
		super(parent, SWT.NONE);
		setLayout(new GridLayout(2, false));
		
		minimum = 12;
		maximum = 127;
		
		myText = new Text(this, style);
		myText.setLayoutData(new GridData(30, 15));
		
		myText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				String retText = ((Text) e.widget).getText();
				validateText(retText);
			}
		});
		
		Composite buttonContainer = new Composite(this, SWT.NONE);
		buttonContainer.setLayout(new FillLayout(SWT.VERTICAL));
		up = new Button(buttonContainer, SWT.ARROW | SWT.UP);
		up.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				myValue++;
				updateValue();
			}
		});
		dn = new Button(buttonContainer, SWT.ARROW | SWT.DOWN);
		dn.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				myValue--;
				updateValue();
			}
		});
		updateValue();
	}
	
	public void setSelection(int newValue)
	{
		myValue = newValue;
		updateValue();
	}
	
	public int getSelection()
	{
		return myValue;
	}
	
	public void setSelection(String newText)
	{
		validateText(newText);
	}
	
	public int getMinimum() {
		return minimum;
	}

	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}

	public int getMaximum() {
		return maximum;
	}

	public void setMaximum(int maximum) {
		this.maximum = maximum;
	}
	
	private void validateText(String theText)
	{
		System.out.println("-------");
		System.out.println("Validating Text: " + theText);
		int noteNum = -1;
		int octave = -1;
		try
		{
			
			if (theText.contains("#") || theText.contains("b")) //enharmonic case
			{
				System.out.println("Enharmonic");
				String noteName = theText.substring(0, 2);
				String octaveStr = theText.substring(2, 3);
				System.out.println("Note Name: " + noteName);
				System.out.println("Octave String: " + octaveStr);
				octave = Integer.parseInt(octaveStr);
				for (int i=0;i<noteNamesFlat.length;i++)
				{
					if (noteNamesFlat[i].equals(noteName) || 
							noteNamesSharp[i].equals(noteName))
					{
						noteNum = i;
					}
				}
			}
			else //harmonic case
			{
				System.out.println("Harmonic");
				String noteName = theText.substring(0, 1);
				String octaveStr = theText.substring(1, 2);
				System.out.println("Note Name: " + noteName);
				System.out.println("Octave String: " + octaveStr);
				
				octave = Integer.parseInt(octaveStr);
				for (int i=0;i<noteNamesFlat.length;i++)
				{
					if (noteNamesFlat[i].equals(noteName) || 
							noteNamesSharp[i].equals(noteName))
					{
						noteNum = i;
					}
				}
			}
		}
		catch (Exception e) {} //ignore parse integer exceptions
		
		if (noteNum == -1 || octave == -1)
		{
			myText.setForeground(new Color(this.getDisplay(), 255, 0, 0));
		}
		else
		{
			myText.setForeground(new Color(this.getDisplay(), 0, 0, 0));
			myValue = ((octave+1)*12) + noteNum;
			System.out.println("new value: " + myValue);
		}
		
	}
	
	private void updateValue()
	{
		if (myValue > maximum)
			myValue = maximum;
		if (myValue < minimum)
			myValue = minimum;
		int note = myValue % 12;
		int octave = (myValue / 12) - 1;
		myText.setText(noteNamesFlat[note] + "" + octave);
		System.out.println("--------");
		System.out.println("My Value: " + myValue);
		System.out.println("Note: " + myValue + " % 12 = " + note);
		System.out.println("Octave: " + myValue + " / 12 = " + octave);
		
	}

	public static void main(String[] args)
	{
		Display display = new Display ();
		Shell shell = new Shell (display);
		shell.setLayout(new GridLayout());
		
		NoteSpinner ns = new NoteSpinner(shell, SWT.BORDER);
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}


	
}
