package com.samsung.android.example.Faterminatorprovider.service;
/**
 * Created by HJH 
 */
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import smartPhone.FileFacade.FinalStaticVariables;
import smartPhone.activityIdentifier.CollectedSensingData;

import com.samsung.android.example.Faterminatorprovider.R;
import com.samsung.android.example.Faterminatorprovider.service.FaterminatorProviderService.LocalIBinder;

import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class StartExerciseActivity extends Activity implements OnClickListener{
	
	private Button m_btnStartExercise;
	private Button m_btnStopExercise;
//	private Button m_btnRunning;
//	private Button m_btnWalking;
//	private Button m_btnSkippingRope;
//	private Button m_btnJumpingJack;
	private TextView m_tvExercising;
	private ProgressBar m_pgMeaningless;

	private FaterminatorProviderService m_helloService;
	private boolean m_bound = false; 
	
	private boolean mlsBackButtonTouched=false; //Back key가 한번 눌렸는가 flag
	private Timer timer; 			//Back key를 2초안에 두번눌러야 종료되도록 타이머 설정
	
	private boolean m_isExerStarted = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_exercise);
		m_btnStartExercise = (Button)findViewById(R.id.startExercise);
		m_btnStartExercise.setOnClickListener(this);
		
		m_btnStopExercise = (Button)findViewById(R.id.stopExercise);
		m_btnStopExercise.setOnClickListener(this);
		
//		m_btnRunning = (Button)findViewById(R.id.RunningBtn);
//		m_btnRunning.setOnClickListener(this);
//		
//		m_btnWalking = (Button)findViewById(R.id.WalkingBtn);
//		m_btnWalking.setOnClickListener(this);
//		
//		m_btnSkippingRope = (Button)findViewById(R.id.SkippingRope);
//		m_btnSkippingRope.setOnClickListener(this);
//		
//		m_btnJumpingJack = (Button)findViewById(R.id.JumpingJack);
//		m_btnJumpingJack.setOnClickListener(this);
		
		m_tvExercising = (TextView)findViewById(R.id.textView1);
		m_pgMeaningless = (ProgressBar)findViewById(R.id.progressBar1);
		
		m_btnStartExercise.setVisibility(View.VISIBLE);
		m_btnStopExercise.setVisibility(View.INVISIBLE);
		m_tvExercising.setVisibility(View.INVISIBLE);
		m_pgMeaningless.setVisibility(View.INVISIBLE);
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		Intent intent = new Intent(this, FaterminatorProviderService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(m_bound)
		{
			unbindService(mConnection);
			m_bound=false;
			
		}
		if(m_isExerStarted)
		{
			m_helloService.sendMsg("stop sensing");
			
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if ( arg0 == m_btnStartExercise)
		{
			if (m_bound && !m_isExerStarted)
			{
				String strUserInfoPath = "user_info.csv";
			    String userInfoPath =FinalStaticVariables.DIR_FOLDER+strUserInfoPath;
			    
			    File file = new File(userInfoPath);
			    if(!file.exists())
			    {
			    	Intent dialogIntent = new Intent(getBaseContext(), MessageViewerActivity.class);
			    	dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			    	getApplication().startActivity(dialogIntent);
			    	m_helloService.sendMsg("stop sensing");
			    	Log.d("try send msg","true");
			    }
			    else
			    {
				    Log.d("try send msg","true");
					m_helloService.sendMsg("start sensing");
					m_btnStartExercise.setVisibility(View.INVISIBLE);
					m_btnStopExercise.setVisibility(View.VISIBLE);
					m_tvExercising.setVisibility(View.VISIBLE);
					m_pgMeaningless.setVisibility(View.VISIBLE);
					m_isExerStarted=true;
			    }
			    
			}
		}
		else if ( arg0 == m_btnStopExercise)
		{
			if (m_bound && m_isExerStarted)
			{
				Log.d("try send msg","true");
				m_helloService.sendMsg("stop sensing");
				m_btnStartExercise.setVisibility(View.VISIBLE);
				m_btnStopExercise.setVisibility(View.INVISIBLE);
				m_tvExercising.setVisibility(View.INVISIBLE);
				m_pgMeaningless.setVisibility(View.INVISIBLE);
				m_isExerStarted=false;
			}
		}
//		else if ( arg0 == m_btnRunning)
//		{
//			if (m_bound)
//			{
//				Log.d("try send msg","true");
//				m_helloService.sendMsg("start exercise,Running");
//			}
//		}
//		else if ( arg0 == m_btnWalking)
//		{
//			if (m_bound)
//			{
//				Log.d("try send msg","true");
//				m_helloService.sendMsg("start exercise,Walking");
//			}
//		}
//		else if ( arg0 == m_btnSkippingRope)
//		{
//			if (m_bound)
//			{
//				Log.d("try send msg","true");
//				m_helloService.sendMsg("start exercise,SkippingRope");
//			}
//		}
//		else if ( arg0 == m_btnJumpingJack)
//		{
//			if (m_bound)
//			{
//				Log.d("try send msg","true");
//				m_helloService.sendMsg("start exercise,JumpingJack");
//			}
//		}
	}
	
	private ServiceConnection mConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			m_bound = false;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			LocalIBinder binder = (LocalIBinder) service;
			m_helloService = binder.getService();
			m_bound = true;
		}
	};
	
	@Override
	public void onBackPressed(){
		if(!mlsBackButtonTouched){
			mlsBackButtonTouched=true;
			Toast.makeText(this,"One more press back btn, it will exit.",Toast.LENGTH_SHORT).show();
			
			//back키가 2초내에 두번 눌렸는지 감지
			TimerTask seconds = new TimerTask(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					timer.cancel();
					timer = null;
					mlsBackButtonTouched = false;
				}
				
			};
			if(timer!=null)
			{
				timer.cancel();
				timer=null;
			}
			timer = new Timer();
			timer.schedule(seconds, 2000);
		}
		else if (mlsBackButtonTouched){
			//안전하게 종료할 수 있는 상황인지 파악 후 종료
			onClick(m_btnStopExercise);
			finish();
		}
	}//onBackPressed(); Back key가 눌렸을 때 이벤트 발생
}
