package smartcooking.developer.com.smartcooking.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.view.View;

/**
 * Code from https://medium.com/@arekk/how-to-preview-image-on-long-click-and-blur-background-351737f5feda
 */

class ImagePreviewerUtils {

    static BitmapDrawable getBlurredScreenDrawable(Context context, View screen) {
        Bitmap screenshot = takeScreenshot(screen);
        Bitmap blurred = blurBitmap(context, screenshot);
        return new BitmapDrawable(context.getResources(), blurred);
    }

    private static Bitmap takeScreenshot(View screen) {
        screen.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screen.getDrawingCache());
        screen.setDrawingCacheEnabled(false);
        return bitmap;
    }

    private static Bitmap blurBitmap(Context context, Bitmap bitmap) {
        float bitmapScale = 0.3f;
        float blurRadius = 10f;

        int width = Math.round(bitmap.getWidth() * bitmapScale);
        int height = Math.round(bitmap.getHeight() * bitmapScale);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        theIntrinsic.setRadius(blurRadius);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }
}
