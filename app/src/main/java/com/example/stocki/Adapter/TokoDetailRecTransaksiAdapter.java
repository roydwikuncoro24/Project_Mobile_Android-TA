package com.example.stocki.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
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
import com.example.stocki.ModelData.TransaksiProses;
import com.example.stocki.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TokoDetailRecTransaksiAdapter extends RecyclerView.Adapter<TokoDetailRecTransaksiAdapter.HolderData> {
    List<PenjualanModelData> penjualanList;
    Activity activity;

    public TokoDetailRecTransaksiAdapter(ArrayList<PenjualanModelData> penjualanList, Activity activity) {
        this.penjualanList= penjualanList;
        this.activity = activity;
    }

    @Override
    public TokoDetailRecTransaksiAdapter.HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_transaksi,
                parent, false);
        TokoDetailRecTransaksiAdapter.HolderData holderData = new TokoDetailRecTransaksiAdapter.HolderData(v);
        return holderData;
    }


    @Override
    public void onBindViewHolder(HolderData holder, int position) {
        PenjualanModelData penjualan = penjualanList.get(position);
        holder.txJumlah.setText(penjualan.getJumlah());
        holder.txHarga.setText("Rp " + doubleToStringNoDecimal(Double.parseDouble(penjualan.
                getHjualtoko())));
        holder.txNama.setText(penjualan.getNamabarang());

        RequestOptions options = new RequestOptions()
                .fitCenter()
                .error(R.drawable.ic_picture)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        Glide.with(activity.getApplicationContext())
                .load(penjualan.getImage())
                .apply(options)
                .into(holder.image);
        holder.posisi = position;
    }


    @Override
    public int getItemCount() {
        return penjualanList.size();
    }

    public class HolderData extends RecyclerView.ViewHolder {
        TextView txJumlah;
        TextView txNama, txHarga, txStok;
        ImageView image;
        int posisi;

        public HolderData(View itemView) {
            super(itemView);
            txJumlah = (TextView)itemView.findViewById(R.id.tx_stokdb);
            txNama = (TextView)itemView.findViewById(R.id.tx_namadb);
            txHarga = (TextView)itemView.findViewById(R.id.tx_hargadb);
            image = (ImageView) itemView.findViewById(R.id.img_dbarangList);
        }
    }

    public static String doubleToStringNoDecimal(double d) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter .applyPattern("#,###");
        return formatter.format(d).replace(",",".");
    }

    // Clean all elements of the recycler
    public void clear() {
        penjualanList.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<PenjualanModelData> list) {
        penjualanList.addAll(list);
        notifyDataSetChanged();
    }

    public void updateList(List<PenjualanModelData> list){
        penjualanList = list;
        notifyDataSetChanged();
    }
}
