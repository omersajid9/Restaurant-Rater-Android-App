package com.depauw.restaurantrater;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.depauw.restaurantrater.databinding.ActivityViewReviewsBinding;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ViewReviewsActivity extends AppCompatActivity {

    private ActivityViewReviewsBinding binding;
    private static ArrayList<Review> reviewArray;
    CustomAdapter myAdapter;

    private View.OnClickListener buttonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {

            Intent reviewIntent = new Intent(ViewReviewsActivity.this, AddReviewActivity.class);
            startActivity(reviewIntent);


        }
    };


    private AdapterView.OnItemClickListener adapterItemClickListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
        {
            Review temp = (Review) adapterView.getItemAtPosition(position);
//            System.out.println(adapterView.getItemAtPosition(position));

            AlertDialog.Builder myBuilder = new AlertDialog.Builder(ViewReviewsActivity.this);
            myBuilder.setTitle("Review Details")
                    .setMessage("This review was cerated on " + temp.getReviewDate() + " at " + temp.getReviewTime());
            AlertDialog myDialog = myBuilder.create();
            myDialog.show();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewReviewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        reviewArray = retrieve();
        myAdapter = new CustomAdapter(this, retrieve());
        binding.listviewReviews.setAdapter(myAdapter);
        binding.listviewReviews.setOnItemClickListener(adapterItemClickListener);
        binding.buttonAddReview.setOnClickListener(buttonOnClickListener);


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        myAdapter = new CustomAdapter(this, reviewArray);
        binding.listviewReviews.setAdapter(myAdapter);
    }




    public static void addReview(Review inst)
    {
        reviewArray.add(inst);
    }

    private ArrayList<Review> retrieve()
    {
        ArrayList<Review> returnArray = new ArrayList<Review>();
        File myFile = new File(getFilesDir(), "reviews.csv" );
        try(Scanner myScanner = new Scanner(myFile))
        {
            while(myScanner.hasNext())
            {
                String line = myScanner.nextLine();
                String[] splitArray = line.split(",");
                if(splitArray.length == 6)
                {
                    Log.d("Review", String.valueOf(Boolean.valueOf(splitArray[5])));
                    Review newReview = new Review(splitArray[0], splitArray[1], splitArray[2], splitArray[3], Float.valueOf(splitArray[4]), intToBool(Integer.valueOf(splitArray[5])));
                    returnArray.add(newReview);
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return returnArray;
    }

    private boolean intToBool(int val)
    {
        if(val == 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}