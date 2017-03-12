package it.matteosilvestro.perudocounter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    public void displayDiceValue(int number) {
        String text = "";
        if (aceMode) {
            text = "ace";
            if (diceNumber > 1) {
                text = text + "s";
            }
        } else {
            text = String.valueOf(number);
        }
        TextView scoreView = (TextView) findViewById(R.id.diceValueText);
        scoreView.setText(text);
    }

    // Increment the value of dice for the bet and display it.
    public void incrementDiceValue(View view) {
        if (diceValue < 6 & diceValue > 1) {
            diceValue++;
        }
        displayDiceValue(diceValue);
    }

    /**
     * Display the number of dice for the bet.
     * Recall that it is not displayed the actual number but the number in words (e.g. "one" for 1)
     */
    public void displayDiceNumber(int number) {
        String numberInLetters = "";
        switch (number) {
            case 1:
                numberInLetters = "one";
                break;
            case 2:
                numberInLetters = "two";
                break;
            case 3:
                numberInLetters = "three";
                break;
            case 4:
                numberInLetters = "four";
                break;
            case 5:
                numberInLetters = "five";
                break;
            case 6:
                numberInLetters = "six";
                break;
            case 7:
                numberInLetters = "seven";
                break;
            case 8:
                numberInLetters = "eight";
                break;
            case 9:
                numberInLetters = "nine";
                break;
            case 10:
                numberInLetters = "ten";
                break;
            default:
                numberInLetters = "nan";
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
        }
        diceValue = 2; // reset also the value of dice, according to bet rules.
        displayDiceNumber(diceNumber);
        displayDiceValue(diceValue);
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
            displayDiceNumber(diceNumber);
            displayDiceValue(diceValue);
        } else {
            // the new number of dice must not exceed the total number of dice in game
            if (diceNumber * 2 + 1 <= totalDice) {
                // ace mode: we want to switch to normal mode
                aceMode = false;
                // to bet, we must use as number of dice twice the actual value plus one
                diceNumber = diceNumber * 2 + 1;
                // set the value of dice to a default 2, can be any number from 2 to 6
                diceValue = 2;
                displayDiceNumber(diceNumber);
                displayDiceValue(diceValue);
            } else {
                Toast.makeText(getApplicationContext(), "Cannot switch back to normal mode, the bet is too high!", Toast.LENGTH_SHORT).show();
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
        displayDiceNumber(diceNumber);
        displayDiceValue(diceValue);
    }

    // Display the number of dice for player A.
    public void displayDiceForPlayerA(int number) {
        String text = number + " ";
        if (number == 1) {
            text = text + "die";
        } else {
            text = text + "dice";
        }
        TextView diceText = (TextView) findViewById(R.id.diceForPlayerAText);
        diceText.setText(text);
    }

    // Display the number of victories for player A.
    public void displayVictoriesForPlayerA(int number) {
        String text = number + " ";
        if (number == 1) {
            text = text + "victory";
        } else {
            text = text + "victories";
        }
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
                displayVictoriesForPlayerB(victoriesForPlayerB);
                Toast.makeText(getApplicationContext(), "Player B wins!", Toast.LENGTH_SHORT).show();
            }
            displayDiceForPlayerA(diceForPlayerA);
        }
    }

    // Display the number of dice for player B.
    public void displayDiceForPlayerB(int number) {
        String text = number + " ";
        if (number == 1) {
            text = text + "die";
        } else {
            text = text + "dice";
        }
        TextView diceText = (TextView) findViewById(R.id.diceForPlayerBText);
        diceText.setText(text);
    }

    // Display the number of victories for player B.
    public void displayVictoriesForPlayerB(int number) {
        String text = number + " ";
        if (number == 1) {
            text = text + "victory";
        } else {
            text = text + "victories";
        }
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
                displayVictoriesForPlayerA(victoriesForPlayerA);
                Toast.makeText(getApplicationContext(), "Player A wins!", Toast.LENGTH_SHORT).show();
            }
        }
        displayDiceForPlayerB(diceForPlayerB);
    }

    // wrapper for the button start new match
    public void startNew(View view) {
        startNew();
    }

    public void startNew() {
        diceForPlayerA = 5;
        diceForPlayerB = 5;
        totalDice = 10;
        displayDiceForPlayerA(diceForPlayerA);
        displayDiceForPlayerB(diceForPlayerB);
        resetBet();
    }

    public void resetAll(View view) {
        resetAll();
    }

    public void resetAll() {
        victoriesForPlayerA = 0;
        victoriesForPlayerB = 0;
        displayVictoriesForPlayerA(victoriesForPlayerA);
        displayVictoriesForPlayerB(victoriesForPlayerB);
        startNew();
    }

}
