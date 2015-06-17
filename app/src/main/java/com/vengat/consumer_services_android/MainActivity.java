package com.vengat.consumer_services_android;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    public final static String USER_MOBILE_NUMBER = "com.vengat.consumer_services_android.MOBILE_NUMBER";
    public final static String USER_PINCODE = "com.vengat.consumer_services_android.PINCODE";
    public final static String USER_NAME = "com.vengat.consumer_services_android.NAME";

    ImageButton imageButtonServiceProvider, imageButtonCustomer;

    private static final String PREFS = "prefs";
    private static final String NAME = "nameKey";
    private static final String PHONE = "phoneKey";
    private static final String PINCODE = "pincodeKey";
    SharedPreferences mSharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageButtonServiceProvider = (ImageButton) findViewById(R.id.imageButtonSP);
        imageButtonServiceProvider.setOnClickListener(this);

        imageButtonCustomer = (ImageButton) findViewById(R.id.imageButtonCS);
        imageButtonCustomer.setOnClickListener(this);

        getDetails();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.imageButtonSP) {
            Intent intent = new Intent(this, ServiceProviderActivity.class);
            String mobileNumber = mSharedPreferences.getString(PHONE, "");
            intent.putExtra(USER_MOBILE_NUMBER, mobileNumber);
            startActivity(intent);
        } else if (v.getId() == R.id.imageButtonCS) {
            Intent intent = new Intent(this, CustomerActivity.class);
            intent.putExtra(USER_MOBILE_NUMBER,  mSharedPreferences.getString(PHONE, ""));
            intent.putExtra(USER_NAME, mSharedPreferences.getString(NAME, ""));
            intent.putExtra(USER_PINCODE, mSharedPreferences.getString(PINCODE, ""));
            startActivity(intent);
        }

    }

    public void getDetails() {
        // Access the device's key-value storage
        mSharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);

        // Read the user's name,
        // or an empty string if nothing found
        //String name = mSharedPreferences.getString(PREF_NAME, "");

        String mobileNumber = mSharedPreferences.getString(PHONE, "");
        String name = mSharedPreferences.getString(NAME, "");
        String pincode = mSharedPreferences.getString(PINCODE, "");

        if (mobileNumber.length() == 10 && name.length() != 0 && pincode.length() == 6) {

            // If the name is valid, display a Toast welcoming them
            Toast.makeText(this, "Welcome back, " + mobileNumber + "!", Toast.LENGTH_LONG).show();
        } else {

            // otherwise, show a dialog to ask for their name
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

            LayoutInflater inflater = this.getLayoutInflater();

            alert.setTitle("Hello!");
            alert.setMessage("Details please");
            View view = inflater.inflate(R.layout.alert_dialog_welcome, null);
            alert.setView(view);

            final EditText userNameEdit = (EditText) view.findViewById(R.id.username);

            final EditText phoneNumberEdit = (EditText) view.findViewById(R.id.phonenumber);

            final EditText pincodeEdit = (EditText) view.findViewById(R.id.pincode);

            // Create EditText for entry
            //final EditText input = new EditText(this);
            //alert.setView(input);
            //final EditText input = new EditText(this);
            //alert.setView(input);


            // Make an "OK" button to save the name
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {

                    // Grab the EditText's input
                    //String inputName = input.getText().toString();
                    String name = userNameEdit.getText().toString();
                    String mobileNumber = phoneNumberEdit.getText().toString();
                    String pincode = pincodeEdit.getText().toString();

                    // Put it into memory (don't forget to commit!)
                    SharedPreferences.Editor e = mSharedPreferences.edit();
                    //e.putString(PREF_NAME, inputName);
                    e.putString(NAME, name);
                    e.putString(PHONE, mobileNumber);
                    e.putString(PINCODE, pincode);
                    e.commit();

                    // Welcome the new user
                    Toast.makeText(getApplicationContext(), "Welcome, " + mobileNumber + "!", Toast.LENGTH_LONG).show();
                }
            });

            // Make a "Cancel" button
            // that simply dismisses the alert
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {}
            });

            alert.show();
        }
    }
}
