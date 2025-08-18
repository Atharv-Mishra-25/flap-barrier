package com.dpdtech.application.newApp;


import android.annotation.SuppressLint;
import android.app.Activity;

import android.app.ProgressDialog;
import android.app.smdt.SmdtManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dpdtech.application.BarcodeData;
import com.dpdtech.application.network.QBWebSocketClient;
import com.dpdtech.application.network.retrofit.APIServices;
import com.dpdtech.application.network.retrofit.RetrofitClass;
import com.dpdtech.application.oper.GetData;
import com.dpdtech.application.util.ScanReaderHelper;
import com.tgw.BuildConfig;
import com.tgw.R;
import com.tniuds.sdk.peripherals.UartDev;
import com.dpdtech.application.oper.UartUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ScheduledExecutorService;

/**
 * author : Atharv
 * email : 397935390@qq.com
 * date:06/12/2024INOpenDoor
 */


public class MainActivity extends Activity {
    ProgressDialog progressDialog;
    UartDev uartS1, uartReadAccessControlPanelData;

    private ScheduledExecutorService scheduler;

    private SmdtManager smdt;
    private TextView logContent, text;
    LinearLayout layout, lytdata;
    private final String TAG = "MainActivity";
    APIServices apiinterface;
    //    EditText barcodetext;
    BarcodeData barcodedata;
    GetData getData;
    Handler handler;
    String ip = "";
    private boolean shouldScan = true;
    private Runnable connectionChecker;


    private QBWebSocketClient socketClient;

    private String gateSelected = "gate1";
    long gateTiming;
    Button btn_hide, btn_clear, btnConnect, btnOpenDoor;
    TextView txttotal, txtadult, txtcollege, txtchild, txtcat, txttime;

    private static final long CHECK_INTERVAL = 100L;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        smdt = SmdtManager.create(this);
        apiinterface = RetrofitClass.getClient().create(APIServices.class);
        text = findViewById(R.id.text);
//        barcodetext = findViewById(R.id.barcodetext);
        logContent = findViewById(R.id.logContent);
        lytdata = findViewById(R.id.lytdata);
        layout = findViewById(R.id.layout);
        btn_hide = findViewById(R.id.bt_view);
//        btn_clear = findViewById(R.id.bt_clear);
        txttotal = findViewById(R.id.txttotal);
        btnConnect = findViewById(R.id.btnConnect);
        btnOpenDoor = findViewById(R.id.bt_INOpenDoorTest);

        TextView selectedGateTextView = findViewById(R.id.selectedGateTextView);
        TextView selectedChainIdTextView = findViewById(R.id.selectedChainIdTextView);
        TextView selectedStoreIdTextView = findViewById(R.id.selectedStoreIdTextView);
        TextView selectedRegionIdTextView = findViewById(R.id.selectedRegionIdTextView);

        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String storeId = sharedPreferences.getString("storeId", null);
        String regionId = sharedPreferences.getString("regionID", null);
        String chainId = sharedPreferences.getString("chainId", null);
        String gate = sharedPreferences.getString("gate", null);
        gateTiming = Long.parseLong(sharedPreferences.getString("timing", "2"));


        Button btnSettings = findViewById(R.id.setupBtn);

        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        Log.d("Loggggggg", androidId);

        if (BuildConfig.FLAVOR == "staging"  || BuildConfig.FLAVOR == "prod") {
            openBlue();
        }

        btnSettings.setOnClickListener(view -> {
            Intent itnent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(itnent);
            disconnect();
            finish();
        });


        // Check if data exists in SharedPreferences
        if (storeId != null && chainId != null && gate != null && regionId != null) {
            // Construct the WebSocket URL

            selectedGateTextView.setText("Selected Gate: " + gate);
            selectedChainIdTextView.setText("Selected ChainId: " + chainId);
            selectedStoreIdTextView.setText("Selected StoreId: " + storeId);
            selectedRegionIdTextView.setText("RegionID: " + regionId);

            String WEB_SOCKET_URL = String.format(
                    "ws://" + Constants.SOCKET_BASE_URL + "/ws/?deviceID=2143&storeID=%s&chainID=%s&gate=%s&regionID=%s",
                    storeId, chainId, gate, regionId
            );

            // Create and connect the WebSocket client
            socketClient = new QBWebSocketClient(WEB_SOCKET_URL, this::handleListener);
            socketClient.connect();
            Log.d("tAAAAGGGG", WEB_SOCKET_URL);
        } else {
            showToast("Please Setup the gate first");
        }

