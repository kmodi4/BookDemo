package com.example.karan.bookdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.example.karan.bookdemo.chat.ChatMessage;
import com.example.karan.bookdemo.chat.ChatMessageAdapter;
import com.example.karan.bookdemo.chat.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class ChatList extends AppCompatActivity {

    private ListView mListView;
    private Button mButtonSend;
    private EditText mEditTextMessage;
    private ImageView mImageView;
    private ChatMessageAdapter mAdapter;
    RequestQueue mQueue11;
    private String Sender;
    private String Receiver = "";
    private static final String LOGIN_URL = "http://kmodi4.net76.net/GcmMsg.php";
    private BroadcastReceiver mRegistrationBroadcastReceiver;


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

        SharedPreferences prefs = getSharedPreferences("UserDetails",
                Context.MODE_PRIVATE);
        Sender = prefs.getString("Name", "");
        onnotifiCLick();

        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mEditTextMessage.getText().toString();
                if (TextUtils.isEmpty(message)) {
                    return;
                }

                Bundle b = getIntent().getExtras();
                if(b!=null) {
                    if (b.containsKey("name")) {

                        sendMessage(message);
                        message = Sender + " "+ message;
                        String name = getIntent().getStringExtra("name");
                        volleyRequest(name, message);
                        mEditTextMessage.setText("");
                        scrollMyListViewToBottom();
                    }
                    else {

                        if (Receiver.equals("")) {
                            Toast.makeText(getApplicationContext(), "Sending Problem", Toast.LENGTH_SHORT).show();
                        } else {
                            sendMessage(message);
                            message = Sender + " "+ message;
                            volleyRequest(Receiver, message);
                            mEditTextMessage.setText("");
                            scrollMyListViewToBottom();
                        }
                    }
                }
                else {
                        Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_SHORT).show();
                }
            }

        });

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
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
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter("pushmsg"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApplication.activityPaused();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    @Override
    public void onNewIntent(Intent intent) {
        Bundle extras = intent.getExtras();
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
}
