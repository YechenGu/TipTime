package com.example.testapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity {
	
	private Button registerBtn,loginBtn;
	private EditText userEditText,pwdEditText;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		query();

		Log.d("LoginActivity","111");
		registerBtn = (Button)findViewById(R.id.register);
		loginBtn = (Button)findViewById(R.id.login);
		
		userEditText = (EditText)findViewById(R.id.username);
		pwdEditText = (EditText)findViewById(R.id.password);
		Log.d("LoginActivity","222");
		registerBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(validate(pwdEditText)){
					if(register()){
						showDialog("注册成功");
					}else{
						showDialog("草，你注册失败了");
					}
				}else {
				}
			}
		});
		
		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(validate(userEditText)){
					if(login()){
						showDialog("登录成功");
					}else{
						showDialog("草，你登录失败了");
					}
				}
			}
		});
	}

	//登录
	private boolean login(){
		String username = userEditText.getText().toString().trim();
		String pwd = pwdEditText.getText().toString().trim();
		String result=loginQuery(username,pwd);
		if(result!=null&&result.equals("1")){
			return true;
		}else{
			return false;
		}
	}

	//注册
	private boolean register(){
		String username = userEditText.getText().toString();
		Log.d("LoginActivity",username);
		String pwd = pwdEditText.getText().toString();
		Log.d("LoginActivity",pwd);
		//sendByHttpClient(username,pwd);
		query();
		return  true;
	}

	//输入合法
	private boolean validate(EditText editText){
		String username = editText.getText().toString();
		if(username==null || username.trim().equals("")){
			showDialog("username invalid");
			return false;
		}
		String pwd = pwdEditText.getText().toString();
		if(pwd==null || pwd.trim().equals("")){
			showDialog("password invalid");
			return false;
		}
		return true;
	}

	private void showDialog(String msg){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg)
		       .setCancelable(false)
		       .setPositiveButton("cao", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private String registerQuery(String username,String password){
		String queryString = "caozuo=register"+"&name="+username+"&password="+password;
		//BASE_URL="http://b3t2506506.qicp.vip/register.jsp";
		String url = HttpUtil.BASE_URL+"servlet/registerServlet?"+queryString;
		return HttpUtil.queryStringForPost(url);
    }

	private String loginQuery(String username,String password){
		String queryString = "username="+username+"&password="+password;
		String url = HttpUtil.BASE_URL+"servlet/LoginServlet?"+queryString;
		return HttpUtil.queryStringForPost(url);
	}

	public static final int SHOW_RESPONSE=1;
	@SuppressLint("HandlerLeak")
	public Handler handler=new Handler() {
		public void handleMessage(Message msg)
		{
			switch (msg.what){
				case SHOW_RESPONSE:
					String response=(String)msg.obj;
					break;
				default:
					break;
			}
		}
	};

	private void sendByHttpClient(final String name, final String pw) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					HttpClient httpclient=new DefaultHttpClient();
					HttpPost httpPost=new HttpPost("http://b3t2506506.qicp.vip/register.jsp");
					List<NameValuePair> params=new ArrayList<NameValuePair>();//将id和pw装入list
					params.add(new BasicNameValuePair("caozuo","register"));
					params.add(new BasicNameValuePair("name",name));
					params.add(new BasicNameValuePair("password",pw));
					final UrlEncodedFormEntity entity=new UrlEncodedFormEntity(params,"utf-8");
					httpPost.setEntity(entity);
					HttpResponse httpResponse= httpclient.execute(httpPost);
					if(httpResponse.getStatusLine().getStatusCode()==200)
					{
						HttpEntity entity1=httpResponse.getEntity();
						String response= EntityUtils.toString(entity1, "utf-8");
						Message message=new Message();
						message.obj=response;
						handler.sendMessage(message);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		Log.d("LoginActivity","9999");
	}

	public volatile static String url1,url2,url;

	public void query(){//查询函数
		try {
			url1 = "http://b3t2506506.qicp.vip/register.jsp";
			url2="/user?caozuo=register";
			url = url1 + url2;
			String result;
			System.out.println("1");
			HttpGet request = new HttpGet(url);//调用servlet的doget方法   
			System.out.println("2");    //在这里执行请求,访问url，并获取响应    
			HttpResponse reponse = new DefaultHttpClient().execute(request);
			System.out.println("3");    //获取返回码,等于200即表示连接成功,并获得响应   
			if (reponse.getStatusLine().getStatusCode() == 200) {
				System.out.println("4");        //获取响应中的数据     
				result = EntityUtils.toString(reponse.getEntity());
				System.out.println("结果为：" + result);//输出查询的结果   
			} else {
				System.out.println("连接失败");
			}
		} catch(Exception e){
		e.printStackTrace();
	}
}
}