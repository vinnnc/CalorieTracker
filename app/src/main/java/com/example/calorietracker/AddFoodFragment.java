package com.example.calorietracker;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.calorietracker.Database.Food;
import com.example.calorietracker.Database.RestClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AddFoodFragment extends Fragment {
    View vAddFood;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

    FindFoodAsyncTask findFoodAsyncTask = new FindFoodAsyncTask();
        findFoodAsyncTask.execute();
        ArrayList<Food> foods = null;
        try {
            foods = findFoodAsyncTask.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final Spinner spCategory = vAddFood.findViewById(R.id.sp_category);
        final List<String> categoryList = new ArrayList<>();
        for (Food food : foods) {
            if (!categoryList.contains(food.getCategory()))
                categoryList.add(food.getCategory());
        }
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, categoryList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(categoryAdapter);
        spCategory.setSelection(0);

        return vAddFood;
    }

    @SuppressLint("StaticFieldLeak")
    private class FindFoodAsyncTask extends AsyncTask<Void, Void, ArrayList<Food>> {

        @Override
        protected ArrayList<Food> doInBackground(Void...params) {
            ArrayList<Food> foods = new ArrayList<>();
            String jsonFood = RestClient.findAllFood();
            try {
                JSONArray jsonArray = new JSONArray(jsonFood);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int foodId = jsonObject.getInt("foodid");
                    String name = jsonObject.getString("foodname");
                    String category = jsonObject.getString("category");
                    int amount = jsonObject.getInt("amount");
                    int calorie = jsonObject.getInt("calorie");
                    int fat = jsonObject.getInt("fat");
                    String unit = jsonObject.getString("unit");
                    Food food = new Food(foodId, name, category, calorie, unit, amount, fat);
                    foods.add(food);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            return foods;
        }
    }
}
