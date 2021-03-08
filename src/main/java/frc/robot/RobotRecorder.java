/*
This class will have an array of hashmaps that store given info about the robot and play it back on request
an example of how to use this can be found (link here) 
this is also used in the CK_23.5 repo found (link here)
TODO:
use the sendable chooser to let drivers choose what file to read from?
    if not just make the filename an option in constants( current solution )
*/
package frc.robot; 

// import constants from constants.java
import frc.robot.Constants.RobotRecorderConstants;

// use an arraylist of hashmaps for storing the data about the robot
import java.util.ArrayList;
import java.util.HashMap;

// for saving and retrieving files 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RobotRecorder {

    // import constants from constants.java
    static final double  UPDATE_FREQ    	= RobotRecorderConstants.UPDATE_FREQUENCY;
    static final double  RECORD_LENGTH  	= RobotRecorderConstants.RECORDING_DURATION; 
    static final String  FILE_EXT       	= RobotRecorderConstants.SAVE_FILE_EXTENSION;
    static final String  FILE_PATH      	= RobotRecorderConstants.SAVE_FILE_PATH;
    static final String  FILE_NAME      	= RobotRecorderConstants.SAVE_FILE_NAME;
    static final boolean PRINT_DEBUG    	= RobotRecorderConstants.PRINT_DEBUG_INFO;
    static final boolean VERBOSE_DEBUG  	= RobotRecorderConstants.VERBOSE_DEBUG_PRINT;
    static final boolean SHOULD_RECORD 		= RobotRecorderConstants.SHOULD_RECORD;
    static final boolean INTERPOLATE_VALUES 	= RobotRecorderConstants.INTERPOLATE_VALUES;
	    
    // time recording started (to stop recording once the auton timer is over ) 
    private double startTime; 
    
    // use an arraylist of HashMaps for storing and reading data about the robot 
    private ArrayList<HashMap<String, Double>> recordArray;

    /* HashMap that holds info about the robot in a single moment.
     gets saved and cleared every update() */
    private HashMap<String, Double> curState = new HashMap<String, Double>();

    // current ID for the robot's State in the arraylist, for playback 
    private int curUpdateIndex; 
    
    // last time update() ran 
    private double lastUpdate;

    // the modes of operation for the robotRecorder 
    enum Mode{
        PLAY,
        RECORD,
        NORMAL
    }
    private Mode curMode = Mode.NORMAL;
	
	
    // methods for saing and retrieving recordArray to/from files
	
    private void saveRecordArray(String fileName){
        File outFile =  new File(FILE_PATH+fileName+FILE_EXT); // make a new file
	try { // attempt to save the array to the file
        boolean fileSuccess = outFile.createNewFile();
		FileOutputStream fs = new FileOutputStream(outFile);
		ObjectOutputStream os = new ObjectOutputStream(fs);
		os.writeObject(recordArray);
		os.close();
        infoPrint("file made: "+fileSuccess, false);
	} catch (Exception e) { // in the case the file cannot be written to
		e.printStackTrace();
	}
    }
    
    private void loadRecordArray(String fileName){
        try {
		FileInputStream fs = new FileInputStream(FILE_PATH+fileName+FILE_EXT); // try to find file by name
		ObjectInputStream os = new ObjectInputStream(fs);
				
		recordArray = (ArrayList<HashMap<String, Double>>) os.readObject(); // cast the data to an array of HashMaps and then assign it to recordArray
				
		os.close();
		fs.close();
	} 
	catch (IOException e) {
		e.printStackTrace();
	} 
	catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
    }
    
    // methods for starting and stopping the recorder's different opporations   
	
    public void startPlayback(){
        curMode = Mode.PLAY;
        curUpdateIndex = 1; // start at 1, not zero, first vale in array in null
	    infoPrint("playback starting", false);
        // grab recordArray from a file
        loadRecordArray(FILE_NAME);
    }
    
    public void stopPlaying(){
        curMode = Mode.NORMAL;
	    infoPrint("playback over", false);
    }

    public void startRecording(){
	// if recording is not enabled stop here
	if(!SHOULD_RECORD){ return; }
        recordArray = new ArrayList<HashMap<String, Double>>();
        startTime = System.currentTimeMillis();
        curMode = Mode.RECORD;
	    infoPrint("recording starting", false);
    }
    
    public void stopRecording(){
        curMode = Mode.NORMAL;
	    infoPrint("recording over", false);
        // save recordArray to a file
        saveRecordArray(FILE_NAME);
    }

    /**
     * @param Key a unique string value that is a name for the value stored in the recording
     * @param Value the value sto be stored at this point in the record, must be a double
     * will store a value under a unique key at the current point in the robot record, only works when the robot is in recording mode.
     */
    public void setRobotData(String Key, double Value){
        if(curMode == Mode.RECORD){
            curState.put(Key,Value); // at the current state, put this key value pair in the hashMap
        }
    }
    
    /**
     * @param Key the unique string name of a value to retrieve at this point in the record
     * if no such key is found or not in playback mode, returns null
     */
    public Double getRobotData(String Key){
        if(curMode == Mode.PLAY){
		if(INTERPOLATE_VALUES){ // if values should be interpolated
			if(recordArray.get(curUpdateIndex+1) == null){ return curState.get(Key); } // if the next value does not exist don't try to interpolate
			
			Double dataAge = System.currentTimeMillis() - lastUpdate; // how old the curent info is
			Double fraction = dataAge/UPDATE_FREQ; // the data's age / how long it will live = % till the data is refreshed
			/* value1 + fraction * (value2 - value1) = mix of current and next value according to fraction */
			Double lerpData = curState.get(Key) + fraction * (recordArray.get(curUpdateIndex+1).get(key) - curState.get(Key));
			return lerpData;
		}
            return curState.get(Key);
        }
        return (Double) null;
    }
	
    /**
    * @param text the text that should be printed
    * @param verbose is this text verbose debug info
    * a method that should be used for printing that considers if something should be printed or not depending on the settings
    */
    private void infoPrint(String text, boolean verbose){   
        if(!PRINT_DEBUG){ return; } // if printing is turned off then stop right here
        if(verbose){
            if(VERBOSE_DEBUG){
                System.out.println(text); // print verbose messages of allowed
            }
        }else{
            System.out.println(text); // print normal messages
        }
        
    }

    public void update(){
        if( (System.currentTimeMillis() - lastUpdate) >= UPDATE_FREQ){ // after the given time frequency
            if(curMode == Mode.PLAY){ // when playing back info
                //infoPrint(String.valueOf(recordArray), false);
                if(curUpdateIndex >= recordArray.size()){ // stop when out of instructions to follow
                    stopPlaying();
                    return;
                }
                curState = recordArray.get(curUpdateIndex); // update curState
                curUpdateIndex++;   // update index for save array on next go around
            }else if(curMode == Mode.RECORD){ // when recording info
                if(System.currentTimeMillis()-startTime > RECORD_LENGTH*1000){ // stop recording when auton recording ends
                    stopRecording();
                    return;
                }
                if(curState != null){
                    recordArray.add(curState); // save current state to record array
                }
                curState = new HashMap<String, Double>();// clear state for next go around
            }
            lastUpdate = System.currentTimeMillis(); // set lastUpdate to reset the timer 
        }
    }
}
