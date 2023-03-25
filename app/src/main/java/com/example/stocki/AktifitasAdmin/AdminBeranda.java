package com.example.stocki.AktifitasAdmin;

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

import com.example.stocki.Adapter.AdminBerandaAdapter;
import com.example.stocki.Adapter.AdminPenjualanAdapter;
import com.example.stocki.Adapter.TokoBerandaAdapter;
import com.example.stocki.ModelData.AdminModelData;
import com.example.stocki.ModelData.BarangModelData;
import com.example.stocki.ModelData.PenjualanModelData;
import com.example.stocki.ModelResponse.BarangGetResponse;
import com.example.stocki.ModelResponse.PenjualanGetResponse;
import com.example.stocki.PickDeviceActivity;
import com.example.stocki.R;
import com.example.stocki.helper.SortbyIdBarangDESC;
import com.example.stocki.helper.SortbyIdPenjualanDESC;
import com.example.stocki.helper.TinyDB;
import com.example.stocki.service.APIService;
import com.example.stocki.service.RetrofitHelper;

import java.net.SocketTimeoutException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.stocki.AktifitasAdmin.AdminBarang.doubleToStringNoDecimal;

public class AdminBeranda extends Fragment {

    ImageView greetImg;
    TextView greetText, nama, txUntung, txAset;
    int aset; int untung;
    int jual = 0; int laba = 0; int modal = 0;
    ImageView btnNextUntung, btnNextAset,
              btnMitra, btnBarang, btnPiutang, btnPenjualan;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    TinyDB tinyDB;
    SpotsDialog dialogadm;
    AdminBerandaAdapter tapAdapter;
    ArrayList<BarangModelData> resultBarang = new ArrayList<BarangModelData>();
    ArrayList<BarangModelData> tempBar= new ArrayList<>();
    ArrayList<PenjualanModelData> penjualantList = new ArrayList<PenjualanModelData>();
    ArrayList<PenjualanModelData> temp= new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.beranda_admin, container, false);
        tinyDB = new TinyDB(getContext());
        setHasOptionsMenu(true);

        greetImg = v.findViewById(R.id.greeting_img);
        greetText = v.findViewById(R.id.greeting_text);
        nama = v.findViewById(R.id.userAdm);
        dialogadm = new SpotsDialog(getContext(), "Loading...");

        txUntung = v.findViewById(R.id.home_untung);
        txAset = v.findViewById(R.id.home_Aset);

        try {
            nama.setText(((AdminModelData) tinyDB.getObject("admin_login", AdminModelData.class)).getNama());
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnNextUntung = v.findViewById(R.id.homeAdm_imgNextUntung);
        btnNextAset = v.findViewById(R.id.homeAdm_imgNextAset);
        btnNextUntung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AdminDataPenjualan.class);
                startActivity(intent);
            }
        });
        btnNextAset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AdminBarang.class);
                startActivity(intent);
            }
        });
        btnMitra = v.findViewById(R.id.homeadm_mitra);
        btnBarang = v.findViewById(R.id.homeadm_barang);
        btnPiutang = v.findViewById(R.id.homeadm_piutang);
        btnPenjualan = v.findViewById(R.id.homeadm_penjualan);

        btnMitra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AdminManajemenMitra.class);
                startActivity(intent);
            }
        });
        btnBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AdminBarang.class);
                startActivity(intent);
            }
        });
        btnPiutang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AdminTanggungan.class);
                startActivity(intent);
            }
        });
        btnPenjualan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AdminDataPenjualan.class);
                startActivity(intent);
            }
        });
        recyclerView = v.findViewById(R.id.recHomeAdm);
        tapAdapter = new AdminBerandaAdapter(penjualantList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(tapAdapter);
        greeting();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getListBarang();
    }

    @SuppressLint("SetTextI18n")
    private void greeting() {
        getListBarang();
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

    public void getListBarang(){
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<BarangGetResponse> call = api.showBarangAdmin(((AdminModelData)
                tinyDB.getObject("admin_login", AdminModelData.class)).getIdadmin());
        System.out.println("masuk");
        call.enqueue(new Callback<BarangGetResponse>() {
            @Override
            public void onResponse(Call<BarangGetResponse> call, Response<BarangGetResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code().equals("1")) {
                    resultBarang.clear();
                    resultBarang.addAll(response.body().getResult_barang());
                    tempBar = resultBarang;
                    calculateAset(resultBarang);
                    getListPenjualan();
                }else {
                    Toast.makeText(getContext(), "Tidak ada aset", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<BarangGetResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(getContext(), "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getListPenjualan() {
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<PenjualanGetResponse> call = api.showDataPenjualanAdmin(((AdminModelData)
                tinyDB.getObject("admin_login", AdminModelData.class)).getIdadmin());
        System.out.println("masuk");
        call.enqueue(new Callback<PenjualanGetResponse>() {
            @Override
            public void onResponse(Call<PenjualanGetResponse> call, Response<PenjualanGetResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code().equals("1")) {
                    penjualantList.clear();
                    penjualantList.addAll(response.body().getResult_penjualan());
                    temp = penjualantList;
                    Collections.sort(penjualantList, new SortbyIdPenjualanDESC());
                    tapAdapter.updateList(penjualantList);
                    calculateBalance(penjualantList);
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

    public void calculateAset(ArrayList<BarangModelData> calculateBarang) {
        aset = 0;
        for (BarangModelData barang : calculateBarang) {
            aset = aset + Integer.parseInt(barang.getAset());
        }
        txAset.setTextColor(Color.BLACK);
        txAset.setText("Rp. " + doubleToStringNoDecimal(String.valueOf(aset)));
    }

    public void calculateBalance(ArrayList<PenjualanModelData> calculateList) {
        modal = 0;
        jual = 0;
        laba = 0;
        for (PenjualanModelData pm : calculateList) {
            modal = modal + Integer.parseInt(pm.getJumhargadasar());
            jual = jual + Integer.parseInt(pm.getJumhargajual());
            laba = jual - modal;
        }
        txUntung.setTextColor(Color.BLACK);
        txUntung.setText("Rp " + doubleToStringNoDecimal(String.valueOf(laba)));
    }

    public static String doubleToStringNoDecimal(String value) {
        double d = Double.parseDouble(value);
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###");
        return formatter.format(d).replace(",", ".");
    }
}
