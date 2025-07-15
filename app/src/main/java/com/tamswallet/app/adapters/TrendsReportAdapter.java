package com.tamswallet.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.tamswallet.app.R;
import com.tamswallet.app.data.model.MonthlyTrend;
import com.tamswallet.app.utils.CurrencyUtils;
import java.util.List;

public class TrendsReportAdapter extends RecyclerView.Adapter<TrendsReportAdapter.TrendsReportViewHolder> {
    
    private List<MonthlyTrend> monthlyTrends;
    
    public TrendsReportAdapter(List<MonthlyTrend> monthlyTrends) {
        this.monthlyTrends = monthlyTrends;
    }
    
    public void updateMonthlyTrends(List<MonthlyTrend> newTrends) {
        this.monthlyTrends = newTrends;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public TrendsReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_monthly_trend, parent, false);
        return new TrendsReportViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull TrendsReportViewHolder holder, int position) {
        MonthlyTrend trend = monthlyTrends.get(position);
        holder.bind(trend);
    }
    
    @Override
    public int getItemCount() {
        return monthlyTrends.size();
    }
    
    static class TrendsReportViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMonthYear;
        private TextView tvIncome;
        private TextView tvExpense;
        private TextView tvNetIncome;
        private TextView tvSavingsRate;
        private TextView tvTransactionCount;
        
        public TrendsReportViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMonthYear = itemView.findViewById(R.id.tvMonthYear);
            tvIncome = itemView.findViewById(R.id.tvIncome);
            tvExpense = itemView.findViewById(R.id.tvExpense);
            tvNetIncome = itemView.findViewById(R.id.tvNetIncome);
            tvSavingsRate = itemView.findViewById(R.id.tvSavingsRate);
            tvTransactionCount = itemView.findViewById(R.id.tvTransactionCount);
        }
        
        public void bind(MonthlyTrend trend) {
            tvMonthYear.setText(trend.getMonthYear());
            tvIncome.setText(CurrencyUtils.formatCurrency(trend.getIncome()));
            tvExpense.setText(CurrencyUtils.formatCurrency(trend.getExpense()));
            tvNetIncome.setText(CurrencyUtils.formatCurrency(trend.getNetIncome()));
            tvSavingsRate.setText(String.format("%.1f%%", trend.getSavingsRate()));
            tvTransactionCount.setText(trend.getTotalTransactions() + " transactions");
            
            // Set color based on positive/negative
            if (trend.isPositive()) {
                tvNetIncome.setTextColor(itemView.getContext().getResources().getColor(R.color.success_color));
            } else {
                tvNetIncome.setTextColor(itemView.getContext().getResources().getColor(R.color.error_color));
            }
        }
    }
}