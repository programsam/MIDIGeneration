package org.zapto.bensmith.midi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;
import org.zapto.bensmith.midi.custom.MessageDialog;
import org.zapto.bensmith.midi.custom.NameDialog;
import org.zapto.bensmith.midi.custom.RootPathGetter;
import org.zapto.bensmith.midi.form.FormGenerator;
import org.zapto.bensmith.midi.pitch.*;
import org.zapto.bensmith.midi.rhythm.*;
import org.zapto.bensmith.midi.vel.*;


public class MIDIGen implements MetaEventListener {
	
	MIDIModel myMakeMidi;
	Spinner channelNumber;
	
	PitchGenerator pitchGenerator;
	RhythmGenerator rhythmGenerator;
	VelocityGenerator velocityGenerator;
	FormGenerator formGenerator;
	
	ExpandBar theBar;
	ExpandItem controlsItem, pitchGeneratorItem, formGeneratorItem, rhythmGeneratorItem, velGeneratorItem, loggerItem;
	
	Menu m;
	Menu pitchSubmenu, velSubmenu, rhythmSubmenu, formSubmenu;
	
	Button play;
	
	public static boolean deployed = false;
	
	public static Properties props;
	
	static Text currentStatus = null;
	
	Display display;
	Shell shell;
	
	Text currentMidi = null;
	
	public static void main(String[] args) {
		if (args.length > 0)
		{
			MIDIGen tfe = new MIDIGen(true);
		}
		else
		{
			MIDIGen tfe = new MIDIGen(false);
		}
	}
	
	public MIDIGen()
	{
		this(false);
	}
	
