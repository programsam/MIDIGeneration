package org.zapto.bensmith.midi.pitch;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;
import org.zapto.bensmith.midi.MIDIGen;
import org.zapto.bensmith.midi.custom.NoteSpinner;
public class Arpeggiator extends PitchGenerator {
	
	NoteSpinner startPitch, maxPitch, minPitch;
	ArrayList<Spinner> chordShifts;
	ArrayList<Label> chordShiftLabels;
	boolean goingUp = true;
	Random rnd = new Random();
	int noteNum = 0;
	Composite shiftyGroups;
	ArrayList<int[]> chordGroups = new ArrayList<int[]>();
	ArrayList<String> chordTypes = new ArrayList<String>();
	ArrayList<Integer> scaleShiftValues = new ArrayList<Integer>();
	Combo chordCombo;
	ChordToneSeries cts = new ChordToneSeries();
	
	
	public Arpeggiator(Composite parent)
	{
		super(parent, SWT.NONE);
		setLayout(new GridLayout());
		
		Composite spinnerGroup = new Composite(this, SWT.BORDER);
		spinnerGroup.setLayout(new RowLayout());
		
		
		addLabel(spinnerGroup, "Start Pitch: ");
		startPitch = new NoteSpinner (spinnerGroup, SWT.BORDER);
		startPitch.setMinimum(0);
		startPitch.setMaximum(126);
		startPitch.setSelection(60);
		
		addLabel(spinnerGroup, "Min Pitch: ");
		minPitch = new NoteSpinner (spinnerGroup, SWT.BORDER);
		minPitch.setMinimum(0);
		minPitch.setMaximum(126);
		minPitch.setSelection(60);
		
		addLabel(spinnerGroup, "Max Pitch: ");
		maxPitch = new NoteSpinner (spinnerGroup, SWT.BORDER);
		maxPitch.setMinimum(0);
		maxPitch.setMaximum(126);
		maxPitch.setSelection(90);
		
		spinnerGroup.pack(true);
		chordCombo = new Combo(this, SWT.NONE);
		chordCombo.setText("<please select a chord type>");
		chordCombo.setSize(300, 50);
		
		chordCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (chordCombo.getSelectionIndex() != -1)
					setScaleShiftValues(chordGroups.get(chordCombo.getSelectionIndex()));
			}
		});
		
		shiftyGroups = new Composite(this, SWT.BORDER);
		shiftyGroups.setLayout(new GridLayout(2,true));
		
		ArrayList<String> predefinedChordLabels = cts.getPredefinedChordList();
		ArrayList<int[]> predefinedChordShifts = cts.getPredefinedChords();
		
		for (int i=0;i<predefinedChordLabels.size();i++)
		{
			chordCombo.add(predefinedChordLabels.get(i));
			chordGroups.add(predefinedChordShifts.get(i));
		}
		
		chordShifts = new ArrayList<Spinner>();
		chordShiftLabels = new ArrayList<Label>();
		
		setScaleShiftValues(chordGroups.get(0));
		
		pack();
	}
	
	public void addLabel(Composite parent, String text)
	{
		Label addedLabel = new Label(parent, SWT.BORDER);
		addedLabel.setText(text);
	}
	
	public void addLabel(String text)
	{
		addLabel(this, text);
	}
	
	public void loadScaleShiftValues()
	{
		scaleShiftValues.clear();
		for (int i=0;i<chordShifts.size();i++)
		{
			scaleShiftValues.add(i, chordShifts.get(i).getSelection());
		}
	}
	
	public void setScaleShiftValues(int[] theArray)
	{
		shiftyGroups.dispose();
		shiftyGroups = new Composite(this, SWT.BORDER);
		shiftyGroups.setLayout(new GridLayout(2,true));
		
		chordShifts.clear();
		chordShiftLabels.clear();
		
		for (int i=0;i<theArray.length;i++)
		{
			Label addedLabel = new Label(shiftyGroups, SWT.BORDER);
			addedLabel.setText("Scale shift " + (i+1));
			chordShiftLabels.add(addedLabel);
			Spinner x = new Spinner(shiftyGroups, SWT.BORDER);
			x.setMaximum(11);
			x.setMinimum(1);
			x.setSelection(theArray[i]);
			chordShifts.add(x);
		}
		
		shiftyGroups.pack(true);
		layout(true);
		notifyListeners(49129, new Event());
		
	}
	
	
	
	public int nextPitch()
	{
		return cts.thisTone();
	}
	
	public void restart()
	{
		loadScaleShiftValues();
		cts = new ChordToneSeries(startPitch.getSelection(), scaleShiftValues);
		cts.setTonePosition(cts.getHomePosition() - 1);
		noteNum = 0;
		goingUp = true;
	}
	
	public void next()
	{
		noteNum++;
		
		if (cts.thisTone() >= maxPitch.getSelection())
			goingUp = false;
		else if (cts.thisTone() <= minPitch.getSelection())
			goingUp = true;
		
		if (goingUp)
			cts.forward();
		else
			cts.back();
	}
	
	
	public Properties getSettings()
	{
		Properties p = new Properties();
		p.setProperty("arpeggiator.startPitch", startPitch.getSelection() + "");
		p.setProperty("arpeggiator.maxPitch", maxPitch.getSelection() + "");
		p.setProperty("arpeggiator.minPitch", minPitch.getSelection() + "");
		
		//construct current scale shifts array
		loadScaleShiftValues();
		String array = "";
		for (int i=0;i<scaleShiftValues.size();i++)
		{
			array += scaleShiftValues.get(i) + "#";
		}
		p.setProperty("arpeggiator.scaleShifts", array);
		
		return p;
	}
	
	public void setSettings(Properties p)
	{
		startPitch.setSelection(Integer.parseInt(p.get("arpeggiator.startPitch").toString()));
		maxPitch.setSelection(Integer.parseInt(p.get("arpeggiator.maxPitch").toString()));
		minPitch.setSelection(Integer.parseInt(p.get("arpeggiator.minPitch").toString()));
		
		String myScaleShiftValues = p.getProperty("arpeggiator.scaleShifts");
		String[] array = myScaleShiftValues.split("#");
		
		int[] intArray = new int[array.length];
		
		for (int i=0;i<array.length;i++)
		{
			intArray[i] = Integer.parseInt(array[i]);
		}
		
		setScaleShiftValues(intArray);
		
	}
}
