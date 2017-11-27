package com.landvibe.kian82.my_reservation;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.inhareservation.R;
import com.landvibe.kian82.common.MenuActivity;
import com.landvibe.kian82.common.PagerSlidingTabStrip;
import com.landvibe.kian82.group_reservation.GroupReservationActivity.MyPagerAdapter;
import com.landvibe.kian82.login.LoginUtils;


public class MyReservationActivity extends FragmentActivity implements OnClickListener {

	private String[] end_time_array;
	int end_time_index;
	private String[] tabTitle = {"예약중","사용완료"};
	private static PagerSlidingTabStrip tab;
	private static ViewPager pager;
	private static PagerAdapter adpt;
   // 달력
   GregorianCalendar calendar;
   String dateYear;

   Button button;
   TextView date_tv, fragment_month_my_tv;
   static TextView myReservation_username_tv;
   DatePickerDialog myDialog;
   String currentDate;
   String currentTime;
   SimpleDateFormat dateFormat;
 
   private static int student_seq;
   private static String student_name;
   ImageButton goToMenu_btn;
   TextView userName;
   static TextView reserveNum;
   static TextView completeNum;
   static Button refreshReserveBtn;
   int tabPositionOpend;
   
   Resources resources;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_my_reservation);
      
      resources = getResources();
      end_time_array = resources.getStringArray(R.array.endTimeList_);
      
      goToMenu_btn = (ImageButton) findViewById(R.id.login_home_img_btn);
      goToMenu_btn.setOnClickListener(this);

      // 로그인한 학생의 s_seq값을 가져옴
      student_seq = LoginUtils.getSeq(this);
      student_name = LoginUtils.getName(this);
      
      userName = (TextView) findViewById(R.id.myreservation_username_tv);
      userName.setText(student_name + " 님의\n예약중 / 사용완료 현황 ");
      reserveNum = (TextView) findViewById(R.id.myreservation_reservenum_tv);
      completeNum = (TextView) findViewById(R.id.myreservation_completenum_tv);
      
      refreshReserveBtn = (Button) findViewById(R.id.myreservation_refresh_btn);
      refreshReserveBtn.setOnClickListener(this);
      
      calendar = new GregorianCalendar();
      dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()); // 현재 시스템 날짜
      currentDate = dateFormat.format(calendar.getTime());
      Log.d("error_", "dateFormat " + Integer.parseInt(currentDate));
      dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault()); // 현재 시스템 시간
      currentTime = dateFormat.format(calendar.getTime());
      
      ////////////////// 현재 시스템 시간을 end_time_index 로 얻음 /////////////////////
      int count=0;
		for(String date : end_time_array){	
			count++;
			if(date.compareTo(dateFormat.format(calendar.getTime()) ) >= 0){
				end_time_index = count;
				Log.d("error_", "count : " + count);
				break;
			}
			// 현재 시간이 21:59 이상인 경우
			if(count == end_time_array.length){
				end_time_index = count+1;
				break;
			}
		}
      ///////////////////////////////////////////////////////////////////////
      
      
      ////////////////////////////////탭 어뎁터 ////////////////////////////////
      tab = (PagerSlidingTabStrip) this.findViewById(R.id.my_reservation_tap);
      tab.setShouldExpand(true);
      pager = (ViewPager) this.findViewById(R.id.my_reservation_pager);
      adpt = new PagerAdapter(getSupportFragmentManager());
      // Adapter > ViewPager > Tap
      // 초기화 한 tab, pager을 가지고 뷰페이저에 어댑터를 맞추고
      pager.setAdapter(adpt);
      // 탭에다가 뷰페이저를 맞춰준다 순서중요!!
      tab.setViewPager(pager);
      ////////////////////////////////////////////////////////////////////////
      
      reserveNum.setText("0");
      completeNum.setText("0");
      

   }
   

   
   public static void refresh(){
	   pager.setAdapter(adpt);
	   tab.setViewPager(pager);
   }
   public static void setReserveNumTv(int reserveNumber){
	   reserveNum.setText(Integer.toString(reserveNumber));
   }
   
   public static void setCompleteNumTv(int completeNumber){
	   completeNum.setText(Integer.toString(completeNumber));
   }
   
   

   // 클릭 이벤트 처리 메소드
   @Override
   public void onClick(View v) {

      switch (v.getId()) {
      
      case R.id.login_home_img_btn:
        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click));
	    v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click2));
		Intent menuIntent = new Intent(getApplicationContext(), MenuActivity.class);
		startActivity(menuIntent);
		finish();
		overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
		break;
		
      case R.id.myreservation_refresh_btn:
    	  tabPositionOpend = pager.getCurrentItem();
    	  v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click));
		  v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click2));
		  
		  // 버튼을 빠르게 누르면, null pointer exception 발생. 따라서 버튼을 누른 뒤 1초간 간격을 준 뒤 버튼 활성화
          refreshReserveBtn.setEnabled(false);
          refreshReserveBtn.postDelayed(new Runnable() {
              @Override
              public void run() {
            	  refreshReserveBtn.setEnabled(true);
              }
          }, 1000);
		  PagerAdapter adpt = new PagerAdapter(getSupportFragmentManager());
    	  pager.setAdapter(adpt);
          tab.setViewPager(pager);
    	  pager.setCurrentItem(tabPositionOpend);
	
      }
   } // click 이벤트 처리 끝

// ============================================================= 프래그먼트 어뎁터 ============================================================= //
	class PagerAdapter extends FragmentPagerAdapter {
		public PagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// agr0 position
			// tabTitle 배열의 arg0번째 문자열과, 순서 그 자체를 보내줌
			// , dateYear, groupName, student_seq, student_name
			return MyReservationFragment.create(position, student_seq, Integer.parseInt(currentDate), end_time_index);
			
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
	public void onBackPressed() {

	    // 여기에 코드 입력
		
		finish();
		overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);

	}

}