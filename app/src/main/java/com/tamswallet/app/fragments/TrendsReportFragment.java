package com.tamswallet.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.tamswallet.app.R;
import com.tamswallet.app.adapters.TrendsReportAdapter;
import com.tamswallet.app.data.model.MonthlyTrend;
import com.tamswallet.app.viewmodels.ReportsViewModel;
import com.tamswallet.app.utils.CurrencyUtils;
import java.util.ArrayList;
import java.util.List;

public class TrendsReportFragment extends Fragment {
    
    private TextView tvAverageIncome;
    private TextView tvAverageExpense;
    private TextView tvTrendDirection;
    private RecyclerView recyclerViewTrends;
    private TrendsReportAdapter trendsReportAdapter;
    private ReportsViewModel reportsViewModel;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reportsViewModel = new ViewModelProvider(this).get(ReportsViewModel.class);
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trends_report, container, false);
        
        initViews(view);
        setupRecyclerView();
        observeData();
        
        return view;
    }
    
    private void initViews(View view) {
        tvAverageIncome = view.findViewById(R.id.tvAverageIncome);
        tvAverageExpense = view.findViewById(R.id.tvAverageExpense);
        tvTrendDirection = view.findViewById(R.id.tvTrendDirection);
        recyclerViewTrends = view.findViewById(R.id.recyclerViewTrends);
    }
    
    private void setupRecyclerView() {
        trendsReportAdapter = new TrendsReportAdapter(new ArrayList<>());
        recyclerViewTrends.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewTrends.setAdapter(trendsReportAdapter);
    }
    
    private void observeData() {
        reportsViewModel.getMonthlyTrends().observe(getViewLifecycleOwner(), monthlyTrends -> {
            if (monthlyTrends != null) {
                updateUI(monthlyTrends);
            }
        });
        
        reportsViewModel.loadMonthlyTrends();
    }
    
    private void updateUI(List<MonthlyTrend> monthlyTrends) {
        if (monthlyTrends.isEmpty()) {
            tvAverageIncome.setText(CurrencyUtils.formatCurrency(0));
            tvAverageExpense.setText(CurrencyUtils.formatCurrency(0));
            tvTrendDirection.setText("No data available");
            return;
        }
        
        double totalIncome = 0;
        double totalExpense = 0;
        
        for (MonthlyTrend trend : monthlyTrends) {
            totalIncome += trend.getIncome();
            totalExpense += trend.getExpense();
        }
        
        double averageIncome = totalIncome / monthlyTrends.size();
        double averageExpense = totalExpense / monthlyTrends.size();
        
        tvAverageIncome.setText(CurrencyUtils.formatCurrency(averageIncome));
        tvAverageExpense.setText(CurrencyUtils.formatCurrency(averageExpense));
        
        // Calculate trend direction
        if (monthlyTrends.size() >= 2) {
            MonthlyTrend latest = monthlyTrends.get(monthlyTrends.size() - 1);
            MonthlyTrend previous = monthlyTrends.get(monthlyTrends.size() - 2);
            
            double latestNet = latest.getIncome() - latest.getExpense();
            double previousNet = previous.getIncome() - previous.getExpense();
            
            if (latestNet > previousNet) {
                tvTrendDirection.setText("📈 Improving");
                tvTrendDirection.setTextColor(getResources().getColor(R.color.success_color));
            } else if (latestNet < previousNet) {
                tvTrendDirection.setText("📉 Declining");
                tvTrendDirection.setTextColor(getResources().getColor(R.color.error_color));
            } else {
                tvTrendDirection.setText("➡️ Stable");
                tvTrendDirection.setTextColor(getResources().getColor(R.color.warning_color));
            }
        } else {
            tvTrendDirection.setText("Not enough data");
        }
        
        trendsReportAdapter.updateMonthlyTrends(monthlyTrends);
    }
}