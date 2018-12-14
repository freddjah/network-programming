package se.kth.id1212.hangman;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;

import se.kth.id1212.hangman.controller.GameController;
import se.kth.id1212.hangman.model.GameViewModel;
import se.kth.id1212.hangman.net.ServerHandler;
import se.kth.id1212.hangman.validator.TextValidator;

public class MainActivity extends AppCompatActivity {
    private GameController gameController;
    private TextView score;
    private TextView attemptsLeft;
    private TextView letters;
    private EditText inputField;

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

                progress.dismiss();

                System.out.println("progress dismissed");

                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                this.initViewComponents();
                this.startGame();

            } catch (Exception e) {
                e.printStackTrace();
                setErrorMessage("Unable to connect to server. Please restart the application.", 100000000);
            }
        }).start();
    }

    private void setErrorMessage(String message, int duration) {
        View rootView = this.getWindow().getDecorView().getRootView();
        Snackbar.make(rootView, message, duration).show();
    }

    private void setInfoMessage(String message, int duration) {
        View rootView = this.getWindow().getDecorView().getRootView();
        Snackbar.make(rootView, message, duration).show();
    }

    private void initButton() {
        Button guessButton = findViewById(R.id.guessButton);
        guessButton.setOnClickListener(view -> {
            EditText inputField = findViewById(R.id.inputField);

            new Thread(() -> {
                try {
                    GameViewModel model = this.gameController.guess(inputField.getText().toString());
                    this.updateGameFields(model);
                } catch (Exception e) {
                    this.setErrorMessage("Something went wrong.", 4000);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
