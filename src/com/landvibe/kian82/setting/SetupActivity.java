package com.landvibe.kian82.setting;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inhareservation.R;
import com.google.i18n.phonenumbers.NumberParseException;
import com.landvibe.kian82.common.MenuActivity;
import com.landvibe.kian82.login.LoginActivity;
import com.landvibe.kian82.login.LoginUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class SetupActivity extends Activity implements OnClickListener
      {
private AlertDialog setup_dialog = null;
   private SharedPreferences prvPref;
   private Editor prvEditor;
   private AlertDialog developer_dialog = null;
   private AlertDialog error_dialog = null;
   private ImageButton home;
   TextView auto_login_tv;
   TextView change_information_tv;
   TextView logout_tv;
   TextView developer_tv;
   TextView change_mode;
   TextView my_circle_tv;
   private AlertDialog.Builder cancelDialog;

 
   
   Switch auto_login_cb;
   public static boolean autoc;
   
 
 
 
   int bigCategoryInt, midCategoryInt, smallCategoryInt;
   String URL;
   AsyncHttpClient client = new AsyncHttpClient();
   AsyncHttpClient clientX = new AsyncHttpClient();
   RequestParams params = new RequestParams();
   int s_seq;


   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_setup);
  	home = (ImageButton) this.findViewById(R.id.login_home_img_btn);
	home.setOnClickListener(this);
      my_circle_tv = (TextView) findViewById(R.id.setup_mycir);
      auto_login_tv = (TextView) findViewById(R.id.setup_auto_login);
      change_information_tv = (TextView) findViewById(R.id.setup_change_information);
      change_mode = (TextView) findViewById(R.id.setup_change_mode);
      logout_tv = (TextView) findViewById(R.id.setup_logout);
      developer_tv = (TextView) findViewById(R.id.setup_developer);
      auto_login_cb = (Switch) findViewById(R.id.setup_auto_login_cb);
      auto_login_cb.setChecked(LoginUtils.getautocheck(getApplicationContext()));

      auto_login_tv.setOnClickListener(this);
      change_information_tv.setOnClickListener(this);
      logout_tv.setOnClickListener(this);
      developer_tv.setOnClickListener(this);
    my_circle_tv.setOnClickListener(this);
      change_mode.setOnClickListener(this);
      s_seq = LoginUtils.getSeq(getApplicationContext());

      auto_login_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton buttonView,
               boolean isChecked) {
            // TODO Auto-generated method stub
            if (buttonView.getId() == R.id.setup_auto_login_cb) {
               if (isChecked) {
                  autoc = isChecked;
                  LoginUtils.setautocheck(SetupActivity.this, isChecked);

               } else {
                  autoc = isChecked;
                  LoginUtils.setautocheck(SetupActivity.this, isChecked);
               }
            }
         }
      });
      
      cancelDialog = new AlertDialog.Builder(this);
		cancelDialog.setTitle("로그아웃 하시겠습니까?");
		makeCancleDialog();

 
       
   }
   
	public void makeCancleDialog(){	
		cancelDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (LoginUtils.isLoggedIn(getApplicationContext()) == true)
					LoginUtils.removeSeqName(getApplicationContext());
				//LoginUtils.removeautocheck(getApplicationContext());
				Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
				startActivity(myIntent);
				overridePendingTransition(0, 0);
				MenuActivity aActivity = (MenuActivity)MenuActivity.menuActivity;
				aActivity.finish();
				finish();
			}
		});
		cancelDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		
	}

   @Override
   public void onClick(View v) {
      // TODO Auto-generated method stub

      switch (v.getId()) {
      case R.id.setup_mycir: {
        Intent myIntent = new Intent(this,SetupCircleActivity.class);
        startActivity(myIntent);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
         }
         break;

      case R.id.login_home_img_btn: {
    	  v.startAnimation(AnimationUtils.loadAnimation(this,
					R.anim.button_click));
			v.startAnimation(AnimationUtils.loadAnimation(this,
					R.anim.button_click2));
			Intent myIntent = new Intent(SetupActivity.this,
					MenuActivity.class);
			startActivity(myIntent);
			overridePendingTransition(R.anim.slide_in_from_right,
					R.anim.slide_out_to_left);
            }
           break;
      
      case R.id.setup_change_information: {
         Intent myIntent = new Intent(this,SetupChangeInfoActivity.class);
         startActivity(myIntent);
         overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
      }

         break;
  	case R.id.setup_logout: {
		
  			cancelDialog.show();
	}

         break;
      case R.id.setup_developer: {
         developer_dialog = developerinfo();
         developer_dialog.show();
      }
         break;
      
      case R.id.setup_change_mode: {
         Intent myIntent = new Intent(Intent.ACTION_VIEW);
         Uri u = Uri.parse("https://portal.inha.ac.kr/");
         myIntent.setData(u);
         startActivity(myIntent);
      }
         break;
      }

   }



   private AlertDialog developerinfo() {
      AlertDialog.Builder dialog = new AlertDialog.Builder(this);
      dialog.setMessage("\n[Developer]  \n\n    이 현 석\n    이 상 직\n    최 승 위\n    이 성 온\n    김 형 준\n\n[Designer] \n\n    Kim Han Sol  \n\n\n Copyright Kian82. \n All Rights Reserved.");
      TextView title = new TextView(this);
		title.setText("개발자 정보");
		title.setBackgroundColor(Color.DKGRAY);
		title.setPadding(20, 20, 20, 20);
		title.setGravity(Gravity.CENTER);
		title.setTextColor(Color.WHITE);
		title.setTextSize(30);

		dialog.setCustomTitle(title);
      dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface arg0, int arg1) {
            developer_dialog.dismiss();
         }
      });
      return dialog.create();
   }
   
	public void onBackPressed() {

	    // 여기에 코드 입력
		
		finish();
		overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);

	}



}