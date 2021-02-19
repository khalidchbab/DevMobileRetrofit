package com.khalid.apicalls;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.khalid.apicalls.adapter.DataAdapter;
import com.khalid.apicalls.models.CurrentObservation;
import com.khalid.apicalls.models.ResponseUser;
import com.khalid.apicalls.models.User;
import com.khalid.apicalls.network.GetDataService;
import com.khalid.apicalls.network.RetroClientInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.khalid.apicalls.MESSAGE";
    private DataAdapter adapter;
    private RecyclerView recyclerView;
    ProgressDialog progressDoalog;
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDoalog = new ProgressDialog(MainActivity.this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();
        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = RetroClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<CurrentObservation> call = service.getAllUsers();
        call.enqueue(new Callback<CurrentObservation>() {

            @Override
            public void onResponse(Call<CurrentObservation> call, Response<CurrentObservation> response) {
                progressDoalog.dismiss();
                System.out.println(response);
                CurrentObservation list = response.body();
                List<User> data = list.getUsers();
                generateDataList(data);
            }

            @Override
            public void onFailure(Call<CurrentObservation> call, Throwable t) {
                progressDoalog.dismiss();
                Log.d(TAG,"Im here 2" +t.toString());
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
        Call<ResponseUser> call2 = service.getUser("users/2");
        call2.enqueue(new Callback<ResponseUser>() {

            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                System.out.println(response);
                ResponseUser user = response.body();
                System.out.println("body"+response.body().getUser().getFirst_name());
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {
                Log.d(TAG,"Im here 3" +t.toString());
            }
        });
    }
    private void generateDataList(List<User> usersList) {
        recyclerView = findViewById(R.id.customRecyclerView);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(MainActivity.this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        System.out.println("Clicked here"+ position);

                        Intent intent = new Intent(MainActivity.this, UserActivity.class);
                        String message =Integer.toString(adapter.getItem(position).getId());
                        intent.putExtra(EXTRA_MESSAGE, message);
                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        System.out.println("Long Clicked here");
                    }
                })
        );
        adapter = new DataAdapter(this,usersList);
        Log.d(TAG,usersList.get(0).toString());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
    public void onClick(View view){
        Log.d(TAG,"Clicks here");
    }
}