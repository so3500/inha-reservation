package com.landvibe.kian82.login;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.inhareservation.R;
import com.landvibe.kian82.common.MenuActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class LoginActivity extends Activity implements OnClickListener {
	Button registerBtn;
	Button loginBtn;
	EditText idEdit;
	EditText passwordEdit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		registerBtn = (Button) findViewById(R.id.login_join_btn);
		loginBtn = (Button) findViewById(R.id.login_login_btn);

		idEdit = (EditText) findViewById(R.id.login_id_edit);
		passwordEdit = (EditText) findViewById(R.id.login_pwd_edit);

		loginBtn.setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (LoginUtils.isLoggedIn(LoginActivity.this) == true
				&& LoginUtils.getautocheck(getApplicationContext()) == true) {
			Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
			startActivity(intent);
			overridePendingTransition(0, 0);
			finish();
		}
		if (LoginUtils.isLoggedIn(LoginActivity.this) == false
				&& LoginUtils.getautocheck(getApplicationContext()) == true) {

		}
		if (LoginUtils.isLoggedIn(LoginActivity.this) == true
				&& LoginUtils.getautocheck(getApplicationContext()) == false) {
			Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
			startActivity(intent);
			overridePendingTransition(0, 0);
			finish();
		}
		if (LoginUtils.isLoggedIn(LoginActivity.this) == false
				&& LoginUtils.getautocheck(getApplicationContext()) == false) {

		}

		// if (LoginUtils.isLoggedIn(LoginActivity.this)) {
		// Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
		// startActivity(intent);
		// overridePendingTransition(0, 0);
		// finish();
		// }

	}

	@Override
	public void onClick(View v) {
		if (registerBtn == v) {
			Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
		}

		if (loginBtn == v) {
			v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click));
			v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click2));
			AsyncHttpClient client = new AsyncHttpClient();
			RequestParams params = new RequestParams();
			params.put("s_id", idEdit.getText());
			params.put("s_pwd", passwordEdit.getText());
			client.post("http://oursoccer.co.kr/study/reserve/login.php",
					params, new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							super.onSuccess(statusCode, headers, response);
							try {
								LoginUtils.storeSeq(LoginActivity.this,
										response.getInt("s_seq"),
										response.getString("s_name"));
								Toast.makeText(
										LoginActivity.this,
										response.getString("s_name")
												+ "님 안녕하세요", Toast.LENGTH_SHORT)
										.show();
								onResume();
							} catch (Exception e) {
								Toast.makeText(LoginActivity.this,
										"로그인 중 오류가 발생하였습니다.", Toast.LENGTH_LONG)
										.show();
							}
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								String responseString, Throwable throwable) {
							super.onFailure(statusCode, headers,
									responseString, throwable);
							Toast.makeText(LoginActivity.this,
									"로그인 중 오류가 발생하였습니다." + responseString,
									Toast.LENGTH_LONG).show();
						}
					});
		}
	}

}
