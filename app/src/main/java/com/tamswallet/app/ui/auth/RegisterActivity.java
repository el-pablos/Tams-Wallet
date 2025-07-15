package com.tamswallet.app.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tamswallet.app.R;
import com.tamswallet.app.data.repository.UserRepository;
import com.tamswallet.app.ui.dashboard.MainActivity;
import com.tamswallet.app.utils.SecurityUtils;
import com.tamswallet.app.utils.SessionManager;

public class RegisterActivity extends AppCompatActivity {
    
    private TextInputLayout tilName, tilEmail, tilPassword, tilConfirmPassword;
    private TextInputEditText etName, etEmail, etPassword, etConfirmPassword;
    private MaterialButton btnRegister, btnLogin;
    
    private UserRepository userRepository;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userRepository = UserRepository.getInstance(this);
        sessionManager = new SessionManager(this);
        
        initViews();
        setupClickListeners();
    }

    private void initViews() {
        tilName = findViewById(R.id.tilName);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void setupClickListeners() {
        btnRegister.setOnClickListener(v -> attemptRegister());
        btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });
    }

    private void attemptRegister() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Reset errors
        tilName.setError(null);
        tilEmail.setError(null);
        tilPassword.setError(null);
        tilConfirmPassword.setError(null);

        boolean isValid = true;

        if (TextUtils.isEmpty(name)) {
            tilName.setError("Nama tidak boleh kosong");
            isValid = false;
        }

        if (TextUtils.isEmpty(email)) {
            tilEmail.setError("Email tidak boleh kosong");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Format email tidak valid");
            isValid = false;
        }

        if (TextUtils.isEmpty(password)) {
            tilPassword.setError("Password tidak boleh kosong");
            isValid = false;
        } else if (password.length() < 6) {
            tilPassword.setError("Password minimal 6 karakter");
            isValid = false;
        } else if (!SecurityUtils.isPasswordStrong(password)) {
            tilPassword.setError(SecurityUtils.getPasswordStrengthDescription(password));
            isValid = false;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            tilConfirmPassword.setError("Konfirmasi password tidak boleh kosong");
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            tilConfirmPassword.setError("Password tidak sama");
            isValid = false;
        }

        if (isValid) {
            // Disable button to prevent multiple clicks
            btnRegister.setEnabled(false);
            btnRegister.setText("Memproses...");
            
            // Register user
            userRepository.registerUser(name, email, password, new UserRepository.RegisterCallback() {
                @Override
                public void onSuccess(long userId) {
                    runOnUiThread(() -> {
                        // Show success message
                        Toast.makeText(RegisterActivity.this, "Registrasi berhasil! Silahkan login.", Toast.LENGTH_SHORT).show();
                        
                        // Navigate to login activity
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.putExtra("email", email); // Pre-fill email in login
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        // Re-enable button
                        btnRegister.setEnabled(true);
                        btnRegister.setText("Daftar");
                        
                        // Show error message
                        Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_LONG).show();
                        
                        // Clear password fields on error
                        etPassword.setText("");
                        etConfirmPassword.setText("");
                    });
                }
            });
        }
    }
}