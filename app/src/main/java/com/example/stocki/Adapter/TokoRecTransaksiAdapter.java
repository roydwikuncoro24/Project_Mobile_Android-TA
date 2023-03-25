package com.example.stocki.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stocki.AktifitasKaryawan.Karyawan;
import com.example.stocki.AktifitasKaryawan.KaryawanPenjualan;
import com.example.stocki.AktifitasToko.TokoDetailRecTransaksi;
import com.example.stocki.AktifitasToko.TokoRecTransaksi;
import com.example.stocki.ModelData.TransaksiModelData;
import com.example.stocki.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TokoRecTransaksiAdapter extends RecyclerView.Adapter<TokoRecTransaksiAdapter.HolderData> {
    List<TransaksiModelData> resultList;
    Activity activity;

    public TokoRecTransaksiAdapter(ArrayList<TransaksiModelData> resultList, Activity activity) {
        this.resultList = resultList;
        this.activity = activity;
    }

    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_rectransaksi, parent, false);
        HolderData holderData = new HolderData(v);
        return holderData;
    }

    @Override
    public void onBindViewHolder(HolderData holder, int position) {
        TransaksiModelData histrans = resultList.get(position);
        holder.txNilai.setText("Rp " + doubleToStringNoDecimal(Double.parseDouble(histrans.getJual())));
        holder.txJmlBar.setText(histrans.getJumlah());
        holder.txNamTk.setText(histrans.getNamatoko());
        holder.txPetugas.setText(histrans.getPetugas());
        holder.txTanggal.setText(histrans.getTanggal());
        holder.id = histrans.getId();
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class HolderData extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txNilai, txJmlBar, txNamTk,
               txPetugas, txTanggal;
        public String id;

        public HolderData(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            txNilai = (TextView) itemView.findViewById(R.id.tx_jumlah_hargatrans);
            txJmlBar = (TextView) itemView.findViewById(R.id.tx_jmlbar_trans);
            txNamTk = (TextView) itemView.findViewById(R.id.tx_ketNamTk);
            txPetugas = (TextView) itemView.findViewById(R.id.tx_petugasTk);
            txTanggal = (TextView) itemView.findViewById(R.id.tx_tgl_laporan);
        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            Intent intent = new Intent(context, TokoDetailRecTransaksi.class);
            intent.putExtra("id",id);
            intent.putExtra("jumlah",txNilai.getText().toString());
            intent.putExtra("jmlbar",txJmlBar.getText().toString());
            intent.putExtra("petugas",txPetugas.getText().toString());
            intent.putExtra("tanggal",txTanggal.getText().toString());
            context.startActivity(intent);
        }
    }

    public void updateList(ArrayList<TransaksiModelData> list) {
        resultList = list;
        notifyDataSetChanged();
    }

    public static String doubleToStringNoDecimal(double d) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter .applyPattern("#,###");
        return formatter.format(d).replace(",",".");
    }
    // Clean all elements of the recycler
    public void clear() {
        resultList.clear();
        notifyDataSetChanged();
    }
    // Add a list of items -- change to type used
    public void addAll(List<TransaksiModelData> list) {
        resultList.addAll(list);
        notifyDataSetChanged();
    }
}
