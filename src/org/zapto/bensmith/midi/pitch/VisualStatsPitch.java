package org.zapto.bensmith.midi.pitch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Properties;
import java.util.Random;

import jsc.distributions.ChiSquared;
import jsc.distributions.Exponential;
import jsc.distributions.Normal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.*;
import org.zapto.bensmith.midi.MIDIGen;
public class VisualStatsPitch extends PitchGenerator {
	
	Random rnd = new Random();
	int noteNum = 0;
	final int widths = 7;
	final Canvas canvas = new Canvas(this, SWT.BORDER);
	private boolean mouseDown = false;
	ArrayList<Double> bounds = new ArrayList<Double>();
	ArrayList<Integer> pitches = new ArrayList<Integer>();
	Random rng = new Random();
	
	private ArrayList<Point> pointList = new ArrayList<Point>();
	
	public VisualStatsPitch(Composite parent)
	{
		super(parent, SWT.NONE);
		Button b = new Button(this, SWT.PUSH);
		b.setText("Clear");
		b.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				pointList.clear();
				redraw();
			}
		});
		b.setBounds(5,5,150,25);
		
		Button normal = new Button(this, SWT.PUSH);
		normal.setText("Normal");
		normal.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				pointList.clear();
				double mu = 444.0;
				double sd = 150;
				
				Normal n = new Normal(mu, sd);
				
				for (int i=0;i<widths*127;i+=widths)
				{
					MIDIGen.log(i + ": ");
					double iL = (double) i;
					double iU = iL + widths;
					double px = (n.cdf(iU) - n.cdf(iL));
					MIDIGen.log("P[" + iU + "] = " + px + ", ");
					MIDIGen.log("Height: " + (px*10488.0));
					int height = (int) (px * 10488.0);
					MIDIGen.log(", Round: " + height);
					Point p = new Point((int) iL, 195-height);
					pointList.add(p);
				}
				
				redraw();
			}
		});
		normal.setBounds(155,5,150,25);
		
		
		canvas.setSize(widths*127, 200);
		canvas.setBounds(5, 30, widths*127, 200);
		canvas.addMouseMoveListener(new MouseMoveListener() {
			
			@Override
			public void mouseMove(MouseEvent e) {
				if (mouseDown)
				{
					GC gc = new GC(canvas);
					int noteNum = e.x / widths;
					int x = noteNum * widths;
					
					gc.drawRectangle(x, e.y, widths - 1, 195-e.y);
					Point p = new Point(x, e.y);
					
					for (int i=0;i<pointList.size();i++)
					{
						if (pointList.get(i).x == x)
							pointList.remove(i);
					}
					
					pointList.add(p);
					canvas.redraw();
				}
			}
		});
		canvas.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				mouseDown = false;
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				mouseDown = true;
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
			}
		});
		
		canvas.addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(PaintEvent e) {
				for (int i=0;i<pointList.size();i++)
				{
					e.gc.drawRectangle(pointList.get(i).x, pointList.get(i).y, widths-1, 195-pointList.get(i).y);
				}
							//x1, y1, x2, y2
				e.gc.drawLine(0, 195, widths*127, 195);
				for (int i=0;i<widths*127;i+=widths)
				{
					e.gc.drawLine(i, 195, i, 190);
				}
				
			}
		});
		
		Label x = new Label(this, SWT.NONE);
		x.setText("Pitch: Each tick = 1 Half Step");
		x.setBounds(300, 230, 300, 20);
		
		Label y = new Label(this, SWT.NONE);
		y.setText("Relative Probability, taller rectangles = more likely");
		y.setBounds(widths*127+10, 100, 300, 20);
		
		Label info = new Label(this, SWT.NONE);
		info.setText("Click and drag to draw probability distribution");
		info.setBounds(350, 5, 300, 20);
		
		pack(true);
	}
	
	
	public int nextPitch()
	{
		double value = rng.nextDouble();
		int toRet = pitches.get(0);
		for (int i=1;i<pitches.size();i++)
		{
			if (value > bounds.get(i))
				toRet = pitches.get(i);
		}
		
		return toRet;
	}
	
	
	public void restart()
	{
		noteNum = 0;
		bounds.clear();
		pitches.clear();
		MIDIGen.log("***RESTART CALLED, DIAGNOSTIC OUTPUT***");
		MIDIGen.log("Size of PointList: " + pointList.size());
		if (pointList.size() == 0)
		{
			MIDIGen.log("Nothing in pointlist.  Return a bunch of Cs.");
			bounds.add(0.0);
			bounds.add(1.0);
			pitches.add(60);
			return;
		}
		double totalValue = 0.0;
		MIDIGen.log(pointList.toString());
		
		for (int i=0;i<pointList.size();i++)
		{
			totalValue +=  (195.0 - (double) pointList.get(i).y);
		}
		System.out.println("Summed Ys: " + totalValue);
		bounds.ensureCapacity(200);
		bounds.add(0.0);
		for (int i=0;i<pointList.size();i++)
		{
			int pitch = pointList.get(i).x / widths;
			double px = (195.0 - (double) pointList.get(i).y) / totalValue;
			MIDIGen.log("Pitch: " + pitch + ", P[X]: " + px);
			bounds.add(i+1, bounds.get(i) + px);
			pitches.add(pitch);
		}
		MIDIGen.log(bounds.toString());
		MIDIGen.log(pitches.toString());
		
	}
	
	public void next()
	{
	}
	
	
	public Properties getSettings()
	{
		Properties p = new Properties();
		String output = "";
		for (int i=0;i<pointList.size();i++)
		{
			output +=  pointList.get(i).x + "," + pointList.get(i).y + "#";
		}
		p.setProperty("mouseBasedPitch.points", output);
		return p;
	}
	
	public void setSettings(Properties p)
	{
		pointList.clear();
		String points = p.getProperty("mouseBasedPitch.points");
		String[] pointsArray = points.split("#");
		for (int i=0;i<pointsArray.length;i++)
		{
			String[] xAndY = pointsArray[i].split(",");
			Point pt = new Point(Integer.parseInt(xAndY[0]), Integer.parseInt(xAndY[1]));
			pointList.add(pt);
		}
	}
}
