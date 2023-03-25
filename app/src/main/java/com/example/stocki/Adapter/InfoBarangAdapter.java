package com.example.stocki.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stocki.AktifitasAdmin.AdminInfoBarang;
import com.example.stocki.AktifitasKaryawan.KaryawanBarang;
import com.example.stocki.AktifitasToko.TokoInfoBarang;
import com.example.stocki.ModelData.PenjualanModelData;
import com.example.stocki.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class InfoBarangAdapter extends RecyclerView.Adapter<InfoBarangAdapter.HolderData> {
    List<PenjualanModelData> penjualanList;
    Activity activity;

    public InfoBarangAdapter(ArrayList<PenjualanModelData> penjualantList, Activity activity) {
        this.penjualanList = penjualantList;
        this.activity = activity;
    }

    @Override
    public InfoBarangAdapter.HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_penjualan, parent, false);
        InfoBarangAdapter.HolderData holderData = new InfoBarangAdapter.HolderData(v);
        return holderData;
    }

    @Override
    public void onBindViewHolder(InfoBarangAdapter.HolderData holder, int position) {
        PenjualanModelData pen = penjualanList.get(position);
        if(activity instanceof AdminInfoBarang){
            holder.txNamaBarang.setVisibility(View.GONE);
            holder.imgBarang.setVisibility(View.GONE);
            holder.txJual.setText("Rp " + doubleToStringNoDecimal(Double.parseDouble
                    (pen.getJumhargajual())));
            holder.txJmlBar.setText(pen.getJumlah());
            holder.txTgl.setText(pen.getTanggal());
        } else {
            holder.txNamaBarang.setVisibility(View.GONE);
            holder.imgBarang.setVisibility(View.GONE);
            holder.txJual.setText("Rp " + doubleToStringNoDecimal(Double.parseDouble
                    (pen.getJumhargajualtoko())));
            holder.txJmlBar.setText(pen.getJumlah());
            holder.txTgl.setText(pen.getTanggal());
        }
    }

    @Override
    public int getItemCount() {
        return penjualanList.size();
    }

    public class HolderData extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txJual, txJmlBar, txTgl, txNamaBarang;
        public ImageView imgBarang;
        public String id;
        public HolderData(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            txNamaBarang = (TextView) itemView.findViewById(R.id.namabarPenjualan);
            txJual = (TextView) itemView.findViewById(R.id.tx_hargapenjualan);
            txJmlBar = (TextView) itemView.findViewById(R.id.jmlBarDialogJual);
            txTgl = (TextView) itemView.findViewById(R.id.tx_tanggalpenjualan);
            imgBarang = (ImageView) itemView.findViewById(R.id.img_penjualan);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public static String doubleToStringNoDecimal(double d) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter .applyPattern("#,###");
        return formatter.format(d).replace(",",".");
    }

    public void clear() {
        penjualanList.clear();
        notifyDataSetChanged();
    }
    public void addAll(List<PenjualanModelData> list) {
        penjualanList.addAll(list);
        notifyDataSetChanged();
    }

    public void updateList(ArrayList<PenjualanModelData> penjualantList) {
        penjualanList = penjualantList;
        notifyDataSetChanged();
    }
}
