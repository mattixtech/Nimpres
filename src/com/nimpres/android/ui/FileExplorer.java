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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nimpres.R;
import com.nimpres.android.NimpresObjects;

public class FileExplorer extends ListActivity {
	private List<String> item = null;
	private List<String> path = null;
	private String root = NimpresObjects.ctx.getFilesDir().toString();
	private TextView myPath;

	private void getDir(String dirPath) {
		myPath.setText("Location: " + dirPath);

		item = new ArrayList<String>();
		path = new ArrayList<String>();

		File f = new File(dirPath);
		File[] files = f.listFiles();

		if (!dirPath.equals(root)) {

			item.add(root);
			path.add(root);

			item.add("../");
			path.add(f.getParent());

		}

		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			path.add(file.getPath());
			if (file.isDirectory())
				item.add(file.getName() + "/");
			else
				item.add(file.getName());
		}

		ArrayAdapter<String> fileList = new ArrayAdapter<String>(this, R.layout.row, item);
		setListAdapter(fileList);
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_explorer);
		myPath = (TextView) findViewById(R.id.fePath);
		getDir(root);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		File file = new File(path.get(position));

		if (file.isDirectory()) {
			if (file.canRead())
				getDir(path.get(position));
			else {
				new AlertDialog.Builder(this).setIcon(R.drawable.icon).setTitle("[" + file.getName() + "] folder can't be read!").setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
			}
		}
		else {
			NimpresObjects.hostedPresentationFileName = file.getName();
			new AlertDialog.Builder(this).setIcon(R.drawable.icon).setTitle("[" + file.getName() + "]").setPositiveButton("OK", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					HostPresentation.updateFileName();
					dialog.dismiss();
					Intent intent = new Intent();
					setResult(RESULT_OK, intent);
					finish();
				}
			}).show();
		}

	}
}
