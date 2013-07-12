package async;

import misc.AsyncCallbackResult;
import views.Album_View;
import webservices.WebServiceManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import business.ASAPUser;

import com.example.asappics.R;

/**
 * Connexion au serveur. Contrôle des coordonnées utilisateur.
 * @author Alex
 *
 */
public class ConnectUserAsyncCallback extends AsyncTask<String, Void, Integer> {

	private ProgressDialog progressDialog;
	private Context context;
	
	public ConnectUserAsyncCallback(Context context) {
		this.context = context;
	}
	
	@Override
	protected void onPreExecute() {
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage("Chargement des informations");
		progressDialog.show();
	}
	
	@Override
	protected Integer doInBackground(String... params) {
			
			try {
				if (WebServiceManager.check_password(params[0], params[1])) {
					
					ASAPUser.id = WebServiceManager.get_user_ID(params[0]);
					ASAPUser.login = params[0];
					
					return AsyncCallbackResult.TRUE;
				} else {
					return AsyncCallbackResult.FALSE;
				}
			} catch (Exception e) {
				Log.e("AsyncTaskException", "LoginView.class: " + e.toString());
				return AsyncCallbackResult.EXCEPTION;
			}
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		
		if (progressDialog.isShowing())
			progressDialog.dismiss();
		
		if (result == AsyncCallbackResult.TRUE) {
			
			/* Ouvre la vue suivante et termine celle en cours */
			Intent intent = new Intent(context, Album_View.class);
			context.startActivity(intent);
			
		} else if (result == AsyncCallbackResult.FALSE) {

			Toast.makeText(context,  context.getResources().getString(R.string.login_error_message), Toast.LENGTH_LONG).show();
			
		} else if (result == AsyncCallbackResult.EXCEPTION) {
			
			Toast.makeText(context, context.getResources().getString(R.string.no_network_error), Toast.LENGTH_LONG).show();
			
		}
	}
	
}
