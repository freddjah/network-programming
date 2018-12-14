package se.kth.id1212.hangman;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import se.kth.id1212.hangman.controller.GameController;
import se.kth.id1212.hangman.model.GameViewModel;
import se.kth.id1212.hangman.net.ServerHandler;
import se.kth.id1212.hangman.validator.TextValidator;
import se.kth.id1212.hangman.view.SnackbarFactory;

public class MainActivity extends AppCompatActivity {
    private GameController gameController;
    private TextView score;
    private TextView attemptsLeft;
    private TextView letters;
    private EditText inputField;

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setInfoMessage(intent.getExtras().getString("message"));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProgressDialog progress = new ProgressDialog(this);

        progress.setTitle("Loading...");
        progress.setMessage("Connecting to server");

        progress.setCancelable(false);
        progress.show();

        new Thread(() -> {
            try {
                ServerHandler connection = connectToServer();
                this.gameController = new GameController(connection);

                System.out.println("progress dismissed");

                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                this.initViewComponents();
                this.startGame();

            } catch (Exception e) {
                e.printStackTrace();

                setErrorMessage("Unable to connect to server. Please restart the application.");
            } finally {
                progress.dismiss();
            }
        }).start();
    }

    @Override
    protected void onStart() {
        super.onStart();

        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("user_notification"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
    }

    private void setErrorMessage(String message) {
        ConstraintLayout mainContainer = findViewById(R.id.mainContainer);

        Snackbar snackbar = SnackbarFactory.createErrorSnackbar(mainContainer, message);
        snackbar.show();
    }

    private void setInfoMessage(String message) {
        ConstraintLayout mainContainer = findViewById(R.id.mainContainer);

        Snackbar snackbar = SnackbarFactory.createInfoSnackbar(mainContainer, message);
        snackbar.show();
    }

    private void initButton() {
        Button guessButton = findViewById(R.id.guessButton);
        guessButton.setOnClickListener(view -> {
            EditText inputField = findViewById(R.id.inputField);

            new Thread(() -> {
                try {
                    GameViewModel model = this.gameController.guess(inputField.getText().toString());
                    this.updateGameFields(model);

                    if (!model.getLetters().contains("_")) {
                        this.setInfoMessage("Woohoo! Correct!");
                    } else if (model.getAttemptsLeft() == 0) {
                        this.setInfoMessage("Booo! Out of guesses.");
                    } else {
                        return;
                    }

                    this.inputField.setText("");
                    this.startGame();
                } catch (Exception e) {
                    this.setErrorMessage("Something went wrong.");
                }
            }).start();

        });
    }

    private void updateGameFields(GameViewModel model) {
        runOnUiThread(() -> {
            this.score.setText("Score: " + model.getScore());
            this.letters.setText(model.getLetters());
            this.attemptsLeft.setText("Attempts left: " + model.getAttemptsLeft());
        });
    }

    private ServerHandler connectToServer() throws IOException {
        return new ServerHandler("130.229.188.13", 3000);
    }

    private void initViewComponents() {
        this.inputField = findViewById(R.id.inputField);
        this.inputField.addTextChangedListener(new TextValidator(inputField));

        this.score = findViewById(R.id.score);
        this.letters = findViewById(R.id.currentWord);
        this.attemptsLeft = findViewById(R.id.attemptsLeft);

        this.initButton();
    }

    private void startGame() throws Exception {
        GameViewModel model = this.gameController.startGame();
        this.updateGameFields(model);
    }

}
