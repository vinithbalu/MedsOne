package com.gopher.MedsOne;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.VIBRATE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.ContentProvider;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import eu.livotov.labs.android.camview.ScannerLiveView;
import eu.livotov.labs.android.camview.scanner.decoder.zxing.ZXDecoder;

public class ScanQRcodeActivity extends AppCompatActivity {

    private ScannerLiveView scannerLiveView;
    private TextView scannerTV;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);
        scannerLiveView = findViewById(R.id.cameraLiveView);
        scannerTV = findViewById(R.id.idTVScannedData);
        if (checkpermission()) {
            Toast.makeText(this, "Permissions Granted..", Toast.LENGTH_SHORT).show();
        }else{
            requestpermission();
        }
        scannerLiveView.setScannerViewEventListener(new ScannerLiveView.ScannerViewEventListener() {
            @Override
            public void onScannerStarted(ScannerLiveView scanner) {
                Toast.makeText(ScanQRcodeActivity.this, "Scanner started..", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScannerStopped(ScannerLiveView scanner) {
                Toast.makeText(ScanQRcodeActivity.this, "Scanner stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScannerError(Throwable err) {
                scannerTV.setText("Error");
            }

            @Override
            public void onCodeScanned(String data) {
                System.out.println("Scanned data: "+data);
                scannerTV.setText("Scanned: "+data);
                Intent i = new Intent(ScanQRcodeActivity.this, MedicineDetailsActivity.class);
                i.putExtra("barcode", data);
                startActivity(i);
            }
        });
    }
    private boolean checkpermission(){
        int camer_permission = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int vibrate_permission = ContextCompat.checkSelfPermission(getApplicationContext(),VIBRATE);
        return camer_permission == PackageManager.PERMISSION_GRANTED && vibrate_permission == PackageManager.PERMISSION_GRANTED;
    }
    private void requestpermission(){
        int PERMISSION_CODE = 200;
        ActivityCompat.requestPermissions(this,new String[]{CAMERA,VIBRATE},PERMISSION_CODE);
    }

    @Override
    protected void onPause() {
        scannerLiveView.stopScanner();
        super.onPause();
    }

    @Override
    protected void onResume() {
        ZXDecoder decoder = new ZXDecoder();
        decoder.setScanAreaPercent(0.8);
        scannerLiveView.setDecoder(decoder);
        scannerLiveView.startScanner();
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0){
            boolean cameraccepted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
            boolean vibrationAccepted = grantResults[1]==PackageManager.PERMISSION_GRANTED;
            if(cameraccepted && vibrationAccepted){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Permission Denied \n you cannot use the app without permissions", Toast.LENGTH_SHORT).show();
            }
        }
    }
}