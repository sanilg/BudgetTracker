package eecs285.proj4.prabd_sanilgbudgettracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import android.content.Context;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CategoryAdapter extends ArrayAdapter<Category> {
    CategoryAdapter(Context context, int resource, ArrayList<Category> categories){
        super(context, resource, categories);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_categories, parent, false);
        }

        // Sets name and number for each category
        TextView nameView = convertView.findViewById(R.id.categoryTitleView);
        nameView.setText(getItem(position).getName());

        TextView valueView = convertView.findViewById(R.id.categoryValueView);
        double num = getItem(position).getSum();
        String numAsString = String.format("%.2f", num);
        valueView.setText("$"+numAsString);
        return convertView;
    }
}
