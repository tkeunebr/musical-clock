package fr.tkeunebr.ic07.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

import fr.tkeunebr.ic07.R;

public class NumberPickerDialogPreference extends DialogPreference {
    private static final int DEFAULT_MIN_VALUE = 0;
    private static final int DEFAULT_MAX_VALUE = 60;
    private static final int DEFAULT_VALUE = 15;
    private int mMinValue;
    private int mMaxValue;
    private int mValue;
    private NumberPicker mNumberPicker;

    public NumberPickerDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        initAttrs(context, attrs);
        initLayout();
    }

    public NumberPickerDialogPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initAttrs(context, attrs);
        initLayout();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        final TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.NumberPickerDialogPreference, 0, 0);
        try {
            setMinValue(a.getInteger(R.styleable.NumberPickerDialogPreference_min, DEFAULT_MIN_VALUE));
            setMaxValue(a.getInteger(R.styleable.NumberPickerDialogPreference_android_max, DEFAULT_MAX_VALUE));
        } finally {
            if (a != null) {
                a.recycle();
            }
        }
    }

    private void initLayout() {
        setDialogLayoutResource(R.layout.preference_number_picker_dialog);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
        setDialogIcon(null);
    }

    public void setMinValue(int minValue) {
        mMinValue = minValue;
        setValue(Math.max(mValue, mMinValue));
    }

    public void setMaxValue(int maxValue) {
        mMaxValue = maxValue;
        setValue(Math.min(mValue, mMaxValue));
    }

    public void setValue(int value) {
        final int newValue = Math.max(Math.min(value, mMaxValue), mMinValue);

        if (newValue != mValue) {
            mValue = newValue;
            persistInt(newValue);
            notifyChanged();
        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            mValue = getPersistedInt(DEFAULT_VALUE);
        } else {
            setValue((Integer) defaultValue);
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, DEFAULT_VALUE);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        mNumberPicker = (NumberPicker) view;
        mNumberPicker.setMinValue(mMinValue);
        mNumberPicker.setMaxValue(mMaxValue);
        mNumberPicker.setValue(mValue);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        // when the user selects "OK", persist the new value
        if (positiveResult) {
            final int numberPickerValue = mNumberPicker.getValue();
            if (callChangeListener(numberPickerValue)) {
                setValue(numberPickerValue);
            }
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        // Check whether this Preference is persistent (continually saved)
        if (isPersistent()) {
            // No need to save instance state since it's persistent, use superclass state
            return superState;
        }

        // Create instance of custom BaseSavedState
        final SavedState savedState = new SavedState(superState);
        // Set the state's value with the class member that holds current setting value
        savedState.value = mNumberPicker.getValue();
        savedState.maxValue = mNumberPicker.getMaxValue();
        savedState.minValue = mNumberPicker.getMinValue();
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        // Check whether we saved the state in onSaveInstanceState
        if (state == null || !state.getClass().equals(SavedState.class)) {
            // Didn't save the state, so call superclass
            super.onRestoreInstanceState(state);
            return;
        }

        // Cast state to custom BaseSavedState and pass to superclass
        final SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());

        // Set this Preference's widget to reflect the restored state
        setMinValue(savedState.minValue);
        setMaxValue(savedState.maxValue);
        setValue(savedState.value);

    }

    private static final class SavedState extends BaseSavedState {
        @SuppressWarnings("unused")
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
        int value;
        int maxValue;
        int minValue;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel source) {
            super(source);

            value = source.readInt();
            maxValue = source.readInt();
            minValue = source.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);

            dest.writeInt(value);
            dest.writeInt(maxValue);
            dest.writeInt(minValue);
        }
    }
}
