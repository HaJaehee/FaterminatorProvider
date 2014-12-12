package smartPhone.activityIdentifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.util.Log;
import au.com.bytecode.opencsv.CSVReader;
import smartPhone.FileFacade.IdentifiedActivity;
import smartPhone.FileFacade.MyKNN;
import smartPhone.FileFacade.UserInfo;

/**
 * Created by HJH 
 * 테스트 파일로부터 feature 를 extracting 하고, KNN 알고리즘을 이용하여 activity 를 판단한다.
 */
public class ActivityIdentifier {

    private MyFeatureExtractor mfe = new MyFeatureExtractor(new Double[0]);

    private MyKNN m_mK;

    private ArrayList<Double> m_doubleListX;
    private ArrayList<Double> m_doubleListY;
    private ArrayList<Double> m_doubleListZ;

    private int m_label;

//    private String m_strTestDataPath;



    public ActivityIdentifier(){
        m_mK = new MyKNN();
        m_doubleListX = new ArrayList<Double>();
        m_doubleListY = new ArrayList<Double>();
        m_doubleListZ = new ArrayList<Double>();
       // mMyFileReader = new MyFileReader();
    }/**MyClassifyingClass();
    * MyKNN 객체를 초기화하고, ArrayList 들을 초기화한다. MyFileReader 객체도 초기화한다.
    * */

    public void clearListsInKNN(){
        m_mK.clearLists();
    }

    public void setTestDataPath (String a_strTestDataPath)
    {
//        m_strTestDataPath = a_strTestDataPath;
        //mMyFileReader.setFolder(a_strTestDataPath);
    }/** setTestDataPath();
    * MyFileReader 객체의 setFolder 메서드를 실행하여 테스트 데이터가 있는 디렉터리를 설정한다.
    * */

  /*  public ArrayList<String> ClassifyingTestCSV() {


        mMyFileReader.getFilePathList();
        ArrayList<String> filePathList = mMyFileReader.getCSVList();
        ArrayList<String> labels = new ArrayList<String>();

        for (String filePath : filePathList) {
//            Log.d("filepath",filePath);
            File testFile = new File(filePath);
            String[] nextLine;

            try {
                CSVReader reader = new CSVReader(new FileReader(testFile));

                while ((nextLine = reader.readNext()) != null) //read 시작
                {
                  //  Log.d("is double?",nextLine[0]);
                    m_doubleListX.add(Double.parseDouble(nextLine[0]));
                    m_doubleListY.add(Double.parseDouble(nextLine[1]));
                    m_doubleListZ.add(Double.parseDouble(nextLine[2])); //String으로 가져온 값을 Double로 변환하여 List에 저장
                }

                reader.close();

                double[] features = getFeatures();

                int Label = m_mK.getLabel(features);

                switch (Label) {
                    case finalStaticVariables.ACTIVITY_RUNNING:
                        labels.add("RUNNING\n");
                        break;
                    case finalStaticVariables.ACTIVITY_WALKING:
                        labels.add("WALKING\n");
                        break;
                    case finalStaticVariables.ACTIVITY_PUSHUP:
                        labels.add("PUSHUP\n");
                        break;
                    case finalStaticVariables.ACTIVITY_SITUP:
                        labels.add("SITUP\n");
                        break;
                }


            } catch (IOException e) {
                Log.d("cannot open file", "y");
            }

        }

        return labels;
    }*//** ClassifyingTestCSV();
    * MyFileReader 객체로부터 테스트 파일 목록을 불러와서 그 테스트파일들을 각각 feature extracting 한 다음에
    * KNN 을 사용하여 Classifying 하는 클래스이다.
    * 테스트파일마다 Classifying 하여 그것의 Label 을 ArrayList 에 add 하고, 테스트파일들의 classifying 이 완료되면
    * ArrayList 를 리턴한다.
    * */

//    public int classify_Test(double[] a_doubleArrFeatureOfTwoSens)
//    {
//        int Label = m_mK.getLabel(a_doubleArrFeatureOfTwoSens);
//        return Label;
//    }



    public boolean setKOfKNN (int a_K){
        if (m_mK!=null) {
            m_mK.setK(a_K);
            return true;
        }
        else
            return false;
    }/** setKOfKNN();
    * MyKNN 객체의 K값을 세팅한다.
    *
    */

//    public boolean setGyroClassifierFromFile (BufferedReader br){
//        if (m_mK!=null) {
//            m_mK.setGyroClassifierFromFile(br);
//            return true;
//        }
//        else
//            return false;
//    }

