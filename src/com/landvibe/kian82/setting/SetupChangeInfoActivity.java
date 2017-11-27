package com.landvibe.kian82.setting;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.example.inhareservation.R;
import com.landvibe.kian82.common.MenuActivity;
import com.landvibe.kian82.login.LoginUtils;
import com.landvibe.kian82.login.SignUpActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class SetupChangeInfoActivity extends Activity implements
		OnClickListener {
	private ImageButton home;
	Button joinBtn;
	Button cancelBtn;
	TextView nametv;
	TextView idtv;
	EditText passwordEdit;
	EditText majorEdit;
	EditText numberEdit;
	EditText emailEdit;
	Spinner emailDomainSpinner;
	int s_seq;
	int sp_num;
	AsyncHttpClient clients = new AsyncHttpClient();
	RequestParams param = new RequestParams();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup_change_info);

		joinBtn = (Button) findViewById(R.id.join_yes_btn);
		cancelBtn = (Button) findViewById(R.id.join_no_btn);
		idtv = (TextView) findViewById(R.id.join_id_et);
		nametv = (TextView) findViewById(R.id.join_name_et);
		passwordEdit = (EditText) findViewById(R.id.join_password_et);
		majorEdit = (EditText) findViewById(R.id.join_major_et);
		numberEdit = (EditText) findViewById(R.id.join_number_et);
		emailEdit = (EditText) findViewById(R.id.textEmailAddress);
		home = (ImageButton) this.findViewById(R.id.login_home_img_btn);
		home.setOnClickListener(this);
		joinBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		s_seq = LoginUtils.getSeq(getApplicationContext());

		emailDomainSpinner = (Spinner) findViewById(R.id.emailDomain);
		ArrayAdapter<?> emailAdapter = ArrayAdapter.createFromResource(this,
				R.array.emailList, R.layout.spinner_item);
		emailAdapter
				.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
		emailDomainSpinner.setAdapter(emailAdapter);
		emailDomainSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub
						sp_num = emailDomainSpinner.getSelectedItemPosition();
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}

				});

		param.put("s_seq", s_seq);
		String URL = "http://oursoccer.co.kr/study/reserve/setup.php";
		clients.post(URL, param, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {

				for (int i = 0; i < response.length(); i++) {
					try {
						Log.e("선택날짜는", "" + s_seq);
						String id = response.getJSONObject(i).getString("s_id");
						String name = response.getJSONObject(i).getString(
								"s_name");
						String pwd = response.getJSONObject(i).getString(
								"s_pwd");
						String major = response.getJSONObject(i).getString(
								"s_major");
						String phone = response.getJSONObject(i).getString(
								"s_phone");
						String email = response.getJSONObject(i).getString(
								"s_email");
						int email_domain = response.getJSONObject(i).getInt(
								"s_email_domain");

						emailDomainSpinner.setSelection(email_domain);
						nametv.setText(name);
						idtv.setText(id);
						numberEdit.setText(phone);
						passwordEdit.setText(pwd);
						majorEdit.setText(major);
						emailEdit.setText(email);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);

				// Toast.makeText(Setup_Change_Info_Activity.this,statusCode,
				// Toast.LENGTH_LONG).show();
			}
		});

	}

	@Override
	public void onClick(View v) {
		if (joinBtn == v) {

			String URL = "http://oursoccer.co.kr/study/reserve/updateinfo.php";
			AsyncHttpClient client = new AsyncHttpClient();
			RequestParams params = new RequestParams();
			params.put("s_seq", s_seq);
			
			params.put("s_email_domain", emailDomainSpinner.getSelectedItemPosition());
			params.put("s_email", emailEdit.getText());
			params.put("s_pwd", passwordEdit.getText());
			params.put("s_major", majorEdit.getText());
			params.put("s_phone", numberEdit.getText());
			client.post(URL, params, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						String responseString, Throwable throwable) {
					Toast.makeText(SetupChangeInfoActivity.this,
							"개인정보가 변경되었습니다.", Toast.LENGTH_LONG).show();
					finish();
				}
			});
		}

		if (cancelBtn == v) {
			finish();
			overridePendingTransition(R.anim.slide_in_from_left,
					R.anim.slide_out_to_right);
		}
		if (v == home) {
			v.startAnimation(AnimationUtils.loadAnimation(this,
					R.anim.button_click));
			v.startAnimation(AnimationUtils.loadAnimation(this,
					R.anim.button_click2));
			Intent myIntent = new Intent(SetupChangeInfoActivity.this,
					MenuActivity.class);
			startActivity(myIntent);
			overridePendingTransition(R.anim.slide_in_from_top,
					R.anim.slide_out_to_buttom);
		}
	}

	public void onBackPressed() {

		// 여기에 코드 입력

		finish();
		overridePendingTransition(R.anim.slide_in_from_left,
				R.anim.slide_out_to_right);

	}
}
