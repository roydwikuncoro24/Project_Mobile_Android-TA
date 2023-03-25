package com.example.stocki.helper;

import com.example.stocki.ModelData.BarangModelData;
import com.example.stocki.ModelData.BayarTanggunganModelData;

import java.util.Comparator;

public class SortbyIdBayarTanggunganDESC implements Comparator<BayarTanggunganModelData> {
    // Used for sorting in ascending order of
    // roll number
    public int compare(BayarTanggunganModelData a, BayarTanggunganModelData b)
    {
        return Integer.parseInt(b.getId()) - Integer.parseInt(a.getId());
    }
}
