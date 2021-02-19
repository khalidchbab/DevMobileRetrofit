package com.khalid.apicalls;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.khalid.apicalls.models.ResponseUser;
import com.khalid.apicalls.network.GetDataService;
import com.khalid.apicalls.network.RetroClientInstance;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity {
    ProgressDialog progressDoalog;
    private static final String TAG = "userActivity";
    private ImageView coverImage;
    TextView firstname;
    TextView lastname;
    TextView email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        firstname = findViewById(R.id.firstnameVar);
        lastname = findViewById(R.id.lastnameVar);
        email = findViewById(R.id.emailVar);
        coverImage = findViewById(R.id.avatar);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        System.out.println("Im heeeeeeeeeeeeeeeeeeeeere"+message);

        progressDoalog = new ProgressDialog(UserActivity.this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();
        String url = "users/"+message;
        System.out.println(url);
        GetDataService service = RetroClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<ResponseUser> call2 = service.getUser(url);
        call2.enqueue(new Callback<ResponseUser>() {

            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                progressDoalog.dismiss();
                System.out.println(response);
                ResponseUser user = response.body();
                System.out.println("body"+response.body().getUser().getFirst_name());
                firstname.setText(response.body().getUser().getFirst_name());
                lastname.setText(response.body().getUser().getLast_name());
                email.setText(response.body().getUser().getEmail());
                Picasso.Builder builder = new Picasso.Builder(UserActivity.this);
                builder.downloader(new OkHttp3Downloader(UserActivity.this));
                builder.build().load(response.body().getUser().getAvatar())
                        .placeholder((R.drawable.ic_launcher_background))
                        .error(R.drawable.ic_launcher_background)
                        .into(coverImage);
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {
                Log.d(TAG,"Im here 3" +t.toString());
            }
        });

    }
    public void onClick(View v){
        finish();
    }
}