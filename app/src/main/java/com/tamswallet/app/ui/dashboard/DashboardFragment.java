package com.tamswallet.app.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.github.mikephil.charting.charts.PieChart;
import com.tamswallet.app.R;

public class DashboardFragment extends Fragment {
    
    private TextView tvTotalBalance, tvTodayIncome, tvTodayExpense;
    private CardView cardTodayIncome, cardTodayExpense;
    private PieChart pieChart;
    
    // TODO: Add ViewModel for dashboard data
    // private DashboardViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        
        initViews(view);
        setupChart();
        observeData();
        
        return view;
    }

    private void initViews(View view) {
        tvTotalBalance = view.findViewById(R.id.tvTotalBalance);
        tvTodayIncome = view.findViewById(R.id.tvTodayIncome);
        tvTodayExpense = view.findViewById(R.id.tvTodayExpense);
        cardTodayIncome = view.findViewById(R.id.cardTodayIncome);
        cardTodayExpense = view.findViewById(R.id.cardTodayExpense);
        pieChart = view.findViewById(R.id.pieChart);
    }

    private void setupChart() {
        // TODO: Configure MPAndroidChart PieChart
        // Set colors: green-blue theme as specified
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(getResources().getColor(R.color.background_color));
        pieChart.setTransparentCircleRadius(61f);
        
        // TODO: Load actual data and create PieData
    }

    private void observeData() {
        // TODO: Observe LiveData from ViewModel
        // Update balance, today's income/expense
        // Update chart data
        
        // Placeholder data
        tvTotalBalance.setText("Rp 5,250,000");
        tvTodayIncome.setText("Rp 150,000");
        tvTodayExpense.setText("Rp 75,000");
    }
}