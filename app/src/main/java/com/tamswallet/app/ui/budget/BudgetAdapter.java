package com.tamswallet.app.ui.budget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.tamswallet.app.R;
import com.tamswallet.app.data.model.Budget;
import com.tamswallet.app.utils.CurrencyUtils;
import java.util.ArrayList;
import java.util.List;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder> {
    
    private List<Budget> budgets;
    private OnBudgetClickListener listener;
    
    public interface OnBudgetClickListener {
        void onBudgetClick(Budget budget);
        void onBudgetLongClick(Budget budget);
    }
    
    public BudgetAdapter() {
        this.budgets = new ArrayList<>();
    }
    
    public void setOnBudgetClickListener(OnBudgetClickListener listener) {
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_budget, parent, false);
        return new BudgetViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, int position) {
        Budget budget = budgets.get(position);
        holder.bind(budget);
        
        // Set click listeners
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBudgetClick(budget);
            }
        });
        
        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onBudgetLongClick(budget);
            }
            return true;
        });
    }
    
    @Override
    public int getItemCount() {
        return budgets.size();
    }
    
    public void updateBudgets(List<Budget> newBudgets) {
        this.budgets.clear();
        if (newBudgets != null) {
            this.budgets.addAll(newBudgets);
        }
        notifyDataSetChanged();
    }
    
    public void addBudget(Budget budget) {
        budgets.add(0, budget); // Add to beginning
        notifyItemInserted(0);
    }
    
    public void removeBudget(int position) {
        if (position >= 0 && position < budgets.size()) {
            budgets.remove(position);
            notifyItemRemoved(position);
        }
    }
    
    public Budget getBudget(int position) {
        if (position >= 0 && position < budgets.size()) {
            return budgets.get(position);
        }
        return null;
    }
    
    public class BudgetViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCategory, tvPeriod, tvSpent, tvLimit, tvRemaining, tvPercentage;
        private ProgressBar progressBar;
        private View statusIndicator;
        
        public BudgetViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvPeriod = itemView.findViewById(R.id.tvPeriod);
            tvSpent = itemView.findViewById(R.id.tvSpent);
            tvLimit = itemView.findViewById(R.id.tvLimit);
            tvRemaining = itemView.findViewById(R.id.tvRemaining);
            tvPercentage = itemView.findViewById(R.id.tvPercentage);
            progressBar = itemView.findViewById(R.id.progressBar);
            statusIndicator = itemView.findViewById(R.id.statusIndicator);
        }
        
        public void bind(Budget budget) {
            tvCategory.setText(budget.getCategory());
            tvPeriod.setText(budget.getPeriod());
            tvSpent.setText(CurrencyUtils.formatCurrency(budget.getSpent()));
            tvLimit.setText(CurrencyUtils.formatCurrency(budget.getLimit()));
            
            // Calculate remaining and percentage
            double remaining = budget.getRemainingBudget();
            double percentage = budget.getPercentageUsed();
            
            tvRemaining.setText(CurrencyUtils.formatCurrency(remaining));
            tvPercentage.setText(String.format("%.1f%%", percentage));
            
            // Set progress bar
            progressBar.setProgress((int) percentage);
            
            // Set status indicator color based on budget status
            if (percentage >= 100) {
                // Exceeded budget
                statusIndicator.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.error_color));
                progressBar.setProgressTintList(ContextCompat.getColorStateList(itemView.getContext(), R.color.error_color));
                tvPercentage.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.error_color));
            } else if (percentage >= 80) {
                // Warning - close to limit
                statusIndicator.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.warning_color));
                progressBar.setProgressTintList(ContextCompat.getColorStateList(itemView.getContext(), R.color.warning_color));
                tvPercentage.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.warning_color));
            } else {
                // Normal - within budget
                statusIndicator.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.success_color));
                progressBar.setProgressTintList(ContextCompat.getColorStateList(itemView.getContext(), R.color.success_color));
                tvPercentage.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.success_color));
            }
            
            // Set remaining amount color
            if (remaining < 0) {
                tvRemaining.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.error_color));
            } else {
                tvRemaining.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.success_color));
            }
        }
    }
}