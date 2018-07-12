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
import com.gfso.client.oauthclientapplication.msg.ResponseMsg;
import com.gfso.client.oauthclientapplication.okhttp.OkhttpHelper;
import com.gfso.client.oauthclientapplication.okhttp.HttpLoadingDialog;
import com.gfso.client.oauthclientapplication.util.Contents;
import com.squareup.okhttp.Request;
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
    MyEditText userId ;
    @BindView(R.id.password)
    MyEditText password ;
    OkhttpHelper okhttpHelper;
    Toolbar toolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        userId.setInputType(InputType.TYPE_CLASS_TEXT); //输入类型
        userId.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)}); //最大输入长度
        password.setTransformationMethod(PasswordTransformationMethod.getInstance()); //设置为密码输入框
        okhttpHelper = OkhttpHelper.getOkhttpHelper() ;
        Text() ;
    }

    protected void addListener() {
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin(v) ;
//                User user = new User();
//                user.setUsername("jack");
//                MyApplication.getInstance().putUser(user,"token");
//                Intent intent = new Intent();
//                Bundle conData = new Bundle();
//                conData.putString(Contents.LOGIN_USERID, "jack");
//                intent.putExtras(conData);
//                setResult(RESULT_OK, intent);
//                finish();
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

    private void doLogin( final View v ){
        final String userName = userId.getText().toString() ;

        if ( userName == null ){
            Toast.makeText( v.getContext() , "请输入邮箱" , Toast.LENGTH_SHORT).show();
            return;
        }

        String pwd = password.getText().toString() ;
        if (pwd == null){
            Toast.makeText( v.getContext() , "请输入密码" , Toast.LENGTH_SHORT).show() ;
            return;
        }

        String uri = Contents.LOGIN_URL ;
        Map< String , String > params = new HashMap<String, String>() ;
        params.put("userId" , userName ) ;
        params.put("password" , pwd) ;

        okhttpHelper.doJSONPost(uri, new HttpLoadingDialog<ResponseMsg>(LoginActivity.this ) {
            @Override
            public void onError(Response response, int responseCode, Exception e) throws IOException {
                this.closeSpotsDialog();
                Toast.makeText(v.getContext(), response.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void callBackSucces(Response response, ResponseMsg msg) throws IOException {
                this.closeSpotsDialog();
                closeKeyMode();

                if(msg.isSuccess()){
                    //save user token
                    String token = msg.getData().get("token");
                    User user = new User();
                    user.setUsername(userName);
                    MyApplication.getInstance().putUser(user,token);
                    //finish activity
                    Intent intent = new Intent();
                    Bundle conData = new Bundle();
                    conData.putString(Contents.LOGIN_USERID, userName);
                    intent.putExtras(conData);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    showLoginErrorMsg(msg.getErrorMessage()) ;
                }

//                if(userLoginRespMsg.getStatus() == 1){

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
//                }else {
//                    showLoginErrorMsg() ;
//                    userId.setText("");
//                    password.setText("");
//                }
            }
        }, params);
    }

    public void Text(){
        userId.setText("jack.yu05@sap.com");
        password.setText("123456");
    }

    private void showLoginErrorMsg(String msg){
        closeKeyMode();
        Toast.makeText(this, msg , Toast.LENGTH_LONG).show();
    }
}
