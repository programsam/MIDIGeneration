package org.zapto.bensmith.midi;


import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.sound.midi.*;

import org.zapto.bensmith.midi.form.FormGenerator;

public class MIDIModel {

	 private String names[] = { 
	           "Piano", "Chromatic Perc.", "Organ", "Guitar", 
	           "Bass", "Strings", "Ensemble", "Brass", 
	           "Reed", "Pipe", "Synth Lead", "Synth Pad",
	           "Synth Effects", "Ethnic", "Percussive", "Sound Effects" };
	 
	 MIDIGen mmg;
	 Track track;
	 Sequencer sequencer;
	    Sequence sequence;
	    Synthesizer synthesizer;
	    Instrument instruments[];
	    ChannelData channels[];
	    ChannelData cc;    // current channel
	    
	    static final int PROGRAM = 192;
	    static final int NOTEON = 144;
	    static final int NOTEOFF = 128;
	    static final int SUSTAIN = 64;
	    static final int REVERB = 91;
	    static final int ON = 0, OFF = 1;
	    
	    
	    Random rnd = new Random();
	
	    public MIDIModel(MIDIGen rGen)
	    {
	    	mmg = rGen;
	    }
	    
	    
	 public void setup() throws Exception
	 {
		mmg.log("Obtaining system MIDI Synthesizer...");
			try {
	            if (synthesizer == null) {
	                if ((synthesizer = MidiSystem.getSynthesizer()) == null) {
	                    mmg.log("getSynthesizer() failed!");
	                    return;
	                }
	            } 
	            mmg.log("Opening the synth..");
	            synthesizer.open();
	            mmg.log("Getting the sequencer");
	            sequencer = MidiSystem.getSequencer();
	        } catch (Exception ex) { ex.printStackTrace(); return; }
			
	 }
	 
	 public void restartSynth()
	 {
		 try
		 {
			 killMidi();
			 setup();
		 }
		 catch (Exception e)
		 {
			 e.printStackTrace();
		 }
	 }
	 
	 public long generateMidi(FormGenerator p, int channel) throws Exception
	 {
		 mmg.log("Generating the MIDI data");
		 p.restart();
		 mmg.log("Creting a new sequence");
         sequence = new Sequence(Sequence.PPQ, 12);
         int[] channels = new int[1];
         channels[0] = channel;
	        for (int t=0;t<p.getNumChannels();t++)
	        {
		        track = sequence.createTrack();
		        
		        while (! p.isDone()){
		        	p.next();
		        	int pitch = p.getNextPitch();
		        	int velocity = p.getNextVelocity();
		        	long start = p.getNextStartTick();
		        	long length = p.getNextLength();
		        	
		        	playNote(channels[t], pitch, velocity, start, length);
		        }
	        }
	        
	        mmg.log("Done generating MIDI");
	        return sequence.getTickLength();
	 }
	 
	 public void playMidi() throws Exception
	 {
		 
		 	mmg.log(sequence.getTickLength() + " ticks to play...");
		 	mmg.log("Opening the sequencer");
		 	if (! sequencer.isOpen())
		 		sequencer.open();
		   mmg.log("Setting the sequence");
		   
		   sequencer.setSequence(sequence);
		   mmg.log("*** PLAYING ***");
		   sequencer.setTickPosition(0);
		   sequencer.start();
	 }
	 
	 public void saveMidiFile(File file) {
         try {
             int[] fileTypes = MidiSystem.getMidiFileTypes(sequence);
             if (fileTypes.length == 0) {
            	 mmg.log("Can't save sequence");
             } else {
                 if (MidiSystem.write(sequence, fileTypes[0], file) == -1) {
                     throw new IOException("Problems writing to file");
                 } 
             }
         } catch (SecurityException ex) { 
         } catch (Exception ex) { 
             ex.printStackTrace(); 
         }
     }
	 
	 public void stopMidi() throws Exception
	 {
		 sequencer.stop();
	 }
	 
	 public void addEvent(int type, int num, int velocity, long tick) throws Exception
	 {
		 ShortMessage sm = new ShortMessage();
	     sm.setMessage(type, num, velocity); 
	     MidiEvent e = new MidiEvent(sm, tick);
         track.add(e);
	 }
	 
	 public void playNote(int channel, int num, int velocity, long startTick, long length) throws Exception
	 {
		 addEvent(NOTEON+channel, num, velocity, startTick);
		 addEvent(NOTEOFF+channel, num, velocity, startTick+length);
	 }
	 
	 public void playNote(int num, int velocity, long startTick, long length) throws Exception
	 {
		 addEvent(NOTEON, num, velocity, startTick);
		 addEvent(NOTEOFF, num, velocity, startTick+length);
	 }
	 
	
	 public void killMidi() 
	 {
		 mmg.log("Stopping the sequencer...");
		 if (sequencer.isRunning())
			 sequencer.stop();
		 mmg.log("Closing the sequencer and synthesizer");
	     if (sequencer.isOpen())
	        sequencer.close();
	     if (synthesizer.isOpen())
	    	 synthesizer.close();
	 }
}
