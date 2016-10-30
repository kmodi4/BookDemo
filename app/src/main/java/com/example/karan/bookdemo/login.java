package com.example.karan.bookdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Arrays;


public class login extends AppCompatActivity implements  GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener,GoogleApiClient.ConnectionCallbacks,MyServer {

    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;
    private Boolean status = false;
    private Button signup;
    LoginButton loginButton;
    Profile newProfile;
    private CallbackManager callbackManager;
    private boolean mIntentInProgress;
    private boolean signedInUser;
    private ConnectionResult mConnectionResult;
    private SignInButton signinButton;
    private LinearLayout profileFrame, signinFrame;
    private ImageView image;
    private TextView username, emailLabel,mStatusTextView;
    private Button signout,login;
    private EditText user,pass;
    private static final String LOGIN_URL = MyServerUrl+"login.php";    //url of your php file
    RequestQueue mQueue11;
    private ProgressDialog pDialog;
    String personEmail = "";
    String personId = "";
    String personPhoto="";
    String pname="";
    private Boolean gl;
    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;
    ProfilePictureView profilePictureView;
    private SharedPreferences sharedPreferences;
    private SharedPreferences sharedPreferences2;
    private SharedPreferences.Editor editor;
    private SharedPreferences.Editor editor2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_detail);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            //getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            //getSupportActionBar().setTitle(getTitle());
        }*/
        loginButton = (LoginButton) findViewById(R.id.login_button);
        //image = (ImageView) findViewById(R.id.image10);
        username = (TextView) findViewById(R.id.username);

        //profileFrame = (LinearLayout) findViewById(R.id.profileFrame);
        signinButton = (SignInButton) findViewById(R.id.gsignin);
        signout = (Button) findViewById(R.id.logout);
        login = (Button) findViewById(R.id.btnLogin);
        user = (EditText) findViewById(R.id.name);
        pass = (EditText) findViewById(R.id.password);
        signup = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(login.this, signup.class);
                startActivity(i);
                // finish();
            }
        });

        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        sharedPreferences2 = getSharedPreferences("UserDetail", Context.MODE_PRIVATE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        Log.d("gApi","Start");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                 .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //mGoogleApiClient.stopAutoManage(this);
        gl = sharedPreferences.getBoolean("gout", false);
        Log.i("oncrete GL:", String.valueOf(gl));


        signinButton.setSize(SignInButton.SIZE_STANDARD);
        signinButton.setScopes(gso.getScopeArray());

        //mStatusTextView = (TextView) findViewById(R.id.TEXTVIEW_ID_HERE);


        MyVolley.init(this);
        mQueue11 = MyVolley.getRequestQueue();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startprogress();
                volleyconnect();
            }
        });

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldprofile, Profile newprofile) {
                newProfile = newprofile;
                getProfileData(newprofile);

            }
        };
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                AccessToken accessToken = loginResult.getAccessToken();
                //Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                final SharedPreferences sp2 = getSharedPreferences("offlineprofile",Context.MODE_PRIVATE);
                final SharedPreferences.Editor ed2 = sp2.edit();
                Profile profile = Profile.getCurrentProfile();
                if (profile != null) {
                    //tv.setText("Welcome " + profile.getName());
                }

                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {


                                // Application code

                                try {
                                    if(object!=null) {
                                        Log.v("JsonResp:", object.toString());
                                        String email = object.getString("email");
                                        String id = object.getString("id");
                                        String name = object.getString("name");
                                        String userid = "";
                                        int index = name.indexOf(" ");
                                        if(index != -1){
                                            userid = name.substring(0,index);
                                        }
                                        else {
                                            userid = name;
                                        }
                                        int len = id.length();
                                        len = len - 4;
                                        userid = userid + id.substring(len);
                                        String url = "https://graph.facebook.com/"+id+"/picture?height=100&width=100";
                                        ed2.putString("Name",name);
                                        ed2.putString("EmailId",email);
                                       // ed2.putString("Phoneno",response.getString("Phoneno"));
                                        ed2.putString("username",userid);
                                        ed2.putString("url",url);
                                        ed2.apply();
                                        // String birthday = object.getString("birthday"); // 01/31/1980 format
                                        Toast.makeText(getApplicationContext(), email, Toast.LENGTH_SHORT).show();

                                        editor = sharedPreferences.edit();
                                        editor2 = sharedPreferences2.edit();

                                        editor.putString("url",url);
                                        editor.putBoolean("LStatus", true);
                                        editor.putBoolean("fblogin",true);
                                        editor2.putString("sellerid",userid);
                                        editor2.putString("username",name);
                                        editor.apply();
                                        editor2.apply();
                                        stopProgressDialog();
                                        Intent i= new Intent(com.example.karan.bookdemo.login.this,MainActivity.class);
                                        i.putExtra("name",name);
                                        startActivity(i);
                                        finish();

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    stopProgressDialog();
                                    Toast.makeText(getApplicationContext(),"cant get info",Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                startprogress();
                request.executeAsync();



            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.e("Err:", exception.getCause().toString());
            }
        });
        accessTokenTracker.startTracking();
        profileTracker.startTracking();

    }

    private void stopAutoManage() {
        if (mGoogleApiClient != null)
            mGoogleApiClient.stopAutoManage(login.this);
    }

    public void getProfileData(Profile newprofile){
        if(newprofile!=null) {
           // tv.setText("Welcome " + newprofile.getName());
            //Toast.makeText(getApplicationContext(), newprofile.getLinkUri().toString(), Toast.LENGTH_LONG).show();
            //String url = "https://graph.facebook.com/" + newprofile.getId()+ "/picture?type=small";
            String url = newprofile.getProfilePictureUri(100,100).toString();

            Log.e("Url:",url);
           // Glide.with(getApplicationContext()).load(url).crossFade().into(iv);
           // profilePictureView.setProfileId(newprofile.getId());
        }
        else {
           // iv.setImageDrawable(null);
           // profilePictureView.setProfileId(null);
           // tv.setText("Welcome to FB");
        }

    }

    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

            }
        }).executeAsync();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
        disconnectFromFacebook();

    }

    @Override
    protected void onStop() {
        super.onStop();
       // signOut();
        Log.e("onstop:",String.valueOf(mGoogleApiClient.isConnected()));
    }

    @Override
    public void onStart() {
        super.onStart();


        // gl = sharedPreferences.getBoolean("g+",false);

          /*  OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
            if (opr.isDone()) {
                // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
                // and the GoogleSignInResult will be available instantly.
                Log.d("TAG", "Got cached sign-in");
                GoogleSignInResult result = opr.get();
                handleSignInResult(result);

            } else {
                // If the user has not previously signed in on this device or the sign-in has expired,
                // this asynchronous branch will attempt to sign in the user silently.  Cross-device
                // single sign-on will occur in this branch.
                startprogress();
                opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                    @Override
                    public void onResult(GoogleSignInResult googleSignInResult) {
                        stopProgressDialog();
                        handleSignInResult(googleSignInResult);
                    }
                });
            }*/



    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("TAG", "handleSignInResult:" + result.isSuccess());



        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            editor = sharedPreferences.edit();
            editor2 = sharedPreferences2.edit();

            final SharedPreferences sp2 = getSharedPreferences("offlineprofile",Context.MODE_PRIVATE);
            final SharedPreferences.Editor ed2 = sp2.edit();
            try {
                pname=acct.getDisplayName();
                int index = pname.indexOf(" ");
                String userid = "";
                if( index != -1) {
                     userid = pname.substring(0,index);

                }
                else {
                    userid = pname;
                }
                personId = acct.getId();
                editor.putBoolean("LStatus", true);
                editor.putBoolean("g+",true);
                personEmail = acct.getEmail();
                //personId = personId.substring(0,4);
                int len = personId.length();
                len = len - 4;
                userid = userid + personId.substring(len);
                ed2.putString("Name",pname);
                ed2.putString("EmailId",personEmail);
                // ed2.putString("Phoneno",response.getString("Phoneno"));
                ed2.putString("username",userid);

                editor2.putString("sellerid",userid);
                editor2.putString("username",pname);
                editor2.apply();
                editor.apply();
                ed2.apply();
                if(acct.getPhotoUrl()!=null){
                    personPhoto = acct.getPhotoUrl().toString();
                    editor.putString("url",personPhoto);
                    ed2.putString("url",personPhoto);
                }
                editor.apply();
                ed2.apply();

               // mStatusTextView.setText(getString(R.string.signed_in_fmt,pname ));
            }catch (NullPointerException nl){
                Toast.makeText(getApplicationContext(),"No Profile-Pic Found", Toast.LENGTH_SHORT).show();
            }


            Toast.makeText(getApplicationContext(),personEmail+"\n"+personId+"\n"+pname,Toast.LENGTH_LONG).show();
            if(!(personPhoto.equals(""))){
               // Glide.with(this).load(personPhoto).crossFade().into(img);
                Log.i("photoUrl:",personPhoto);
            }
            //new LoadProfileImage(img).execute(personPhoto);
           Log.e("G+",String.valueOf(sharedPreferences.getBoolean("g+",false)));
            updateUI(true);
            boolean gout = sharedPreferences.getBoolean("gout",false);
            Log.d("got:",String.valueOf(gout));
           /* if (gout){
                editor.putBoolean("gout",false);

                editor.apply();
                //signOut();

            }*/
           // signOut();
            if (mGoogleApiClient.isConnected()) {
                Intent i = new Intent(login.this, MainActivity.class);
                signOut();
                //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("name", personEmail);
                // i.putExtra("url",personPhoto);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                startActivity(i);
            }


        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }
    // [END handleSignInResult]

    // [START signIn]
    private void signIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        Toast.makeText(getApplicationContext(),"Signing",Toast.LENGTH_SHORT).show();
    }
    // [END signIn]

    // [START signOut]
    public void signOut() {



           // if(mGoogleApiClient.isConnected()){

                Log.e("before soingout",String.valueOf(mGoogleApiClient.isConnected()));
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            // [START_EXCLUDE]
                            updateUI(false);
                          /*  Intent i = new Intent(login.this, MainActivity.class);
                            //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.putExtra("name", personEmail);
                            // i.putExtra("url",personPhoto);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                            startActivity(i);
                            finish();*/
                            // }
                            // [END_EXCLUDE]
                        }
                    });
        //}

        Log.e("After soingout",String.valueOf(mGoogleApiClient.isConnected()));
    }
    // [END signOut]

    // [START revokeAccess]
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d("result",String.valueOf(result.getStatus()));
            handleSignInResult(result);
        }
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void startprogress(){
        pDialog = new ProgressDialog(login.this);

        pDialog.setMessage("Attempting for Login...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    private void stopProgressDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }




    public String getstr(EditText et){
        String st1 = et.getText().toString();
        return st1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    private void volleyconnect(){
        final String username=getstr(user);
        String password=getstr(pass);
         editor = sharedPreferences.edit();

        JSONObject jo = new JSONObject();
        try {
            jo.put("username",username);
            jo.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

       editor2 = sharedPreferences2.edit();
        final SharedPreferences sp2 = getSharedPreferences("offlineprofile",Context.MODE_PRIVATE);
        final SharedPreferences.Editor ed2 = sp2.edit();

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                LOGIN_URL,
                jo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            Toast.makeText(getApplicationContext(),response.getString("message"),Toast.LENGTH_SHORT).show();
                            int success = response.getInt("success");

                            if(success==1) {
                                Intent i = new Intent(login.this, MainActivity.class);
                                //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                ed2.putString("Name",response.getString("Name"));
                                ed2.putString("EmailId",response.getString("EmailId"));
                                ed2.putString("Phoneno",response.getString("Phoneno"));
                                ed2.putString("username",username);
                                ed2.apply();
                                i.putExtra("name",username);
                                editor2.putString("sellerid",username);
                                editor2.putString("username",username);
                                editor2.apply();
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                editor.putBoolean("LStatus", true);
                                editor.putBoolean("g+",false);
                                editor.apply();
                                startActivity(i);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    },new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Unknow Error",Toast.LENGTH_SHORT).show();

                        }

                        });


        mQueue11.add(myReq);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void updateProfile(boolean isSignedIn) {
        if (isSignedIn) {
            //signinFrame.setVisibility(View.GONE);
            profileFrame.setVisibility(View.VISIBLE);

        } else {
            //signinFrame.setVisibility(View.VISIBLE);
            profileFrame.setVisibility(View.GONE);
        }

    }




    private void updateUI(boolean signedIn) {
        if (signedIn) {
           signinButton.setVisibility(View.GONE);
           signout.setVisibility(View.VISIBLE);
        } else {
            //mStatusTextView.setText(R.string.signed_out);

            signinButton.setVisibility(View.VISIBLE);
            signout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gsignin:
                signIn();
                break;
            case R.id.logout:
                signOut();
                break;
            /*case R.id.disconnect_button:
                revokeAccess();
                break;*/
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    // download Google Account profile image, to complete profile
    private class LoadProfileImage extends AsyncTask<String,Void,Bitmap> {
        ImageView downloadedImage;

        public LoadProfileImage(ImageView image) {
            this.downloadedImage = image;
        }
        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap icon = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                icon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return icon;
        }

        protected void onPostExecute(Bitmap result) {
            downloadedImage.setImageBitmap(result);
        }



    }
}
