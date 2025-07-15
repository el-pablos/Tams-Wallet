package com.tamswallet.app.ui.budget;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tamswallet.app.R;
import com.tamswallet.app.data.model.Budget;
import com.tamswallet.app.data.repository.BudgetRepository;
import com.tamswallet.app.utils.SessionManager;

public class AddBudgetActivity extends AppCompatActivity {
    
    private TextInputLayout tilCategory, tilLimit, tilPeriod;
    private AutoCompleteTextView actvCategory, actvPeriod;
    private TextInputEditText etLimit;
    private MaterialButton btnSave;
    
    private String[] categories = {"Makanan", "Transport", "Belanja", "Tagihan", "Hiburan", "Kesehatan", "Lainnya"};
    private String[] periods = {"Mingguan", "Bulanan"};
    
    private BudgetRepository budgetRepository;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_budget);

        budgetRepository = BudgetRepository.getInstance(this);
        sessionManager = new SessionManager(this);

        initViews();
        setupDropdowns();
        setupClickListeners();
    }

    private void initViews() {
        tilCategory = findViewById(R.id.tilCategory);
        tilLimit = findViewById(R.id.tilLimit);
        tilPeriod = findViewById(R.id.tilPeriod);
        
        actvCategory = findViewById(R.id.actvCategory);
        actvPeriod = findViewById(R.id.actvPeriod);
        etLimit = findViewById(R.id.etLimit);
        
        btnSave = findViewById(R.id.btnSave);
    }

    private void setupDropdowns() {
        // Category dropdown
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, categories);
        actvCategory.setAdapter(categoryAdapter);
        
        // Period dropdown
        ArrayAdapter<String> periodAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, periods);
        actvPeriod.setAdapter(periodAdapter);
    }

    private void setupClickListeners() {
        btnSave.setOnClickListener(v -> saveBudget());
    }

    private void saveBudget() {
        // Reset errors
        tilCategory.setError(null);
        tilLimit.setError(null);
        tilPeriod.setError(null);

        String category = actvCategory.getText().toString().trim();
        String limitStr = etLimit.getText().toString().trim();
        String period = actvPeriod.getText().toString().trim();

        boolean isValid = true;

        if (TextUtils.isEmpty(category)) {
            tilCategory.setError("Kategori harus dipilih");
            isValid = false;
        }

        if (TextUtils.isEmpty(limitStr)) {
            tilLimit.setError("Limit anggaran tidak boleh kosong");
            isValid = false;
        }

        if (TextUtils.isEmpty(period)) {
            tilPeriod.setError("Periode harus dipilih");
            isValid = false;
        }

        if (isValid) {
            try {
                double limit = Double.parseDouble(limitStr);
                
                // Disable save button to prevent multiple saves
                btnSave.setEnabled(false);
                btnSave.setText("Menyimpan...");
                
                // Create budget object
                Budget budget = new Budget(category, limit, period.toLowerCase());
                budget.setUserId(sessionManager.getUserId());
                
                // Save to database
                budgetRepository.insertBudget(budget, new BudgetRepository.BudgetCallback() {
                    @Override
                    public void onSuccess(long budgetId) {
                        runOnUiThread(() -> {
                            Toast.makeText(AddBudgetActivity.this, "Budget berhasil disimpan", Toast.LENGTH_SHORT).show();
                            finish();
                        });
                    }

                    @Override
                    public void onError(String error) {
                        runOnUiThread(() -> {
                            // Re-enable save button
                            btnSave.setEnabled(true);
                            btnSave.setText("Simpan");
                            
                            Toast.makeText(AddBudgetActivity.this, error, Toast.LENGTH_LONG).show();
                        });
                    }
                });
                
            } catch (NumberFormatException e) {
                tilLimit.setError("Format limit tidak valid");
            }
        }
    }
}