package com.example.stocki.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stocki.ModelData.BayarTanggunganModelData;
import com.example.stocki.ModelData.TanggunganModelData;
import com.example.stocki.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TokoTanggunganAdapter extends RecyclerView.Adapter<TokoTanggunganAdapter.HolderData>{

    List<BayarTanggunganModelData> resultBayar;
    Context context;

    public TokoTanggunganAdapter(ArrayList<BayarTanggunganModelData> resultBayar, Context context) {
        this.resultBayar = resultBayar;
        this.context = context;
    }

    @Override
    public TokoTanggunganAdapter.HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_pembayaran, parent, false);
        TokoTanggunganAdapter.HolderData holderData = new TokoTanggunganAdapter.HolderData(v);
        return holderData;
    }

    @Override
    public void onBindViewHolder(HolderData holder, int position) {
        BayarTanggunganModelData ptmData = resultBayar.get(position);
        holder.tvBayar.setText("Rp " + doubleToStringNoDecimal(Double.parseDouble(ptmData.getPembayaran())));
        holder.tvToko.setText(ptmData.getNamatoko());
        holder.tvTanggal.setText(ptmData.getTanggal());
        holder.id = ptmData.getId();
    }

    @Override
    public int getItemCount() {
        return resultBayar.size();
    }

    public class HolderData extends RecyclerView.ViewHolder{

        public TextView tvTangBay, tvBayar, tvToko, tvTanggal;
        public String id, idhutpit;

        public HolderData(View itemView) {
            super(itemView);
            tvTangBay = (TextView) itemView.findViewById(R.id.tx_ketTanggungan);
            tvBayar = (TextView) itemView.findViewById(R.id.tx_sjumlah_bayartanggungan);
            tvToko = (TextView) itemView.findViewById(R.id.tx_ketToko);
            tvTanggal = (TextView) itemView.findViewById(R.id.tx_tgl_bayar);
        }
    }

    public void updateList(ArrayList<BayarTanggunganModelData> list) {
        resultBayar = list;
        notifyDataSetChanged();
    }

    public static String doubleToStringNoDecimal(double d) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter .applyPattern("#,###");
        return formatter.format(d).replace(",",".");
    }
    // Clean all elements of the recycler
    public void clear() {
        resultBayar.clear();
        notifyDataSetChanged();
    }
    // Add a list of items -- change to type used
    public void addAll(List<BayarTanggunganModelData> list) {
        resultBayar.addAll(list);
        notifyDataSetChanged();
    }
}
