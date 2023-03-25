package com.example.stocki.helper;

import com.example.stocki.ModelData.PenjualanModelData;

import java.util.Comparator;

public class SortbyIdPenjualanDESC implements Comparator<PenjualanModelData> {
    // Used for sorting in ascending order of
    // roll number
    public int compare(PenjualanModelData a, PenjualanModelData b)
    {
        return Integer.parseInt(b.getId()) - Integer.parseInt(a.getId());
    }
}
