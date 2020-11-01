package eecs285.proj4.prabd_sanilgbudgettracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class TransactionAdapter extends ArrayAdapter<Transaction> {
    TransactionAdapter(Context context, int resource, List<Transaction> transactions){
        super(context, resource, transactions);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item_transaction, parent, false);
        }

        Transaction t = getItem(position);
        TextView nameView = convertView.findViewById(R.id.nameView);
        TextView costView = convertView.findViewById(R.id.costView);
        TextView dateView = convertView.findViewById(R.id.dateView);
        TextView catView = convertView.findViewById(R.id.catView);

        nameView.setText(t.getItem());

        Double num = t.getCost();
        String numAsString = String.format("%.2f", num);
        costView.setText("$" + numAsString);

        String dateStr = t.getDate() + "  " + t.getTime();
        dateView.setText(dateStr);
        catView.setText(t.getCategory());


        return convertView;
    }

}
