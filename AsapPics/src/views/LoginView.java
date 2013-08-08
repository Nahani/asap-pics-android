package views;

import misc.AsyncCallbackResult;

import com.asappics.R;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import async.ConnectUserAsyncCallback;

public class LoginView extends Activity {
	
	private TextView login;
	private TextView pwd;
	private Button loginButton;
	private Button createButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_view);
		
		loginButton = (Button) findViewById(R.id.LoginView_connect_button);
		createButton = (Button) findViewById(R.id.LoginView_create_account_button);
		
		loginButton.setOnClickListener(new LoginOnClickListener());
		createButton.setOnClickListener(new SubscribeOnClickListener());
		
		/* Contrôle les connexions à internet */
	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    
	    if (netInfo == null || !netInfo.isConnectedOrConnecting()) {
	    	AlertDialog dialog = (new AlertDialog.Builder(this)).create();
	    	
	    	dialog.setIcon(R.drawable.ic_about);
	    	dialog.setTitle(getResources().getString(R.string.no_connectivity_title));
	    	dialog.setMessage(getResources().getString(R.string.no_connectivity_message));
	    	
	    	dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
	    		
	    	});
	    	
	    	dialog.show();
	    }
	}
	
	private class LoginOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			
			login = (TextView) findViewById(R.id.LoginView_login_text_field);
			pwd = (TextView) findViewById(R.id.LoginView_password_text_field);
			String stringLogin = login.getText().toString();
			String stringPassword = pwd.getText().toString();
			
			if(stringLogin.equals("") ||  stringPassword.equals("")){
				
				Toast.makeText(getApplicationContext(),  getResources().getString(R.string.blank_fields_message), Toast.LENGTH_LONG).show();
				
			} else {
				
				/* Empêche de multiples créations de threads */
				((Button) findViewById(R.id.LoginView_connect_button)).setEnabled(false);
				AsyncTask<String, Void, Integer> asyncTask = new ConnectUserAsyncCallback(LoginView.this).execute(stringLogin, stringPassword);
				Integer result = AsyncCallbackResult.EXCEPTION;
				
				try {
					result = asyncTask.get();
				} catch (Exception e) {}
				
				if (result == AsyncCallbackResult.TRUE) {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(login.getWindowToken(), 0);
					finish();
				} else {
					((Button) findViewById(R.id.LoginView_connect_button)).setEnabled(true);
				}
			}
		}		
	}
	
	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);		
		builder.setTitle("Quitter l'application")
				.setMessage(this.getResources().getString(R.string.quit_message))
				.setPositiveButton("Oui", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
					
				})
				.setNegativeButton("Non",  new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
					
				})
				.create()
				.show();
	}
	
	private class SubscribeOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(LoginView.this,SubscribeView.class);
			startActivity(intent);
			finish();
		}
	}

}



