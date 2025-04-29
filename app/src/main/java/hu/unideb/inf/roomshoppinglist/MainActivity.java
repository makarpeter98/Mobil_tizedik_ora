package hu.unideb.inf.roomshoppinglist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

        shoppingListDatabase = Room.databaseBuilder(this,
                        ShoppingListDatabase.class, "shoppinglist_db")
                .fallbackToDestructiveMigration()
                .build();

        shoppingListDatabase.shoppingListDAO().getAllItems()
                .observe(this,
                        shoppingListItems -> resultTextView.setText(shoppingListItems.toString())
                );

        //refreshUI();

    }

    public void addItem(View view) {
        ShoppingListItem sli = new ShoppingListItem();
        sli.setName(newItemEditText.getText().toString());
        new Thread(
                () -> {
                    shoppingListDatabase.shoppingListDAO().insertItem(sli);
                    //refreshUI();
                }
        ).start();
    }

    public void clearDB(View view){
        new Thread(
                () -> shoppingListDatabase.shoppingListDAO().clearDB()
        ).start();
    }
}