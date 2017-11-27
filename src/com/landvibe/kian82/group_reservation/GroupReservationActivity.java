package com.landvibe.kian82.group_reservation;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat.Style;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.example.inhareservation.R;
import com.landvibe.kian82.common.MenuActivity;
import com.landvibe.kian82.common.PagerSlidingTabStrip;
import com.landvibe.kian82.login.LoginUtils;
import com.landvibe.kian82.my_reservation.MyReservationActivity;
import com.landvibe.kian82.my_reservation.MyReservationAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

public class GroupReservationActivity extends FragmentActivity implements OnClickListener {
	String[] circle_list;
	private PagerSlidingTabStrip tab;
	private ViewPager pager;
	private String[] tabTitle;
	public static GroupReservationAdapter adapter_groupreservation;
	public ListView listview;
	Button groupReservation_date_tv_btn;
	Button button;
	EditText groupName_et;
	ImageButton imgButton;
	String dateYear;  // 일시
	String groupName; // 단체명
	TextView groupName_tv;
	
	ImageButton goToMenu_btn;
	// 달력
	static int myYear;
	int myMonth;
	int myDay;
	GregorianCalendar calendar;
	TextView date_tv;
	DatePickerDialog myDialog;
	private boolean isOkayClicked = false;
	
	private int currentColor = 0xFF666666;
	
	private int student_seq;
	private String student_name;
	
	AutoCompleteTextView autocompletetextview;
	Button refreshReserveBtn;
	int tabPosition;
	boolean firstActivityOpened;
	
	RequestParams param;
	SyncHttpClient client;
	int s_seq;
	boolean excuteBefore;
	
	//ReserveSelectTask reserveSelectTask;
	//Integer[] taskParams = new Integer[1];
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_reservation);
		firstActivityOpened = true;
		excuteBefore = false;
		
		// res/values 에 선언된 string을 가져오는 방법
		Resources res = getResources();
		circle_list = res.getStringArray(R.array.circle_list);
		ArrayList<String> circleList = new ArrayList<String>();
		for(String str : circle_list){
			circleList.add(str);
		}
		tabTitle  = res.getStringArray(R.array.groupreservation_tabtitle);
		
		groupName = null;
		

		goToMenu_btn = (ImageButton) findViewById(R.id.login_home_img_btn);
		goToMenu_btn.setOnClickListener(this);
		
		student_seq = LoginUtils.getSeq(this);
		student_name = LoginUtils.getName(this);
		
		myDialog = new DatePickerDialog(GroupReservationActivity.this, dateSetListener, myYear, myMonth, myDay);		
		
		//////////////////// 탭 어뎁터 //////////////////////
		tab = (PagerSlidingTabStrip) this.findViewById(R.id.group_reservation_tap);
		pager = (ViewPager) this.findViewById(R.id.group_reservation_pager);
		MyPagerAdapter adpt = new MyPagerAdapter(getSupportFragmentManager());
		// Adapter > ViewPager > Tap
		// 초기화 한 tab, pager을 가지고 뷰페이저에 어댑터를 맞추고
		pager.setAdapter(adpt);
		// 탭에다가 뷰페이저를 맞춰준다 순서중요!!
		tab.setViewPager(pager);
		/////////////////////////////////////////////////////
		
		calendar = new GregorianCalendar();
		myYear = calendar.get(Calendar.YEAR);
		myMonth = calendar.get(Calendar.MONTH);
		myDay = calendar.get(Calendar.DAY_OF_MONTH);
		myDialog = new DatePickerDialog(GroupReservationActivity.this, dateSetListener, myYear, myMonth, myDay);
		pager.setCurrentItem(myMonth);
		myDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "확인", myDialog);
	    myDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "취소", myDialog);
	    
		// 일시 dialog 설정
		date_tv = (TextView) findViewById(R.id.groupReservation_showReservation_tv);
		date_tv.setOnClickListener(this);
		
		AutoCompleteAdapter adapter = new AutoCompleteAdapter(this, R.layout.activity_group_reservation_listlayout, circleList);
		autocompletetextview = (AutoCompleteTextView) findViewById(R.id.groupReservation_Autotv);
		autocompletetextview.setThreshold(1);
		autocompletetextview.setAdapter(adapter);
		
		refreshReserveBtn = (Button) findViewById(R.id.groupreservation_refresh_btn);
	    refreshReserveBtn.setOnClickListener(this);

		// 처음 액티비티가 시작할때 시스템의 월로 탭 시작
		if(firstActivityOpened == true){
			pager.setCurrentItem(myMonth);
			firstActivityOpened = false;
		}
		
		param = new RequestParams();
		client = new SyncHttpClient();
		
		dialogDeleteDayMonth(); // 년만 표시
		dialogSetButton();
		autocompletetextviewSetFunction();

		if( excuteBefore == false){
			excuteBefore = true;
			GetGroupNameThread thread = new GetGroupNameThread();
			thread.start();
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		/*s_seq = LoginUtils.getSeq(GroupReservationActivity.this);
		//reserveSelectTask = new ReserveSelectTask();
		//taskParams[0] = s_seq;
		//reserveSelectTask.execute(taskParams);
*/		
		
		updateDateText(); // 선택한 날짜
		autocompletetextview.dismissDropDown();
	}

	////////////////////////////////////// 동아리명 검색 ////////////////////////////

