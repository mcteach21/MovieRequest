package mc.apps.movies.tools;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import mc.apps.movies.R;

public class Dialogs {
    public static void showCustomDialog(Activity context,
                                        int custom_layout,
                                        String title,
                                        String positiveTitle,
                                        String negativeTitle,
                                        DialogInterface.OnClickListener positiveListener,
                                        DialogInterface.OnShowListener onShowListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        if(!title.isEmpty())
            builder.setTitle(title);

        final View customLayout = context.getLayoutInflater().inflate(custom_layout, null);
        builder.setView(customLayout);

        if(!positiveTitle.isEmpty())
            builder.setPositiveButton(positiveTitle, positiveListener);
        if(!negativeTitle.isEmpty())
            builder.setNegativeButton(negativeTitle, (dialog, which) -> {
                dialog.dismiss();
            });
        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(onShowListener);

        dialog.show();
    }


}
