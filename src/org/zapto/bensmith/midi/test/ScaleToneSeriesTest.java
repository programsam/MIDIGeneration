package org.zapto.bensmith.midi.test;

import static org.junit.Assert.*;

import javax.xml.ws.ServiceMode;

import org.junit.Test;
import org.zapto.bensmith.midi.pitch.ScaleToneSeries;

public class ScaleToneSeriesTest {

	private ScaleToneSeries myScaleToneSeries = null;
	
	@Test
	public void testCMajorScale() {
		myScaleToneSeries = new ScaleToneSeries();
		System.out.println(myScaleToneSeries.getToneSeries());
		assertEquals(60, myScaleToneSeries.thisTone());
		assertEquals(62, myScaleToneSeries.forward());
		assertEquals(60, myScaleToneSeries.back());
		assertEquals(64, myScaleToneSeries.forward(2));
		assertEquals(60, myScaleToneSeries.back(2));
		assertEquals(48, myScaleToneSeries.back(7));
		myScaleToneSeries.goHome();
		assertEquals(62, myScaleToneSeries.forward());
		assertEquals(75, myScaleToneSeries.size());
	}
	//48 49 50 51 52 53 54 55 56 57 58 59 60 61 62 63 64 65 66 67 68 69 70 71 72
	//C  C# D  Eb E  F  F# G  Ab A  Bb B  C  C# D  Eb E  F  F# G  Ab A  Bb B  C
	
	@Test
	public void testDMajorScale() {
		myScaleToneSeries = new ScaleToneSeries(62); //set root note to D
		assertEquals(62, myScaleToneSeries.thisTone()); //should return D
		assertEquals(64, myScaleToneSeries.forward()); //should be E
		assertEquals(62, myScaleToneSeries.back()); //should be D again
		assertEquals(66, myScaleToneSeries.forward(2)); //forward 2 should be F#
		assertEquals(62, myScaleToneSeries.back(2)); //F#-2 is D again
		assertEquals(50, myScaleToneSeries.back(7)); //should return the lower D
		myScaleToneSeries.goHome(); //this takes us back to D, 62
		assertEquals(64, myScaleToneSeries.forward()); //one up, 64
		assertEquals(76, myScaleToneSeries.size()); //and there should be 75?
	}
	
	@Test
	public void testTooFarGone() {
		myScaleToneSeries = new ScaleToneSeries(); //set root note to D
		int size = myScaleToneSeries.size();
		myScaleToneSeries.setTonePosition(size-2);
		assertEquals(127, myScaleToneSeries.forward());
		assertEquals(127, myScaleToneSeries.forward());
		assertEquals(127, myScaleToneSeries.forward(6));
		myScaleToneSeries.setTonePosition(0);
		assertEquals(0, myScaleToneSeries.back());
		
	}

}
