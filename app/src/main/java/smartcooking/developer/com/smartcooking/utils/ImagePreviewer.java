package smartcooking.developer.com.smartcooking.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import smartcooking.developer.com.smartcooking.R;

/**
 * Code from https://medium.com/@arekk/how-to-preview-image-on-long-click-and-blur-background-351737f5feda
 */

class ImagePreviewer {
    @SuppressLint("ClickableViewAccessibility")
    void show(Context context, ImageView source) {
        BitmapDrawable background = ImagePreviewerUtils.getBlurredScreenDrawable(context, source.getRootView());

        View dialogView = LayoutInflater.from(context).inflate(R.layout.image_preview, null);
        ImageView imageView = dialogView.findViewById(R.id.previewer_image);

        try {
            if (source.getDrawable().getConstantState() != null) {
                Drawable copy = source.getDrawable().getConstantState().newDrawable();
                imageView.setImageDrawable(copy);


                final Dialog dialog = new Dialog(context, R.style.ImagePreviewerTheme);
                if (dialog.getWindow() != null)
                    dialog.getWindow().setBackgroundDrawable(background);
                dialog.setContentView(dialogView);
                dialog.show();

                source.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (dialog.isShowing()) {
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                            int action = event.getActionMasked();
                            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                                v.getParent().requestDisallowInterceptTouchEvent(false);
                                dialog.dismiss();
                                return true;
                            }
                        }
                        return false;
                    }
                });
            }
        } catch (Exception ignored) {

        }
    }
}
