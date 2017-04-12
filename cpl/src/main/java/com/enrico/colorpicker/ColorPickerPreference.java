package com.enrico.colorpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ColorPickerPreference extends Preference implements
        ColorPickerDialog.ColorSeletectedListener {

    private int mColor = Color.RED;
    private boolean mAlphaEnabled = false;
    private CircleDrawable mDrawable;
    private ColorPickerDialog mDialog;

    public ColorPickerPreference(Context context) {
        this(context, null);
    }

    public ColorPickerPreference(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ColorPickerPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ColorPickerPreference, 0, 0);

        mColor = a.getColor(R.styleable.ColorPickerPreference_defaultColor, mColor);
        mAlphaEnabled = a.getBoolean(R.styleable.ColorPickerPreference_alphaEnabled, mAlphaEnabled);

        a.recycle();
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, Color.RED);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        colorPicked(restoreValue ? getPersistedInt(mColor) : (Integer) defaultValue);
    }

    @Override
    public void colorPicked(int color) {
        mColor = color;
        if (mDrawable != null) {
            mDrawable.setColor(color);
        }
        persistInt(mColor);

        if (getOnPreferenceChangeListener() != null) {
            getOnPreferenceChangeListener().onPreferenceChange(this, color);
        }
    }

    @Override
    public void performClick() {
        mDialog = new ColorPickerDialog(getContext(), this);
        mDialog.show();
        mDialog.updateColor(mColor);
        mDialog.setAlphaEnabled(mAlphaEnabled);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        float density = getContext().getResources().getDisplayMetrics().density;

        LinearLayout widgetFrame = (LinearLayout) holder.findViewById(android.R.id.widget_frame);
        if (widgetFrame == null)
            return;

        widgetFrame.setVisibility(View.VISIBLE);
        if (widgetFrame.getChildCount() > 0) {
            widgetFrame.removeAllViews();
        }

        mDrawable = new CircleDrawable(mColor);
        ImageView iv = new ImageView(getContext());
        iv.setLayoutParams(new LinearLayout.LayoutParams(
                ((int) density * 31), ((int) density * 31)));
        iv.setImageDrawable(mDrawable);
        widgetFrame.addView(iv);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        if (mDialog == null || !mDialog.isShowing()) {
            return superState;
        }

        final SavedState state = new SavedState(superState);
        state.dialogBundle = mDialog.onSaveInstanceState();
        return state;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        mDialog = new ColorPickerDialog(getContext(), this);
        if (savedState.dialogBundle != null) {
            mDialog.onRestoreInstanceState(savedState.dialogBundle);
        }
        mDialog.show();
        mDialog.updateColor(mColor);
    }

    private static class SavedState extends BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
        Bundle dialogBundle;

        public SavedState(Parcel source) {
            super(source);
            dialogBundle = source.readBundle(getClass().getClassLoader());
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeBundle(dialogBundle);
        }
    }
}
