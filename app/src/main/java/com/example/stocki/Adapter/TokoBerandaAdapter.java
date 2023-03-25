package com.example.stocki.Adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.stocki.ModelData.PenjualanModelData;
import com.example.stocki.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TokoBerandaAdapter extends RecyclerView.Adapter<TokoBerandaAdapter.HolderData> {

    List<PenjualanModelData> penjualanList;
    Fragment fragment;

    public TokoBerandaAdapter(ArrayList<PenjualanModelData> penjualanList, Fragment fragment) {
        this.penjualanList = penjualanList;
        this.fragment = fragment;
    }

    @Override
    public TokoBerandaAdapter.HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_penjualan, parent, false);
        TokoBerandaAdapter.HolderData holderData = new TokoBerandaAdapter.HolderData(v);
        return holderData;
    }

    @Override
    public void onBindViewHolder(TokoBerandaAdapter.HolderData holder, int position) {
        PenjualanModelData pen = penjualanList.get(position);
        holder.txNamaBarang.setText("Barang : "+pen.getNamabarang());
        holder.txJual.setText("Rp " + doubleToStringNoDecimal(Double.parseDouble
                (pen.getJumhargajualtoko())));
        holder.txJmlBar.setText(pen.getJumlah());
        holder.txTgl.setText(pen.getTanggal());

        RequestOptions options = new RequestOptions()
                .fitCenter()
                .error(R.drawable.ic_picture)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        Glide.with(fragment.getContext())
                .load(pen.getImage())
                .apply(options)
                .into(holder.imgBarang);
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
    public void updateList(List<PenjualanModelData> list){
        penjualanList = list;
        notifyDataSetChanged();
    }
}
