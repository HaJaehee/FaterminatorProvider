package smartPhone.activityIdentifier;
/**
 * Created by HJH 
 * */
import smartPhone.FileFacade.IdentifiedActivity;
import smartPhone.activityIdentifier.MyFeatureExtractor;
import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MakeClassifier extends Thread {
	private boolean isAlive = false;

    private int m_label;
    private String m_strActivity;
    private double[][] m_accFeatures;
    private double[][] m_gyroFeatures;
    private MyFeatureExtractor mfe;
    private int m_cntFeaturs;
    private int m_averSensedData;

	public MakeClassifier(String a_strActivity) {
		super();

        if(a_strActivity.equals("Running"))
            m_label=IdentifiedActivity.ACTIVITY_RUNNING;
        else if (a_strActivity.equals("Walking"))
            m_label=IdentifiedActivity.ACTIVITY_WALKING;
        else if(a_strActivity.equals("SkippingRope"))
            m_label=IdentifiedActivity.ACTIVITY_SKIPPING_ROPE;
        else if(a_strActivity.equals("JumpingJack"))
            m_label=IdentifiedActivity.ACTIVITY_JUMPING_JACK;

        m_strActivity=a_strActivity;

        m_cntFeaturs =25;
        m_averSensedData = 44;
        
        m_accFeatures = new double[m_cntFeaturs][];
        m_gyroFeatures = new double[m_cntFeaturs][];
        
        for (int i=0 ; i<m_cntFeaturs ; i++)
        { 
	        m_accFeatures[i] = new double[48];
	        m_gyroFeatures[i] = new double[48];
        }

	}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
        Log.d("write classifier" , "start");
		isAlive = true;

        int accSensCount = 0;
        int gyroSensCount = 0;
        
        int accTrainingCount = 0;
        int gyroTrainingCount = 0;

         double[] rawAccSensingDataX = new double[m_averSensedData];
         double[] rawAccSensingDataY = new double[m_averSensedData];
         double[] rawAccSensingDataZ = new double[m_averSensedData];
         double[] rawGyroSensingDataX = new double[m_averSensedData];
         double[] rawGyroSensingDataY = new double[m_averSensedData];
         double[] rawGyroSensingDataZ = new double[m_averSensedData];
        
		while (isAlive) {
			if(accTrainingCount==m_cntFeaturs && gyroTrainingCount==m_cntFeaturs){
                isAlive=false;
                break;
            }
			if (!CollectedSensingData.acc_raw_data.isEmpty() && accTrainingCount<m_cntFeaturs) {

				double[] sensData = CollectedSensingData.acc_raw_data.poll();
                rawAccSensingDataX[accSensCount] = sensData[0];
                rawAccSensingDataY[accSensCount] = sensData[1];
                rawAccSensingDataZ[accSensCount] = sensData[2];

                if (accSensCount==m_averSensedData-1)
                {
                    m_accFeatures[accTrainingCount] = getFeatures(rawAccSensingDataX, rawAccSensingDataY,rawAccSensingDataZ);

                    rawAccSensingDataX = new double[m_averSensedData];
                    rawAccSensingDataY = new double[m_averSensedData];
                    rawAccSensingDataZ = new double[m_averSensedData];


                    Log.d("accTrainingCount",accTrainingCount+"");
                    accTrainingCount++;
                    accSensCount=-1;
                }

                accSensCount++;
			}
			if (!CollectedSensingData.gyro_raw_data.isEmpty() && gyroTrainingCount<m_cntFeaturs) {

                double[] sensData = CollectedSensingData.gyro_raw_data.poll();
                rawGyroSensingDataX[gyroSensCount] = sensData[0];
                rawGyroSensingDataY[gyroSensCount] = sensData[1];
                rawGyroSensingDataZ[gyroSensCount] = sensData[2];

                if (gyroSensCount==m_averSensedData-1)
                {
                    m_gyroFeatures[gyroTrainingCount] = getFeatures(rawGyroSensingDataX, rawGyroSensingDataY,rawGyroSensingDataZ);

                    rawGyroSensingDataX = new double[m_averSensedData];
                    rawGyroSensingDataY = new double[m_averSensedData];
                    rawGyroSensingDataZ = new double[m_averSensedData];

                    Log.d("gyroTrainingCount",gyroTrainingCount+"");
                    gyroSensCount=-1;
                    gyroTrainingCount++;
                }

                gyroSensCount++;
			}			
		}

        writeClassifierFile(m_label,m_strActivity,"AccClassifier",m_accFeatures);
        writeClassifierFile(m_label,m_strActivity,"GyroClassifier",m_gyroFeatures);

        Log.d("write classifier" , "end");
	}

    public double[] getFeatures (double[] rawDataX,double[] rawDataY,double[] rawDataZ){

        int index = 0;
        double[] featuresForAllElements = new double[48];

        mfe = new MyFeatureExtractor(rawDataX);
        double[] features = mfe.getFeatures();
        for (double d : features){
            featuresForAllElements[index++] = d;
        }

        mfe = new MyFeatureExtractor(rawDataY);
        features = mfe.getFeatures();
        for (double d : features){
            featuresForAllElements[index++] = d;
        }

        mfe = new MyFeatureExtractor(rawDataZ);
        features = mfe.getFeatures();
        for (double d : features){
            featuresForAllElements[index++] = d;
        }

        double[] XsubY = new double[m_averSensedData];
        for (int i = 0; i < m_averSensedData ; i++)
        {
            XsubY[i] = rawDataX[i] - rawDataY[i];
        }

        double[] YsubZ = new double[m_averSensedData];
        for (int i = 0; i < m_averSensedData ; i++)
        {
            YsubZ[i] = rawDataY[i] - rawDataZ[i];
        }

        double[] ZsubX = new double[m_averSensedData];
        for (int i = 0; i < m_averSensedData ; i++)
        {
            ZsubX[i] = rawDataZ[i] - rawDataX[i];
        }

        mfe = new MyFeatureExtractor(XsubY);
        features = mfe.getFeatures();
        for (double d : features){
            featuresForAllElements[index++] = d;
        }

        mfe = new MyFeatureExtractor(YsubZ);
        features = mfe.getFeatures();
        for (double d : features){
            featuresForAllElements[index++] = d;
        }

        mfe = new MyFeatureExtractor(ZsubX);
        features = mfe.getFeatures();
        for (double d : features){
            featuresForAllElements[index++] = d;
        }

        return featuresForAllElements;
    }

    private void writeClassifierFile (int a_label, String a_strActivity, String a_strSensor, double[][] a_FeaturesArr){
        File dataFile;
        String path = Environment.getExternalStorageDirectory() + "/Faterminator/"+a_strSensor;
        dataFile = new File(path);
        if (!dataFile.exists())
            dataFile.mkdirs();

        dataFile = new File(path + "/" + a_strActivity + ".csv");

        if(dataFile.exists())
            dataFile.delete();

        try{
            FileOutputStream fos = new FileOutputStream(dataFile);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            for (double[] features : a_FeaturesArr)
            {
                bw.write(a_label+",");
                for (int i = 0 ; i<47 ; i++)
                {
                    bw.write(features[i]+",");
                }
                bw.write(features[47]+"\n");
            }

            bw.close();
            fos.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
