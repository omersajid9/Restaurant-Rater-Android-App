package com.depauw.restaurantrater;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    private Context context;

    public CustomAdapter(Context context, ArrayList<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    private ArrayList<Review> reviews;


    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_review, parent, false);
        }

        Review thisReview = reviews.get(position);

        RatingBar ratingBarIsFavorite = convertView.findViewById(R.id.ratingBar_isFavorite);
        TextView textViewName = convertView.findViewById(R.id.textView_Name);
        RadioGroup radioGroupMeal = convertView.findViewById(R.id.radiogroup_Meal);
//        RadioButton radioButtonBreakfast = convertView.findViewById(R.id.radio_breakfast);
//        RadioButton radioButtonLunch = convertView.findViewById(R.id.radio_lunch);
//        RadioButton radioButtonBreakfast = convertView.findViewById(R.id.radio_breakfast);

        ProgressBar progressBarReview = convertView.findViewById(R.id.progressBar_Review);

        Log.d("OmerO", String.valueOf(thisReview.isFavorite()));
        Log.d("Omer", String.valueOf(boolToInt(thisReview.isFavorite())));

        ratingBarIsFavorite.setRating((float) boolToInt(thisReview.isFavorite()));
        textViewName.setText(thisReview.getName());

        String meal = thisReview.getMeal();

        if (meal.equalsIgnoreCase("Breakfast"))
        {
//            ((RadioButton) radioGroupMeal.getChildAt(0)).setChecked(true);
//            ((RadioButton) convertView.findViewById(R.id.radio_breakfast)).setChecked(true);
//            a.setChecked(true);
            radioGroupMeal.check(R.id.radioButton_Breakfast);
        }
        else if (meal.equalsIgnoreCase("Lunch"))
        {
//            ((RadioButton) radioGroupMeal.findViewById(R.id.radio_breakfast)).setChecked(true);
            radioGroupMeal.check(R.id.radioButton_lunch);

        }
        else if (meal.equalsIgnoreCase("Dinner"))
        {
//            ((RadioButton) radioGroupMeal.findViewById(R.id.radio_breakfast)).setChecked(true);
            radioGroupMeal.check(R.id.radioButton_Dinner);

        }
        System.out.println(radioGroupMeal.getCheckedRadioButtonId());
        progressBarReview.setProgress((int) thisReview.getRating());

        return convertView;
    }

    private static int boolToInt(boolean val)
    {
        return val ? 1 : 0;
    }
}
