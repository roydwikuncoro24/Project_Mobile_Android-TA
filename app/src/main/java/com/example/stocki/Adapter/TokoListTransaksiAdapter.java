package com.example.stocki.Adapter;

import android.app.Activity;
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
import com.example.stocki.AktifitasKaryawan.KaryawanListTransaksi;
import com.example.stocki.AktifitasToko.TokoListTransaksi;
import com.example.stocki.ModelData.TransaksiProses;
import com.example.stocki.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TokoListTransaksiAdapter extends RecyclerView.Adapter<TokoListTransaksiAdapter.HolderData> {

    private List<TransaksiProses> transaksiList;
    private Activity activity;

    public TokoListTransaksiAdapter(List<TransaksiProses> transaksiList, Activity activity) {
        this.transaksiList = transaksiList;
        this.activity = activity;
    }

    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_list_transaksi, parent, false);
        HolderData holderData = new HolderData(v);
        return holderData;
    }

    @Override
    public void onBindViewHolder(HolderData holder, int position) {
        TransaksiProses transaksi = transaksiList.get(position);
        int jum = Integer.parseInt(transaksi.getJumlah());
        holder.txJumlah.setText(String.valueOf(jum));
        holder.txHarga.setText("Rp " + doubleToStringNoDecimal(Double.parseDouble(transaksi.
                getJualtoko())));
        holder.txNama.setText(transaksi.getNamabarang());
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .error(R.drawable.ic_picture)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        Glide.with(activity.getApplicationContext())
                .load(transaksi.getImage())
                .apply(options)
                .into(holder.image);
        holder.posisi = position;
    }

    @Override
    public int getItemCount() {
        return transaksiList.size();
    }

    public class HolderData extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txJumlah;
        TextView txNama, txHarga, txStok;
        ImageView image;
        int posisi;

        public HolderData(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            txJumlah = (TextView)itemView.findViewById(R.id.tx_stokdb);
            txNama = (TextView)itemView.findViewById(R.id.tx_namadb);
            txHarga = (TextView)itemView.findViewById(R.id.tx_hargadb);
            image = (ImageView) itemView.findViewById(R.id.img_dbarangList);
        }

        @Override
        public void onClick(View v) {
            if(activity instanceof TokoListTransaksi){
                ((TokoListTransaksi)activity).showEditTransaksi(posisi, image.getDrawable(),
                        txNama.getText().toString(),txHarga.getText().toString(),
                        txJumlah.getText().toString());
            } else if(activity instanceof KaryawanListTransaksi){
                ((KaryawanListTransaksi)activity).showEditTransaksi(posisi, image.getDrawable(),
                        txNama.getText().toString(),txHarga.getText().toString(),
                        txJumlah.getText().toString());
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
        transaksiList.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<TransaksiProses> list) {
        transaksiList.addAll(list);
        notifyDataSetChanged();
    }

    public void updateList(List<TransaksiProses> list){
        transaksiList = list;
        notifyDataSetChanged();
    }
}
