package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.tlaabs.timetableview.Schedule;

import java.util.ArrayList;

import com.example.project.contract.EditContract;
import com.example.project.presenter.EditPresenter;
import com.example.project.view.TimeBoxView;


public class Edit_time_table extends AppCompatActivity implements EditContract.View {
    public static final int RESULT_OK_ADD = 1;
    public static final int RESULT_OK_EDIT = 2;
    public static final int RESULT_OK_DELETE = 3;

    private EditPresenter editPresenter;

    ArrayList<Schedule> allSchedules = new ArrayList<Schedule>();

    TextView titleView;

    EditText classTitle, classPlace, professorName;
    TimeBoxView timeBox;
    Button addTimeBtn;

    LinearLayout backBtn;
    LinearLayout addBtn;
    LinearLayout deleteBtn;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker);
        context = this;

        editPresenter = new EditPresenter(this);

        titleView = findViewById(R.id.title);
        classTitle = findViewById(R.id.class_title);
        classPlace = findViewById(R.id.class_place);
        professorName = findViewById(R.id.professor_name);
        timeBox = findViewById(R.id.time_box);

        addTimeBtn = findViewById(R.id.add_time);
        backBtn = findViewById(R.id.back);
        addBtn = findViewById(R.id.add);
        deleteBtn = findViewById(R.id.delete);

        addTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPresenter.clickAddTimeBtn();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPresenter.submit(allSchedules,timeBox.getSchedules());
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPresenter.clickDeleteBtn();
            }
        });

        init();
    }

    private void init() {
        allSchedules = (ArrayList<Schedule>) getIntent().getSerializableExtra("allSchedules");
        if (isEditMode()) {
            ArrayList<Schedule> itemSchedules = (ArrayList<Schedule>) getIntent().getSerializableExtra("schedules");
            editPresenter.prepare(true, itemSchedules);
        } else {
            editPresenter.prepare(false, null);
        }
    }

    private boolean isEditMode() {
        int mode = getIntent().getIntExtra("mode", -1);
        if (mode == time_table.REQUEST_EDIT) return true;
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void restoreViews(ArrayList<Schedule> schedules) {
        for(Schedule schedule : schedules) {
            classTitle.setText(schedule.getClassTitle());
            classPlace.setText(schedule.getClassPlace());
            professorName.setText(schedule.getProfessorName());
            timeBox.add(schedule);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void createTimeView(Schedule schedule) {
        timeBox.add(schedule);
    }

    private ArrayList<Schedule> addMetaData(ArrayList<Schedule> resultSchedule){
        for(Schedule schedule : resultSchedule){
            schedule.setClassTitle(classTitle.getText().toString());
            schedule.setClassPlace(classPlace.getText().toString());
            schedule.setProfessorName(professorName.getText().toString());
        }
        return resultSchedule;
    }

    @Override
    public void setResult(ArrayList<Schedule> resultSchedule) {
        Intent i = new Intent();
        if(resultSchedule == null){
            i.putExtra("idx",getIntent().getIntExtra("idx",-1));
            setResult(RESULT_OK_DELETE,i);
            finish();
            return;
        }
        resultSchedule = addMetaData(resultSchedule);
        i.putExtra("schedules", resultSchedule);
        if(isEditMode()){
            i.putExtra("idx",getIntent().getIntExtra("idx",-1));
            setResult(RESULT_OK_EDIT, i);
        }
        else{
            setResult(RESULT_OK_ADD, i);
        }
        finish();
    }

    @Override
    public void hideDeleteBtn() {
        deleteBtn.setVisibility(View.GONE);
    }

    @Override
    public void showDeleteBtn() {
        deleteBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void setActivityTitle(String title) {
        titleView.setText(title);
    }

    @Override
    public void showToastMessage(String msg) {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }


}
