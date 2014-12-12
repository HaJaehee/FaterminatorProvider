package smartPhone.activityIdentifier;
/**
 * Created by HJH 
 */
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CollectedSensingData {
	public static ConcurrentLinkedQueue<double[]> acc_raw_data =  new ConcurrentLinkedQueue<double[]>();
	public static ConcurrentLinkedQueue<double[]> gyro_raw_data = new ConcurrentLinkedQueue<double[]>();
	public static int heart_rate_data = 0;
	public static double cumulative_burnt_calorie = 0;
	public static long exercise_start_time = 0;
	public static long curr_time = 0;
	
	
	public static long exr_running_time = 0;
	public static long exr_walking_time = 0;
	public static long exr_skipping_rope_time = 0;
	public static long exr_jumping_jack_time = 0;
	public static long exr_stay_time = 0;
}
