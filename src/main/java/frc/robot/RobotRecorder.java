/**

This class will have an array of robotStates (like hashmaps but are made to be serializeable) that store given info about the robot and play it back on request

an example of how to use this can be found (link here) 
this is also used in the CK_23.5 repo found (link here)

TODO:
make make a robotState class that is serializeable and make curstate type robotState
    re-write saving process to accomodate this
    same with reading
on save use file and object output to save files
on load use file and object input to load files
use the sendable chooser to let drivers choose what file to read from?
    if not just make the filename an option in constants

*/
package org.usfirst.frc.team79.robot;

// import constants from constants.java
import frc.robot.Constants.RobotRecorderConstants;

// use an arraylist of robotStates for storing the data about the robot
import java.util.ArrayList;
/* robotState uses HashMap */
import java.util.HashMap;

/* for saving and retrieving files */
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RobotRecorder {

    // import constants from constants.java
    static final double  UPDATE_FREQ    = RobotRecorderConstants.UPDATE_FREQUENCY;
    static final double  AUTO_LENGTH    = RobotRecorderConstants.AUTONOMOUS_DURATION; 
    static final String  FILE_EXT       = RobotRecorderConstants.SAVE_FILE_EXTENTION;
    static final String  FILE_PATH      = RobotRecorderConstants.SAVE_FILE_PATH;
    static final String  FILE_NAME      = RobotRecorderConstants.SAVE_FILE_NAME;
    static final boolean PRINT_DEBUG    = RobotRecorderConstants.PRINT_DEBUG_INFO;
    static final boolean VERBOSE_DEBUG  = RobotRecorderConstants.VERBOSE_DEBUG_PRINT;
    
    private double startTime; // time recording started (to stop recording once the auton timer is over )
    
    // use an arraylist of robot states for storing and reading data about the robot
    private ArrayList<RobotState> recordArray;

    // robotState that hold info about the robot in a single moment
    // gets saved and cleared every update()
    private RobotState curState;

    /* current ID for the robot's State in the arraylist, for playback */
    private int curUpdateIndex; 
    
    /* current time for update() (milliseconds) (might make this microseconds) */
    private double curTime = System.currentTimeMillis(); 
    /* last time update() ran */
    private double lastUpdate;

    // the modes of operation for the robotRecorder
    enum Mode{
        PLAY,
        RECORD,
        NORMAL
    }
    private Mode curMode = Mode.NORMAL;
    
    // begining of making curstate a serializable class
    /**
    * Describes the robot's state in a single moment in time
    * arraylist of robotStates will be used to make a recording of the robot
    */
    private static class RobotState implements java.io.Serializable {
        /* every serializable class needs a serial version id as a long, made it just a random number because i think it only has to be unique */
        private static final long serialVersionUID = -4022972738L; 
        
        /* the keys and vlues that actually describe the robot */
        public HashMap<String, Double> valueMap;
        
        public void RobotState(){
            valueMap = new HashMap<String, Double>();
        }

        // functions just for making this class act like a hashMap
        public double get(String key){
            return valueMap.get(key);
        }

        public void put(String key, double value){
            valueMap.put(key,value);
        }

        public void clear(){
            valueMap.clear();
        }
    }
    
    // methods for saing and retrieving recordArray to/from files
    private void saveRecordArray(String fileName){
        File outFile =  new File(fileName+FILE_EXT); // make a new file
	try { // attempt to save the array to the file
		FileOutputStream fs = new FileOutputStream(outFile);
		ObjectOutputStream os = new ObjectOutputStream(fs);
		os.writeObject(recordArray);
		os.close();
	} catch (Exception e) { // in the case the file cannot be written to
		e.printStackTrace();
	}
    }
    
    private ArrayList<RobotState> loadRecordArray(String fileName){
        
    }
    
    // methods for starting and stopping the recorder's different opporations    
    public void startPlayback(){
        curMode = Mode.PLAY;
        curUpdateIndex = 0;
        // grab recordArray from a file
        recordArray = loadRecordArray(FILE_NAME);
    }
    
    public void stopPlaying(){
        curMode = Mode.NORMAL;
    }

    public void startRecording(){
        recordArray = new ArrayList<RobotState>();
        startTime = System.currentTimeMillis()*1000;
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
     * only works 
     */
    public double getRobotData(String Key){
        if(curMode == Mode.PLAY){
            return curState.get(Key);
        }
        return (Double) null;
    }
	
    private void infoPrint(String text, boolean verbose){   
	if(!PRINT_DEBUG){ return; }
	if(verbose){
		if(PRINT_VERBOSE){
			System.out.println(text);
		}
	}else{
		System.out.println(text);
	}
	
    }

    private void update(){
        
        if( (curTime - lastUpdate) >= UPDATE_FREQ){ // after the given time frequency
            if(curMode == Mode.PLAY){ // when playing back info

                if(curUpdateIndex > recordArray.size()){ // stop when out of instructions to follow

                    stopPlaying();
                    return;
                }
                curUpdateIndex++;   // update index for save array on next go around
                curState = recordArray.get(curUpdateIndex); // update curState
            }else if(curMode == Mode.RECORD){ // when recording info

                if( System.currentTimeMillis()*1000-startTime > AUTO_LENGTH){ // stop recording when auton recording ends
			
                    stopRecording();
                    return;
                }
                recordArray.add(curState); // save current state to record array
                curState.clear(); // clear state for next go around
            }
            lastUpdate = curTime; // set lastUpdate to reset the timer 
        }
    }
}