private void autocompletetextviewSetFunction() {
	autocompletetextview.setOnItemClickListener(new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			AutoCompleteTextView autocompletetextview = (AutoCompleteTextView) findViewById(R.id.groupReservation_Autotv);
			
			updateDateText();
			// 열려있는 입력 키보드 닫기
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(autocompletetextview.getWindowToken(), 0);

		}});/////////	
	
	autocompletetextview.setOnEditorActionListener(new OnEditorActionListener() {
//KeyEvent.KEYCODE_SEARCH
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (event != null&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) ) {
            	
            	updateDateText();
            	// 열려있는 입력창 닫기
            	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    			imm.hideSoftInputFromWindow(autocompletetextview.getWindowToken(), 0);

            }
            return false;
        }
    });
	}

	/////////////////////////////////////////////// 동아리명 검색 끝 ////////////////////////////////////
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("currentColor", currentColor);
	}



	
	// 달력 선택 버튼 설정(확인, 취소), LG G2, 갤럭시S3 에서 차이가 나서 주석처리
		public void dialogSetButton() {
			
			myDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "확인", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (which == DialogInterface.BUTTON_POSITIVE) {
						isOkayClicked = true;
					}
				}
			});
			myDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "취소", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (which == DialogInterface.BUTTON_NEGATIVE) {
						myDialog.cancel();
						isOkayClicked = false;
					}

				}
			});
		}
	
	// datePicker DateSet 설정
		private OnDateSetListener dateSetListener = new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				if(isOkayClicked == true) {
					myYear = year;
					myMonth = monthOfYear;
					myDay = dayOfMonth;
					calendar.set(Calendar.YEAR, year);
					calendar.set(Calendar.MONTH, monthOfYear+1);
					calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
					updateDateText();
				}

				}//Adapter_MyReservation
		};
		
		// updateDiaplay
	public void updateDateText() {
		tabPosition = pager.getCurrentItem();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
		date_tv.setText(dateFormat.format(calendar.getTime()));
		dateYear = dateFormat.format(calendar.getTime());
		groupName = autocompletetextview.getText().toString();
		Log.d("error__", "autocompletetextview.getText().toString(); : " + autocompletetextview.getText().toString());
		Log.d("error__", "autocompletetextview.getText().toString(); : " + autocompletetextview.getEditableText().toString());
		if(groupName.equals("")){
			groupName = "There is no group";
		}
		MyPagerAdapter adpt = new MyPagerAdapter(getSupportFragmentManager());		
		pager.setAdapter(adpt);			
		pager.setCurrentItem(tabPosition);
		}	
	// datePicker 일 삭제
	public void dialogDeleteDayMonth() {

		try {
			// import java.lang.reflect.Field;
			Field[] f = myDialog.getClass().getDeclaredFields();
			for (Field dateField : f) {
				// Log.d("date_", "dateField.getName() " + dateField.getName());
				if (dateField.getName().equals("mDatePicker")) {
					dateField.setAccessible(true);

					DatePicker datePicker = (DatePicker) dateField.get(myDialog);
					Field datePickerFields[] = dateField.getType().getDeclaredFields();

					for (Field datePickerField : datePickerFields) {
						 //Log.d("date_", "datePickerField.getName() " + datePickerField.getName());
						if ("mDaySpinner".equals(datePickerField.getName())) {
							datePickerField.setAccessible(true);
							Object dayPicker = new Object();
							dayPicker = datePickerField.get(datePicker);
							((View) dayPicker).setVisibility(View.GONE);
						}
						if ("mMonthSpinner".equals(datePickerField.getName())) {
							datePickerField.setAccessible(true);
							Object monthPicker = new Object();
							monthPicker = datePickerField.get(datePicker);
							((View) monthPicker).setVisibility(View.GONE);
						}
					}
				}
			}
			// myDialog.show(); // 이거하면 시작, 취소버튼 정의가 안됨
		}

		catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}

	} // dialogDeleteDay() 끝
	
	
	
	// 클릭 이벤트 처리 메소드
	@Override
	public void onClick(View v) {
		switch (v.getId()) {		
		case R.id.groupReservation_showReservation_tv:
			v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click));
		    v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click2));
			myDialog.setTitle("날짜선택");
			myDialog.show();
			break;
			
		case R.id.login_home_img_btn:
			v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click));
			v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click2));
			Intent menuIntent = new Intent(getApplicationContext(), MenuActivity.class);
			startActivity(menuIntent);
			finish();
			overridePendingTransition(R.anim.slide_in_from_buttom, R.anim.slide_out_to_top);
			break;
			
		case R.id.groupreservation_refresh_btn:
			v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click));
			v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click2));
			updateDateText();
		}
	} // click 이벤트 처리 끝
