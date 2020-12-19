package work.kgeun.intelligenttutoringsystem.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.Timer;
import java.util.TimerTask;

import work.kgeun.intelligenttutoringsystem.R;
import work.kgeun.intelligenttutoringsystem.databinding.ActivitySceneBinding;
import work.kgeun.intelligenttutoringsystem.items.Scene;

/**
 * Created by kgeun on 2017. 12. 4..
 */

public class SceneActivity extends AppCompatActivity implements ExoPlayer.EventListener {

    private BandwidthMeter bandwidthMeter;
    private TrackSelector trackSelector;
    private TrackSelection.Factory trackSelectionFactory;
    private SimpleExoPlayer player;
    private DataSource.Factory dataSourceFactory;
    private ExtractorsFactory extractorsFactory;
    private DefaultBandwidthMeter defaultBandwidthMeter;
    private MediaSource mediaSource;
    private TimerTask timerTask = null;
    private TimerTask timerTask2 = null;
    private Timer timer = null;
    private Timer timer2 = null;
    private TextView[] nursesQuestions;

    private String songUrl = "http://programmerguru.com/android-tutorial/wp-content/uploads/2013/04/hosannatelugu.mp3";

    ActivitySceneBinding binding;
    Scene mScene;
    View[] elements = new View[5];
    float[][] originalPositionOfElements = new float[5][2];
    MediaPlayer mp;

    final int STETHOSCOPE = 0;
    final int THERMOMETER = 1;
    final int SPHYGMOMANOMETER = 2;
    final int PULSE_OXIMETER = 3;
    final int STOPWATCH = 4;

    int courseId, lessonId, orderNumber;

