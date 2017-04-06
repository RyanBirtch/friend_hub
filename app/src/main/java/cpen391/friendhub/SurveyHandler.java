package cpen391.friendhub;

import android.util.Log;

/**
 * Created by Ryan on 2017-03-13.
 */



public class SurveyHandler {
    double att1; //extraversion;
    double att2; //openness;
    double att3; //agreeableness;
    double att4; //conscientiousness;
    double att5; //neuroticism;

    String[] attributeStrings = {"Extraversion","Openness","Agreeableness","Conscientiousness","Neuroticism"};

    String questionArray[][] = {{"I frequently like to go out with large groups of people", "I enjoy talking to and meeting new people"},
                                {"I speak openly and plainly to strangers about myself","I tell my close family and friends about my life in great detail"},
                                {"I don't like to argue with people", "I prefer to avoid disputes with friend and family"},
                                {"I examine my actions and how they affect others every day","I spend a lot of time planning and thinking through tasks"},
                                {"I frequently feel envious of other's accomplishments","My mood fluctuates greatly during the day"}
                                };

    final int numQuestions = 2;


    int att1Scores[] = new int[numQuestions];
    int att2Scores[] = new int[numQuestions];
    int att3Scores[] = new int[numQuestions];
    int att4Scores[] = new int[numQuestions];
    int att5Scores[] = new int[numQuestions];

    public String getAttributeString(int attribute){
        return attributeStrings[attribute];
    }

    public double getAttributeSurveyScore(int attributeNumber){
        compileSurveyScores();
        switch (attributeNumber){
            case 1:
                return att1;
            case 2:
                return att2;
            case 3:
                return att3;
            case 4:
                return att4;
            case 5:
                return att5;
        }
        return 0;
    }

    public void submitSurveyScore(int attributeNumber,int questionNumber, int score){
        if(!(attributeNumber >= questionArray.length || questionNumber >= questionArray[0].length)) {

            switch (attributeNumber) {
                case 1:
                    att1Scores[questionNumber] = score;
                case 2:
                    att2Scores[questionNumber] = score;
                case 3:
                    att3Scores[questionNumber] = score;
                case 4:
                    att4Scores[questionNumber] = score;
                case 5:
                    att5Scores[questionNumber] = score;
            }
        }
    }

    public void compileSurveyScores(){
        for(int i : att1Scores) att1+=i/numQuestions;

        for(int i : att2Scores) att2+=i/numQuestions;

        for(int i : att3Scores) att3+=i/numQuestions;

        for(int i : att4Scores) att4+=i/numQuestions;

        for(int i : att5Scores) att5+=i/numQuestions;
    }

    public String getQuestionText(int attributeNumber, int questionNumber) {
        if(attributeNumber >= questionArray.length || questionNumber >= questionArray[0].length)
            return "";
        Log.e("RyanOut","Attribute " + attributeNumber + " Question " + questionNumber);
        return questionArray[attributeNumber][questionNumber];
        //return "Stand in"+ attributeNumber + questionNumber;
    }
}