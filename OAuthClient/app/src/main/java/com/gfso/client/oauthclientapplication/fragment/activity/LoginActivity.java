package com.gfso.client.oauthclientapplication.fragment.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gfso.client.oauthclientapplication.MyApplication;
import com.gfso.client.oauthclientapplication.R;
import com.gfso.client.oauthclientapplication.bean.User;
import com.gfso.client.oauthclientapplication.fragment.widget.MyEditText;
import com.gfso.client.oauthclientapplication.msg.LoginRespMsg;
import com.gfso.client.oauthclientapplication.okhttp.OkhttpHelper;
import com.gfso.client.oauthclientapplication.okhttp.loadingSpotsDialog;
import com.gfso.client.oauthclientapplication.util.Content;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.button_log)
    Button login_button ;
    @BindView(R.id.forgetPass)
    TextView forget_button ;
    @BindView(R.id.register)
    TextView register_button ;
    @BindView(R.id.userId)
    MyEditText phone ;
    @BindView(R.id.password)
    MyEditText password ;
    OkhttpHelper okhttpHelper;
    Toolbar toolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_login_activity);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyMode();
                finish();//返回
            }
        });

        addListener();
        initView();
    }
    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Toolbar 必须在onCreate()之后设置标题文本，否则默认标签将覆盖我们的设置
        if (toolbar != null) {
            toolbar.setTitle("");
        }
    }

    protected void initView() {
        phone.setInputType(InputType.TYPE_CLASS_TEXT); //输入类型
        phone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)}); //最大输入长度
        password.setTransformationMethod(PasswordTransformationMethod.getInstance()); //设置为密码输入框
        okhttpHelper = OkhttpHelper.getOkhttpHelper() ;
        Text() ;
    }

    protected void addListener() {
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //doLogin(v) ;
                User user = new User();
                user.setUsername("jack");
                MyApplication.getInstance().putUser(user,"token");
                Intent intent = new Intent();
                Bundle conData = new Bundle();
                conData.putString(Content.LOGIN_USERID, "jack");
                intent.putExtras(conData);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        forget_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("----" , "register------------------------------") ;
                //LoginActivity.this.startActivity(new Intent(LoginActivity.this , RegisterActivity.class));
            }
        });
    }

    private void closeKeyMode(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(password.getWindowToken(),0);
    }

    private void doLogin( View v ){
        String phoneNum = phone.getText().toString() ;

        if ( phoneNum == null ){
            Toast.makeText( v.getContext() , "请输入手机号码" , Toast.LENGTH_SHORT).show();
            return;
        }else if(phoneNum.length() != 11){
            Toast.makeText( v.getContext() , "请输入正确的手机号码" , Toast.LENGTH_SHORT).show();
            return;
        }

        String pwd = password.getText().toString() ;
        if (pwd == null){
            Toast.makeText( v.getContext() , "请输入密码" , Toast.LENGTH_SHORT).show() ;
            return;
        }

        String uri = "http://myOAuthServer" ;
        Map< String , String > params = new HashMap<String, String>() ;
        params.put("phone" , phoneNum ) ;
        params.put("password" , pwd) ;

        okhttpHelper.doPost(uri, new loadingSpotsDialog<LoginRespMsg<User>>(LoginActivity.this ) {
            @Override
            public void onErroe(Response response, int responseCode, Exception e) throws IOException {
                this.closeSpotsDialog();
            }

            @Override
            public void callBackSucces(Response response, LoginRespMsg<User> userLoginRespMsg) throws IOException {
                this.closeSpotsDialog();

                if(userLoginRespMsg.getStatus() == 1){

//                    MyApplication.getInstance().putUser(userLoginRespMsg.getData() , userLoginRespMsg.getTocken());
//                    closeKeyMode() ;
//
//                    if (null == MyApplication.getInstance().getIntent()){
//                        setResult(RESULT_OK);
//                        finish();
//                    }else {
//                        MyApplication.jumpToTargetoActivity(LoginActivity.this);
//                        finish();
//                    }

                }else {
                    showLoginErrorMsg() ;
                    phone.setText("");
                    password.setText("");
                }
            }
        }, params);
    }

    public void Text(){
        phone.setText("jack.yu05@sap.com");
        password.setText("123456");
    }

    private void showLoginErrorMsg(){
        closeKeyMode();
        Toast.makeText(this, "密码错误" , Toast.LENGTH_LONG).show();
    }
}
