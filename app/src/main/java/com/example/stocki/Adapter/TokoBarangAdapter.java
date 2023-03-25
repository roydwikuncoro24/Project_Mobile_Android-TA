package com.example.stocki.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
import com.example.stocki.AktifitasKaryawan.Karyawan;
import com.example.stocki.AktifitasKaryawan.KaryawanBarang;
import com.example.stocki.AktifitasKaryawan.KaryawanInfoBarang;
import com.example.stocki.AktifitasToko.TokoBarang;
import com.example.stocki.AktifitasToko.TokoInfoBarang;
import com.example.stocki.AktifitasToko.TokoInputBarang;
import com.example.stocki.AktifitasToko.TokoTransaksi;
import com.example.stocki.ModelData.BarangModelData;
import com.example.stocki.ModelData.KaryawanModelData;
import com.example.stocki.ModelData.TokoModelData;
import com.example.stocki.R;
import com.example.stocki.helper.TinyDB;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TokoBarangAdapter extends RecyclerView.Adapter<TokoBarangAdapter.HolderData>{

    private List<BarangModelData> barangList;
    private Activity activity;
    int tipe;
    TinyDB tinyDB;

    public TokoBarangAdapter(List<BarangModelData> mdBarang, Activity barangToko) {
        this.barangList = mdBarang;
        this.activity = barangToko;
    }

    public TokoBarangAdapter(List<BarangModelData> barangList, Activity activity, int tipe) {
        this.barangList = barangList;
        this.activity = activity;
        this.tipe = tipe;
    }
    @Override
    public TokoBarangAdapter.HolderData onCreateViewHolder(ViewGroup parent, int position) {
        View v = tipe==0 ?
                LayoutInflater.from(parent.getContext()).inflate
                        (R.layout.item_barang2, parent, false) :
                LayoutInflater.from(parent.getContext()).inflate
                        (R.layout.item_barang, parent, false);
        HolderData holderData = new HolderData(v);
        return holderData;
    }

    @Override
    public void onBindViewHolder(TokoBarangAdapter.HolderData holder, int position) {
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
            holder.txHarga.setText("Rp " + doubleToStringNoDecimal(Double.parseDouble(barang.
                    getHargajualtoko())));
        } else {
            holder.txNama.setText(barang.getNamabarang());
            holder.txStok.setText(barang.getStok());
            if (barang.getHargajualtoko().equals("0")){
                holder.txHarga.setText("Edit Harga Jual");
            } else {
                holder.txHarga.setText("Rp " + doubleToStringNoDecimal(Double.parseDouble(barang.
                        getHargajualtoko())));
            }
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

        holder.idbarang = barang.getId();
        holder.idadmin = barang.getIdadmin();
        holder.namabarang = barang.getNamabarang();
        holder.stok = barang.getStok();
        holder.kode = barang.getKode();
        holder.hdasar = barang.getHargadasar();
        holder.hjual = barang.getHargajual();
        holder.hjualtoko = barang.getHargajualtoko();
        holder.pos = position;
        holder.toko = barang.getIdtoko();
        holder.keterangan = barang.getKeterangan();
        holder.image = barang.getImage();
        holder.idtipe = barang.getidTipe();
    }

    @Override
    public int getItemCount() {
        return barangList.size();
    }

    public class HolderData extends RecyclerView.ViewHolder implements View.OnClickListener {

        int pos;
        TextView txKode, txNama, txHarga, txBeli, txStok, txKet ;
        ImageView imgBarang, imgBarang2;
        String idbarang, idadmin, kode, namabarang, stok, toko, hdasar, hjual,hjualtoko, image, keterangan, idtipe;

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
            if(activity instanceof TokoBarang){
                if (tipe==1){
                    Context context = v.getContext();
                    Intent intent = new Intent(context, TokoInfoBarang.class);
//                    intent.putExtra("tipe","1");
                    intent.putExtra("namabarang",txNama.getText().toString());
                    intent.putExtra("stok",txStok.getText().toString());
                    intent.putExtra("kode",kode);
                    intent.putExtra("hargajual",hjual.replace("Rp. ",""));
                    intent.putExtra("hargajualtoko",hjualtoko.replace("Rp. ",""));
                    intent.putExtra("idbarang",idbarang);
                    intent.putExtra("image",image);
                    intent.putExtra("keterangan",keterangan);
                    intent.putExtra("idadmin",idadmin);
                    intent.putExtra("idtipe",idtipe);
                    context.startActivity(intent);

                } else if (tipe!=4){
                    Context context = v.getContext();
                    Intent intent = new Intent(context, TokoInfoBarang.class);
//                    intent.putExtra("tipe","1");
                    intent.putExtra("namabarang",txNama.getText().toString());
                    intent.putExtra("stok",txStok.getText().toString());
                    intent.putExtra("kode",kode);
                    intent.putExtra("hargajual",hjual.replace("Rp. ",""));
                    intent.putExtra("hargajualtoko",hjualtoko.replace("Rp. ",""));
                    intent.putExtra("idbarang",idbarang);
                    intent.putExtra("image",image);
                    intent.putExtra("keterangan",keterangan);
                    intent.putExtra("idadmin",idadmin);
                    intent.putExtra("idtipe",idtipe);
                    context.startActivity(intent);
                }
            } else if(activity instanceof KaryawanBarang){
                if (tipe==1){
                    Context context = v.getContext();
                    Intent intent = new Intent(context, KaryawanInfoBarang.class);
//                    intent.putExtra("tipe","1");
                    intent.putExtra("namabarang",txNama.getText().toString());
                    intent.putExtra("stok",txStok.getText().toString());
                    intent.putExtra("kode",kode);
                    intent.putExtra("hargajual",hjual.replace("Rp. ",""));
                    intent.putExtra("hargajualtoko",hjualtoko.replace("Rp. ",""));
                    intent.putExtra("idbarang",idbarang);
                    intent.putExtra("image",image);
                    intent.putExtra("keterangan",keterangan);
                    intent.putExtra("idadmin",idadmin);
                    intent.putExtra("idtipe",idtipe);
                    context.startActivity(intent);
                } else if (tipe!=4){
                    Context context = v.getContext();
                    Intent intent = new Intent(context, KaryawanInfoBarang.class);
//                    intent.putExtra("tipe","1");
                    intent.putExtra("namabarang",txNama.getText().toString());
                    intent.putExtra("stok",txStok.getText().toString());
                    intent.putExtra("kode",kode);
                    intent.putExtra("hargajual",hjual.replace("Rp. ",""));
                    intent.putExtra("hargajualtoko",hjualtoko.replace("Rp. ",""));
                    intent.putExtra("idbarang",idbarang);
                    intent.putExtra("image",image);
                    intent.putExtra("keterangan",keterangan);
                    intent.putExtra("idadmin",idadmin);
                    intent.putExtra("idtipe",idtipe);
                    context.startActivity(intent);
                }
//                    Context context = v.getContext();
//                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
//                    LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
//                    View dialogView = inflater.inflate(R.layout.dialog_barang, null);
//                    dialogBuilder.setView(dialogView);
//
//                    final TextView txNamabar = (TextView) dialogView.findViewById(R.id.tx_dialogbarNama);
//                    final TextView txKode = (TextView) dialogView.findViewById(R.id.tx_dialogKode);
//                    final TextView txStok = (TextView) dialogView.findViewById(R.id.tx_dialogStok);
//                    final TextView txHj = (TextView) dialogView.findViewById(R.id.tx_dialogHJT);
//                    final TextView txKet = (TextView) dialogView.findViewById(R.id.petugasDialogJual);
//                    final ImageView imgDialog = (ImageView) dialogView.findViewById(R.id.img_Dialogbarang);
//                    this.idbarang.toString();
//                    txNamabar.setText(this.namabarang);
//                    txKode.setText(this.kode);
//                    txStok.setText(this.stok);
//                    txHj.setText("Rp. "+doubleToStringNoDecimal(Double.parseDouble(this.hjualtoko)));
//                    txKet.setText(this.keterangan);
//
//                    RequestOptions options = new RequestOptions()
//                            .fitCenter()
//                            .error(R.drawable.ic_picture)
//                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .priority(Priority.HIGH);
//
//                    Glide.with(activity.getApplicationContext())
//                            .load(imgBarang.getDrawable())
//                            .apply(options)
//                            .into(imgDialog);
//
//                    dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    });
//                    AlertDialog alertDialog = dialogBuilder.create();
//                    alertDialog.show();
//                }
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
    public void addAll(List<BarangModelData> list) {
        barangList.addAll(list);
        notifyDataSetChanged();
    }

    public void updateList(List<BarangModelData> list){
        barangList = list;
        notifyDataSetChanged();
    }
}
