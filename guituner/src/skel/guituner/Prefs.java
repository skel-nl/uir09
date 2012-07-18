package skel.guituner;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import skel.misc.note.Tuning;
import skel.misc.note.TuningUtils;

public class Prefs extends PreferenceActivity {

    private static final String OPT_TUNING = "tuning";
    private static final String OPT_TUNING_DEF = "E";

    private static final String OPT_TUNING_SHIFT_CHECKBOX = "tuning_shift_checkbox";
    private static final boolean OPT_TUNING_SHIFT_CHECKBOX_DEF = false;

    private static final String OPT_TUNING_SHIFT = "tuning_shift";
    private static final String OPT_TUNING_SHIFT_DEF = "-1";

    private static final String OPT_SAMPLE_RATE = "sample_rate";
    private static final String OPT_SAMPLE_RATE_DEF = "8000";

    private static final String OPT_SAMPLE_LOG = "sample_log";
    private static final String OPT_SAMPLE_LOG_DEF = "13";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    public static Tuning getTuning(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        Tuning tuning = TuningUtils.getTuning(prefs.getString(OPT_TUNING, OPT_TUNING_DEF));

        boolean shiftingNeeded = prefs.getBoolean(OPT_TUNING_SHIFT_CHECKBOX,
                OPT_TUNING_SHIFT_CHECKBOX_DEF);

        if (shiftingNeeded) {
            // prefs.getInt() doesn't work correctly for ListPreference
            int shift = Integer.parseInt(prefs.getString(OPT_TUNING_SHIFT, OPT_TUNING_SHIFT_DEF));
            tuning = tuning.shift(shift);
        }

        return tuning;
    }

    public static int getSampleRate(Context context) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context)
                .getString(OPT_SAMPLE_RATE, OPT_SAMPLE_RATE_DEF));
    }

    public static int getSampleLog(Context context) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context)
                .getString(OPT_SAMPLE_LOG, OPT_SAMPLE_LOG_DEF));
    }
}
