package org.zapto.bensmith.midi.test;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.zapto.bensmith.midi.pitch.MouseBasedPitch;
import org.zapto.bensmith.midi.pitch.TwelveTone;
import org.zapto.bensmith.midi.pitch.VisualStatsPitch;

public class SingleGenerator {

	public static void main(String[] args)
	{
		Display display = new Display ();
		Shell shell = new Shell(display);
		MouseBasedPitch mbp = new MouseBasedPitch(shell);
		shell.pack ();
		shell.setSize(700,800);
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}
}
