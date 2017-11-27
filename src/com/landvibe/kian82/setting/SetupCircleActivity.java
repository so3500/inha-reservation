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
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inhareservation.R;
import com.google.i18n.phonenumbers.NumberParseException;
import com.landvibe.kian82.login.LoginActivity;
import com.landvibe.kian82.login.LoginUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class SetupCircleActivity extends Activity implements OnClickListener {

	private SharedPreferences prvPref;
	private Editor prvEditor;

	TextView my_circle;

	Button ok_btn;
	Button cancle_btn;
	Button erase_btn;

	boolean isThereError = false;
	Spinner bigCategorySpinner = null, midCategorySpinner = null,
			smallCategorySpinner = null;

	EditText circleEditText;
	String circleStr=null;
	int bigCategoryInt=-1, midCategoryInt=-1, smallCategoryInt=-1;
	String URL;
	AsyncHttpClient client = new AsyncHttpClient();
	AsyncHttpClient clientX = new AsyncHttpClient();
	RequestParams params = new RequestParams();
	RequestParams param = new RequestParams();
	RequestParams p = new RequestParams();
	int s_seq;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup_circle);

		s_seq = LoginUtils.getSeq(getApplicationContext());
		ok_btn = (Button) findViewById(R.id.submit_btn);
		cancle_btn = (Button) findViewById(R.id.cancle_btn);
		erase_btn = (Button) findViewById(R.id.erase_btn);
		my_circle = (TextView) findViewById(R.id.mycircle);

		ok_btn.setOnClickListener(this);
		cancle_btn.setOnClickListener(this);
		erase_btn.setOnClickListener(this);
		param.put("s_seq", s_seq);
		client.post("http://oursoccer.co.kr/study/reserve/setup.php", param,
				new JsonHttpResponseHandler() {

					public void onSuccess(int statusCode, Header[] headers,
							JSONArray response) {

						// Toast.makeText(Setup_Change_Info_Activity.this,statusCode,
						// Toast.LENGTH_LONG).show();
						for (int i = 0; i < response.length(); i++) {
							try {
								int num = response.getJSONObject(i)
										.getInt("s_smallcategory");
								String name = response.getJSONObject(i)
										.getString("s_circle");

								if (num != -1)
									my_circle.setText("나의 단체는 " + name+ " 입니다.");
								else if (num == -1)
									my_circle.setText("지정된 단체가 없습니다.");

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						super.onFailure(statusCode, headers, responseString,
								throwable);

						Toast.makeText(SetupCircleActivity.this, "텍스트뷰 실패",
								Toast.LENGTH_SHORT).show();

					}

				});

		bigCategorySpinner = (Spinner) findViewById(R.id.bigcategory);
		midCategorySpinner = (Spinner) findViewById(R.id.midcategory);
		smallCategorySpinner = (Spinner) findViewById(R.id.smallcategory);

		ArrayAdapter<?> bigCategoryAdapter = ArrayAdapter.createFromResource(
				this, R.array.big_categoryList, R.layout.spinner_item);
		bigCategoryAdapter
				.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);

		bigCategorySpinner.setAdapter(bigCategoryAdapter);

		circleEditText = (EditText) findViewById(R.id.circle_ed);
		
		bigCategorySpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							final int bigPosition, long arg3) {
						midCategorySpinner.setSelection(0);
						smallCategorySpinner.setSelection(0);
						if (1 <= bigPosition && bigPosition <= 2) {
							circleEditText.setText(null);
						}
						final int base = R.array.big_categoryList;
						final int[] offset = { 0, 1, 12, 15, 27, 40, 44 };
						if (bigPosition != 0) {
							ArrayAdapter<?> midCategoryAdapter = ArrayAdapter
									.createFromResource(
											SetupCircleActivity.this, base
													+ offset[bigPosition],
											R.layout.spinner_item);
							midCategoryAdapter
									.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
							midCategorySpinner.setAdapter(midCategoryAdapter);
							// midCategorySpinner.requestFocus();
							
							
							midCategorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
										

										@Override
										public void onItemSelected(
												AdapterView<?> arg0, View arg1,
												int midPosition, long arg3) {
											circleEditText.setText(null);
											// TODO Auto-generated method stub
											if (midPosition != 0) {
												ArrayAdapter<?> smallCategoryAdapter = ArrayAdapter
														.createFromResource(
																SetupCircleActivity.this,
																base
																		+ offset[bigPosition]
																		+ midPosition,
																R.layout.spinner_item);
												smallCategoryAdapter
														.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
												smallCategorySpinner
														.setAdapter(smallCategoryAdapter);
												// smallCategorySpinner.requestFocus();

												smallCategorySpinner
														.setOnItemSelectedListener(new OnItemSelectedListener() {

															@Override
															public void onItemSelected(
																	AdapterView<?> arg0,
																	View arg1,
																	int smallPosition,
																	long arg3) {
																// TODO
																// Auto-generated
																// method stub
																if (1 <= bigPosition
																		&& bigPosition <= 2
																		&& smallPosition != 0) {
																	circleEditText
																			.setText(smallCategorySpinner
																					.getSelectedItem()
																					.toString());
																	
																} else if (bigPosition > 2
																		&& smallPosition != 0) {
																	circleEditText.setText(null);
																	// circleEditText.requestFocus();
																}
																bigCategoryInt = bigCategorySpinner.getSelectedItemPosition();
																midCategoryInt = midCategorySpinner.getSelectedItemPosition();
																smallCategoryInt = smallCategorySpinner.getSelectedItemPosition();
															
																}

															@Override
															public void onNothingSelected(
																	AdapterView<?> arg0) {
																// TODO
																// Auto-generated
																// method stub

															}
														});
											}
										}

										@Override
										public void onNothingSelected(
												AdapterView<?> arg0) {
											// TODO Auto-generated method stub

										}
									});
						}
					
					
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
					
			
				});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
