package com.example.tester;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FlingAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;

import android.animation.LayoutTransition;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.neo.arcchartview.ArcChartView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Vector;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    public int[] stats = new int[6];
    public int[] answers = new int[24];
    Database database = new Database();
    ResultsTable resultsTable = new ResultsTable();
    int mQuestion = 0;
    Button startTest, firstAnswer, secondAnswer, thirdAnswer, toText;
    String[] questions = new String[24];
    String[] firstAnswers = new String [24];
    String[] secondAnswers = new String [24];
    String[] thirdAnswers = new String[24];
    String[] orient, orientDescription = new String[6];
    TextView resultOne, textResultOne, resultTwo, textResultTwo, resultThree, textResultThree, resultFour, textResultFour, resultFive, textResultFive, resultSix, textResultSix;
    TextView Commends, professions;
    ArcChartView resultsView;
    TextSwitcher mSwitcher;
    Animation buttonAnimation;
    ProgressBar progressBar;
    private double mCurrAngle = 0;
    private double mPrevAngle = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Setting buttons and view
        thirdAnswer = (Button) findViewById(R.id.thirdAnswer);
        startTest = (Button) findViewById(R.id.startTest);
        firstAnswer = (Button) findViewById(R.id.firstAnswer);
        secondAnswer = (Button) findViewById(R.id.secondAnswer);
        mSwitcher = (TextSwitcher) findViewById(R.id.textSwitcher);
        progressBar = (ProgressBar) findViewById(R.id.pBar);
        mSwitcher.setInAnimation(getApplicationContext(), android.R.anim.slide_in_left);
        mSwitcher.setOutAnimation(getApplicationContext(), android.R.anim.slide_out_right);
        buttonAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ((ViewGroup) findViewById(R.id.answers)).getLayoutTransition()
                    .enableTransitionType(LayoutTransition.CHANGING);
        }

        //Getting question and descriptions
        questions = getResources().getStringArray(R.array.question);
        firstAnswers = getResources().getStringArray(R.array.firstAnswers);
        secondAnswers = getResources().getStringArray(R.array.secondAnswers);
        thirdAnswers = getResources().getStringArray(R.array.thirdAnswers);
        orient = getResources().getStringArray(R.array.orientations);
        orientDescription = getResources().getStringArray(R.array.professions);

        resultsTable.fillResults();
        setButtons();
    }

    private void setButtons() {
        startTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!database.CheckDatabase(getApplicationContext())) {
                    startTest.setVisibility(View.INVISIBLE);
                    firstAnswer.setVisibility(View.VISIBLE);
                    secondAnswer.setVisibility(View.VISIBLE);
                    thirdAnswer.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    nextQuestion(); }
                else {
                    database.OpenDatabase(getApplicationContext(), stats);
                    showResults();
                }
            }
        });

        firstAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answers[mQuestion] = 1;
                nextQuestion();
            }
        });
        secondAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answers[mQuestion] = 2;
                nextQuestion();
            }
        });
        thirdAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answers[mQuestion] = 3;
                nextQuestion();
            }
        });
    }

    public void nextQuestion() {
        mSwitcher.setText(String.valueOf(questions[mQuestion]));
        firstAnswer.setText(String.valueOf(firstAnswers[mQuestion]));
        secondAnswer.setText(String.valueOf(secondAnswers[mQuestion]));
        thirdAnswer.setText(String.valueOf(thirdAnswers[mQuestion]));
        mQuestion++;
        progressBar.setProgress(mQuestion);
        if (mQuestion == 24) {
            mQuestion = 0;
            knewResults();
        }

    }

    private void knewResults() {
        for (int i = 0; i < answers.length; i++) {
            checkResult(answers[i]);
            mQuestion++;
        }
        database.SaveDatabase(getApplicationContext(), stats);
        showResults();
    }

    private void showResults() {
        setContentView(R.layout.results_layout);
        resultsView = findViewById(R.id.arc_chart_view);
        SetSectionValues(resultsView);
        SetIcons(resultsView);
        resultsView.setOnTouchListener(this);
        toText = (Button) findViewById(R.id.textView);
        toText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTextResults();
            }
        });

    }

    private void SetIcons(ArcChartView resultsView) {
        List<Bitmap> icons = new ArrayList<>();
        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.people);
        icons.add(icon);
        icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.research);
        icons.add(icon);
        icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.practice);
        icons.add(icon);
        icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.art);
        icons.add(icon);
        icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.extreme);
        icons.add(icon);
        icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.economic);
        icons.add(icon);
        resultsView.setSectionIcons(icons);
    }

    private void showTextResults() {
        setContentView(R.layout.text_results);
        Commends = findViewById(R.id.commends);
        professions = findViewById(R.id.professions);
        SettingAttributes();
        SetCommends();
    }

    private void SetCommends() {
        int max = 0;
        int maxCount = 0;
        for (int i = 0; i < stats.length; i++) {
            if (max < stats[i]) {
                max = stats[i];
                maxCount = i;
            }
        }
        Commends.setText("У вас " + orient[maxCount] + "\n" + max + " баллов в этой области");
        professions.setText(orientDescription[maxCount]);
    }

    private void SetSectionValues(ArcChartView n) {
        for (int i = 0; i < 6; i++) {
            n.setSectionValue(i, stats[i]);
            Log.e("SECTIONS", i + " index is " + String.valueOf(stats[i]));
        }
    }

    private void SettingAttributes() {
        resultOne = (TextView) findViewById(R.id.ResultOne);
        resultTwo = (TextView) findViewById(R.id.ResultTwo);
        resultThree = (TextView) findViewById(R.id.ResultThree);
        resultFour = (TextView) findViewById(R.id.ResultFour);
        resultFive = (TextView) findViewById(R.id.ResultFive);
        resultSix = (TextView) findViewById(R.id.ResultSix);

        resultOne.setText(String.valueOf(stats[0]));
        resultTwo.setText(String.valueOf(stats[1]));
        resultThree.setText(String.valueOf(stats[2]));
        resultFour.setText(String.valueOf(stats[3]));
        resultFive.setText(String.valueOf(stats[4]));
        resultSix.setText(String.valueOf(stats[5]));

        textResultOne = (TextView) findViewById(R.id.textResultOne);
        textResultTwo = (TextView) findViewById(R.id.textResultTwo);
        textResultThree = (TextView) findViewById(R.id.textResultThree);
        textResultFour = (TextView) findViewById(R.id.textResultFour);
        textResultFive = (TextView) findViewById(R.id.textResultFive);
        textResultSix = (TextView) findViewById(R.id.textResultSix);

        textResultOne.setText(orient[0]);
        textResultTwo.setText(orient[1]);
        textResultThree.setText(orient[2]);
        textResultFour.setText(orient[3]);
        textResultFive.setText(orient[4]);
        textResultSix.setText(orient[5]);
    }

    private void checkResult(int n) {
        for (int i = 0; i < 6; i++) {
            if (resultsTable.getResults(mQuestion, i) == n) {
                stats[i] += 1;
            }
        }
    }

    private VelocityTracker mVelocityTracker;



    @Override
    public boolean onTouch(final View v, MotionEvent event) {
        final float xc = resultsView.getWidth() / 2;
        final float yc = resultsView.getHeight() / 2;

        final float x = event.getX();
        final float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                if(mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                } else {
                    mVelocityTracker.clear();
                }
                mVelocityTracker.addMovement(event);
                Log.d("1 VELO", String.valueOf(mVelocityTracker.getXVelocity()));
                mCurrAngle = Math.toDegrees(Math.atan2(x - xc, yc - y));
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                mPrevAngle = mCurrAngle;
                mVelocityTracker.addMovement(event);
                Log.d("2 VELO", String.valueOf(mVelocityTracker.getXVelocity()));
                mVelocityTracker.computeCurrentVelocity(1000);
                mCurrAngle = Math.toDegrees(Math.atan2(x - xc, yc - y));
                animate(mPrevAngle, mCurrAngle, 0);
                break;
            }
            case MotionEvent.ACTION_UP : {
                mPrevAngle = mCurrAngle;
                FlingAnimation anim = new FlingAnimation(resultsView, DynamicAnimation.ROTATION);
                mPrevAngle = mCurrAngle;
                Log.d("3 VELO", String.valueOf(mVelocityTracker.getXVelocity()));
                anim.setStartVelocity(mVelocityTracker.getXVelocity())
                        .setFriction(1.1f)
                        .start();
                break;
            }
        }
        return true;
    }


    private void animate(double fromDegrees, double toDegrees,
                         long durationMillis) {
        final RotateAnimation rotate = new RotateAnimation((float) fromDegrees,
                (float) toDegrees, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(durationMillis);
        rotate.setFillEnabled(true);
        rotate.setFillAfter(true);
        rotate.setInterpolator(new LinearInterpolator());
        resultsView.startAnimation(rotate);
    }



}
