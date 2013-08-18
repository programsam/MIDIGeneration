package org.zapto.bensmith.midi.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class PropertiesLoader {
	
	public static void main(String[] args)
	{
		SomeClass c = new SomeClass();
		
		Field[] fieldNames = c.getClass().getFields();
		for (int i=0;i<fieldNames.length;i++)
		{
			System.out.println(fieldNames[i].getName());
			try
			{
				System.out.println(fieldNames[i].get(c));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
