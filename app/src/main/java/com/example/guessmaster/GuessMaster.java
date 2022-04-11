package com.example.guessmaster;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.view.View;
import android.widget.*;
import android.os.Bundle;

import java.util.Random;

public class GuessMaster extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_activity);
        // Specify the button in the view
        guessButton = (Button) findViewById(R.id.btnguess);
        btnclearContent = (Button) findViewById(R.id.btnclear);
        // EditText for user input
        userIn = (EditText) findViewById(R.id.guessinput);
        // TextView for total tickets
        ticketsum = (TextView) findViewById(R.id.ticket);
        entityImage = (ImageView) findViewById(R.id.entityImage);
        entityName = (TextView) findViewById(R.id.entityname);

        Country usa = new Country("United States", new Date("July", 4, 1776), "Washington DC", 0.1);
        Person myCreator = new Person("myCreator", new Date("May", 6, 1800), "Male", 1);
        Politician jTrudeau = new Politician("Justin Trudeau", new Date("December", 25, 1971), "Male", "Liberal", 0.25);
        Singer cDion = new Singer("Celine Dion", new Date("March", 30, 1961), "Female", "La voix du bon Dieu", new Date("November", 6, 1981), 0.5);

        addEntity(jTrudeau);
        addEntity(cDion);
        addEntity(myCreator);
        addEntity(usa);
        entityId = genRandomEntityId();
        Entity currentEntity = entities[entityId];
        welcomeToGame(currentEntity);

        final GuessMaster gm = new GuessMaster();

        btnclearContent.setOnClickListener(new View.OnClickListener() { // changeEntity button onClick method
            @Override
            public void onClick(View v) {
                changeEntity();
            }
        });
        guessButton.setOnClickListener(new View.OnClickListener() { // Guess button onClick method
            @Override
            public void onClick(View v) {
                //playing game
                playGame();

            }
        });
    }

    // instance variables
    private TextView entityName;
    private TextView ticketsum;
    private Button guessButton;
    private EditText userIn;
    private Button btnclearContent;
    private String user_input;
    private ImageView entityImage;
    String answer;

    private int numOfEntities;
    private Entity[] entities;
    private int totalTicketNum = 0;
    int entityId = 0;


    public GuessMaster() { // guessmaster constructor
        numOfEntities = 0;
        entities = new Entity[10];
    }

    public void getEntity(Entity[] entities) {
        this.entities = entities;
    }

    public void addEntity(Entity entity) { // add entity method
//		entities[numOfEntities++] = new Entity(entity);
//		entities[numOfEntities++] = entity;//////
        entities[numOfEntities++] = entity.clone();
    }


    public void playGame(Entity entity) { // Play game function
        answer = userIn.getText().toString();
        answer = answer.replace("\n", "").replace("\r", "");

        try {
            Date date = new Date(answer);
            if (date.precedes(entity.getBorn())) { // If date is later than entity date
                wrongDialog("later ");
            } else if (!date.equals(entity.getBorn())) { // If date is earlier than entity date
                wrongDialog("earlier ");
            } else { // Correct answer
                totalTicketNum += entity.getAwardedTicketNumber();
                ticketsum.setText("Total Tickets: " + totalTicketNum);
                correctDialog(entity);
            }

        } catch (Exception e) { // If no valid date is entered
        }
    }

    public void playGame() { // Start game
        Entity currentEntity = entities[entityId];
        playGame(currentEntity);
    }

    public int genRandomEntityId() { // Generate a random entity
        Random randomNumber = new Random();
        return randomNumber.nextInt(numOfEntities);
    }

    public void changeEntity() { // Change entity method
        continueGame();
    }

    public void ImageSetter() { // Set Imager as well as entity name based on current entity
        Entity currentEntity = entities[entityId];

        if (currentEntity.getName().equals("Justin Trudeau")) {
            entityImage.setImageResource(R.drawable.justint);
            entityName.setText(currentEntity.getName());
        } else if (currentEntity.getName().equals("Celine Dion")) {
            entityImage.setImageResource(R.drawable.celidion);
            entityName.setText(currentEntity.getName());
        } else if (currentEntity.getName().equals("myCreator")) {
            entityImage.setImageResource(R.drawable.mycreator);
            entityName.setText(currentEntity.getName());
        } else if (currentEntity.getName().equals("United States")) {
            entityImage.setImageResource(R.drawable.usaflag);
            entityName.setText(currentEntity.getName());
        }
    }

    public void welcomeToGame(Entity entity) { // Welcome to game dialog box
        AlertDialog.Builder welcomealert =
                new AlertDialog.Builder(GuessMaster.this);
        welcomealert.setTitle("GuessMaster_Game_V3");
        welcomealert.setMessage(entity.welcomeMessage());
        welcomealert.setCancelable(false); // no cancel button

        welcomealert.setNegativeButton("START_GAME", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getBaseContext(), "Game_is_starting...Enjoy",
                        Toast.LENGTH_SHORT).show();
            }
        });
        // Show dialog
        AlertDialog dialog = welcomealert.create();
        dialog.show();
        ImageSetter();
    }

    public void wrongDialog(String hint) { // method used if guess is wrong
        AlertDialog.Builder wrongAlert =
                new AlertDialog.Builder(GuessMaster.this);
        wrongAlert.setTitle("Incorrect");
        wrongAlert.setMessage("Try a " + hint + "date!");
        wrongAlert.setCancelable(false);

        wrongAlert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = wrongAlert.create();
        dialog.show();
    }

    public void correctDialog(final Entity entity) { // Dialog box for correct answer
        AlertDialog.Builder correctAlert = new AlertDialog.Builder(GuessMaster.this);
        correctAlert.setTitle("CORRECT!");
        correctAlert.setMessage(entity.closingMessage());
        correctAlert.setCancelable(false);

        correctAlert.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getBaseContext(), "You won " + entity.getAwardedTicketNumber() + "tickets", Toast.LENGTH_SHORT).show();
                continueGame();
            }
        });
        AlertDialog dialog = correctAlert.create();
        dialog.show();
    }

    public void continueGame() { // Method to continue game
        userIn.getText().clear();
        entityId = genRandomEntityId();
        Entity currentEntity = entities[entityId];
        entityName.setText(currentEntity.getName());
        ImageSetter();
        // display welcome message
        playGame(currentEntity);
    }
}



