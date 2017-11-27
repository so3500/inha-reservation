package com.landvibe.kian82.login;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.inhareservation.R;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class SignUpActivity extends Activity implements OnClickListener, OnFocusChangeListener {
   PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
   Button joinBtn;
   Button cancelBtn;
   Button checkBtn;
   EditText nameEdit;
   EditText idEdit;
   EditText passwordEdit;
   EditText majorEdit;
   EditText numberEdit;
   EditText emailEdit;
   Spinner emailDomainSpinner;
   String nameStr, idStr, passwordStr, majorStr, numberStr, emailStr;
   int emailDomain;
   int ID;
   boolean check, isThereError, isNumberValid, isThereEnglish,
   isThereNumber, isThereSpecialLetter;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_sign_up);

      joinBtn = (Button) findViewById(R.id.join_yes_btn);
      cancelBtn = (Button) findViewById(R.id.join_no_btn);
      checkBtn = (Button) findViewById(R.id.check_btn);
      nameEdit = (EditText) findViewById(R.id.join_name_et);
      idEdit = (EditText) findViewById(R.id.join_id_et);
      passwordEdit = (EditText) findViewById(R.id.join_password_et);
      majorEdit = (EditText) findViewById(R.id.join_major_et);
      numberEdit = (EditText) findViewById(R.id.join_number_et);
      emailEdit = (EditText) findViewById(R.id.textEmailAddress);

      joinBtn.setOnClickListener(this);
      cancelBtn.setOnClickListener(this);
      checkBtn.setOnClickListener(this);
      
      nameEdit.setOnFocusChangeListener(this);
      idEdit.setOnFocusChangeListener(this);
      passwordEdit.setOnFocusChangeListener(this);
      majorEdit.setOnFocusChangeListener(this);
      numberEdit.setOnFocusChangeListener(this);
      emailEdit.setOnFocusChangeListener(this);

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
                  emailDomain = emailDomainSpinner.getSelectedItemPosition();
               }

               @Override
               public void onNothingSelected(AdapterView<?> parent) {
                  // TODO Auto-generated method stub

               }

            });
      emailDomainSpinner.setOnFocusChangeListener(this);

      TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        
        String myPhoneNum = mTelephonyMgr.getLine1Number();

        numberEdit.setInputType(android.text.InputType.TYPE_CLASS_PHONE);
      numberEdit.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
      numberEdit.setText(myPhoneNum);

      check = false;
      isThereError = false;
      isNumberValid = true;
      isThereEnglish = false;
      isThereNumber = false;
      isThereSpecialLetter = false;
   }

   @Override
   public void onFocusChange(View view, boolean hasFocus) {
      // TODO Auto-generated method stub
      if (!hasFocus)
      {
         view.setBackgroundResource(R.drawable.lost_focus_border);
      }
   }

   @Override
   public void onClick(View v) {
      final AlertDialog.Builder builder = new AlertDialog.Builder(this);
      
      if (joinBtn == v) {
         if (check == true) {
            isThereError = false;
            
            nameStr = nameEdit.getText().toString();
            idStr = idEdit.getText().toString();
            passwordStr = passwordEdit.getText().toString();
            majorStr = majorEdit.getText().toString();
            numberStr = numberEdit.getText().toString();
            emailStr = emailEdit.getText().toString();
            emailDomain = emailDomainSpinner.getSelectedItemPosition();
            
            
            try {
               if (!phoneNumberUtil.isValidNumber(phoneNumberUtil.parse(numberStr, "KR")))
               {
                  isNumberValid = false;
               }
            } catch (NumberParseException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }

            if (9 <= passwordStr.length() && passwordStr.length() <= 12)
            {
               for (int i = 0; i < passwordStr.length(); i++)
               {
                  if (48 <= passwordStr.charAt(i) && passwordStr.charAt(i) <= 57)
                  {
                     isThereNumber = true;
                  }
                  else if ((65 <= passwordStr.charAt(i) && passwordStr.charAt(i) <= 90)
                        || (97 <= passwordStr.charAt(i) && passwordStr.charAt(i) <= 122))
                  {
                     isThereEnglish = true;
                  }
                  else if ((33 <= passwordStr.charAt(i) && passwordStr.charAt(i) <= 47)
                        || (58 <= passwordStr.charAt(i) && passwordStr.charAt(i) <= 64)
                        || passwordStr.charAt(i) == 91
                        || (93 <= passwordStr.charAt(i) && passwordStr.charAt(i) <= 96)
                        || passwordStr.charAt(i) == 123
                        || (125 <= passwordStr.charAt(i) && passwordStr.charAt(i) <= 126)
                        )
                  {
                     isThereSpecialLetter = true;
                  }
               }
               if (!(isThereEnglish && isThereNumber && isThereSpecialLetter))
               {
                  passwordEdit.setBackgroundResource(R.drawable.focus_border);
                  passwordEdit.requestFocus();
                  builder.setMessage("영문+숫자+특수문자를 조합하여 비밀번호를 입력하세요.");
                  isThereError = true;
               }
            }
            
            if (nameStr.isEmpty())
            {
               nameEdit.setBackgroundResource(R.drawable.focus_border);
               nameEdit.requestFocus();
               builder.setMessage("이름을 입력하세요.");
               isThereError = true;
            }
            else if (!(9 <= passwordStr.length() && passwordStr.length() <= 12))
            {
               passwordEdit.setBackgroundResource(R.drawable.focus_border);
               passwordEdit.requestFocus();
               builder.setMessage("9~12자리 이내의비밀번호를 입력하세요.");
               isThereError = true;
            }
            else if (majorStr.isEmpty())
            {
               majorEdit.setBackgroundResource(R.drawable.focus_border);
               majorEdit.requestFocus();
               builder.setMessage("학과을 입력하세요.");
               isThereError = true;
            }
            else if (numberStr.isEmpty())
            {
                numberEdit.setBackgroundResource(R.drawable.focus_border);
                numberEdit.requestFocus();
                builder.setMessage("전화번호를 입력하세요.");
                isThereError = true;
            }
            else if (!isNumberValid)
            {
               numberEdit.setBackgroundResource(R.drawable.focus_border);
               numberEdit.requestFocus();
               builder.setMessage("유효한 전화번호를 입력하세요.");
               isThereError = true;
            }
            else if (emailStr.isEmpty())
            {
               emailEdit.setBackgroundResource(R.drawable.focus_border);
               emailEdit.requestFocus();
               builder.setMessage("이메일을 입력하세요.");
               isThereError = true;
            }
            else if (emailDomain == 0)
            {
               emailDomainSpinner.setBackgroundResource(R.drawable.focus_border);
               emailDomainSpinner.requestFocus();
               builder.setMessage("이메일주소를 선택하세요.");
               isThereError = true;
            }
            
            if (isThereError)
            {
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
            }
            else
            {
               String URL = "http://oursoccer.co.kr/study/reserve/register.php";
               AsyncHttpClient client = new AsyncHttpClient();
               RequestParams params = new RequestParams();
               params.put("s_name", nameStr);
               params.put("s_id", idStr);
               params.put("s_pwd", passwordStr);
               params.put("s_major", majorStr);
               params.put("s_phone", numberStr);
               params.put("s_email", emailStr);
               params.put("s_email_domain", emailDomain);

               client.post(URL, params, new JsonHttpResponseHandler() {
                  @Override
                  public void onSuccess(int statusCode, Header[] headers,
                        JSONObject response) {
                     // super.onSuccess(statusCode, headers, response);
                     builder.setCancelable(true) // 뒤로 버튼 클릭시 취소 가능 설정
                     .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        // 취소 버튼 클릭시 설정
                        public void onClick(DialogInterface dialog, int whichButton) {
                           Intent reqIntent = new Intent(getApplicationContext(),
                                 LoginActivity.class);                           
                           startActivity(reqIntent);
                           finish();
                        }
                     });

                     builder.setTitle("가입 완료");
                     builder.setMessage("회원 가입이 성공적으로 완료되었습니다.");
                     AlertDialog dialog = builder.create(); // 알림창 객체 생성
                     dialog.setCanceledOnTouchOutside(false);
                     dialog.show();
                  }

                  @Override
                  public void onFailure(int statusCode, Header[] headers,
                        String responseString, Throwable throwable) {
                     super.onFailure(statusCode, headers,
                     responseString, throwable);
                     //Toast.makeText(SignUpActivity.this, responseString,
                     //      Toast.LENGTH_SHORT).show();
                     finish();
                  }
               });
            }
         }
         else
         {
             idEdit.requestFocus();
             idEdit.setBackgroundResource(R.drawable.focus_border);

             builder.setMessage("아이디 중복확인을 수행하세요.");
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
         }
      }
      if (cancelBtn == v) {
         finish();
      }
      if (checkBtn == v) {
         idStr = idEdit.getText().toString();
         if (idStr.length() == 8) {
            idStr = idEdit.getText().toString();
            String url = "http://oursoccer.co.kr/study/reserve/register_checkid.php";
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("s_id", idStr);
            client.post(url, params, new JsonHttpResponseHandler() {
               @Override
               public void onSuccess(int statusCode,
                     Header[] headers, JSONArray response) {
                  // TODO Auto-generated method stub
                  super.onSuccess(statusCode, headers, response);
                  
                  if (response.length() > 0)
                  {
                	  idEdit.setBackgroundResource(R.drawable.focus_border);
                      idEdit.requestFocus();
                     
                      
                     builder.setMessage("중복된 아이디가 존재합니다.");
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
                  }
               }
               @Override
                     public void onFailure(int statusCode,
                           Header[] headers, String responseString,
                           Throwable throwable) {
                        // TODO Auto-generated method stub
                        super.onFailure(statusCode, headers, responseString, throwable);
                	 passwordEdit.requestFocus();
                     
                     builder.setMessage("사용 가능한 아이디 입니다.");
                     builder.setCancelable(true) // 뒤로 버튼 클릭시 취소 가능 설정
                     .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        // 취소 버튼 클릭시 설정
                        public void onClick(DialogInterface dialog, int whichButton) {
                           dialog.cancel();
                        }
                     });

                     builder.setTitle("사용 가능"); // 제목 설정
                     AlertDialog dialog = builder.create(); // 알림창 객체 생성
                     dialog.setCanceledOnTouchOutside(false);
                     dialog.show();
                     
                     idEdit.setFocusableInTouchMode(false);
                     idEdit.setFocusable(false);
                     idEdit.setClickable(false);
                     check = true;
                     }
            });

         }
         else {
            idEdit.requestFocus();
            idEdit.setBackgroundResource(R.drawable.focus_border);

            builder.setMessage("유효한 아이디를 입력하세요.");
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
         }
      }
   }
}