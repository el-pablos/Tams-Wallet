package com.tamswallet.app.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tamswallet.app.R;
import com.tamswallet.app.data.repository.UserRepository;
import com.tamswallet.app.ui.dashboard.MainActivity;
import com.tamswallet.app.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {
    
    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin;
    private Switch switchTwoFactor;
    private MaterialButton btnRegister;
    
    private UserRepository userRepository;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userRepository = UserRepository.getInstance(this);
        sessionManager = new SessionManager(this);
        
        // Check if user is already logged in
        if (sessionManager.isLoggedIn()) {
            navigateToMainActivity();
            return;
        }

        initViews();
        setupClickListeners();
        
        // Pre-fill email if provided from registration
        String prefilledEmail = getIntent().getStringExtra("email");
        if (prefilledEmail != null && !prefilledEmail.isEmpty()) {
            etEmail.setText(prefilledEmail);
        }
    }

    private void initViews() {
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        switchTwoFactor = findViewById(R.id.switchTwoFactor);
        btnRegister = findViewById(R.id.btnRegister);
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> attemptLogin());
        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    private void attemptLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Reset errors
        tilEmail.setError(null);
        tilPassword.setError(null);

        boolean isValid = true;

        if (TextUtils.isEmpty(email)) {
            tilEmail.setError("Email tidak boleh kosong");
            isValid = false;
        }

        if (TextUtils.isEmpty(password)) {
            tilPassword.setError("Password tidak boleh kosong");
            isValid = false;
        }

        if (isValid) {
            // Disable button to prevent multiple clicks
            btnLogin.setEnabled(false);
            btnLogin.setText("Memproses...");
            
            // Authenticate user
            userRepository.authenticateUser(email, password, new UserRepository.AuthCallback() {
                @Override
                public void onSuccess(com.tamswallet.app.data.model.User user) {
                    runOnUiThread(() -> {
                        // Save user session
                        sessionManager.createLoginSession(user);
                        
                        // Show success message
                        Toast.makeText(LoginActivity.this, "Login berhasil! Selamat datang, " + user.getName(), Toast.LENGTH_SHORT).show();
                        
                        // Navigate to main activity
                        navigateToMainActivity();
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        // Re-enable button
                        btnLogin.setEnabled(true);
                        btnLogin.setText("Login");
                        
                        // Show error message
                        Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
                    });
                }
            });
        }
    }
    
    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // TODO: Implement biometric authentication
    private void setupBiometricAuth() {
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login dengan Biometric")
                .setSubtitle("Gunakan sidik jari atau face unlock")
                .setNegativeButtonText("Batal")
                .build();
    }
}