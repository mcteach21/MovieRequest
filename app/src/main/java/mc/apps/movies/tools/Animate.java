package mc.apps.movies.tools;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

public class Animate {

    public static void rotateView(final View v,int duration, boolean complete) {
        v.animate().setDuration(duration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                })
                .rotation(complete?360f:180f);
    }
}
