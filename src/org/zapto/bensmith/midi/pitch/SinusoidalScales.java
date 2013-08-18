package org.zapto.bensmith.midi.pitch;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;
import org.zapto.bensmith.midi.MIDIGen;
import org.zapto.bensmith.midi.custom.NoteSpinner;
public class SinusoidalScales extends PitchGenerator {
	
	NoteSpinner startPitch, maxPitch, minPitch;
	ArrayList<Spinner> scaleShifts;
	ArrayList<Label> scaleShiftLabels;
	boolean goingUp = true;
	Random rnd = new Random();
	int noteNum = 0;
	Composite shiftyGroups;
	ArrayList<int[]> scaleGroups = new ArrayList<int[]>();
	ArrayList<String> scaleTypes = new ArrayList<String>();
	ArrayList<Integer> scaleShiftValues = new ArrayList<Integer>();
	Combo scaleCombo;
	ScaleToneSeries sts = new ScaleToneSeries();
	
	
	public SinusoidalScales(Composite parent)
	{
		super(parent, SWT.NONE);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite spinnerGroup = new Composite(this, SWT.BORDER);
		spinnerGroup.setLayout(new GridLayout(2, true));
		
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
		scaleCombo = new Combo(spinnerGroup, SWT.NONE);
		scaleCombo.setText("<please select a scale type>");
		scaleCombo.setSize(300, 50);
		
		scaleCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (scaleCombo.getSelectionIndex() != -1)
					setScaleShiftValues(scaleGroups.get(scaleCombo.getSelectionIndex()));
			}
		});
		
		shiftyGroups = new Composite(this, SWT.BORDER);
		shiftyGroups.setLayout(new GridLayout(2,true));
		
		ScaleToneSeries sts = new ScaleToneSeries();
		ArrayList<int[]> scales = sts.getPredefinedScales();
		ArrayList<String> scaleNames = sts.getPredefinedScaleList();
		
		for (int i=0;i<scales.size();i++)
		{
			scaleCombo.add(scaleNames.get(i));
			scaleGroups.add(scales.get(i));
		}
		
		
		scaleShifts = new ArrayList<Spinner>();
		scaleShiftLabels = new ArrayList<Label>();
		setScaleShiftValues(scales.get(0));
		
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
		for (int i=0;i<scaleShifts.size();i++)
		{
			scaleShiftValues.add(i, scaleShifts.get(i).getSelection());
		}
	}
	
	public void setScaleShiftValues(int[] theArray)
	{
		shiftyGroups.dispose();
		shiftyGroups = new Composite(this, SWT.BORDER);
		shiftyGroups.setLayout(new GridLayout(4,true));
		
		scaleShifts.clear();
		scaleShiftLabels.clear();
		
		for (int i=0;i<theArray.length;i++)
		{
			Label addedLabel = new Label(shiftyGroups, SWT.BORDER);
			addedLabel.setText("Scale shift " + (i+1));
			scaleShiftLabels.add(addedLabel);
			Spinner x = new Spinner(shiftyGroups, SWT.BORDER);
			x.setMaximum(5);
			x.setMinimum(1);
			x.setSelection(theArray[i]);
			scaleShifts.add(x);
		}
		
		notifyListeners(49129, new Event());
		shiftyGroups.pack(true);
		layout(true);
	}
	
	
	
	public int nextPitch()
	{
		return sts.thisTone();
	}
	
	public void restart()
	{
		loadScaleShiftValues();
		sts = new ScaleToneSeries(startPitch.getSelection(), scaleShiftValues);
		sts.setTonePosition(sts.getHomePosition() - 1);
		noteNum = 0;
		goingUp = true;
	}
	
	public void next()
	{
		noteNum++;
		
		if (sts.thisTone() >= maxPitch.getSelection())
			goingUp = false;
		else if (sts.thisTone() <= minPitch.getSelection())
			goingUp = true;
		
		if (goingUp)
			sts.forward();
		else
			sts.back();
	}
	
	public Properties getSettings()
	{
		Properties p = new Properties();
		p.setProperty("sinusoidalScales.startPitch", startPitch.getSelection() + "");
		p.setProperty("sinusoidalScales.maxPitch", maxPitch.getSelection() + "");
		p.setProperty("sinusoidalScales.minPitch", minPitch.getSelection() + "");
		
		//construct current scale shifts array
		loadScaleShiftValues();
		String array = "";
		for (int i=0;i<scaleShiftValues.size();i++)
		{
			array += scaleShiftValues.get(i) + "#";
		}
		p.setProperty("sinusoidalScales.scaleShifts", array);
		
		return p;
	}
	
	public void setSettings(Properties p)
	{
		startPitch.setSelection(Integer.parseInt(p.get("sinusoidalScales.startPitch").toString()));
		maxPitch.setSelection(Integer.parseInt(p.get("sinusoidalScales.maxPitch").toString()));
		minPitch.setSelection(Integer.parseInt(p.get("sinusoidalScales.minPitch").toString()));
		
		String myScaleShiftValues = p.getProperty("sinusoidalScales.scaleShifts");
		String[] array = myScaleShiftValues.split("#");
		
		int[] intArray = new int[array.length];
		
		for (int i=0;i<array.length;i++)
		{
			intArray[i] = Integer.parseInt(array[i]);
		}
		
		setScaleShiftValues(intArray);
		
	}
}
