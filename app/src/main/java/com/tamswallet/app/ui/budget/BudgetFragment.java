package com.tamswallet.app.ui.budget;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tamswallet.app.R;
import com.tamswallet.app.data.model.Budget;
import com.tamswallet.app.data.repository.BudgetRepository;
import com.tamswallet.app.utils.SessionManager;
import java.util.List;

public class BudgetFragment extends Fragment {
    
    private RecyclerView recyclerView;
    private FloatingActionButton fabAddBudget;
    
    private BudgetAdapter adapter;
    private BudgetRepository budgetRepository;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, container, false);
        
        budgetRepository = BudgetRepository.getInstance(getContext());
        sessionManager = new SessionManager(getContext());
        
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
        
        adapter = new BudgetAdapter();
        recyclerView.setAdapter(adapter);
        
        // Set click listeners
        adapter.setOnBudgetClickListener(new BudgetAdapter.OnBudgetClickListener() {
            @Override
            public void onBudgetClick(Budget budget) {
                // TODO: Navigate to budget details or edit
                Toast.makeText(getContext(), "Budget: " + budget.getCategory(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBudgetLongClick(Budget budget) {
                // TODO: Show options dialog (edit, delete)
                showBudgetOptionsDialog(budget);
            }
        });
    }

    private void setupFab() {
        fabAddBudget.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), AddBudgetActivity.class));
        });
    }

    private void observeData() {
        long userId = sessionManager.getUserId();
        budgetRepository.getBudgetsByUserId(userId).observe(getViewLifecycleOwner(), new Observer<List<Budget>>() {
            @Override
            public void onChanged(List<Budget> budgets) {
                adapter.updateBudgets(budgets);
            }
        });
    }
    
    private void showBudgetOptionsDialog(Budget budget) {
        // TODO: Implement options dialog for edit/delete
        Toast.makeText(getContext(), "Long clicked on: " + budget.getCategory(), Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Refresh data when returning from AddBudgetActivity
        observeData();
    }
}