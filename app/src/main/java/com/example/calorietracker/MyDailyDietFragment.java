package com.example.calorietracker;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.calorietracker.Database.Food;
import com.example.calorietracker.Database.RestClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyDailyDietFragment extends Fragment {
    View vMyDailyFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vMyDailyFragment = inflater.inflate(R.layout.fragment_my_daily_diet, container,
                false);
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

        final Spinner spCategory = vMyDailyFragment.findViewById(R.id.sp_category);
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

        final Spinner spFood = vMyDailyFragment.findViewById(R.id.sp_food);
        final List<String> foodList = new ArrayList<>();
        for (Food food : foods) {
            if (food.getCategory().equals(categoryList.get(0)))
                foodList.add(food.getName());
        }
        ArrayAdapter<String> foodAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, foodList);
        foodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFood.setAdapter(foodAdapter);
        spFood.setSelection(0);



        final ArrayList<Food> finalFoods = foods;
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position,
                                       long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                if (selectedCategory != null) {
                    foodList.clear();
                    for (Food food : finalFoods) {
                        if (food.getCategory().equals(selectedCategory))
                            foodList.add(food.getName());
                    }
                }
                spFood.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return vMyDailyFragment;
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
