package com.tamswallet.app.ui.reports;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.tamswallet.app.R;

public class CategoryReportFragment extends Fragment {
    
    private TextView tvCategoryInfo;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_report, container, false);
        
        tvCategoryInfo = view.findViewById(R.id.tvCategoryInfo);
        
        // TODO: Implement category-wise spending analysis
        tvCategoryInfo.setText("Laporan pengeluaran per kategori akan ditampilkan di sini");
        
        return view;
    }
}