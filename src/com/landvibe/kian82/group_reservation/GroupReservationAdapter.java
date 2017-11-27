package com.landvibe.kian82.group_reservation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inhareservation.R;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

public class GroupReservationAdapter extends BaseAdapter {
	private String[] start_time_array;
	private String[] end_time_array;
	private String start_time = null;
	private String end_time = null;	
	private String category = null;
	private Context context = null;
	private JSONArray jsonArray = null;
	private JSONObject jsonObject = null;
	private LayoutInflater layoutInflater = null;
	ViewHolder viewHolder = null;
	private AlertDialog.Builder cancelDialog;
	private int reserveSeq=0;
	private String reservation = null;
	private HashMap<Integer, List<String>> reserveMap = new HashMap<Integer, List<String>>();
	private String reserveDialogStr = null;
	private Dialog mMainDialog;
	Resources resources;
	// 상세 내역 관련 뷰
	TextView categoryTv;
	TextView circleTv;
	TextView nameTv;
	TextView facilityTv;
	TextView datetTv;
	TextView EventTv;
	Button cancelBtn;
	Button shareBtn;
	String categoryForkakao;
	String dataForKakao;
	String timeForKakao;
	
	public JSONArray getJSONArray(){
		return jsonArray;
	}
	
	public void clearData() {
		jsonArray = null;
	}
	
	class ViewHolder{
		TextView r_day_tv;
		TextView f_name_tv;
		Button detail_btn;
	}
	public GroupReservationAdapter(Context context, JSONArray jsonArray, Resources resources) {
		
		this.context = context;
		this.jsonArray = jsonArray;
		this.resources = resources;
		layoutInflater = LayoutInflater.from(context);
		start_time_array = resources.getStringArray(R.array.startTimeList_);
		end_time_array = resources.getStringArray(R.array.endTimeList_);
		
	}
	
	public AlertDialog createDialog(String category , String circle,String name, String facility, String date , String event, int reserveSeq)
	{
	//	LinearLayout li = (LinearLayout) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View myview = LayoutInflater.from(context).inflate(R.layout.dialog_layout_detail, null);
		AlertDialog.Builder ab = new AlertDialog.Builder(context);
		
		TextView title = new TextView(context);
		title.setText("상세내역");
		title.setBackgroundColor(Color.DKGRAY);
		title.setPadding(20, 20, 20, 20);
		title.setGravity(Gravity.CENTER);
		title.setTextColor(Color.WHITE);
		title.setTextSize(30);

		ab.setCustomTitle(title);
	
		ab.setView(myview);
		categoryTv = (TextView) myview.findViewById(R.id.detail_category_tv);
		circleTv = (TextView) myview.findViewById(R.id.detail_circle_tv);
		nameTv = (TextView) myview.findViewById(R.id.detail_name_tv);
		facilityTv = (TextView) myview.findViewById(R.id.detail_facility_tv);
		datetTv = (TextView) myview.findViewById(R.id.detail_date_tv);
		EventTv = (TextView) myview.findViewById(R.id.detail_event_tv);
		Button cancelBtn = (Button) myview.findViewById(R.id.detail_cancel_btn);
		Button shareBtn = (Button) myview.findViewById(R.id.detail_share_btn);
		shareBtn.setId(reserveSeq);
		shareBtn.setOnClickListener( new OnClickListener(){
			@Override
			public void onClick(View v) {
				v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.button_click));
				v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.button_click2));
				int reserveSeq_ = v.getId();
				List<String> detailList = reserveMap.get(reserveSeq_);
				String shareInfo = "소속 : " + detailList.get(6) + "\n"
						+ "사용 단체 : " + detailList.get(1) + "\n"
						+ "신청인 : " + detailList.get(2) + "\n"
						+ "시설 : " + detailList.get(3) + "\n"
						+ "예약 날짜 : " + detailList.get(7) + "\n"
						+ "예약 시간 : " + detailList.get(8) + "\n"
						+  "행사명 : " + detailList.get(5);
				Log.d("error__", shareInfo);
				
