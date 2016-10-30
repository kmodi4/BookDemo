package com.example.karan.bookdemo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.example.karan.bookdemo.chat.ChatMessage;
import com.example.karan.bookdemo.chat.ChatMessageAdapter;
import com.example.karan.bookdemo.chat.MyApplication;
import com.example.karan.bookdemo.chat.QuickstartPreferences;
import com.example.karan.bookdemo.chat.RegistrationIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class ChatList extends AppCompatActivity implements MyServer {

    private ListView mListView;
    private Button mButtonSend;
    private ProgressDialog pDialog;
    private AlertDialog dialog;
    private EditText mEditTextMessage;
    private ImageView mImageView;
    private ChatMessageAdapter mAdapter;
    RequestQueue mQueue11;
    private String Sender;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "ChatActivity";
    private String Receiver = "";
    private String regid = "";
    private static final String LOGIN_URL = MyServerUrl+"Chatgcm.php";
    private BroadcastReceiver broadcastReceiver;
    private SharedPreferences sp;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private SharedPreferences sp2;
    private SharedPreferences.Editor editor;
    private SharedPreferences.Editor editor2;
    private String username;
    private boolean isReceiverRegistered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);


        mListView = (ListView) findViewById(R.id.listView);
        mButtonSend = (Button) findViewById(R.id.btn_send);
        mEditTextMessage = (EditText) findViewById(R.id.et_message);
        mImageView = (ImageView) findViewById(R.id.iv_image);

        mAdapter = new ChatMessageAdapter(this, new ArrayList<ChatMessage>());
        mListView.setAdapter(mAdapter);
        MyVolley.init(this);
        mQueue11 = MyVolley.getRequestQueue();

        final Bundle b = getIntent().getExtras();
        if(b!=null){
            Receiver = b.getString("seller");
        }

        sp = getSharedPreferences("gcmDetails", Context.MODE_PRIVATE);


        sp2 = getSharedPreferences("UserDetail",Context.MODE_PRIVATE);
        username = sp2.getString("sellerid","");
        Toast.makeText(getApplicationContext(), username, Toast.LENGTH_SHORT).show();
        onnotifiCLick();

        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mEditTextMessage.getText().toString();
                Sender = username;
                if (TextUtils.isEmpty(message)) {
                    return;
                }


                        if (Receiver.equals("")) {
                            Toast.makeText(getApplicationContext(), "Sending Problem", Toast.LENGTH_SHORT).show();
                        } else {
                            sendMessage(message);
                            Log.d("Sender:",Sender);
                            Log.d("rec",Receiver);
                            message = Sender + ":"+ message;
                            volleyRequest(Receiver, message);
                            mEditTextMessage.setText("");
                            scrollMyListViewToBottom();
                        }
                    }




        });

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String message = intent.getStringExtra("data");
                Receiver = intent.getStringExtra("sender");
                ChatMessage chatMessage = new ChatMessage(message, false, false);
                mAdapter.add(chatMessage);
                    scrollMyListViewToBottom();
                //tv1.setText(message);

            }
        };

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
               // mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                   // Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
                    regid = intent.getStringExtra("regid");
                   // Toast.makeText(getApplicationContext(),"size is "+regid.length(),Toast.LENGTH_SHORT).show();
                    regToserver(regid);
                    // mInformationTextView.setText(getString(R.string.gcm_send_message));
                } else {
                    //mInformationTextView.setText(getString(R.string.token_error_message));
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        };

        registerReceiver();

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        dialog = new SpotsDialog(this, R.style.Custom);
        dialog.show();


    }

    public void regToserver(String regid){
        String rid = sp.getString("Regid","");
        if(rid.equals("")) {
            editor = sp.edit();
            editor.putString("Regid", regid);
            editor.apply();

            if(username.equals("")){
                Toast.makeText(getApplicationContext(),"Invalide user id",Toast.LENGTH_SHORT).show();
                editor.putString("Regid", "");
                dialog.dismiss();
            }
            else {
                volleyconnect(regid,username);
            }
        }
        else{
            if(!(rid.equals(regid))){
                editor = sp.edit();
                editor.putString("Regid", regid);
                editor.apply();
                //voley2
                dialog.dismiss();
            }
            else {
                dialog.dismiss();
            }
        }
    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void startprogress(){
        pDialog = new ProgressDialog(ChatList.this);

        pDialog.setMessage("Attempting for Registrating...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    private void scrollMyListViewToBottom() {
        mListView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                mListView.setSelection(mAdapter.getCount() - 1);
            }
        });
    }

    private void sendMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, true, false);
        mAdapter.add(chatMessage);

        //mimicOtherMessage(message);
    }

    private void mimicOtherMessage(String message) {

        ChatMessage chatMessage = new ChatMessage(message, false, false);
        mAdapter.add(chatMessage);
    }



    private void ReceiveFromOther(String message){
        //String message = getIntent().getStringExtra("data");
        ChatMessage chatMessage = new ChatMessage(message, false, false);
        mAdapter.add(chatMessage);

    }

    private void sendMessage() {
        ChatMessage chatMessage = new ChatMessage(null, true, true);
        mAdapter.add(chatMessage);

        mimicOtherMessage();
    }

    private void mimicOtherMessage() {
        ChatMessage chatMessage = new ChatMessage(null, false, true);
        mAdapter.add(chatMessage);
    }

    public void onnotifiCLick(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("data")) {
                //setContentView(R.layout.viewmain);
                // extract the extra-data in the Notification
                String msg = extras.getString("data");
                Receiver = extras.getString("sender");


                Log.i("newIntent", msg);

                ChatMessage chatMessage = new ChatMessage(msg, false, false);
                mAdapter.add(chatMessage);
                //tv1.setText(msg);
            }
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.activityResumed();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("pushmsg"));
        registerReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApplication.activityPaused();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
    }

    @Override
    public void onNewIntent(Intent intent) {
        Bundle extras = intent.getExtras();

       // Toast.makeText(getApplicationContext(),"OnNew Inntent",Toast.LENGTH_SHORT).show();
        if (extras != null) {
            if (extras.containsKey("data")) {
                //setContentView(R.layout.viewmain);
                // extract the extra-data in the Notification
                String msg = extras.getString("data");
                Receiver = extras.getString("sender");
                Log.i("newIntent", msg);

                ChatMessage chatMessage = new ChatMessage(msg, false, false);
                mAdapter.add(chatMessage);
                //tv1.setText(msg);
            }
        }
    }

    public void volleyRequest(String username,String msg){

        JSONObject jo = new JSONObject();

        Log.i("name:", username);
        try {
            jo.put("name",username);
            jo.put("msg",msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                LOGIN_URL,
                jo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            int i = response.getInt("success");
                            //String msg = response.getString("message");
                            if(i==0){
                                Toast.makeText(getApplicationContext(),"invalid Registration",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // pDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Unknow Error",Toast.LENGTH_SHORT).show();

            }

        });
        mQueue11.add(myReq);

    }

    public void volleyconnect(String regid, String name){
        final String LOGIN_URL = MyServerUrl+"registerId.php";
        JSONObject jo = new JSONObject();
        try {
            jo.put("regid", regid);
            jo.put("name", name);
            Log.i("json:",jo.getString("regid"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(LOGIN_URL, jo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //VolleyLog.v("Response:%n %s", response.toString(4));
                            dialog.dismiss();
                            int i = response.getInt("success");
                            String msg = response.getString("message");
                            if (i == 1) {
                                /*Intent ii = new Intent(register.this, MainActivity.class);
                                finish();
                                // this finish() method is used to tell android os that we are done with current //activity now! Moving to other activity
                                startActivity(ii);*/
                            }


                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Log.i("Eror:",error.toString());
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Unknown Error", Toast.LENGTH_SHORT).show();

            }
        });
        mQueue11.add(req);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat_list, menu);
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


    public void onDeviceRegisted(String s) {

        regid = s;
        Log.d("regid:",regid);
        String rid = sp.getString("Regid","");
        if(rid.equals("")) {
            editor = sp.edit();
            editor.putString("Regid", regid);
            editor.apply();

            if(username.equals("")){
                Toast.makeText(getApplicationContext(),"Invalide user id",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
            else {
                volleyconnect(regid,username);
            }
        }
        else{
            if(!(rid.equals(regid))){
                editor = sp.edit();
                editor.putString("Regid", regid);
                editor.apply();
                //voley2
                dialog.dismiss();
            }
            else {
                dialog.dismiss();
            }
        }

    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

}
