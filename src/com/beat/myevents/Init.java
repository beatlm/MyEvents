package com.beat.myevents;

import java.util.Arrays;

import com.facebook.FacebookException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class Init extends ActionBarActivity {

	private String TAG = "MainActivity";

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_init);

		LoginButton authButton = (LoginButton) findViewById(R.id.authButton);

		// session state call back event
	//	authButton.setReadPermissions(Arrays
		//		.asList("user_likes", "user_status"));

		authButton.setSessionStatusCallback(new Session.StatusCallback() {

			@Override
			public void call(Session session, SessionState state,
					Exception exception) {

				if (session.isOpened()) {

					Log.i(TAG, "Access Token" + session.getAccessToken());

					Request.newMeRequest(session,

					new Request.GraphUserCallback() {

						@Override
						public void onCompleted(GraphUser user,
								Response response) {

							if (user != null) {
						
								Log.i(TAG, "User ID " + user.getId());

								Log.i(TAG, "Email " + user.asMap().get("email"));
 
								Intent i = new Intent(Init.this,
										MenuActivity.class);
								i.putExtra(Constants.USERNAME, user.getName());

								startActivity(i);
							}

						}

					}).executeAsync();

				}

			}

		});

	}

	public void logar(View v) {
		Log.d("beatlm", "Entramos en logar");
		EditText e = (EditText) findViewById(R.id.textUser);
		String user = e.getText().toString();

		Log.d("beatlm", user);

		Intent intent = new Intent(this, MenuActivity.class);
		intent.putExtra(Constants.USERNAME, user);

		startActivity(intent);

	}

}
