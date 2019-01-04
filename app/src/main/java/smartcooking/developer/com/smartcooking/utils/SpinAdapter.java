package smartcooking.developer.com.smartcooking.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import smartcooking.developer.com.smartcooking.R;
import smartcooking.developer.com.smartcooking.db.Ingredient.Ingredient;

/*

CODE FROM:  https://stackoverflow.com/questions/1625249/android-how-to-bind-spinner-to-custom-object-list

*/

public class SpinAdapter extends ArrayAdapter<Ingredient> {

    // the list of ingredients from the database
    private final List<Ingredient> values;
    private final Context c;
    private final Typeface font;

    public SpinAdapter(Context context, int textViewResourceId,
                       List<Ingredient> values, Context c) {
        super(context, textViewResourceId, values);
        this.values = values;
        this.c = c;
        this.font = ResourcesCompat.getFont(getContext(), R.font.montserrat);
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Ingredient getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        // to draw the spinner with the selected item, when the dropdown is closed

        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(ContextCompat.getColor(c, R.color.colorPrimary));

        // Now, we can get the current item using the values array and the current position
        label.setText(values.get(position).getName());
        label.setTypeface(font);
        label.setTextSize(16);

        return label;
    }

    // And here is when the "chooser" is popped up
    @Override
    public View getDropDownView(int position, View convertView,
                                @NonNull ViewGroup parent) {

        // creates every spinner option

        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(ContextCompat.getColor(c, R.color.colorPrimary));
        label.setText(values.get(position).getName());
        label.setTypeface(font);
        label.setTextSize(16);

        return label;
    }
}