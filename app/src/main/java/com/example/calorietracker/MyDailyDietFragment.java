package com.example.calorietracker;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calorietracker.Database.Consumption;
import com.example.calorietracker.Database.Food;
import com.example.calorietracker.Database.RestClient;
import com.example.calorietracker.Database.Users;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyDailyDietFragment extends Fragment {
    View vMyDailyFragment;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
        List<String> foodList = new ArrayList<>();
        for (Food food : foods) {
            if (food.getCategory().equals(categoryList.get(0)))
                foodList.add(food.getFoodname());
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
                List<String> newFoodList = new ArrayList<>();
                if (selectedCategory != null) {
                    newFoodList.clear();
                    for (Food food : finalFoods) {
                        if (food.getCategory().equals(selectedCategory))
                            newFoodList.add(food.getFoodname());
                    }
                }
                ArrayAdapter<String> foodAdapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_item, newFoodList);
                foodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spFood.setAdapter(foodAdapter);
                spFood.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spFood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String food = parent.getItemAtPosition(position).toString();
                SearchAsyncTask searchAsyncTask= new SearchAsyncTask();
                searchAsyncTask.execute(food);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button btnConfirm = vMyDailyFragment.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = spCategory.getSelectedItem().toString();
                String foodname = spFood.getSelectedItem().toString();
                EditText etQuantity = vMyDailyFragment.findViewById(R.id.et_quantity);
                if (etQuantity.getText().toString().isEmpty()) {
                    etQuantity.setError("Quantity cannot be empty");
                    return;
                }
                int amount = Integer.valueOf(etQuantity.getText().toString());
                CreateConsumptionAsyncTask createConsumptionAsyncTask =
                        new CreateConsumptionAsyncTask();
                createConsumptionAsyncTask.execute(category, foodname, "" + amount);

            }
        });

        Button btnAddFood = vMyDailyFragment.findViewById(R.id.btn_add_new_food);
        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame,
                        new AddFoodFragment()).commit();
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
                    Double amount = jsonObject.getDouble("amount");
                    Double calorie = jsonObject.getDouble("calorie");
                    Double fat = jsonObject.getDouble("fat");
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
    private class SearchAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            ArrayList<String> special = new ArrayList<String>(){{
                add("apple");
                add("kiwi");
                add("apples");
                add("kiwis)");
            }};
            String number = "1";
            if (special.contains(params[0].toLowerCase()))
                number = "2";
            return API.search(params[0], new String[]{"num"}, new String[]{number});
        }

        @Override
        protected void onPostExecute(String result) {
            TextView tvDescription = vMyDailyFragment.findViewById(R.id.tv_description);
            tvDescription.setText(API.getSnippet(result));
            DownloadImageTask downloadImageTask = new DownloadImageTask();
            downloadImageTask.execute(API.getImageSrc(result));
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... strings) {
            String urldisplay = strings[0];
            Bitmap bmp = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bmp;
        }
        protected void onPostExecute(Bitmap result) {
            ImageView ivFood = vMyDailyFragment.findViewById(R.id.iv_food);
            ivFood.setImageBitmap(result);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class CreateConsumptionAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            int conid = RestClient.count("consumption") + 1;
            String category = params[0];
            String foodname = params[1];
            int quantity = Integer.valueOf(params[2]);
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            final Bundle bundle = getActivity().getIntent(). getExtras();
            assert bundle != null;
            Users users = null;
            try {
                JSONObject jsonUser = new JSONObject(bundle.getString("jsonUsers"));
                Date dob = sdf.parse(jsonUser.getString("dob"));
                users = new Users(jsonUser.getInt("userid"),
                        jsonUser.getString("firstname"), jsonUser.getString("surname"),
                        jsonUser.getString("email"), dob, jsonUser.getInt("height"),
                        jsonUser.getInt("weight"), jsonUser.getString("gender"),
                        jsonUser.getString("address"), jsonUser.getInt("postcode"),
                        jsonUser.getInt("activitylv"), jsonUser.getInt("steppermile"));
            } catch (ParseException | JSONException e) {
                e.printStackTrace();
            }
            String result = RestClient.findFoodByFoodnameAndCategory(foodname, category);
            Food food = new Food();
            try {
                JSONObject jsonObject = new JSONObject(result);
                int foodid = jsonObject.getInt("foodid");
                Double calorie = jsonObject.getDouble("calorie");
                String unit = jsonObject.getString("unit");
                Double amount = jsonObject.getDouble("amount");
                Double fat = jsonObject.getDouble("fat");
                food = new Food(foodid, foodname, category, calorie, unit, amount, fat);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Consumption consumption = new Consumption(conid, users,
                    Calendar.getInstance().getTime(), food, quantity);
            RestClient.create("consumption", consumption);
            return "Consumption has been created";
        }

        @Override
        protected void onPostExecute(String response) {
            Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
