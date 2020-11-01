package eecs285.proj4.prabd_sanilgbudgettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class TransactionListActivity extends AppCompatActivity {

    private ArrayAdapter<Transaction> adapter;
    private ArrayList<Transaction> transactions;
    private static final String TRANSACTIONS_FILE = "transactions";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);
        setTitle("All Transactions");
        readTransactions();

        adapter = new TransactionAdapter(this,
                R.layout.list_item_transaction, transactions);

        ListView listView = findViewById(R.id.transactionListView);
        listView.setAdapter(adapter);
    }

    private void readTransactions() {
        File file = new File(getFilesDir(), TRANSACTIONS_FILE);
        try (ObjectInputStream input =
                     new ObjectInputStream(new FileInputStream(file))){
            transactions = (ArrayList<Transaction>) input.readObject();
        } catch (IOException | ClassNotFoundException exception) {
            transactions = new ArrayList<>();
        }
    }
}
