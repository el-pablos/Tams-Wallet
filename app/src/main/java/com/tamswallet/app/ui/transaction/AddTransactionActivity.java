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
import com.tamswallet.app.utils.ValidationUtils;
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

        // Validate amount using ValidationUtils
        String amountError = ValidationUtils.getAmountValidationError(amountStr);
        if (amountError != null) {
            tilAmount.setError(amountError);
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

        // Validate description
        if (!ValidationUtils.isValidDescription(description)) {
            tilDescription.setError("Deskripsi mengandung karakter tidak valid");
            isValid = false;
        }

        // Check if date is selected
        if (selectedDate == null) {
            Toast.makeText(this, "Tanggal harus dipilih", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (isValid) {
            try {
                double amount = Double.parseDouble(amountStr);

                // Additional validation for amount
                if (amount <= 0) {
                    tilAmount.setError("Jumlah harus lebih dari 0");
                    return;
                }

                if (amount > 999999999.99) {
                    tilAmount.setError("Jumlah terlalu besar");
                    return;
                }

                // Disable save button to prevent multiple saves
                btnSave.setEnabled(false);
                btnSave.setText("Menyimpan...");

                // Check if user is logged in
                long userId = sessionManager.getUserId();
                if (userId == -1) {
                    Toast.makeText(this, "Silakan login terlebih dahulu", Toast.LENGTH_SHORT).show();
                    btnSave.setEnabled(true);
                    btnSave.setText("Simpan");
                    return;
                }

                // Create transaction object
                Transaction transaction = new Transaction(amount, type.toLowerCase(), category, description, selectedDate.getTimeInMillis());
                transaction.setUserId(userId);

                // Log transaction details for debugging
                android.util.Log.d("AddTransaction", "Creating transaction: " +
                    "Amount=" + amount + ", Type=" + type + ", Category=" + category +
                    ", Date=" + selectedDate.getTimeInMillis() + ", UserId=" + sessionManager.getUserId());

                // Save to database
                transactionRepository.insertTransaction(transaction, new TransactionRepository.TransactionCallback() {
                    @Override
                    public void onSuccess(long transactionId) {
                        android.util.Log.d("AddTransaction", "Transaction saved successfully with ID: " + transactionId);
                        runOnUiThread(() -> {
                            Toast.makeText(AddTransactionActivity.this, "Transaksi berhasil disimpan", Toast.LENGTH_SHORT).show();
                            finish();
                        });
                    }

                    @Override
                    public void onError(String error) {
                        android.util.Log.e("AddTransaction", "Failed to save transaction: " + error);
                        runOnUiThread(() -> {
                            // Re-enable save button
                            btnSave.setEnabled(true);
                            btnSave.setText("Simpan");

                            Toast.makeText(AddTransactionActivity.this, "Gagal menyimpan: " + error, Toast.LENGTH_LONG).show();
                        });
                    }
                });

            } catch (NumberFormatException e) {
                android.util.Log.e("AddTransaction", "Number format exception: " + e.getMessage());
                tilAmount.setError("Format jumlah tidak valid");
                btnSave.setEnabled(true);
                btnSave.setText("Simpan");
            } catch (Exception e) {
                android.util.Log.e("AddTransaction", "Unexpected error: " + e.getMessage());
                Toast.makeText(this, "Terjadi kesalahan: " + e.getMessage(), Toast.LENGTH_LONG).show();
                btnSave.setEnabled(true);
                btnSave.setText("Simpan");
            }
        }
    }
}