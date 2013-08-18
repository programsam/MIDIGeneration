package org.zapto.bensmith.midi;

import javax.sound.midi.MidiChannel;

public class ChannelData {

	MidiChannel channel;
    boolean solo, mono, mute, sustain;
    int velocity, pressure, bend, reverb;
    int row, col, num;
 
     public ChannelData(MidiChannel channel, int num) {
            this.channel = channel;
            this.num = num;
            velocity = pressure = 64;
            bend = reverb = 0;
        }
}
