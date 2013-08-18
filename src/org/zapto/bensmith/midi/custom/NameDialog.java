package org.zapto.bensmith.midi.custom;

import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Random;

import org.eclipse.swt.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import com.apple.eawt.Application;

public class NameDialog {

	
	Display display;
	Shell shell;
	Label result;
	String rootPath;
	Random rng = new Random();
	int numWords = -1;
	
	public void addLabel(String text)
	{
		Label addedLabel = new Label(shell, SWT.BORDER);
		addedLabel.setText(text);
	}
	
	
	public NameDialog(Display d, String rp)
	{
		rootPath = rp;
		display = d;
		
		shell = new Shell(display, SWT.CLOSE | SWT.BORDER);
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		
		
		try
		{
			numWords = getLength();
		}
		catch (Exception e) {}
		
		addLabel(numWords + " words available.");
		
		Button generate = new Button(shell, SWT.PUSH);
		generate.setText("Generate");
		
		generate.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				generateNewName();
			}
		});
		
		result=new Label(shell, SWT.BORDER);
		generateNewName();
		
		shell.layout(true);
		shell.pack();
		shell.open();
		shell.setText("Name Generator");
		
		
		
		while (!display.isDisposed()) {
		if (!display.readAndDispatch ()) display.sleep ();
		}
		shell.dispose();
		
	}
	
	public void generateNewName()
	{
		try
		{
			int firstWord = rng.nextInt(numWords);
			int secondWord = rng.nextInt(numWords);
			result.setText(getLine(firstWord) + " " + getLine(secondWord));
			shell.layout(true);
			shell.pack();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public int getLength() throws Exception
	{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rootPath + "words.english.txt")));
		String str = br.readLine();
		int total=-1;
		while (str != null)
		{
			total++;
			str = br.readLine();
		}
		
		return total;
	}
	
	public String getLine(int lineNum) throws Exception
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rootPath + "words.english.txt")));
		
		String str = br.readLine();
		for (int i=0;i<lineNum;i++)
		{
			str = br.readLine();
		}
		
		return str;
	}
}