// ============================================================= 프래그먼트 어뎁터 ================================================================== //
	public class MyPagerAdapter extends FragmentPagerAdapter {
		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// agr0 position
			// tabTitle 배열의 arg0번째 문자열과, 순서 그 자체를 보내줌
			return GroupReservationFragment.create(position, dateYear, groupName, student_name);
		}

		// 탭과 페이지의 숫자. 탭 숫자에 맞춰 페이지를 생성해야 하므로 탭타이들의 길이 리턴
		@Override
		public int getCount() {
			return tabTitle.length;
		}

		// 페이지 제목을 보내주는 함수
		// Override/implement Methode 메뉴에서 PagerAdapter 항목에서 찾을 수 있다.
		// 페이지 제목을 요구하므로 탭타이틀 배열에서 얻어가게 하면 됨
		public CharSequence getPageTitle(int position) {
			return tabTitle[position];
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			super.destroyItem(container, position, object);

			FragmentManager manager = ((Fragment) object).getFragmentManager();
			FragmentTransaction trans = manager.beginTransaction();
			trans.remove((Fragment) object);
			trans.commit();
		}

	}
	
/*	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// 뷰의 id 식별, 키보드의 완료 키 입력 검출
		if(v.getId() == R.id.groupReservation_Autotv&& actionId == EditorInfo.IME_ACTION_SEARCH){
		}
		return false;}*/
	
	public class AutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
	    ArrayList<String> Original_CircleName;
	    ArrayList<String> filtered_CircleName = new ArrayList<String>();
	    
	    public AutoCompleteAdapter(Context context, int textViewResourceId, ArrayList<String> str) {
	        super(context, textViewResourceId);
	        Original_CircleName = str;
	    }

	    @Override
	    public int getCount() {
	    	if(filtered_CircleName != null){
	    		return filtered_CircleName.size();
	    	}
	    	return 0;
	    }

	    @Override
	    public String getItem(int index) {
	    	if(filtered_CircleName != null){
	    		return filtered_CircleName.get(index);
	    	}
	    	return null;
	    }

	    @Override
	    public Filter getFilter() {
	        Filter myFilter = new Filter() {
	            @Override
	            protected FilterResults performFiltering(CharSequence constraint) {
	                FilterResults filterResults = new FilterResults();
	                if(constraint != null ) {
	                	filtered_CircleName.clear();
	                	 for(String str : Original_CircleName){
	                         if(str.toLowerCase().contains(constraint.toString().toLowerCase())){
	                        	 filtered_CircleName.add(str);
	                         }
	                       } // Now assign the values and count to the FilterResults object
	                    filterResults.values = filtered_CircleName;
	                    filterResults.count = filtered_CircleName.size();
	                }
	                return filterResults;
	            }

	            @Override
	            protected void publishResults(CharSequence contraint, FilterResults results) {	
	            	if( results.count > 0) {	
	                notifyDataSetChanged();
	                }
	                else {
	                    notifyDataSetInvalidated();
	                }
	            }
	        };
	        return myFilter;
	    }
	}
	
	public void onBackPressed() {

	    // 여기에 코드 입력
		
		finish();
		overridePendingTransition(R.anim.slide_in_from_buttom, R.anim.slide_out_to_top);

	}

///////////////////////////////// 필터 ////////////////////////////////////////
	public class GetGroupNameThread extends Thread{
		public void run(){
			s_seq = LoginUtils.getSeq(GroupReservationActivity.this);
			param.put("s_seq", s_seq);
			client.post("http://oursoccer.co.kr/study/reserve/setup.php", param, new JsonHttpResponseHandler() {
						public void onSuccess(int statusCode, Header[] headers,JSONArray response) {
							for (int i = 0; i < response.length(); i++) {
								try {
									String name = response.getJSONObject(i).getString("s_circle");
									autocompletetextview.setText(name);
									Log.d("error__", "단체이름 가져옴 끝");
									
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
						@Override
						public void onFailure(int statusCode, Header[] headers,String responseString, Throwable throwable) {
							super.onFailure(statusCode, headers, responseString,throwable);
						}
					});
		}
	}
	
	//데이터를 받아오는 동안 백그라운드 작업을 수행할 클래스 AsyncTask<params, progress, result>
/*		private class ReserveSelectTask extends AsyncTask<Integer, Integer, Boolean>{
		   @Override
		   protected Boolean doInBackground(Integer... params) {
			   // taskParams = {position, r_day, student_seq, end_time_index_p}
		      param.put("s_seq", params[0]);
		      client.post("http://oursoccer.co.kr/study/reserve/setup.php", param, new JsonHttpResponseHandler() {
		         @Override
		         public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
		            super.onFailure(statusCode, headers, responseString, throwable);

		         }
		         @Override
		         public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
		        	 if(response.length() == 1){
		        	 	String name;
						try {
							name = response.getJSONObject(0).getString("s_circle");
							autocompletetextview.setText(name);
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
		        	 	}
		            }
		         });
		      
		      return null;
		   } // doInBackground 끝
		   
		   @Override
		    protected void onPostExecute(Boolean result) {           
		        super.onPostExecute(result);

		    }
		} // =============== AsyncTask 끝 ================
	*/
}





