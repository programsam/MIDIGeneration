package org.zapto.bensmith.midi;

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;

import org.eclipse.swt.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class PlayDialog implements MetaEventListener {

	
	Display display;
	Shell shell;
	MIDIModel myMakeMidi;
	ProgressBar pb;
	Label status;
	Label durationStatus;
	long startingTime = 0l;
	
	
	public void addLabel(String text)
	{
		Label addedLabel = new Label(shell, SWT.BORDER);
		addedLabel.setText(text);
	}
	
	
	public PlayDialog(MIDIModel inc, Display d)
	{
		myMakeMidi = inc;
		display = d;
		
		shell = new Shell(display, SWT.TITLE | SWT.BORDER);
		shell.setLayout(new GridLayout(1, true));
		int xpos = (display.getClientArea().width/2) - 50;
		int ypos = (display.getClientArea().height/2) - 50;
		shell.setLocation(xpos, ypos);
		
		myMakeMidi.sequencer.addMetaEventListener(this);
		
		pb = new ProgressBar(shell, SWT.SMOOTH);
		
		status = new Label(shell, SWT.BORDER);
		status.setSize(300, 20);
		status.setText("Playing...");
		status.pack();
		
		durationStatus = new Label(shell, SWT.BORDER);
		durationStatus.setSize(300, 20);
		durationStatus.setText("Playing...");
		durationStatus.pack();
		
		Button stop = new Button(shell, SWT.PUSH);
		stop.setText("Stop");
		shell.setDefaultButton(stop);
		
		stop.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				
				try
				{
					myMakeMidi.stopMidi();
					shell.dispose();
				}
				catch (Exception e2)
				{
					e2.printStackTrace();
				}
			}
		});
		
		shell.pack();
		shell.open();
		shell.setText("Playing...");
		
		pb.setMaximum((int) myMakeMidi.sequence.getTickLength());
		Thread t = new Thread() {
			public void run() {
				while (myMakeMidi.sequencer.isRunning()) {
				try {Thread.sleep (100);} catch (Throwable th) {}
					if (display.isDisposed()) return;
					display.asyncExec(new Runnable() {
						public void run() {
						if (pb.isDisposed ()) return;
						
							status.setText("" + myMakeMidi.sequencer.getTickPosition() + " / " + myMakeMidi.sequence.getTickLength());
							
							String duration = convertMillisToWatch(myMakeMidi.sequence.getMicrosecondLength());
							long currentTimeMillis = System.currentTimeMillis()*1000;
							long startingTimeMillis = startingTime*1000;
							long elapsedTimeMillis = currentTimeMillis - startingTimeMillis;
							String elapsed = convertMillisToWatch(elapsedTimeMillis);
							
							durationStatus.setText(elapsed + " / " + duration);
							pb.setSelection((int) myMakeMidi.sequencer.getTickPosition());
							status.pack(true);
							durationStatus.pack(true);
						}
					});
				}
			}
		};
		t.start();
		startingTime = System.currentTimeMillis();
		
		while (t.isAlive()) {
		if (!display.readAndDispatch ()) display.sleep ();
		}
		shell.dispose();
		
	}
	
	private String convertMillisToWatch(long milliseconds)
	{
		int seconds = ((int)milliseconds )/ 1000000;
		int minutes = seconds / 60;
		int andSeconds = seconds % 60;
		
		return leadingZero(minutes) + ":" + leadingZero(andSeconds);
	}
	
	private String leadingZero(int number)
	{
		if (number < 10)
			return "0" + number;
		else
			return ""+number;
	}


	@Override
	public void meta(MetaMessage meta) {
		if (meta.getType() == 47)
		{
			try
			{
			     if (myMakeMidi.sequencer.isOpen())
			     {
			    	 if (myMakeMidi.sequencer.isRunning())
						 myMakeMidi.sequencer.stop();
			     }
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
		myMakeMidi.sequencer.removeMetaEventListener(this);
		}
	}
}
