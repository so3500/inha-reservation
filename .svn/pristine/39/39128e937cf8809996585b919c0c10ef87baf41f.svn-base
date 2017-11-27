package com.landvibe.kian82.group_reservation;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.inhareservation.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

public class GroupReservationFragment extends Fragment {
	private int position;
	private String dateYear;
	private String groupName;
	private SyncHttpClient client_select = new SyncHttpClient();
	private String URL_select = "http://oursoccer.co.kr/study/reserve/Group_SelectGroupReserve.php";
	private RequestParams params_select = new RequestParams();
	private JSONArray jsonArray = null;
	private JSONObject jsonObject = null;
	private String student_name;
	private String[] dateArray = { "01", "02", "03", "04", "05", "06", "07","08", "09", "10", "11", "12" };
	public static GroupReservationAdapter adapter_groupreservation;
	public ListView listview;
	TextView noreserve_tv;
	GetJsonArrayThread getJsonArrayThread;
	Resources resources;
	

	// frgmant_Group 을 되돌려주는 함수 like 생성자
	public static GroupReservationFragment create( int position,String dateYear, String groupName,String student_name) {
		GroupReservationFragment frg_month = new GroupReservationFragment();
		Bundle b = new Bundle();
		b.putInt("position", position);
		b.putString("dateYear", dateYear);
		b.putString("groupName", groupName);
		b.putString("student_name", student_name);
		frg_month.setArguments(b);
		return frg_month;
	}

	/*
	 * class viewHolder { LinearLayout list_Layout; TextView reserve_Date;
	 * TextView facility_Name; }
	 */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.month_1); 이거 안써짐
		// 프래그먼트의 onCreate에서, create 함수에서 받아두었던 값들을 가져와서, 위에위에 만들어둔 전역 변수에 대입
		this.position = this.getArguments().getInt("position"); // 월의 index 값
		this.dateYear = this.getArguments().getString("dateYear");
		this.groupName = this.getArguments().getString("groupName");
		this.student_name = this.getArguments().getString("student_name");

		resources = getResources();
		
		String tempDateStr = dateYear + dateArray[position]; // ex) 201507
		int YearMonth = Integer.parseInt(tempDateStr);
		if(groupName == null || groupName == ""){
			groupName = "There is no groupName parameter";
		}
		params_select.put("g_name", groupName);
		params_select.put("r_day", YearMonth);		
	}

	// onCreateView를 오버라이드하는데, 뷰페이저에서 보여줄 뷰를 설정하는 것
	@Override
	public synchronized View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		final LinearLayout l_Layout = (LinearLayout) inflater.inflate(R.layout.fragment_month_group, container, false); // (resource,root)
		noreserve_tv = (TextView) l_Layout.findViewById(R.id.groupReservation_noreserve_tv);
		
		// /////////////////////////////////////////// 프래그먼트에서// findviewbyid 하는 법, listview얻는 법// ////////////////////////////////////////////
		adapter_groupreservation = new GroupReservationAdapter((GroupReservationActivity) getActivity(),jsonArray, resources ); // 어뎁터를 생성하고 데이터 설정	
		listview = (ListView) l_Layout.findViewById(R.id.GroupReservation_listview); // 리스트뷰에 어뎁터 설정
		listview.setAdapter(adapter_groupreservation);
		
		getJsonArrayThread = new GetJsonArrayThread();
		getJsonArrayThread.start();
		try {
			getJsonArrayThread.join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(jsonArray == null || jsonArray.length() == 0){ 
			noreserve_tv.setHeight(300);
			noreserve_tv.setText("예약 현황이 없습니다.");
			
			} else {
			noreserve_tv.setHeight(0);
			noreserve_tv.setText("");
			}
		
		adapter_groupreservation.setItem(jsonArray);
		adapter_groupreservation.notifyDataSetChanged();
		
		//TextView noreserve_tv = new TextView((GroupReservationActivity) getActivity());
		// 예약 결과가 없을 시 "예약 현황이 없습니다" 로 설정
		//Log.d("error_", "l_Layout.getChildCount() : " + l_Layout.getChildCount());
		
		return l_Layout;

	}

	
	public class GetJsonArrayThread extends Thread{
		public void run(){
			client_select.post(URL_select, params_select,new JsonHttpResponseHandler() {
				@Override
				public void onFailure(int statusCode, Header[] headers,String responseString, Throwable throwable) {
					super.onFailure(statusCode, headers, responseString,throwable);
				}
				@Override
				public void onSuccess(int statusCode, Header[] headers,JSONArray response) {					
					jsonArray = response;
					//Log.d("time_", "In client");
					//Log.d("error_","GroupReservation jsonArray.length() : "+ jsonArray.length());
				}
			});
		}
	}
	
	

}