				try {
					KakaoLink kakaoLink = KakaoLink.getKakaoLink(context);
					KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
					kakaoTalkLinkMessageBuilder.addText(shareInfo);
			        final String linkContents = kakaoTalkLinkMessageBuilder.build();
			        kakaoLink.sendMessage(linkContents, context);
				} catch (KakaoParameterException e) {
		            e.getMessage();
		        }
			}
			
		});
		cancelBtn.setVisibility(View.GONE);
		categoryTv.setText(category);
		circleTv.setText(circle);
		nameTv.setText(name);
		facilityTv.setText(facility);
		datetTv.setText(date);
		EventTv.setText(event);
		ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			
		
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		
		}); 
			
		return ab.create();
		
	}
	
	private OnClickListener OnClickListener() {
		// TODO Auto-generated method stub
		return null;
	}

	// 확인, 취소버튼 클릭 시 나오는 dialog 정의
	public void makeCancleDialog(){
		
		cancelDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(context, "상세내역 확인", Toast.LENGTH_SHORT).show();
			}
		});
		
	}

	@Override
	public int getCount() {
		if( jsonArray != null){
			//Log.d("error_", "int getCount()   not null!  jsonArray.length() : " + jsonArray.length() );
			return jsonArray.length();
		} else {
			//Log.d("error_", "int getCount()   null!");
			return 0;
		}
	}
	// getCount 여기까지 확인완료  제이슨 배열 됨

	// 현재 아이템의 오브젝트를 리턴, Object를 상황에 맞게 변경하거나 리턴받은 오브젝트를 캐스팅해서 사용
	@Override
	public Object getItem(int position) {
		Log.d("error_", "JSONObject getItemId " + position);
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = jsonArray.getJSONObject(position);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
		
	}

	@Override
	public long getItemId(int position) {
		//Log.d("error_", "long getItemId " + position);
		return position;
	}

	// 출력 될 아이템 관리
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int pos = position;
		View listview_layout = convertView;
		
		//Log.d("error_", "Adapter_MyReservation 생성자 인수 jsonArray.length():" + jsonArray.length());
		//Log.d("error_", "View getView의 position : " + position);
			try {
				jsonObject = jsonArray.getJSONObject(position);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			// 1. 어뎁터뷰가 재사용할 뷰를 넘겨주지 않은 경우에만 새로운 뷰를 생성
			if( listview_layout == null){
				listview_layout = layoutInflater.inflate(R.layout.listview_groupreservation, null);
				
				// 2. 현재 아이템의 내용을 변경할 뷰를 찾는다
				// view holder를 생성하고 사용할 자식 뷰를 찾아 view holder에 참조시킨다.
				// 생성된 view holder는 아이템에 설정해 두고, 다음에 아이템 재사용 시 참조한다
				viewHolder= new ViewHolder();
				viewHolder.r_day_tv = (TextView) listview_layout.findViewById(R.id.listview_GroupReservation_rday_tv);
				viewHolder.f_name_tv = (TextView) listview_layout.findViewById(R.id.listview_GroupReservation_fname_tv);
				viewHolder.detail_btn = (Button) listview_layout.findViewById(R.id.listview_GroupReservation_detail_btn);
				listview_layout.setTag(viewHolder);
			}
			
			else {
				// 재사용 아이템에는 이전에 View Holder 객체를 설정해 두었다.
				// 그러므로 설정된 View Holder 객체를 이용해서 findViewById 함수를 사용하지 않고 원하는 뷰를 참조할 수 있다.
				viewHolder = (ViewHolder) listview_layout.getTag();
			}
			
				try {
					//Log.d("error_", "start_time_index" + jsonObject.toString());
					//Log.d("error_", "start_time_index" + jsonObject.getInt("start_time_index"));
					start_time = start_time_array[jsonObject.getInt("start_time_index")];
					end_time = end_time_array[jsonObject.getInt("end_time_index")-1];
					category = jsonObject.getString("big_category") + "\n" + jsonObject.getString("mid_category") + "\n" + jsonObject.getString("small_category");
					categoryForkakao = jsonObject.getString("big_category") + "/" + jsonObject.getString("mid_category") + "/" + jsonObject.getString("small_category");
					dataForKakao = jsonObject.getString("r_day");
					timeForKakao = start_time + " ~ " + end_time;
					
					// 각각의 view들에 대한 id 지정. 예약취소할 때 id로 r_seq를 가져와서 씀
					// 예약취소 dialog에서 해당 id(r_seq)에 해당하는 예약내용을 가져와서 씀
					viewHolder.detail_btn.setId(jsonObject.getInt("r_seq"));
					viewHolder.r_day_tv.setId(jsonObject.getInt("r_seq"));
					viewHolder.f_name_tv.setId(jsonObject.getInt("r_seq"));
					reserveDialogStr = jsonObject.getString("r_day") + " / " +start_time + " ~ " + end_time;
					
					viewHolder.r_day_tv.setText(jsonObject.getString("r_day") + " / " +start_time + " ~ " + end_time);
					viewHolder.f_name_tv.setText(jsonObject.getString("f_name"));
					
					List<String> detailList = new ArrayList<String>();
					
					detailList.add(category);
					detailList.add(jsonObject.getString("c_name"));
					detailList.add(jsonObject.getString("s_name"));
					detailList.add(jsonObject.getString("f_name"));
					detailList.add(reserveDialogStr);
					detailList.add(jsonObject.getString("event"));
					detailList.add(categoryForkakao);
					detailList.add(dataForKakao);						// detailList.get(7);
					detailList.add(timeForKakao);						// detailList.get(8);
					// 해당 r_seq 의 값에 해당하는 예약 문자열을 저장, 예약취소 dialog에서 쓸것임
					reserveMap.put(jsonObject.getInt("r_seq") , detailList);
					
					//Log.d("error_", "reserveMap :      " + reserveMap.get(jsonObject.getInt("r_seq")));
					//Log.d("error_", "jsonObject.getInt(r_seq) :      " + jsonObject.getInt("r_seq"));

				} catch (JSONException e) {
					e.printStackTrace();
				}
				

					// 버튼을 터치 했을 때 이벤트 발생
					
				viewHolder.detail_btn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.button_click));
							v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.button_click2));
							reserveSeq = v.getId(); // 클릭한 버튼의 id(r_seq)를 가져옴
