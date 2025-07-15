package com.tamswallet.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.tamswallet.app.R;
import com.tamswallet.app.utils.BackupRestoreUtils;
import java.util.List;

public class BackupFilesAdapter extends RecyclerView.Adapter<BackupFilesAdapter.BackupFileViewHolder> {
    
    private List<BackupRestoreUtils.BackupFileInfo> backupFiles;
    private OnItemClickListener onItemClickListener;
    
    public BackupFilesAdapter(List<BackupRestoreUtils.BackupFileInfo> backupFiles) {
        this.backupFiles = backupFiles;
    }
    
    public void updateBackupFiles(List<BackupRestoreUtils.BackupFileInfo> newBackupFiles) {
        this.backupFiles = newBackupFiles;
        notifyDataSetChanged();
    }
    
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    
    @NonNull
    @Override
    public BackupFileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_backup_file, parent, false);
        return new BackupFileViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull BackupFileViewHolder holder, int position) {
        BackupRestoreUtils.BackupFileInfo backupFile = backupFiles.get(position);
        holder.bind(backupFile);
        
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(backupFile, position);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return backupFiles.size();
    }
    
    static class BackupFileViewHolder extends RecyclerView.ViewHolder {
        private TextView tvFileName;
        private TextView tvFileSize;
        private TextView tvCreatedAt;
        
        public BackupFileViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFileName = itemView.findViewById(R.id.tvFileName);
            tvFileSize = itemView.findViewById(R.id.tvFileSize);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
        }
        
        public void bind(BackupRestoreUtils.BackupFileInfo backupFile) {
            tvFileName.setText(backupFile.fileName);
            tvFileSize.setText(backupFile.getFormattedSize());
            tvCreatedAt.setText(backupFile.getFormattedDate());
        }
    }
    
    public interface OnItemClickListener {
        void onItemClick(BackupRestoreUtils.BackupFileInfo backupFile, int position);
    }
}