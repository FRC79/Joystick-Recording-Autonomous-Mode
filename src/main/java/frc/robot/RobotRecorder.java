
// inport constants from constants.java
import frc.robot.Constants.RobotRecorderConstants;

public class RobotRecorder {

    // inport constants from constants.java
    static final double  UPDATE_FREQ    = RobotRecorderConstants.UPDATE_FREQUENCY;
    static final double  AUTO_LENGTH    = RobotRecorderConstants.AUTONOMOUS_DURATION; 
    static final String  FILE_EXT       = RobotRecorderConstants.SAVE_FILE_EXTENTION;
    static final String  FILE_PATH      = RobotRecorderConstants.SAVE_FILE_PATH;
    static final boolean PRINT_DEBUG    = RobotRecorderConstants.PRINT_DEBUG_INFO;

    private int curUpdateIndex;

    enum Mode{
        PLAY,
        RECORD,
        NORMAL
    }
    private Mode curMode = Mode.NORMAL;

    public void update(){
        
        if( (curTime - lastUpdate) >= updateFrequency){ // after the given time frequency
            if(curMode == Mode.PLAY){ // when playing back info

                if(curUpdateIndex > recordArray.size()){ // stop when out of instructions to follow

                    stopPlaying();
                    return;
                }else{

                    curState = recordArray.get(curUpdateIndex); // get instructions from this point in the save array 
                }
                curUpdateIndex++;   // update index for save array on next go around
            }else if(curMode == Mode.RECORD){ // when recording

                if(curUpdateIndex >= recordArray.size()){ // stop recording when there's nothing more to record

                    stopRecording();
                    return;
                }else{

                    saveCurInfo(); // save robot state at this point in the recording
                }
            }
            lastUpdate = curTime; // set lastUpdate to reset the timer
        }
    }
}
