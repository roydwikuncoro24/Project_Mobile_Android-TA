package com.example.stocki.AktifitasKaryawan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stocki.Adapter.TokoBerandaAdapter;
import com.example.stocki.AktifitasToko.TokoBarang;
import com.example.stocki.AktifitasToko.TokoTransaksi;
import com.example.stocki.ModelData.KaryawanModelData;
import com.example.stocki.ModelData.PenjualanModelData;
import com.example.stocki.ModelData.TokoModelData;
import com.example.stocki.ModelResponse.PenjualanGetResponse;
import com.example.stocki.PickDeviceActivity;
import com.example.stocki.R;
import com.example.stocki.helper.SortbyIdPenjualanDESC;
import com.example.stocki.helper.TinyDB;
import com.example.stocki.service.APIService;
import com.example.stocki.service.RetrofitHelper;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KaryawanBeranda extends Fragment {

    ImageView greetImg;
    TextView greetText, nama;
    ImageView btnTransaksi, btnBarang, btnDataTrans, btnPerangkat;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    ArrayList<PenjualanModelData> resultList = new ArrayList<PenjualanModelData>();
    ArrayList<PenjualanModelData> tempPen= new ArrayList<>();
    TokoBerandaAdapter tbAdapter;
    TinyDB tinyDB;
    SpotsDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.beranda_karyawan, container, false);
        tinyDB = new TinyDB(getContext());
        setHasOptionsMenu(true);

        greetImg = v.findViewById(R.id.greetingkaryawan_img);
        greetText = v.findViewById(R.id.greetingkaryawan_text);
        nama = v.findViewById(R.id.userKy);
        dialog = new SpotsDialog(getContext(), "Loading...");

        try {
            nama.setText(((KaryawanModelData) tinyDB.getObject("karyawan_login", KaryawanModelData.class)).getNamakaryawan());
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnTransaksi = v.findViewById(R.id.homeKy_trasaksi);
        btnBarang = v.findViewById(R.id.homeKy_barang);
        btnDataTrans = v.findViewById(R.id.homeKy_data);
        btnPerangkat = v.findViewById(R.id.homeKy_perangkat);
        btnTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), KaryawanTransaksi.class);
                startActivity(intent);
            }
        });
        btnBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), KaryawanBarang.class);
                startActivity(intent);
            }
        });
        btnDataTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), KaryawanPenjualan.class);
                startActivity(intent);
            }
        });
        btnPerangkat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PickDeviceActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = v.findViewById(R.id.recHomeKy);
        tbAdapter = new TokoBerandaAdapter(resultList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(tbAdapter);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        greeting();
        getListPenjualan();
    }

    @SuppressLint("SetTextI18n")
    private void greeting() {
        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12){
            greetText.setText("Selamat Pagi");
            greetImg.setImageResource(R.drawable.img_default_half_morning);
        } else if (timeOfDay >= 12 && timeOfDay < 15) {
            greetText.setText("Selamat Siang");
            greetImg.setImageResource(R.drawable.img_default_half_afternoon);
        } else if (timeOfDay >= 15 && timeOfDay < 18) {
            greetText.setText("Selamat Sore");
            greetImg.setImageResource(R.drawable.img_default_half_without_sun);
        } else if (timeOfDay >= 18 && timeOfDay < 24) {
            greetText.setText("Selamat Malam");
            nama.setTextColor(Color.WHITE);
            greetText.setTextColor(Color.WHITE);
            greetImg.setImageResource(R.drawable.img_default_half_night);
        }
    }

    public void getListPenjualan() {
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<PenjualanGetResponse> call = api.showPenjualanToko(((KaryawanModelData)
                tinyDB.getObject("karyawan_login", KaryawanModelData.class)).getIdtoko());
        System.out.println("masuk");
        call.enqueue(new Callback<PenjualanGetResponse>() {
            @Override
            public void onResponse(Call<PenjualanGetResponse> call, Response<PenjualanGetResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code().equals("1")) {
                    resultList.clear();
                    resultList.addAll(response.body().getResult_penjualan());
                    tempPen = resultList;
                    Collections.sort(resultList, new SortbyIdPenjualanDESC());
                    tbAdapter.updateList(resultList);
                  } else {
                    Toast.makeText(getContext(), "Tidak ada penjualan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PenjualanGetResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(getContext(), "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
