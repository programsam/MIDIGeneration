package org.zapto.bensmith.midi.pitch;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;
public class TwelveTone extends PitchGenerator {
	
	/**
	 * Ala Josh Marcus Twelve Tone Music Theory
	 * 
	 *  p0 = the first set of pitches
	 *  
	 * 
	 */
	
	
	Composite rowGroup;
	ArrayList<Spinner> theFundamentalRow;
	ArrayList<Label> rowLabels;
	ArrayList<int[]> rowsToUse;
	Table pitchTable;
	Random rnd = new Random();
	int noteNum = 0;
	int pitch = 0;
	int ofTwelve = -1;
	int[] currentRow;
	int rowNumber;
	Random rng = new Random();
	
	List primeList, inversionList, retrogradeList, retroInvList;
	int[][] primes, inversions, retrograde, retrogradeInversions;
	
	String[] noteNames = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

	public TwelveTone(Composite parent)
	{
		super(parent, SWT.NONE);
		setLayout(new GridLayout(3, false));
		
		
		rowGroup = new Composite(this, SWT.BORDER);
		rowGroup.setLayout(new RowLayout());
		
		theFundamentalRow = new ArrayList<Spinner>();
		for (int i=0;i<12;i++)
		{
			Spinner thisOne = new Spinner(rowGroup, SWT.BORDER);
			thisOne.setMinimum(0);
			thisOne.setMaximum(11);
			thisOne.setSelection(i);
			thisOne.addListener (SWT.Selection, new Listener () {
				public void handleEvent (Event e) {
					populateTable();
				}
			});
			
			theFundamentalRow.add(thisOne);
		}
		
		
		Composite themRows = new Composite(this, SWT.BORDER);
		themRows.setLayout(new RowLayout());
		
		//prime set (P), inversion (I), retrograde inversion (RI), retrograde (R)
		//List primeList, inversionList, retrogradeList, retroInvList;
		
		primeList = new List (themRows, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		for (int i=0; i<11; i++) 
			primeList.add ("P" + i);
		Rectangle clientArea = this.getClientArea ();
		primeList.setBounds (clientArea.x, clientArea.y, 100, 100);
		primeList.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				setRowsToUse();
			}
		});
		primeList.setSelection(0);
		
		inversionList = new List (themRows, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		for (int i=0; i<11; i++)
			inversionList.add ("I" + i);
		inversionList.setBounds (clientArea.x, clientArea.y, 100, 100);
		inversionList.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				setRowsToUse();
			}
		});
		
		retrogradeList = new List (themRows, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		for (int i=0; i<11; i++) 
			retrogradeList.add ("R" + i);
		retrogradeList.setBounds (clientArea.x, clientArea.y, 100, 100);
		retrogradeList.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				setRowsToUse();
			}
		});
		
		retroInvList = new List (themRows, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		for (int i=0; i<11; i++) 
			retroInvList.add ("RI" + i);
		retroInvList.setBounds (clientArea.x, clientArea.y, 100, 100);
		retroInvList.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				setRowsToUse();
			}
		});
		
		//Set up the table
		pitchTable = new Table (this, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.VIRTUAL);
		pitchTable.setLinesVisible (true);
		pitchTable.setHeaderVisible (true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 250;
		pitchTable.setLayoutData(data);
		pitchTable.setBounds (clientArea.x, clientArea.y, 250, 250);
		for (int i=0;i<12;i++)
		{
			TableColumn tc = new TableColumn(pitchTable, SWT.NONE);
			tc.setText("" + (i+1));
		}
		populateTable();
		setRowsToUse();
		pack();
	}
	
	
	private void extractPitchesForRows()
	{
		ArrayList<Integer> pds = new ArrayList<Integer>();
		for (int i=0;i<12;i++)
		{ 
			int thisPD = 12 - (theFundamentalRow.get(i).getSelection());
			if (thisPD >= 12)
				thisPD = 0;
			pds.add(thisPD);
		}
		primes = new int[12][12];
		for (int i=0;i<12;i++)
		{
			for (int j=0;j<12;j++)
			{
				String text = pitchTable.getItem(i).getText(j);
				Integer t = Integer.parseInt(text);
				primes[pds.get(i)][j] = t;
			}
		}
		
		inversions = new int[12][12];
		for (int i=0;i<12;i++)
		{
			for (int j=0;j<12;j++)
			{
				String text = pitchTable.getItem(j).getText(i);
				Integer t = Integer.parseInt(text);
				inversions[theFundamentalRow.get(i).getSelection()][j] = t;
			}
		}
		
		retrograde = new int[12][12];
		for (int i=0;i<12;i++)
		{
			for (int j=0;j<12;j++)
			{
				retrograde[i][j] = primes[i][11-j];
			}
		}
		
		
		retrogradeInversions = new int[12][12];
		for (int i=0;i<12;i++)
		{
			for (int j=0;j<12;j++)
			{
				retrogradeInversions[i][j] = inversions[i][11-j];
			}
		}
		
	}
	
	private void printMatrix(String prefix, int[][] matrix)
	{
		for (int i=0;i<12;i++)
		{
			for (int j=0;j<12;j++)
			{
				System.out.print(noteNames[matrix[i][j]] + ",");
			}
			System.out.println("]");
		}
	}
	
	private void populateTable()
	{
		pitchTable.removeAll();
		
		ArrayList<Integer> pds = new ArrayList<Integer>();
		
		for (int i=0;i<12;i++)
		{
			pds.add(12 - (theFundamentalRow.get(i).getSelection()));
		}
		
		//Add elements to the table
				for (int i=0;i<12;i++)
				{
					TableItem newItem = new TableItem(pitchTable, SWT.NONE);
					int thisShift = pds.get(i);
					for (int j=0;j<12;j++)
					{
						int homeRowNumber = theFundamentalRow.get(j).getSelection();
						int resultantNumber = homeRowNumber + thisShift;
						resultantNumber %= 12;
						newItem.setText(j, resultantNumber + "");
					}
				}
				
				for (int i=0;i<12;i++)
					pitchTable.getColumn(i).pack();
				pack(true);
				pitchTable.pack(true);
		extractPitchesForRows();
	}
	
	private void setRowsToUse()
	{
		//List primeList, inversionList, retrogradeList, retroInvList;
		rowsToUse = new ArrayList<int[]>();
		
		int[] selectedPrimes = primeList.getSelectionIndices();
		for (int i=0;i<selectedPrimes.length;i++)
		{
			rowsToUse.add(primes[selectedPrimes[i]]);
		}
		
		int[] selectedInvs = inversionList.getSelectionIndices();
		for (int i=0;i<selectedInvs.length;i++)
		{
			rowsToUse.add(inversions[selectedInvs[i]]);
		}
		
		int[] selectedRetros = retrogradeList.getSelectionIndices();
		for (int i=0;i<selectedRetros.length;i++)
		{
			rowsToUse.add(retrograde[selectedRetros[i]]);
		}
		
		int[] selectedRetroInvs = retroInvList.getSelectionIndices();
		for (int i=0;i<selectedRetroInvs.length;i++)
		{
			rowsToUse.add(retrogradeInversions[selectedRetroInvs[i]]);
		}
		
		for (int i=0;i<rowsToUse.size();i++)
		{
//			printAnArray(rowsToUse.get(i));
		}
	}
	
	private void printAnArray(int[] theArray)
	{
		for (int i=0;i<theArray.length;i++)
		{
			System.out.print(theArray[i] + " ");
		}
		System.out.println();
	}
	
	public void addLabel(String text)
	{
		Label addedLabel = new Label(this, SWT.BORDER);
		addedLabel.setText(text);
	}
	
	public int nextPitch()
	{
		if (ofTwelve >= 12)
		{
			ofTwelve = 0;
			rowNumber++;
		}
		
		if (null == currentRow || ofTwelve == 0)
		{
			int rowNumber = rnd.nextInt(rowsToUse.size());
			currentRow = rowsToUse.get(rowNumber);
		}
		
		return currentRow[ofTwelve]+60;
	}
	
	public void restart()
	{
		noteNum = 0;
		ofTwelve = -1;
		rowNumber = 0;
	}
	
	public void next()
	{
		noteNum++;
		ofTwelve++;
	}
	
	public Properties getSettings()
	{
		Properties p = new Properties();
		
		//construct current scale shifts array
		String array = "";
		for (int i=0;i<theFundamentalRow.size();i++)
		{
			array += theFundamentalRow.get(i).getSelection() + "#";
		}
		p.setProperty("twelveTone.fundamentalRow", array);
		
		System.out.println(p);
		return p;
	}
	
	public void setSettings(Properties p)
	{
		String myScaleShiftValues = p.getProperty("twelveTone.fundamentalRow");
		String[] array = myScaleShiftValues.split("#");
		
		int[] intArray = new int[array.length];
		
		for (int i=0;i<array.length;i++)
		{
			intArray[i] = Integer.parseInt(array[i]);
			theFundamentalRow.get(i).setSelection(intArray[i]);
		}
		
		populateTable();
	}
}
