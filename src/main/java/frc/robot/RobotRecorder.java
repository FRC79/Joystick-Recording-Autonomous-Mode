
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
    
    public StartRecording(){
        recordArray = new ArrayList<>
    }
    
    public SetRobotData(String Key, double Value){
        if(curMode == Mode.RECORD & curUpdateIndex < recordArray.size()){
            recordArray.get(curUpdateIndex).put(Key,Value); // at the current index, put this key value pair in the hashMap
        }
    }
    
    public double GetRobotData(String Key){
        if(curMode == Mode.PLAY & curUpdateIndex < recordArray.size()){
            return recordArray.get(curUpdateIndex).get(Key);
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
            }else if(curMode == Mode.RECORD){ // when recording info

                if(curUpdateIndex > recordArray.size()){ // stop recording when there's nothing more to record

                    stopRecording();
                    return;
                }
                curUpdateIndex++;   // update index for save array on next go around
            }
            lastUpdate = curTime; // set lastUpdate to reset the timer
        }
    }
}
