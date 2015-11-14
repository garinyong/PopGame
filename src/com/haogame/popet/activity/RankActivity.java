package com.haogame.popet.activity;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.haogame.popet.PopETApp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class RankActivity extends Activity
{
	
	private ProgressDialog dialog;
	private WebView web;
	private PopETApp app;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		web = new WebView(this);
		setContentView(web);
		app = (PopETApp) getApplicationContext();
		dialog = new ProgressDialog(this);
		dialog.setMessage("loading.....");
		dialog.show();
		WebSettings webSettings = web.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(false);
		web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		web.setWebViewClient(new WebViewClient()
		{
			@Override
			public void onPageFinished(WebView view, String url)
			{
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				dialog.dismiss();
			}
		});
		
		
		new Thread()
		{
			public void run()
			{
				sendRequest();
			}
		}.start();
	}
	
	
	public void sendRequest()
	{
		String sGameId = null;
		try
		{
			Bundle bundle = getPackageManager().getApplicationInfo(
					getPackageName(), PackageManager.GET_META_DATA).metaData;
			sGameId = bundle.getString("appKey");
		}
		catch (NameNotFoundException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try
		{
			
			if (app.isNeedSubmit())
			{
				String sURL = String.format(PopETApp.URL_RANKING_SET,
						app.mPhoneID, app.gamePlayer,
						app.getHighestScore(), 0, sGameId);
				URL aURL = new URL(sURL);
				HttpURLConnection conn = (HttpURLConnection) aURL.openConnection();
				conn.connect();
				if(conn.getResponseCode() == 200)
					app.saveNeedSubmit(false);
			}
			
		}
		catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			String sURL = String.format(PopETApp.URL_RANKING_GET,
							app.mPhoneID, 0, sGameId,
							getPackageName());
			web.loadUrl(sURL);
			
		}
	}
	
}
