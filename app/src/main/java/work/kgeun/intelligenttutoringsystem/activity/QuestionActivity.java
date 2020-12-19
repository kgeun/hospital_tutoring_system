package work.kgeun.intelligenttutoringsystem.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import work.kgeun.intelligenttutoringsystem.R;
import work.kgeun.intelligenttutoringsystem.databinding.ActivityQuestionBinding;
import work.kgeun.intelligenttutoringsystem.items.SGATQuery;

/**
 * Created by kgeun on 2017. 12. 12..
 */

public class QuestionActivity extends AppCompatActivity {

    ActivityQuestionBinding binding;
    int courseId, lessonId, sceneId,questionOrder,orderNumber;
    private SGATQuery mQuery;
    private List<SGATQuery> queryCandidateList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_question);

        courseId = getIntent().getIntExtra("courseId",0);
        lessonId = getIntent().getIntExtra("lessonId",0);
        sceneId = getIntent().getIntExtra("sceneId",0);
        orderNumber = getIntent().getIntExtra("orderNumber",0);
        questionOrder = getIntent().getIntExtra("questionOrder",0);

        init();

        binding.buttonNextQuestionactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionActivity.this, QuestionActivity.class);
                intent.putExtra("courseId",courseId);
                intent.putExtra("lessonId",lessonId);
                intent.putExtra("sceneId",sceneId);
                intent.putExtra("questionOrder",questionOrder + 1);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    void init(){
        queryCandidateList = new ArrayList<>();
        for(SGATQuery item : LessonsActivity.querysList){
            if(sceneId == item.getSceneId()){
                queryCandidateList.add(item);
            }
        }
        if(questionOrder > queryCandidateList.size()) {
            // GO TO NEXT SCENE
            //finish();
            Intent intent = new Intent(QuestionActivity.this,SceneActivity.class);
            intent.putExtra("courseId",courseId);
            intent.putExtra("lessonId",lessonId);
            intent.putExtra("orderNumber",orderNumber + 1);

            startActivity(intent);
        }
        mQuery = queryCandidateList.get(questionOrder);
        binding.textviewQuestionTextQuestionactivity.setText(mQuery.getQuestion());
    }
}