    public boolean setAccClassifierFromFile (BufferedReader br){
    	if (m_mK!=null) {
            m_mK.setAccClassifierFromFile(br);
            return true;
        }
        else
            return false;
    }/** setClassifierFromFile();
    * MyKNN 객체의 Classifier 파일의 경로를 세팅한다.
    *
    */
    
    public void setUserInfoFromFile (String a_path){
       try {
    	   CSVReader reader = new CSVReader( new FileReader(new File(a_path)));
    	   String[] nextLine = null;
    	   
    	   reader.readNext();
    	   nextLine = reader.readNext();
	   
    	   UserInfo.name = nextLine[0];
    	   UserInfo.age = Integer.parseInt(nextLine[1]);
    	   UserInfo.sex = nextLine[2];
    	   UserInfo.height = Double.parseDouble(nextLine[3]);
    	   UserInfo.weight = Double.parseDouble(nextLine[4]);
    	   
	       reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void clear_array()
    {
        m_doubleListX.clear();
        m_doubleListY.clear();
        m_doubleListZ.clear();
    }

//    public boolean setAccTrainingClassifierFromFile (ArrayList<String> a_accTrainingClassifier)
//    {
//        if(m_mK!=null)
//        {
//            m_mK.setAccTrainingClassifierFromFile(a_accTrainingClassifier);
//            return true;
//        }
//        else
//            return false;
//    }
//
//    public boolean setGyroTrainingClassifierFromFile (ArrayList<String> a_gyroTrainingClassifier)
//    {
//        if(m_mK!=null)
//        {
//            m_mK.setGyroTrainingClassifierFromFile(a_gyroTrainingClassifier);
//            return true;
//        }
//        else
//            return false;
//    }

    private double[] getFeatures (){

        double features[] = new double[48];
        int index = 0;

        Double[] doubleArrayAccX = (Double[])m_doubleListX.toArray(new Double[0]); //List 형식의 데이터를 Array 형식의 데이터로 변환

        mfe.setDoubleArr(doubleArrayAccX); //MyFeatureExtractor 객체 생성, 데이터 Array 전달

        for (double d : mfe.getFeatures())
        {
            features[index++]=d;
        }

        Double[] doubleArrayAccY = (Double[])m_doubleListY.toArray(new Double[0]);

        mfe.setDoubleArr(doubleArrayAccY);

        for (double d : mfe.getFeatures())
        {
            features[index++]=d;
        }


        Double[] doubleArrayAccZ = (Double[])m_doubleListZ.toArray(new Double[0]);

        mfe.setDoubleArr(doubleArrayAccZ);

        for (double d : mfe.getFeatures())
        {
            features[index++]=d;
        }

        int arrLength = doubleArrayAccX.length;

        Double[] doubleArrXSubY = new Double[arrLength];

        for (int i = 0 ; i< arrLength ; i++)
            doubleArrXSubY[i]=doubleArrayAccX[i]-doubleArrayAccY[i]; //X-Y array


        Double[] doubleArrYSubZ = new Double[arrLength];

        for (int i = 0 ; i<arrLength ; i++)
            doubleArrYSubZ[i]=doubleArrayAccY[i]-doubleArrayAccZ[i]; //Y-Z array

        Double[] doubleArrZSubX = new Double[arrLength];

        for (int i = 0 ; i<arrLength ; i++)
            doubleArrZSubX[i]=doubleArrayAccZ[i]-doubleArrayAccX[i]; //Z-X array

        mfe.setDoubleArr(doubleArrXSubY);
        for (double d : mfe.getFeatures())
        {
            features[index++]=d;
        }


        mfe.setDoubleArr(doubleArrYSubZ);
        for (double d : mfe.getFeatures())
        {
            features[index++]=d;
        }


        mfe.setDoubleArr(doubleArrZSubX);
        for (double d : mfe.getFeatures())
        {
            features[index++]=d;
        }

        return features;
    }/** getFeatures();
    * MyFeatureExtractor 클래스로 테스트 파일로부터 읽어온 값의 feature extraction 을 한다.
    * feature 는 48개이다.
    */


    public ArrayList<Double> getM_doubleListX() {
        return m_doubleListX;
    }

    public void addM_doubleListX(Double a_doubleListX) {
        this.m_doubleListX.add(a_doubleListX);
    }
    
    public void setM_doubleListX(ArrayList<Double> a_doubleListX) {
    	this.m_doubleListX = a_doubleListX;
    }

    public ArrayList<Double> getM_doubleListY() {
        return m_doubleListY;
    }

    public void addM_doubleListY(Double a_doubleListY) {
        this.m_doubleListY.add(a_doubleListY);
    }
    
    public void setM_doubleListY(ArrayList<Double> a_doubleListY) {
    	this.m_doubleListY = a_doubleListY;
    }

    public ArrayList<Double> getM_doubleListZ() {
        return m_doubleListZ;
    }

    public void addM_doubleListZ(Double a_doubleListZ) {
        this.m_doubleListZ.add(a_doubleListZ);
    }
    
    public void setM_doubleListZ(ArrayList<Double> a_doubleListZ) {
    	this.m_doubleListZ = a_doubleListZ;
    }
    
    public void sendSensedData (ArrayList<Double> a_doubleListX, ArrayList<Double> a_doubleListY, ArrayList<Double> a_doubleListZ)
    {
    	this.m_doubleListX = a_doubleListX;
    	this.m_doubleListY = a_doubleListY;
    	this.m_doubleListZ = a_doubleListZ;
    	
    	
    	indentifyingActivity ();
    }
    
    private void indentifyingActivity ()
    {
    	double[] features = getFeatures();
    	m_label = getLabel(features);
    	calcCalorieBurnt(m_label);
    }

    private int getLabel (double[] a_features)
    {
    	int label = m_mK.getLabel(a_features);
    	return label;
    }
    
    public int getLabel ()
    {
    	return m_label;
    }
    
    private void calcCalorieBurnt(int label)
    {
    	double calorieFactor = 0;
    	double exerciseTimeRatio = 0;
    	int minHR = 120;
    	Date date = new Date();
    	long currentTime = date.getTime();
    	long exerciseTime = currentTime - CollectedSensingData.curr_time;
    	Log.d("exerciseTime",Long.toString(exerciseTime));
    	double calorieTimeFactor = exerciseTime/900;
    	
    	if (UserInfo.age <= 20)
    		minHR = 120;
    	else if (UserInfo.age < 40)
    		minHR = 110;
    	else if (UserInfo.age < 70)
    		minHR = 100;
    	else //if UserInfo.age >= 70
    		minHR = 90;
 
    	if (label == IdentifiedActivity.ACTIVITY_RUNNING)
    	{
    		CollectedSensingData.exr_running_time+=exerciseTime;
    		calorieFactor = 2.0;
    	}
    	else if (label == IdentifiedActivity.ACTIVITY_WALKING)
    	{
    		CollectedSensingData.exr_walking_time+=exerciseTime;
    		calorieFactor = 0.9;
    	}
    	else if (label == IdentifiedActivity.ACTIVITY_SKIPPING_ROPE)
    	{
    		CollectedSensingData.exr_skipping_rope_time+=exerciseTime;
    		calorieFactor = 2.5;
    	}
    	else if (label == IdentifiedActivity.ACTIVITY_JUMPING_JACK)
    	{
    		CollectedSensingData.exr_jumping_jack_time+=exerciseTime;
    		calorieFactor = 2.5;
    	}
    	else
    	{
    		CollectedSensingData.exr_stay_time+=exerciseTime;
    		calorieFactor = 0.2;
    	}
    	
    	if (CollectedSensingData.heart_rate_data < minHR)
    		calorieFactor*=0.8;
    	else if (CollectedSensingData.heart_rate_data > minHR+40)
    		calorieFactor*=1.5;
    	else if (CollectedSensingData.heart_rate_data > minHR+20)
    		calorieFactor*=1.2;
    
    	double BMI=  UserInfo.weight / ((UserInfo.height-100)*0.9);
    	
    	if (BMI < 1.05)
    	{
    		BMI = 1.0;
    	}
    	
    	CollectedSensingData.cumulative_burnt_calorie += BMI * calorieFactor * calorieTimeFactor;
    	
    	Log.d("calorie",Double.toString(CollectedSensingData.cumulative_burnt_calorie));
    	
    	CollectedSensingData.curr_time = date.getTime();
    }
}

