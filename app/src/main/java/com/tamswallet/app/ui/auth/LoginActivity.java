package com.tamswallet.app.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Switch;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tamswallet.app.R;
import com.tamswallet.app.data.repository.UserRepository;
import com.tamswallet.app.ui.dashboard.MainActivity;
import com.tamswallet.app.utils.SessionManager;
import java.util.concurrent.Executor;

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

        // Check if biometric authentication is available and enabled
        if (sessionManager.hasStoredCredentials()) {
            setupBiometricAuth();
        }

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

    private void setupBiometricAuth() {
        // Check if biometric authentication is available
        BiometricManager biometricManager = BiometricManager.from(this);

        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                showBiometricPrompt();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(this, "Perangkat tidak mendukung biometric", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(this, "Biometric tidak tersedia saat ini", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(this, "Belum ada biometric yang terdaftar", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void showBiometricPrompt() {
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(LoginActivity.this, "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(LoginActivity.this, "Authentication succeeded!", Toast.LENGTH_SHORT).show();

                // Auto-login with biometric success
                if (sessionManager.hasStoredCredentials()) {
                    navigateToMainActivity();
                } else {
                    Toast.makeText(LoginActivity.this, "Silakan login dengan email dan password terlebih dahulu", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login dengan Biometric")
                .setSubtitle("Gunakan sidik jari atau face unlock untuk masuk")
                .setNegativeButtonText("Batal")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }
}