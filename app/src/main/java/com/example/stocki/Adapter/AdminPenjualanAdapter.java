package com.example.stocki.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.stocki.ModelData.PenjualanModelData;
import com.example.stocki.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdminPenjualanAdapter extends RecyclerView.Adapter<AdminPenjualanAdapter.HolderData> {

    List<PenjualanModelData> resultList;
    Activity activity;

    public AdminPenjualanAdapter(ArrayList<PenjualanModelData> resultList, Activity activity) {
        this.resultList = resultList;
        this.activity = activity;
    }

    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_penjualan, parent, false);
        HolderData holderData = new HolderData(v);
        return holderData;
    }

    @Override
    public void onBindViewHolder(HolderData holder, int position) {
        PenjualanModelData pen = resultList.get(position);
        holder.txNamaBarang.setText(pen.getNamabarang());
        holder.txJual.setText("Rp " + doubleToStringNoDecimal(Double.parseDouble
                (pen.getJumhargajual())));
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
        laba = (Integer.parseInt(pen.getJumhargajual()) - (Integer.parseInt(pen.getJumhargadasar())));
        holder.junt = (laba);
        holder.jmod = pen.getJumhargadasar();

        holder.id = pen.getId();
        holder.idtransaksi = pen.getIdtransaksi();
//        holder.petuas = pen.getPetugas();
        holder.hj = pen.getHjual();
        holder.mod = pen.getHdasar();
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class HolderData extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txJual, txJmlBar, txTgl, txNamaBarang;
        public ImageView imgBarang;
        public String id, idtransaksi, jmod, petuas, hj, mod;
        public int junt;

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
//            final TextView petuas = (TextView) dialogView.findViewById(R.id.petugasDialogJual);
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
//            petuas.setText(this.petuas);
            hj.setText("Rp "+ doubleToStringNoDecimal(Double.parseDouble(this.hj)));
            mod.setText("Rp "+ doubleToStringNoDecimal(Double.parseDouble(this.mod)));

//            dialogBuilder.setNegativeButton("CEK TRANSAKSI", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Context context = activity;
//                    Intent intent = new Intent(context, TokoDetailRecTransaksi.class);
//                    intent.putExtra("id",idtransaksi);
//                    context.startActivity(intent);
//                }
//            });
            dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
        }

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
    public void addAll(List<PenjualanModelData> list) {
        resultList.addAll(list);
        notifyDataSetChanged();
    }

    public void updateList(ArrayList<PenjualanModelData> list) {
        resultList = list;
        notifyDataSetChanged();
    }
}
