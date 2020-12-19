package work.kgeun.intelligenttutoringsystem.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import work.kgeun.intelligenttutoringsystem.R;
import work.kgeun.intelligenttutoringsystem.databinding.ActivityDragTestBinding;

/**
 * Created by kgeun on 2017. 10. 2..
 */

public class DragTestActivity extends AppCompatActivity {

    ActivityDragTestBinding binding;

    int[][] list = new int[5][2];
    float[][] originalPositionOfElements = new float[5][2];
    int[] originalPositionOfElement1 = new int[2];

    View[] elements = new View[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_drag_test);

        binding.linearlayoutCanvasDragtestactivity.addView(new MyView(DragTestActivity.this));

        elements[0] = binding.viewElement1Dragtestactivity;
        elements[1] = binding.viewElement2Dragtestactivity;
        elements[2] = binding.viewElement3Dragtestactivity;
        elements[3] = binding.viewElement4Dragtestactivity;
        elements[4] = binding.viewElement5Dragtestactivity;

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

                            for (int i = 0; i < 5; i++) {
                                if (view.getX() + view.getWidth() / 2 <= list[i][0] + 40 && list[i][0] - 40 <= view.getX() + view.getWidth() / 2
                                        && view.getY() + view.getHeight() / 2 <= list[i][1] + 40 && list[i][1] - 40 <= view.getY() + +view.getHeight() / 2) {
                                    Toast.makeText(getApplicationContext(), "SUCCESS", Toast.LENGTH_SHORT).show();
                                    System.out.println("LIST X : " + list[i][0] + " Y : " + list[i][1]);
                                    System.out.println("CURRENT X : " + (view.getX() + view.getWidth() / 2) + " Y : " + (view.getY() + view.getHeight() / 2));
                                    success = true;
                                    break;
                                }
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


        binding.buttonSubmitDragtestactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean wrong = false;

                for (int i = 0; i < 5; i++) {
                    if (elements[i].getX() + elements[i].getWidth() / 2 <= list[i][0] + 40 && list[i][0] - 40 <= elements[i].getX() + elements[i].getWidth() / 2
                            && elements[i].getY() + elements[i].getHeight() / 2 <= list[i][1] + 40 && list[i][1] - 40 <= elements[i].getY() + +elements[i].getHeight() / 2) {
                    }
                    else {
                        wrong = true;
                        break;
                    }
                }

                if(wrong)
                {
                    Toast.makeText(getApplicationContext(),"WRONG",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"RIGHT",Toast.LENGTH_SHORT).show();
                }

            }
        });

        binding.buttonNextDragtestactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DragTestActivity.this,DragTest2Activity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        //binding.viewElement1Dragtestactivity.getLocationOnScreen(originalPositionOfElement1);

        binding.viewElement1Dragtestactivity.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.viewElement1Dragtestactivity.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                originalPositionOfElements[0][0] = binding.viewElement1Dragtestactivity.getX();
                originalPositionOfElements[0][1] = binding.viewElement1Dragtestactivity.getY();
                originalPositionOfElements[1][0] = binding.viewElement2Dragtestactivity.getX();
                originalPositionOfElements[1][1] = binding.viewElement2Dragtestactivity.getY();
                originalPositionOfElements[2][0] = binding.viewElement3Dragtestactivity.getX();
                originalPositionOfElements[2][1] = binding.viewElement3Dragtestactivity.getY();
                originalPositionOfElements[3][0] = binding.viewElement4Dragtestactivity.getX();
                originalPositionOfElements[3][1] = binding.viewElement4Dragtestactivity.getY();
                originalPositionOfElements[4][0] = binding.viewElement5Dragtestactivity.getX();
                originalPositionOfElements[4][1] = binding.viewElement5Dragtestactivity.getY();

            }
        });

        super.onResume();
    }

    class MyView extends View {
        public MyView(Context context) {
            super(context); // 부모의 인자값이 있는 생성자를 호출한다
        }

        @Override
        protected void onDraw(Canvas canvas) { // 화면을 그려주는 작업
            Paint paint = new Paint();
            paint.setColor(Color.RED);

            for(int i = 0 ; i < 5 ; i++) {
                list[i][0] = i * 200 + 100;
                list[i][1] = i * 200 + 100;
            }

            for(int i = 0 ; i < 5 ; i++){
                canvas.drawCircle(list[i][0], list[i][1], 20, paint);
            }
        }
    }


}
