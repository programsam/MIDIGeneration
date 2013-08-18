package org.zapto.bensmith.midi.rhythm;

import java.util.ArrayList;

public class RhythmList {

	private ArrayList<Long> startTicks, lengthTicks;
	private int arrayPosition = 0;
	private long offsetDelta = 0L;
	
	public RhythmList()
	{
		startTicks = new ArrayList<Long>();
		lengthTicks = new ArrayList<Long>();
	}
	
	public boolean isRest()
	{
		return (startTicks.size() == 0) && (lengthTicks.size() == 0);
	}
	
	public void setStarts(long[] array)
	{
		for (long thisOne : array)
		{
			startTicks.add(thisOne);
		}
	}
	
	public void setLengths(long[] array)
	{
		for (long thisOne : array)
		{
			lengthTicks.add(thisOne);
		}
	}
	
	public long getOffsetDelta() {
		return offsetDelta;
	}

	public void setOffsetDelta(long offsetDelta) {
		this.offsetDelta = offsetDelta;
	}
	
	public void restart()
	{
		arrayPosition = 0;
	}
	
	public long nextStart()
	{
		return startTicks.get(arrayPosition);
	}
	
	public long nextLength()
	{
		return lengthTicks.get(arrayPosition);
	}
	
	public boolean next()
	{
		arrayPosition++;
		if (arrayPosition >= startTicks.size())
			return false;
		else
			return true;
	}
}
