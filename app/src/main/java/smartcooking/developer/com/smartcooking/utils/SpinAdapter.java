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

public class SpinAdapter extends ArrayAdapter<Ingredient> {

    // Your sent context
    // Your custom values for the spinner (User)
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


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(ContextCompat.getColor(c, R.color.colorPrimary));

        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(values.get(position).getName());
        label.setTypeface(font);
        label.setTextSize(16);

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(ContextCompat.getColor(c, R.color.colorPrimary));
        label.setText(values.get(position).getName());
        label.setTypeface(font);
        label.setTextSize(16);

        return label;
    }
}