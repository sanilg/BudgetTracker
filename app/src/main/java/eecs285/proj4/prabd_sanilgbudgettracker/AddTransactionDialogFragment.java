package eecs285.proj4.prabd_sanilgbudgettracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;

public class AddTransactionDialogFragment extends DialogFragment {

    private AddTransactionDialogListener listener;
    private View viewInflated;
    // Interface to be implemented by parent activity.
    interface AddTransactionDialogListener{
        void onDialogPositiveClick(DialogFragment dialog, String cat, String name, String val);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstance){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        builder.setTitle(R.string.add_transaction);
        View view = inflater.inflate(R.layout.dialog_add_transaction, null);
        viewInflated = view;
        builder.setView(view);

        // Sets function when positive button is clicked.
        builder.setPositiveButton(R.string.add, null);
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> getDialog().cancel());
        return builder.create();
    }

    @Override
    public void onResume(){
        super.onResume();
        final AlertDialog d = (AlertDialog) getDialog();
        if(d != null){
            Button positiveButton = d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v){
                        Boolean wantToCloseDialog = true;

                        EditText catText = d.findViewById(R.id.categoryInputField);
                        EditText nameText = d.findViewById(R.id.nameInputField);
                        EditText costText = d.findViewById(R.id.costInputField);

                        TextInputLayout catLay = getDialog().findViewById(R.id.catLayout);
                        TextInputLayout nameLay = getDialog().findViewById(R.id.nameLayout);
                        TextInputLayout costLay = getDialog().findViewById(R.id.costLayout);
                        // Check category text
                        if(catText.getText().toString().trim().equals("")){
                            catLay.setError("Enter a category");
                            wantToCloseDialog = false;
                        } else {
                            catLay.setError(null);
                        }

                        // Check name text
                        if(nameText.getText().toString().trim().equals("")){
                            nameLay.setError("Enter a name");
                            wantToCloseDialog = false;
                        } else {
                            nameLay.setError(null);
                        }

                        // Check cost text
                        String costStr = costText.getText().toString().trim();
                        // Check empty
                        if(costText.getText().toString().trim().equals("")){
                            costLay.setError("Enter a valid cost");
                            wantToCloseDialog = false;
                        } else {
                            // Check if there is a decimal
                            int indexOfDecimal = costStr.indexOf('.');
                            // If found, check two decimal places
                            if(indexOfDecimal != -1) {
                                int size = costStr.length();
                                // If error
                                if(size - (indexOfDecimal + 1) != 2){
                                    costLay.setError("Enter a valid cost");
                                    wantToCloseDialog = false;
                                }
                                else {
                                    costLay.setError(null);
                                }
                            }
                            // Else if not found, don't do anything
                            else {
                                costLay.setError(null);
                            }
                        }

                        if(wantToCloseDialog){
                            addTransaction(viewInflated);
                            d.dismiss();
                        }
                    }
                });
        }
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        listener = (AddTransactionDialogListener) context;
    }

    // This function calls custom listener
    private void addTransaction(View view){
        EditText catText = view.findViewById(R.id.categoryInputField);
        EditText nameText = view.findViewById(R.id.nameInputField);
        EditText costText = view.findViewById(R.id.costInputField);
        // Everything passed to the onPositiveClick will be trimmed
        listener.onDialogPositiveClick(this, catText.getText().toString().trim(),
                nameText.getText().toString().trim(), costText.getText().toString().trim());
    }


}
