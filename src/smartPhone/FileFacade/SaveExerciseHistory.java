package smartPhone.FileFacade;
/**
 * Created by HJH 
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;

import smartPhone.activityIdentifier.CollectedSensingData;
import android.os.Environment;

public class SaveExerciseHistory {
	public SaveExerciseHistory()
	{
		
	}
	
	public void save (String history)
	{
		File dataFile;
		String path = Environment.getExternalStorageDirectory() + "/Faterminator/";
        dataFile = new File(path);
        if (!dataFile.exists())
            dataFile.mkdirs();

        dataFile = new File(path + "/exercise_history.csv");

	    if(!dataFile.exists())
	    {
	    	try{
                FileWriter fw = new FileWriter(dataFile);
            	fw.write("exercise_end_time,burnt_calorie,exercise_time,running_time,walking_time,skipping_rope_time,jumping_jack_time,stay_time\n");
            	fw.write(history+"\n");
            	fw.close();
     
	        }catch(IOException e)
	        {
	            e.printStackTrace();
	        } 	
	    }
	    else
	    {
	    	try{
                FileWriter fw = new FileWriter(dataFile,true);//true, allow to appends.
                fw.append(history+"\n");
            	fw.close();
     
	        }catch(IOException e)
	        {
	            e.printStackTrace();
	        } 	
	    }
	}
}
