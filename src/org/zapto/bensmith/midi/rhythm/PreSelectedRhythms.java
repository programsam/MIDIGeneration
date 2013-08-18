package org.zapto.bensmith.midi.rhythm;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;
import org.zapto.bensmith.midi.MIDIGen;
import org.zapto.bensmith.midi.custom.NameDialog;
import org.zapto.bensmith.midi.custom.RootPathGetter;

public class PreSelectedRhythms extends RhythmGenerator {
	
	Random rng = new Random();

	long offsetTick = 0;
	ArrayList<RhythmList> availableRhythms;
	RhythmList myRhythm;
	ArrayList<Button> options;
	
	
	public PreSelectedRhythms(Composite parent)
	{
		super(parent, SWT.NONE);
		setLayout(new GridLayout(9, true));
		options = new ArrayList<Button>();
		
		//and yes these will appear in the list in order
		addNewOption(new long[] {0}, new long[] {72}, 72, "dottedWhole.png");
		addNewOption(new long[] {0}, new long[] {48}, 48L, "wholeNote.png");
		addNewOption(new long[] {0}, new long[] {36}, 36L, "dottedHalf.png");
		addNewOption(new long[] {0}, new long[] {24}, 24L, "halfNote.png");
		addNewOption(new long[] {0}, new long[] {18}, 18L, "dottedQuarter.png");
		addNewOption(new long[] {0}, new long[] {12}, 12L, "quarterNote.png");
		addNewOption(new long[] {0,6}, new long[] {6,6}, 12L, "eighthNotes.png");
		addNewOption(new long[] {0,4,8}, new long[] {4,4,4}, 12L, "triplets.png");
		addNewOption(new long[] {0,3,6,9}, new long[] {3,3,3,3}, 12L, "sixteenthNotes.png");
		addNewOption(new long[] {0,2,4,6,8,10}, new long[] {2,2,2,2,2,2}, 12L, "sextuplets.png");
		addNewOption(new long[] {0,6,9}, new long[] {6,3,3}, 12L, "1anda.png");
		addNewOption(new long[] {0,3,9}, new long[] {3,6,3}, 12L, "1eresta.png");
		addNewOption(new long[] {0,3,6}, new long[] {3,3,3}, 12L, "1eand.png");
		addNewOption(new long[] {0,8,16}, new long[] {8,8,8}, 24L, "quarterNoteTriplets.png");
		addNewOption(new long[] {0,8}, new long[] {4,4}, 12L, "1le.png");
		addNewOption(new long[] {}, new long[] {}, 48L, "wholeRest.png");
		addNewOption(new long[] {}, new long[] {}, 24, "halfRest.png");
		addNewOption(new long[] {}, new long[] {}, 12L, "quarterRest.png");
		addNewOption(new long[] {}, new long[] {}, 6L, "eighthRests.png");
		addNewOption(new long[] {}, new long[] {}, 3L, "sixteenthRest.png");
		
		
		availableRhythms = new ArrayList<RhythmList>();
		
		pack();
	}
	
	private void addNewOption(long[] starts, long[] lengths, long offsetDelta, String imageName)
	{
		RootPathGetter rpg = new RootPathGetter();
		
		Button x = new Button(this, SWT.CHECK);
		x.setImage(new Image(getDisplay(), rpg.getRootPath() + "img/" + imageName));
		RhythmList rl = new RhythmList();
		rl.setLengths(lengths);
		rl.setStarts(starts);
		rl.setOffsetDelta(offsetDelta);
		x.setData(rl);
		x.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				reassimilateList();
			}
		});
		options.add(x);
	}
	
	private void reassimilateList()
	{
		availableRhythms.clear();
		for (int i=0;i<options.size();i++)
		{
			if (options.get(i).getSelection())
				availableRhythms.add((RhythmList) options.get(i).getData());
		}
	}
	
	public void addLabel(String text)
	{
		Label addedLabel = new Label(this, SWT.BORDER);
		addedLabel.setText(text);
	}
	
	
	public void restart()
	{
		reassimilateList();
		if (availableRhythms.size() == 0)
		{
			options.get(0).setSelection(true);
			reassimilateList();
		}
		
		for (int i=0;i<availableRhythms.size();i++)
			availableRhythms.get(i).restart();
		offsetTick = 0L;
		myRhythm = null;
	}
	
	public void next()
	{
		if (null == myRhythm)
		{
			do
			{
				myRhythm = availableRhythms.get(rng.nextInt(availableRhythms.size()));
				myRhythm.restart();
				if (myRhythm.isRest())
					offsetTick += myRhythm.getOffsetDelta();
			} while (myRhythm.isRest());
		}
		else if (myRhythm.next() == false)
		{
			offsetTick += myRhythm.getOffsetDelta();
			do
			{
				myRhythm = availableRhythms.get(rng.nextInt(availableRhythms.size()));
				myRhythm.restart();
				if (myRhythm.isRest())
					offsetTick += myRhythm.getOffsetDelta();
			} while (myRhythm.isRest());
		}
		
	}
	
	public long nextStart()
	{
		return offsetTick + myRhythm.nextStart();
	}
	
	public long nextLength()
	{
		return myRhythm.nextLength();
	}
	
	
	public Properties getSettings()
	{
		Properties p = new Properties();
		String toRet = "";
		for (int i=0;i<options.size();i++)
		{
			toRet += options.get(i).getSelection() + ",";
		}

		p.setProperty("preselectedRhythms.rhythms", toRet);
		return p;
	}
	
	public void setSettings(Properties p)
	{
		String incoming = p.getProperty("preselectedRhythms.rhythms");
		String[] array = incoming.split(",");
		
		for (int i=0;i<array.length;i++)
			options.get(i).setSelection(Boolean.parseBoolean(array[i]));
		
		reassimilateList();
		
	}
}
