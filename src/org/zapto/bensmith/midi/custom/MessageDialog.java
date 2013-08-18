package org.zapto.bensmith.midi.custom;

import org.eclipse.swt.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class MessageDialog {

	
	Display display;
	Shell shell;
	Label message;
	Button OK;
	
	public MessageDialog(Display d, String messageText)
	{
		generateMD(d, messageText, "OK");
	}
	
	public MessageDialog(Display d, String messageText, String buttonText)
	{
		generateMD(d, messageText, buttonText);
	}
	
	public void generateMD(Display d, String messageText, String buttonText)
	{
		display = d;
		display.beep();
		shell = new Shell(display, SWT.TITLE | SWT.BORDER);
		shell.setLayout(new GridLayout());
		int xpos = (display.getClientArea().width/2) - 50;
		int ypos = (display.getClientArea().height/2) - 50;
		shell.setLocation(xpos, ypos);
		message = new Label(shell, SWT.BORDER);
		message.setText(messageText);
		
		OK = new Button(shell, SWT.BORDER);
		
		OK.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				
				try
				{
					shell.dispose();
				}
				catch (Exception e2)
				{
					e2.printStackTrace();
				}
			}
		});
		OK.setText(buttonText);
		shell.setLayout(new GridLayout(1, true));
		shell.pack();
		shell.setDefaultButton(OK);
		
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}
}
