package tage.audio;

/**
 * The type of audio resource of the sound file.  SAMPLE is usually a small sound file that 
 * is less than a minute long.  STREAM is usually a large sound file that is 
 * over a minute long.  How the audio resource is treated is dependent on the whether it is a 
 * sample or a stream. 
 * 
 * @author Kenneth Barnett
 * 
 * Modified from the SAGE Audio package for the RAGE game engine by Juan E. Ruiz.
 */

public enum AudioResourceType { AUDIO_SAMPLE, AUDIO_STREAM }