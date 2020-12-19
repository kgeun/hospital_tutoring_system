package work.kgeun.intelligenttutoringsystem.activity;

import android.databinding.DataBindingUtil;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import work.kgeun.intelligenttutoringsystem.R;
import work.kgeun.intelligenttutoringsystem.databinding.ActivityMainBinding;
import work.kgeun.intelligenttutoringsystem.fragment.CoursesListFragment;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    private final int HOME_FRAGMENT = 1;
    private final int COURSE_FRAGMENT = 2;
    private final int USER_FRAGMENT = 3;

    public static int currentCourseID;
    public static int currentLessonID;
    public static int currentSceneID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_home) {
                    callFragment(HOME_FRAGMENT);
                } else if (tabId == R.id.tab_courses) {
                    callFragment(COURSE_FRAGMENT);
                } else if (tabId == R.id.tab_user) {
                    callFragment(USER_FRAGMENT);
                }
            }
        });
    }


    private void callFragment(int frament_no) {

        // 프래그먼트 사용을 위해
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (frament_no) {
            case 1:
                // '프래그먼트1' 호출
                CoursesListFragment coursesListFragment = new CoursesListFragment();
                transaction.replace(R.id.contentContainer, coursesListFragment);
                transaction.commit();
                break;

            case 2:
                // '프래그먼트2' 호출
                CoursesListFragment coursesListFragment2 = new CoursesListFragment();
                transaction.replace(R.id.contentContainer, coursesListFragment2);
                transaction.commit();
                break;

            case 3:
                // '프래그먼트3' 호출
                CoursesListFragment coursesListFragment3 = new CoursesListFragment();
                transaction.replace(R.id.contentContainer, coursesListFragment3);
                transaction.commit();
                break;
        }
    }


}