        this.handler = new Handler(Looper.getMainLooper());
        this.connectionChecker = new Runnable() {
            @Override
            public void run() {
                checkConnection();
                handler.postDelayed(this, CHECK_INTERVAL);
            }
        };


//        File myFile = new File(getExternalFilesDir(null), "textfile");
//        File gpxfile;
//        if (!myFile.exists()) {
//            myFile.mkdir();
//            gpxfile = new File(myFile, "ipaddress");
//            try {
//
//                FileWriter writer = new FileWriter(gpxfile);
//                writer.append("write ip address here");
//                writer.flush();
//                writer.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            StringBuilder text = new StringBuilder();
//            gpxfile = new File(myFile, "ipaddress");
//            try {
//                BufferedReader br = new BufferedReader(new FileReader(gpxfile));
//                String line;
//
//                while ((line = br.readLine()) != null) {
//                    text.append(line);
//
//                }
//                br.close();
//                ip = text.toString();
//            } catch (IOException e) {
//                //You'll need to add proper error handling here
//            }
//        }

        Toast.makeText(getApplicationContext(), ip.trim(), Toast.LENGTH_LONG).show();
        txtcat = (TextView) findViewById(R.id.txtcat);
        txttime = (TextView) findViewById(R.id.txttime);
        logContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        logContent.setHorizontallyScrolling(true); // 不让超出屏幕的文本自动换行，使用滚动条
        logContent.setFocusable(true);
        Thread serverthread = new Thread(new ServerThread());
        serverthread.start();
        Thread cardreadthread = new Thread(new CardReadThread());
        cardreadthread.start();
//
        btn_hide.setOnClickListener(view -> {
            if (btn_hide.getText().equals("HIDE")) {
                btn_hide.setText("VIEW");
                layout.setVisibility(View.GONE);
                text.setVisibility(View.VISIBLE);
            } else {
                text.setVisibility(View.GONE);
                btn_hide.setText("HIDE");
                layout.setVisibility(View.VISIBLE);
            }
        });

        btnConnect.setOnClickListener(view -> {
            connect();
        });

