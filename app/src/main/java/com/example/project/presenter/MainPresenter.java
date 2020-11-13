package com.example.project.presenter;

import android.content.Context;

import com.github.tlaabs.timetableview.Schedule;

import java.util.ArrayList;
import java.util.Calendar;

import com.example.project.contract.MainContract;
import com.example.project.model.PrefManager;

import static java.util.Calendar.DAY_OF_WEEK;

public class MainPresenter implements MainContract.UserActions {
    final MainContract.View mainView;
    private PrefManager prefManager;

    public MainPresenter(MainContract.View mainView){
        this.mainView = mainView;
    }

    @Override
    public void setPrefManager(PrefManager prefManager) {
        this.prefManager = prefManager;
    }

    @Override
    public void addMenuClick() {
        mainView.startEditActivityForAdd();
    }

    @Override
    public void selectSticker(int idx, ArrayList<Schedule> schedules) {
        mainView.startEditActivityForEdit(idx,schedules);
    }

    @Override
    public void prepare() {
        String savedData = prefManager.get((Context)mainView);
        if(savedData == null || savedData.equals("")) return;
        mainView.restoreTimetable(savedData);

        mainView.setDayHighlight(today());
    }

    private int today() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(DAY_OF_WEEK) -1;
        if(day > 0 && day < 6) return day;
        return -1;
    }

    @Override
    public void save(String data) {
        prefManager.save((Context)mainView,data);
    }
}