//		if( circleEditText.getText().toString()!=null){
//		circleStr = circleEditText.getText().toString();
//		}
		circleStr = circleEditText.getText().toString();
		switch (v.getId()) {
		case R.id.submit_btn: {

			if (circleStr.isEmpty() == false &&bigCategoryInt !=-1&&midCategoryInt != 0 &&smallCategoryInt !=0) {
				
				params.put("s_seq", s_seq);
				params.put("big_category", bigCategoryInt);
				params.put("mid_category", midCategoryInt);
				params.put("small_category", smallCategoryInt);
				params.put("s_circle", circleStr);

				client.post(
						"http://oursoccer.co.kr/study/reserve/setup_mycircle.php",
						params, new JsonHttpResponseHandler() {
							@Override
							public void onSuccess(int statusCode,
									Header[] headers, JSONArray response) {

							}

							@Override
							public void onFailure(int statusCode,
									Header[] headers, String responseString,
									Throwable throwable) {
								// TODO Auto-generated method stub
								super.onFailure(statusCode, headers,
										responseString, throwable);
								
								finish();
							}
						});
			}
			
			else
				{
				Toast.makeText(SetupCircleActivity.this,"정확한 정보를 입력하세요", Toast.LENGTH_SHORT).show();
				}
		}
			break;
		case R.id.cancle_btn: {
			finish();
		}
			break;
		case R.id.erase_btn: {
			bigCategoryInt = -1;
			midCategoryInt = -1;
			smallCategoryInt = -1;
			circleStr = null;

			params.put("s_seq", s_seq);
			params.put("big_category", bigCategoryInt);
			params.put("mid_category", midCategoryInt);
			params.put("small_category", smallCategoryInt);
			params.put("s_circle", circleStr);

			client.post(
					"http://oursoccer.co.kr/study/reserve/setup_mycircle.php",
					params, new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONArray response) {

						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								String responseString, Throwable throwable) {
							// TODO Auto-generated method stub
							super.onFailure(statusCode, headers,
									responseString, throwable);
							Toast.makeText(SetupCircleActivity.this, "제거 성공",
									Toast.LENGTH_SHORT).show();
							finish();
						}
					});

		}
			break;
		}

	}
	
	public void onBackPressed() {

	    // 여기에 코드 입력
		
		finish();
		overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);

	}

}