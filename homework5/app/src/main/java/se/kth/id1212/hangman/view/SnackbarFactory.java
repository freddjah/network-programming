package se.kth.id1212.hangman.view;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

public class SnackbarFactory {

    private static final int ERROR_BACKGROUND_COLOR = Color.rgb(204,57,57);
    private static final int ERROR_TEXT_COLOR = Color.WHITE;
    private static final int ERROR_DURATION = 6000;

    private static final int INFO_BACKGROUND_COLOR = Color.rgb(96,130,193);
    private static final int INFO_TEXT_COLOR = Color.WHITE;
    private static final int INFO_DURATION = 4000;

    public static Snackbar createErrorSnackbar(View view, String message, int duration) {

        final Snackbar snackbar = Snackbar.make(view, message, duration);
        snackbar.setAction("DISMISS", snackbarView -> snackbar.dismiss());
        setColorProperties(snackbar, ERROR_BACKGROUND_COLOR, ERROR_TEXT_COLOR);

        return snackbar;
    }

    public static Snackbar createErrorSnackbar(View view, String message) {
        return createErrorSnackbar(view, message, ERROR_DURATION);
    }

    public static Snackbar createInfoSnackbar(View view, String message, int duration) {

        final Snackbar snackbar = Snackbar.make(view, message, duration);
        snackbar.setAction("DISMISS", snackbarView -> snackbar.dismiss());
        setColorProperties(snackbar, INFO_BACKGROUND_COLOR, INFO_TEXT_COLOR);

        return snackbar;
    }

    public static Snackbar createInfoSnackbar(View view, String message) {
        return createInfoSnackbar(view, message, INFO_DURATION);
    }

    private static void setColorProperties(Snackbar snackbar, int backgroundColor, int textColor) {

        snackbar.setActionTextColor(textColor);

        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(backgroundColor);

        TextView textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(textColor);
        textView.setTextSize(14);
    }

}
