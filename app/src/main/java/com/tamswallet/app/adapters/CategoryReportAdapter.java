package com.tamswallet.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.tamswallet.app.R;
import com.tamswallet.app.data.model.CategoryReport;
import com.tamswallet.app.utils.CurrencyUtils;
import java.util.List;

public class CategoryReportAdapter extends RecyclerView.Adapter<CategoryReportAdapter.CategoryReportViewHolder> {
    
    private List<CategoryReport> categoryReports;
    
    public CategoryReportAdapter(List<CategoryReport> categoryReports) {
        this.categoryReports = categoryReports;
    }
    
    public void updateCategoryReports(List<CategoryReport> newReports) {
        this.categoryReports = newReports;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public CategoryReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_category_report, parent, false);
        return new CategoryReportViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull CategoryReportViewHolder holder, int position) {
        CategoryReport report = categoryReports.get(position);
        holder.bind(report);
    }
    
    @Override
    public int getItemCount() {
        return categoryReports.size();
    }
    
    static class CategoryReportViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCategory;
        private TextView tvAmount;
        private TextView tvTransactionCount;
        private TextView tvPercentage;
        private ProgressBar progressBar;
        
        public CategoryReportViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvTransactionCount = itemView.findViewById(R.id.tvTransactionCount);
            tvPercentage = itemView.findViewById(R.id.tvPercentage);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
        
        public void bind(CategoryReport report) {
            tvCategory.setText(report.getCategory());
            tvAmount.setText(CurrencyUtils.formatCurrency(report.getTotalAmount()));
            tvTransactionCount.setText(report.getTransactionCount() + " transactions");
            tvPercentage.setText(String.format("%.1f%%", report.getPercentage()));
            
            // Set progress bar
            progressBar.setProgress((int) report.getPercentage());
        }
    }
}