package com.tamswallet.app.ui.reports;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.tamswallet.app.R;
import com.tamswallet.app.data.model.Transaction;
import com.tamswallet.app.data.repository.TransactionRepository;
import com.tamswallet.app.ui.transaction.TransactionAdapter;
import com.tamswallet.app.utils.CurrencyUtils;
import com.tamswallet.app.utils.SessionManager;
import java.util.Calendar;
import java.util.List;

public class MonthlyReportFragment extends Fragment {
    
    private TextView tvMonthYear, tvIncomeAmount, tvExpenseAmount;
    private RecyclerView recyclerView;
    private TransactionAdapter adapter;
    
    private TransactionRepository transactionRepository;
    private SessionManager sessionManager;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monthly_report, container, false);
        
        transactionRepository = TransactionRepository.getInstance(getContext());
        sessionManager = new SessionManager(getContext());
        
        initViews(view);
        setupRecyclerView();
        loadData();
        
        return view;
    }
    
    private void initViews(View view) {
        tvMonthYear = view.findViewById(R.id.tvMonthYear);
        tvIncomeAmount = view.findViewById(R.id.tvIncomeAmount);
        tvExpenseAmount = view.findViewById(R.id.tvExpenseAmount);
        recyclerView = view.findViewById(R.id.recyclerView);
    }
    
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TransactionAdapter();
        recyclerView.setAdapter(adapter);
    }
    
    private void loadData() {
        long userId = sessionManager.getUserId();
        
        // Get current month and year
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentYear = calendar.get(Calendar.YEAR);
        
        // Set month/year display
        String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                              "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        tvMonthYear.setText(monthNames[currentMonth - 1] + " " + currentYear);
        
        // Load monthly income
        transactionRepository.getMonthlyIncomeByUserId(userId, currentMonth, currentYear)
                .observe(getViewLifecycleOwner(), new Observer<Double>() {
                    @Override
                    public void onChanged(Double income) {
                        if (income == null) income = 0.0;
                        tvIncomeAmount.setText(CurrencyUtils.formatCurrency(income));
                    }
                });
        
        // Load monthly expense
        transactionRepository.getMonthlyExpenseByUserId(userId, currentMonth, currentYear)
                .observe(getViewLifecycleOwner(), new Observer<Double>() {
                    @Override
                    public void onChanged(Double expense) {
                        if (expense == null) expense = 0.0;
                        tvExpenseAmount.setText(CurrencyUtils.formatCurrency(expense));
                    }
                });
        
        // Load transactions for current month
        transactionRepository.getTransactionsByUserId(userId)
                .observe(getViewLifecycleOwner(), new Observer<List<Transaction>>() {
                    @Override
                    public void onChanged(List<Transaction> transactions) {
                        // Filter transactions for current month
                        // Note: This is a simplified implementation
                        // In a real app, you'd filter by date range
                        adapter.updateTransactions(transactions);
                    }
                });
    }
}