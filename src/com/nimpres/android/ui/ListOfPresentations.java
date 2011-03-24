package com.nimpres.android.ui;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.nimpres.R;
import com.nimpres.android.NimpresObjects;
import com.nimpres.android.lan.PeerStatus;

public class ListOfPresentations extends ListActivity {
	private ArrayList<PeerStatus> item = new ArrayList<PeerStatus>();
	public static PeerStatus peerStatus = null;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_of_presentations);

		// setup Search button listener
		Button searchButton = (Button) findViewById(R.id.lopSearch);
		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d("ListOfPresentations","search clicked");
				EditText presenterID = (EditText) findViewById(R.id.lopPresenterID);
				String presenterIDString = presenterID.getText().toString();
				Log.d("ListOfPresentations","presenter searching: "+presenterIDString);
				Log.d("ListOfPresentations","peer list size: "+NimpresObjects.peerPresentations.size());
				for (int i = 0; i < NimpresObjects.peerPresentations.size(); i++) {
					Log.d("ListOfPresentations","checking: "+NimpresObjects.peerPresentations.get(i).getPresenterName());
					if (presenterIDString.equals(NimpresObjects.peerPresentations.get(i).getPresenterName())) {
						item.add(NimpresObjects.peerPresentations.get(i));
						Log.d("ListOfPresentations","added item to list"+ NimpresObjects.peerPresentations.get(i).getPresentationName());
					}
				}
		    	ArrayAdapter<PeerStatus> presentationList =
		    		new ArrayAdapter<PeerStatus>(view.getContext(), R.layout.row, item);
		    	setListAdapter(presentationList);
		    	if (item.size() == 0) {
		    		TextView notice = (TextView) findViewById(R.id.lopEmpty);
		    		notice.setText("No Presentations are currently behing hosted by that presenter");
		    	}
		    	else {
		    		TextView notice = (TextView) findViewById(R.id.lopEmpty);
		    		notice.setText("");
		    	}
			}
		});
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		ListOfPresentations.peerStatus = item.get(position);

		Builder selected = new AlertDialog.Builder(this);
		
		selected.setIcon(R.drawable.icon);
		selected.setTitle("[" + peerStatus.getPresentationName() + "]");
		selected.setPositiveButton("OK", 
				new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//TODO send info back to join presentation class
						
						NimpresObjects.updateSource = ListOfPresentations.peerStatus.getSource();
						NimpresObjects.presentationID = ListOfPresentations.peerStatus.getPresentationID();
						JoinPresentation.updateID();
					}
				}).show();
		
		selected.setNegativeButton("Cancel", 
				new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//TODO send info back to join presentation class
				dialog.dismiss();
			}
		}).show();
	}
	
}
