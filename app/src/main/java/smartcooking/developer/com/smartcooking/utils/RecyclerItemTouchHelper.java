package smartcooking.developer.com.smartcooking.utils;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.view.View;

import smartcooking.developer.com.smartcooking.R;

/**
 * Created by ravi on 29/09/17.
 */

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private final RecyclerItemTouchHelperListener listener;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
        // swipeDirs = allowed swipe directions
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            final View foregroundView = ((MyViewHolder) viewHolder).getViewForeground();

            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((MyViewHolder) viewHolder).getViewForeground();

        int primary_color = ContextCompat.getColor(((MyViewHolder) viewHolder).getViewBackground().getContext(), R.color.colorPrimary);
        String hexColor = String.format("#%06X", (0xFFFFFF & primary_color));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) foregroundView.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        // map the "dX" variable (movement variation on 'X' axis) from the interval [0, width} to [0, 255] and set it as the alpha of the background
        int width = displayMetrics.widthPixels;
        int alpha = Math.round(Math.abs(dX) * ((float) 255.0 / (float) width));
        String alpha_str = Integer.toHexString(alpha);
        String hexColor_final = "#" + (alpha_str.length() < 2 ? "0" : "") + alpha_str + hexColor.substring(1);


        ((MyViewHolder) viewHolder).getViewBackground().setBackgroundColor(Color.parseColor(hexColor_final));

        // only change the position of the foreground
        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        final View foregroundView = ((MyViewHolder) viewHolder).getViewForeground();
        getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((MyViewHolder) viewHolder).getViewForeground();

        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        // to call the "onSwiped" method of the "favorite fragment"
        listener.onSwiped(viewHolder);
    }

    public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder);
    }
}