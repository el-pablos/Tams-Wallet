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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.github.mikephil.charting.charts.PieChart;
import com.tamswallet.app.R;
import com.tamswallet.app.data.repository.TransactionRepository;
import com.tamswallet.app.utils.CurrencyUtils;
import com.tamswallet.app.utils.SessionManager;

public class DashboardFragment extends Fragment {
    
    private TextView tvTotalBalance, tvTodayIncome, tvTodayExpense;
    private CardView cardTodayIncome, cardTodayExpense;
    private PieChart pieChart;
    
    private TransactionRepository transactionRepository;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        
        transactionRepository = TransactionRepository.getInstance(getContext());
        sessionManager = new SessionManager(getContext());
        
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
        long userId = sessionManager.getUserId();
        
        // Observe total income
        transactionRepository.getTotalIncomeByUserId(userId).observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double totalIncome) {
                if (totalIncome == null) totalIncome = 0.0;
                updateBalance(totalIncome);
            }
        });
        
        // Observe total expense
        transactionRepository.getTotalExpenseByUserId(userId).observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double totalExpense) {
                if (totalExpense == null) totalExpense = 0.0;
                updateBalance(totalExpense);
            }
        });
        
        // Observe today's income
        transactionRepository.getTodayIncomeByUserId(userId).observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double todayIncome) {
                if (todayIncome == null) todayIncome = 0.0;
                tvTodayIncome.setText(CurrencyUtils.formatCurrency(todayIncome));
            }
        });
        
        // Observe today's expense
        transactionRepository.getTodayExpenseByUserId(userId).observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double todayExpense) {
                if (todayExpense == null) todayExpense = 0.0;
                tvTodayExpense.setText(CurrencyUtils.formatCurrency(todayExpense));
            }
        });
    }
    
    private void updateBalance(Double amount) {
        // This is a simplified version - you might want to calculate balance differently
        // For now, we'll just use the total balance from the repository
        transactionRepository.getTotalBalance().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double balance) {
                if (balance == null) balance = 0.0;
                tvTotalBalance.setText(CurrencyUtils.formatCurrency(balance));
            }
        });
    }
}