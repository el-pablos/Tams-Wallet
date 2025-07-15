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
import com.tamswallet.app.adapters.CategoryReportAdapter;
import com.tamswallet.app.data.model.CategoryReport;
import com.tamswallet.app.viewmodels.ReportsViewModel;
import com.tamswallet.app.utils.CurrencyUtils;
import java.util.ArrayList;
import java.util.List;

public class CategoryReportFragment extends Fragment {
    
    private TextView tvTotalSpending;
    private TextView tvTopCategory;
    private RecyclerView recyclerViewCategories;
    private CategoryReportAdapter categoryReportAdapter;
    private ReportsViewModel reportsViewModel;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reportsViewModel = new ViewModelProvider(this).get(ReportsViewModel.class);
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_report, container, false);
        
        initViews(view);
        setupRecyclerView();
        observeData();
        
        return view;
    }
    
    private void initViews(View view) {
        tvTotalSpending = view.findViewById(R.id.tvTotalSpending);
        tvTopCategory = view.findViewById(R.id.tvTopCategory);
        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);
    }
    
    private void setupRecyclerView() {
        categoryReportAdapter = new CategoryReportAdapter(new ArrayList<>());
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewCategories.setAdapter(categoryReportAdapter);
    }
    
    private void observeData() {
        reportsViewModel.getCategoryReports().observe(getViewLifecycleOwner(), categoryReports -> {
            if (categoryReports != null) {
                updateUI(categoryReports);
            }
        });
        
        reportsViewModel.loadCategoryReports();
    }
    
    private void updateUI(List<CategoryReport> categoryReports) {
        if (categoryReports.isEmpty()) {
            tvTotalSpending.setText(CurrencyUtils.formatCurrency(0));
            tvTopCategory.setText("No data available");
            return;
        }
        
        double totalSpending = 0;
        CategoryReport topCategory = categoryReports.get(0);
        
        for (CategoryReport report : categoryReports) {
            totalSpending += report.getTotalAmount();
            if (report.getTotalAmount() > topCategory.getTotalAmount()) {
                topCategory = report;
            }
        }
        
        tvTotalSpending.setText(CurrencyUtils.formatCurrency(totalSpending));
        tvTopCategory.setText(topCategory.getCategory() + " (" + 
            CurrencyUtils.formatCurrency(topCategory.getTotalAmount()) + ")");
        
        categoryReportAdapter.updateCategoryReports(categoryReports);
    }
}