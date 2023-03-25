package com.example.stocki.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.example.stocki.AktifitasAdmin.AdminInfoBarang;
import com.example.stocki.AktifitasAdmin.AdminInputBarang;
import com.example.stocki.ModelData.BarangModelData;
import com.example.stocki.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdminBarangAdapter extends RecyclerView.Adapter<AdminBarangAdapter.HolderData> {

    List<BarangModelData> barangList;
    Activity activity;
    int tipe;

//    public AdminBarangAdapter(ArrayList<BarangModelData> mdBarang, Activity barangAdmin) {
//        this.barangList = mdBarang;
//        this.activity = barangAdmin;
//    }

    public AdminBarangAdapter(List<BarangModelData> barangList, Activity activity, int tipe) {
        this.barangList = barangList;
        this.activity = activity;
        this.tipe = tipe;
    }

    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int position) {
        View v = tipe==0 ? LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barang2, parent, false) :
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barang, parent, false);
        HolderData holderData = new HolderData(v);
        return holderData;
    }


    @Override
    public void onBindViewHolder(HolderData holder, int position) {
        BarangModelData barang = barangList.get(position);
        if (tipe!=0) {
            holder.imgBarang.setVisibility(View.GONE);
            holder.imgBarang2.setVisibility(View.VISIBLE);
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .error(R.drawable.ic_picture)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH);
            Glide.with(activity.getApplicationContext())
                    .load(barang.getImage())
                    .apply(options)
                    .into(holder.imgBarang2);
        } else {
            holder.imgBarang.setVisibility(View.VISIBLE);
            holder.imgBarang2.setVisibility(View.GONE);
        }

        if (tipe==3){
            holder.imgBarang2.setVisibility(View.GONE);
            holder.txNama.setText(barang.getNamabarang());
            holder.txStok.setText(barang.getStok());
            holder.txHarga.setText(barang.getKode());
        } else if (tipe==4){
            holder.imgBarang2.setVisibility(View.VISIBLE);
            holder.txNama.setText(barang.getNamabarang());
            holder.txStok.setVisibility(View.GONE);
            holder.txBeli.setText(barang.getStok());
            holder.txHarga.setText(
                    "Modal : "+ doubleToStringNoDecimal(Double.parseDouble(barang.getHargadasar())) +"\n" +
                    "Jual : Rp " + doubleToStringNoDecimal(Double.parseDouble(barang.getHargajual())));
        } else {
            holder.txNama.setText(barang.getNamabarang());
            holder.txStok.setText(barang.getStok());
            holder.txHarga.setText("Rp " + doubleToStringNoDecimal(Double.parseDouble(barang.getHargajual())));
        }

        if (tipe==0){
            holder.txBeli.setVisibility(View.GONE);
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .error(R.drawable.ic_picture)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH);

            Glide.with(activity.getApplicationContext())
                    .load(barang.getImage())
                    .apply(options)
                    .into(holder.imgBarang);
        }else if (tipe==1){
            holder.txBeli.setVisibility(View.VISIBLE);
            holder.txBeli.setText(barang.getKode());
        }
        holder.hjual = barang.getHargajual();
        holder.pos = position;
        holder.kode = barang.getKode();
        holder.idtoko = barang.getIdtoko();
        holder.namatoko = barang.getNamatoko();
        holder.idbarang = barang.getId();
        holder.hdasar = barang.getHargadasar();
        holder.keterangan = barang.getKeterangan();
        holder.image = barang.getImage();
    }

    @Override
    public int getItemCount() {
        return barangList.size();
    }

    public class HolderData extends RecyclerView.ViewHolder implements View.OnClickListener {

        int pos;
        TextView txKode, txNama, txHarga, txBeli, txStok, txKet ;
        ImageView imgBarang, imgBarang2;
        String idbarang, kode, idtoko, namatoko, hdasar, hjual, image, keterangan;

        public HolderData(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            txNama = (TextView) itemView.findViewById(R.id.tx_namatrans);
            txHarga = (TextView) itemView.findViewById(R.id.tx_hargatrans);
            txBeli = (TextView) itemView.findViewById(R.id.tx_belitrans);
            txStok = (TextView) itemView.findViewById(R.id.tx_stoktrans);
            imgBarang = (ImageView) itemView.findViewById(R.id.img_dbarang);
            imgBarang2 = (ImageView) itemView.findViewById(R.id.img_dbarang2);
        }

        @Override
        public void onClick(View v) {
            if (tipe==1){
                Context context = v.getContext();
                Intent intent = new Intent(context, AdminInfoBarang.class);
//                intent.putExtra("tipe","1");
                intent.putExtra("namabarang",txNama.getText().toString());
                intent.putExtra("stok",txStok.getText().toString());
                intent.putExtra("kode",kode);
                intent.putExtra("hargadasar",hdasar.replace("Rp. ",""));
                intent.putExtra("hargajual",hjual.replace("Rp. ",""));
                intent.putExtra("idtoko",idtoko);
                intent.putExtra("namatoko",namatoko);
                intent.putExtra("idbarang",idbarang);
                intent.putExtra("image",image);
                intent.putExtra("keterangan",keterangan);
                context.startActivity(intent);
            } else if (tipe!=4){
                Context context = v.getContext();
                Intent intent = new Intent(context, AdminInfoBarang.class);
//                intent.putExtra("tipe","1");
                intent.putExtra("namabarang",txNama.getText().toString());
                intent.putExtra("stok",txStok.getText().toString());
                intent.putExtra("kode",kode);
                intent.putExtra("hargadasar",hdasar.replace("Rp. ",""));
                intent.putExtra("hargajual",hjual.replace("Rp. ",""));
                intent.putExtra("idtoko",idtoko);
                intent.putExtra("namatoko",namatoko);
                intent.putExtra("idbarang",idbarang);
                intent.putExtra("image",image);
                intent.putExtra("keterangan",keterangan);
                context.startActivity(intent);
            }
        }
    }

    public static String doubleToStringNoDecimal(double d) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter .applyPattern("#,###");
        return formatter.format(d).replace(",",".");
    }

    // Clean all elements of the recycler
    public void clear() {
        barangList.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(ArrayList<BarangModelData> list) {
        barangList.addAll(list);
        notifyDataSetChanged();
    }

    public void updateList(ArrayList<BarangModelData> list){
        barangList = list;
        notifyDataSetChanged();
    }
}
