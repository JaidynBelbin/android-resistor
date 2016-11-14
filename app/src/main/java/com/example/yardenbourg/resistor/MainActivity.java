package com.example.yardenbourg.resistor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    // Declaring each of my Views
    TextView resistorTextView;
    ImageView bandOne, bandTwo, bandThree, bandTolerance;
    NumberPicker pickerOne, pickerTwo, pickerMultiplier, pickerTolerance;
    Button btnSettings;
    RelativeLayout background;

    // Keys to identify the data being saved
    static final String CURRENT_VALUE = "currentValue";
    static final String PICKER_ONE = "pickerOneValue";
    static final String PICKER_ONE_IMAGE = "pickerOneImage";
    static final String PICKER_TWO = "pickerTwoValue";
    static final String PICKER_TWO_IMAGE = "pickerTwoImage";
    static final String PICKER_MULT = "pickerMultValue";
    static final String PICKER_MULT_IMAGE = "pickerMultImage";
    static final String PICKER_TOL = "pickerTolValue";
    static final String PICKER_TOL_IMAGE = "pickerTolImage";
    static final int SETTINGS_INFO = 1;
    static final String TYPEFACE = "textTypeFace";
    static final String BACKGROUND = "appBackground";
    static final String TEXT_COLOUR = "textColour";

    // Static variables to hold the data to be saved
    String resistorText;
    static int pickerOneVal;
    static int pickerOneImage;
    static int pickerTwoVal;
    static int pickerTwoImage;
    static int pickerMultVal;
    static int pickerMultImage;
    static int pickerTolVal;
    static int pickerTolImage;
    static boolean isTextBold;
    static boolean isBackgroundDark;
    static boolean isThemeLight;
    static boolean isTextLight;

    // Filling four int arrays to hold each ImageViews' possible values. Then, I can get the current image simply
    // by referencing the array element that corresponds to the current picker position.

    public int[] bandOneImageArray = new int[] { R.drawable.brown, R.drawable.red, R.drawable.orange, R.drawable.yellow,
            R.drawable.green, R.drawable.blue, R.drawable.violet, R.drawable.grey, R.drawable.white};

    public int[] bandTwoImageArray = new int[] { R.drawable.black, R.drawable.brown, R.drawable.red, R.drawable.orange, R.drawable.yellow,
            R.drawable.green, R.drawable.blue, R.drawable.violet, R.drawable.grey, R.drawable.white};

    public int[] bandThreeImageArray = new int[] { R.drawable.black, R.drawable.brown, R.drawable.red, R.drawable.orange, R.drawable.yellow,
            R.drawable.green, R.drawable.blue, R.drawable.violet, R.drawable.grey, R.drawable.white, R.drawable.gold,
            R.drawable.silver};

    public int[] bandToleranceImageArray = new int[] { R.drawable.brown, R.drawable.red, R.drawable.orange, R.drawable.yellow,
            R.drawable.green, R.drawable.blue, R.drawable.violet, R.drawable.grey, R.drawable.gold,
            R.drawable.silver};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Changing the theme before any Views are created so it applies to the whole app.
        changeTheme();
        setContentView(R.layout.layout);

        // Initialising the UI
        initUI();

        // Loading the saved data from the SharedPreferences
        loadFromPreferences();
    }

    /**
     * Inflating my custom Action Bar, with the settings button embedded.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Handling the interactions of the menu buttons
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.btnSettings:

                // Creating a new Intent to open up the settings Activity, which will display our preferences screen
                Intent intentPreferences = new Intent(getApplicationContext(), SettingsActivity.class);

                // Starting the new Activity, and getting its result sent back to me.
                startActivityForResult(intentPreferences, SETTINGS_INFO);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method receives the information from the previous activity (SettingsActivity). From here, the
     * settings are changed, and the variables that track the users preferences are saved. Then, the app is forced
     * to restart, which allows the theme to be applied when the user hits the Back button after changing the settings.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // If the code sent back to us matches the one we passed into the Intent above
        if (requestCode == SETTINGS_INFO) {

            // First, the changeSettings() method is called, to set boolean variables to track the users
            // preference based on information from the Preferences activity.
            changeSettings();

            // Then, the saveData() is called, to save the boolean variables that represent the user choices
            // in a sharedPreferences instance, along with the other saved data.
            saveData();

            // Then, a call to recreate() forces the app to restart, because the change in theme has to occur in the
            // onCreate() method
            recreate();
        }
    }

    /**
     * Method to load any data from an existing preferences file in the event of the user not making any changes,
     * and therefore, not creating a savedInstanceState.
     */
    private void loadFromPreferences() {

        // Getting the saved resistor value
        String savedTextViewValue = getPreferences(Context.MODE_PRIVATE).getString(CURRENT_VALUE, "EMPTY");

        // If there is a value saved, set it
        if (!savedTextViewValue.equals("EMPTY")) {
            resistorTextView.setText(savedTextViewValue);
        }

        // Getting each saved NumberPicker value
        int savedPickerOneValue = getPreferences(Context.MODE_PRIVATE).getInt(PICKER_ONE, 0);
        int savedPickerTwoValue = getPreferences(Context.MODE_PRIVATE).getInt(PICKER_TWO, 0);
        int savedPickerThreeValue = getPreferences(Context.MODE_PRIVATE).getInt(PICKER_MULT, 0);
        int savedPickerFourValue = getPreferences(Context.MODE_PRIVATE).getInt(PICKER_TOL, 0);

        // If there are values saved, set them.
        if (!(savedPickerOneValue == 0)) {
            pickerOne.setValue(savedPickerOneValue);
        }

        if (!(savedPickerTwoValue == 0)) {
            pickerTwo.setValue(savedPickerTwoValue);
        }

        if (!(savedPickerThreeValue == 0)) {
            pickerMultiplier.setValue(savedPickerThreeValue);
        }

        if (!(savedPickerFourValue == 0)) {
            pickerTolerance.setValue(savedPickerFourValue);
        }

        // Getting each saved image state
        int savedBandOneImage = getPreferences(Context.MODE_PRIVATE).getInt(PICKER_ONE_IMAGE, R.drawable.brown);
        int savedBandTwoImage = getPreferences(Context.MODE_PRIVATE).getInt(PICKER_TWO_IMAGE, R.drawable.black);
        int savedBandThreeImage = getPreferences(Context.MODE_PRIVATE).getInt(PICKER_MULT_IMAGE, R.drawable.black);
        int savedBandFourImage = getPreferences(Context.MODE_PRIVATE).getInt(PICKER_TOL_IMAGE, R.drawable.brown);

        // If there are saved images, set them.
        if (!(savedBandOneImage == R.drawable.brown)) {
            bandOne.setImageResource(savedBandOneImage);
        }

        if (!(savedBandTwoImage == R.drawable.black)) {
            bandTwo.setImageResource(savedBandTwoImage);
        }

        if (!(savedBandThreeImage == R.drawable.black)) {
            bandThree.setImageResource(savedBandThreeImage);
        }

        if (!(savedBandFourImage == R.drawable.brown)) {
            bandTolerance.setImageResource(savedBandFourImage);
        }

        // Getting the boolean value that represents whether the TextView typeface was set to bold or not
        boolean savedTypeface = getPreferences(Context.MODE_PRIVATE).getBoolean(TYPEFACE, false);
        boolean savedTextColour = getPreferences(Context.MODE_PRIVATE).getBoolean(TEXT_COLOUR, false);

        // If the text was set to bold
        if (!savedTypeface == false) {

            // Make the text bold again
            resistorTextView.setTypeface(null, Typeface.BOLD);
        }

        // If the background is dark, change the text colour to white
        if (!savedTextColour == false) {

            resistorTextView.setTextColor(getResources().getColor(R.color.white));
        }
    }

    /**
     * Method to initialise the Views and set the onValueChangedListeners on each picker.
     */
    protected void initUI() {

        // Instantiating each of my Views and Layout
        bandOne = (ImageView)findViewById(R.id.colorOne);
        bandTwo = (ImageView)findViewById(R.id.colorTwo);
        bandThree = (ImageView)findViewById(R.id.colorThree);
        bandTolerance = (ImageView)findViewById(R.id.Tolerance);
        resistorTextView = (TextView)findViewById(R.id.resValue);
        pickerOne = (NumberPicker)findViewById(R.id.pickerOne);
        pickerTwo = (NumberPicker)findViewById(R.id.pickerTwo);
        pickerMultiplier = (NumberPicker)findViewById(R.id.pickerThree);
        pickerTolerance = (NumberPicker)findViewById(R.id.pickerFour);
        btnSettings = (Button)findViewById(R.id.btnSettings);
        background = (RelativeLayout)findViewById(R.id.appBackground);

        // Filling four string arrays from the ones defined in the strings.xml resource file.
        String[] pickerOneArray = getResources().getStringArray(R.array.picker_one);
        String[] pickerTwoArray = getResources().getStringArray(R.array.picker_two);
        String[] pickerMultiplierArray = getResources().getStringArray(R.array.picker_multiplier);
        String[] pickerToleranceArray = getResources().getStringArray(R.array.picker_tolerance);

        // Setting the minimum and maximum values for each picker and filling their displayed values with strings
        // instead of numbers for each colour.
        pickerOne.setMinValue(0);
        pickerOne.setMaxValue(pickerOneArray.length -1);
        pickerOne.setDisplayedValues(pickerOneArray);

        pickerTwo.setMinValue(0);
        pickerTwo.setMaxValue(pickerTwoArray.length -1);
        pickerTwo.setDisplayedValues(pickerTwoArray);

        pickerMultiplier.setMinValue(0);
        pickerMultiplier.setMaxValue(pickerMultiplierArray.length -1);
        pickerMultiplier.setDisplayedValues(pickerMultiplierArray);

        pickerTolerance.setMinValue(0);
        pickerTolerance.setMaxValue(pickerToleranceArray.length -1);
        pickerTolerance.setDisplayedValues(pickerToleranceArray);

        // Calculating the initial value of the resistor as it first appears (10 Ohms with a 1% bandTolerance).
        resistorTextView.setText(calculateValue());

        /**
         * Changes the first value band. Note that the colours start at "Brown", or 1, because the first value
         * obviously can't be zero. Even though the case starts at zero, this is accounted for in the calculateValue()
         * method, where another switch/case moves the value up by one (so case 0 means the value is 1, and so on).
         */
        pickerOne
                .setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal,
                                              int newVal) {

                        switch (newVal) {
                            case 0:
                                bandOne.setImageResource(bandOneImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 1:
                                bandOne.setImageResource(bandOneImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 2:
                                bandOne.setImageResource(bandOneImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 3:
                                bandOne.setImageResource(bandOneImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 4:
                                bandOne.setImageResource(bandOneImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 5:
                                bandOne.setImageResource(bandOneImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 6:
                                bandOne.setImageResource(bandOneImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 7:
                                bandOne.setImageResource(bandOneImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 8:
                                bandOne.setImageResource(bandOneImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                        }

                        // Collecting the data to save
                        saveData();
                    }
                });

        /**
         * Changes the second band
         */
        pickerTwo
                .setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal,
                                              int newVal) {

                        switch (newVal) {
                            case 0:
                                bandTwo.setImageResource(bandTwoImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 1:
                                bandTwo.setImageResource(bandTwoImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 2:
                                bandTwo.setImageResource(bandTwoImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 3:
                                bandTwo.setImageResource(bandTwoImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 4:
                                bandTwo.setImageResource(bandTwoImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 5:
                                bandTwo.setImageResource(bandTwoImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 6:
                                bandTwo.setImageResource(bandTwoImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 7:
                                bandTwo.setImageResource(bandTwoImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 8:
                                bandTwo.setImageResource(bandTwoImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 9:
                                bandTwo.setImageResource(bandTwoImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                        }

                        // Collecting the data to save
                        saveData();
                    }
                });

        /**
         * Changes the third band
         */
        pickerMultiplier
                .setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                        switch (newVal) {
                            case 0:
                                bandThree.setImageResource(bandThreeImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 1:
                                bandThree.setImageResource(bandThreeImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 2:
                                bandThree.setImageResource(bandThreeImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 3:
                                bandThree.setImageResource(bandThreeImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 4:
                                bandThree.setImageResource(bandThreeImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 5:
                                bandThree.setImageResource(bandThreeImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 6:
                                bandThree.setImageResource(bandThreeImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 7:
                                bandThree.setImageResource(bandThreeImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 8:
                                bandThree.setImageResource(bandThreeImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 9:
                                bandThree.setImageResource(bandThreeImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 10:
                                bandThree.setImageResource(bandThreeImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 11:
                                bandThree.setImageResource(bandThreeImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                        }

                        // Collecting the data to save
                        saveData();
                    }
                });

        /**
         * Changes the fourth band
         */
        pickerTolerance
                .setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                        switch(newVal) {

                            case 0:
                                bandTolerance.setImageResource(bandToleranceImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 1:
                                bandTolerance.setImageResource(bandToleranceImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 2:
                                bandTolerance.setImageResource(bandToleranceImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 3:
                                bandTolerance.setImageResource(bandToleranceImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 4:
                                bandTolerance.setImageResource(bandToleranceImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 5:
                                bandTolerance.setImageResource(bandToleranceImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 6:
                                bandTolerance.setImageResource(bandToleranceImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 7:
                                bandTolerance.setImageResource(bandToleranceImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 8:
                                bandTolerance.setImageResource(bandToleranceImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                            case 9:
                                bandTolerance.setImageResource(bandToleranceImageArray[newVal]);
                                resistorTextView.setText(calculateValue());
                                break;
                        }

                        // Collecting the data to save
                        saveData();
                    }
                });
    }

    /**
     * Method to update user settings
     */
    private void changeSettings() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // If the Dark Mode checkbox was clicked...
        if (sharedPreferences.getBoolean("pref_dark_mode", false)) {

            // The user wants the background to be dark
            isBackgroundDark = true;
            // and the text to be light
            isTextLight = true;

        } else {

            // Otherwise, they want the background to return to normal
            isBackgroundDark = false;
            // and the text to return to normal as well
            isTextLight = false;
        }

        // If the Bold Text checkbox was clicked...
        if (sharedPreferences.getBoolean("pref_text_bold", false)) {

            // Set the typeface to bold
            resistorTextView.setTypeface(null, Typeface.BOLD);
            isTextBold = true;

        } else {

            // When it becomes unchecked, set the typeface back to normal.
            resistorTextView.setTypeface(null, Typeface.NORMAL);
            isTextBold = false;
        }
    }

    /**
     * Method to copy the current data to variables to store in a sharedPreferences file
     */
    public void saveData() {

        // Getting the current state of each of the Pickers and TextArea.
        // The current images are inferred from the current value of the Spinners,
        // eliminating the need to set them each time the Spinners are changed, and meaning if
        // the user doesn't change any of the Spinners, the image state isn't lost.
        resistorText = resistorTextView.getText().toString();
        pickerOneVal = pickerOne.getValue();
        pickerTwoVal = pickerTwo.getValue();
        pickerMultVal = pickerMultiplier.getValue();
        pickerTolVal = pickerTolerance.getValue();
        pickerOneImage = bandOneImageArray[pickerOneVal];
        pickerTwoImage = bandTwoImageArray[pickerTwoVal];
        pickerMultImage = bandThreeImageArray[pickerMultVal];
        pickerTolImage = bandToleranceImageArray[pickerTolVal];

        // Creating a SharedPreferences instance
        SharedPreferences.Editor sPEditor = getPreferences(Context.MODE_PRIVATE).edit();

        // Putting each piece of data into the SharedPreferences file
        sPEditor.putString(CURRENT_VALUE, resistorText);
        sPEditor.putInt(PICKER_ONE, pickerOneVal);
        sPEditor.putInt(PICKER_TWO, pickerTwoVal);
        sPEditor.putInt(PICKER_MULT, pickerMultVal);
        sPEditor.putInt(PICKER_TOL, pickerTolVal);
        sPEditor.putInt(PICKER_ONE_IMAGE, pickerOneImage);
        sPEditor.putInt(PICKER_TWO_IMAGE, pickerTwoImage);
        sPEditor.putInt(PICKER_MULT_IMAGE, pickerMultImage);
        sPEditor.putInt(PICKER_TOL_IMAGE, pickerTolImage);
        sPEditor.putBoolean(TYPEFACE, isTextBold);
        sPEditor.putBoolean(BACKGROUND, isBackgroundDark);
        sPEditor.putBoolean(TEXT_COLOUR, isTextLight);

        sPEditor.commit();
    }

    /**
     * Method to change the theme based on the users saved preference
     */
    public void changeTheme() {

        isThemeLight = getPreferences(Context.MODE_PRIVATE).getBoolean(BACKGROUND, false);

        if (!isThemeLight == false) {

            setTheme(R.style.AppThemeDark);

        } else {

            setTheme(R.style.AppThemeLight);
        }
    }

    /**
     * Method to calculate resistor value.
     * @return String
     */
    public String calculateValue() {

        int val1;
        int val2;
        double mult = 1;
        double tol = 1;
        double total;
        String finalTotal;

        // If the pickers have not been set, give the two bands initial starting values
        if (pickerOne.getValue() < 0 && pickerTwo.getValue() < 0) {

            val1 = 1;
            val2 = 0;
        } else {

            // Otherwise, get the current value from the pickers
            val1 = pickerOne.getValue();
            val2 = pickerTwo.getValue();
        }

        /**
         * Because the NumberPickers start at zero, I needed to modify the value of the first variable at each point
         * to ensure that both values don't start at zero. I don't have to worry about the second band, because
         * it has to start at zero.
         */
        switch(pickerOne.getValue()) {

            case 0:
                val1 = 1;
                break;
            case 1:
                val1 = 2;
                break;
            case 2:
                val1 = 3;
                break;
            case 3:
                val1 = 4;
                break;
            case 4:
                val1 = 5;
                break;
            case 5:
                val1 = 6;
                break;
            case 6:
                val1 = 7;
                break;
            case 7:
                val1 = 8;
                break;
            case 8:
                val1 = 9;
                break;
        }

        /**
         * Modifying the multiplier value at each point on the NumberPicker so it scales properly, i.e.
         * so it multiplies by 10, 100, 1000 etc.
         */
        switch(pickerMultiplier.getValue()) {

            case 0:
                break;
            case 1:
                mult = 10;
                break;
            case 2:
                mult = 100;
                break;
            case 3:
                mult = 1000;
                break;
            case 4:
                mult = 10000;
                break;
            case 5:
                mult = 100000;
                break;
            case 6:
                mult = 1000000;
                break;
            case 7:
                mult = 10000000;
                break;
            case 8:
                mult = 100000000;
                break;
            case 9:
                mult = 1000000000;
                break;
            case 10:
                mult = 0.1;
                break;
            case 11:
                mult = 0.01;
                break;
        }

        /**
         * Modifying the multiplier value at each point on the NumberPicker so it scales properly, i.e.
         * so it multiplies by 10, 100, 1000 etc.
         */
        switch(pickerTolerance.getValue()) {

            case 0:
                break;
            case 1:
                tol = 2;
                break;
            case 2:
                tol = 3;
                break;
            case 3:
                tol = 4;
                break;
            case 4:
                tol = 0.5;
                break;
            case 5:
                tol = 0.25;
                break;
            case 6:
                tol = 0.1;
                break;
            case 7:
                tol = 0.05;
                break;
            case 8:
                tol = 5;
                break;
            case 9:
                tol = 10;
                break;
        }

        // Calculating the total
        total = ((10 * val1) + (1 * val2)) * mult;

        if (total >= 1e6) {
            /**
             * If the total is greater than or equal to 1000000, i.e. a M Ohm, divide by
             * 1000000 to determine the number of millions, and cast the value as a String
             * in order to append "M Ohms" to it.
             */
            total /= 1e6;
            finalTotal = Double.toString(total);
            finalTotal += " M Ohms";

        } else if (total >= 1e3) {

            /**
             * If the total is greater than or equal to 1000, i.e. a K Ohm, divide by
             * 1000 to determine the number of thousands, and cast the value as a String
             * in order to append "K Ohms" to it.
             */
            total /= 1e3;
            finalTotal = Double.toString(total);
            finalTotal += " K Ohms";

        } else {

            /**
             * If none of the above, simply keep the value as is and add "Ohms" to the end.
             */
        finalTotal = Double.toString(total);
        finalTotal += " Ohms";

        }

        // Appending the bandTolerance value and returning the final String
        finalTotal += " " + tol + "%";
        return finalTotal;
     }
}
