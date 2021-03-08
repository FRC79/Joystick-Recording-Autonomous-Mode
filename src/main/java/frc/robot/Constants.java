
package frc.robot;

public final class Constants {
  public static final class RobotRecorderConstants{
    
    public static final double  RECORDING_FREQUENCY   = 15;     // miliseconds (maybe change to microseconds)
    public static final double  AUTONOMOUS_DURATION   = 5;      // seconds
    public static final String  SAVE_FILE_EXTENSION   = ".lmao";// extension for files that robotArrays are saved in
    public static final String  SAVE_FILE_PATH        = "/home/lvuser/";// path on the roborio to keep robotArray files( thanks for ruining the cool looking code )
    public static final String  SAVE_FILE_NAME        = "test"; // name of the file to make or read
    public static final boolean PRINT_DEBUG_INFO      = true;   // true to print info about the recoring and playback
    public static final boolean VERBOSE_DEBUG_PRINT   = true;   // true to print a lot of in depth info about recording and playback
    public static final boolean SHOULD_RECORD         = true;   // if false will block the "start recording" method
    public static final boolean INTERPOLATE_VALUES    = true;   // should data retreived between updates be interpolated (a method of constructing new data points between two given data points)
  }
}
