package org.zapto.bensmith.midi.vel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Properties;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.*;
public class MouseBasedVelocity extends VelocityGenerator {
	
	Random rnd = new Random();
	int noteNum = 0;
	final Canvas canvas = new Canvas(this, SWT.BORDER);
	
	private ArrayList<Point> pointList = new ArrayList<Point>();
	public MouseBasedVelocity(Composite parent)
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
//		b.setSize(100,20);
		canvas.setSize(635, 127);
		canvas.setBounds(5, 30, 635, 127);
		canvas.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				GC gc = new GC(canvas);
				gc.drawOval(e.x, e.y, 10, 10);
				Point p = new Point(e.x, e.y);
				pointList.add(p);
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
					e.gc.drawOval(pointList.get(i).x, pointList.get(i).y, 10, 10);
				}
				
			}
		});
		
		
		pack();
	}
	
	
	class NotePositionComparator implements Comparator<Point>
	{

		@Override
		public int compare(Point o1, Point o2) {
			if (o1.x < o2.x)
				return -5;
			else if (o1.x > o2.x)
				return 5;
			else
				return 0;
		}
		
	}
	
	public int nextVelocity()
	{
		Point p = pointList.get(noteNum);
		return p.y;
	}
	
	
	public void restart()
	{
		Collections.sort(pointList, new NotePositionComparator());
		noteNum = 0;
	}
	
	public void next()
	{
		noteNum++;
		if (noteNum >= pointList.size())
			noteNum = 0;
	}
	
	
	public Properties getSettings()
	{
		Properties p = new Properties();
		String output = "";
		for (int i=0;i<pointList.size();i++)
		{
			output +=  pointList.get(i).x + "," + pointList.get(i).y + "#";
		}
		p.setProperty("mouseBasedVelocity.points", output);
		return p;
	}
	
	public void setSettings(Properties p)
	{
		pointList.clear();
		String points = p.getProperty("mouseBasedVelocity.points");
		String[] pointsArray = points.split("#");
		for (int i=0;i<pointsArray.length;i++)
		{
			String[] xAndY = pointsArray[i].split(",");
			Point pt = new Point(Integer.parseInt(xAndY[0]), Integer.parseInt(xAndY[1]));
			pointList.add(pt);
		}
	}
}
