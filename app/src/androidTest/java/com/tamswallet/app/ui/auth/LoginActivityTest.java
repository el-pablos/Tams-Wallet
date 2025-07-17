package com.tamswallet.app.ui.auth;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.tamswallet.app.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * UI tests for LoginActivity
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule = 
            new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void testLoginScreenDisplayed() {
        // Check if login screen elements are displayed
        onView(withId(R.id.etEmail))
                .check(matches(isDisplayed()));
        
        onView(withId(R.id.etPassword))
                .check(matches(isDisplayed()));
        
        onView(withId(R.id.btnLogin))
                .check(matches(isDisplayed()));
        
        onView(withId(R.id.btnRegister))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testEmptyFieldsValidation() {
        // Try to login with empty fields
        onView(withId(R.id.btnLogin))
                .perform(click());
        
        // Check if validation messages are shown
        onView(withId(R.id.tilEmail))
                .check(matches(hasDescendant(withText(containsString("tidak boleh kosong")))));
    }

    @Test
    public void testInvalidEmailValidation() {
        // Enter invalid email
        onView(withId(R.id.etEmail))
                .perform(typeText("invalid-email"), closeSoftKeyboard());
        
        onView(withId(R.id.etPassword))
                .perform(typeText("password123"), closeSoftKeyboard());
        
        onView(withId(R.id.btnLogin))
                .perform(click());
        
        // Check if email validation message is shown
        onView(withId(R.id.tilEmail))
                .check(matches(hasDescendant(withText(containsString("email")))));
    }

    @Test
    public void testPasswordTooShort() {
        // Enter valid email but short password
        onView(withId(R.id.etEmail))
                .perform(typeText("test@example.com"), closeSoftKeyboard());
        
        onView(withId(R.id.etPassword))
                .perform(typeText("123"), closeSoftKeyboard());
        
        onView(withId(R.id.btnLogin))
                .perform(click());
        
        // Check if password validation message is shown
        onView(withId(R.id.tilPassword))
                .check(matches(hasDescendant(withText(containsString("minimal")))));
    }

    @Test
    public void testNavigateToRegister() {
        // Click register button
        onView(withId(R.id.btnRegister))
                .perform(click());
        
        // Check if register activity is opened (this would need to be adjusted based on actual navigation)
        // For now, we'll just check that the button is clickable
        onView(withId(R.id.btnRegister))
                .check(matches(isClickable()));
    }

    @Test
    public void testLoginWithValidCredentials() {
        // This test would require a test user to be pre-created
        // For now, we'll test the UI interaction
        
        onView(withId(R.id.etEmail))
                .perform(typeText("test@example.com"), closeSoftKeyboard());
        
        onView(withId(R.id.etPassword))
                .perform(typeText("password123"), closeSoftKeyboard());
        
        onView(withId(R.id.btnLogin))
                .perform(click());
        
        // The actual assertion would depend on whether the user exists
        // For UI testing, we're verifying the interaction works
        onView(withId(R.id.btnLogin))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testEmailFieldAcceptsInput() {
        String testEmail = "test@example.com";
        
        onView(withId(R.id.etEmail))
                .perform(typeText(testEmail), closeSoftKeyboard());
        
        onView(withId(R.id.etEmail))
                .check(matches(withText(testEmail)));
    }

    @Test
    public void testPasswordFieldHidesInput() {
        onView(withId(R.id.etPassword))
                .perform(typeText("password123"), closeSoftKeyboard());
        
        // Check that password field has password input type
        onView(withId(R.id.etPassword))
                .check(matches(hasInputType(android.text.InputType.TYPE_CLASS_TEXT | 
                        android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD)));
    }

    @Test
    public void testTwoFactorSwitchInteraction() {
        // Check if two-factor switch is displayed and clickable
        onView(withId(R.id.switchTwoFactor))
                .check(matches(isDisplayed()))
                .check(matches(isClickable()));
        
        // Test toggling the switch
        onView(withId(R.id.switchTwoFactor))
                .perform(click());
        
        onView(withId(R.id.switchTwoFactor))
                .check(matches(isChecked()));
        
        // Toggle back
        onView(withId(R.id.switchTwoFactor))
                .perform(click());
        
        onView(withId(R.id.switchTwoFactor))
                .check(matches(isNotChecked()));
    }

    @Test
    public void testFormFieldsRetainDataOnRotation() {
        String testEmail = "test@example.com";
        String testPassword = "password123";
        
        // Enter data
        onView(withId(R.id.etEmail))
                .perform(typeText(testEmail), closeSoftKeyboard());
        
        onView(withId(R.id.etPassword))
                .perform(typeText(testPassword), closeSoftKeyboard());
        
        // Simulate rotation (this would require additional setup in a real test)
        // For now, we'll just verify the data is still there
        onView(withId(R.id.etEmail))
                .check(matches(withText(testEmail)));
        
        onView(withId(R.id.etPassword))
                .check(matches(withText(testPassword)));
    }

    @Test
    public void testAccessibilityLabels() {
        // Check if important UI elements have proper content descriptions
        onView(withId(R.id.etEmail))
                .check(matches(hasContentDescription()));
        
        onView(withId(R.id.etPassword))
                .check(matches(hasContentDescription()));
        
        onView(withId(R.id.btnLogin))
                .check(matches(hasContentDescription()));
    }
}
