package it.matteosilvestro.perudocounter;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.duration;
import static android.R.attr.id;
import static android.R.attr.value;

public class MainActivity extends AppCompatActivity {

    int diceNumber = 1;             // number of dice in the bet
    int diceValue = 2;              // value of dice in the bet
    boolean aceMode = false;        // false = number mode (default), true = ace mode
    int totalDice = 10;             // total number of dice in game
    int diceForPlayerA = 5;         // total number of dice for player A
    int diceForPlayerB = 5;         // total number of dice for player B
    int victoriesForPlayerA = 0;    // number of victories for player A
    int victoriesForPlayerB = 0;    // number of victories for player B

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resetAll();
    }

    // --------------------------------------------------------------
    // Change the actual bet values (number of dice + value of dice).
    // --------------------------------------------------------------

    // Display the value of dice for the bet.
    public void displayDiceValue() {
        String text = "";
        if (aceMode) { // the bet is on aces
            // Get the right word, singular or plural of "ace"
            Resources res = getResources();
            text = res.getQuantityString(R.plurals.ace, diceNumber);
        } else {
            text = String.valueOf(diceValue);
        }
        TextView scoreView = (TextView) findViewById(R.id.diceValueText);
        scoreView.setText(text);
    }

    // Increment the value of dice for the bet and display it.
    public void incrementDiceValue(View view) {
        if (!aceMode) {
            if (diceValue < 6 & diceValue > 1) {
                diceValue++;
            }
            displayDiceValue();
        }
    }

    /**
     * Display the number of dice for the bet.
     * Recall that it is not displayed the actual number but the number in words (e.g. "one" for 1)
     */
    public void displayDiceNumber() {
        String numberInLetters = "";
        switch (diceNumber) {
            case 1:
                numberInLetters = getString(R.string.one);
                break;
            case 2:
                numberInLetters = getString(R.string.two);
                break;
            case 3:
                numberInLetters = getString(R.string.three);
                break;
            case 4:
                numberInLetters = getString(R.string.four);
                break;
            case 5:
                numberInLetters = getString(R.string.five);
                break;
            case 6:
                numberInLetters = getString(R.string.six);
                break;
            case 7:
                numberInLetters = getString(R.string.seven);
                break;
            case 8:
                numberInLetters = getString(R.string.eight);
                break;
            case 9:
                numberInLetters = getString(R.string.nine);
                break;
            case 10:
                numberInLetters = getString(R.string.ten);
                break;
            default:
                numberInLetters = getString(R.string.nan);
                break;
        }
        TextView scoreView = (TextView) findViewById(R.id.diceNumberText);
        scoreView.setText(numberInLetters);
    }

    // Increment the number of dice and display it.
    public void incrementDiceNumber(View view) {
        // the number of dice you can bet on must not be greater than the total number of dice in game
        if (diceNumber < totalDice) {
            diceNumber++;
            if (!aceMode) {
                diceValue = 2; // reset also the value of dice, according to bet rules.
                displayDiceNumber();
                displayDiceValue();
            } else {
                displayDiceNumber();
                displayDiceValue();
            }
        }
    }

    // switch from number mode to ace mode.
    public void switchBetMode(View view) {
        if (!aceMode) {
            // normal mode: we want to switch to ace mode
            aceMode = true;
            // the number of dice must be halved (integer part plus one if odd)
            if (diceNumber % 2 == 0) {
                diceNumber = diceNumber / 2;
            } else {
                diceNumber = diceNumber / 2 + 1;
            }
            // for us, the value of dice 1 is equivalent to an ace
            diceValue = 1;
            displayDiceNumber();
            displayDiceValue();
        } else {
            // the new number of dice must not exceed the total number of dice in game
            if (diceNumber * 2 + 1 <= totalDice) {
                // ace mode: we want to switch to normal mode
                aceMode = false;
                // to bet, we must use as number of dice twice the actual value plus one
                diceNumber = diceNumber * 2 + 1;
                // set the value of dice to a default 2, can be any number from 2 to 6
                diceValue = 2;
                displayDiceNumber();
                displayDiceValue();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.cant_switch), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Wrapper for the action of the button to reset bet.
    public void resetBetButton(View view) {
        resetBet();
    }

    // Reset the bet to starting values.
    public void resetBet() {
        aceMode = false;
        diceNumber = 1;
        diceValue = 2;
        displayDiceNumber();
        displayDiceValue();
    }

    // Display the number of dice for player A.
    public void displayDiceForPlayerA() {
        Resources res = getResources();
        String text = res.getQuantityString(R.plurals.die, diceForPlayerA, diceForPlayerA);
        TextView diceText = (TextView) findViewById(R.id.diceForPlayerAText);
        diceText.setText(text);
    }

    // Display the number of victories for player A.
    public void displayVictoriesForPlayerA() {
        Resources res = getResources();
        String text = res.getQuantityString(R.plurals.victory, victoriesForPlayerA, victoriesForPlayerA);
        TextView victoriesText = (TextView) findViewById(R.id.victoriesForPlayerAText);
        victoriesText.setText(text);
    }

    // Decrement the number of dice for player A.
    public void decrementDiceForPlayerA(View view) {
        // if one of the players has no dice, a match is ended (you cannot update the number of dice until a new match starts).
        if (diceForPlayerA > 0 && diceForPlayerB > 0) {
            diceForPlayerA--;
            totalDice--;
            resetBet();
            if (diceForPlayerA == 0) {
                victoriesForPlayerB++;
                displayVictoriesForPlayerB();
                Toast.makeText(getApplicationContext(), getString(R.string.wins, "B"), Toast.LENGTH_SHORT).show();
            }
            displayDiceForPlayerA();
        }
    }

    // Display the number of dice for player B.
    public void displayDiceForPlayerB() {
        Resources res = getResources();
        String text = res.getQuantityString(R.plurals.die, diceForPlayerB, diceForPlayerB);
        TextView diceText = (TextView) findViewById(R.id.diceForPlayerBText);
        diceText.setText(text);
    }

    // Display the number of victories for player B.
    public void displayVictoriesForPlayerB() {
        Resources res = getResources();
        String text = res.getQuantityString(R.plurals.victory, victoriesForPlayerB, victoriesForPlayerB);
        TextView victoriesText = (TextView) findViewById(R.id.victoriesForPlayerBText);
        victoriesText.setText(text);
    }

    // Decrement the number of dice for player B.
    public void decrementDiceForPlayerB(View view) {
        // if one of the players has no dice, a match is ended (you cannot update the number of dice until a new match starts).
        if (diceForPlayerA > 0 && diceForPlayerB > 0) {
            diceForPlayerB--;
            totalDice--;
            resetBet();
            if (diceForPlayerB == 0) {
                victoriesForPlayerA++;
                displayVictoriesForPlayerA();
                Toast.makeText(getApplicationContext(), getString(R.string.wins, "A"), Toast.LENGTH_SHORT).show();
            }
        }
        displayDiceForPlayerB();
    }

    // wrapper for the button start new match
    public void startNew(View view) {
        startNew();
    }

    public void startNew() {
        diceForPlayerA = 5;
        diceForPlayerB = 5;
        totalDice = 10;
        displayDiceForPlayerA();
        displayDiceForPlayerB();
        resetBet();
    }

    public void resetAll(View view) {
        resetAll();
    }

    public void resetAll() {
        victoriesForPlayerA = 0;
        victoriesForPlayerB = 0;
        displayVictoriesForPlayerA();
        displayVictoriesForPlayerB();
        startNew();
    }

}
