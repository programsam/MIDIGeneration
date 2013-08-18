package org.zapto.bensmith.midi.test;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Random;

import jsc.distributions.Normal;

public class NormalGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception 
	{
		PrintWriter pw = new PrintWriter(new FileWriter("normaldistro.csv"));
		
		pw.println("data");
		
		double mu = 63.0;
		double sd = 15.0;
		
		Normal n = new Normal(mu, sd);
		
		
		for (int i=0;i<127;i++)
		{
			System.out.print(i + ": ");
			double iL = (double) i;
			double iU = iL + 1.0d;
			double px = (n.cdf(iU) - n.cdf(iL));
			System.out.print("P[" + iU + "] = " + px + ", ");
//			double max = 195.0 * 127.0;
			System.out.print("Height: " + (px*7337.0));
			int height = (int) (px * 7337.0);
			System.out.println(", Round: " + height);
		}
		
	}

}
