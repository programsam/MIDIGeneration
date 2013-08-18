package org.zapto.bensmith.midi.custom;

public class RootPathGetter {
	
	private static boolean deployed = false;
	
	
	public static boolean isDeployed() {
		return deployed;
	}


	public static void setDeployed(boolean deployed) {
		RootPathGetter.deployed = deployed;
	}


	public String getRootPath()
	{
		String path = "";
		if (deployed)
		{
			String f = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
			path = f;
		}
		else
		{
			path = "";
		}
		
		return path;
	}
}