        btnOpenDoor.setOnClickListener(view -> {
            showToast("Gate Opened");
            log("Gate Opened");
            smdt.smdtSetXrm117xGpioDirection(5, 1, 0);
            smdt.smdtSetXrm117xGpioValue(5, 0);
            Thread thread_gpio = new Thread(runngpio5Test);
            thread_gpio.start();
        });

    }


    private void checkConnection() {
        if (!isWebSocketConnected()) {
            System.out.println("WebSocket not connected. Reconnecting...");
            reconnect();
        } else {
            System.out.println("WebSocket connected. Sending ping...");
            socketClient.send("ping"); // Send a ping message to keep the connection alive
        }
    }

    private void reconnect() {
//        disconnect();  // Close the current connection
        connect();     // Re-establish connection
    }

    public void disconnect() {
        handler.removeCallbacks(connectionChecker); // Stop the periodic check
        if (socketClient != null) {
            socketClient.close(1000, "Disconnecting");
        }
    }

    public void connect() {

        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String storeId = sharedPreferences.getString("storeId", null);
        String chainId = sharedPreferences.getString("chainId", null);
        String gate = sharedPreferences.getString("gate", null);
        String regionId = sharedPreferences.getString("regionID", null);

        if (storeId != null && chainId != null && gate != null && regionId != null) {
            // Construct the WebSocket URL
            disconnect();


            String WEB_SOCKET_URL = String.format(
                    "ws://" + Constants.SOCKET_BASE_URL + "/ws/?deviceID=2143&storeID=%s&chainID=%s&gate=%s&regionID=%s",
                    storeId, chainId, gate, regionId
            );

            // Create and connect the WebSocket client
            socketClient = new QBWebSocketClient(WEB_SOCKET_URL, this::handleListener);
            socketClient.connect();
            showToast("Connected");
            log("Connected");
            Log.d("tAAAAGGGG", WEB_SOCKET_URL);
        }


    }

    private boolean isWebSocketConnected() {
        if (socketClient != null) {
            try {
                socketClient.send("ping"); // Attempt to send a ping message
                return true;
            } catch (Exception e) {
                System.out.println("WebSocket not connected: " + e.getMessage());
                return false;
            }
        }
        return false;
    }

    private void handleListener(String s) {
        try {
            if (s == null || s.isEmpty()) {
                Log.e("Error", "Received empty or null string");
                return;
            }

            JSONObject jsonObject = new JSONObject(s);
            String event = jsonObject.optString("event", "unknown");

//            if (event.equals("use")) {
//                shouldScan = false;
//            }
//            else {
//                shouldScan = true;
//            }
//
            shouldScan = true;
            if ("use".equals(event)) {
                JSONObject responseObject = jsonObject.optJSONObject("response");

                if (responseObject != null) {
                    boolean status = responseObject.optBoolean("status", false);
                    String ticket = responseObject.optString("ticket", null);
                    String transactionID = responseObject.optString("transactionID", "N/A");

                    int sender = jsonObject.optInt("sender", -1);
                    String uuid = jsonObject.optString("uuid", "");

                    if (status) {
                        INOpenDoor(transactionID);
//                        showToast(transactionID);
                    }
                }

            } else if ("scan".equals(event)) {
                //shouldScan = true;
                JSONObject responseObject = jsonObject.optJSONObject("response");
                if (responseObject != null && !responseObject.optBoolean("status", true)) {
                    if (BuildConfig.FLAVOR == "staging"  || BuildConfig.FLAVOR == "prod") {
                        openRed();
                    }
                }
            }


        } catch (JSONException e) {
            Log.e("JSON Error", "Error parsing JSON: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("Error", "Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }


    class CardReadThread implements Runnable {
        Handler h = new Handler();

        @Override
        public void run() {
            h.post(new Runnable() {
                @Override
                public void run() {


                }
            });


        }
    }


    class ServerThread implements Runnable {
        Socket s;
        ServerSocket ss;
        InputStreamReader isr;
        BufferedReader bufferedReader;
        String message;
        Handler h = new Handler();

        @Override
        public void run() {
            try {
                ss = new ServerSocket(3030);
                while (true) {
                    s = ss.accept();
                    isr = new InputStreamReader(s.getInputStream());
                    bufferedReader = new BufferedReader(isr);
                    message = bufferedReader.readLine();
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            smdt.smdtSetXrm117xGpioValue(1, 1);//扩展 IO 口
                            smdt.smdtSetXrm117xGpioValue(2, 1);//扩展 IO 口
                            smdt.smdtSetXrm117xGpioValue(3, 1);//扩展 IO 口
                            if (message.equals("OK READ")) {
                                smdt.smdtSetXrm117xGpioDirection(2, 1, 0);
                                smdt.smdtSetXrm117xGpioValue(2, 0);
                                smdt.smdtSetXrm117xGpioDirection(5, 1, 0);
                                smdt.smdtSetXrm117xGpioValue(5, 0);
                                Thread thread_gpio = new Thread(runngpio5Test1);
                                thread_gpio.start();
                                log("swallowCard success");
                            } else if (message.equals("OK DISCOUNT")) {
                                smdt.smdtSetXrm117xGpioDirection(3, 1, 0);
                                smdt.smdtSetXrm117xGpioValue(3, 0);
                                smdt.smdtSetXrm117xGpioDirection(1, 1, 0);
                                smdt.smdtSetXrm117xGpioValue(1, 0);
                                smdt.smdtSetXrm117xGpioDirection(5, 1, 0);
                                smdt.smdtSetXrm117xGpioValue(5, 0);
                                Thread thread_gpio = new Thread(runngpio5Test1);
                                thread_gpio.start();
                            } else if (message.equals("OK INVALID")) {
                                smdt.smdtSetXrm117xGpioDirection(1, 1, 0);
                                smdt.smdtSetXrm117xGpioValue(1, 0);
                                Thread thread_gpio = new Thread(runngpio5Testclose);
                                thread_gpio.start();
                            }
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    //	uartXRM0_Test_handler


    //hide

    public void openRed() {
//        内部IO 口
//        内部IO 口
//		smdt.smdtSetGpioDirection(1,1,0);
//		smdt.smdtSetExtrnalGpioValue(1,false);
//		Thread thread_gpio = new Thread(runngpioA6Test);
//		thread_gpio.start();

//        扩展IO 口
//        showToast("Red Light On");
//        log("Red Light On");
        smdt.smdtSetXrm117xGpioDirection(1, 1, 0);
        smdt.smdtSetXrm117xGpioValue(1, 0);

//		Thread thread_gpio = new Thread(runngpioA6Test);
//		thread_gpio.start();
    }

    //openGreenTest
    public void openGreen() {
        //内部IO 口
//		smdt.smdtSetGpioDirection(2,1,1);
//		smdt.smdtSetExtrnalGpioValue(2,true);
//		Thread thread_gpio = new Thread(runngpioA7Test);
//		thread_gpio.start();

        //扩展IO 口
        smdt.smdtSetXrm117xGpioDirection(2, 1, 0);
        smdt.smdtSetXrm117xGpioValue(2, 0);
//		Thread thread_gpio = new Thread(runngpioA7Test);
//		thread_gpio.start();
//        log("openGreen success");
    }


    //openBlueTest
    public void openBlue() {
        //内部IO 口
//		smdt.smdtSetGpioDirection(2,1,1);
//		smdt.smdtSetExtrnalGpioValue(2,true);
//		Thread thread_gpio = new Thread(runngpioA7Test);
//		thread_gpio.start();

        //扩展IO 口

        smdt.smdtSetXrm117xGpioDirection(3, 1, 0);
        smdt.smdtSetXrm117xGpioValue(3, 0);
//        log("openBlue success");
//		Thread thread_gpio = new Thread(runngpioA7Test);
//		thread_gpio.start();
    }

    //closeTest
    public void close(View vies) {
        //内部IO 口
//		smdt.smdtSetGpioDirection(2,1,1);
//		smdt.smdtSetExtrnalGpioValue(2,true);
//		Thread thread_gpio = new Thread(runngpioA7Test);
//		thread_gpio.start();

        //扩展IO 口
        smdt.smdtSetXrm117xGpioValue(1, 1);//扩展 IO 口
        smdt.smdtSetXrm117xGpioValue(2, 1);//扩展 IO 口
        smdt.smdtSetXrm117xGpioValue(3, 1);//扩展 IO 口

//		Thread thread_gpio = new Thread(runngpioA7Test);
//		thread_gpio.start();
        log("openGreen success");
    }


    //swallowCardTest
    public void swallowCard(View vies) {
        //内部IO 口
//		smdt.smdtSetGpioDirection(2,1,1);
//		smdt.smdtSetExtrnalGpioValue(2,true);
//		Thread thread_gpio = new Thread(runngpioA7Test);
//		thread_gpio.start();

        //扩展IO 口
        smdt.smdtSetXrm117xGpioDirection(4, 1, 0);
        smdt.smdtSetXrm117xGpioValue(4, 0);
        Thread thread_gpio = new Thread(runngpio4Test);
        thread_gpio.start();
        log("swallowCard success");
    }


    //INOpenDoorTest
    public void INOpenDoor(String transactionID) {
        //内部IO 口
//		smdt.smdtSetGpioDirection(2,1,1);
//		smdt.smdtSetExtrnalGpioValue(2,true);
//		Thread thread_gpio = new Thread(runngpioA7Test);
//		thread_gpio.start();
        if (BuildConfig.FLAVOR == "staging"  || BuildConfig.FLAVOR == "prod") {
            openGreen();
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            try {
                // Check if transactionID is valid
                if (transactionID == null || transactionID.isEmpty() || transactionID.equals("N/A")) {
                    return;
                }

                try {
                    JSONObject msgClose = new JSONObject();
                    msgClose.put("event", "close");
                    msgClose.put("ticket", transactionID);


                    // Send the message
                    socketClient.send(msgClose.toString());
                    if (BuildConfig.FLAVOR == "staging"  || BuildConfig.FLAVOR == "prod") {
                        openBlue();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Error", "JSONException: " + e.getMessage());
                }


            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Error", "Exception: " + e.getMessage());
            }
        }, gateTiming * 1000);

        // Delay of 4000 milliseconds (4 seconds)
//        showToast("Gate Opened");
//        log("Gate Opened");
        if (BuildConfig.FLAVOR == "staging"  || BuildConfig.FLAVOR == "prod") {
            smdt.smdtSetXrm117xGpioDirection(5, 1, 0);
            smdt.smdtSetXrm117xGpioValue(5, 0);
            Thread thread_gpio = new Thread(runngpio5Test);
            thread_gpio.start();
        }


        //扩展IO 口
    }


    //testIO6
    public void testIO6(View vies) {
        //内部IO 口
//		smdt.smdtSetGpioDirection(2,1,1);
//		smdt.smdtSetExtrnalGpioValue(2,true);
//		Thread thread_gpio = new Thread(runngpioA7Test);
//		thread_gpio.start();

        //扩展IO 口
        smdt.smdtSetXrm117xGpioDirection(6, 1, 0);
        smdt.smdtSetXrm117xGpioValue(6, 0);
        Thread thread_gpio = new Thread(runngpio6Test);
        thread_gpio.start();
        log("testIO6 success");
    }

    //ReadAccessControlPanelData
    public void ReadAccessControlPanelData(View vies) {
        if (uartReadAccessControlPanelData != null) {
            uartReadAccessControlPanelData.uartClose();
        }
        uartReadAccessControlPanelData = new UartDev(UartUtil.UART_DEVXRM0, UartUtil.UART_B19200, true, ReadAccessControlPanelData_Test_handler);
        uartReadAccessControlPanelData.setParity(UartUtil.UART_PARITY_N);
        uartReadAccessControlPanelData.uartOpen();
    }

    //	ReadAccessControlPanelData_Test_handler
    @SuppressLint("HandlerLeak")
    Handler ReadAccessControlPanelData_Test_handler = new Handler() {
        public void handleMessage(Message msg) {
            String rbuf = (String) msg.obj;
            if (rbuf == null) {
                Log.i(TAG, "--------------------No data！");
            } else {
                log("ReadAccessControlPanelData:" + rbuf);
            }

        }
    };

    public Runnable runngpio6Test = new Runnable() {
        public void run() {
            try {
                Thread.sleep(1500);
//				smdt.smdtSetExtrnalGpioValue(1,true);  //内部 IO 口
                smdt.smdtSetXrm117xGpioValue(6, 1);//扩展 IO 口
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    public Runnable runngpio5Test = new Runnable() {
        public void run() {
            try {
                Thread.sleep(1500);
//				smdt.smdtSetExtrnalGpioValue(1,true);  //内部 IO 口

                smdt.smdtSetXrm117xGpioValue(5, 1);//扩展 IO 口

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    public Runnable runngpio5Test1 = new Runnable() {
        public void run() {
            try {
                Thread.sleep(1500);
//				smdt.smdtSetExtrnalGpioValue(1,true);  //内部 IO 口

                smdt.smdtSetXrm117xGpioValue(5, 1);//扩展 IO 口
                //test
                smdt.smdtSetXrm117xGpioValue(1, 1);//扩展 IO 口
                smdt.smdtSetXrm117xGpioValue(2, 1);//扩展 IO 口
                smdt.smdtSetXrm117xGpioValue(3, 1);//扩展 IO 口
                smdt.smdtSetXrm117xGpioDirection(3, 1, 0);
                smdt.smdtSetXrm117xGpioValue(3, 0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    public Runnable runngpio5Testclose = new Runnable() {
        public void run() {
            try {
                Thread.sleep(1500);
//				smdt.smdtSetExtrnalGpioValue(1,true);  //内部 IO 口
                //test
                smdt.smdtSetXrm117xGpioValue(1, 1);//扩展 IO 口
                smdt.smdtSetXrm117xGpioValue(2, 1);//扩展 IO 口
                smdt.smdtSetXrm117xGpioValue(3, 1);//扩展 IO 口
                smdt.smdtSetXrm117xGpioDirection(3, 1, 0);
                smdt.smdtSetXrm117xGpioValue(3, 0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    class ReadThread implements Runnable {

        Handler h = new Handler();

        @Override
        public void run() {
            try {
                Thread.sleep(1500);
//				smdt.smdtSetExtrnalGpioValue(2,false);  //内部 IO 口
                smdt.smdtSetXrm117xGpioValue(4, 1);  //扩展IO 口
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        layout.setVisibility(View.VISIBLE);
                        text.setVisibility(View.GONE);
                        btn_hide.setText("HIDE");
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Runnable runngpio4Test = new Runnable() {
        public void run() {
            try {
                Thread.sleep(1500);
//				smdt.smdtSetExtrnalGpioValue(2,false);  //内部 IO 口
                smdt.smdtSetXrm117xGpioValue(4, 1);  //扩展IO 口
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    public Runnable runngpio4Test1 = new Runnable() {
        Handler h = new Handler();

        public void run() {
            try {
                //Handler h=new Handler();
                Thread.sleep(1500);
//				smdt.smdtSetExtrnalGpioValue(2,false);  //内部 IO 口
                smdt.smdtSetXrm117xGpioValue(4, 1);  //扩展IO 口
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        layout.setVisibility(View.VISIBLE);
                        text.setVisibility(View.GONE);
                        btn_hide.setText("HIDE");
                    }
                });


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    public void log(String log) {
        logContent.setText(logContent.getText().toString() + "\n" + log);
    }

    //logClear 清除日志
    public void logClear(View vies) {
        logContent.setText("");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (uartS1 != null) {
            uartS1.uartClose();
            uartS1 = null;
        }
        if (uartReadAccessControlPanelData != null) {
            uartReadAccessControlPanelData.uartClose();
            uartReadAccessControlPanelData = null;
        }
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        try {
            return ScanReaderHelper.readHardwareScan(event, this::onQrScanned);
        } catch (Exception e) {
//            log("Error:" + e.getMessage());
            e.printStackTrace();
            showToast("Oops looks like gate is out of sync. Please Re-Scan");
            connect();
        }
        return false;
    }

    final static long SCAN_DELAY = 750;
    long lastScanTime = 0;

    private void onQrScanned(String qrCode) {
        ScanReaderHelper.clearBarcode();
//        connect();
        if (qrCode != null) {
            log(qrCode);





            SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
            String storeId = sharedPreferences.getString("storeId", null);
            String chainId = sharedPreferences.getString("chainId", null);
            String gate = sharedPreferences.getString("gate", null);


            if (storeId != null && chainId != null && gate != null) {

                try {
                    long currentTime = System.currentTimeMillis();

                    if (!(currentTime - lastScanTime < SCAN_DELAY)) {
                        sendScanEvent(qrCode);
                    }

                    lastScanTime = currentTime;


//            if (isWebSocketConnected()) {
//
//
//            } else {
//                showToast("Connection lost please try reconnecting");
//                log("Connection lost please try reconnecting");
//                log(String.valueOf(isNetworkConnected()) + String.valueOf(isWebSocketConnected()));
//            }
//                runOnUiThread();
//                showToast("Scanned: " + qrCode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                showToast("Please select store and chain");
            }
        } else {
            connect();
            log("looks like connection is lost please reconnect");
        }


    }

    private void sendScanEvent(String qrCode) throws JSONException {
        JSONObject msg = new JSONObject();

        msg.put("event", "scan");
        msg.put("ticket", qrCode);

        log(qrCode);

        socketClient.send(msg.toString());
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (socketClient != null) {
            socketClient.close();
        }
    }
}
