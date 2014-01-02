package com.btten.calltaxi.Service;

import com.btten.Jms.SplashScreen;
import com.btten.calltaxi.CallTaxi.tools.CallTaxiScreenLock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class CallTaxiJumpWindow extends Activity {
	private CallTaxiScreenLock screenLock = null;
	private View v;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		v = new View(this);
		v.setLayoutParams(lp);

		setContentView(v);

		screenLock = new CallTaxiScreenLock(this);
		this.mIntent = getIntent();
		t = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					while (!BeWellPrepared)
						Thread.sleep(50);
					String action = mIntent.getStringExtra("ACTION");
					if (action != null)
						if (action.equals("doCall"))
							uiHandler.sendEmptyMessage(DOCALL);
						else if (action.equals("reShow"))
							uiHandler.sendEmptyMessage(RESHOW);
						else if (action.equals("OnCallResult"))
							if (mIntent.getIntExtra("STATUS", -1) == 1)
								uiHandler.sendEmptyMessage(ONSUCCESS);
							else
								uiHandler.sendEmptyMessage(ONFAIL);

					if (t != null) {
						t.interrupt();
						t = null;
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		setIntent(intent);
		this.mIntent = intent;
		t = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					while (!BeWellPrepared)
						Thread.sleep(50);
					String action = mIntent.getStringExtra("ACTION");
					if (action != null)
						if (action.equals("doCall"))
							uiHandler.sendEmptyMessage(DOCALL);
						else if (action.equals("reShow"))
							uiHandler.sendEmptyMessage(RESHOW);
						else if (action.equals("OnCallResult"))
							if (mIntent.getIntExtra("STATUS", -1) == 1)
								uiHandler.sendEmptyMessage(ONSUCCESS);
							else
								uiHandler.sendEmptyMessage(ONFAIL);

					if (t != null) {
						t.interrupt();
						t = null;
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	private final int DOCALL = 0;
	private final int ONSUCCESS = 1;
	private final int ONFAIL = 2;
	private final int RESHOW = 3;

	Intent mIntent = null;
	boolean BeWellPrepared = false;
	Thread t = null;

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus)
			BeWellPrepared = true;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (t != null)
			t.start();
	}

	boolean DONE = false;
	Handler uiHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOCALL:
				doCall();
				break;
			case ONSUCCESS:
				OnSuccess();
				break;
			case ONFAIL:
				OnFail();
				break;
			case RESHOW:
				ReShow();
				break;

			default:
				break;
			}

			super.handleMessage(msg);
		};
	};

	private void doCall() {
		DONE = false;
		screenLock.show(v);
		screenLock.waitingForRequest();
	}

	protected void ReShow() {
		// TODO Auto-generated method stub
		screenLock.show(v);
	}

	private void OnSuccess() {
		DONE = true;
		screenLock.show(v);
		screenLock.onSuccess(mIntent);
	}

	private void OnFail() {
		DONE = true;
		screenLock.show(v);
		screenLock.onFail(mIntent);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (DONE)
			super.onBackPressed();
		else {
			Intent intent = new Intent(this, SplashScreen.class);
			intent.setAction(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			this.startActivity(intent);
			overridePendingTransition(0, 0);
		}
	}
}
