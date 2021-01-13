
// inport constants from constants.java
import frc.robot.Constants.RobotRecorderConstants;

public class RobotRecorder {

    // inport constants from constants.java
    static final double  SAVE_FREQ      = RobotRecorderConstants.RECORDING_FREQUENCY;
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
        
        /**   if( (curTime - lastUpdate) >= updateFrequency){
            if(curMode == Mode.PLAY){

                if(curUpdateIndex >= recordArray.size()){

                    stopPlaying();
                    return;
                }else{

                    curState = recordArray.get(curUpdateIndex);
                }
            }else if(curMode == Mode.RECORD){

                if(curUpdateIndex >= recordArray.size()){ 

                    stopRecording();
                    return;
                }else{

                    saveCurInfo();
                }
            }
            lastUpdate = curTime;
        }*/
    }
}
