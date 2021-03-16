package ru.konstantin_starikov.samsung.izhhelper;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

public class ViolationTypeSelectionActivity extends AppCompatActivity {

    private ViolationReport violationReport;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        violationReport = (ViolationReport) getIntent().getSerializableExtra(PlaceChoiceActivity.VIOLATION_REPORT);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_violation_type_selection);

        //ActionBar - настройка
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Выберете тип нарушения");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //настройка фрагментов типов науршений
        FragmentManager fragmentManager = getSupportFragmentManager();
        //получение фрагментов
        ViolationTypeFragment fragment = (ViolationTypeFragment) fragmentManager.findFragmentById(R.id.fragment);
        ViolationTypeFragment fragment2 = (ViolationTypeFragment) fragmentManager.findFragmentById(R.id.fragment2);
        ViolationTypeFragment fragment3 = (ViolationTypeFragment) fragmentManager.findFragmentById(R.id.fragment3);
        ViolationTypeFragment fragment4 = (ViolationTypeFragment) fragmentManager.findFragmentById(R.id.fragment4);
        ViolationTypeFragment fragment5 = (ViolationTypeFragment) fragmentManager.findFragmentById(R.id.fragment5);
        //учтановка иконок типов нарушений
        fragment.setViolationTypeIcon(getDrawable(R.drawable.blue_square));
        fragment2.setViolationTypeIcon(getDrawable(R.drawable.blue_square));
        fragment3.setViolationTypeIcon(getDrawable(R.drawable.blue_square));
        fragment4.setViolationTypeIcon(getDrawable(R.drawable.blue_square));
        fragment5.setViolationTypeIcon(getDrawable(R.drawable.blue_square));
        //установка названий типов нарушений
        fragment.setViolationTypeName("Стоянка на газоне");
        fragment2.setViolationTypeName("Стоянка на пешеходном переходе");
        fragment3.setViolationTypeName("Стоянка на тротуаре");
        fragment4.setViolationTypeName("Стоянка в зоне действия знака «Стоянка запрещена»");
        fragment5.setViolationTypeName("Стоянка в зоне действия знака «Остановка запрещена»");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}