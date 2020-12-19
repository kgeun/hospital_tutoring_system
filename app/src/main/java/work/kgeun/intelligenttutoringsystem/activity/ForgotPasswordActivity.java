package work.kgeun.intelligenttutoringsystem.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import work.kgeun.intelligenttutoringsystem.R;
import work.kgeun.intelligenttutoringsystem.databinding.ActivityForgotPasswordBinding;

/**
 * Created by kgeun on 2017. 9. 29..
 */

public class ForgotPasswordActivity extends AppCompatActivity{

    ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
    }
}