	public MIDIGen(boolean rDepl)
	{
//		deployed = rDepl;
		RootPathGetter.setDeployed(rDepl);
		//init the midi model
		myMakeMidi = new MIDIModel(this);
		
		//init the display and shell
		display = new Display();
		shell = new Shell(display);
		shell.setLayout(new FillLayout());
		
		setupFileMenu();
		setupGeneratorMenu();
		//create the ExpandBar and the initial controls tab
		
		theBar = new ExpandBar(shell, SWT.V_SCROLL | SWT.H_SCROLL);
		
		controlsItem = new ExpandItem(theBar, SWT.BORDER);
		controlsItem.setText("Controls");
		Composite controls = new Composite(theBar, SWT.NONE);
		
		controls.setLayout(new FillLayout());
		
		Composite synthControl = new Composite(controls, SWT.BORDER);
		synthControl.setLayout(new RowLayout());
		
		Composite extraneous = new Composite(controls, SWT.BORDER);
		extraneous.setLayout(new RowLayout());
		
		shell.setText("MIDGen v1.9.1 by Ben Smith, Josh Marcus, and Reese Richardson");
		
		//setup the initial controls tab: FORM
		formGeneratorItem = new ExpandItem(theBar, SWT.BORDER);
		formGeneratorItem.setText("Form Generator");

		//setup the initial controls tab: PITCH
		pitchGeneratorItem = new ExpandItem(theBar, SWT.BORDER);
		pitchGeneratorItem.setText("Pitch Generator");
		
		//setup the initial controls tab: RHYTHM
		rhythmGeneratorItem = new ExpandItem(theBar, SWT.BORDER);
		rhythmGeneratorItem.setText("Rhythm Generator");
		
		//setup the initial controls tab: VELOCITY
		velGeneratorItem = new ExpandItem(theBar, SWT.BORDER);
		velGeneratorItem.setText("Velocity Generator");

		//set up the logging tab.
		loggerItem = new ExpandItem(theBar, SWT.BORDER);
		loggerItem.setText("Status");
		Composite loggerComponent = new Composite(theBar, SWT.NONE);
		loggerItem.setControl(loggerComponent);
		loggerComponent.setLayout(new FillLayout());
				
		currentStatus = new Text(loggerComponent, SWT.MULTI | SWT.V_SCROLL);
		currentStatus.pack(true);
		currentStatus.setEditable(false);
		
		//finish setting up the controls tab
		
		Label l = new Label(extraneous, SWT.NONE);
		l.setText("Channel Number:");
		
		channelNumber = new Spinner(extraneous, SWT.BORDER);
		channelNumber.setMaximum(30);
		channelNumber.setMinimum(0);
		channelNumber.setSelection(0);
		
		
		Button generate = new Button(synthControl, SWT.PUSH);
		generate.setText("Generate");
		
		generate.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (null == formGenerator)
				{
					new MessageDialog(display, "You must select a form generator.");
					return;
				}
				
				if (null == pitchGenerator || null == rhythmGenerator || null == velocityGenerator)
				{
					new MessageDialog(display, "Please make a selection for each generator type.");
					return;
				}
				formGenerator.setPitchGenerator(pitchGenerator);
				formGenerator.setRhythmGenerator(rhythmGenerator);
				formGenerator.setVelocityGenerator(velocityGenerator);
				try
				{
					long tix = myMakeMidi.generateMidi(formGenerator, channelNumber.getSelection());
					String converted = convertMillisToWatch(myMakeMidi.sequence.getMicrosecondLength());
					currentMidi.setText(converted + " (" + tix + " ticks) of MIDI in the sequencer.");
				}
				catch (Exception e2)
				{
					log("***ERROR OCCURED***");
					log(e2.getMessage());
					e2.printStackTrace();
				}
			}
		});
		
		
		//Now set up the play controls
		
		play = new Button(synthControl, SWT.PUSH);
		play.setText("Play");
		
		play.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				try
				{
					if (null == myMakeMidi.sequence)
					{
						new MessageDialog(display, "There is no MIDI to play!");
						return;
					}
					
					for (int i=0;i<display.getShells().length;i++)
					{
						if (display.getShells()[i].getText().equals("Playing..."))
							return;
					}
					
					myMakeMidi.playMidi();
					PlayDialog pd = new PlayDialog(myMakeMidi, display);
				}
				catch (Exception e2)
				{
					log("***AN ERROR OCCURED WHILE PLAYING MIDI***");
					e2.printStackTrace();
				}
			}
		});
		
		
		Button redoSynth = new Button(synthControl, SWT.PUSH);
		redoSynth.setText("Restart Synthesizer");
		
		redoSynth.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
					myMakeMidi.restartSynth();
			}
		});
		
		currentMidi = new Text(controls, SWT.MULTI);
		currentMidi.setText("Loading the synthesizer. This may take a few moments...");
		currentMidi.setEditable(false);
		
		
		//finish setting up the expandabar items.
		controlsItem.setControl(controls);
		controls.pack();
		controlsItem.setHeight(controls.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		
		loggerItem.setHeight(200);
		
		Composite placeHolder = new Composite(theBar, SWT.BORDER);
		placeHolder.setLayout(new FillLayout());
		new Label(placeHolder, SWT.NONE).setText("Please select a form generator");
		formGeneratorItem.setControl(placeHolder);
		formGeneratorItem.setHeight(placeHolder.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		
		placeHolder = new Composite(theBar, SWT.BORDER);
		placeHolder.setLayout(new FillLayout());
		new Label(placeHolder, SWT.NONE).setText("Please select a pitch generator");
		pitchGeneratorItem.setControl(placeHolder);
		pitchGeneratorItem.setHeight(placeHolder.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		
		placeHolder = new Composite(theBar, SWT.BORDER);
		placeHolder.setLayout(new FillLayout());
		new Label(placeHolder, SWT.NONE).setText("Please select a rhythm generator");
		rhythmGeneratorItem.setControl(placeHolder);
		rhythmGeneratorItem.setHeight(placeHolder.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		
		placeHolder = new Composite(theBar, SWT.BORDER);
		placeHolder.setLayout(new FillLayout());
		new Label(placeHolder, SWT.NONE).setText("Please select a velocity generator");
		velGeneratorItem.setControl(placeHolder);
		velGeneratorItem.setHeight(placeHolder.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		
		
		controlsItem.setExpanded(true);
		shell.setMaximized(true);
		shell.open();
		
		log("We are in a deployment situation: " + deployed);
		

		display.asyncExec(new Runnable() {
				public void run() {
					log("Loading MIDI synthesizer...");
					try
					{
						myMakeMidi.setup();
						myMakeMidi.sequencer.addMetaEventListener(myMakeMidi.mmg);
					}
					catch (Exception e)
					{
						log("*** ERROR WHILE LOADING MIDI ***");
					}
					currentMidi.setText("Synthesizer loaded. No MIDI in sequencer.");
				}
			});
		
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		
		display.dispose();
		
		System.exit(1);
	}
	
	private void setupFileMenu()
	{
		//set up the menu
				m = new Menu(shell, SWT.BAR);
				shell.setMenuBar(m);
				MenuItem fileItem = new MenuItem (m, SWT.CASCADE);
				//and create File Menu
				fileItem.setText ("&File");
				Menu submenu = new Menu (shell, SWT.DROP_DOWN);
				fileItem.setMenu (submenu);
				//begin file menu items
				MenuItem ng = new MenuItem (submenu, SWT.PUSH);
				ng.addListener (SWT.Selection, new Listener () {
					public void handleEvent (Event e) {
						NameDialog pd = new NameDialog(display, getRootPath());
					}
				});
				ng.setText ("Open Name Generator...\tCtrl+G");
				ng.setAccelerator (SWT.MOD1 + 'G');
				new MenuItem(submenu, SWT.SEPARATOR);
				
				MenuItem item = new MenuItem (submenu, SWT.PUSH);
				item.addListener (SWT.Selection, new Listener () {
					public void handleEvent (Event e) {
						FileDialog fd = new FileDialog(shell, SWT.SAVE);
						fd.setFilterNames(new String[] {"MIDI Files"});
						fd.setFilterExtensions(new String[] {"*.mid"});
						fd.setFileName("output.mid");
						String filename = fd.open();
						if (null == filename)
						{
							log("You canceled the file save operation.");
							return;
						}
						File f = new File(filename);
						if (null != f)
						{
							myMakeMidi.saveMidiFile(f);
							new MessageDialog(display, "File " + f + " was saved successfully.");
						}
						else
						{
							new MessageDialog(display, "You did not select a valid filename.");
						}
					}
				});
				item.setText ("Save MIDI as...\tCtrl+S");
				item.setAccelerator (SWT.MOD1 + 'S');
				new MenuItem(submenu, SWT.SEPARATOR);
				
				MenuItem config = new MenuItem (submenu, SWT.PUSH);
				config.addListener (SWT.Selection, new Listener () {
					public void handleEvent (Event e) {
						try
						{
								FileDialog fd = new FileDialog(shell, SWT.SAVE);
								fd.setFilterNames(new String[] {"XML Properties Files"});
								fd.setFilterExtensions(new String[] {"*.xml"});
								fd.setFileName("midigen.xml");
								String filename = fd.open();
								if (null == filename)
								{
									log("You canceled the file save operation.");
									return;
								}
								File f = new File(filename);
								if (null != f)
								{
									Properties p = new Properties();
									p.put("pitchGenerator", pitchGenerator.getClass().getSimpleName());
									p.put("rhythmGenerator", rhythmGenerator.getClass().getSimpleName());
									p.put("velocityGenerator", velocityGenerator.getClass().getSimpleName());
									p.put("formGenerator", formGenerator.getClass().getSimpleName());
									
									p.putAll(pitchGenerator.getSettings());
									p.putAll(rhythmGenerator.getSettings());
									p.putAll(velocityGenerator.getSettings());
									p.putAll(formGenerator.getSettings());
									p.storeToXML(new FileOutputStream(f, false), "Saved settings");
									
									new MessageDialog(display, "File " + f + " was saved successfully.");
								}
								else
								{
									new MessageDialog(display, "You did not select a valid filename.");
								}
							}
						catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				});
				config.setText ("Save configuration as...\tCtrl+Z");
				config.setAccelerator (SWT.MOD1 + 'Z');
				
				
				MenuItem openconfig = new MenuItem (submenu, SWT.PUSH);
				openconfig.addListener (SWT.Selection, new Listener () {
					public void handleEvent (Event e) {
						try
						{
								FileDialog fd = new FileDialog(shell, SWT.OPEN);
								fd.setFilterNames(new String[] {"XML Properties Files"});
								fd.setFilterExtensions(new String[] {"*.xml"});
								String filename = fd.open();
								if (null == filename)
								{
									log("You canceled the file save operation.");
									return;
								}
								File f = new File(filename);
								if (null != f)
								{
									Properties p = new Properties();
									p.loadFromXML(new FileInputStream(f));
									
									loadPitch(p.get("pitchGenerator").toString());
									loadRhythm(p.get("rhythmGenerator").toString());
									loadVelocity(p.get("velocityGenerator").toString());
									loadForm(p.get("formGenerator").toString());
									
									
									String velocity = p.get("velocityGenerator").toString();
									for (int i=0;i<velSubmenu.getItems().length;i++)
									{
										if (velSubmenu.getItem(i).getText().equals(velocity))
											velSubmenu.getItem(i).setSelection(true);
									}
									
									String form = p.get("formGenerator").toString();
									for (int i=0;i<formSubmenu.getItems().length;i++)
									{
										if (formSubmenu.getItem(i).getText().equals(form))
											formSubmenu.getItem(i).setSelection(true);
									}
									
									String rhythm = p.get("rhythmGenerator").toString();
									for (int i=0;i<rhythmSubmenu.getItems().length;i++)
									{
										if (rhythmSubmenu.getItem(i).getText().equals(rhythm))
											rhythmSubmenu.getItem(i).setSelection(true);
									}
									
									String pitch = p.get("pitchGenerator").toString();
									for (int i=0;i<pitchSubmenu.getItems().length;i++)
									{
										if (pitchSubmenu.getItem(i).getText().equals(pitch))
											pitchSubmenu.getItem(i).setSelection(true);
									}
									
									pitchGenerator.setSettings(p);
									rhythmGenerator.setSettings(p);
									velocityGenerator.setSettings(p);
									formGenerator.setSettings(p);
								}
								else
								{
									new MessageDialog(display, "You did not select a valid filename.");
								}
							}
						catch (Exception e2) {
							new MessageDialog(display, "An error occured while opening the file...");
							e2.printStackTrace();
						}
					}
				});
				openconfig.setText ("Open Configuration...\tCtrl+O");
				openconfig.setAccelerator (SWT.MOD1 + 'O');
				
				
	}
	
	private void setupGeneratorMenu()
	{
		//set up the menu
				MenuItem fileItem = new MenuItem (m, SWT.CASCADE);
				//and create File Menu
				fileItem.setText ("&Generators");
				Menu submenu = new Menu (shell, SWT.DROP_DOWN);
				fileItem.setMenu (submenu);
				//begin file menu items
				MenuItem fg = new MenuItem (submenu, SWT.CASCADE);
				fg.setText("Form Generator");
				formSubmenu = new Menu(shell, SWT.DROP_DOWN);
				fg.setMenu(formSubmenu);
				
				MenuItem pg = new MenuItem (submenu, SWT.CASCADE);
				pg.setText("Pitch Generator");
				pitchSubmenu = new Menu(shell, SWT.DROP_DOWN);
				pg.setMenu(pitchSubmenu);
				
				MenuItem rg = new MenuItem (submenu, SWT.CASCADE);
				rg.setText("Rhythm Generator");
				rhythmSubmenu = new Menu(shell, SWT.DROP_DOWN);
				rg.setMenu(rhythmSubmenu);
				
				MenuItem vg = new MenuItem (submenu, SWT.CASCADE);
				vg.setText("Velocity Generator");
				velSubmenu = new Menu(shell, SWT.DROP_DOWN);
				vg.setMenu(velSubmenu);
				
				
				try
				{
					String[] names = PackageLister.getGeneratorList("org.zapto.bensmith.midi.form", "FormGenerator");
					for (int i=0;i<names.length;i++)
					{
						MenuItem thisone = new MenuItem(formSubmenu, SWT.RADIO);
						thisone.setText(names[i]);
						thisone.setData(names[i]);
						
						thisone.addSelectionListener(new SelectionAdapter() {
							public void widgetSelected(SelectionEvent e2)
							{
								MenuItem item = (MenuItem)e2.widget;
								loadForm(item.getData().toString());
							}
						});
					}
					
					names = PackageLister.getGeneratorList("org.zapto.bensmith.midi.pitch", "PitchGenerator");
					for (int i=0;i<names.length;i++)
					{
						MenuItem thisone = new MenuItem(pitchSubmenu, SWT.RADIO);
						thisone.setText(names[i]);
						thisone.setData(names[i]);
						
						thisone.addSelectionListener(new SelectionAdapter() {
							public void widgetSelected(SelectionEvent e2)
							{
								MenuItem item = (MenuItem)e2.widget;
								loadPitch(item.getData().toString());
							}
						});
					}
					
					names = PackageLister.getGeneratorList("org.zapto.bensmith.midi.rhythm", "RhythmGenerator");
					for (int i=0;i<names.length;i++)
					{
						MenuItem thisone = new MenuItem(rhythmSubmenu, SWT.RADIO);
						thisone.setText(names[i]);
						thisone.setData(names[i]);
						
						thisone.addSelectionListener(new SelectionAdapter() {
							public void widgetSelected(SelectionEvent e2)
							{
								MenuItem item = (MenuItem)e2.widget;
								loadRhythm(item.getData().toString());
							}
						});
					}
					
					names = PackageLister.getGeneratorList("org.zapto.bensmith.midi.vel", "VelocityGenerator");
					for (int i=0;i<names.length;i++)
					{
						MenuItem thisone = new MenuItem(velSubmenu, SWT.RADIO);
						thisone.setText(names[i]);
						thisone.setData(names[i]);
						
						thisone.addSelectionListener(new SelectionAdapter() {
							public void widgetSelected(SelectionEvent e2)
							{
								MenuItem item = (MenuItem)e2.widget;
								loadVelocity(item.getData().toString());
							}
						});
					}
				} catch(Exception e)
				{
					e.printStackTrace();
				}
				
				
	}
	
	private String convertMillisToWatch(long milliseconds)
	{
		int seconds = ((int)milliseconds )/ 1000000;
		int minutes = seconds / 60;
		int andSeconds = seconds % 60;
		
		return leadingZero(minutes) + ":" + leadingZero(andSeconds);
	}
	
	private String leadingZero(int number)
	{
		if (number < 10)
			return "0" + number;
		else
			return ""+number;
	}
	
	public void meta(MetaMessage message)  {
		log("The midi synthesizer stopped playing.");
	}
	
	public String getRootPath()
	{
		log("Determining root path. We are deployed: " + deployed);
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
		
		log("Path returned: " + path);
		return path;
	}
	
	public static void log(String str)
	{
		
		if (! currentStatus.isDisposed())
		{
			String status = currentStatus.getText();
			DateFormat dateFormat = new SimpleDateFormat("h:mm:ssa");
			String date = dateFormat.format(new Date());
			currentStatus.setText(date + " - " + str + "\n" + status);
		}
		
	}
	
	private void loadPitch(String classname)
	{
		
		if (pitchGeneratorItem.getControl() != null)
			pitchGeneratorItem.getControl().dispose();
		
		if (pitchGenerator != null)
		{
			log("You are replacing the current pitch generator with " + classname);
			
			pitchGenerator.dispose();
		}
		
		if (null == classname || classname.equals(""))
		{
			new MessageDialog(display, "No valid generator was selected!");
			return;
		}
		PitchGenerator incoming = null;
		try
		{
			Class clazz = Class.forName("org.zapto.bensmith.midi.pitch." + classname);
			Constructor ct = clazz.getConstructors()[0];
			Object[] arglist = new Object[1];
			arglist[0] = theBar;
			incoming = (PitchGenerator) ct.newInstance(arglist);
		}
		catch (Exception e2) {
			new MessageDialog(display, "An error occured while loading " + classname);
			log("***ERROR OCCURED LOADING CLASS***");
			log(e2.getMessage());
			e2.printStackTrace();
			return;
		}
		pitchGenerator = incoming;
		pitchGeneratorItem.setControl(pitchGenerator);
		pitchGeneratorItem.setHeight(pitchGenerator.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		pitchGeneratorItem.setText("Pitch Generator: " + classname);
		pitchGenerator.addListener(49129, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				pitchGeneratorItem.setHeight(pitchGenerator.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
			}
		});
	}
	
	private void loadVelocity(String classname)
	{
		if (velGeneratorItem.getControl() != null)
			velGeneratorItem.getControl().dispose();
		
		if (velocityGenerator != null)
		{
			log("You are replacing the current pitch generator with " + classname);
			velocityGenerator.dispose();
		}
		
		if (null == classname || classname.equals(""))
		{
			new MessageDialog(display, "No valid generator was selected!");
			return;
		}
		VelocityGenerator incoming = null;
		try
		{
			Class clazz =Class.forName("org.zapto.bensmith.midi.vel." + classname);
			Constructor ct = clazz.getConstructors()[0];
			Object[] arglist = new Object[1];
			arglist[0] = theBar;
			incoming = (VelocityGenerator) ct.newInstance(arglist);
		}
		catch (Exception e2) {
			new MessageDialog(display, "An error occured while loading " + classname);
			log("***ERROR OCCURED LOADING CLASS***");
			log(e2.getMessage());
			e2.printStackTrace();
			return;
		}
		velocityGenerator = incoming;
		velGeneratorItem.setControl(velocityGenerator);
		velGeneratorItem.setHeight(velocityGenerator.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		velGeneratorItem.setText("Velocity Generator: " + classname);
	}
	
	private void loadRhythm(String classname)
	{
		if (rhythmGeneratorItem.getControl() != null)
			rhythmGeneratorItem.getControl().dispose();
		
		if (rhythmGenerator != null)
		{
			log("You are replacing the current rhythm generator with " + classname);
			rhythmGenerator.dispose();
		}
		
		if (null == classname || classname.equals(""))
		{
			new MessageDialog(display, "No valid generator was selected!");
			return;
		}
		RhythmGenerator incoming = null;
		try
		{
			Class clazz =Class.forName("org.zapto.bensmith.midi.rhythm." + classname);
			Constructor ct = clazz.getConstructors()[0];
			Object[] arglist = new Object[1];
			arglist[0] = theBar;
			incoming = (RhythmGenerator) ct.newInstance(arglist);
		}
		catch (Exception e2) {
			new MessageDialog(display, "An error occured while loading " + classname);
			log("***ERROR OCCURED LOADING CLASS***");
			log(e2.getMessage());
			e2.printStackTrace();
			return;
		}
		if (incoming == null)
			new MessageDialog(display, "Incoming was null...");
		rhythmGenerator = incoming;
		rhythmGeneratorItem.setControl(rhythmGenerator);
		rhythmGeneratorItem.setHeight(rhythmGenerator.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		rhythmGeneratorItem.setText("Rhythm Generator: " + classname);
	}
	
	private void loadForm(String classname)
	{
		if (formGeneratorItem.getControl() != null)
			formGeneratorItem.getControl().dispose();
		
		if (formGenerator != null)
		{
			log("You are replacing the current form generator with " + classname);
			formGenerator.dispose();
		}
		
		if (null == classname || classname.equals(""))
		{
			new MessageDialog(display, "No valid generator was selected!");
			return;
		}
		FormGenerator incoming = null;
		try
		{
			Class clazz =Class.forName("org.zapto.bensmith.midi.form." + classname);
			Constructor ct = clazz.getConstructors()[0];
			Object[] arglist = new Object[1];
			arglist[0] = theBar;
			incoming = (FormGenerator) ct.newInstance(arglist);
		}
		catch (Exception e2) {
			new MessageDialog(display, "An error occured while loading " + classname);
			log("***ERROR OCCURED LOADING CLASS***");
			log(e2.getMessage());
			e2.printStackTrace();
			return;
		}
		if (incoming == null)
			new MessageDialog(display, "Incoming was null...");
		formGenerator = incoming;
		formGeneratorItem.setControl(formGenerator);
		formGeneratorItem.setHeight(formGenerator.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		formGeneratorItem.setText("Form Generator: " + classname);
	}
}
