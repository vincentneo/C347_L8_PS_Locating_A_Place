package sg.edu.rp.c347.id19007966.locatingaplace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Marker northMarker, centralMarker, eastMarker;
    GoogleMap map;

    Spinner locationSpinner;
    ArrayList<String> locationNames;

    // singapore centre's coordinates
    LatLng singaporeCoords = new LatLng(1.3521, 103.8198);

    // since addresses provided are invalid,
    // i am using substitute coordinates based on the given image

    // sembawang park -> blk 333 admiralty
    LatLng northCoords = new LatLng(1.460770, 103.835687);

    // mt emily park -> blk 3a orchard
    LatLng centralCoords = new LatLng(1.304549, 103.847205);

    // play@west -> blk 555 tampines
    LatLng eastCoords = new LatLng(1.348285, 103.933529);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationSpinner = findViewById(R.id.locationSpinner);

        locationNames = new ArrayList<>();
        locationNames.add("Select a location");
        locationNames.add("HQ - North");
        locationNames.add("Central");
        locationNames.add("East");

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, locationNames);

        locationSpinner.setAdapter(adapter);
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                switch(index) {
                    case 1:
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(northCoords, 17));
                        break;
                    case 2:
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(centralCoords, 17));
                        break;
                    case 3:
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(eastCoords, 17));
                        break;
                    default:
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(singaporeCoords, 10));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);

        mapFragment.getMapAsync(googleMap -> {
            map = googleMap;
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(singaporeCoords, 10));
            setupMarkers();
            setMapUIElements();
            setupCurrentLocation();
        });
    }

    void setupMarkers() {
        MarkerOptions northMarkerOptions =
                new MarkerOptions()
                        .position(northCoords)
                        .title(locationNames.get(1))
                        .snippet("Block 333, Admiralty Ave 3, 765654\n"
                                + "Operating hours: 10am-5pm\n"
                                + "Tel:65433456")
                        .icon(BitmapDescriptorFactory
                                .fromResource(android.R.drawable.star_big_on));
        northMarker = map.addMarker(northMarkerOptions);

        MarkerOptions centralMarkerOptions =
                new MarkerOptions()
                        .position(centralCoords)
                        .title(locationNames.get(2))
                        .snippet("Block 3A, Orchard Ave 3, 134542\n"
                                + "Operating hours: 11am-8pm\n"
                                + "Tel:67788652")
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_RED));
        centralMarker = map.addMarker(centralMarkerOptions);

        MarkerOptions eastMarkerOptions =
                new MarkerOptions()
                        .position(eastCoords)
                        .title(locationNames.get(3))
                        .snippet("Block 555, Tampines Ave 3, 287788\n"
                                + "Operating hours: 9am-5pm\n"
                                + "Tel:66776677")
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        eastMarker = map.addMarker(eastMarkerOptions);

        map.setOnMarkerClickListener(this::onMarkerClick);
    }

    void setMapUIElements() {
        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
    }

    void setupCurrentLocation() {
        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck == PermissionChecker.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        }
        else {
            Log.e("GMaps - Permission", "GPS access has not been granted");
            String[] fineLoc = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(MainActivity.this, fineLoc, 0);
        }
    }

    boolean onMarkerClick(final Marker marker) {
        String title = marker.getTitle();
        Toast.makeText(MainActivity.this, title, Toast.LENGTH_SHORT).show();
        return false; // returning true will fully override default behaviour.
    }
}