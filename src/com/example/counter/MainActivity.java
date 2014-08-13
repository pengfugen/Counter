package com.example.counter;

import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;

public class MainActivity extends Activity implements View.OnClickListener{

	TextView textview_counter;
	Button button_start;
	Button button_stop;
	
	private ICounterService counterService;
	
	private static final String LOG_TAG = "Counter";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textview_counter = (TextView)findViewById(R.id.textview_counter);
		button_start = (Button)findViewById(R.id.button_start);
		button_stop = (Button)findViewById(R.id.button_stop);
		
		button_start.setOnClickListener(this);  
		button_stop.setOnClickListener(this);  
      
		button_start.setEnabled(true);  
		button_stop.setEnabled(false);
		Intent intent = new Intent(this, CounterService.class);
		bindService(intent, conn, Context.BIND_AUTO_CREATE);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		IntentFilter counterActionFilter = new IntentFilter(CounterService.BROADCAST_COUNTER_ACTION);  
	    registerReceiver(counterActionReceiver, counterActionFilter); 
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.button_start)
		{
			counterService.startCounter();
			button_start.setEnabled(false);  
			button_stop.setEnabled(true);  
		}
		else if(v.getId() == R.id.button_stop)
		{
			counterService.stopCounter();
			button_start.setEnabled(true);  
			button_stop.setEnabled(false);
		}
		
		
	}

	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unbindService(conn);
		unregisterReceiver(counterActionReceiver);
	}


	private ServiceConnection conn = new ServiceConnection()
	{

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			counterService = ((CounterService.CounterBinder)service).getService();
			Log.v(LOG_TAG, "onServiceConnected======counterService:"+counterService);
			
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.v(LOG_TAG, "onServiceDisconnected======name:"+name);
			 counterService = null;  
		}
		
	};
	
	private BroadcastReceiver counterActionReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			int counter = intent.getIntExtra(CounterService.COUNTER_VALUE, 0);
			String text = String.valueOf(counter);
			textview_counter.setText(text);
			Log.i(LOG_TAG, "Receive counter event");
		}
	};
}
