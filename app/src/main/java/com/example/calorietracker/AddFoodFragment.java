package com.example.calorietracker;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.calorietracker.Database.Food;
import com.example.calorietracker.Database.RestClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AddFoodFragment extends Fragment {
    View vAddFood;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vAddFood = inflater.inflate(R.layout.fragment_add_food, container, false);

        FindFoodAsyncTask findFoodAsyncTask = new FindFoodAsyncTask();
        findFoodAsyncTask.execute();
        ArrayList<Food> foods = null;
        try {
            foods = findFoodAsyncTask.get();
        } catch (ExecutionException | InterruptedException e) {
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

        Button btnAddFood = vAddFood.findViewById(R.id.btn_add_food);
        final ArrayList<Food> finalFoods = foods;
        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etFoodName = vAddFood.findViewById(R.id.et_food_name);
                final String foodName = etFoodName.getText().toString().toLowerCase();
                if (foodName.isEmpty()) {
                    etFoodName.setError("Food cannot be empty");
                    return;
                }

                ArrayList<String> names = new ArrayList<>();
                    names.add(foodName);
                    names.add(foodName + "s");
                    names.add(foodName + "es");
                    names.add(foodName.substring(0,foodName.length()-1) + "ies");
                for (Food food : finalFoods){
                    if (names.contains(food.getFoodname())){
                        etFoodName.setError("Food is already exist.");
                        return;
                    }
                }
                AddFoodAsyncTask addFoodAsyncTask = new AddFoodAsyncTask();
                addFoodAsyncTask.execute(foodName);
            }
        });

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
                    double amount = jsonObject.getDouble("amount");
                    double calorie = jsonObject.getDouble("calorie");
                    double fat = jsonObject.getDouble("fat");
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

    @SuppressLint("StaticFieldLeak")
    private class AddFoodAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String jsonFood = API.newFood(strings[0]);
            Spinner spCategory = vAddFood.findViewById(R.id.sp_category);
            String category = spCategory.getSelectedItem().toString();
            int foodid = RestClient.count("food") + 1;
            try {
                JSONObject jsonObject = new JSONObject(jsonFood);
                jsonObject = jsonObject.getJSONArray("parsed").getJSONObject(0);
                jsonObject = jsonObject.getJSONObject("food");

                jsonObject = jsonObject.getJSONObject("nutrients");
                double calorie = jsonObject.getDouble("ENERC_KCAL");
                double fat = jsonObject.getDouble("FAT");
                Food food = new Food(foodid, strings[0], category, calorie, "gram",
                        100.0, fat);
                RestClient.create("food", food);
            } catch (JSONException e) {
                e.printStackTrace();
                return "ERROR: There is not food named that, please try another one.";
            }
            return "Add food Successfully.";
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_SHORT)
                    .show();
            if (result.equals("Add food Successfully.")) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame,
                        new MyDailyDietFragment()).commit();
            }
        }
    }
}
