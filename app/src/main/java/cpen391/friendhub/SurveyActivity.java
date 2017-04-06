package cpen391.friendhub;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import static android.view.View.*;

public class SurveyActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    private static SurveyHandler sHandler;
    private static int questionTracker[] = {1,1,1,1,1};
    final static int numQuestions = 2;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private static double userAttributes[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        sHandler = new SurveyHandler();
        userAttributes = new double[5];

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }
    protected void onExit(Bundle savedInstanceState){

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_survey, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }
        private boolean isSurveyComplete(){
            int sum = 0;
            for(int i : questionTracker){
                sum += i;
            }
            Log.e("RyanOut","out"+Integer.toString(sum));
            return (sum == numQuestions*5 );

        }

        private void setSurveyResults() {
            sHandler.compileSurveyScores();
            for (int i = 0; i < 5; i++) {
                userAttributes[i] = sHandler.getAttributeSurveyScore(i);
            }
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_survey, container, false);

            final RadioGroup surveyGroup = (RadioGroup) rootView.findViewById(R.id.surveyRadioGroup);
            //Initiate Buttons
            final TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            final RadioButton surveyRadio1 = (RadioButton) rootView.findViewById(R.id.surveyRadioButton1);
            final RadioButton surveyRadio2 = (RadioButton) rootView.findViewById(R.id.surveyRadioButton2);
            final RadioButton surveyRadio3 = (RadioButton) rootView.findViewById(R.id.surveyRadioButton3);
            final RadioButton surveyRadio4 = (RadioButton) rootView.findViewById(R.id.surveyRadioButton4);
            final Button nextButton = (Button) rootView.findViewById(R.id.nextQButton);
            final Button finishButton = (Button) rootView.findViewById(R.id.FinishButton);

            final int attributeNumber = getArguments().getInt(ARG_SECTION_NUMBER);

            if (questionTracker[attributeNumber-1] == numQuestions) nextButton.setVisibility(GONE);


            nextButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(surveyGroup.getCheckedRadioButtonId() != -1) {
                        int attributeNumber = getArguments().getInt(ARG_SECTION_NUMBER);
                        if (questionTracker[attributeNumber - 1] < numQuestions) {
                            questionTracker[attributeNumber - 1]++;
                            surveyGroup.clearCheck();
                            textView.setText(sHandler.getQuestionText(attributeNumber, questionTracker[attributeNumber - 1]));
                        } else if (questionTracker[attributeNumber - 1] == numQuestions && surveyGroup.getCheckedRadioButtonId() != -1)
                            nextButton.setVisibility(GONE);
                        //questionTracker[attributeNumber-1]++;
                        if (isSurveyComplete()) {
                            Log.e("ryanout", "completedSurvey");
                            nextButton.setVisibility(GONE);
                            finishButton.setVisibility(VISIBLE);
                        }

                    }
                }});

            surveyGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                //The page number taken from the switcher is then used as the attribute number for questions
                    if (checkedId == surveyRadio1.getId()) {
                        sHandler.submitSurveyScore(attributeNumber,questionTracker[attributeNumber-1],10);
                    }else if(checkedId == surveyRadio2.getId()){
                        sHandler.submitSurveyScore(attributeNumber,questionTracker[attributeNumber-1],7);
                    }else if(checkedId == surveyRadio3.getId()){
                        sHandler.submitSurveyScore(attributeNumber,questionTracker[attributeNumber-1],4);
                    }else if (checkedId == surveyRadio4.getId()) {
                        sHandler.submitSurveyScore(attributeNumber,questionTracker[attributeNumber-1],1);
                    }

                }

            });
            textView.setText(sHandler.getQuestionText(attributeNumber, questionTracker[attributeNumber-1]));
            finishButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSurveyResults();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("EXIT","SURVEY");
                    intent.putExtra("User Attributes",userAttributes);
                    startActivity(intent);
                }
            });
            return rootView;
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }



        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
                case 3:
                    return "SECTION 4";
                case 4:
                    return "SECTION 5";
            }
            return null;
        }
    }
}
