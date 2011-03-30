/**
 * Project:			Nimpres Android Client
 * File name: 		ExistingAccount.java
 * Date modified:	2011-03-30
 * Description:		Login User Interface
 * 
 * License:			Copyright (c) 2011 (Matthew Brooks, Jordan Emmons, William Kong)
					
					Permission is hereby granted, free of charge, to any person obtaining a copy
					of this software and associated documentation files (the "Software"), to deal
					in the Software without restriction, including without limitation the rights
					to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
					copies of the Software, and to permit persons to whom the Software is
					furnished to do so, subject to the following conditions:
					
					The above copyright notice and this permission notice shall be included in
					all copies or substantial portions of the Software.
					
					THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
					IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
					FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
					AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
					LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
					OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
					THE SOFTWARE.
 */
package com.nimpres.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.nimpres.R;
import com.nimpres.android.NimpresClient;

public class ExistingAccount extends Activity {
	@Override
	public void onCreate(Bundle created) {
		super.onCreate(created);
		setContentView(R.layout.existing_account);

		// setup Login button listener
		Button loginButton = (Button) findViewById(R.id.eaLogin);
		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO login code
				// TODO checking algorithms for the password

				Intent launchview = new Intent(view.getContext(),
						NimpresClient.class);
				startActivity(launchview);
			}
		});

		// setup Back button listener
		Button backButton = (Button) findViewById(R.id.eaBack);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}
}
