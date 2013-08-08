package views;

import misc.AsyncCallbackResult;
import webservices.WebServiceManager;

import business.ASAPUser;

import com.asappics.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SubscribeView extends Activity {

	private Button subscribeButton;
	private EditText loginText;
	private EditText passwordText;
	private EditText firstNameText;
	private EditText lastNameText;
	private EditText emailText;
	
	static final String EXTRA_LOGIN = "user_login";
	static final String EXTRA_ALBUMADD = "is_album_add";
	
	static final Integer TRUE = 1;
	static final Integer FALSE = 0;
	static final Integer EXCEPTION = -1;
	
	/**
	 * Traitement asynchrone de l'appel distant
	 * @author Alex
	 *
	 */
	private class AsyncCallback extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			
			try {
				if (WebServiceManager.add_user(firstNameText.getText().toString(), lastNameText.getText().toString(), loginText.getText().toString(), emailText.getText().toString(),passwordText.getText().toString())) {
					ASAPUser.id = WebServiceManager.get_user_ID(loginText.getText().toString());
					ASAPUser.login = loginText.getText().toString();
					return AsyncCallbackResult.TRUE;
				} else {
					return AsyncCallbackResult.FALSE;
				}
			} catch(Exception e) {
				return AsyncCallbackResult.EXCEPTION;
			}
		}
	}
	
	private class SubscribeButtonListener implements OnClickListener {
		
		@Override
		public void onClick(View v) {
	        
	        if(loginText.getText().toString().equals("") | passwordText.getText().toString().equals("") | firstNameText.getText().toString().equals("") | lastNameText.getText().toString().equals("") | emailText.getText().toString().equals("")) {

				Toast.makeText(getApplicationContext(),  getResources().getString(R.string.blank_fields_message), Toast.LENGTH_LONG).show();
				
	        } else {
	        	
	        	AsyncTask<Void, Void, Integer> asyncTask = (new AsyncCallback()).execute();
	        	Integer ajout = AsyncCallbackResult.EXCEPTION;
	        	
				try {
					ajout = asyncTask.get();
				} catch (Exception e) {
					Log.e("SubscribeAsyncTaskException", "Subscribe.class: " + e.toString());
				}
	        	
	        	if(ajout == AsyncCallbackResult.TRUE) {

	        		Intent intent = new Intent(SubscribeView.this, Album_View.class);					
					startActivity(intent);

					Toast.makeText(getApplicationContext(), getResources().getString(R.string.account_created_message), Toast.LENGTH_LONG).show();
					
					finish();
					
	        	} else if (ajout == AsyncCallbackResult.FALSE) {
	        		
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.login_already_used_message), Toast.LENGTH_LONG).show();

	        	} else if (ajout == AsyncCallbackResult.EXCEPTION) {
	        		
	        		Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_network_error), Toast.LENGTH_LONG).show();
	        		
	        	}
	        }
		}
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe_window);
        
        subscribeButton = (Button) findViewById(R.id.subscribe_button);
        loginText = (EditText) findViewById(R.id.LoginView_login_text_field);
        passwordText = (EditText) findViewById(R.id.password_text_field);
        firstNameText = (EditText) findViewById(R.id.first_name_text_field);
        lastNameText = (EditText) findViewById(R.id.last_name_text_field);
        emailText = (EditText) findViewById(R.id.email_text_field);
        
        subscribeButton.setOnClickListener(new SubscribeButtonListener());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.subscribe_view_menu, menu);
        return true;
    }
    
}
