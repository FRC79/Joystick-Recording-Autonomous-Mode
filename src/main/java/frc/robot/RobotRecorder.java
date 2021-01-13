
// inport constants from robotMap

public class RobotRecorder {

    // inport constants from robotMap
    static final double  SAVE_FREQ      = // constant
    static final double  AUTO_LENGTH    = // constant 
    static final String  FILE_EXT       = // constant
    static final String  FILE_PATH      = // constant
    static final boolean PRINT_DEBUG    = // constant

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
