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

public class TrendsReportFragment extends Fragment {
    
    private TextView tvTrendsInfo;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trends_report, container, false);
        
        tvTrendsInfo = view.findViewById(R.id.tvTrendsInfo);
        
        // TODO: Implement spending trends analysis
        tvTrendsInfo.setText("Analisis tren pengeluaran akan ditampilkan di sini");
        
        return view;
    }
}