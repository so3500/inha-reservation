package com.landvibe.kian82.reserve;

import java.util.Arrays;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inhareservation.R;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.landvibe.kian82.common.MenuActivity;
import com.landvibe.kian82.common.RangeSeekBar;
import com.landvibe.kian82.login.LoginUtils;
import com.landvibe.kian82.schedule.ScheduleActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ReserveFormActivity extends Activity implements OnClickListener, OnItemSelectedListener, OnTouchListener, OnFocusChangeListener {
   final String tag = "log_";
   PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
   
   public boolean[] startTimeAble = new boolean[85];
   public boolean[] endTimeAble = new boolean[85];
   public boolean[] tempEndTimeAble = new boolean[85];
   String[] startTimeStr = {"선택하세요", "08:00", "08:10", "08:20", "08:30", "08:40", "08:50", "09:00", "09:10", "09:20", "09:30", "09:40", "09:50", "10:00", "10:10", "10:20", "10:30", "10:40", "10:50", "11:00", "11:10", "11:20", "11:30", "11:40", "11:50", "12:00", "12:10", "12:20", "12:30", "12:40", "12:50", "13:00", "13:10", "13:20", "13:30", "13:40", "13:50", "14:00", "14:10", "14:20", "14:30", "14:40", "14:50", "15:00", "15:10", "15:20", "15:30", "15:40", "15:50", "16:00", "16:10", "16:20", "16:30", "16:40", "16:50", "17:00", "17:10", "17:20", "17:30", "17:40", "17:50", "18:00", "18:10", "18:20", "18:30", "18:40", "18:50", "19:00", "19:10", "19:20", "19:30", "19:40", "19:50", "20:00", "20:10", "20:20", "20:30", "20:40", "20:50", "21:00", "21:10", "21:20", "21:30", "21:40", "21:50"};
   String[] endTimeStr = {"선택하세요", "08:09", "08:19", "08:29", "08:39", "08:49", "08:59", "09:09", "09:19", "09:29", "09:39", "09:49", "09:59", "10:09", "10:19", "10:29", "10:39", "10:49", "10:59", "11:09", "11:19", "11:29", "11:39", "11:49", "11:59", "12:09", "12:19", "12:29", "12:39", "12:49", "12:59", "13:09", "13:19", "13:29", "13:39", "13:49", "13:59", "14:09", "14:19", "14:29", "14:39", "14:49", "14:59", "15:09", "15:19", "15:29", "15:39", "15:49", "15:59", "16:09", "16:19", "16:29", "16:39", "16:49", "16:59", "17:09", "17:19", "17:29", "17:39", "17:49", "17:59", "18:09", "18:19", "18:29", "18:39", "18:49", "18:59", "19:09", "19:19", "19:29", "19:39", "19:49", "19:59", "20:09", "20:19", "20:29", "20:39", "20:49", "20:59", "21:09", "21:19", "21:29", "21:39", "21:49", "21:59"};
   
   String URL;
   AsyncHttpClient client = new AsyncHttpClient();
   AsyncHttpClient defaultClient = new AsyncHttpClient();
   AsyncHttpClient circleClient = new AsyncHttpClient();
   RequestParams params = new RequestParams();
   
   int s_seq, f_id;
   String r_day, day = "";

   JSONObject obj;
   JSONArray arr;

   ViewGroup layout;
   Spinner startTimeSpinner = null, endTimeSpinner = null, bigCategorySpinner = null, midCategorySpinner = null, smallCategorySpinner = null, emailDomainSpinner = null, airSpinner = null;
   Button requestButton, cancelButton;
   TextView facilityTextView, dateTextView, timeTextView,timeTextView2, categoryTextView, circleTextView, phoneTextView, emailTextView, eventTextView, numOfPpTextView, airTextView;
   EditText circleEditText, phoneEditText, emailEditText, eventEditText, numOfPpEditText, toolEditText, rentalEditText;
   ImageButton homeButton;
   
   String[] f_name = {"", "대운동장", "다목적구장1", "다목적구장2", "농구장1", "농구장2", "농구장3", "농구장4", "학생회관 회의실", "학생회관 5층", "학생회관 6층", "학생회관 가무연습실1", "학생회관 가무연습실2", "학생회관 가무연습실3", "본관대강당", "본관중강당", "본관소강당", "하이테크강당", "5남소강당", "보미프라자"};
   String bigCategoryStr, midCategoryStr, smallCategoryStr, circleStr = "", phoneStr, emailStr, emailDomainStr, eventStr, numOfPpStr, airStr, toolStr, rentalStr;
   int bigCategoryIndex = 0, midCategoryIndex = 0, smallCategoryIndex = 0, emailDomainIndex;
   int startTime, endTime;
   int start, end;
   boolean isThereError = false, isNumberValid = true;

   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_reserve_form);
      
      s_seq = LoginUtils.getSeq(getApplicationContext());
      Intent intent = getIntent();
      r_day = intent.getStringExtra("r_day");
      f_id = intent.getIntExtra("f_id", -1);
      
      day += r_day.substring(0, 4);
      day += " - ";
      day += r_day.substring(5, 7);
      day += " - ";
      day += r_day.substring(8, 10);
      
      Arrays.fill(startTimeAble, true);
      startTimeAble[0] = false;
      Arrays.fill(endTimeAble, true);
      endTimeAble[0] = false;
      
      facilityTextView = (TextView) findViewById(R.id.facilityTextView);
      facilityTextView.setText(f_name[f_id]);
      
      dateTextView = (TextView) findViewById(R.id.dateTextView);
      dateTextView.setText(day);
      dateTextView.setPaintFlags(dateTextView.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
      
      timeTextView = (TextView) findViewById(R.id.starttimeTextView);
      final SpannableStringBuilder timeSpannable = new SpannableStringBuilder("시작시간*");
      timeSpannable.setSpan(new ForegroundColorSpan(Color.RED), 4, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      timeTextView.append(timeSpannable);
      
      timeTextView2 = (TextView) findViewById(R.id.endtimeTextView);
      final SpannableStringBuilder timeSpannable2 = new SpannableStringBuilder("종료시간*");
      timeSpannable2.setSpan(new ForegroundColorSpan(Color.RED), 4, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      timeTextView2.append(timeSpannable2);
      
      categoryTextView = (TextView) findViewById(R.id.categoryTextView);
      final SpannableStringBuilder categorySpannable = new SpannableStringBuilder("사용단체*");
      categorySpannable.setSpan(new ForegroundColorSpan(Color.RED), 4, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      categoryTextView.append(categorySpannable);

      circleTextView = (TextView) findViewById(R.id.circleTextView);
      final SpannableStringBuilder circleSpannable = new SpannableStringBuilder("단체명*");
      circleSpannable.setSpan(new ForegroundColorSpan(Color.RED), 3, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      circleTextView.append(circleSpannable);

      phoneTextView = (TextView) findViewById(R.id.phoneTextView);
      final SpannableStringBuilder phoneSpannable = new SpannableStringBuilder("연락처*");
      phoneSpannable.setSpan(new ForegroundColorSpan(Color.RED), 3, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      phoneTextView.append(phoneSpannable);

      emailTextView = (TextView) findViewById(R.id.emailTextView);
      final SpannableStringBuilder emailSpannable = new SpannableStringBuilder("이메일*");
      emailSpannable.setSpan(new ForegroundColorSpan(Color.RED), 3, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      emailTextView.append(emailSpannable);

      eventTextView = (TextView) findViewById(R.id.eventTextView);
      final SpannableStringBuilder eventSpannable = new SpannableStringBuilder("행사명*");
      eventSpannable.setSpan(new ForegroundColorSpan(Color.RED), 3, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      eventTextView.append(eventSpannable);

      numOfPpTextView = (TextView) findViewById(R.id.peopleTextView);
      final SpannableStringBuilder numOfPpSpannable = new SpannableStringBuilder("행사인원*");
      numOfPpSpannable.setSpan(new ForegroundColorSpan(Color.RED), 4, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      numOfPpTextView.append(numOfPpSpannable);
      
      airTextView = (TextView) findViewById(R.id.airTextView);
      final SpannableStringBuilder airSpannable = new SpannableStringBuilder("냉/난방*");
      airSpannable.setSpan(new ForegroundColorSpan(Color.RED), 4, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      airTextView.append(airSpannable);      
      
      layout = (ViewGroup) findViewById(R.id.seekBarLayout);
      
      URL = "http://oursoccer.co.kr/study/reserve/getIndex.php";
      params.put("r_day", r_day);
      params.put("f_id", f_id);

      client.get(URL, params, new JsonHttpResponseHandler() {
         @Override
         public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            try {
               RangeSeekBar<Integer> defaultSeekBar = new RangeSeekBar<Integer>(getApplicationContext());
               defaultSeekBar.setRangeValues(0, 100);
               defaultSeekBar.setSelectedMinValue(0);
               defaultSeekBar.setSelectedMaxValue(0);
               layout.addView(defaultSeekBar);
               arr = response;
               
               for (int i = 0; i < response.length(); i++) {
                  Log.d("error", response.toString());
                  obj = response.getJSONObject(i);
                  start = obj.getInt("start_time_index");
                  end = obj.getInt("end_time_index");
                  for (int j = start; j < end; j++)
                  {
                     startTimeAble[j + 1] = false;
                     endTimeAble[j + 1] = false;
                  }
                  //endTimeAble[end + 1] = false;

                  RangeSeekBar<Integer> seekBar = new RangeSeekBar<Integer>(getApplicationContext(),false);
                  seekBar.setRangeValues(0, 100);
                  seekBar.setSelectedMinValue(obj.getInt("start_time_index")+8);
                  seekBar.setSelectedMaxValue(obj.getInt("end_time_index")+8);
                  layout.addView(seekBar);
               }
            } catch (JSONException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
         }
         @Override
         public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            // TODO Auto-generated method stub
            super.onFailure(statusCode, headers, responseString, throwable);
         
            Log.i(tag, Integer.toString(statusCode));
         }
      });
      
      URL = "http://oursoccer.co.kr/study/reserve/getData.php";
      params.put("s_seq", s_seq);

      defaultClient.post(URL, params, new JsonHttpResponseHandler() {
         @Override
         public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            // TODO Auto-generated method stub
            super.onSuccess(statusCode, headers, response);
            try {
               phoneStr = response.getString("phone");
               emailStr = response.getString("email");
               emailDomainIndex = response.getInt("emailDomain");
               
               phoneEditText.setText(phoneStr);
               emailEditText.setText(emailStr);
               emailDomainSpinner.setSelection(emailDomainIndex);
            } catch (JSONException e) {
               // TODO Auto-generated catch block
               Toast.makeText(ReserveFormActivity.this, Integer.toString(statusCode), Toast.LENGTH_LONG).show();
               e.printStackTrace();
            }
         }
         @Override
         public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            // TODO Auto-generated method stub
            super.onFailure(statusCode, headers, responseString, throwable);
            Toast.makeText(ReserveFormActivity.this, responseString, Toast.LENGTH_LONG).show();
         }
      });
      
      URL = "http://oursoccer.co.kr/study/reserve/setup.php";
      params.put("s_seq", s_seq);

      circleClient.post(URL, params, new JsonHttpResponseHandler() {
         @Override
         public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            // TODO Auto-generated method stub
            super.onSuccess(statusCode, headers, response);
            
            JSONObject obj;
            try {
               if (response.length() > 0)
               {
                  obj = response.getJSONObject(0);
                  bigCategoryIndex = obj.getInt("s_bigcategory");
                  midCategoryIndex = obj.getInt("s_midcategory");
                  smallCategoryIndex = obj.getInt("s_smallcategory");
                  circleStr = obj.getString("s_circle");
                  
                  bigCategorySpinner.setSelection(bigCategoryIndex);
               }
            } catch (JSONException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
         }
         @Override
         public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            // TODO Auto-generated method stub
            super.onFailure(statusCode, headers, responseString, throwable);
            Toast.makeText(ReserveFormActivity.this, "fail", Toast.LENGTH_LONG).show();
         }
      });
      

      startTimeSpinner = (Spinner) findViewById(R.id.startTime);
      endTimeSpinner = (Spinner) findViewById(R.id.endTime);
      bigCategorySpinner = (Spinner) findViewById(R.id.bigCategory);
      midCategorySpinner = (Spinner) findViewById(R.id.midCategory);
      smallCategorySpinner = (Spinner) findViewById(R.id.smallCategory);
      emailDomainSpinner = (Spinner) findViewById(R.id.emailDomain);
      airSpinner = (Spinner) findViewById(R.id.air);

      ArrayAdapter<?> bigCategoryAdapter = ArrayAdapter.createFromResource(this, R.array.big_categoryList, R.layout.spinner_item);
      bigCategoryAdapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
      ArrayAdapter<?> emailAdapter = ArrayAdapter.createFromResource(this, R.array.emailList, R.layout.spinner_item);
      emailAdapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
      ArrayAdapter<?> airAdapter = ArrayAdapter.createFromResource(this, R.array.airList, R.layout.spinner_item);
      airAdapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
      
      startTimeSpinner.setFocusableInTouchMode(true);
      endTimeSpinner.setFocusableInTouchMode(true);
      
      startTimeSpinner.setAdapter(new MyAdapter(ReserveFormActivity.this, R.layout.time_spinner_item, startTimeStr, true));
      endTimeSpinner.setAdapter(new MyAdapter(ReserveFormActivity.this, R.layout.time_spinner_item, endTimeStr, false));
      
      bigCategorySpinner.setFocusableInTouchMode(true);
      bigCategorySpinner.setAdapter(bigCategoryAdapter);
      emailDomainSpinner.setFocusableInTouchMode(true);
      emailDomainSpinner.setAdapter(emailAdapter);
      airSpinner.setFocusableInTouchMode(true);
      airSpinner.setAdapter(airAdapter);
      midCategorySpinner.setFocusableInTouchMode(true);
      smallCategorySpinner.setFocusableInTouchMode(true);
      
      startTimeSpinner.setOnItemSelectedListener(this);
      endTimeSpinner.setOnItemSelectedListener(this);
      emailDomainSpinner.setOnItemSelectedListener(this);
      airSpinner.setOnItemSelectedListener(this);
      
      startTimeSpinner.setOnTouchListener(this);
      endTimeSpinner.setOnTouchListener(this);
      bigCategorySpinner.setOnTouchListener(this);
      midCategorySpinner.setOnTouchListener(this);
      smallCategorySpinner.setOnTouchListener(this);
      emailDomainSpinner.setOnTouchListener(this);
      airSpinner.setOnTouchListener(this);
      
      startTimeSpinner.setOnFocusChangeListener(this);
      endTimeSpinner.setOnFocusChangeListener(this);
      bigCategorySpinner.setOnFocusChangeListener(this);
      midCategorySpinner.setOnFocusChangeListener(this);
      smallCategorySpinner.setOnFocusChangeListener(this);
      emailDomainSpinner.setOnFocusChangeListener(this);
      airSpinner.setOnFocusChangeListener(this);
      
      circleEditText = (EditText) findViewById(R.id.circle);
      phoneEditText = (EditText) findViewById(R.id.phone);
      emailEditText = (EditText) findViewById(R.id.email);
      eventEditText = (EditText) findViewById(R.id.event);
      numOfPpEditText = (EditText) findViewById(R.id.people);
      toolEditText = (EditText) findViewById(R.id.tool);
      rentalEditText = (EditText) findViewById(R.id.rental);

      circleEditText.setOnFocusChangeListener(this);
      phoneEditText.setOnFocusChangeListener(this);
      emailEditText.setOnFocusChangeListener(this);
      eventEditText.setOnFocusChangeListener(this);
      numOfPpEditText.setOnFocusChangeListener(this);
      toolEditText.setOnFocusChangeListener(this);
      
      
      
      emailEditText.setNextFocusDownId(R.id.emailDomain);
      numOfPpEditText.setNextFocusDownId(R.id.tool);
      
      homeButton = (ImageButton) findViewById(R.id.login_home_img_btn);
      homeButton.setOnClickListener(this);
      
      requestButton = (Button) findViewById(R.id.reqBtn);
      cancelButton = (Button) findViewById(R.id.cancelBtn);
      requestButton.setOnClickListener(this);
      cancelButton.setOnClickListener(this);
      
      airSpinner.setSelection(1);

      startTimeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
    	  
    	  @Override
    	public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
    			long arg3) {
    		// TODO Auto-generated method stub
    		  
    		  if (pos != 0)
    		  {
        		  tempEndTimeAble = endTimeAble.clone();
        		  for (int i = 0; i < pos; i++)
        		  {
        			  tempEndTimeAble[i] = false;
        		  }
        	      for (int i = pos + 12; i < 85; i++)
        	      {
        	    	  tempEndTimeAble[i] = false;
        	      }
        	      endTimeSpinner.setAdapter(new MyAdapter(ReserveFormActivity.this, R.layout.time_spinner_item, endTimeStr, false));
    		  }
    		  else
    		  {
        		  tempEndTimeAble = endTimeAble.clone();
    		  }
    	}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}
      });
      
      
      bigCategorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

         @Override
         public void onItemSelected(AdapterView<?> arg0, View arg1, final int bigPosition, long arg3) {
            midCategorySpinner.setSelection(0);
            smallCategorySpinner.setSelection(0);
            if (1 <= bigPosition && bigPosition <= 2) {
               circleEditText.setText("");
            }
            final int base = R.array.big_categoryList;
            final int[] offset = { 0, 1, 12, 15, 27, 40, 44 };
            if (bigPosition != 0) {
               ArrayAdapter<?> midCategoryAdapter = ArrayAdapter.createFromResource(ReserveFormActivity.this, base + offset[bigPosition], R.layout.spinner_item);
               midCategoryAdapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
               midCategorySpinner.setAdapter(midCategoryAdapter);
               midCategorySpinner.requestFocus();
               midCategorySpinner.setSelection(midCategoryIndex);
               midCategoryIndex = 0;   
               
               midCategorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

                  @Override
                  public void onItemSelected(AdapterView<?> arg0, View arg1, int midPosition, long arg3) {
                     circleEditText.setText("");
                     // TODO Auto-generated method stub
                     if (midPosition != 0) {
                        ArrayAdapter<?> smallCategoryAdapter = ArrayAdapter.createFromResource(ReserveFormActivity.this, base + offset[bigPosition] + midPosition, R.layout.spinner_item);
                        smallCategoryAdapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
                        smallCategorySpinner.setAdapter(smallCategoryAdapter);
                        smallCategorySpinner.requestFocus();
                        smallCategorySpinner.setSelection(smallCategoryIndex);
                        smallCategoryIndex = 0;   
                        
                        smallCategorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

                           @Override
                           public void onItemSelected(AdapterView<?> arg0, View arg1, int smallPosition, long arg3) {
                              // TODO Auto-generated method stub
                              circleEditText.setText(circleStr);
                              circleStr = "";
                              if (1 <= bigPosition && bigPosition <= 2 && smallPosition != 0) {
                                 circleStr = smallCategorySpinner.getSelectedItem().toString();
                                 circleEditText.setText(circleStr);
                                 eventEditText.requestFocus();
                              }
                              else if (bigPosition > 2 && smallPosition != 0)
                              {
                                 circleEditText.requestFocus();
                              }
                              else if (smallPosition == 0)
                              {
                                 circleStr = "";
                                 circleEditText.setText(circleStr);
                              }
                           }

                           @Override
                           public void onNothingSelected(AdapterView<?> arg0) {
                              // TODO Auto-generated method stub
                           }
                        });
                     }
                     else
                     {
                        ArrayAdapter<?> smallCategoryAdapter = ArrayAdapter.createFromResource(ReserveFormActivity.this, R.array.empty, R.layout.spinner_item);
                        smallCategoryAdapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
                        smallCategorySpinner.setAdapter(smallCategoryAdapter);
                     }
                  }

                  @Override
                  public void onNothingSelected(AdapterView<?> arg0) {
                     // TODO Auto-generated method stub

                  }
               });
            }
            else
            {
               ArrayAdapter<?> midCategoryAdapter = ArrayAdapter.createFromResource(ReserveFormActivity.this, R.array.empty, R.layout.spinner_item);
               midCategoryAdapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
               midCategorySpinner.setAdapter(midCategoryAdapter);

               ArrayAdapter<?> smallCategoryAdapter = ArrayAdapter.createFromResource(ReserveFormActivity.this, R.array.empty, R.layout.spinner_item);
               smallCategoryAdapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
               smallCategorySpinner.setAdapter(smallCategoryAdapter);
            }
         }

         @Override
         public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

         }
      });
      
            
      TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        
      String myPhoneNum = mTelephonyMgr.getLine1Number();

      phoneEditText.setInputType(android.text.InputType.TYPE_CLASS_PHONE);
      phoneEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
   }

   
   @Override
   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      // TODO Auto-generated method stub
      Spinner spinner = (Spinner) parent;
      switch (spinner.getId())
      {
      case R.id.startTime:
         endTimeSpinner.requestFocus();
         break;
         
      case R.id.endTime:
         bigCategorySpinner.requestFocus();
         break;
         
      case R.id.emailDomain:
         eventEditText.requestFocus();
         break;
         
      case R.id.air:
//         toolEditText.requestFocus();
         break;
      }
   }
   @Override
   public void onNothingSelected(AdapterView<?> arg0) {
      // TODO Auto-generated method stub
      
   }

   
   @Override
   public boolean onTouch(View v, MotionEvent event) {
      // TODO Auto-generated method stub
      v.requestFocus();
      return false;
   }


   @Override
   public void onFocusChange(View view, boolean hasFocus) {
      // TODO Auto-generated method stub
      if (!hasFocus)
      {
         view.setBackgroundResource(R.drawable.lost_focus_border);
      }
      else if (view.getId() == R.id.tool && hasFocus)
      {
         InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
         imm.hideSoftInputFromWindow(toolEditText.getWindowToken(), 0);
      }
   }

   
   @Override
   public void onClick(View view) {
      // TODO Auto-generated method stub
      switch (view.getId()) {
      case R.id.login_home_img_btn: 
         view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click));
         view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click2));
         Intent myIntent = new Intent(ReserveFormActivity.this, MenuActivity.class);
         startActivity(myIntent);
         overridePendingTransition(R.anim.slide_in_from_top, R.anim.slide_out_to_buttom);
         break;
   
      case R.id.reqBtn:
         isThereError = false;
         isNumberValid = true;
         final AlertDialog.Builder builder = new AlertDialog.Builder(this);
         startTime = startTimeSpinner.getSelectedItemPosition();
         endTime = endTimeSpinner.getSelectedItemPosition();
         startTime--;
         bigCategoryStr = bigCategorySpinner.getSelectedItem().toString();
         emailDomainStr = emailDomainSpinner.getSelectedItem().toString();
         airStr = airSpinner.getSelectedItem().toString();

         circleStr = circleEditText.getText().toString();
         phoneStr = phoneEditText.getText().toString();
         emailStr = emailEditText.getText().toString();
         eventStr = eventEditText.getText().toString();
         numOfPpStr = numOfPpEditText.getText().toString();
         toolStr = toolEditText.getText().toString();
         rentalStr = rentalEditText.getText().toString();
         

         try {
            if (!phoneNumberUtil.isValidNumber(phoneNumberUtil.parse(phoneStr, "KR")))
            {
               isNumberValid = false;
            }
         } catch (NumberParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         
         
         // check other condition. If is it, then set flag to true
         if (startTimeSpinner.getSelectedItemPosition() == 0)
         {
            startTimeSpinner.setBackgroundResource(R.drawable.focus_border);
            startTimeSpinner.requestFocus();
            builder.setMessage("시작시간을 선택하세요.");
            isThereError = true;
         }
         else if (endTimeSpinner.getSelectedItemPosition() == 0)
         {
            endTimeSpinner.setBackgroundResource(R.drawable.focus_border);
            endTimeSpinner.requestFocus();
            builder.setMessage("종료시간을 선택하세요.");
            isThereError = true;
         }
         else if (startTime >= endTime) {
            startTimeSpinner.setBackgroundResource(R.drawable.focus_border);
            startTimeSpinner.requestFocus();
            builder.setMessage("시작시간이 종료시간 이후입니다.");
            isThereError = true;
         }
////////////////////////////////////////////////////////////////////////////////////////         
         else if (endTime - startTime > 12) {
            startTimeSpinner.setBackgroundResource(R.drawable.focus_border);
            startTimeSpinner.requestFocus();
            builder.setMessage("신청시간이 2시간 이상입니다.");
            isThereError = true;
         }
////////////////////////////////////////////////////////////////////////////////////////
//         else if (endTime > 60) {
//            endTimeSpinner.setBackgroundResource(R.drawable.focus_border);
//            endTimeSpinner.requestFocus();
//            builder.setMessage("종료시간이 18시 이후입니다.");
//            isThereError = true;
//         }
////////////////////////////////////////////////////////////////////////////////////////         
         else if (bigCategorySpinner.getSelectedItemPosition() == 0)
         {
            bigCategorySpinner.setBackgroundResource(R.drawable.focus_border);
            bigCategorySpinner.requestFocus();
            builder.setMessage("사용단체 대분류를 선택하세요.");
            isThereError = true;
         }
         else if (midCategorySpinner.getSelectedItemPosition() == 0)
         {
            midCategorySpinner.setBackgroundResource(R.drawable.focus_border);
            midCategorySpinner.requestFocus();
            builder.setMessage("사용단체 중분류를 선택하세요.");
            isThereError = true;
         }
         else if (smallCategorySpinner.getSelectedItemPosition() == 0)
         {
            smallCategorySpinner.setBackgroundResource(R.drawable.focus_border);
            smallCategorySpinner.requestFocus();
            builder.setMessage("사용단체 소분류를 선택하세요.");
            isThereError = true;
         }
         else if (circleStr.isEmpty())
         {
            circleEditText.setBackgroundResource(R.drawable.focus_border);
            circleEditText.requestFocus();
            builder.setMessage("단체명을 입력하세요.");
            isThereError = true;
         }
         else if (phoneStr.isEmpty())
         {
            phoneEditText.setBackgroundResource(R.drawable.focus_border);
            phoneEditText.requestFocus();
            builder.setMessage("연락처를 입력하세요.");
            isThereError = true;
         }
         else if (!isNumberValid)
         {
            phoneEditText.setBackgroundResource(R.drawable.focus_border);
            phoneEditText.requestFocus();
            builder.setMessage("유효한 연락처를 입력하세요.");
            isThereError = true;
         }
         else if (emailStr.isEmpty())
         {
            emailEditText.setBackgroundResource(R.drawable.focus_border);
            emailEditText.requestFocus();
            builder.setMessage("이메일을 입력하세요.");
            isThereError = true;
         }
         else if (emailDomainSpinner.getSelectedItemPosition() == 0)
         {
            emailDomainSpinner.setBackgroundResource(R.drawable.focus_border);
            emailDomainSpinner.requestFocus();
            builder.setMessage("이메일주소를 선택하세요.");
            isThereError = true;
         }
         else if (eventStr.isEmpty())
         {
            eventEditText.setBackgroundResource(R.drawable.focus_border);
            eventEditText.requestFocus();
            builder.setMessage("행사명을 입력하세요.");
            isThereError = true;
         }
         else if (numOfPpStr.isEmpty())
         {
            numOfPpEditText.setBackgroundResource(R.drawable.focus_border);
            numOfPpEditText.requestFocus();
            builder.setMessage("행사인원을 입력하세요.");
            isThereError = true;
         }
         else if (airSpinner.getSelectedItemPosition() == 0)
         {
            airSpinner.setBackgroundResource(R.drawable.focus_border);
            airSpinner.requestFocus();
            
            builder.setMessage("냉/난방을 선택하세요.");
            isThereError = true;
         }
         // check flag. If flag is true, then it means there are error so
         // deny request
         if (isThereError) {
            builder.setCancelable(true) // 뒤로 버튼 클릭시 취소 가능 설정
            .setNegativeButton("확인", new DialogInterface.OnClickListener() {
               // 취소 버튼 클릭시 설정
               public void onClick(DialogInterface dialog, int whichButton) {
                  dialog.cancel();
               }
            });

            builder.setTitle("Error Message"); // 제목 설정
            AlertDialog dialog = builder.create(); // 알림창 객체 생성
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
         } else {
            midCategoryStr = midCategorySpinner.getSelectedItem().toString();
            smallCategoryStr = smallCategorySpinner.getSelectedItem().toString();
            // permit request(insert information to DB)
            URL = "http://oursoccer.co.kr/study/reserve/insertToReserveTableByPrepare.php";
            params.put("s_seq", s_seq);
            params.put("f_id", f_id);
            params.put("r_day", r_day);
            params.put("start_time_index", startTime);
            params.put("end_time_index", endTime);
            params.put("big_category", bigCategoryStr);
            params.put("mid_category", midCategoryStr);
            params.put("small_category", smallCategoryStr);
            params.put("c_name", circleStr);
            params.put("phone", phoneStr);
            params.put("email", emailStr);
            params.put("email_domain", emailDomainStr);
            params.put("event", eventStr);
            params.put("num_of_pp", Integer.parseInt(numOfPpStr));
            params.put("use_coldhot", airStr);
            params.put("tool", toolStr);
            params.put("rental", rentalStr);

            client.post(URL, params, new JsonHttpResponseHandler() {
               @Override
               public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                  // show dialog
                  builder.setCancelable(true) // 뒤로 버튼 클릭시 취소 가능 설정
                  .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                     // 취소 버튼 클릭시 설정
                     public void onClick(DialogInterface dialog, int whichButton) {
                        Intent reqIntent = new Intent(getApplicationContext(), ScheduleActivity.class);
                        reqIntent.putExtra("r_day", r_day);
                        
                        startActivity(reqIntent);
                        ReserveCalendarActivity calendarActivity = (ReserveCalendarActivity)ReserveCalendarActivity.calendarActivity;
                        calendarActivity.finish();
                        finish();
                     }
                  });

                  builder.setTitle("신청 완료");
                  builder.setMessage("기안 신청이 성공적으로 완료되었습니다.");
                  AlertDialog dialog = builder.create(); // 알림창 객체 생성
                  dialog.setCanceledOnTouchOutside(false);
                  dialog.show();
               }
               @Override
               public void onFailure(int statusCode, Header[] headers, String responseString,
                     Throwable throwable) {
                  // TODO Auto-generated method stub
                  super.onFailure(statusCode, headers, responseString, throwable);
                  Toast.makeText(ReserveFormActivity.this, responseString, Toast.LENGTH_LONG).show();
               }
            });
         }
         break;
      case R.id.cancelBtn:
         Intent cancelIntent = new Intent(this, ReserveCalendarActivity.class);
         startActivity(cancelIntent);
         overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
         break;
      }
   }
   
   @Override
   public void onBackPressed() {
       // 뒤로가기 버튼 클릭시
      finish();
      overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);

   }
   
    public class MyAdapter extends ArrayAdapter<String>{
       String[] subs = {"사용 가능", "사용 불가"};
       int arr_images[] = {R.drawable.choice_icon, R.drawable.check_icon, R.drawable.sign_delete_icon};
       boolean isStartTimeSpinner;
       
       public MyAdapter(Context context, int textViewResourceId, String[] objects, boolean isStartTimeSpinner) {
          super(context, textViewResourceId, objects);
          this.isStartTimeSpinner = isStartTimeSpinner;
          
       }

       @Override
       public View getDropDownView(int position, View convertView,ViewGroup parent) {
          return getCustomView(position, convertView, parent);
       }

       @Override
        public boolean isEnabled(int position) {
            // TODO Auto-generated method stub
    	   
    	   if (isStartTimeSpinner)
    	   {
               if (startTimeAble[position]) {
                   return true;
               }
               return false;
    	   }
    	   else
    	   {
               if (tempEndTimeAble[position]) {
                   return true;
               }
               return false;
    	   }
       }
       
       @Override
       public View getView(int position, View convertView, ViewGroup parent) {
          return getCustomView(position, convertView, parent);
       }

       public View getCustomView(int position, View convertView, ViewGroup parent) {

          LayoutInflater inflater=getLayoutInflater();
          View row=inflater.inflate(R.layout.time_spinner_item, parent, false);
          TextView time=(TextView)row.findViewById(R.id.time);
          TextView sub=(TextView)row.findViewById(R.id.sub);
          ImageView icon=(ImageView)row.findViewById(R.id.image);

          if (isStartTimeSpinner)
          {
             time.setText(startTimeStr[position]);
             if (startTimeAble[position])
             {
                sub.setText(subs[0]);
                icon.setImageResource(arr_images[1]);
             }
             else
             {
                if (position == 0)
                {
                   icon.setImageResource(arr_images[0]);
                }
                else
                {
                   sub.setText(subs[1]);
                   icon.setImageResource(arr_images[2]);
                }
             }
          }
          else
          {
             time.setText(endTimeStr[position]);
             if (tempEndTimeAble[position])
             {
                sub.setText(subs[0]);
                icon.setImageResource(arr_images[1]);
             }
             else
             {
                if (position == 0)
                {
                   icon.setImageResource(arr_images[0]);
                }
                else
                {
                   sub.setText(subs[1]);
                   icon.setImageResource(arr_images[2]);
                }
             }      
          }

          return row;
       }
    }
}