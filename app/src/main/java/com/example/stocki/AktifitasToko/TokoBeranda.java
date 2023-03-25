package com.example.stocki.AktifitasToko;

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
import com.example.stocki.ModelData.BayarTanggunganModelData;
import com.example.stocki.ModelData.PenjualanModelData;
import com.example.stocki.ModelData.TanggunganModelData;
import com.example.stocki.ModelData.TokoModelData;
import com.example.stocki.ModelResponse.BayarTanggunganGetResponse;
import com.example.stocki.ModelResponse.PenjualanGetResponse;
import com.example.stocki.ModelResponse.TanggunganGetResponse;
import com.example.stocki.PickDeviceActivity;
import com.example.stocki.R;
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

public class TokoBeranda extends Fragment {

    ImageView greetImg;
    TextView greetText, tvnama, tvuntung, tvtanggungan;
    ImageView btnNextUntung, btnNextTanggungan,
              btnTransaksi, btnBarang, btnKaryawan, btnPerangkat;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<PenjualanModelData> resultList = new ArrayList<PenjualanModelData>();
    ArrayList<PenjualanModelData> tempPen= new ArrayList<>();
    ArrayList<TanggunganModelData> resultTanggungan = new ArrayList<TanggunganModelData>();
    ArrayList<TanggunganModelData> tempTang= new ArrayList<>();
    ArrayList<BayarTanggunganModelData> resultBayarTanggungan = new ArrayList<BayarTanggunganModelData>();
    ArrayList<BayarTanggunganModelData> tempBay= new ArrayList<>();
    int jual = 0; int laba = 0; int modal = 0;
    int tanggungan = 0; int bayar = 0; int sisa = 0;
    TokoBerandaAdapter tbAdapter;
    RecyclerView recyclerView;
    TinyDB tinyDB;
    SpotsDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.beranda_toko, container, false);
        tinyDB = new TinyDB(getContext());
        setHasOptionsMenu(true);

        greetImg = v.findViewById(R.id.greetingtoko_img);
        greetText = v.findViewById(R.id.greetingtoko_text);
        tvnama = v.findViewById(R.id.userTk);
        dialog = new SpotsDialog(getContext(), "Loading...");

        tvuntung = v.findViewById(R.id.home_untungTk);
        tvtanggungan = v.findViewById(R.id.home_tanggunganTk);

        try {
            tvnama.setText(((TokoModelData) tinyDB.getObject("toko_login", TokoModelData.class)).getNamapemilik());
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnNextUntung = v.findViewById(R.id.homeTk_imgNextUntungTk);
        btnNextTanggungan = v.findViewById(R.id.homeTk_imgNextTanggunganTk);
        btnNextUntung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TokoPenjualan.class);
                startActivity(intent);
            }
        });
        btnNextTanggungan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TokoTanggungan.class);
                startActivity(intent);
            }
        });
        btnTransaksi = v.findViewById(R.id.homeTk_trasaksi);
        btnBarang = v.findViewById(R.id.homeTk_barang);
        btnKaryawan = v.findViewById(R.id.homeTk_karyawan);
        btnPerangkat = v.findViewById(R.id.homeTk_perangkat);
        btnTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TokoTransaksi.class);
                startActivity(intent);
            }
        });
        btnBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TokoBarang.class);
                startActivity(intent);
            }
        });
        btnKaryawan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TokoManajemenKaryawan.class);
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
        recyclerView = v.findViewById(R.id.recHomeTk);

        tbAdapter = new TokoBerandaAdapter(resultList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(tbAdapter);
        return v;
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
            tvnama.setTextColor(Color.WHITE);
            greetText.setTextColor(Color.WHITE);
            greetImg.setImageResource(R.drawable.img_default_half_night);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        greeting();
        getListPenjualan();
    }

    public void getListPenjualan() {
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<PenjualanGetResponse> call = api.showPenjualanToko(((TokoModelData)
                tinyDB.getObject("toko_login", TokoModelData.class)).getIdtoko());
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
                    calculateBalance(resultList);
                    getListTanggungan();
                    getListBayartangTanggungan();
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

    public void getListTanggungan() {
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<TanggunganGetResponse> call = api.showTanggunganToko(((TokoModelData)
                tinyDB.getObject("toko_login", TokoModelData.class)).getIdtoko());
        System.out.println("masuk");
        call.enqueue(new Callback<TanggunganGetResponse>() {
            @Override
            public void onResponse(Call<TanggunganGetResponse> call, Response<TanggunganGetResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code().equals("1")) {
                    resultTanggungan.clear();
                    resultTanggungan.addAll(response.body().getResult_tanggungan());
                    tempTang = resultTanggungan;
                    calculateTanggungan(resultTanggungan,resultBayarTanggungan);
                } else {
//                    Toast.makeText(getContext(), "Tidak ada tanggungan", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<TanggunganGetResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(getContext(), "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getListBayartangTanggungan() {
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<BayarTanggunganGetResponse> call = api.showBayarTanggunganToko(((TokoModelData)
                tinyDB.getObject("toko_login", TokoModelData.class)).getIdtoko());
        System.out.println("masuk");
        call.enqueue(new Callback<BayarTanggunganGetResponse>() {
            @Override
            public void onResponse(Call<BayarTanggunganGetResponse> call, Response<BayarTanggunganGetResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code().equals("1")) {
                    resultBayarTanggungan.clear();
                    resultBayarTanggungan.addAll(response.body().getResult_bayartanggungan());
                    tempBay = resultBayarTanggungan;
                    calculateTanggungan(resultTanggungan, resultBayarTanggungan);
                } else {
//                    Toast.makeText(getContext(), "Tidak ada pembayaran tanggungan", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<BayarTanggunganGetResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(getContext(), "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void calculateTanggungan(ArrayList<TanggunganModelData> calculateList, ArrayList<BayarTanggunganModelData> calculateBayarList) {
        tanggungan = 0;
        bayar = 0;
        sisa = 0;
        for (TanggunganModelData rk : calculateList) {
            tanggungan = tanggungan + Integer.parseInt(rk.getTanggungan());
        }
        for (BayarTanggunganModelData bt : calculateBayarList) {
            bayar = bayar + Integer.parseInt(bt.getPembayaran());
        }
        sisa = tanggungan - bayar;
        tvtanggungan.setTextColor(Color.RED);
        tvtanggungan.setText("Rp " + doubleToStringNoDecimal(String.valueOf(sisa)));
    }

    public void calculateBalance(ArrayList<PenjualanModelData> calculateList) {
        modal = 0;
        jual = 0;
        laba = 0;
        for (PenjualanModelData pm : calculateList) {
            modal = modal + Integer.parseInt(pm.getJumhargajual());
            jual = jual + Integer.parseInt(pm.getJumhargajualtoko());
            laba = jual - modal;
        }
        tvuntung.setTextColor(Color.BLACK);
        tvuntung.setText("Rp " + doubleToStringNoDecimal(String.valueOf(laba)));
    }
    public static String doubleToStringNoDecimal(String value) {
        double d = Double.parseDouble(value);
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###");
        return formatter.format(d).replace(",", ".");
    }
}
