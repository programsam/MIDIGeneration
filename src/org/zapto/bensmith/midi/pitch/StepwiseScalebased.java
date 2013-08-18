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
public class StepwiseScalebased extends PitchGenerator {
	
	NoteSpinner startPitch, maxPitch, minPitch;
	Spinner upRange, downRange;
	ArrayList<Spinner> scaleShifts;
	ArrayList<Label> scaleShiftLabels;
	Random rnd = new Random();
	int noteNum = 0;
	Composite shiftyGroups;
	ArrayList<int[]> scaleGroups = new ArrayList<int[]>();
	ArrayList<Integer> pitches = new ArrayList<Integer>();
	ArrayList<String> scaleTypes = new ArrayList<String>();
	ArrayList<Integer> scaleShiftValues = new ArrayList<Integer>();
	Combo scaleCombo;
	Random rng = new Random();
	int currentPos = 0, maxPos = 0, minPos = 0;
	Button allowZero;
	
	
	public StepwiseScalebased(Composite parent)
	{
		super(parent, SWT.NONE);
		setLayout(new FillLayout());
		Composite spinnerGroup = new Composite(this, SWT.BORDER);
		spinnerGroup.setLayout(new GridLayout(2, false));
		
		addLabel(spinnerGroup, "Start Pitch: ");
		startPitch = new NoteSpinner (spinnerGroup, SWT.BORDER);
		startPitch.setMinimum(0);
		startPitch.setMaximum(126);
		startPitch.setSelection(60);
		
		addLabel(spinnerGroup, "Min Pitch: ");
		minPitch = new NoteSpinner (spinnerGroup, SWT.BORDER);
		minPitch.setMinimum(0);
		minPitch.setMaximum(126);
		minPitch.setSelection(58);
		
		addLabel(spinnerGroup, "Max Pitch: ");
		maxPitch = new NoteSpinner (spinnerGroup, SWT.BORDER);
		maxPitch.setMinimum(0);
		maxPitch.setMaximum(126);
		maxPitch.setSelection(90);
		
		addLabel(spinnerGroup, "Up Range: ");
		upRange = new Spinner (spinnerGroup, SWT.BORDER);
		upRange.setMinimum(0);
		upRange.setMaximum(10);
		upRange.setSelection(1);
		
		addLabel(spinnerGroup, "Down Range: ");
		downRange = new Spinner (spinnerGroup, SWT.BORDER);
		downRange.setMinimum(0);
		downRange.setMaximum(10);
		downRange.setSelection(1);
		
		spinnerGroup.pack(true);
		
		allowZero = new Button(spinnerGroup, SWT.CHECK);
		allowZero.setText("Require pitch movement");
		allowZero.setSelection(true);

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
		shiftyGroups.setLayout(new GridLayout(4,true));
		
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
			int minimum = 0;
			if (allowZero.getSelection())
				minimum = 1;
			if (rnd.nextBoolean()) //do we go up?
			{
				int toRet = currentPos + rnd.nextInt(upRange.getSelection()) + minimum;
				if (toRet >= maxPos)
				{
					currentPos = maxPos;
					return pitches.get(maxPos);
				}
				else
				{
					currentPos = toRet;
					return pitches.get(currentPos);
				}
			}
			else
			{
				int toRet = currentPos - rnd.nextInt(upRange.getSelection()) - minimum;
				if (toRet < minPos)
				{
					currentPos = minPos;
					return pitches.get(currentPos);
				}
				else
				{
					currentPos = toRet;
					return pitches.get(currentPos);
				}
			}
	}
	
	public void restart()
	{
		loadScaleShiftValues();
		ScaleToneSeries sts = new ScaleToneSeries(startPitch.getSelection(), scaleShiftValues);
		pitches = sts.getToneSeries();
		currentPos = sts.getHomePosition();
		for (int i=0;i<pitches.size();i++)
		{
			if (pitches.get(i) >= maxPitch.getSelection())
			{
				maxPos = i;
				break;
			}
		}
		
		for (int i=pitches.size()-1;i>0;i--)
		{
			if (pitches.get(i) <= minPitch.getSelection())
			{
				minPos = i;
				break;
			}
		}
		
		noteNum = 0;
	}
	
	public void next()
	{
		noteNum++;
	}
	
	
	public Properties getSettings()
	{
		Properties p = new Properties();
		p.setProperty("stepwiseScalebased.startPitch", startPitch.getSelection() + "");
		p.setProperty("stepwiseScalebased.upRange", upRange.getSelection() + "");
		p.setProperty("stepwiseScalebased.downRange", downRange.getSelection() + "");
		p.setProperty("stepwiseScalebased.maxPitch", maxPitch.getSelection() + "");
		p.setProperty("stepwiseScalebased.minPitch", minPitch.getSelection() + "");
		p.setProperty("stepwiseScalebased.allowZero", allowZero.getSelection() + "");
		loadScaleShiftValues();
		String array = "";
		for (int i=0;i<scaleShiftValues.size();i++)
		{
			array += scaleShiftValues.get(i) + "#";
		}
		p.setProperty("stepwiseScalebased.scaleShifts", array);
		
		return p;
	}
	
	public void setSettings(Properties p)
	{
		startPitch.setSelection(Integer.parseInt(p.get("stepwiseScalebased.startPitch").toString()));
		upRange.setSelection(Integer.parseInt(p.get("stepwiseScalebased.upRange").toString()));
		downRange.setSelection(Integer.parseInt(p.get("stepwiseScalebased.downRange").toString()));
		maxPitch.setSelection(Integer.parseInt(p.get("stepwiseScalebased.maxPitch").toString()));
		minPitch.setSelection(Integer.parseInt(p.get("stepwiseScalebased.minPitch").toString()));
		allowZero.setSelection(Boolean.parseBoolean(p.get("stepwiseScalebased.allowZero").toString()));
		
		String myScaleShiftValues = p.getProperty("stepwiseScalebased.scaleShifts");
		String[] array = myScaleShiftValues.split("#");
		
		int[] intArray = new int[array.length];
		
		for (int i=0;i<array.length;i++)
		{
			intArray[i] = Integer.parseInt(array[i]);
		}
		
		setScaleShiftValues(intArray);
	}
}
