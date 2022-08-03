package com.depauw.restaurantrater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.depauw.restaurantrater.databinding.ActivityAddReviewBinding;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class AddReviewActivity extends AppCompatActivity {

    private ActivityAddReviewBinding binding;

    AlertDialog.Builder myBuilder;

    private DatePickerDialog.OnDateSetListener datepickerOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int date) {
            binding.edittextReviewDate.setText(month+1 + "/" + date + "/" + year);
        }
    };

    private View.OnClickListener datePickerOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            Calendar myCalender = Calendar.getInstance();
            DatePickerDialog myPicker = new DatePickerDialog(AddReviewActivity.this, datepickerOnDateSetListener,
                    myCalender.get(Calendar.YEAR),
                    myCalender.get(Calendar.MONTH),
                    myCalender.get(Calendar.DATE));
            myPicker.show();
        }


    };

    private TimePickerDialog.OnTimeSetListener timepickerontimesetlistener = new TimePickerDialog.OnTimeSetListener()
    {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute)
        {
            if(hour>12)
            {
                binding.edittextReviewTime.setText(String.valueOf(hour%12) + ":" + minute + " PM");
            }
            else
            {
                binding.edittextReviewTime.setText(hour + ":" + minute+ " AM");
            }
        }
    };

    private View.OnClickListener timePickerOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Calendar myCalender = Calendar.getInstance();
            TimePickerDialog myPicker = new TimePickerDialog(AddReviewActivity.this,
                    timepickerontimesetlistener,
                    myCalender.get(Calendar.HOUR_OF_DAY),
                    myCalender.get(Calendar.MINUTE),
                    false);
            myPicker.show();
        }
    };




    private View.OnClickListener reviewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {



            if(!lengthEqual(binding.edittextRestaurantName, 0))
            {
                if(!lengthEqual(binding.edittextReviewDate, 0))
                {
                    if(!lengthEqual(binding.edittextReviewTime, 0))
                    {
                        Review curReview = collectData();
                        File myfile = new File(getFilesDir(), "reviews.csv");
                        try(FileWriter myWriter = new FileWriter(myfile, true))
                        {
                            String reviewString = reviewToString(curReview);
                            myWriter.write(reviewString + System.lineSeparator());
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        ViewReviewsActivity.addReview(curReview);
                        finish();
                    }
                    else
                    {
                        AlertDialog myDialog = createAlertBuilder("Enter correct time", "Enter date in the format HH:MM AM/PM");
                        myDialog.show();
                    }
                }
                else
                {
                    AlertDialog myDialog = createAlertBuilder("Enter correct date", "Enter date in the format MM/DD/YYYY");
                    myDialog.show();
                }

            }
            else
            {
                AlertDialog myDialog = createAlertBuilder("Restaurant name not found", "Please enter Restaurants name");
                myDialog.show();
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityAddReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.edittextReviewDate.setOnClickListener(datePickerOnClickListener);
        binding.edittextReviewTime.setOnClickListener(timePickerOnClickListener);
        binding.buttonAddReview.setOnClickListener(reviewOnClickListener);


    }

    private Review collectData()
    {

        String name = binding.edittextRestaurantName.getText().toString();
        String date = binding.edittextReviewDate.getText().toString();
        String time = binding.edittextReviewTime.getText().toString();
        String meal = "";

        System.out.println(binding.radiogroupMeals.getCheckedRadioButtonId());
        System.out.println(R.id.radioButton_Breakfast);
        System.out.println(R.id.radioButton_lunch);
        System.out.println(R.id.radioButton_Dinner);
        if(binding.radiogroupMeals.getCheckedRadioButtonId() == R.id.radioButton_breakfast)
        {
            meal = "Breakfast";
            System.out.println("Checked");
        }
        if(binding.radiogroupMeals.getCheckedRadioButtonId() == R.id.radioButton_lunch)
            meal = "Lunch";
        if(binding.radiogroupMeals.getCheckedRadioButtonId() == R.id.radioButton_dinner)
            meal = "Dinner";
        float rating = binding.seekbarRating.getProgress();
        boolean isFavorite = binding.checkboxFavorite.isChecked();

        Review newReview = new Review(name, date, time, meal, rating, isFavorite);
        return newReview;
    }






    private AlertDialog createAlertBuilder(String title, String message)
    {
        myBuilder = new AlertDialog.Builder(AddReviewActivity.this);
        myBuilder.setTitle(title)
                .setMessage(message);
        return myBuilder.create();
    };

    private boolean lengthEqual(EditText text, int length)
    {
        return text.getText().toString().length() == length;
    }

    private String reviewToString(Review curReview)
    {
        return curReview.getName() + "," +
                curReview.getReviewDate() + "," +
                curReview.getReviewTime() + "," +
                curReview.getMeal() + "," +
                curReview.getRating() + "," +
                boolToInt(curReview.isFavorite());

    }

    private int boolToInt (boolean val)
    {
        return val ? 1 : 0;
    }
}