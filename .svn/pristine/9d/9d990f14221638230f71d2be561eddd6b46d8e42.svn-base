package com.landvibe.kian82.common;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inhareservation.R;
import com.landvibe.kian82.group_reservation.GroupReservationActivity;
import com.landvibe.kian82.login.LoginActivity;
import com.landvibe.kian82.login.LoginUtils;
import com.landvibe.kian82.my_reservation.MyReservationActivity;
import com.landvibe.kian82.reserve.ReserveCalendarActivity;
import com.landvibe.kian82.schedule.ScheduleActivity;
import com.landvibe.kian82.setting.SetupActivity;

public class MenuActivity extends Activity implements OnClickListener {

	RelativeLayout reserve_tv;
	RelativeLayout schedule_tv;
	RelativeLayout my_reserve_tv;
	RelativeLayout group_reserve_tv;
	ImageButton log_out_tv;
	  private SharedPreferences prvPref;
	private Editor prvEditor;
	  private BackPressCloseSystem backPressCloseSystem;
	  public static Activity menuActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		reserve_tv = (RelativeLayout) findViewById(R.id.menu_reserve_tv);
		schedule_tv = (RelativeLayout) findViewById(R.id.menu_schedule_tv);
		my_reserve_tv = (RelativeLayout) findViewById(R.id.menu_my_reserve_tv);
		group_reserve_tv = (RelativeLayout) findViewById(R.id.menu_group_reserve_tv);
		log_out_tv = (ImageButton) findViewById(R.id.menu_log_out_btn);

		reserve_tv.setOnClickListener(this);
		schedule_tv.setOnClickListener(this);
		my_reserve_tv.setOnClickListener(this);
		group_reserve_tv.setOnClickListener(this);
		log_out_tv.setOnClickListener(this);
		  backPressCloseSystem = new BackPressCloseSystem(this);
		  menuActivity = MenuActivity.this;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.menu_reserve_tv: {
			v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click));
			v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click2));
			Intent myIntent = new Intent(this, ReserveCalendarActivity.class);
			startActivity(myIntent);
			overridePendingTransition(R.anim.slide_in_from_buttom, R.anim.slide_out_to_top);
		}

			break;
		case R.id.menu_schedule_tv: {
			v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click));
			v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click2));
			Intent myIntent = new Intent(this, ScheduleActivity.class);
			startActivity(myIntent);
			overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
		}

			break;
		case R.id.menu_my_reserve_tv: {
			v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click));
			v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click2));
			Intent myIntent = new Intent(this, MyReservationActivity.class);
			startActivity(myIntent);
			overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
		}

			break;
		case R.id.menu_group_reserve_tv: {
			v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click));
			v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click2));
			Intent myIntent = new Intent(this, GroupReservationActivity.class);
			startActivity(myIntent);
			overridePendingTransition(R.anim.slide_in_from_top, R.anim.slide_out_to_buttom);
		}
		break;
		case R.id.menu_log_out_btn:
		{
			v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click3));
			v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click4));
			Intent myIntent = new Intent(this,SetupActivity.class);
			startActivity(myIntent);
			overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
		}
			break;
		}

	}

	
	@Override
    public void onBackPressed() {
                     backPressCloseSystem.onBackPressed();
    }
//	  @Override
//	public void onStop(){
//		  super.onStop();
//		  
//		if(LoginUtils.getautocheck(getApplicationContext()) ==false)
//		{
//			LoginUtils.removeSeqName(getApplicationContext());
//			
//		}
//		else if(LoginUtils.getautocheck(getApplicationContext()) ==true)
//		{
//			
//		}
//	}

	  
	  public class BackPressCloseSystem {

		    private long backKeyPressedTime = 0;
		    private Toast toast;

		     private Activity activity;

		     public BackPressCloseSystem(Activity activity) {
		                  this.activity = activity;
		    }

		     public void onBackPressed() {

		                   if (isAfter2Seconds()) {
		                           backKeyPressedTime = System.currentTimeMillis();
		                           // 현재시간을 다시 초기화

		                            toast = Toast.makeText(activity, 
		                                                                    "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.",
		                                                                    Toast.LENGTH_SHORT);
		                           toast.show();

		                            return;
		                  }

		                  if (isBefore2Seconds()) {
		                	
		                            programShutdown();
		                            toast.cancel();
		                            if(LoginUtils.getautocheck(getApplicationContext()) ==false)
		                        	{
				              			LoginUtils.removeSeqName(getApplicationContext());
				              			LoginUtils.removeautocheck(getApplicationContext());
				              			SetupActivity.autoc = false;
				              		}
				              		else if(LoginUtils.getautocheck(getApplicationContext()) ==true)
				              		{
				              			
				              		}
		                  }
		     }

		      private boolean isAfter2Seconds() {
		              return System.currentTimeMillis() > backKeyPressedTime + 2000;
		                             // 2초 지났을 경우
		     }  

		      private boolean isBefore2Seconds() {
		              return System.currentTimeMillis() <= backKeyPressedTime + 2000;
		                             // 2초가 지나지 않았을 경우
		              
		     }

		      private void programShutdown() {
		                     activity .moveTaskToBack(true);
		                     activity .finish();
		                //     android.os.Process.killProcess(android.os.Process.myPid());
		                   //  System.exit(0);
		                   
			              	
		     }
		}
	  
}


