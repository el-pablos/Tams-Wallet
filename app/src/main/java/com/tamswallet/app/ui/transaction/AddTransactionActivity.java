package com.tamswallet.app.ui.transaction;

import android.app.DatePickerDialog;
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
import com.tamswallet.app.data.model.Transaction;
import com.tamswallet.app.data.repository.TransactionRepository;
import com.tamswallet.app.utils.SessionManager;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTransactionActivity extends AppCompatActivity {
    
    private TextInputLayout tilAmount, tilType, tilCategory, tilDate, tilDescription;
    private TextInputEditText etAmount, etDate, etDescription;
    private AutoCompleteTextView actvType, actvCategory;
    private MaterialButton btnSave;
    
    private Calendar selectedDate;
    private String[] transactionTypes = {"Income", "Expense"};
    private String[] incomeCategories = {"Gaji", "Freelance", "Investasi", "Lainnya"};
    private String[] expenseCategories = {"Makanan", "Transport", "Belanja", "Tagihan", "Hiburan", "Kesehatan", "Lainnya"};
    
    private TransactionRepository transactionRepository;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        transactionRepository = TransactionRepository.getInstance(this);
        sessionManager = new SessionManager(this);

        initViews();
        setupDropdowns();
        setupDatePicker();
        setupValidation();
        setupClickListeners();
    }

    private void initViews() {
        tilAmount = findViewById(R.id.tilAmount);
        tilType = findViewById(R.id.tilType);
        tilCategory = findViewById(R.id.tilCategory);
        tilDate = findViewById(R.id.tilDate);
        tilDescription = findViewById(R.id.tilDescription);
        
        etAmount = findViewById(R.id.etAmount);
        etDate = findViewById(R.id.etDate);
        etDescription = findViewById(R.id.etDescription);
        
        actvType = findViewById(R.id.actvType);
        actvCategory = findViewById(R.id.actvCategory);
        
        btnSave = findViewById(R.id.btnSave);
        
        selectedDate = Calendar.getInstance();
    }

    private void setupDropdowns() {
        // Transaction type dropdown
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, 
                android.R.layout.simple_dropdown_item_1line, transactionTypes);
        actvType.setAdapter(typeAdapter);
        
        // Handle type selection to update categories
        actvType.setOnItemClickListener((parent, view, position, id) -> {
            String selectedType = transactionTypes[position];
            updateCategoryOptions(selectedType);
        });
    }

    private void updateCategoryOptions(String type) {
        String[] categories = type.equals("Income") ? incomeCategories : expenseCategories;
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, categories);
        actvCategory.setAdapter(categoryAdapter);
        actvCategory.setText(""); // Clear previous selection
    }

    private void setupDatePicker() {
        etDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        selectedDate.set(year, month, dayOfMonth);
                        updateDateDisplay();
                    },
                    selectedDate.get(Calendar.YEAR),
                    selectedDate.get(Calendar.MONTH),
                    selectedDate.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });
        
        // Set today's date as default
        updateDateDisplay();
    }

    private void updateDateDisplay() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        etDate.setText(sdf.format(selectedDate.getTime()));
    }

    private void setupValidation() {
        // TODO: Implement real-time validation
        // Enable/disable save button based on form completion
    }

    private void setupClickListeners() {
        btnSave.setOnClickListener(v -> saveTransaction());
    }

    private void saveTransaction() {
        // Reset errors
        tilAmount.setError(null);
        tilType.setError(null);
        tilCategory.setError(null);
        tilDescription.setError(null);

        String amountStr = etAmount.getText().toString().trim();
        String type = actvType.getText().toString().trim();
        String category = actvCategory.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        boolean isValid = true;

        if (TextUtils.isEmpty(amountStr)) {
            tilAmount.setError("Jumlah tidak boleh kosong");
            isValid = false;
        }

        if (TextUtils.isEmpty(type)) {
            tilType.setError("Jenis transaksi harus dipilih");
            isValid = false;
        }

        if (TextUtils.isEmpty(category)) {
            tilCategory.setError("Kategori harus dipilih");
            isValid = false;
        }

        if (isValid) {
            try {
                double amount = Double.parseDouble(amountStr);
                
                // Disable save button to prevent multiple saves
                btnSave.setEnabled(false);
                btnSave.setText("Menyimpan...");
                
                // Create transaction object
                Transaction transaction = new Transaction(amount, type.toLowerCase(), category, description, selectedDate.getTimeInMillis());
                transaction.setUserId(sessionManager.getUserId());
                
                // Save to database
                transactionRepository.insertTransaction(transaction, new TransactionRepository.TransactionCallback() {
                    @Override
                    public void onSuccess(long transactionId) {
                        runOnUiThread(() -> {
                            Toast.makeText(AddTransactionActivity.this, "Transaksi berhasil disimpan", Toast.LENGTH_SHORT).show();
                            finish();
                        });
                    }

                    @Override
                    public void onError(String error) {
                        runOnUiThread(() -> {
                            // Re-enable save button
                            btnSave.setEnabled(true);
                            btnSave.setText("Simpan");
                            
                            Toast.makeText(AddTransactionActivity.this, error, Toast.LENGTH_LONG).show();
                        });
                    }
                });
                
            } catch (NumberFormatException e) {
                tilAmount.setError("Format jumlah tidak valid");
            }
        }
    }
}