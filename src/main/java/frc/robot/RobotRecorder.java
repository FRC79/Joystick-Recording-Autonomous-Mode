/**

This class will have an array of hashmaps that store given info about the robot and play it back on request

an example of how to use this can be found (link here) 
this is also used in the CK_23.5 repo found (link here)

TODO:
make make a robotState class that is serializeable and make curstate type robotState
    re-write saving process to accomodate this
    same with reading
    mabye even change methods for assigning and retrieving keys/values
on save use file and object output to save files
on load use file and object input to load files
use the sendable chooser to let drivers choose what file to read from?
    if not just make the filename an option in constants

*/
// import constants from constants.java
import frc.robot.Constants.RobotRecorderConstants;

// use an arraylist of hashMaps for storing the data about the robot
import java.util.ArrayList;
import java.util.HashMap;

public class RobotRecorder {

    // import constants from constants.java
    static final double  UPDATE_FREQ    = RobotRecorderConstants.UPDATE_FREQUENCY;
    static final double  AUTO_LENGTH    = RobotRecorderConstants.AUTONOMOUS_DURATION; 
    static final String  FILE_EXT       = RobotRecorderConstants.SAVE_FILE_EXTENTION;
    static final String  FILE_PATH      = RobotRecorderConstants.SAVE_FILE_PATH;
    static final boolean PRINT_DEBUG    = RobotRecorderConstants.PRINT_DEBUG_INFO;
    static final boolean VERBOSE_DEBUG  = RobotRecorderConstants.VERBOSE_DEBUG_PRINT;
    
    private double startTime; // time recording started (to stop recording once the auton period is over )
    
    // use an arraylist of robot states for storing and reading data about the robot
    private ArrayList<RobotState> recordArray;

    // robotState that hold info about the robot in one moment
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
    * Describes the robot's state in a single moment
    * arraylist of robotStates will be used to make a recording of the robot
    */
    private static class RobotState implements java.io.Serializable {
        /* every serializable class needs a serial version id as a long, idk what to make it though */
        private static final long serialVersionUID = -1L; 
        
        /* the keys and vlues that actually describe the robot */
        public HashMap<String, Double> valueMap = new HashMap<String, Double>();
        
        /* need to overwrite toString, used in the serialization process */
        @Override
        public String toString() {
            /**
            TODO: mabye loop through valueMap and print keys with values or something
            */
        }
    }
    
    public void startPlayback(){
        curMode = Mode.PLAY;
        curUpdateIndex = 0;
        // grab recordArray from a file or something
    }
    
    public void stopPlaying(){
        curMode = Mode.NORMAL;
    }

    public void startRecording(){
        recordArray = new ArrayList<RobotState>();
        startTime = System.currentTimeMillis()*1000;
        curMode = Mode.RECORD;
    }
    
    public void stopRecording(){
        curMode = Mode.NORMAL;
        // save recordArray to a file
    }

    /**
     * @param Key a unique string value that is a name for the value stored in the recording
     * @param Value the value sto be stored at this point in the record, must be a double
     * will store a value under a unique key at the current point in the robot record, only works when the robot is in recording mode.
     */
    public void SetRobotData(String Key, double Value){
        if(curMode == Mode.RECORD & curUpdateIndex < recordArray.size()){
            curState.put(Key,Value); // at the current state, put this key value pair in the hashMap
        }
    }
    
    /**
     * @param Key the unique string name of a value to retrieve at this point in the record
     * if no such key is found or not in playback mode, returns null
     * only works 
     */
    public double GetRobotData(String Key){
        if(curMode == Mode.PLAY & curUpdateIndex < recordArray.size()){
            return curState.get(Key);
        }
        return null;
    }

    private void update(){
        
        if( (curTime - lastUpdate) >= UPDATE_FREQ){ // after the given time frequency
            if(curMode == Mode.PLAY){ // when playing back info

                if(curUpdateIndex > recordArray.size()){ // stop when out of instructions to follow

                    if(PRINT_DEBUG){ System.out.println("playback over, out of instructions"); }
                    if(PRINT_DEBUG && VERBOSE_DEBUG){ // in depth info about the end of playback
                        System.out.println("Recording End info:"+recordArray.size()+"instructions"); 
                    }
                    stopPlaying();
                    return;
                }
                curUpdateIndex++;   // update index for save array on next go around
                curState = recordArray.get(curUpdateIndex); // update curState
            }else if(curMode == Mode.RECORD){ // when recording info

                if( System.currentTimeMillis()*1000-startTime > AUTO_LENGTH){ // stop recording when auton recording ends

                    if(PRINT_DEBUG){ System.out.println("Recording over, autonomous timer expired"); }
                    if(PRINT_DEBUG && VERBOSE_DEBUG){  // in depth info about the end of recording
                        System.out.println("Recording End info:"+recordArray.size()+"instructions"); 
                    }
                    stopRecording();
                    return;
                }
                recordArray.add(curState); // save current state to array
                curState.clear(); // clear state for next go around
            }
            lastUpdate = curTime; // set lastUpdate to reset the timer 
        }
    }
}
