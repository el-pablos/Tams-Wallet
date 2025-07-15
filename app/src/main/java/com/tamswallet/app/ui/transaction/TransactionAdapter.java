package com.tamswallet.app.ui.transaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.tamswallet.app.R;
import com.tamswallet.app.data.model.Transaction;
import com.tamswallet.app.utils.CurrencyUtils;
import com.tamswallet.app.utils.DateUtils;
import java.util.ArrayList;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    
    private List<Transaction> transactions;
    private OnTransactionClickListener listener;
    
    public interface OnTransactionClickListener {
        void onTransactionClick(Transaction transaction);
        void onTransactionLongClick(Transaction transaction);
    }
    
    public TransactionAdapter() {
        this.transactions = new ArrayList<>();
    }
    
    public void setOnTransactionClickListener(OnTransactionClickListener listener) {
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.bind(transaction);
        
        // Set click listeners
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTransactionClick(transaction);
            }
        });
        
        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onTransactionLongClick(transaction);
            }
            return true;
        });
    }
    
    @Override
    public int getItemCount() {
        return transactions.size();
    }
    
    public void updateTransactions(List<Transaction> newTransactions) {
        this.transactions.clear();
        if (newTransactions != null) {
            this.transactions.addAll(newTransactions);
        }
        notifyDataSetChanged();
    }
    
    public void addTransaction(Transaction transaction) {
        transactions.add(0, transaction); // Add to beginning
        notifyItemInserted(0);
    }
    
    public void removeTransaction(int position) {
        if (position >= 0 && position < transactions.size()) {
            transactions.remove(position);
            notifyItemRemoved(position);
        }
    }
    
    public Transaction getTransaction(int position) {
        if (position >= 0 && position < transactions.size()) {
            return transactions.get(position);
        }
        return null;
    }
    
    public class TransactionViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCategory, tvDescription, tvAmount, tvDate;
        private View indicatorView;
        
        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDate = itemView.findViewById(R.id.tvDate);
            indicatorView = itemView.findViewById(R.id.indicatorView);
        }
        
        public void bind(Transaction transaction) {
            tvCategory.setText(transaction.getCategory());
            tvDescription.setText(transaction.getDescription());
            tvDate.setText(DateUtils.formatDate(transaction.getDate()));
            
            // Format amount with currency
            String formattedAmount = CurrencyUtils.formatCurrency(transaction.getAmount());
            tvAmount.setText(formattedAmount);
            
            // Set color based on transaction type
            if (transaction.getType().equals("income")) {
                tvAmount.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.success_color));
                indicatorView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.success_color));
            } else {
                tvAmount.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.error_color));
                indicatorView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.error_color));
            }
        }
    }
}