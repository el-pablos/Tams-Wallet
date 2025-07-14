package com.tamswallet.app.ui.settings;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tamswallet.app.R;

public class ProfileActivity extends AppCompatActivity {
    
    private ImageView ivProfileImage;
    private TextView tvEmail;
    private TextInputLayout tilName;
    private TextInputEditText etName;
    private MaterialButton btnUpdateProfile, btnChangePassword, btnUploadImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();
        setupClickListeners();
        loadUserData();
    }

    private void initViews() {
        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvEmail = findViewById(R.id.tvEmail);
        tilName = findViewById(R.id.tilName);
        etName = findViewById(R.id.etName);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnUploadImage = findViewById(R.id.btnUploadImage);
    }

    private void setupClickListeners() {
        btnUpdateProfile.setOnClickListener(v -> updateProfile());
        
        btnChangePassword.setOnClickListener(v -> {
            // TODO: Navigate to change password activity
        });
        
        btnUploadImage.setOnClickListener(v -> {
            // TODO: Implement image picker functionality
            // Use intent to pick image from gallery
        });
    }

    private void loadUserData() {
        // TODO: Load user data from Room database
        // Display current user information
        tvEmail.setText("user@example.com"); // Placeholder
        etName.setText("User Name"); // Placeholder
    }

    private void updateProfile() {
        String name = etName.getText().toString().trim();
        
        if (name.isEmpty()) {
            tilName.setError("Nama tidak boleh kosong");
            return;
        }
        
        // TODO: Update user profile in Room database
        // Show success message and finish activity
        finish();
    }
}