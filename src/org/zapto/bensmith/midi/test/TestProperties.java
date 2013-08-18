package org.zapto.bensmith.midi.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Properties;

public class TestProperties {

	public static void main(String[] args) throws Exception
	{
		Properties p = new Properties();
		ArrayList<String> xlist = new ArrayList<String>();
		
		p.put("myProperty", xlist);
		p.store(new FileOutputStream("testprops.properties", false), "");
		
	}
}
