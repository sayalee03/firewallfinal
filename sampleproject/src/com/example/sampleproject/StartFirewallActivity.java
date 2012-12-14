package com.example.sampleproject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;

import android.app.Activity;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class StartFirewallActivity extends Activity {

	TextView text;
	String received="";
	String mClientMsg = "";
	Thread myCommsThread = null;
	String actualPassword="";
	protected static final int MSG_ID = 0x1337;
	ServerSocket ss = null;
	ArrayList<Socket> sockets = new ArrayList<Socket>();
	ArrayList<Socket> secureSockets = new ArrayList<Socket>();
	ServerSocket secured=null;
	DatabaseManager database;
	Global gb= new Global();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_firewall);
		TextView ipTv = (TextView) findViewById(R.id.ipAddress);
		try{
			database=new DatabaseManager(this);
			// WifiManager wifi = (WifiManager) getSystemService(WIFI_SERVICE);
			// WifiInfo info = wifi.getConnectionInfo();
			// int rawLocalAddress = info.getIpAddress();
			// byte[] rawBytes = BigInteger.valueOf(rawLocalAddress).toByteArray();
			// InetAddress actualAddress = InetAddress.getByAddress(rawBytes);
			// String ipAddress = actualAddress.getHostAddress().toString();
			String ipAddress="";
			Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
			for(;en.hasMoreElements();){
				NetworkInterface interf = en.nextElement();
				for(Enumeration<InetAddress> addresses=interf.getInetAddresses();addresses.hasMoreElements();){
					InetAddress inetAddr = addresses.nextElement();
					if(!inetAddr.isLoopbackAddress())
						ipAddress = inetAddr.getHostAddress().toString();
				}
			}
			ipTv.setText("Your IP Address is " + ipAddress);
		}
		catch(Exception e){
			ipTv.setText("Unable to fetch IP Address now");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_start_firewall, menu);
		return true;
	}

	public void startCapture(View view){
		TextView tv = (TextView) findViewById(R.id.textView);
		tv.setText("Nothing from client yet");
		new MakeConnectionServer().execute();
		try {
			ss = new ServerSocket(Constants.NORMAL_PORT);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		try{
			secured = new ServerSocket(Constants.SECURE_PORT);
		}
		catch(IOException e){
			received += Constants.SOCKET_CONNECTION_FAILED;
			received += e.getMessage();
		}

	}

	private class MakeConnectionServer extends AsyncTask<URL,Integer,Long>{
		Resources res = getResources();
		int success = 0;
		protected Long doInBackground(URL...urls ){
			int count=0;
			while(count<Constants.THREAD_COUNT){

				Socket s=null;
				Message m = new Message();
				m.what = 0x1337;
				try {
					if(s == null)
						s=ss.accept();

					EditText password = (EditText) findViewById(R.id.password);
					actualPassword = password.getText().toString();
					DataInputStream in = new DataInputStream(s.getInputStream());
					DataOutputStream out = new DataOutputStream(s.getOutputStream());
					String sentPassword = in.readUTF();
					String sourceIp = s.getInetAddress().toString();
					received += res.getString(R.string.CONNECTION_FROM_MESSAGE) + sourceIp;

					if(actualPassword.equals(sentPassword)){
						out.writeUTF(Constants.SUCCESS);
						received += Constants.SUCCESS_MESSAGE + s.getInetAddress() +"\n";
						success=1;
					}
					else
						out.writeUTF(Constants.FAILED);
					myUpdateHandler.sendMessage(m);
					sockets.add(s);
					count++;
					if(success==1){

						Socket securedSocket = null;
						Message msg = new Message();
						msg.what = 0x1337;
						int error=0;
						try{
							if(securedSocket==null)
								securedSocket = secured.accept();
							secureSockets.add(securedSocket);

							EditText passwordEt = (EditText) findViewById(R.id.password);
							actualPassword = passwordEt.getText().toString();
							DataInputStream secureIn = new DataInputStream(securedSocket .getInputStream());
							DataOutputStream secureOut = new DataOutputStream(securedSocket .getOutputStream());
							String sentMsg = secureIn.readUTF();
							String sentSecurePassword = sentMsg.substring(0, sentMsg.indexOf(Constants.DELIMITER));
							String website=sentMsg.substring(sentMsg.indexOf(Constants.DELIMITER)+1);
							String clientIp =securedSocket.getInetAddress().toString();

							if(clientIp.startsWith("/")){
								clientIp = clientIp.substring(1);
							}

							Rule searchRule = new Rule();
							searchRule.setIpAddress(clientIp);
							searchRule.setWebsiteAddress(website);
							ArrayList<Rule> searchResults = database.selectRules(searchRule);

							if(searchResults.size()==0){
								error = Constants.SECURITY_ERROR;
							}

							boolean allowed = false;
							for(int i=0;i<searchResults.size();i++){
								Rule currentResult = searchResults.get(i);
								if(currentResult.getIpAddress().equals("*") && currentResult.getWebsiteAddress().equals("*") && currentResult.getAction().equals("accept")){
									allowed = true;
									break;
								}
								if(currentResult.getIpAddress().equals(clientIp) && currentResult.getWebsiteAddress().equals("*") && currentResult.getAction().equals("accept")){
									allowed = true;
									break;
								}
								if(currentResult.getIpAddress().equals("*") && currentResult.getWebsiteAddress().equals(website) && currentResult.getAction().equals("accept")){
									allowed = true;
									break;
								}
								if(currentResult.getIpAddress().equals(clientIp) && currentResult.getWebsiteAddress().equals(website) && currentResult.getAction().equals("accept")){
									allowed = true;
									break;
								}
								
							}

							if(error !=Constants.SECURITY_ERROR && allowed){
								received += Constants.CONNECTED_TO_MESSAGE + website + " from " + securedSocket.getInetAddress()+"\n";

								if(!sentSecurePassword.equals(actualPassword)){
									throw new SecurityException();
								}

								URL url = new URL(website);
								BufferedReader buf = new BufferedReader(new InputStreamReader(url.openStream()));

								String inputLine="";
								while((inputLine=buf.readLine())!=null){
									secureOut.writeUTF(inputLine);
								}

								secureOut.writeUTF(Constants.FINISHED_SENDING);
							}
							else
								secureOut.writeUTF(Constants.SECURITY_EXCEPTION);

							securedSocket.close();
							secureSockets.add(securedSocket);
						}

						catch(Exception e){
							received += "Error while writing data";
							
						}
						myUpdateHandler.sendMessage(msg);
					}
					s.close();
				}
				catch(SecurityException e){
					received += Constants.ACCESS_DENIED +s.getInetAddress();
				}

				catch (Exception e) {
					received += Constants.GENERAL_EXCEPTION + s.getInetAddress();
				}
				Log.i("Start Firewall", received);
			}
			return (long)count;
		}
		protected void onPostExecute(Long result){
			TextView tv = (TextView) findViewById(R.id.textView);
			tv.setText(received);
		}
	}

	Handler myUpdateHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_ID:
				TextView tv = (TextView) findViewById(R.id.textView);
				tv.setText(received);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

}
