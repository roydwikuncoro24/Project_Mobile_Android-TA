package com.example.stocki.AktifitasAdmin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.stocki.Adapter.AdminMitraAdapter;
import com.example.stocki.Adapter.TokoKaryawanAdapter;
import com.example.stocki.AktifitasToko.TokoManajemenKaryawan;
import com.example.stocki.AktifitasToko.TokoTambahKaryawan;
import com.example.stocki.ModelData.AdminModelData;
import com.example.stocki.ModelData.TokoModelData;
import com.example.stocki.ModelResponse.GetMitraNKaryawan;
import com.example.stocki.R;
import com.example.stocki.helper.TinyDB;
import com.example.stocki.service.APIService;
import com.example.stocki.service.RetrofitHelper;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminManajemenMitra extends AppCompatActivity {

    AdminMitraAdapter mAdapter;
    List<TokoModelData> mdToko = new ArrayList<>();
    FloatingActionButton fab;
    RecyclerView recyclerView;
    LinearLayout eror;
    SwipeRefreshLayout swipeContainer;
    Button btnCobalagi;
    SpotsDialog dialog;
    ProgressBar loadingbar;
    TinyDB tinyDB;
    CardView loadingmore;
    public static Activity amMitra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_mitrakaryawan);
        getSupportActionBar().setTitle("Manajemen Mitra");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tinyDB = new TinyDB(this);
        amMitra = this;
        eror = (LinearLayout)findViewById(R.id.error);
        btnCobalagi = (Button)findViewById(R.id.btnCobalagi);
        dialog = new SpotsDialog(AdminManajemenMitra.this, "Memuat data...");
        loadingbar = (ProgressBar)findViewById(R.id.loadingbar);
        loadingmore = (CardView)findViewById(R.id.loadingmore);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerManajemenMitraKaryawan);
        mAdapter = new AdminMitraAdapter(mdToko, AdminManajemenMitra.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager
                (AdminManajemenMitra.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        swipeContainer = (SwipeRefreshLayout)findViewById(R.id.swipeManajemenMitraKaryawan);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListUser();
            }
        });

        btnCobalagi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdToko.clear();
                getListUser();
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        fab = (FloatingActionButton) findViewById(R.id.fab_ManajemenMitraKarayan);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminManajemenMitra.this, AdminTambahMitra.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void getListUser() {
        dialog.show();
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<GetMitraNKaryawan> call = api.showMitra(((AdminModelData) tinyDB.getObject("admin_login", AdminModelData.class)).getIdadmin());
        System.out.println("masuk");
        call.enqueue(new Callback<GetMitraNKaryawan>() {
            @Override
            public void onResponse(Call<GetMitraNKaryawan> call, Response<GetMitraNKaryawan> response) {
                if (response.body().getStatus_code().equals("1")) {
                    mAdapter.clear();
                    mAdapter.addAll(response.body().getResult_mitra());
                    swipeContainer.setRefreshing(false);
                    dialog.dismiss();
                }else{
                    dialog.dismiss();
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(AdminManajemenMitra.this,"Tdiak Ada Data",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<GetMitraNKaryawan> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    swipeContainer.setRefreshing(false);
                    dialog.dismiss();
                    Toast.makeText(AdminManajemenMitra.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        swipeContainer.setRefreshing(true);
        getListUser();
    }
}
