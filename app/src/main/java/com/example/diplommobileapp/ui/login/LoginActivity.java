package com.example.diplommobileapp.ui.login;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.diplommobileapp.MyApplication;
import com.example.diplommobileapp.R;
import com.example.diplommobileapp.data.models.auth.AuthModel;
import com.example.diplommobileapp.data.models.auth.AuthResponseModel;
import com.example.diplommobileapp.data.models.auth.UserStamp;
import com.example.diplommobileapp.services.httpwork.IApi;
import com.example.diplommobileapp.services.httpwork.RetrofitFactory;
import com.example.diplommobileapp.ui.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.time.LocalTime;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText usernameEditText;
    Button loginButton ;
    ProgressBar loadingProgressBar;
    LocalTime timeToReSend;
    AuthModel authModel;
    TextView timerTv;
    TextView resendTv;
    LinearLayout holder;
    private static final int REQUEST_CODE_PERMISSION_NOTIFICATION = 123;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        loginButton = findViewById(R.id.loginButton);
        loadingProgressBar = findViewById(R.id.loading);
        authModel= new AuthModel();
        loginButton.setOnClickListener(LoginActivity.this::readyButtonOnClick);
        holder = findViewById(R.id.itemsHolder);
        loadingProgressBar.setVisibility(View.GONE);

        showLoading();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_PERMISSION_NOTIFICATION
            );
        }

        RetrofitFactory.CreateIApiInstance(this);
        checkAuthorize();
    }
    private void checkAuthorize(){
        IApi retrofit = RetrofitFactory.getApiService();
        retrofit.CheckAuthorize().enqueue(new Callback<UserStamp>() {
            @Override
            public void onResponse(@NonNull Call<UserStamp> call, @NonNull Response<UserStamp> response) {

                if (response.isSuccessful()){
                    assert response.body() != null;
                    MyApplication.userStamp = response.body().getStamp();
                    goToMain();
                }else{
                    endLoading();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserStamp> call, @NonNull Throwable t) {
                endLoading();
                showFail();
            }
        });
    }
    private void goToMain(){
        new Thread(()-> {
            try {
                MyApplication.client.connect();
                MyApplication.client.setUserCode(MyApplication.userStamp);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    String result = task.getResult();
                    IApi retrofit = RetrofitFactory.getApiService();
                    retrofit.AddDeviceKey(result).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {

                        }

                        @Override
                        public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {

                        }
                    });
                }
            }
        });

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void readyButtonOnClick(View view){
        String text = String.valueOf(usernameEditText.getText());

        if (checkPhoneNumber(text)){
            showLoading();
            authModel.setPhone(text);

            IApi retrofit = RetrofitFactory.getApiService();
            retrofit.CheckPhone(authModel).enqueue(new Callback<AuthResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<AuthResponseModel> call, @NonNull Response<AuthResponseModel> response) {
                    endLoading();
                    if (response.isSuccessful()){
                        AuthResponseModel model = response.body();

                        assert model != null;
                        timeToReSend = LocalTime.parse(model.getTimeToNextRequest());
                        setContentView(R.layout.code_enter);
                        loadingProgressBar = findViewById(R.id.loading);
                        usernameEditText = findViewById(R.id.username);
                        loginButton = findViewById(R.id.loginButton);
                        loginButton.setOnClickListener(LoginActivity.this::codeReadyButtonClick);
                        holder = findViewById(R.id.itemsHolder);
                        timerTv = findViewById(R.id.timerTv);
                        resendTv = findViewById(R.id.resendTv);
                        resendTv.setOnClickListener(LoginActivity.this::resendCodeClick);

                        long millis = (long) timeToReSend.getMinute() *60*1000 + timeToReSend.getSecond() * 1000L;
                        startCountdownTimer(millis);
                    }else{
                        Toast toast = new Toast(LoginActivity.this);
                        toast.setText(R.string.login_failed);
                        toast.show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AuthResponseModel> call, @NonNull Throwable t) {
                    endLoading();
                    showFail();
                }
            });
        }else{
            editTextError(usernameEditText,"Введите номер телефона");
        }
    }
    private void codeReadyButtonClick(View view){
        String text = String.valueOf(usernameEditText.getText());
        showLoading();

        if (text.length()==5){
            loadingProgressBar.setVisibility(View.VISIBLE);

            authModel.setCode(text);
            authModel.setDeviceToken("");

            IApi retrofit = RetrofitFactory.getApiService();
            retrofit.LoginByCode(authModel).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    endLoading();
                    if (response.isSuccessful()){
                        checkAuthorize();
                    }else{
                        showFail();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    runOnUiThread(() -> loadingProgressBar.setVisibility(View.GONE));
                    endLoading();
                    Toast toast = new Toast(LoginActivity.this);
                    toast.setText(R.string.login_failed);
                    toast.show();
                }
            });
        }
        else{
            editTextError(usernameEditText,"Введите код");
        }

    }
    private void resendCodeClick(View view){
        timerTv.setVisibility(View.VISIBLE);
        resendTv.setVisibility(View.GONE);

        long millis = (long) timeToReSend.getMinute() *60*1000 + timeToReSend.getSecond() * 1000L;
        startCountdownTimer(millis);

        IApi retrofit = RetrofitFactory.getApiService();
        retrofit.ResendCode(authModel).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {

            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                showFail();
            }
        });
    }
    private void editTextError(EditText editText, String text){

        float translationX = 4f;
        ObjectAnimator shakeAnimator = ObjectAnimator.ofFloat(editText, View.TRANSLATION_X, -translationX, translationX);
        shakeAnimator.setDuration(100);
        shakeAnimator.setRepeatCount(5);
        shakeAnimator.setRepeatMode(ValueAnimator.REVERSE);
        shakeAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                editText.setTranslationX(0f);
            }
        });
        shakeAnimator.start();

        editText.setError(text);
    }
    private void startCountdownTimer(long durationMillis) {
        CountDownTimer countDownTimer = new CountDownTimer(durationMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / (60 * 1000);
                long seconds = (millisUntilFinished % (60 * 1000)) / 1000;
                String secondsString = seconds < 10 ? 0 + String.valueOf(seconds) : String.valueOf(seconds);
                if (millisUntilFinished > 0)
                    timerTv.setText(getString(R.string.time_to_resend) + " " + minutes + ":" + secondsString);
            }

            @Override
            public void onFinish() {
                timerTv.setText(getString(R.string.time_to_resend) + " 00:00");
                timerTv.setVisibility(View.GONE);
                resendTv.setVisibility(View.VISIBLE);
            }
        };
        countDownTimer.start();
    }
    private boolean checkPhoneNumber(String number){
        number = number.replace("(","")
                .replace(")","")
                .replace("+","")
                .replace("-","");
        if (number.length()==11){
            return number.charAt(0) == '7' || number.charAt(0) == '8';
        }
        return false;
    }
    private void showFail(){
        Toast toast = Toast.makeText(LoginActivity.this,R.string.login_failed,Toast.LENGTH_SHORT);
        toast.show();
    }
    private void showLoading(){
        runOnUiThread(() -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            holder.setVisibility(View.GONE);
        });
    }
    private void endLoading(){
        runOnUiThread(() -> {
            loadingProgressBar.setVisibility(View.GONE);
            holder.setVisibility(View.VISIBLE);
        });
    }
}