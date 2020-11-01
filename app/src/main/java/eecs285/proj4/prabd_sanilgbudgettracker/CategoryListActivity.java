package eecs285.proj4.prabd_sanilgbudgettracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class CategoryListActivity extends AppCompatActivity implements AddTransactionDialogFragment.AddTransactionDialogListener {

    private static final String CATEGORIES_FILE = "categories";
    private static final String TRANSACTIONS_FILE = "transactions";
    private ArrayList<Category> categories;
    private ArrayList<Transaction> transactions;
    private ArrayAdapter<Category> adapter;
    private Double totalSum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        readCategories();
        readTransactions();
        readTotal();

        adapter = new CategoryAdapter(this, R.layout.list_item_categories, categories);
        ListView listView = findViewById(R.id.categoryListView);
        listView.setAdapter(adapter);
    }

    /**
     * Reads category list from file.
     */
    private void readCategories() {
        File file = new File(getFilesDir(), CATEGORIES_FILE);
        try (ObjectInputStream input =
                    new ObjectInputStream(new FileInputStream(file))){
            categories = (ArrayList<Category>) input.readObject();
        } catch (IOException | ClassNotFoundException exception) {
            categories = new ArrayList<>();
        }
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
    /**
     * Writes category list to file.
     */
    private void writeCategories() {
        File file = new File(getFilesDir(), CATEGORIES_FILE);
        try (ObjectOutputStream output =
                new ObjectOutputStream(new FileOutputStream(file))) {
            output.writeObject(categories);
        } catch (IOException exception) {
            throw new IllegalStateException("something bad happened");
        }
    }

    private void writeTransactions() {
        File file = new File(getFilesDir(), TRANSACTIONS_FILE);
        try (ObjectOutputStream output =
                     new ObjectOutputStream(new FileOutputStream(file))) {
            output.writeObject(transactions);
        } catch (IOException exception) {
            throw new IllegalStateException("something bad happened");
        }
    }


    // onClick handler for the "Add" button, this causes an instance of the dialog, which then handles the rest
    public void addTransactionHandler(View view){
        DialogFragment dialog = new AddTransactionDialogFragment();
        dialog.show(getSupportFragmentManager(), "AddTransactionDialogFragment");
    }

    // onClick handler for "Clear" button
    public void clearHandler(View view){

        // Create confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setMessage("WARNING: This will delete all transactions.\nProceed?");
        builder.setPositiveButton(this.getString(R.string.yes), (dialog, which) -> clear(dialog));
        builder.setNegativeButton(this.getString(R.string.cancel), (dialog, which) -> dialog.dismiss());
        AlertDialog alert = builder.create();
        alert.show();
    }

    // Helper for clearHandler
    public void clear(DialogInterface dialog) {
        transactions.clear();
        categories.clear();
        writeCategories();
        writeTransactions();
        adapter.notifyDataSetChanged();
        readTotal();
        dialog.dismiss();
    }

    // Handler for viewing transactions
    public void viewTransactionsHandler(View view){
        Intent intent = new Intent(this, TransactionListActivity.class);
        //intent.putExtra("transactions", transactions);
        startActivity(intent);
    }

    // This is the function that adds a transaction, after getting input from the dialog
    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String cat, String name, String val){
        // All arguments are already trimmed
        DateFormat hourFormat = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat dayFormat = new SimpleDateFormat("MM/dd/yy");
        String date = dayFormat.format(new Date());
        String time = hourFormat.format(new Date());

        Transaction transaction = new Transaction(time, date, name, cat, Double.parseDouble(val));
        transactions.add(0, transaction);
        writeTransactions();
        updateCategoryView(transaction);
    }


    // Updates the value of total, and updates the TextField
    private void readTotal(){
        Double sum = 0.0;
        for(Category c : categories){
            sum += c.getSum();
        }
        totalSum = sum;
        TextView total = findViewById(R.id.totalTextView);
        DecimalFormat df = new DecimalFormat("#.00");
        String display = this.getString(R.string.total);
        if(totalSum.equals(0.0)){
             display += "0.00";
        }
        else {
            display += df.format(totalSum);
        }
        total.setText(display);

    }


    // Takes a newly created transaction, uses its info to update the category it belongs to
    private void updateCategoryView(Transaction transaction){
        String cat = transaction.getCategory();
        boolean found = false;
        for(Category c : categories){
            if(c.getName().equals(cat)){
                c.addToSum(transaction.getCost());
                found = true;
                totalSum += transaction.getCost();
                break;
            }
        }
        // If category of transaction is not found, then create new category, append, sort
        if(!found){
            Category category = new Category(transaction.getCategory(), transaction.getCost());
            categories.add(category);
            totalSum += transaction.getCost();
            Collections.sort(categories);
        }
        // Updates listView
        writeCategories();
        adapter.notifyDataSetChanged();
        // Update total
        TextView total = findViewById(R.id.totalTextView);

        DecimalFormat df = new DecimalFormat("#.00");
        String display = this.getString(R.string.total);
        display += df.format(totalSum);

        total.setText(display);
    }
}
