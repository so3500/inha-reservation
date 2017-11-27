package com.landvibe.kian82.reserve;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inhareservation.R;
import com.landvibe.kian82.common.MenuActivity;
import com.landvibe.kian82.common.RangeSeekBar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ReserveCalendarActivity extends FragmentActivity implements
		OnClickListener, OnItemSelectedListener {

	private String URL = "http://oursoccer.co.kr/study/reserve/positiwee_select_time.php";
	private static JSONArray jsonarray = new JSONArray();
	AsyncHttpClient clientForIndex = new AsyncHttpClient();
	RequestParams paramsForIndex = new RequestParams();
	JSONObject obj;
	JSONArray arr;
	int checkspinner = 1;
	String checkdate = null;
	String tempdate = null;
	int id = 0;
	int precount = 0;
	LinearLayout templayout = null;
	String fac_name = null;
	private static final String tag = "MyCalendarActivity";
	private AlertDialog reserve_dialog = null;
	private AlertDialog impossible_dialog = null;
	private ImageButton home;

	ArrayList<String> arraylist;
	Button reserve_cancle_btn;
	Button reserve_submit_btn;
	ImageView calendar_choice_fac_btn;
	private LinearLayout calendar_main;
	private LinearLayout layout_timeslot;
	private TextView reserve_currentmonth_tv;
	private ImageView reserve_prevmonth_iv;
	private ImageView reserve_nextmonth_iv;
	private GridView calendarView;
	private GridCellAdapter adapter;
	private Calendar _calendar;
	Button prevbtn = null;
	@SuppressLint("NewApi")
	private int month, year;
	@SuppressWarnings("unused")
	@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi" })
	private final DateFormat dateFormatter = new DateFormat();
	private static final String dateTemplate = "MMMM yyyy";
	private static final String Template = "yyyyMMdd";
	public static Activity calendarActivity;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reserve_calendar);
		// layout = (ViewGroup) findViewById(R.id.seekBarLayout1);
		// ////장소 선택 스피너 ///

		calendarActivity = ReserveCalendarActivity.this;

		arraylist = new ArrayList<String>();

		arraylist.add("축구장");
		arraylist.add("농구장");
		arraylist.add("학생회관");
		arraylist.add("본관");
		arraylist.add("기타");

		ArrayAdapter<String> spinneradapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_activated_1, arraylist);
		Spinner sp = (Spinner) this.findViewById(R.id.reserve_place_spin);

		sp.setSelection(0);
	
		sp.setAdapter(spinneradapter);
		sp.setOnItemSelectedListener(this);
		// //////////////////////////////////////////////////////////////////

		// ////////맨아래 신청 취소 버튼////////////////////////////
		// reserve_submit_btn = (Button) findViewById(R.id.reserve_submit_btn);
		// reserve_cancle_btn = (Button) findViewById(R.id.reserve_cancle_btn);
		// reserve_submit_btn.setOnClickListener(this);
		// reserve_cancle_btn.setOnClickListener(this);
		// ///////////////////////////////////////////////

		// ///////////달력부분////////////////////////
		_calendar = Calendar.getInstance(Locale.getDefault()); // getInstance
		// 시간정보를 표현하는
		// 메소드
		month = _calendar.get(Calendar.MONTH) + 1; // calendar.get(Calendar.**)
		// = 현재 값을 리턴 (AP_PM)
		year = _calendar.get(Calendar.YEAR);
		Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " + "Year: "
				+ year);
		// facilityTextView1 = (TextView) this
		// .findViewById(R.id.facilityTextView1);

		home = (ImageButton) this.findViewById(R.id.login_home_img_btn);
		home.setOnClickListener(this);

		reserve_prevmonth_iv = (ImageView) this.findViewById(R.id.reserve_prevmonth_iv);
		reserve_prevmonth_iv.setOnClickListener(this);

		reserve_currentmonth_tv = (TextView) this.findViewById(R.id.reserve_currentmonth_tv);
		reserve_currentmonth_tv.setText(DateFormat.format(dateTemplate,
				_calendar.getTime())); // DateFormat은
		// 일자나
		// 시간을
		// 문자열로
		// 바꾸는
		// 기능 제공

		reserve_nextmonth_iv = (ImageView) this
				.findViewById(R.id.reserve_nextmonth_iv);
		reserve_nextmonth_iv.setOnClickListener(this);

		calendarView = (GridView) this.findViewById(R.id.reserve_calendar);

		// Initialised
		adapter = new GridCellAdapter(getApplicationContext(),
				R.id.calendar_day_gridcell, month, year);
		adapter.notifyDataSetChanged(); // 데이터가 변경되면 호출해서 새로운 데이터가 나오도록 한다,
		calendarView.setAdapter(adapter);
		// ////////////////////////////////////////////////////////////////////

	}

	/**
	 * 84 * 85 * @param month 86 * @param year 87
	 */
	private void setGridCellAdapterToDate(int month, int year) {
		adapter = new GridCellAdapter(getApplicationContext(),
				R.id.calendar_day_gridcell, month, year); // 초기화
		_calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
		reserve_currentmonth_tv.setText(DateFormat.format(dateTemplate,
				_calendar.getTime()));
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		if (v == reserve_prevmonth_iv) {
			v.startAnimation(AnimationUtils.loadAnimation(this,
					R.anim.button_click));
			v.startAnimation(AnimationUtils.loadAnimation(this,
					R.anim.button_click2));
			if (month <= 1) {
				month = 12;
				year--;
			} else {
				month--;
			}
			Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: "
					+ month + " Year: " + year);
			setGridCellAdapterToDate(month, year);
		}
		if (v == reserve_nextmonth_iv) {
			v.startAnimation(AnimationUtils.loadAnimation(this,
					R.anim.button_click));
			v.startAnimation(AnimationUtils.loadAnimation(this,
					R.anim.button_click2));
			if (month > 11) {
				month = 1;
				year++;
			} else {
				month++;
			}
			Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: "
					+ month + " Year: " + year);
			setGridCellAdapterToDate(month, year);
		}
		if (v == home) {
			v.startAnimation(AnimationUtils.loadAnimation(this,
					R.anim.button_click));
			v.startAnimation(AnimationUtils.loadAnimation(this,
					R.anim.button_click2));
			Intent myIntent = new Intent(ReserveCalendarActivity.this,
					MenuActivity.class);
			startActivity(myIntent);
			overridePendingTransition(R.anim.slide_in_from_top,
					R.anim.slide_out_to_buttom);

		}

	}

	private AlertDialog no_dialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setMessage(" 신청 기간이 아닙니다\n 예약은 3~30일만 가능합니다.");
		dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				impossible_dialog.dismiss();
			}
		});
		return dialog.create();
	}

	// /////////확인 다이얼로그 처리/////////////////
	@TargetApi(Build.VERSION_CODES.ECLAIR)
	private AlertDialog createrDialog(final int facility_id) {
		final View innerView = getLayoutInflater().inflate(
				R.layout.reserve_dialog, null);
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle("예약자확인사항");
		TextView title = new TextView(this);
		title.setText("예약 확인사항");
		title.setBackgroundColor(Color.DKGRAY);
		title.setPadding(20, 20, 20, 20);
		title.setGravity(Gravity.CENTER);
		title.setTextColor(Color.WHITE);
		title.setTextSize(20);
		ab.setCustomTitle(title);
		ab.setView(innerView);
		ab.setCancelable(false);

		ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {

				Intent myIntent = new Intent(ReserveCalendarActivity.this,
						ReserveFormActivity.class);
				myIntent.putExtra("r_day", checkdate);
				myIntent.putExtra("f_id", facility_id);
				startActivity(myIntent);

				overridePendingTransition(R.anim.slide_in_from_right,
						R.anim.slide_out_to_left);
			}
		});

		ab.setNeutralButton("상세보기", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int whichButton) {

				Intent myIntent = new Intent(Intent.ACTION_VIEW);
				Uri u = Uri
						.parse("https://ins.inha.ac.kr/ITIS/ADM/SS/SS_04002/Caution.htm");
				myIntent.setData(u);

				startActivity(myIntent);

			}

		});

		ab.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				reserve_dialog.dismiss();

			}

		});

		return ab.create();
	}

	// ///////////////////////////////////////////////////

	// ////////달력 처리///////////////////////////////
	@Override
	public void onDestroy() {
		Log.d(tag, "Destroying View ...");
		super.onDestroy();
	}

	
	// Inner Class
	public class GridCellAdapter extends BaseAdapter implements OnClickListener {
		private static final String tag = "GridCellAdapter";
		private final Context _context;

		private final List<String> list;
		private static final int DAY_OFFSET = 1;
		private final String[] weekdays = new String[] { "Sun", "Mon", "Tue",
				"Wed", "Thu", "Fri", "Sat" };
		private final String[] months = { "1", "2", "3", "4", "5", "6", "7",
				"8", "9", "10", "11", "12" };
		private final int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
				31, 30, 31 };
		private int daysInMonth; // daysOfMonth를 받는다
		private int currentDayOfMonth;
		private int currentWeekDay;
		private Button gridcell;

		private Button prevgridcell = null;

		private TextView num_events_per_day;
		private final HashMap<String, Integer> eventsPerMonthMap;
		private final SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"dd-MMM-yyyy"); // 형식

		// Days in Current Month
		public GridCellAdapter(Context context, int textViewResourceId,
				int month, int year) {
			super();
			this._context = context;
			this.list = new ArrayList<String>();
			Log.d(tag, "==> Passed in Date FOR Month: " + month + " "
					+ "Year: " + year);
			Calendar calendar = Calendar.getInstance(); // calendar 객체에 정보 불러옴
			setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH)); // 현재 월의
			// 날짜
			// set

			setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK)); // 현재 요일 set
			// /
			Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
			Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
			Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

			// Print Month
			printMonth(month, year);

			// Find Number of Events
			eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
		}

		private String getMonthAsString(int i) {
			return months[i];
		}

		private String getWeekDayAsString(int i) {
			return weekdays[i];
		}

		private int getNumberOfDaysOfMonth(int i) {
			return daysOfMonth[i];
		}

		public String getItem(int position) {
			return list.get(position);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		/**
		 * Prints Month
		 * 
		 * @param mm
		 * @param yy
		 */
		private void printMonth(int mm, int yy) {
			Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
			int trailingSpaces = 0;
			int daysInPrevMonth = 0;
			int prevMonth = 0;
			int prevYear = 0;
			int nextMonth = 0;
			int nextYear = 0;

			int currentMonth = mm - 1;
			String currentMonthName = getMonthAsString(currentMonth); // 현재 달을
			// string으로
			daysInMonth = getNumberOfDaysOfMonth(currentMonth); // 현재달의 마지막 날을
			// 받는다
			// (31,29,31,30.....)

			Log.d(tag, "Current Month: " + " " + currentMonthName + " having "
					+ daysInMonth + " days.");

			GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
			Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

			if (currentMonth == 11) { // 12월 일 때
				prevMonth = currentMonth - 1;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 0;
				prevYear = yy;
				nextYear = yy + 1;
				Log.d(tag, "*->PrevYear: " + prevYear + " prevMonth:"
						+ prevMonth + " nextMonth: " + nextMonth
						+ " NextYear: " + nextYear);
			} else if (currentMonth == 0) { // 1월 일 때
				prevMonth = 11;
				prevYear = yy - 1;
				nextYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 1;
				Log.d(tag, "**--> PrevYear: " + prevYear + " prevMonth:"
						+ prevMonth + " nextMonth: " + nextMonth
						+ " NextYear: " + nextYear);
			} else {
				prevMonth = currentMonth - 1;
				nextMonth = currentMonth + 1;
				nextYear = yy;
				prevYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				Log.d(tag, "***---> PrevYear: " + prevYear + " prevMonth:"
						+ prevMonth + " nextMonth: " + nextMonth
						+ " NextYear: " + nextYear);
			}

			// //요일 처리
			int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1; // DAY_OF_WEEK은
			// 요일,
			// sunday
			// =0,
			// monday =
			// 1....
			trailingSpaces = currentWeekDay;

			Log.d(tag, "Week Day:" + currentWeekDay + " is "
					+ getWeekDayAsString(currentWeekDay));
			Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
			Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

			if (cal.isLeapYear(cal.get(Calendar.YEAR))) // isLeapYear 윤년일 경우
				// return true
				if (mm == 2)
					++daysInMonth;
				else if (mm == 3)
					++daysInPrevMonth;

			// Trailing Month days
			for (int i = 0; i < trailingSpaces; i++) {
				Log.d(tag,
						"PREV MONTH:= "
								+ prevMonth
								+ " => "
								+ getMonthAsString(prevMonth)
								+ " "
								+ String.valueOf((daysInPrevMonth
										- trailingSpaces + DAY_OFFSET)
										+ i));
				list.add(String
						.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET)
								+ i)
						+ "-GREY"
						+ "-"
						+ getMonthAsString(prevMonth)
						+ "-"
						+ prevYear);
			}

			// Current Month Days
			for (int i = 1; i <= daysInMonth; i++) {
				Log.d(currentMonthName, String.valueOf(i) + " "
						+ getMonthAsString(currentMonth) + " " + yy);
				if (i == getCurrentDayOfMonth()) {
					list.add(String.valueOf(i) + "-BLUE" + "-" // BLUE : 오늘
							+ getMonthAsString(currentMonth) + "-" + yy);
				} else {
					list.add(String.valueOf(i) + "-WHITE" + "-" // WHITE 현재 달의
																// 일자
							+ getMonthAsString(currentMonth) + "-" + yy);
				}
			}

			// Leading Month days
			for (int i = 0; i < list.size() % 7; i++) {
				Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
				list.add(String.valueOf(i + 1) + "-GREY" + "-" // GREY : 현재달이 아닌
																// 일자 @색설정은
																// getview에서 쓰임
						+ getMonthAsString(nextMonth) + "-" + nextYear);
			}
		}

		/**
		 * 308 * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH,
		 * retrieve 309 * ALL entries from a SQLite database for that month.
		 * Iterate over the 310 * List of All entries, and get the dateCreated,
		 * which is converted into 311 * day. 312 * 313 * @param year 314 * @param
		 * month 315 * @return 316
		 */
		private HashMap<String, Integer> findNumberOfEventsPerMonth(int year,
				int month) {
			HashMap<String, Integer> map = new HashMap<String, Integer>();

			return map;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = (LayoutInflater) _context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.screen_gridcell, parent, false);
			}

			// Get a reference to the Day gridcell
			gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
			gridcell.setOnClickListener(this);

			// ACCOUNT FOR SPACING

			Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
			String[] day_color = list.get(position).split("-");
			String theday = day_color[0];
			String themonth = day_color[2];
			String theyear = day_color[3];

			Calendar cal;
			cal = Calendar.getInstance(Locale.getDefault());
			int thisday = cal.get(cal.DATE);
			int thismonth = (cal.get(cal.MONTH) + 1) * 100;
			int thisyear = (cal.get(cal.YEAR)) * 10000;
			int nowdate = thisday + thismonth + thisyear;

			int D = Integer.parseInt(day_color[0].toString());
			int M = (Integer.parseInt(day_color[2].toString())) * 100;
			int Y = (Integer.parseInt(day_color[3].toString()) * 10000);
			int choicedate = D + M + Y;

			if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
				if (eventsPerMonthMap.containsKey(theday)) {
					num_events_per_day = (TextView) row
							.findViewById(R.id.num_events_per_day); // 날짜
																	// 표시되는
					// textview
					Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
					num_events_per_day.setText(numEvents.toString());
				}
			}

			// Set the Day GridCell
			gridcell.setText(theday);
			gridcell.setTag(theyear + "-" + themonth + "-" + theday); // selected에
			// 표시되는
			// 부분
			Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-"
					+ theyear + "선택날짜 : " + choicedate + "현재날짜: " + nowdate);
			if (3 > (choicedate - nowdate) || 100 <= (choicedate - nowdate)) {

				if (day_color[1].equals("GREY")) {
					gridcell.setTextColor(getResources().getColor(
							R.color.lightgray));
				}
				if (day_color[1].equals("WHITE")) {
					gridcell.setTextColor(getResources().getColor(
							R.color.lightgray02));
				}
				if (day_color[1].equals("BLUE")) {
					if ((cal.get(cal.MONTH) + 1) == Integer
							.parseInt(day_color[2].toString()))
						gridcell.setTextColor(getResources().getColor(
								R.color.orrange));
					else
						gridcell.setTextColor(getResources().getColor(
								R.color.lightgray02));
				}
			}

			// if (3 <= (choicedate - nowdate) || 100 > (choicedate - nowdate))
			// {
			else {
				if (day_color[1].equals("GREY")) {
					gridcell.setTextColor(Color.argb(255, 52, 152, 219));
					gridcell.setTextSize(18);
					gridcell.setTypeface(null, Typeface.BOLD);
				}
				if (day_color[1].equals("WHITE")) {
					gridcell.setTextColor(Color.argb(255, 52, 152, 219));
					gridcell.setTextSize(18);
					gridcell.setTypeface(null, Typeface.BOLD);
				}
				if (day_color[1].equals("BLUE")) {
					if ((cal.get(cal.MONTH) + 1) == Integer
							.parseInt(day_color[2].toString()))
						gridcell.setTextColor(getResources().getColor(
								R.color.orrange));
					else
						gridcell.setTextColor(getResources().getColor(
								R.color.lightgray02));
				}
			}

			return row;
		}

		@Override
		public void onClick(View view) { // 달력 클릭시 이벤트 발생
			Calendar cal;
			cal = Calendar.getInstance(Locale.getDefault());
			int thisday = cal.get(cal.DATE);
			int thismonth = (cal.get(cal.MONTH) + 1) * 100;
			int thisyear = (cal.get(cal.YEAR)) * 10000;
			int nowdate = thisday + thismonth + thisyear;

			String date_month_year = (String) view.getTag();

			String[] day_color = date_month_year.split("-");
			if (Integer.parseInt(day_color[1].toString()) < 10)
				day_color[1] = "0" + day_color[1];
			if (Integer.parseInt(day_color[2].toString()) < 10)
				day_color[2] = "0" + day_color[2];

			Log.e("날짜짜짜는", date_month_year);
			checkdate = day_color[0] + "-" + day_color[1] + "-" + day_color[2];
			
			Log.e("날짜짜짜는", checkdate);
			int D = Integer.parseInt(day_color[2].toString());
			int M = (Integer.parseInt(day_color[1].toString())) * 100;
			int Y = (Integer.parseInt(day_color[0].toString()) * 10000);
			int choicedate = D + M + Y;
			// Log.e("Selected date", date_month_year);
			Log.e("선택날짜는", "" + choicedate);
			Log.e("현재날짜는", "" + nowdate);
			Log.e("선택 장소는", "" + checkspinner);

			if (3 > (choicedate - nowdate) || 100 <= (choicedate - nowdate)) {
				impossible_dialog = no_dialog();
				impossible_dialog.show();
				checkdate = tempdate;
			}

			else {
				tempdate = checkdate;
				gridcell = (Button) view
						.findViewById(R.id.calendar_day_gridcell);
				gridcell.setOnClickListener(this);
				gridcell.setSelected(true);

				if (prevgridcell == null) {
					prevgridcell = gridcell;
				} else {
					prevgridcell.setSelected(false);
				}
				prevgridcell = gridcell;

				try {
					showtime();
					Date parsedDate = dateFormatter.parse(date_month_year);
					Log.d(tag, "Parsed Date: " + parsedDate.toString());
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}

		public int getCurrentDayOfMonth() {
			return currentDayOfMonth;
		}

		private void setCurrentDayOfMonth(int currentDayOfMonth) {
			this.currentDayOfMonth = currentDayOfMonth;
		}

		public void setCurrentWeekDay(int currentWeekDay) {
			this.currentWeekDay = currentWeekDay;
		}

		public int getCurrentWeekDay() {
			return currentWeekDay;
		}
	}

	public void showtime() {
		layout_timeslot = (LinearLayout) findViewById(R.id.calendar_time_slot);
		layout_timeslot.removeView(templayout);

		String[] fname_arr = new String[19];

		int counter = 0;

		if (checkspinner == 1) {
			counter = 3;
			id = 1;
		} else if (checkspinner == 2) {
			counter = 4;
			id = 4;
		} else if (checkspinner == 3) {
			counter = 6;
			id = 8;
		} else if (checkspinner == 4) {
			counter = 3;
			id = 14;
		} else if (checkspinner == 5) {
			counter = 3;
			id = 17;
		}
		precount = counter;

		final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final LinearLayout common_layout = (LinearLayout) inflater.inflate(
				R.layout.category_common_for_calendar, null);

		for (int j = 0; j < counter; j++) {

	//		final Map<Integer, List<JSONObject>> scheduleMap = new HashMap<Integer, List<JSONObject>>();

			String URL = "http://oursoccer.co.kr/study/reserve/timeslot2.php";
			paramsForIndex.put("rday", checkdate);
			paramsForIndex.put("fcategory", checkspinner);
			clientForIndex.get(URL, paramsForIndex,
					new JsonHttpResponseHandler() {

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONArray response) {
							LinearLayout item_layout = (LinearLayout) inflater
									.inflate(
											R.layout.category_item_for_calendar,
											null);
							RelativeLayout time_bar = (RelativeLayout) item_layout
									.findViewById(R.id.category_item_schedule_layout);
							TextView textname = (TextView) item_layout
									.findViewById(R.id.category_item_f_name_text);
							calendar_choice_fac_btn = (ImageView) item_layout
									.findViewById(R.id.category_item_fac_btn);
							try {
								for (int i = 0; i < response.length(); i++) {
									Log.d("error", response.toString());
									final int fac_id = response
											.getJSONObject(i).getInt("f_id");

									if (fac_id == id) {
										calendar_choice_fac_btn.setId(fac_id);
										int start = response.getJSONObject(i)
												.getInt("start_time_index");
										int end = response.getJSONObject(i)
												.getInt("end_time_index");

										RangeSeekBar<Integer> seekBar = new RangeSeekBar<Integer>(
												getApplicationContext());
										seekBar.setRangeValues(0, 100);
										seekBar.setSelectedMinValue(start + 8);
										seekBar.setSelectedMaxValue(end + 8);
										time_bar.addView(seekBar);
										final String fac_name = response
												.getJSONObject(i).getString(
														"f_name");
										textname.setText(fac_name);

										calendar_choice_fac_btn
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														// TODO Auto-generated
														// method
														// stub
														v.startAnimation(AnimationUtils.loadAnimation(ReserveCalendarActivity.this, R.anim.button_click));
														v.startAnimation(AnimationUtils.loadAnimation(ReserveCalendarActivity.this, R.anim.button_click2));
														reserve_dialog = createrDialog(fac_id);
														reserve_dialog.show();

													}
												});

									}
									// setContentView( Layout );

								}

								common_layout.addView(item_layout);
								id++;
							}

							catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								String responseString, Throwable throwable) {
							// TODO Auto-generated method stub
							super.onFailure(statusCode, headers,
									responseString, throwable);
							// Toast.makeText(ReserveFormActivity.this,
							// responseString,
							// Toast.LENGTH_LONG).show();
							Log.i(tag, Integer.toString(statusCode));
						}
					});

		}

		templayout = common_layout;
		layout_timeslot.addView(templayout);

	}

	// ////////////////////////////장소선택 처리 (스피너)/////////////////////////
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Spinner sp = (Spinner) this.findViewById(R.id.reserve_place_spin);

		if (sp.getSelectedItemPosition() == 0) {
			Toast.makeText(this, "축구장", Toast.LENGTH_SHORT).show();
			checkspinner = 1;
		}

		else if (sp.getSelectedItemPosition() == 1) {
			Toast.makeText(this, "농구장", Toast.LENGTH_SHORT).show();
			checkspinner = 2;
		}

		else if (sp.getSelectedItemPosition() == 2) {
			Toast.makeText(this, "학생회관", Toast.LENGTH_SHORT).show();
			checkspinner = 3;
		}

		else if (sp.getSelectedItemPosition() == 3) {
			Toast.makeText(this, "본관", Toast.LENGTH_SHORT).show();
			checkspinner = 4;
		} else if (sp.getSelectedItemPosition() == 4) {
			Toast.makeText(this, "기타", Toast.LENGTH_SHORT).show();
			checkspinner = 5;
		}

		if (checkdate != null)
			showtime();
	}

	// ////////////////////////////////////////

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		Spinner sp = (Spinner) this.findViewById(R.id.reserve_place_spin);
	}

	public void onBackPressed() {

		// 여기에 코드 입력

		finish();
		overridePendingTransition(R.anim.slide_in_from_top,
				R.anim.slide_out_to_buttom);

	}
}
