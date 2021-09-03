package mc.apps.movies.tools;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

public class Ui {
    public static void centerImageAndTextInButton(Button button) {
        Rect textBounds = new Rect();
        //Get text bounds
        CharSequence text = button.getText();
        if (text != null && text.length() > 0) {
            TextPaint textPaint = button.getPaint();
            textPaint.getTextBounds(text.toString(), 0, text.length(), textBounds);
        }
        //Set left drawable bounds
        Drawable leftDrawable = button.getCompoundDrawables()[0];
        if (leftDrawable != null) {
            Rect leftBounds = leftDrawable.copyBounds();
            int width = button.getWidth() - (button.getPaddingLeft() + button.getPaddingRight());
            int leftOffset = (width - (textBounds.width() + leftBounds.width()) - button.getCompoundDrawablePadding()) / 2 - button.getCompoundDrawablePadding();
            leftBounds.offset(leftOffset+10, 0);
            leftDrawable.setBounds(leftBounds);
        }
    }

    public static View showCustomDialog(Activity context,
                                        int custom_layout,
                                        DialogInterface.OnClickListener positiveListener,
                                        DialogInterface.OnClickListener negativeListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View customLayout = context.getLayoutInflater().inflate(custom_layout, null);
        builder.setView(customLayout);

        AlertDialog dialog = builder.create();
        dialog.show();

        return customLayout;
    }
}
