package com.softmastersgroup.umo.umoagent;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.softmastersgroup.umo.umoagent.models.IDCardModel;
import com.softmastersgroup.umo.umoagent.models.RegisterBundle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import nouri.in.goodprefslib.GoodPrefs;

public class AddressActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    RelativeLayout rlAddress;
    TextInputLayout tiEmployer;
    TextInputEditText etUnitNo, etAddress,
            etEmployer, etHouseNumber;
    private FusedLocationProviderClient locationProviderClient;
    private LocationRequest mLocationRequest;
    private LocationCallback callback;
    private Location mLocation;
    private Context ctx;
    private int pos;
    private String[] addresses;
    Button btnNext, btnPrev;
    TextView tvText, tvLatLng,tvCountryCity;
    com.softmastersgroup.umo.umoagent.models.Location location;
    Marker marker;
    int locationCount = 0;
    RelativeLayout relDoc;
    IDCardModel proof_of_address;
    public static ImageView ivDoc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        ctx = AddressActivity.this;

        init();

        createLocationRequest();

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        //mLocation = LocationServices.getFusedLocationProviderClient(ctx).getLastLocation().getResult();

        //Toasty.success(ctx,mLocation.toString()).show();

        task.addOnSuccessListener(AddressActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

                locationSettingsResponse.getLocationSettingsStates();
                createLocationRequest();
            }
        });

        callback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {

                    //Toasty.success(getApplicationContext(), "Location still null").show();

                    return;

                }

                mLocation = locationResult.getLastLocation();

                locationCount++;

                //Toasty.success(getApplicationContext(), "Location Set").show();

                Address addr = getAddress(mLocation);


                if (locationCount <= 1) {
                    LatLng ll = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());

                    if (marker != null) {
                        marker.setPosition(ll);
                    } else {
                        marker = mMap.addMarker(new MarkerOptions().position(ll));
                    }

                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(ll));
                    mMap.setMinZoomPreference(18.0f);
                    mMap.getUiSettings().setScrollGesturesEnabled(true);

                    etUnitNo.setText(addr == null ? "" : addr.getFeatureName());
                    etAddress.setText(addr == null ? "" : addr.getAddressLine(0));

                }

                tvLatLng.setText(
                        String.valueOf(mLocation.getLatitude())
                                + " , " + String.valueOf(mLocation.getLongitude())
                                +" "+(addr == null ? "" : addr.getLocality())
                                +" - "+(addr == null ? "" : addr.getCountryName())
                );

                //tvCountryCity.setText(addr == null ? "" : addr.getLocality() + " , " + addr.getCountryName());

            }
        };

        RegisterBundle bundle = GoodPrefs.getInstance().getObject("register_bundle", RegisterBundle.class);

        if (bundle.getLocation() != null && bundle.getLocation().size() > 0) {
            bindData(bundle.getLocation().get(0));
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

    }

    private void init() {
        rlAddress = findViewById(R.id.rlAddress);
        //rlAddress.setVisibility(View.VISIBLE);
        relDoc = findViewById(R.id.relSupDoc);
        //relDoc.setVisibility(View.GONE);

        //etBuildingNo = findViewById(R.id.etBuildingNo);
        etUnitNo = findViewById(R.id.etUnitNo);
        etAddress = findViewById(R.id.etAddress);


        tvCountryCity = findViewById(R.id.tvTownRegion);

        etEmployer = findViewById(R.id.etEmployer);
        tiEmployer = findViewById(R.id.tIEmployerName);
        etHouseNumber = findViewById(R.id.etHouseNumber);

        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);
        tvText = findViewById(R.id.tvText);

        tvLatLng = findViewById(R.id.tvLatLng);

        ivDoc = findViewById(R.id.ivDoc);

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx, IDProofActivity.class));
                finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocationData();
                startActivity(new Intent(ctx, AuthActivity.class));
            }
        });

        bindAddress();

        findViewById(R.id.ivDoc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((EditText)findViewById(R.id.etDocNumber)).getText().toString().isEmpty()){
                    ((EditText)findViewById(R.id.etDocNumber)).setError("Specified Document account required");
                    return;
                }

                proof_of_address.setId_type( ((Spinner) findViewById(R.id.spDocType)).getSelectedItem().toString() );
                proof_of_address.setId_number( ((EditText)findViewById(R.id.etDocNumber)).getText().toString() );

                GoodPrefs.getInstance().saveObject("proof_of_address",proof_of_address);

                captureAddress();

            }
        });

    }

    public void captureAddress() {

        GoodPrefs.getInstance().saveInt("img_type",3);

        Intent intent;
        if (proof_of_address != null && proof_of_address.isTaken()) {
            intent = new Intent(ctx, PreviewActivity.class);
            intent.putExtra("type", 2);
            intent.putExtra("session", 1);
        } else {

            intent = new Intent(ctx, CameraActivity.class);
        }
        intent.putExtra("id", 2);
        startActivity(intent);

    }

    private void bindAddress(){
        proof_of_address = GoodPrefs.getInstance().getObject("proof_of_address",IDCardModel.class);

        if (proof_of_address == null){
            proof_of_address = new IDCardModel();
        }

        if (proof_of_address.getImage()!=null &&  !proof_of_address.getImage().isEmpty()){
            Glide.with(getApplicationContext()).load(proof_of_address.getImage()).into((ImageView) findViewById(R.id.ivDoc));
        }

        ((EditText) findViewById(R.id.etDocNumber)).setText(proof_of_address.getId_number());

        if (!proof_of_address.getId_type().isEmpty()){

            if (proof_of_address.getId_type().equalsIgnoreCase("Water Bill")){
                ((Spinner) findViewById(R.id.spDocType)).setSelection(0);
            }else if (proof_of_address.getId_type().equalsIgnoreCase("Electricity Bill")){
                ((Spinner) findViewById(R.id.spDocType)).setSelection(1);
            }else{
                ((Spinner) findViewById(R.id.spDocType)).setSelection(2);
            }

        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        locationProviderClient.requestLocationUpdates(mLocationRequest, callback, null /* Looper */);
    }

    private void stopLocationUpdates() {
        if (locationProviderClient != null && callback != null)
            locationProviderClient.removeLocationUpdates(callback);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }


        mMap.setMyLocationEnabled(true);
        mMap.setMinZoomPreference(18.0f);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        if (mLocation != null) {
            LatLng ll = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());

            if (marker == null) {
                marker = mMap.addMarker(new MarkerOptions().position(ll));
            } else {
                marker.setPosition(ll);
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLng(ll));

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    private void setLocationData() {

        List<com.softmastersgroup.umo.umoagent.models.Location> locations = new ArrayList<>();
        RegisterBundle bundle = GoodPrefs.getInstance().getObject("register_bundle", RegisterBundle.class);

        if (location == null) {
            location = new com.softmastersgroup.umo.umoagent.models.Location();
        }

        if (pos != 0 && pos != 3) {

            Address addr = getAddress(mLocation);

            location.setType("Address");
            location.setCountry(addr == null ? "" : addr.getAddressLine(0));
            location.setCity(addr == null ? "" : addr.getLocality());
            // location.setBuildingno(etBuildingNo.getText().toString());
            location.setUnitno(etUnitNo.getText().toString());
            location.setStreetaddress(etAddress.getText().toString());
            location.setRegion(addr == null ? "" : addr.getLocality());
            location.setHousenumber(etHouseNumber.getText().toString());

            location.setLatitude(mLocation.getLatitude());
            location.setLongitude(mLocation.getLongitude());

            locations.add(location);

        }

        bundle.setLocation(locations);

        GoodPrefs.getInstance().saveObject("register_bundle", bundle);

    }

    private void bindData(com.softmastersgroup.umo.umoagent.models.Location location1) {
        if (location1 == null) return;

        etUnitNo.setText(location1.getUnitno());
        //etBuildingNo.setText(location1.getBuildingno());
        tvLatLng.setText(
                String.valueOf(location1.getLatitude())
                        + ", " + String.valueOf(location1.getLongitude())
                +" "+location1.getCity()+" - "+location1.getCountry()
        );
        etAddress.setText(location1.getStreetaddress());

    }

    private Address getAddress(Location location) {
        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(this);

        try {

            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

        } catch (IOException e) {

            Log.d("IOException", "Not found", e);

        } catch (IllegalArgumentException e1) {
            Log.d("IllegalArgumentEx", "Not found", e1);
        }

        if (addresses == null || addresses.get(0) == null) {
            return null;
        }

        return addresses.get(0);
    }


}
