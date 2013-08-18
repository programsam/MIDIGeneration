package org.zapto.bensmith.midi.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateStampTester {

	public static void main(String[] args)
	{
		DateFormat dateFormat = new SimpleDateFormat("h:mm:ssa");
		System.out.println(dateFormat.format(new Date()));
	}
}