    @Override
    protected void onResume() {

        binding.getRoot().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.getRoot().getViewTreeObserver().removeGlobalOnLayoutListener(this);

                for(int i = 0 ; i < 5 ;i++) {
                    originalPositionOfElements[i][0] = elements[i].getX();
                    originalPositionOfElements[i][1] = elements[i].getY();
                }
            }
        });

        super.onResume();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_scene);

        bandwidthMeter = new DefaultBandwidthMeter();
        extractorsFactory = new DefaultExtractorsFactory();

        trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);

        trackSelector = new DefaultTrackSelector(trackSelectionFactory);

        defaultBandwidthMeter = new DefaultBandwidthMeter();

        dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "mediaPlayerSample"),defaultBandwidthMeter);

        mediaSource = new ExtractorMediaSource(Uri.parse(songUrl),
                dataSourceFactory,
                extractorsFactory,
                null,
                null);

        player = ExoPlayerFactory.newSimpleInstance(this,trackSelector);

        player.addListener(this);
        player.prepare(mediaSource);

        Intent intent = getIntent();

        courseId = Integer.parseInt(intent.getStringExtra("courseId"));
        lessonId = Integer.parseInt(intent.getStringExtra("lessonId"));
        orderNumber = intent.getIntExtra("orderNumber",0);

        init(courseId, lessonId, orderNumber);

        elements[0] = binding.imageviewStethosceopeScenarioactivity;
        elements[1] = binding.imageviewThermometerScenarioactivity;
        elements[2] = binding.imageviewSphygmomanometerScenarioactivity;
        elements[3] = binding.imageviewPulseOximeterScenarioactivity;
        elements[4] = binding.imageviewStopwatchScenarioactivity;

        for(int element = 0  ; element< 5 ; element++){

            final int current_element = element;

            elements[element].setOnTouchListener(new View.OnTouchListener() {

                float dX;
                float dY;
                int lastAction;

                @Override
                public boolean onTouch(View view, MotionEvent event)
                {

                    switch (event.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                            dX = view.getX() - event.getRawX();
                            dY = view.getY() - event.getRawY();
                            lastAction = MotionEvent.ACTION_DOWN;
                            break;

                        case MotionEvent.ACTION_MOVE:
                            DisplayMetrics metrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(metrics);

                            view.setY(event.getRawY() + dY);
                            view.setX(event.getRawX() + dX);

                            lastAction = MotionEvent.ACTION_MOVE;
                            break;

                        case MotionEvent.ACTION_UP:
                            boolean success = false;

                                if (view.getX() + view.getWidth() / 2 <= binding.imageviewPatientPhotoScenarioactivity.getX()+binding.imageviewPatientPhotoScenarioactivity.getWidth()
                                        && binding.imageviewPatientPhotoScenarioactivity.getX() <= view.getX() + view.getWidth() / 2
                                        && view.getY() + view.getHeight() / 2 <= binding.imageviewPatientPhotoScenarioactivity.getY()+ binding.imageviewPatientPhotoScenarioactivity.getHeight()
                                        && binding.imageviewPatientPhotoScenarioactivity.getY() <= view.getY() + +view.getHeight() / 2) {
                                    doWhenElementIsOnImage(current_element);
                                    System.out.println("CURRENT X : " + (view.getX() + view.getWidth() / 2) + " Y : " + (view.getY() + view.getHeight() / 2));
                                    success = true;
                                    break;
                                }

                            if (!success){
                                System.out.println("CURRENT X : " + (view.getX() + view.getWidth() / 2) + " Y : " + (view.getY() + view.getHeight() / 2));
                                view.setX(originalPositionOfElements[current_element][0]);
                                view.setY(originalPositionOfElements[current_element][1]);
                            }

                            break;

                        default:
                            return false;
                    }
                    return true;
                }
            });
        }

        binding.textviewHeartbeatPlaybuttonScenarioactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.setPlayWhenReady(true);
            }
        });

        binding.textviewHeartbeatStopbuttonScenarioactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.setPlayWhenReady(false);
            }
        });
    }

    private void init(int courseId, int lessonId, int orderNumber) {
        for(Scene item : LessonsActivity.allScenesList){
            if(item.getCourseId() == courseId && item.getLessonId() == lessonId && item.getOrderNumber() == orderNumber){
                mScene = item;
            }
        }
        System.out.println("CID : "+ courseId);
        System.out.println("LID : "+ lessonId);
        System.out.println("ON : "+ orderNumber);

        nursesQuestions = new TextView[mScene.getNurseQuestionsToPatientArray().length];
        //Making Texts
        for(int i = 0 ; i < mScene.getNurseQuestionsToPatientArray().length ; i++){
            TextView tv = new TextView(this);
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
            tv.setText((mScene.getNurseQuestionsToPatientArray())[i]);

            final int currentItem = i;

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    clearQuestionStyle();

                    ((TextView)v).setTextColor(getResources().getColor(R.color.colorPatientQuestionHighlight));
                    ((TextView)v).setTypeface(null, Typeface.BOLD);

                    binding.linearlayoutPatientAnswerBoxScenarioactivity.setVisibility(View.VISIBLE);

                    binding.textviewPatientAnswerScenarioactivity.setText((mScene.getPatientAnswersArray())[currentItem]);
                }
            });
            binding.linearlayoutNursesQuestionBoxSceneactivity.addView(tv);
            nursesQuestions[i] = tv;
        }

        //binding.textviewQuestion1Scenarioactivity.setText((mScene.getNurseQuestionsToPatientArray())[0]);
        //binding.textviewQuestion2Scenarioactivity.setText((mScene.getNurseQuestionsToPatientArray())[1]);
        //binding.textviewQuestion3Scenarioactivity.setText((mScene.getNurseQuestionsToPatientArray())[2]);

        binding.textviewPatientInitialDialogSceneactivity.setText(mScene.getPatientInitialDialog());

        initInitialDialogProg();
        startPatientInitialDialogTimerThread();
        initSceneProg();
        startSceneTimerThread();//타이머 시작

    }

    private void doWhenElementIsOnImage(int current_element) {
        switch(current_element){
            case STETHOSCOPE:
                elements[THERMOMETER].setX(originalPositionOfElements[THERMOMETER][0]);
                elements[THERMOMETER].setY(originalPositionOfElements[THERMOMETER][1]);
                elements[SPHYGMOMANOMETER].setX(originalPositionOfElements[SPHYGMOMANOMETER][0]);
                elements[SPHYGMOMANOMETER].setY(originalPositionOfElements[SPHYGMOMANOMETER][1]);
                elements[PULSE_OXIMETER].setX(originalPositionOfElements[PULSE_OXIMETER][0]);
                elements[PULSE_OXIMETER].setY(originalPositionOfElements[PULSE_OXIMETER][1]);
                elements[STOPWATCH].setX(originalPositionOfElements[STOPWATCH][0]);
                elements[STOPWATCH].setY(originalPositionOfElements[STOPWATCH][1]);

                binding.textviewDiagnosticInformationScenarioactivity.setVisibility(View.VISIBLE);
                binding.linearlayoutHearbeatplayScenarioactivity.setVisibility(View.GONE);
                binding.textviewDiagnosticInformationScenarioactivity.setText("Heart Rate : " + mScene.getHeartRate());

                break;
            case THERMOMETER:
                elements[STETHOSCOPE].setX(originalPositionOfElements[STETHOSCOPE][0]);
                elements[STETHOSCOPE].setY(originalPositionOfElements[STETHOSCOPE][1]);
                elements[SPHYGMOMANOMETER].setX(originalPositionOfElements[SPHYGMOMANOMETER][0]);
                elements[SPHYGMOMANOMETER].setY(originalPositionOfElements[SPHYGMOMANOMETER][1]);
                elements[PULSE_OXIMETER].setX(originalPositionOfElements[PULSE_OXIMETER][0]);
                elements[PULSE_OXIMETER].setY(originalPositionOfElements[PULSE_OXIMETER][1]);
                elements[STOPWATCH].setX(originalPositionOfElements[STOPWATCH][0]);
                elements[STOPWATCH].setY(originalPositionOfElements[STOPWATCH][1]);

                binding.textviewDiagnosticInformationScenarioactivity.setVisibility(View.VISIBLE);
                binding.linearlayoutHearbeatplayScenarioactivity.setVisibility(View.GONE);
                binding.textviewDiagnosticInformationScenarioactivity.setText("Temperature : " + mScene.getTemperature());

                player.setPlayWhenReady(false);

                break;
            case SPHYGMOMANOMETER:
                elements[STETHOSCOPE].setX(originalPositionOfElements[STETHOSCOPE][0]);
                elements[STETHOSCOPE].setY(originalPositionOfElements[STETHOSCOPE][1]);
                elements[THERMOMETER].setX(originalPositionOfElements[THERMOMETER][0]);
                elements[THERMOMETER].setY(originalPositionOfElements[THERMOMETER][1]);
                elements[PULSE_OXIMETER].setX(originalPositionOfElements[PULSE_OXIMETER][0]);
                elements[PULSE_OXIMETER].setY(originalPositionOfElements[PULSE_OXIMETER][1]);
                elements[STOPWATCH].setX(originalPositionOfElements[STOPWATCH][0]);
                elements[STOPWATCH].setY(originalPositionOfElements[STOPWATCH][1]);

                binding.textviewDiagnosticInformationScenarioactivity.setVisibility(View.VISIBLE);
                binding.linearlayoutHearbeatplayScenarioactivity.setVisibility(View.GONE);

                binding.textviewDiagnosticInformationScenarioactivity.setText(mScene.getBloodPressureDiastolic() + "/" + mScene.getBloodPressureSystolic());

                player.setPlayWhenReady(false);

                break;
            case PULSE_OXIMETER:
                elements[STETHOSCOPE].setX(originalPositionOfElements[STETHOSCOPE][0]);
                elements[STETHOSCOPE].setY(originalPositionOfElements[STETHOSCOPE][1]);
                elements[THERMOMETER].setX(originalPositionOfElements[THERMOMETER][0]);
                elements[THERMOMETER].setY(originalPositionOfElements[THERMOMETER][1]);
                elements[SPHYGMOMANOMETER].setX(originalPositionOfElements[SPHYGMOMANOMETER][0]);
                elements[SPHYGMOMANOMETER].setY(originalPositionOfElements[SPHYGMOMANOMETER][1]);
                elements[STOPWATCH].setX(originalPositionOfElements[STOPWATCH][0]);
                elements[STOPWATCH].setY(originalPositionOfElements[STOPWATCH][1]);

                player.setPlayWhenReady(false);

                binding.textviewDiagnosticInformationScenarioactivity.setVisibility(View.VISIBLE);
                binding.linearlayoutHearbeatplayScenarioactivity.setVisibility(View.GONE);

                binding.textviewDiagnosticInformationScenarioactivity.setText("SpO2 : " + mScene.getSpO2());
                break;
            case STOPWATCH:
                elements[STETHOSCOPE].setX(originalPositionOfElements[STETHOSCOPE][0]);
                elements[STETHOSCOPE].setY(originalPositionOfElements[STETHOSCOPE][1]);
                elements[THERMOMETER].setX(originalPositionOfElements[THERMOMETER][0]);
                elements[THERMOMETER].setY(originalPositionOfElements[THERMOMETER][1]);
                elements[SPHYGMOMANOMETER].setX(originalPositionOfElements[SPHYGMOMANOMETER][0]);
                elements[SPHYGMOMANOMETER].setY(originalPositionOfElements[SPHYGMOMANOMETER][1]);
                elements[PULSE_OXIMETER].setX(originalPositionOfElements[PULSE_OXIMETER][0]);
                elements[PULSE_OXIMETER].setY(originalPositionOfElements[PULSE_OXIMETER][1]);

                player.setPlayWhenReady(false);

                binding.textviewDiagnosticInformationScenarioactivity.setVisibility(View.VISIBLE);
                binding.linearlayoutHearbeatplayScenarioactivity.setVisibility(View.GONE);

                binding.textviewDiagnosticInformationScenarioactivity.setText("Airway Respiratory Rate : " + mScene.getAirwayRespiratoryRate());
        }
    }

    void clearQuestionStyle(){

        for(int i = 0 ; i < mScene.getNurseQuestionsToPatientArray().length ; i++) {
            nursesQuestions[i].setTextColor(getResources().getColor(android.R.color.white));
            nursesQuestions[i].setTypeface(null, Typeface.NORMAL);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        player.setPlayWhenReady(false);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        Log.i("TEST", "onLoadingChanged: " + isLoading + "");
        Log.i("TEST", "Buffered Position: " + player.getBufferedPosition() + "");
        Log.i("TEST", "Buffered Percentage: " + player.getBufferedPercentage() + "");
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if(playbackState == ExoPlayer.STATE_READY){
            Log.i("TEST", "ExoPlayer State is: READY");
        } else if (playbackState == ExoPlayer.STATE_BUFFERING){
            Log.i("TEST", "ExoPlayer State is: BUFFERING");
        } else if (playbackState == ExoPlayer.STATE_ENDED){
            Log.i("TEST", "ExoPlayer State is: ENDED");
        } else if (playbackState == ExoPlayer.STATE_IDLE){
            Log.i("TEST", "ExoPlayer State is: IDLE");
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    public void initSceneProg(){
        //binding.progressbarTimerScenarioactivity.setMax(Integer.parseInt(mScene.getTime()));
        //binding.progressbarTimerScenarioactivity.setProgress(Integer.parseInt(mScene.getTime()));

        binding.progressbarTimerScenarioactivity.setMax(120);
        binding.progressbarTimerScenarioactivity.setProgress(120);
    }

    public void initInitialDialogProg(){
        binding.prograssbarPatientInitlalDialogSceneactivity.setMax(Integer.parseInt(mScene.getTime()));
        binding.prograssbarPatientInitlalDialogSceneactivity.setProgress(Integer.parseInt(mScene.getTime()));
    }

    public void startSceneTimerThread(){
        timerTask = new TimerTask(){ //timerTask는 timer가 일할 내용을 기록하는 객체

            @Override
            public void run() {
                // TODO Auto-generated method stub

                decreaseSceneBar();
            }

        };

        timer= new Timer();
        timer.schedule(timerTask, 0,1000);
    }

    public void startPatientInitialDialogTimerThread(){
        timerTask2 = new TimerTask(){ //timerTask는 timer가 일할 내용을 기록하는 객체

            @Override
            public void run() {
                // TODO Auto-generated method stub

                decreasePatientInitialDialogBar();
            }

        };

        timer2= new Timer();
        timer2.schedule(timerTask2, 0,1000);
    }

    private void decreasePatientInitialDialogBar() {
        runOnUiThread(
                new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        int currprog=binding.prograssbarPatientInitlalDialogSceneactivity.getProgress();

                        if(currprog>0){
                            currprog=currprog-1;
                        }else if(currprog==0){
                            binding.linearlayoutQuestionsListScenarioactivity.setVisibility(View.VISIBLE);
                            binding.linearlayoutPatientAnswerBoxScenarioactivity.setVisibility(View.INVISIBLE);
                            binding.linearlayoutInitialDialogSceneactivity.setVisibility(View.GONE);

                            if(timer2 != null) {
                                timer2.cancel();
                                timer2.purge();
                                timer2 = null;
                            }
                        }
                        binding.prograssbarPatientInitlalDialogSceneactivity.setProgress(currprog);
                    }
                }
        );
    }

    public void decreaseSceneBar(){
        runOnUiThread(
                new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        int currprog=binding.progressbarTimerScenarioactivity.getProgress();

                        if(currprog>0){
                            currprog=currprog-1;
                        }else if(currprog==0){
                            Intent intent = new Intent(SceneActivity.this,QuestionActivity.class);
                            intent.putExtra("courseId",mScene.getCourseId());
                            intent.putExtra("lessonId",mScene.getLessonId());
                            intent.putExtra("sceneId",mScene.getSceneId());
                            intent.putExtra("orderNumber",orderNumber);
                            intent.putExtra("questionOrder",0);

                            overridePendingTransition(0, 0);
                            startActivity(intent);

                            if(timer != null) {
                                timer.cancel();
                                timer.purge();
                                timer = null;
                            }
                        }
                        binding.progressbarTimerScenarioactivity.setProgress(currprog);
                        binding.textviewTimertextScenarioactivity.setText("Time left : " + currprog + "sec");
                    }
                }
        );
    }



}
