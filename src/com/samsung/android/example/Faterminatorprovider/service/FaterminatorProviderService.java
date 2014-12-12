/*     * Copyright (c) 2014 Samsung Electronics Co., Ltd.    * All rights reserved.    *    * Redistribution and use in source and binary forms, with or without    * modification, are permitted provided that the following conditions are    * met:    *    *     * Redistributions of source code must retain the above copyright    *        notice, this list of conditions and the following disclaimer.   *     * Redistributions in binary form must reproduce the above   *       copyright notice, this list of conditions and the following disclaimer   *       in the documentation and/or other materials provided with the   *       distribution.   *     * Neither the name of Samsung Electronics Co., Ltd. nor the names of its   *       contributors may be used to endorse or promote products derived from   *       this software without specific prior written permission.   *   * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS   * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT   * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR   * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT   * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,   * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT   * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,   * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY   * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT   * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE   * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */package com.samsung.android.example.Faterminatorprovider.service;/** * Modified by HJH  */import java.io.BufferedReader;import java.io.ByteArrayInputStream;import java.io.File;import java.io.IOException;import java.io.InputStreamReader;import java.security.cert.CertificateException;import java.security.cert.CertificateFactory;import java.util.ArrayList;import java.util.Date;import java.util.HashMap;import java.util.List;import javax.security.cert.X509Certificate;import android.content.Context;import android.content.Intent;import android.content.pm.PackageInfo;import android.content.pm.PackageManager;import android.content.pm.Signature;import android.content.pm.PackageManager.NameNotFoundException;import android.os.Binder;import android.os.IBinder;import android.util.Log;import android.widget.Toast;import com.samsung.android.sdk.SsdkUnsupportedException;import com.samsung.android.sdk.accessory.SA;import com.samsung.android.sdk.accessory.SAAgent;import com.samsung.android.sdk.accessory.SAAuthenticationToken;import com.samsung.android.sdk.accessory.SAPeerAgent;import com.samsung.android.sdk.accessory.SASocket;import smartPhone.FileFacade.FinalStaticVariables;import smartPhone.FileFacade.IdentifiedActivity;import smartPhone.FileFacade.MyKNN;import smartPhone.FileFacade.SaveExerciseHistory;import smartPhone.activityIdentifier.CollectedSensingData;import smartPhone.activityIdentifier.MakeClassifier;import smartPhone.activityIdentifier.ActivityIdentifier;public class FaterminatorProviderService extends SAAgent{	public static final String TAG = "FaterminatorProviderService";		public Boolean isAuthentication = false;	public Context mContext = null;	public static final int SERVICE_CONNECTION_RESULT_OK = 0;	public static final int Faterminator_CHANNEL_ID = 104;	 HashMap<Integer, FaterminatorProviderConnection> mConnectionsMap = null;	private int authCount = 1;	public FaterminatorProviderConnection mMyConnection;		private final IBinder m_binder = new LocalIBinder();	private ArrayList<Double> x;	private ArrayList<Double> y;		private ArrayList<Double> z;	//	private double m_dAcceptedData = 0;//	private double m_dCountReceiving = 0;	//	private MakeClassifier makeClsfierThr;		private ActivityIdentifier m_actIdentifier;		private SaveExerciseHistory m_saveExrHist ;		private int m_k = 9;		private String m_strClassifier;//classifier 파일 경로 지정	private String m_strUserInfoPath;		public FaterminatorProviderService() {		super(TAG, FaterminatorProviderConnection.class);	}	public  class FaterminatorProviderConnection extends SASocket {		private  int mConnectionId;		public FaterminatorProviderConnection() {			super(FaterminatorProviderConnection.class.getName());		}		@Override		public void onError(int channelId, String errorString, int error) {			Log.d("disconnect","disconnect");		}		@Override		public void onReceive(int channelId, byte[] data) {        				String strToUpdateUI = new String(data);			//				onSend("sensor data received");//				Log.d("sensor data", strToUpdateUI);//				if (strToUpdateUI.contains("ax:"))//				{//					x = new ArrayList<Double>();//					strToUpdateUI = strToUpdateUI.replace("ax:", "");//					String strSensoredData[] = strToUpdateUI.split(",");//					for (String strSensoredDatum : strSensoredData)//					{//						x.add(Double.parseDouble(strSensoredDatum));//					}//				}//				else if (strToUpdateUI.contains("ay:"))//				{//					y = new ArrayList<Double>();//					strToUpdateUI = strToUpdateUI.replace("ay:", "");//					String strSensoredData[] = strToUpdateUI.split(",");//					for (String strSensoredDatum : strSensoredData)//					{//						y.add(Double.parseDouble(strSensoredDatum));//					}//				}//				else if (strToUpdateUI.contains("az:"))//				{//					strToUpdateUI = strToUpdateUI.replace("az:", "");//					String strSensoredData[] = strToUpdateUI.split(",");//					for (int i = 0 ; i<strSensoredData.length ; i++)//					{//						double[] accxyz = new double[3];//						accxyz[0] = x.get(i);//						accxyz[1] = y.get(i);//						accxyz[2] = Double.parseDouble(strSensoredData[i]);//						CollectedSensingData.acc_raw_data.add(accxyz);//					}//				}//				else if (strToUpdateUI.contains("rotx:"))//				{//					x = new ArrayList<Double>();//					strToUpdateUI = strToUpdateUI.replace("rotx:", "");//					String strSensoredData[] = strToUpdateUI.split(",");//					for (String strSensoredDatum : strSensoredData)//					{//						x.add(Double.parseDouble(strSensoredDatum));//					}//				}//				else if (strToUpdateUI.contains("roty:"))//				{//					y = new ArrayList<Double>();//					strToUpdateUI = strToUpdateUI.replace("roty:", "");//					String strSensoredData[] = strToUpdateUI.split(",");//					for (String strSensoredDatum : strSensoredData)//					{//						y.add(Double.parseDouble(strSensoredDatum));//					}//				}//				else if (strToUpdateUI.contains("rotz:"))//				{//					strToUpdateUI = strToUpdateUI.replace("rotz:", "");//					String strSensoredData[] = strToUpdateUI.split(",");//					for (int i = 0 ; i<strSensoredData.length ; i++)//					{//						double[] gyroxyz = new double[3];//						gyroxyz[0] = x.get(i);//						gyroxyz[1] = y.get(i);//						gyroxyz[2] = Double.parseDouble(strSensoredData[i]);//						CollectedSensingData.gyro_raw_data.add(gyroxyz);//					}//				}//				else// if (strToUpdateUI.contains("HR"))//				{//					strToUpdateUI = strToUpdateUI.replace("HRbpm:", "");//					CollectedSensingData.heart_rate_data = Integer.parseInt(strToUpdateUI);//					//					//					Log.d("accxyz length",CollectedSensingData.acc_raw_data.size()+"");//					//					Log.d("gyroxyz length",CollectedSensingData.gyro_raw_data.size()+"");//					//					Log.d("HR",CollectedSensingData.heart_rate_data+"");//					//					 m_dAcceptedData =  m_dAcceptedData + (double)CollectedSensingData.acc_raw_data.size();//					 m_dCountReceiving++;//					//					CollectedSensingData.acc_raw_data.clear();//					CollectedSensingData.gyro_raw_data.clear();//					//					Log.d("average accepted data", (m_dAcceptedData/m_dCountReceiving)+"");//				}			if (strToUpdateUI.contains("ax:"))			{				x = new ArrayList<Double>();				strToUpdateUI = strToUpdateUI.replace("ax:", "");				String strSensoredData[] = strToUpdateUI.split(",");				for (String strSensoredDatum : strSensoredData)				{					x.add(Double.parseDouble(strSensoredDatum));				}			}			else if (strToUpdateUI.contains("ay:"))			{				y = new ArrayList<Double>();				strToUpdateUI = strToUpdateUI.replace("ay:", "");				String strSensoredData[] = strToUpdateUI.split(",");				for (String strSensoredDatum : strSensoredData)				{					y.add(Double.parseDouble(strSensoredDatum));				}			}			else if (strToUpdateUI.contains("az:"))			{				z = new ArrayList<Double>();				strToUpdateUI = strToUpdateUI.replace("az:", "");				String strSensoredData[] = strToUpdateUI.split(",");				for (String strSensoredDatum : strSensoredData)				{					z.add(Double.parseDouble(strSensoredDatum));				}			}			else if (strToUpdateUI.contains("HR"))			{				strToUpdateUI = strToUpdateUI.replace("HRbpm:", "");				CollectedSensingData.heart_rate_data = Integer.parseInt(strToUpdateUI);									m_actIdentifier.sendSensedData(x,y,z);				int label = m_actIdentifier.getLabel();				double calorie = CollectedSensingData.cumulative_burnt_calorie;								String message = "";						    	if (label == IdentifiedActivity.ACTIVITY_RUNNING)		    		message += "Running,";		    	else if (label == IdentifiedActivity.ACTIVITY_WALKING)		    		message += "Walking,";		    	else if (label == IdentifiedActivity.ACTIVITY_SKIPPING_ROPE)		    		message += "Skipping Rope,";		    	else if (label == IdentifiedActivity.ACTIVITY_JUMPING_JACK)		    		message += "Jumping Jack,";		    	else		    		message += "Stay,";		    			    	message += CollectedSensingData.heart_rate_data + ",";		    	message += String.format("%.2f",calorie) + ",";		    	message += (new Date().getTime()-CollectedSensingData.exercise_start_time);		    			    	onSend (message);			}						else			{				onSend(strToUpdateUI+" received");//					 m_dAcceptedData = 0;//					 m_dCountReceiving = 0;							}		}		public  void onSend (String a_message) {						String message = a_message;			//			if (message.contains("start exercise"))//			{//				String[] words = message.split(",");//				makeClsfierThr = new MakeClassifier(words[1]);//				makeClsfierThr.start();//				message = words[0];//			}//			else if (message.equals("stop exercise"))//			{//				makeClsfierThr.interrupt();//			}						if (message.equals("start sensing"))			{		        BufferedReader reader = null;		    	m_actIdentifier = new ActivityIdentifier();				m_actIdentifier.setKOfKNN(m_k);				m_strClassifier = "activity_classifier.csv";		        try {    		            reader = new BufferedReader(		                    new InputStreamReader(getAssets().open(m_strClassifier), "UTF-8"));		        }catch (Exception e)		        {		            e.printStackTrace();		        }		        m_actIdentifier.setAccClassifierFromFile(reader);		        //		        try {	//	            m_strClassifier = "GyroClassifier.csv";	//	            reader = new BufferedReader(	//	                    new InputStreamReader(getAssets().open(m_strClassifier), "UTF-8"));	//	        }catch (Exception e)	//	        {	//	            e.printStackTrace();	//	        }	//	        m_actIdentifier.setGyroClassifierFromFile(reader);		        		        m_strUserInfoPath = "user_info.csv";			    String userInfoPath =FinalStaticVariables.DIR_FOLDER+m_strUserInfoPath;			    		        m_actIdentifier.setUserInfoFromFile(userInfoPath);		        				CollectedSensingData.acc_raw_data.clear();				CollectedSensingData.gyro_raw_data.clear();		        CollectedSensingData.cumulative_burnt_calorie = 0;		        CollectedSensingData.curr_time = new Date().getTime();		        CollectedSensingData.exercise_start_time = new Date().getTime();		        CollectedSensingData.exr_running_time=0;		        CollectedSensingData.exr_walking_time=0;		        CollectedSensingData.exr_skipping_rope_time=0;		        CollectedSensingData.exr_jumping_jack_time=0;		        CollectedSensingData.exr_stay_time=0;			}			else if (message.equals("stop sensing"))			{					m_saveExrHist = new SaveExerciseHistory();				String history = CollectedSensingData.curr_time+","; //운동 끝낸 시각		    	history += CollectedSensingData.cumulative_burnt_calorie + ","; //소모된 칼로리		    	history += (new Date().getTime()-CollectedSensingData.exercise_start_time) + ",";//총 운동 시간		    	history += CollectedSensingData.exr_running_time + ",";		    	history += CollectedSensingData.exr_walking_time + ",";		    	history += CollectedSensingData.exr_skipping_rope_time + ",";		    	history += CollectedSensingData.exr_jumping_jack_time + ",";		    	history += CollectedSensingData.exr_stay_time + ",";		    	m_saveExrHist.save(history);			}						final String sending_message = message;						final FaterminatorProviderConnection uHandler = mConnectionsMap.get(Integer					.parseInt(String.valueOf(mConnectionId)));						if(uHandler == null){				return;			}			new Thread(new Runnable() {				public void run() {									try {												uHandler.send(Faterminator_CHANNEL_ID, sending_message.getBytes());						Log.d("msg sended","true");					} catch (IOException e) {						e.printStackTrace();					}				}			}).start();		}						@Override		protected void onServiceConnectionLost(int errorCode) {			if (mConnectionsMap != null) {				mConnectionsMap.remove(mConnectionId);			}		}	}		public void sendMsg(String msg) {		if (mMyConnection!=null)			mMyConnection.onSend(msg);	}	    @Override    public void onCreate() {        super.onCreate();                SA mAccessory = new SA();        try {        	mAccessory.initialize(this);        } catch (SsdkUnsupportedException e) {        	// Error Handling        } catch (Exception e1) {            e1.printStackTrace();			/*			 * Your application can not use Accessory package of Samsung			 * Mobile SDK. You application should work smoothly without using			 * this SDK, or you may want to notify user and close your app			 * gracefully (release resources, stop Service threads, close UI			 * thread, etc.)			 */            stopSelf();        }    }	        @Override     protected void onServiceConnectionRequested(SAPeerAgent peerAgent) {    	/*    	* The authenticatePeerAgent(peerAgent) API may not be working properly     	* depending on the firmware version of accessory device.         * Recommend to upgrade accessory device firmware if possible.        */     	//    	if(authCount%2 == 1)//    		isAuthentication = false;//    	else//    		isAuthentication = true; //    	authCount++;    	    	isAuthentication = false;    	    	if(isAuthentication) {           // Toast.makeText(getBaseContext(), "Authentication On!", Toast.LENGTH_SHORT).show();            authenticatePeerAgent(peerAgent);        }    	else {            //Toast.makeText(getBaseContext(), "Authentication Off!", Toast.LENGTH_SHORT).show();            acceptServiceConnectionRequest(peerAgent);        }    		    }         protected void onAuthenticationResponse(SAPeerAgent uPeerAgent,    		SAAuthenticationToken authToken, int error) {				if (authToken.getAuthenticationType() == SAAuthenticationToken.AUTHENTICATION_TYPE_CERTIFICATE_X509) {			mContext = getApplicationContext();			byte[] myAppKey = getApplicationCertificate(mContext);					if (authToken.getKey() != null) {				boolean matched = true;				if(authToken.getKey().length != myAppKey.length){					matched = false;				}else{					for(int i=0; i<authToken.getKey().length; i++){						if(authToken.getKey()[i]!=myAppKey[i]){							matched = false;						}					}				}								if (matched) {					acceptServiceConnectionRequest(uPeerAgent);				}							}		} else if (authToken.getAuthenticationType() == SAAuthenticationToken.AUTHENTICATION_TYPE_NONE) 			Log.e(TAG, "onAuthenticationResponse : CERT_TYPE(NONE)");			}		private  byte[] getApplicationCertificate(Context context) {		if(context == null) {			return null;		}		Signature[] sigs;		byte[] certificat = null;		String packageName = context.getPackageName();		if (context != null) {			try {				PackageInfo pkgInfo = null;				pkgInfo = context.getPackageManager().getPackageInfo(						packageName, PackageManager.GET_SIGNATURES);				if (pkgInfo == null) {					return null;				}				sigs = pkgInfo.signatures;				if (sigs == null) {				} else {					CertificateFactory cf = CertificateFactory							.getInstance("X.509");					ByteArrayInputStream stream = new ByteArrayInputStream(							sigs[0].toByteArray());					X509Certificate cert;					cert = X509Certificate.getInstance(stream);					certificat = cert.getPublicKey().getEncoded();				}			} catch (NameNotFoundException e) {				// TODO Auto-generated catch block				e.printStackTrace();			} catch (CertificateException e) {				// TODO Auto-generated catch block				e.printStackTrace();			} catch (javax.security.cert.CertificateException e) {				// TODO Auto-generated catch block				e.printStackTrace();			}		}		return certificat;	}    	@Override	protected void onFindPeerAgentResponse(SAPeerAgent arg0, int arg1) {		// TODO Auto-generated method stub	}	@Override	protected void onServiceConnectionResponse(SAPeerAgent peerAgent, SASocket thisConnection,			int result) {		if (result == CONNECTION_SUCCESS) {						if (thisConnection != null) {				mMyConnection = (FaterminatorProviderConnection) thisConnection;				if (mConnectionsMap == null) {					mConnectionsMap = new HashMap<Integer, FaterminatorProviderConnection>();				}				mMyConnection.mConnectionId = (int) (System.currentTimeMillis() & 255);				mConnectionsMap.put(mMyConnection.mConnectionId, mMyConnection);						    	Intent dialogIntent = new Intent(getBaseContext(), StartExerciseActivity.class);		    	dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);		    	getApplication().startActivity(dialogIntent);			} 		}		else if (result == CONNECTION_ALREADY_EXIST) {			Log.e(TAG, "onServiceConnectionResponse, CONNECTION_ALREADY_EXIST");		}	}	@Override	public IBinder onBind(Intent arg0) {		return m_binder;	}		public class LocalIBinder extends Binder{		public FaterminatorProviderService getService(){			return FaterminatorProviderService.this;		}	}}