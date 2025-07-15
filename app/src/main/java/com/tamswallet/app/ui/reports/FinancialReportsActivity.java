package com.tamswallet.app.ui.reports;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.tamswallet.app.R;
import com.tamswallet.app.data.repository.TransactionRepository;
import com.tamswallet.app.utils.CurrencyUtils;
import com.tamswallet.app.utils.SessionManager;
import java.util.Calendar;

public class FinancialReportsActivity extends AppCompatActivity {
    
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private TextView tvTotalIncome, tvTotalExpense, tvNetIncome;
    
    private TransactionRepository transactionRepository;
    private SessionManager sessionManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial_reports);
        
        transactionRepository = TransactionRepository.getInstance(this);
        sessionManager = new SessionManager(this);
        
        initViews();
        setupViewPager();
        loadSummaryData();
    }
    
    private void initViews() {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        tvTotalIncome = findViewById(R.id.tvTotalIncome);
        tvTotalExpense = findViewById(R.id.tvTotalExpense);
        tvNetIncome = findViewById(R.id.tvNetIncome);
    }
    
    private void setupViewPager() {
        ReportsViewPagerAdapter adapter = new ReportsViewPagerAdapter(this);
        viewPager.setAdapter(adapter);
        
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Monthly");
                    break;
                case 1:
                    tab.setText("Category");
                    break;
                case 2:
                    tab.setText("Trends");
                    break;
            }
        }).attach();
    }
    
    private void loadSummaryData() {
        long userId = sessionManager.getUserId();
        
        // Get current month and year
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentYear = calendar.get(Calendar.YEAR);
        
        // Load monthly income
        transactionRepository.getMonthlyIncomeByUserId(userId, currentMonth, currentYear)
                .observe(this, new Observer<Double>() {
                    @Override
                    public void onChanged(Double income) {
                        if (income == null) income = 0.0;
                        tvTotalIncome.setText(CurrencyUtils.formatCurrency(income));
                        updateNetIncome();
                    }
                });
        
        // Load monthly expense
        transactionRepository.getMonthlyExpenseByUserId(userId, currentMonth, currentYear)
                .observe(this, new Observer<Double>() {
                    @Override
                    public void onChanged(Double expense) {
                        if (expense == null) expense = 0.0;
                        tvTotalExpense.setText(CurrencyUtils.formatCurrency(expense));
                        updateNetIncome();
                    }
                });
    }
    
    private void updateNetIncome() {
        try {
            double income = CurrencyUtils.parseCurrency(tvTotalIncome.getText().toString());
            double expense = CurrencyUtils.parseCurrency(tvTotalExpense.getText().toString());
            double netIncome = income - expense;
            
            tvNetIncome.setText(CurrencyUtils.formatCurrencyWithSign(netIncome));
            
            // Set color based on net income
            if (netIncome > 0) {
                tvNetIncome.setTextColor(getResources().getColor(R.color.success_color));
            } else if (netIncome < 0) {
                tvNetIncome.setTextColor(getResources().getColor(R.color.error_color));
            } else {
                tvNetIncome.setTextColor(getResources().getColor(R.color.text_secondary));
            }
        } catch (Exception e) {
            // Handle error
            tvNetIncome.setText("Rp 0");
        }
    }
}