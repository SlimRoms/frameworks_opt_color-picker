package com.enrico.colorpicker;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;

public class ColorPickerDialogFragment extends DialogFragment {

    private ColorPickedListener mListener;

    public static void showColorPicker(AppCompatActivity activity,
                                       ColorPickedListener listener, String tag) {
        ColorPickerDialogFragment fragment = new ColorPickerDialogFragment();
        fragment.mListener = listener;
        fragment.show(activity.getSupportFragmentManager(), tag);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new ColorPickerDialog(getContext(), new ColorPickerDialog.ColorSeletectedListener() {
            @Override
            public void colorPicked(int color) {
                if (mListener != null) mListener.onColorPicked(getTag(), color);
            }
        });
    }

    public interface ColorPickedListener {
        void onColorPicked(String key, int color);
    }
}
