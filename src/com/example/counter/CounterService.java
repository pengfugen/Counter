package com.example.counter;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class CounterService extends Service implements ICounterService{

	public final static String BROADCAST_COUNTER_ACTION = "broadcast_counter_action";
	public final static String COUNTER_VALUE = "counter_value";
	private final static String LOG_TAG = "CounterService";
	private static boolean flag = false;
	private boolean stop = false;  
	private static int count = 0;
	
	private CounterService mCon;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.v(LOG_TAG, "onCreate====");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.v(LOG_TAG, "onDestroy====");
	}

	@Override
	public void onRebind(Intent intent) {
		// TODO Auto-generated method stub
		super.onRebind(intent);
		Log.v(LOG_TAG, "onRebind====");
	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Log.v(LOG_TAG, "onStart====");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.v(LOG_TAG, "onStartCommand====(intent:"+intent+" flags:"+flags+" startid:"+startId+")");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		Log.v(LOG_TAG, "onUnbind====");
		return super.onUnbind(intent);
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.v(LOG_TAG, "onBind====");
		return binder;
	}

	private CounterBinder binder = new CounterBinder();
	
	class CounterBinder extends Binder
	{
		public CounterService getService()
		{
			mCon = CounterService.this;
			return mCon;
		}
	}
	
	@Override
	public void startCounter() {
		Log.v(LOG_TAG, "startCounter====(ThreadId:"+Thread.currentThread().getId()+")");
		/*handler.post(new Runnable()
		{
			@Override
			public void run() {
				
				Log.v(LOG_TAG, "Runnable run======flag:"+flag);
			}
			
		});*/
		flag = true;
		
		new Thread(new MyThread()).start();
		
		/*AsyncTask<Integer, Integer, Integer> task = new AsyncTask<Integer, Integer, Integer>() {
			@Override
			protected Integer doInBackground(Integer... vals) {
				Integer initCounter = vals[0];

				stop = false;
				while (!stop) {
					publishProgress(initCounter);

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					initCounter++;
				}

				return initCounter;
			}

			@Override
			protected void onProgressUpdate(Integer... values) {
				super.onProgressUpdate(values);
				Log.v(LOG_TAG, "onProgressUpdate====");
				int counter = values[0];

				Intent intent = new Intent(BROADCAST_COUNTER_ACTION);
				intent.putExtra(COUNTER_VALUE, counter);

				sendBroadcast(intent);
			}

			@Override
			protected void onPostExecute(Integer val) {
				int counter = val;

				Intent intent = new Intent(BROADCAST_COUNTER_ACTION);
				intent.putExtra(COUNTER_VALUE, counter);

				sendBroadcast(intent);
			}

		};

		task.execute(0);*/
	}

	@Override
	public void stopCounter() {
		
		Log.v(LOG_TAG, "stopCounter====");
		flag = false;
		 stop = true;  
	}
	
	class MyThread implements Runnable
	{
		@Override
		public void run() {
			Log.v(LOG_TAG, "MyThread run======(ThreadId:"+Thread.currentThread().getId()+")");
			while(flag)
			{
				try {
					Thread.sleep(1000);
					Intent intent = new Intent(
							CounterService.BROADCAST_COUNTER_ACTION);
					synchronized(mCon)
					{
						if(mCon != null)
						{
							count++;
							intent.putExtra(COUNTER_VALUE, count);
							mCon.sendBroadcast(intent);
						}
					}

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
}
