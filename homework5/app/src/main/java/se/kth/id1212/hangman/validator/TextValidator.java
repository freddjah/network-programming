package se.kth.id1212.hangman.validator;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class TextValidator implements TextWatcher {
    private EditText inputField;

    public TextValidator(EditText inputField) {
        this.inputField = inputField;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = this.inputField.getText().toString();

        String message = null;

        if (text.length() > 0 && !stringContainsOnlyLetters(text)) {
            message = "Can only contain a-z";
        }

        this.inputField.setError(message);
    }

    private static boolean stringContainsOnlyLetters(String input) {
        return input.matches("[a-zA-Z]+");
    }
}
