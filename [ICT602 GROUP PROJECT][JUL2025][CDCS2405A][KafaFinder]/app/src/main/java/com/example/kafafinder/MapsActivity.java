//package com.example.kafafinder;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.ImageButton;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.core.app.ActivityCompat;
//import androidx.fragment.app.FragmentActivity;
//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.*;
//
//import com.google.android.material.bottomsheet.BottomSheetDialog;
//
//import java.util.*;
//
//public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
//
//    private GoogleMap mMap;
//    private static final int CAMERA_PERMISSION_CODE = 200;
//    private ImageButton btnScanQR;
//    private Spinner areaSpinner;
//
//    private final Map<String, List<School>> schoolData = new HashMap<>();
//
//    private static class School {
//        String name;
//        double lat, lng;
//        String url;
//        String phone;
//
//        School(String name, double lat, double lng, String url, String phone) {
//            this.name = name;
//            this.lat = lat;
//            this.lng = lng;
//            this.url = url;
//            this.phone = phone;
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maps);
//
//        btnScanQR = findViewById(R.id.btnScanQR);
//        areaSpinner = findViewById(R.id.areaSpinner);
//
//        btnScanQR.setOnClickListener(v -> {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
//            } else {
//                startQRScanner();
//            }
//        });
//
//        setupSchoolData();
//        setupSpinner();
//
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        if (mapFragment != null) mapFragment.getMapAsync(this);
//    }
//
//    private void startQRScanner() {
//        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
//
//        try {
//            startActivityForResult(intent, 0);
//        } catch (Exception e) {
//            Toast.makeText(this, "QR scanner not found. Please install ZXing Barcode Scanner.", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        LatLng center = new LatLng(3.07803, 101.48798);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 15f));
//
//        mMap.setOnMarkerClickListener(marker -> {
//            marker.showInfoWindow();
//            return true;
//        });
//
//        mMap.setOnInfoWindowClickListener(marker -> {
//            Object tag = marker.getTag();
//            if (tag instanceof School) {
//                showBottomSheetDialog((School) tag);
//            }
//        });
//    }
//
//    private void showBottomSheetDialog(School school) {
//        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
//        View sheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_school, null);
//
//        TextView txtSchoolName = sheetView.findViewById(R.id.txtSchoolName);
//        txtSchoolName.setText(school.name);
//
//        sheetView.findViewById(R.id.callIcon).setOnClickListener(v -> {
//            Intent callIntent = new Intent(Intent.ACTION_DIAL);
//            callIntent.setData(Uri.parse("tel:" + school.phone));
//            startActivity(callIntent);
//            bottomSheetDialog.dismiss();
//        });
//
//        sheetView.findViewById(R.id.chatIcon).setOnClickListener(v -> {
//            String phoneNumber = school.phone.replaceAll("\\D", "");
//            String url = "https://wa.me/" + phoneNumber;
//            Intent whatsappIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//            startActivity(whatsappIntent);
//            bottomSheetDialog.dismiss();
//        });
//
//        sheetView.findViewById(R.id.mapIcon).setOnClickListener(v -> {
//            Uri gmmIntentUri = Uri.parse("geo:" + school.lat + "," + school.lng + "?q=" + Uri.encode(school.name));
//            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//            mapIntent.setPackage("com.google.android.apps.maps");
//
//            if (mapIntent.resolveActivity(getPackageManager()) != null) {
//                startActivity(mapIntent);
//            } else {
//                Toast.makeText(this, "Google Maps app not installed", Toast.LENGTH_SHORT).show();
//            }
//
//            bottomSheetDialog.dismiss();
//        });
//
//
//        bottomSheetDialog.setContentView(sheetView);
//        bottomSheetDialog.show();
//    }
//
//    private void setupSpinner() {
//        List<String> areas = new ArrayList<>(schoolData.keySet());
//        Collections.sort(areas);
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(
//                this,
//                R.layout.spinner_item,
//                areas
//        );
//        adapter.setDropDownViewResource(R.layout.spinner_item);
//
//        areaSpinner.setAdapter(adapter);
//
//        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String selectedArea = parent.getItemAtPosition(position).toString();
//                List<School> selectedSchools = schoolData.get(selectedArea);
//
//                mMap.clear();
//
//                if (selectedSchools != null && !selectedSchools.isEmpty()) {
//                    for (School school : selectedSchools) {
//                        LatLng location = new LatLng(school.lat, school.lng);
//                        Marker marker = mMap.addMarker(new MarkerOptions()
//                                .position(location)
//                                .title(school.name)
//                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
//                        if (marker != null) marker.setTag(school);
//                    }
//
//                    // Focus camera
//                    LatLng firstLoc = new LatLng(selectedSchools.get(0).lat, selectedSchools.get(0).lng);
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(firstLoc, 15f));
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {}
//        });
//    }
//
//    private void setupSchoolData() {
//        List<School> seksyen7 = new ArrayList<>();
//        seksyen7.add(new School("Hatimurni Seksyen 7 Shah Alam", 3.07679, 101.48858, "https://maps.app.goo.gl/V2QEZuAMCjroGgpw7", "60196664225"));
//        seksyen7.add(new School("Tadika Khalifah Cilik", 3.07803, 101.48798, "https://maps.app.goo.gl/YVXUtaWh5q9Kqjds6", "60199441609"));
//
//        List<School> seksyen3 = new ArrayList<>();
//        seksyen3.add(new School("Religious Primary Schools Section 3", 3.0787, 101.5068, "https://maps.app.goo.gl/irF6jBtxARM7GmJZ7", "60104364565"));
//
//        List<School> seksyen8 = new ArrayList<>();
//        seksyen8.add(new School("Institut Ibnu Majah (INTIM)", 3.09056, 101.51606, "https://maps.app.goo.gl/bSo1JMDrXkNUUFoK8", "60192713839"));
//
//
//        schoolData.put("Seksyen 3", seksyen3);
//        schoolData.put("Seksyen 7", seksyen7);
//        schoolData.put("Seksyen 8", seksyen8);
//    }
//}
package com.example.kafafinder;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.*;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private static final int CAMERA_PERMISSION_CODE = 200;
    private ImageButton btnScanQR;
    private Spinner areaSpinner;

    private final Map<String, List<School>> schoolData = new HashMap<>();

    private static class School {
        String name;
        double lat, lng;
        String url;
        String phone;

        School(String name, double lat, double lng, String url, String phone) {
            this.name = name;
            this.lat = lat;
            this.lng = lng;
            this.url = url;
            this.phone = phone;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        btnScanQR = findViewById(R.id.btnScanQR);
        areaSpinner = findViewById(R.id.areaSpinner);

        btnScanQR.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
            } else {
                startQRScanner();
            }
        });

        setupSchoolData();
        setupSpinner();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) mapFragment.getMapAsync(this);
    }

    private void startQRScanner() {
        Intent intent = new Intent(this, QRScanActivity.class);
        startActivity(intent);
        btnScanQR = findViewById(R.id.btnScanQR);

        btnScanQR.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
            } else {
                startQRScanner();  // Kita akan create method ini di bawah
            }
        });

    }






    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng center = new LatLng(3.07803, 101.48798);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 15f));

        mMap.setOnMarkerClickListener(marker -> {
            marker.showInfoWindow();
            return true;
        });

        mMap.setOnInfoWindowClickListener(marker -> {
            Object tag = marker.getTag();
            if (tag instanceof School) {
                School school = (School) tag;
                showBottomSheetDialog(school);  // ✅ Ini saja!
            }
        });



    }

    private void showBottomSheetDialog(School school) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_school, null);

        TextView txtSchoolName = sheetView.findViewById(R.id.txtSchoolName);
        txtSchoolName.setText(school.name);

        sheetView.findViewById(R.id.callIcon).setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + school.phone));
            startActivity(callIntent);
            bottomSheetDialog.dismiss();
        });

        sheetView.findViewById(R.id.chatIcon).setOnClickListener(v -> {
            String phoneNumber = school.phone.replaceAll("\\D", "");
            String url = "https://wa.me/" + phoneNumber;
            Intent whatsappIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(whatsappIntent);
            bottomSheetDialog.dismiss();
        });

        sheetView.findViewById(R.id.mapIcon).setOnClickListener(v -> {
            Uri gmmIntentUri = Uri.parse("geo:" + school.lat + "," + school.lng + "?q=" + Uri.encode(school.name));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                Toast.makeText(this, "Google Maps app not installed", Toast.LENGTH_SHORT).show();
            }

            bottomSheetDialog.dismiss();
        });


        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }

    private void setupSpinner() {
        List<String> areas = new ArrayList<>(schoolData.keySet());
        Collections.sort(areas);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,
                areas
        );
        adapter.setDropDownViewResource(R.layout.spinner_item);

        areaSpinner.setAdapter(adapter);

        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedArea = parent.getItemAtPosition(position).toString();
                List<School> selectedSchools = schoolData.get(selectedArea);

                mMap.clear();

                if (selectedSchools != null && !selectedSchools.isEmpty()) {
                    for (School school : selectedSchools) {
                        LatLng location = new LatLng(school.lat, school.lng);
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(location)
                                .title(school.name)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                        if (marker != null) marker.setTag(school);
                    }

                    // Focus camera
                    LatLng firstLoc = new LatLng(selectedSchools.get(0).lat, selectedSchools.get(0).lng);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(firstLoc, 15f));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupSchoolData() {
        List<School> seksyen7 = new ArrayList<>();
        seksyen7.add(new School("Hatimurni Seksyen 7 Shah Alam", 3.07679, 101.48858, "https://maps.app.goo.gl/V2QEZuAMCjroGgpw7", "60196664225"));
        seksyen7.add(new School("Tadika Khalifah Cilik", 3.07803, 101.48798, "https://maps.app.goo.gl/YVXUtaWh5q9Kqjds6", "60199441609"));

        List<School> seksyen3 = new ArrayList<>();
        seksyen3.add(new School("Religious Primary Schools Section 3", 3.0787, 101.5068, "https://maps.app.goo.gl/irF6jBtxARM7GmJZ7", "60104364565"));

        List<School> seksyen8 = new ArrayList<>();
        seksyen8.add(new School("Institut Ibnu Majah (INTIM)", 3.09056, 101.51606, "https://maps.app.goo.gl/bSo1JMDrXkNUUFoK8", "60192713839"));


        List<School> seksyen9 = new ArrayList<>();
        seksyen9.add(new School("Pusat pendidikan islam dar attaqwa", 3.0870187907343793, 101.52219471393452, "https://maps.app.goo.gl/bSo1JMDrXkNUUFoK8", "60192713839"));

        List<School> seksyen13 = new ArrayList<>();
        seksyen13.add(new School("KAFA INTEGRASI AL-INSAN S13", 3.0777688110062575, 101.5516650364128, "https://maps.app.goo.gl/L93fDnDiapKV915j6", "0120000013"));

        List<School> seksyen17 = new ArrayList<>();
        seksyen17.add(new School("Sekolah Kafa AL Amin", 3.044420040893293, 101.50175910573239, "https://maps.app.goo.gl/YnbdQWVqeLQ4KzKTA", "0120000017"));

        List<School> seksyen18 = new ArrayList<>();
        seksyen18.add(new School("Kafa Integrasi Nahdhah Al Islam", 3.055049922067538, 101.52167972985218, "https://maps.app.goo.gl/pekSFbTkW798K3qM7", "0120000018"));
        seksyen18.add(new School("KAFA INTEGRASI TARBIATUL AULAD", 3.047764657585382, 101.51901897875999, "https://maps.app.goo.gl/fXkHH86VZou1VsrBA", "0120000018"));

        List<School> seksyen19 = new ArrayList<>();
        seksyen19.add(new School("Kafa Integrasi An-Najahiyah", 3.0538499995505703, 101.52957615244836, "https://maps.app.goo.gl/3waUqxpy4m4VJiRD7", "0120000019"));

        List<School> seksyen25 = new ArrayList<>();
        seksyen25.add(new School("Kafa Intergrasi Al-Falah", 3.031480527422441, 101.52862705233046, "https://maps.app.goo.gl/4BV4rUngc6o5V6LWA", "0120000025"));

        List<School> seksyenU5 = new ArrayList<>();
        seksyenU5.add(new School("Sekolah KAFA Intergrasi Al-Jannah, Mutiara Subang", 3.180265775557265, 101.53733449096964, "https://maps.app.goo.gl/GyT3ZSn8Xqt4ahke6", "0120000005"));

        List<School> seksyenU11 = new ArrayList<>();
        seksyenU11.add(new School("SAR KAFA Integrasi Setia Alam", 3.0970402915760515, 101.45995397505202, "https://maps.app.goo.gl/dvou1wjqhiFhgN4n7", "0120000011"));
        seksyenU11.add(new School("KAFA Integrasi Bukit Bandaraya", 3.0970138644595275, 101.48633815233045, "https://maps.app.goo.gl/fbzmtxfsjzVKeBaP8", "0120000011"));

        // Masukkan ke dalam map
        schoolData.put("Seksyen 3", seksyen3);
        schoolData.put("Seksyen 7", seksyen7);
        schoolData.put("Seksyen 8", seksyen8);
        schoolData.put("Seksyen 9", seksyen9);
        schoolData.put("Seksyen 13", seksyen13);
        schoolData.put("Seksyen 17", seksyen17);
        schoolData.put("Seksyen 18", seksyen18);
        schoolData.put("Seksyen 19", seksyen19);
        schoolData.put("Seksyen 25", seksyen25);
        schoolData.put("Seksyen U15", seksyenU5);
        schoolData.put("Seksyen U11", seksyenU11);
    }
}

