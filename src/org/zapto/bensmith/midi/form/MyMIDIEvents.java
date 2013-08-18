package org.zapto.bensmith.midi.form;

public class MyMIDIEvents {
	private int pitch;
	private int velocity;
	private long start;
	private long length;
	
	public int getPitch() {
		return pitch;
	}
	public void setPitch(int pitch) {
		this.pitch = pitch;
	}
	public int getVelocity() {
		return velocity;
	}
	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}
	public long getStart() {
		return start;
	}
	public void setStart(long start) {
		this.start = start;
	}
	public long getLength() {
		return length;
	}
	public void setLength(long length) {
		this.length = length;
	}
	
	public String toString()
	{
		return "MIDIEvent {Start: " + start + ", Length: " + length + ", Pitch: " + pitch + ", Velocity: " + velocity + "}";
	}
	
}
