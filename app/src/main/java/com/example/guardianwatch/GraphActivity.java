package com.example.guardianwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GraphActivity extends AppCompatActivity {

    ImageView backArrow;
    TextView graphActivityDate;
    TextView graphActivityDay;
    ImageView ArrowLeft;
    ImageView ArrowRight;
    TextView graphSentence1;
    TextView graphSentence2;
    TextView graphSentence3;
    ImageView heatmap;
    ImageView useKcal;
    ImageView moveDistance;
    ImageView calendar;
    ImageView graphView;

    private String userId;
    private String kidName;
    int flag = 1;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        Intent kidInformation = getIntent();

        userId = kidInformation.getStringExtra("userId");
        kidName = kidInformation.getStringExtra("kidName");
        Log.d("Heatmap", userId);
        Log.d("Heatmap", kidName);

        // 현재 날짜를 가져옴
        Calendar calendarTime = Calendar.getInstance();
        // 년도를 가져옴
        int year = calendarTime.get(Calendar.YEAR);
        // 월을 가져옴
        int month = calendarTime.get(Calendar.MONTH) + 1;
        // 일을 가져옴
        int day = calendarTime.get(Calendar.DAY_OF_MONTH);

        graphActivityDate = findViewById(R.id.graphActivityDate);
        graphActivityDay = findViewById(R.id.graphActivityDay);
        ArrowLeft = findViewById(R.id.ArrowLeft);
        ArrowRight = findViewById(R.id.ArrowRight);
        graphSentence1 = findViewById(R.id.graphSentence1);
        graphSentence2 = findViewById(R.id.graphSentence2);
        graphSentence3 = findViewById(R.id.graphSentence3);
        heatmap = findViewById(R.id.heatmap);
        useKcal = findViewById(R.id.useKcal);
        moveDistance = findViewById(R.id.moveDistance);
        calendar = findViewById(R.id.calendar);
        graphView = findViewById(R.id.graphView);

        graphActivityDate.setText(year + "년 " + month + "월");
        graphActivityDay.setText(day + "");

        //뒤로가기 버튼 누를 시에 로그인 페이지로 이동
        backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 이전 버튼의 리스너를 추가
        ArrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 하루 전날의 날짜를 가져옴
                calendarTime.add(Calendar.DATE, -1);
                int year = calendarTime.get(Calendar.YEAR);
                int month = calendarTime.get(Calendar.MONTH) + 1;
                int day = calendarTime.get(Calendar.DAY_OF_MONTH);

                graphActivityDate.setText(year + "년 " + month + "월");
                graphActivityDay.setText(day + "");
            }
        });

        // 다음 버튼의 리스너를 추가
        ArrowRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 하루 다음날의 날짜를 가져옴
                calendarTime.add(Calendar.DATE, 1);
                int year = calendarTime.get(Calendar.YEAR);
                int month = calendarTime.get(Calendar.MONTH) + 1;
                int day = calendarTime.get(Calendar.DAY_OF_MONTH);

                graphActivityDate.setText(year + "년 " + month + "월");
                graphActivityDay.setText(day + "");
            }
        });

        heatmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                heatmap.setImageResource(R.drawable.rectangle11);
                useKcal.setImageResource(R.drawable.rectangle7);
                moveDistance.setImageResource(R.drawable.rectangle7);


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://210.102.178.157:8000/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                Service service = retrofit.create(Service.class);
                Call<ResponseBody> call = service.getHeatMap(
                        userId, kidName,
                        Integer.toString(year),
                        Integer.toString(day),
                        Integer.toString(month)
                );
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String responseData = response.body().string();

                                Log.d("Heatmap", responseData);
//                                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedImageData, 0, decodedImageData.length);

                            } catch (Exception e) {
                                // 오류 메시지 처리
                                Toast.makeText(GraphActivity.this, "분석 Heatmap을 불러오는 데 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            }

                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(GraphActivity.this, "네트워크 에러: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                graphSentence1.setText("");
                graphSentence2.setText("특정 장소에 오래 머물러 있었습니다.");
                graphSentence3.setText("");
                flag = 1;

            }

        });

        useKcal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                heatmap.setImageResource(R.drawable.rectangle7);
                useKcal.setImageResource(R.drawable.rectangle12);
                moveDistance.setImageResource(R.drawable.rectangle7);
                graphSentence1.setText("오늘 또래 평균보다 400kcal 높습니다.");
                graphSentence2.setText("7일 평균 활동량이 또래 평균보다 높습니다.");
                graphSentence3.setText("1달 평균 활동량이 또래 평균보다 높습니다.");
                flag = 2;
            }
        });

        moveDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                heatmap.setImageResource(R.drawable.rectangle7);
                useKcal.setImageResource(R.drawable.rectangle7);
                moveDistance.setImageResource(R.drawable.rectangle10);
                graphSentence1.setText("오늘 또래 평균보다 800m 낮습니다.");
                graphSentence2.setText("7일 평균 활동량이 또래 평균보다 낮습니다.");
                graphSentence3.setText("1달 평균 활동량이 또래 평균보다 낮습니다.");
                flag = 3;
            }
        });

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 날짜 선택 다이얼로그를 띄운다.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        GraphActivity.this,
                        R.style.DatePickerDialogTheme,

                        (view, year, month, dayOfMonth) -> {
                            int monthT = month + 1;
                            graphActivityDate.setText(year + "년 " + monthT + "월");
                            graphActivityDay.setText(dayOfMonth + "");
                        },
                        calendarTime.get(Calendar.YEAR),
                        calendarTime.get(Calendar.MONTH),
                        calendarTime.get(Calendar.DAY_OF_MONTH)
                );
//                datePickerDialog.getDatePicker().setBackgroundColor(Color.parseColor("#D7E7F1"));
                datePickerDialog.show();


            }
        });

    }
}
