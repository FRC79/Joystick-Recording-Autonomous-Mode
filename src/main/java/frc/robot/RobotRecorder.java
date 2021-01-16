
// inport constants from constants.java
import frc.robot.Constants.RobotRecorderConstants;

// use an arraylist of hashMaps for storing the data about the robot
import java.util.Arraylist;
import java.util.HashMap;

public class RobotRecorder {

    // inport constants from constants.java
    static final double  UPDATE_FREQ    = RobotRecorderConstants.UPDATE_FREQUENCY;
    static final double  AUTO_LENGTH    = RobotRecorderConstants.AUTONOMOUS_DURATION; 
    static final String  FILE_EXT       = RobotRecorderConstants.SAVE_FILE_EXTENTION;
    static final String  FILE_PATH      = RobotRecorderConstants.SAVE_FILE_PATH;
    static final boolean PRINT_DEBUG    = RobotRecorderConstants.PRINT_DEBUG_INFO;
    
    private double startTime; // time recording started (to stop recording once it's over the auton period)
    
    // use an arraylist of hashMaps for storing the data about the robot
    private ArrayList<HashMap<String, double>> recordArray;

    private HashMap<String, double> curState;

    private int curUpdateIndex; // current ID for the robotState to be recorded/played 

    // the modes of operation for the robotRecorder
    enum Mode{
        PLAY,
        RECORD,
        NORMAL
    }
    private Mode curMode = Mode.NORMAL;
    
    public StartPlayback(){
        curMode = Mode.PLAY;
        curUpdateIndex = 0;
        // grab stuff from a file or something
    }
    
    public stopPlaying(){
        curMode = Mode.NORMAL;
    }

    public StartRecording(){
        recordArray = new ArrayList<HashMap<String, double>>();
        startTime = System.currentTimeMillis();
        curMode = Mode.RECORD;
    }
    
    public stopRecording(){
        curMode = Mode.NORMAL;
        // save recordArray to a file
    }

    /**
     * @param Key a unique string value that is a name for the value stored in the recording
     * @param Value the value sto be stored at this point in the record, must be a double
     * will store a value under a unique key at the current point in the robot record, only works when the robot is in recording mode.
     */
    public SetRobotData(String Key, double Value){
        if(curMode == Mode.RECORD & curUpdateIndex < recordArray.size()){
            curState.put(Key,Value); // at the current state, put this key value pair in the hashMap
        }
    }
    
    /**
     * @param Key the unique string name of a value you wan to retrieve at this point in the record
     * if no such key is found or not in playback mode, returns null
     * only works 
     */
    public double GetRobotData(String Key){
        if(curMode == Mode.PLAY & curUpdateIndex < recordArray.size()){
            return curState.get(Key);
        }
    }

    public update(){
        
        if( (curTime - lastUpdate) >= updateFrequency){ // after the given time frequency
            if(curMode == Mode.PLAY){ // when playing back info

                if(curUpdateIndex > recordArray.size()){ // stop when out of instructions to follow

                    stopPlaying();
                    return;
                }
                curUpdateIndex++;   // update index for save array on next go around
                curState = recordArray.get(curUpdateIndex); // update curState
            }else if(curMode == Mode.RECORD){ // when recording info

                if( curtime-startTime > AUTO_LENGTH*1000d){ // stop recording when auton recording ends

                    stopRecording();
                    return;
                }
                ArrayList.Add(curState); // save state to array
                curState.clear() // clear state
            }
            lastUpdate = curTime; // set lastUpdate to reset the timer
        }
    }
}
