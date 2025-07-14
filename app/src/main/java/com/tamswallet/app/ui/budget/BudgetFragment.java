package com.tamswallet.app.ui.budget;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tamswallet.app.R;

public class BudgetFragment extends Fragment {
    
    private RecyclerView recyclerView;
    private FloatingActionButton fabAddBudget;
    
    // TODO: Add adapter and ViewModel
    // private BudgetAdapter adapter;
    // private BudgetViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, container, false);
        
        initViews(view);
        setupRecyclerView();
        setupFab();
        observeData();
        
        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        fabAddBudget = view.findViewById(R.id.fabAddBudget);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // TODO: Initialize and set adapter
        // adapter = new BudgetAdapter();
        // recyclerView.setAdapter(adapter);
    }

    private void setupFab() {
        fabAddBudget.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), AddBudgetActivity.class));
        });
    }

    private void observeData() {
        // TODO: Observe LiveData from ViewModel
        // viewModel.getBudgets().observe(getViewLifecycleOwner(), budgets -> {
        //     adapter.submitList(budgets);
        // });
    }
}