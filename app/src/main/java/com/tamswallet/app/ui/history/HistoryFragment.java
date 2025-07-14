package com.tamswallet.app.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.tamswallet.app.R;

public class HistoryFragment extends Fragment {
    
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private TextInputEditText etSearch;
    
    // TODO: Add adapter and ViewModel
    // private TransactionHistoryAdapter adapter;
    // private HistoryViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        
        initViews(view);
        setupRecyclerView();
        setupSwipeRefresh();
        setupSearch();
        observeData();
        
        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        etSearch = view.findViewById(R.id.etSearch);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // TODO: Initialize and set adapter
        // adapter = new TransactionHistoryAdapter();
        // recyclerView.setAdapter(adapter);
        
        // TODO: Add item decoration for spacing
        // recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    private void setupSwipeRefresh() {
        swipeRefresh.setOnRefreshListener(() -> {
            // TODO: Refresh data
            // viewModel.refreshTransactions();
            swipeRefresh.setRefreshing(false);
        });
        
        // Set color scheme
        swipeRefresh.setColorSchemeResources(
                R.color.primary_color,
                R.color.secondary_color
        );
    }

    private void setupSearch() {
        // TODO: Implement search functionality
        // Add TextWatcher to etSearch
        // Filter transactions based on description or category
    }

    private void observeData() {
        // TODO: Observe LiveData from ViewModel
        // viewModel.getTransactions().observe(getViewLifecycleOwner(), transactions -> {
        //     adapter.submitList(transactions);
        // });
    }
}