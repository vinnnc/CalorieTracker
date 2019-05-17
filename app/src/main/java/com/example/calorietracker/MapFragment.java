package com.example.calorietracker;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    GoogleMap mMap;
    View vMap;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vMap = inflater.inflate(R.layout.fragment_map, container, false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment)
                this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return vMap;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng melbourne = new LatLng(-37.814, 144.96332);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(melbourne, 14));

        final Bundle bundle = getActivity().getIntent(). getExtras();
        assert bundle != null;
        String jsonUsers = bundle.getString("jsonUsers");
        String addressStr = "";
        try {
            JSONObject jsonObject = new JSONObject(jsonUsers);
            addressStr = jsonObject.getString("address");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Add a marker in Sydney and move the camera
        Log.d(TAG, "geoLocate: googlecating");

        Geocoder geocoder = new Geocoder(getContext());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(addressStr, 1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);
            Log.e(TAG, "geoLocate: found a location: " + addressStr);
            LatLng location = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(location).title(addressStr));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14));
            NearParksAsyncTask nearParksAsyncTask = new NearParksAsyncTask();
            nearParksAsyncTask.execute(address.getLatitude(), address.getLongitude());
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class NearParksAsyncTask extends AsyncTask<Double, Void, String> {

        @Override
        protected String doInBackground(Double... doubles) {
            return API.nearParks(doubles[0], doubles[1]);
        }

        @Override
        protected void onPostExecute(String result) {
            BitmapDescriptor bitmapDescriptor =
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
            try {
                JSONArray jsonArray = new JSONObject(result).getJSONArray("results");
                if (jsonArray.length() == 0) {
                    Toast.makeText(getContext(), "There is no park within 5km.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonPark = jsonArray.getJSONObject(i);
                    String name = jsonPark.getString("name");
                    JSONObject jsonLocation = jsonPark.getJSONObject("geometry")
                            .getJSONObject("location");
                    double latitude = jsonLocation.getDouble("lat");
                    double longitude = jsonLocation.getDouble("lng");
                    LatLng location = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions().position(location).title(name)
                            .icon(bitmapDescriptor));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
