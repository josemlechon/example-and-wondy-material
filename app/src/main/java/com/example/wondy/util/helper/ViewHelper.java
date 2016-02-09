package com.example.wondy.util.helper;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wondy.R;

import java.util.List;


/**
 * @author jose m lechon on 08/02/16.
 * @version 0.1.0
 * @since 1
 */
public class ViewHelper {

    /**
     * Catch multiple click events
     *
     * @param view view clicked
     * @return true | false
     */
    public static boolean isMultipleClickDone(@NonNull View view) {

        if (view.getTag(R.id.id_view_time_last_click) == null) {

            view.setTag(R.id.id_view_time_last_click, SystemClock.elapsedRealtime());
            return Boolean.FALSE;
        }
        Long mLastClickTime = (Long) view.getTag(R.id.id_view_time_last_click);
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return Boolean.TRUE;
        }

        view.setTag(R.id.id_view_time_last_click, SystemClock.elapsedRealtime());
        return Boolean.FALSE;
    }


    /**
     * Change a view to enabled False during a second
     * then it changes again to enabled TRUE.
     * Useful for onClick events in order to avoid multiple clicks.
     *
     * @param view View you want to block
     */
    public static void blockViewShortTime(@NonNull final View view) {

        view.setEnabled(Boolean.FALSE);
        Handler handler = new Handler();

        Runnable r = new Runnable() {
            public void run() {
                view.setEnabled(Boolean.TRUE);
            }
        };
        handler.postDelayed(r, 1500);
    }

    /**
     * Hide keyboard
     *
     * @param activity activity
     */
    public static void hideKeyboard(@NonNull Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View f = activity.getCurrentFocus();
        if (null != f && null != f.getWindowToken() && EditText.class.isAssignableFrom(f.getClass()))
            imm.hideSoftInputFromWindow(f.getWindowToken(), 0);
        else
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    /**
     * Scale (bigger or lower) part of a text from an string.
     *
     * @param scale a float value the seed will be scaled
     * @param text  full text where the span will be applied
     * @param seed  text you want to scale
     */
    public static SpannableStringBuilder spanSizeText(float scale, String text, String seed) {

        int startSeed = text.indexOf(seed);
        int endSeed = startSeed + seed.length();

        SpannableStringBuilder span = new SpannableStringBuilder(text);
        span.setSpan(new RelativeSizeSpan(scale), startSeed, endSeed, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return span;
    }

    /**
     * Scale (bigger or lower) part of a text from an string.
     *
     * @param scale a float value the seed will be scaled
     * @param text  full text where the span will be applied
     * @param seeds text you want to scale
     */
    public static SpannableStringBuilder spanSizeText(float scale, String text, List<String> seeds) {

        SpannableStringBuilder span = new SpannableStringBuilder(text);

        for (String itemSeed : seeds) {
            int startSeed = text.indexOf(itemSeed);
            int endSeed = startSeed + itemSeed.length();
            span.setSpan(new RelativeSizeSpan(scale), startSeed, endSeed, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return span;
    }

    /**
     * Bold part of a text from an string.
     *
     * @param text full text where the span will be applied
     * @param seed text you want to change to bold
     */
    public static SpannableStringBuilder spanBoldText(String text, String seed) {

        SpannableStringBuilder sb = new SpannableStringBuilder(text);

        if (TextUtils.isEmpty(text) || TextUtils.isEmpty(seed))
            return sb;

        int startSeed = text.indexOf(seed);
        int endSeed = startSeed + seed.length();

        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD); // Span to make text bold
        sb.setSpan(bss, startSeed, endSeed, Spannable.SPAN_INCLUSIVE_INCLUSIVE); // make first 4 characters Bold

        return sb;
    }


    /**
     * Set the max num of characters an EditText can accept
     *
     * @param textView TextView or class that extends it.
     * @param limit    limit of characters
     */
    public static void setLimitCharacters(@NonNull TextView textView, int limit) {


        //Set limit Description
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(limit);
        textView.setFilters(fArray);
    }


    public static boolean checkNetworkValidAndShowToast(@NonNull Context context) {


        if (!NetworkHelper.isNetworkAvailable(context)) {

            Toast.makeText(context, context.getString(R.string.error_network), Toast.LENGTH_LONG).show();
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }


    public static boolean checkNetworkValidAndShowAlert(@NonNull View view) {

        if (!NetworkHelper.isNetworkAvailable(view.getContext())) {

            Snackbar.make(view, view.getResources().getString(R.string.error_network), Snackbar.LENGTH_LONG)
                    .show();
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }




}
