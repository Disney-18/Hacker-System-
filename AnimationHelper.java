package com.hacker.finalapp;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class AnimationHelper {

    public static void slideInFromRight(View view) {
        TranslateAnimation animation = new TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 1.0f,
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f
        );
        animation.setDuration(300);
        view.startAnimation(animation);
    }

    public static void pulseEffect(View view) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(
            1.0f, 1.1f, 1.0f, 1.1f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAnimation.setDuration(150);
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        scaleAnimation.setRepeatCount(1);
        view.startAnimation(scaleAnimation);
    }

    public static void hackSuccessAnimation(View view) {
        ScaleAnimation scale = new ScaleAnimation(
            1.0f, 1.2f, 1.0f, 1.2f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        );
        scale.setDuration(200);
        scale.setRepeatMode(Animation.REVERSE);
        scale.setRepeatCount(1);
        view.startAnimation(scale);
    }

    public static void errorShakeAnimation(View view) {
        TranslateAnimation shake = new TranslateAnimation(0, 10, 0, 0);
        shake.setDuration(50);
        shake.setRepeatMode(Animation.REVERSE);
        shake.setRepeatCount(3);
        view.startAnimation(shake);
    }

    public static void fadeIn(View view) {
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(400);
        view.startAnimation(animation);
    }

    public static void powerUpAppearAnimation(View view) {
        ScaleAnimation scale = new ScaleAnimation(
            0.0f, 1.0f, 0.0f, 1.0f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        );
        scale.setDuration(400);

        AlphaAnimation alpha = new AlphaAnimation(0.0f, 1.0f);
        alpha.setDuration(400);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(scale);
        animationSet.addAnimation(alpha);

        view.startAnimation(animationSet);
    }
}
