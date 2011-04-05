/**
 * Project:			Nimpres Android Client
 * File name: 		
 * Date modified:	2011-04-4
 * Description:		
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

public class Settings extends Activity {

	@Override
	public void onCreate(Bundle created) {
		super.onCreate(created);
		setContentView(R.layout.settings);

		// setup ForgetID button listener
		Button settingsButton = (Button) findViewById(R.id.sForget);
		settingsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO code for forgetting ID
			}
		});

		// setup Settings button listener
		Button backButton = (Button) findViewById(R.id.sBack);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
			}
		});

		// TODO add in code that rewrites the sID textview with "Your ID: ID#"
	}
}
