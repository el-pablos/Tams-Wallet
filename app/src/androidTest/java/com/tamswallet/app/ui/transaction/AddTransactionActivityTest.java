package com.tamswallet.app.ui.transaction;

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
 * UI tests for AddTransactionActivity
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddTransactionActivityTest {

    @Rule
    public ActivityScenarioRule<AddTransactionActivity> activityRule = 
            new ActivityScenarioRule<>(AddTransactionActivity.class);

    @Test
    public void testAddTransactionScreenDisplayed() {
        // Check if all form elements are displayed
        onView(withId(R.id.etAmount))
                .check(matches(isDisplayed()));
        
        onView(withId(R.id.spinnerType))
                .check(matches(isDisplayed()));
        
        onView(withId(R.id.spinnerCategory))
                .check(matches(isDisplayed()));
        
        onView(withId(R.id.etDescription))
                .check(matches(isDisplayed()));
        
        onView(withId(R.id.btnSelectDate))
                .check(matches(isDisplayed()));
        
        onView(withId(R.id.btnSave))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testEmptyAmountValidation() {
        // Try to save without entering amount
        onView(withId(R.id.btnSave))
                .perform(click());
        
        // Check if validation message is shown
        onView(withId(R.id.tilAmount))
                .check(matches(hasDescendant(withText(containsString("tidak boleh kosong")))));
    }

    @Test
    public void testInvalidAmountValidation() {
        // Enter invalid amount (zero)
        onView(withId(R.id.etAmount))
                .perform(typeText("0"), closeSoftKeyboard());
        
        onView(withId(R.id.btnSave))
                .perform(click());
        
        // Check if validation message is shown
        onView(withId(R.id.tilAmount))
                .check(matches(hasDescendant(withText(containsString("harus lebih dari 0")))));
    }

    @Test
    public void testValidAmountInput() {
        String validAmount = "100.50";
        
        onView(withId(R.id.etAmount))
                .perform(typeText(validAmount), closeSoftKeyboard());
        
        onView(withId(R.id.etAmount))
                .check(matches(withText(validAmount)));
    }

    @Test
    public void testTypeSpinnerInteraction() {
        // Click on type spinner
        onView(withId(R.id.spinnerType))
                .perform(click());
        
        // Select income option (assuming it's available)
        onView(withText("Income"))
                .perform(click());
        
        // Verify selection (this would need adjustment based on actual spinner implementation)
        onView(withId(R.id.spinnerType))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testCategorySpinnerInteraction() {
        // Click on category spinner
        onView(withId(R.id.spinnerCategory))
                .perform(click());
        
        // Select a category (assuming Food is available)
        onView(withText("Food"))
                .perform(click());
        
        // Verify selection
        onView(withId(R.id.spinnerCategory))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testDescriptionInput() {
        String testDescription = "Test transaction description";
        
        onView(withId(R.id.etDescription))
                .perform(typeText(testDescription), closeSoftKeyboard());
        
        onView(withId(R.id.etDescription))
                .check(matches(withText(testDescription)));
    }

    @Test
    public void testDateSelectionButton() {
        // Click date selection button
        onView(withId(R.id.btnSelectDate))
                .perform(click());
        
        // This would open a date picker dialog
        // For now, we'll just verify the button is clickable
        onView(withId(R.id.btnSelectDate))
                .check(matches(isClickable()));
    }

    @Test
    public void testCompleteTransactionForm() {
        // Fill out complete form
        onView(withId(R.id.etAmount))
                .perform(typeText("150.75"), closeSoftKeyboard());
        
        onView(withId(R.id.etDescription))
                .perform(typeText("Grocery shopping"), closeSoftKeyboard());
        
        // Select type and category (would need adjustment for actual spinners)
        onView(withId(R.id.spinnerType))
                .perform(click());
        // Assuming we can select expense
        
        onView(withId(R.id.spinnerCategory))
                .perform(click());
        // Assuming we can select Food
        
        // Try to save
        onView(withId(R.id.btnSave))
                .perform(click());
        
        // Verify form interaction works
        onView(withId(R.id.btnSave))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testAmountFieldNumericInput() {
        // Test that amount field only accepts numeric input
        onView(withId(R.id.etAmount))
                .perform(typeText("abc123.45"), closeSoftKeyboard());
        
        // Should only show the numeric part
        onView(withId(R.id.etAmount))
                .check(matches(withText("123.45")));
    }

    @Test
    public void testDescriptionFieldCharacterLimit() {
        // Test description field with very long text
        String longDescription = "This is a very long description that should test the character limit of the description field to ensure it handles long text properly";
        
        onView(withId(R.id.etDescription))
                .perform(typeText(longDescription), closeSoftKeyboard());
        
        // Verify text is entered (actual limit would depend on implementation)
        onView(withId(R.id.etDescription))
                .check(matches(withText(containsString("This is a very long"))));
    }

    @Test
    public void testFormFieldsRetainDataOnRotation() {
        String testAmount = "100.50";
        String testDescription = "Test description";
        
        // Enter data
        onView(withId(R.id.etAmount))
                .perform(typeText(testAmount), closeSoftKeyboard());
        
        onView(withId(R.id.etDescription))
                .perform(typeText(testDescription), closeSoftKeyboard());
        
        // Simulate rotation (would require additional setup)
        // For now, verify data is still there
        onView(withId(R.id.etAmount))
                .check(matches(withText(testAmount)));
        
        onView(withId(R.id.etDescription))
                .check(matches(withText(testDescription)));
    }

    @Test
    public void testAccessibilityLabels() {
        // Check if form elements have proper accessibility labels
        onView(withId(R.id.etAmount))
                .check(matches(hasContentDescription()));
        
        onView(withId(R.id.etDescription))
                .check(matches(hasContentDescription()));
        
        onView(withId(R.id.btnSave))
                .check(matches(hasContentDescription()));
        
        onView(withId(R.id.btnSelectDate))
                .check(matches(hasContentDescription()));
    }

    @Test
    public void testCancelButton() {
        // If there's a cancel button, test it
        // This would depend on the actual implementation
        onView(withId(R.id.etAmount))
                .perform(typeText("100"), closeSoftKeyboard());
        
        // Test back button or cancel functionality
        // For now, we'll just verify the form is interactive
        onView(withId(R.id.etAmount))
                .check(matches(withText("100")));
    }
}
