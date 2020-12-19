package work.kgeun.intelligenttutoringsystem.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import work.kgeun.intelligenttutoringsystem.R;
import work.kgeun.intelligenttutoringsystem.databinding.ActivitySignupBinding;

/**
 * Created by kgeun on 2017. 9. 29..
 */

public class SignUpActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
    }
}
