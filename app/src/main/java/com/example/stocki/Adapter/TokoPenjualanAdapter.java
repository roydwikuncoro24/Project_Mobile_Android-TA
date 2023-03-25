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
import com.example.stocki.AktifitasKaryawan.KaryawanPenjualan;
import com.example.stocki.AktifitasToko.TokoDetailRecTransaksi;
import com.example.stocki.AktifitasToko.TokoPenjualan;
import com.example.stocki.ModelData.PenjualanModelData;
import com.example.stocki.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TokoPenjualanAdapter extends RecyclerView.Adapter<TokoPenjualanAdapter.HolderData> {
    List<PenjualanModelData> penjualanList;
    Activity activity;

    public TokoPenjualanAdapter(List<PenjualanModelData> mdPenjualan, Activity penjualan) {
        this.penjualanList= mdPenjualan;
        this.activity = penjualan;
    }

    @Override
    public TokoPenjualanAdapter.HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_penjualan,
                parent, false);
        TokoPenjualanAdapter.HolderData holderData = new TokoPenjualanAdapter.HolderData(v);
        return holderData;
    }

    @Override
    public void onBindViewHolder(TokoPenjualanAdapter.HolderData holder, int position) {
        PenjualanModelData pen = penjualanList.get(position);
        holder.txNamaBarang.setText(pen.getNamabarang());
        holder.txJual.setText("Rp " + doubleToStringNoDecimal(Double.parseDouble
                (pen.getJumhargajualtoko())));
        holder.txJmlBar.setText(pen.getJumlah());
        holder.txTgl.setText(pen.getTanggal());

        RequestOptions options = new RequestOptions()
                .fitCenter()
                .error(R.drawable.ic_picture)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        Glide.with(activity.getApplicationContext())
                .load(pen.getImage())
                .apply(options)
                .into(holder.imgBarang);

        int laba = 0;
        laba = (Integer.parseInt(pen.getJumhargajualtoko()) - (Integer.parseInt(pen.getJumhargajual())));
        holder.junt = (laba);
        holder.jmod = pen.getJumhargajual();

        holder.id = pen.getId();
        holder.idtransaksi = pen.getIdtransaksi();
//        holder.petugas = pen.getPetugas();
        holder.hj = pen.getHjualtoko();
        holder.mod = pen.getHjual();
    }

    @Override
    public int getItemCount() {
        return penjualanList.size();
    }

    public class HolderData extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txJual, txJmlBar, txTgl, txNamaBarang;
        public ImageView imgBarang;
        public String id, idtransaksi;
        public String jmod;
        public int junt;
        public String petugas;
        public String hj;
        public String mod;
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
            if(activity instanceof TokoPenjualan){
                Context context = v.getContext();
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

                LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                View dialogView = inflater.inflate(R.layout.dialog_penjualan, null);
                dialogBuilder.setView(dialogView);

                final TextView penjualan = (TextView) dialogView.findViewById(R.id.tx_hargapenjualan);
                final TextView namaBarang = (TextView) dialogView.findViewById(R.id.namaDialogJual);
                final TextView modal = (TextView) dialogView.findViewById(R.id.tx_modpenjualan);
                final TextView untung = (TextView) dialogView.findViewById(R.id.tx_untungpenjualan);
                final TextView tgl = (TextView) dialogView.findViewById(R.id.tanggalDialogJual);
                final TextView jmlbar = (TextView) dialogView.findViewById(R.id.jmlBarDialogJual);
//                final TextView petugas = (TextView) dialogView.findViewById(R.id.petugasDialogJual);
                final TextView hj = (TextView) dialogView.findViewById(R.id.hjDialogJual);
                final TextView mod = (TextView) dialogView.findViewById(R.id.modalDialogJual);
                final ImageView img = (ImageView) dialogView.findViewById(R.id.img_Dialogpenjualan);

                this.id.toString();
                RequestOptions options = new RequestOptions()
                        .fitCenter()
                        .error(R.drawable.ic_picture)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .priority(Priority.HIGH);

                Glide.with(activity.getApplicationContext())
                        .load(imgBarang.getDrawable())
                        .apply(options)
                        .into(img);
                penjualan.setText(this.txJual.getText().toString());
                namaBarang.setText(this.txNamaBarang.getText().toString());
                modal.setText("Rp "+ doubleToStringNoDecimal(Double.parseDouble(this.jmod)));
                untung.setText("Rp "+ doubleToStringNoDecimal(Double.parseDouble(String.valueOf(this.junt))));
                tgl.setText(this.txTgl.getText().toString());
                jmlbar.setText(this.txJmlBar.getText().toString());
//                petugas.setText(this.petugas);
                hj.setText("Rp "+ doubleToStringNoDecimal(Double.parseDouble(this.hj)));
                mod.setText("Rp "+ doubleToStringNoDecimal(Double.parseDouble(this.mod)));

                dialogBuilder.setNegativeButton("CEK TRANSAKSI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Context context = activity;
                        Intent intent = new Intent(context, TokoDetailRecTransaksi.class);
                        intent.putExtra("id",idtransaksi);
                        context.startActivity(intent);
                    }
                });
                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            } else if (activity instanceof KaryawanPenjualan){
                Context context = v.getContext();
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

                LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                View dialogView = inflater.inflate(R.layout.dialog_penjualankaryawan, null);
                dialogBuilder.setView(dialogView);

                final TextView barang = (TextView) dialogView.findViewById(R.id.nambarPenjualanK);
                final TextView penjualan = (TextView) dialogView.findViewById(R.id.penjualanK);
                final TextView jmlbar = (TextView) dialogView.findViewById(R.id.jmlbarPenjualanK);
//                final TextView petugas = (TextView) dialogView.findViewById(R.id.petugasPenjualanK);
                final TextView tgl = (TextView) dialogView.findViewById(R.id.tanggalPenjuakanK);
                final TextView hj = (TextView) dialogView.findViewById(R.id.hjPenjualanK);
                final ImageView img = (ImageView) dialogView.findViewById(R.id.img_Dialogpenjualan);

                this.id.toString();
                RequestOptions options = new RequestOptions()
                        .fitCenter()
                        .error(R.drawable.ic_picture)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .priority(Priority.HIGH);

                Glide.with(activity.getApplicationContext())
                        .load(imgBarang.getDrawable())
                        .apply(options)
                        .into(img);
                barang.setText(txNamaBarang.getText().toString());
                penjualan.setText(txJual.getText().toString());
                jmlbar.setText("Jumlah : "+txJmlBar.getText().toString());
//                petugas.setText(this.petugas);
                tgl.setText(this.txTgl.getText().toString());
                hj.setText("Rp. "+ doubleToStringNoDecimal(Double.parseDouble(this.hj)));

                dialogBuilder.setNegativeButton("CEK TRANSAKSI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Context context = activity;
                        Intent intent = new Intent(context, TokoDetailRecTransaksi.class);
                        intent.putExtra("id",idtransaksi);
                        context.startActivity(intent);
                    }
                });
                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
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
