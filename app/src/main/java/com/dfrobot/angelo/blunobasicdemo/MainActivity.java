package com.dfrobot.angelo.blunobasicdemo;

import android.content.Context;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;

public class MainActivity  extends BlunoLibrary {
	private ImageButton imageButton;

	private TextView serialReceivedText;
	private TextView tv;
	private TextView tp;
    private ImageButton refresh;
	TextView tvt;
	TextView tvh;



	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        onCreateProcess();														//onCreate Process by BlunoLibrary


        serialBegin(115200);													//set the Uart Baudrate on BLE chip to 115200

        tv=(TextView) findViewById(R.id.tv);
        tp=(TextView) findViewById(R.id.tp);
        tvt=(TextView) findViewById(R.id.tvt);
        tvh=(TextView) findViewById(R.id.tvh);


        refresh = (ImageButton) findViewById(R.id.refresh);
        refresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				sendReq();

			}
		});

        imageButton = (ImageButton) findViewById(R.id.imageButton);					//initial the button for scanning the BLE device
		imageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				imageButtonOnClickProcess();										//Alert Dialog for selecting the BLE device
			}
		});
				//initial the button for scanning the BLE device

	}


	protected void onResume(){
		super.onResume();
		System.out.println("BlUNOActivity onResume");
		onResumeProcess();														//onResume Process by BlunoLibrary
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		onActivityResultProcess(requestCode, resultCode, data);					//onActivityResult Process by BlunoLibrary
		super.onActivityResult(requestCode, resultCode, data);
	}
	
    @Override
    protected void onPause() {
        super.onPause();
        onPauseProcess();														//onPause Process by BlunoLibrary
    }
	
	protected void onStop() {
		super.onStop();
		onStopProcess();														//onStop Process by BlunoLibrary
	}
    
	@Override
    protected void onDestroy() {
        super.onDestroy();	
        onDestroyProcess();														//onDestroy Process by BlunoLibrary
    }

	@Override
	public void onConectionStateChange(connectionStateEnum theConnectionState) {//Once connection state changes, this function will be called
		switch (theConnectionState) {											//Four connection state
		case isConnected:
			Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
			break;
		case isConnecting:
			Toast.makeText(MainActivity.this, "Connecting",Toast.LENGTH_LONG).show();
			break;
		case isToScan:
			break;
		case isScanning:
			Toast.makeText(MainActivity.this, "Scanning",Toast.LENGTH_SHORT).show();
			break;
		case isDisconnecting:
			Toast.makeText(MainActivity.this, "Disconnected",Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}

	@Override
	public void onSerialReceived(String theString) {							//Once connection data received, this function will be called
		// TODO Auto-generated method stub


        String pmunit="ug/m3";
        String tpunit="C       ";
        String hmunit="%";

		tv.setText(theString.substring(10,12)+pmunit);
		tp.setText(theString.substring(1,6)+tpunit+theString.substring(6,10)+hmunit);

	}
	private void sendReq(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection connection=null;
				BufferedReader reader=null;
				try{
					URL url =new URL("http://api.airvisual.com/v2/city?city=Singapore&state=Singapore&country=Singapore&key=sRokLYiP6ST4QeCtP");
					connection =(HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					InputStream in= connection.getInputStream();
					reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder response=new StringBuilder();
					String line;
					while ((line=reader.readLine())!=null){
						response.append(line);
					}
					int st =response.indexOf("aqius");
					tvt.setText("                             "+response.substring(215,217)+"%"+"\n"+response.substring(244,246)+"C"+"        "+response.substring(st+7,st+9));




				}catch (Exception e){
					e.printStackTrace();
				}finally {
					if(reader!=null){
						try{
							reader.close();
						}catch (IOException e){
							e.printStackTrace();
						}
					}
					if (connection!=null){
						connection.disconnect();
					}
				}
			}
		}).start();
	}

}