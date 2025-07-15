package com.tamswallet.app.ui.history;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.tamswallet.app.R;
import com.tamswallet.app.data.model.Transaction;
import com.tamswallet.app.data.repository.TransactionRepository;
import com.tamswallet.app.ui.transaction.TransactionAdapter;
import com.tamswallet.app.ui.transaction.EditTransactionActivity;
import com.tamswallet.app.utils.SessionManager;
import java.util.List;

public class HistoryFragment extends Fragment {
    
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private TextInputEditText etSearch;
    
    private TransactionAdapter adapter;
    private TransactionRepository transactionRepository;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        
        transactionRepository = TransactionRepository.getInstance(getContext());
        sessionManager = new SessionManager(getContext());
        
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
        
        adapter = new TransactionAdapter();
        recyclerView.setAdapter(adapter);
        
        // Set click listeners
        adapter.setOnTransactionClickListener(new TransactionAdapter.OnTransactionClickListener() {
            @Override
            public void onTransactionClick(Transaction transaction) {
                // Navigate to edit transaction
                Intent intent = new Intent(getContext(), EditTransactionActivity.class);
                intent.putExtra("transaction_id", transaction.getId());
                startActivity(intent);
            }

            @Override
            public void onTransactionLongClick(Transaction transaction) {
                // Show options dialog (edit, delete)
                showTransactionOptionsDialog(transaction);
            }
        });
    }

    private void setupSwipeRefresh() {
        swipeRefresh.setOnRefreshListener(() -> {
            // Refresh data by re-observing
            observeData();
            swipeRefresh.setRefreshing(false);
        });
        
        // Set color scheme
        swipeRefresh.setColorSchemeResources(
                R.color.primary_color,
                R.color.secondary_color
        );
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().trim();
                if (!query.isEmpty()) {
                    // Search transactions
                    transactionRepository.searchTransactions(query).observe(getViewLifecycleOwner(), new Observer<List<Transaction>>() {
                        @Override
                        public void onChanged(List<Transaction> transactions) {
                            adapter.updateTransactions(transactions);
                        }
                    });
                } else {
                    // Show all transactions
                    observeData();
                }
            }
        });
    }

    private void observeData() {
        long userId = sessionManager.getUserId();
        transactionRepository.getTransactionsByUserId(userId).observe(getViewLifecycleOwner(), new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {
                adapter.updateTransactions(transactions);
            }
        });
    }
    
    private void showTransactionOptionsDialog(Transaction transaction) {
        // TODO: Implement options dialog for edit/delete
        Toast.makeText(getContext(), "Long clicked on: " + transaction.getDescription(), Toast.LENGTH_SHORT).show();
    }
}