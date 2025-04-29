package hu.unideb.inf.roomshoppinglist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ShoppingListDatabase shoppingListDatabase = null;
    //Teszt!
    EditText newItemEditText;
    TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newItemEditText = findViewById(R.id.newItemEditText);
        resultTextView = findViewById(R.id.resultTextView);

        Button addButton = findViewById(R.id.addButton);
        Button clearButton = findViewById(R.id.button);

        shoppingListDatabase = Room.databaseBuilder(this,
                        ShoppingListDatabase.class, "shoppinglist_db")
                .fallbackToDestructiveMigration()
                .build();

        shoppingListDatabase.shoppingListDAO().getAllItems()
                .observe(this,
                        shoppingListItems -> resultTextView.setText(shoppingListItems.toString())
                );
        addButton.setOnClickListener( view -> {
            ShoppingListItem sli = new ShoppingListItem();
            sli.setName(newItemEditText.getText().toString());
            new Thread(
                    () -> {
                        shoppingListDatabase.shoppingListDAO().insertItem(sli);
                    }
            ).start();
            newItemEditText.setText("");
        });

      clearButton.setOnClickListener(view ->{
          new Thread(
                  () -> shoppingListDatabase.shoppingListDAO().clearDB()
          ).start();
      });

    }
}