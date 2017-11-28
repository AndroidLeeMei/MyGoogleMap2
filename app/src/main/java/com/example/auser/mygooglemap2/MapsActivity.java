package com.example.auser.mygooglemap2;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.widget.AdapterView.*;

//AppCompatActivity才會有action bar
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
//public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap; //只要取得一次即可
        setmMap(0, 0, "幾內灣");
        // Add a marker in Sydney and move the camera
//        //下面三行移到setmMap,方便重複使用
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    void setmMap(double lat, double lang, String title) {
//        LatLng sydney = new LatLng(-34, 151);//Lat緯度Lng經度
        LatLng sydney = new LatLng(lat, lang);//Lat緯度Lng經度
        mMap.addMarker(new MarkerOptions().position(sydney).title(title));//加上地圖上的圖示.小水滴倒過來的,position:位置,title:標題資訊
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));//看上去像是鏡頭帶過來


    }

    void setPointOfView(float angle, float level) {
        LatLng latlng = mMap.getCameraPosition().target;
        CameraUpdate camUpdate = CameraUpdateFactory.
                newCameraPosition(new CameraPosition.Builder()
                        .target(latlng)
                        .tilt(angle)
                        .zoom(level)//放大的程度,不能超20過,要3D要16,17以上
                        .build());
        mMap.animateCamera(camUpdate);
    }

    @Override//支援動態產生3個spinner
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        int[] ids = {R.id.pointOfViewSpinner, R.id.spotspinner, R.id.mapTypeSpinner};
        int[] arrays = {R.array.point_of_view, R.array.spot, R.array.map_type};

        for (int i = 0; i < ids.length; i++) {
            MenuItem item = menu.findItem(ids[i]);
            Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    this, arrays[i], R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

            spinner.setAdapter(adapter);
            spinner.setGravity(Gravity.RIGHT);
            spinner.setOnItemSelectedListener(itemSelectedListener);
        }
        return true;
    }


    //    @Override//建立在標題列的選單
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.menu, menu);
//
//        MenuItem item = menu.findItem(R.id.spinner);
//        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
//
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.spot, R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
////        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(itemSelectedListener);
//        return super.onCreateOptionsMenu(menu);
//    }
    //OnItemSelectedListener是spinner專用監聽器,與Listview不相同
    AdapterView.OnItemSelectedListener itemSelectedListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (i == 0) {
                return;//此方法提早結束
            }
            String itemName = ((TextView) (view)).getText().toString();
//            Toast.makeText(MapsActivity.this, ((TextView) (view)).getText() + ", " + i, Toast.LENGTH_SHORT).show();
            switch (adapterView.getId()) {
                case R.id.pointOfViewSpinner://視角切換
                    switch (i) {
                        case 1:setPointOfView(0, 16.9f);break;//角度.放大等級(float)
                        case 2:setPointOfView(60, 17);    break;
                    } break;
                case R.id.spotspinner://景點
                    switch (i) {
                        case 1://美國大峽谷
                            setmMap(36.161347, -112.120898, itemName);break;
                        case 2://紐西蘭南島
                            setmMap(-44.854043, 169.900656, itemName);break;
                        case 3://巴黎鐵塔
                            setmMap(48.858370, 2.294932, itemName);break;
                        case 4://中壢火車站//24.953923, 121.225773
                            setmMap(24.953923, 121.225773, itemName);break;
                        case 5://美國自由女神
                            setmMap(40.689258, -74.044275, itemName);
//                        Log.d("map==","40.6892128, -740447512");
                            break;
                        case 6://長江三峽大霸30.822940, 111.003375
                            setmMap(30.8216698, 111.0029461, itemName); break;
                        case 7://內壢高中
                            setmMap(24.980137, 121.256026, itemName); break;
                    }

                    break;
                case R.id.mapTypeSpinner:
                    switch (i) {
                        case 1:mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); break;
                        case 2:
                            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                            break;
                        case 3:
                            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                            break;
                        case 4://
                            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                            break;
                        case 5://
                            mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                            break;
                    }
                    break;

            }
//            switch(i){
//                case 1://美國大峽谷
//                    setmMap(36.161347, -112.120898,itemName);
//                    break;
//                case 2://紐西蘭南島
//                    setmMap(-44.854043, 169.900656,itemName);
//                    break;
//                case 3://巴黎鐵塔
//                    setmMap(48.858370, 2.294932,itemName);
//                    break;
//                case 4://中壢火車站
//                    setmMap(24.953893, 121.225772,itemName);
//                    break;
//            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {


        }
    };
}
