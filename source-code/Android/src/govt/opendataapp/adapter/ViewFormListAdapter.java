package govt.opendataapp.adapter;

import govt.opendataapp.HTMLPageActivity;
import govt.opendataapp.R;
import govt.opendataapp.utils.Constants;
import govt.opendataapp.utils.FormList;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewFormListAdapter extends ArrayAdapter<FormList>{

		
	// declaring our ArrayList of items
		private ArrayList<FormList> formLists;
		private Context mctx;
		private SharedPreferences applicationPreferences;

		/* here we must override the constructor for ArrayAdapter
		* the only variable we care about now is ArrayList<Item> objects,
		* because it is the list of objects we want to display.
		*/
		public ViewFormListAdapter(Context context, int textViewResourceId, ArrayList<FormList> objects) {
			super(context, textViewResourceId, objects);
			this.formLists = objects;
			mctx = context;
		}

		/*
		 * we are overriding the getView method here - this is what defines how each
		 * list item will look.
		 */
		public View getView(final int position, View convertView, ViewGroup parent){

			// assign the view we are converting to a local variable
			View listItemView = convertView;

			// first check to see if the view is null. if so, we have to inflate it.
			// to inflate it basically means to render, or show, the view.
			if (listItemView == null) {
				LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				listItemView = inflater.inflate(R.layout.list_item, null);
			}
			
			if(position==0){
				listItemView.setBackgroundDrawable(mctx.getResources().getDrawable(R.drawable.silverlistviewselector));  
			}else if (position % 2 == 1) {
				listItemView.setBackgroundDrawable(mctx.getResources().getDrawable(R.drawable.bluelistviewselector));  
			} else {
				listItemView.setBackgroundDrawable(mctx.getResources().getDrawable(R.drawable.silverlistviewselector));  
			}

			/*
			 * Recall that the variable position is sent in as an argument to this method.
			 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
			 * iterates through the list we sent it)
			 * 
			 * Therefore, i refers to the current Item object.
			 */
			FormList currentForm = formLists.get(position);

			if (currentForm != null) {

				// This is how you obtain a reference to the TextViews.
				// These TextViews are created in the XML files we defined.

				TextView fileName = (TextView) listItemView.findViewById(R.id.lefttext);
				TextView fileCreatedDate = (TextView) listItemView.findViewById(R.id.righttext);
				RelativeLayout mRelatativeLayout = (RelativeLayout)listItemView.findViewById(R.id.listLayout);
				
				
				applicationPreferences = (SharedPreferences)mctx.getSharedPreferences(Constants.LOGGED_IN_USER_PREFERENCES_NAME, Context.MODE_PRIVATE);
				String isFormDownloaded = applicationPreferences.getString(Constants.PREF_URL_OR_DOWNLOADED, "");
				if(isFormDownloaded.equals(Constants.PREF_URL))
					fileCreatedDate.setVisibility(View.GONE);
				
				// check to see if each individual textview is null.
				// if not, assign some text!
//				if (fileName != null){
					String name = currentForm.getFormName();

					if(name.contains(".")){
						String substr = name.substring(0, name.indexOf("."));
						fileName.setText(substr);
					}
					else{
						fileName.setText(name);
					}
//					fileName.setText(substr);
//				}
				if (currentForm.getFormDownloadTime() != Constants.SEARCH_FORM){

					if(name.contains(".")){
						String substr = name.substring(0, name.indexOf("."));
						fileName.setText(substr);
					}
					else{
						fileName.setText(name);
					}
				}
				if (fileCreatedDate != null){

					long millisecond = currentForm.getFormDownloadTime();
					String dateString= DateFormat.format(Constants.DATE_FORMAT, new Date(millisecond)).toString();
					fileCreatedDate.setText(dateString);
				}
				else{
					fileCreatedDate.setVisibility(View.GONE);
				}

				mRelatativeLayout.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						FormList selectedForm = formLists.get(position);
						
						String name = selectedForm.getFormName();
						Log.i("NAME",name);
						String substr = name;
						if(name.contains(".")){
							substr = name.substring(name.indexOf(".")+1,name.length());
						}
					
						if(substr.equals(Constants.PDF_KEY)){
							File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+Constants.PDF_PATH+name);
							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.setDataAndType(Uri.fromFile(file), Constants.FILE_FORMAT);
							intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
							mctx.startActivity(intent);
						}else{
							Intent intent = new Intent(mctx,HTMLPageActivity.class);
							intent.putExtra(Constants.FORM_NAME, name);
							intent.putExtra(Constants.FORM_LOCATION, selectedForm.getFormLocation());
							mctx.startActivity(intent);
						}					
					}
				});
			}		

			// the view must be returned to our activity
			return listItemView;

		}
}