//							cancelDialog.setMessage(reserveMap.get(reserveSeq)); // 클릭한 버튼의 id(r_seq)으로 예약 내용을 가져와서 setMessage
//							cancelDialog.show();
							List<String> detailList = reserveMap.get(reserveSeq);
							mMainDialog = createDialog(detailList.get(0), detailList.get(1),detailList.get(2),detailList.get(3),detailList.get(4), detailList.get(5), reserveSeq);
							mMainDialog.show();
							
	
						}
					});

					// 리스트 아이템을 터치 했을 때 이벤트 발생
				listview_layout.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// 터치 시 해당 아이템 이름 출력
						}
					});

					// 리스트 아이템을 길게 터치 했을 떄 이벤트 발생
				listview_layout.setOnLongClickListener(new OnLongClickListener() {

						@Override
						public boolean onLongClick(View v) {
							// 터치 시 해당 아이템 이름 출력
							return true;
						}
					});
					
				//Log.d("error_", "listview_layout.equals(null)? : ");
		return listview_layout;
	}

	// 외부에서 아이템 요청 시 사용
    public void setItem(JSONArray jarray) {
    	//jsonArray = null; // 기존 예약 목록 삭제
    	jsonArray = jarray;
    	//Log.d("error_", "jarray.length() : " + jarray.length());
    }
}

