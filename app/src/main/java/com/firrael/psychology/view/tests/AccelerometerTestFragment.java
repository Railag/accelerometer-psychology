package com.firrael.psychology.view.tests;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.firrael.psychology.R;
import com.firrael.psychology.Utils;
import com.firrael.psychology.model.Result;
import com.firrael.psychology.presenter.AccelerometerTestPresenter;
import com.firrael.psychology.view.adapter.BluetoothDeviceAdapter;
import com.firrael.psychology.view.base.BaseFragment;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import butterknife.BindView;
import nucleus.factory.RequiresPresenter;

/**
 * Created by railag on 26.02.2018.
 */

@RequiresPresenter(AccelerometerTestPresenter.class)
public class AccelerometerTestFragment extends BaseFragment<AccelerometerTestPresenter> {

    private final static String TAG = AccelerometerTestFragment.class.getSimpleName();

    private final static int PACKAGE_SIZE = 5;

    private final static int REQUEST_ENABLE_BT = 101;

    private Handler handler;
    private final static int INTERVAL = 10;

    private int counter = 0;
    private ArrayList<Double> x = new ArrayList<>(), y = new ArrayList<>();

    private DisplayMetrics displayMetrics;

    private BluetoothAdapter bluetoothAdapter;

    @BindView(R.id.accelerometer_circle)
    ImageView accelerometerCircle;

    private AcceptThread acceptThread;
    private ConnectedThread connectedThread;

    private ValueAnimator yAnimator;
    private ValueAnimator xAnimator;

    private float currentX = -1f;
    private float currentY = -1f;

    private boolean isX = true;

    private BluetoothDeviceAdapter adapter;
    @BindView(R.id.list)
    RecyclerView list;
    private boolean connected;

    public static AccelerometerTestFragment newInstance() {

        Bundle args = new Bundle();

        AccelerometerTestFragment fragment = new AccelerometerTestFragment();
        fragment.setHasOptionsMenu(true);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.accelerometerTestTitle);
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_test_accelerometer;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.bluetooth, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bluetooth:
                initBluetooth();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        handler = new Handler();

        displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(receiver, filter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initBluetooth() {
        //    startLoading();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Log.e(TAG, "Bluetooth is not supported!");
            stopLoading();
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            stopLoading();
            return;
        }

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                Log.i(TAG, "Paired device: " + deviceName + " with MAC: " + deviceHardwareAddress);
            }
        }

        BluetoothDeviceAdapter.OnDeviceClickListener listener = new BluetoothDeviceAdapter.OnDeviceClickListener() {
            @Override
            public void onSelected(BluetoothDevice device) {
                // start bluetooth host
                acceptThread = new AcceptThread(device);
                acceptThread.start();
            }
        };

        adapter = new BluetoothDeviceAdapter();
        adapter.setDevices(new ArrayList<>(pairedDevices), listener);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        list.setLayoutManager(manager);

        list.setAdapter(adapter);


    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                Log.i(TAG, "Discovered device: " + deviceName + " with MAC: " + deviceHardwareAddress);
            }
        }
    };

    private void clear() {
        x.clear();
        y.clear();

        counter = 0;
    }

    private void readFromBluetooth() {
        if (connectedThread != null) {
            connectedThread.read();
            update();
        }
    }

    public void onSuccess(Result result) {
        if (result == null) {
            onError(new IllegalArgumentException());
            return;
        }
        if (result.invalid()) {
            toast(result.error);
            return;
        }

        // TODO handle? log?
    }

    public void onError(Throwable throwable) {
        stopLoading();
        throwable.printStackTrace();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }

        if (getActivity() != null) {
            getActivity().unregisterReceiver(receiver);
        }

        if (xAnimator != null) {
            xAnimator.cancel();
        }

        if (yAnimator != null) {
            yAnimator.cancel();
        }

        stopBluetooth();
    }

    private DecimalFormat df = new DecimalFormat("#.00");

    private void update() {
        if (counter >= PACKAGE_SIZE * 2) {
            int realWidth = displayMetrics.widthPixels; // 1920
            int realHeight = displayMetrics.heightPixels; // 1080

            float[] xValues = new float[this.x.size()];
            for (int i = 0; i < this.x.size(); i++) {
                xValues[i] = this.x.get(i).floatValue();
            }

            xAnimator = ValueAnimator.ofFloat(xValues);
            xAnimator.setInterpolator(new LinearInterpolator());
            xAnimator.setDuration(500);
            xAnimator.addUpdateListener(animation -> {

                float value = (float) animation.getAnimatedValue();

                float adjustedX = adjust(value, realWidth, true);
                if (currentX == -1f) {
                    if (accelerometerCircle != null) {
                        accelerometerCircle.setX(adjustedX);
                    }
                    currentX = adjustedX;
                } else {
                    float medianX = (currentX + adjustedX) / 2;
                    if (accelerometerCircle != null) {
                        accelerometerCircle.setX(medianX);
                    }
                    currentX = medianX;
                }
            });

            float[] yValues = new float[this.y.size()];
            for (int i = 0; i < this.y.size(); i++) {
                yValues[i] = this.y.get(i).floatValue();
            }

            yAnimator = ValueAnimator.ofFloat(yValues);
            yAnimator.setInterpolator(new LinearInterpolator());
            yAnimator.setDuration(500);
            yAnimator.addUpdateListener(animation -> {
                float value = (float) animation.getAnimatedValue();

                float adjustedY = adjust(value, realHeight, false);
                if (currentY == -1f) {
                    if (accelerometerCircle != null) {
                        accelerometerCircle.setY(adjustedY);
                    }
                    currentY = adjustedY;
                } else {
                    float medianY = (currentY + adjustedY) / 2;
                    if (accelerometerCircle != null) {
                        accelerometerCircle.setY(medianY);
                    }
                    currentY = medianY;
                }
            });

            xAnimator.start();
            yAnimator.start();

            clear();
        }
    }

    private void addValue(double value) {
        if (isX) {
            this.x.add(Double.valueOf(df.format(value)));
        } else {
            this.y.add(Double.valueOf(df.format(value)));
        }

        counter++;
    }

    private float adjust(double paramToAdjust, double maxValue, boolean inverse) { // 7 - xResolutionMax (e.g. 1920), 3 - y?
        //    7-1920
        //    paramToAdjust-x
        double value = (maxValue / 2) + (float) (paramToAdjust * maxValue / (Utils.THRESHOLD_ACCELEROMETER_MAX * 2));
        return inverse ? (float) Math.abs(maxValue - value) : (float) value;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                initBluetooth();
            }
        }
    }

    private class AcceptThread extends Thread {
        private final BluetoothSocket mmSocket;

        public AcceptThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            try {
                tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("0cbb85aa-7951-41a6-b891-b2ee53960860"));
            } catch (IOException e) {
                Log.e(TAG, "Socket's listen() method failed", e);
            }

            mmSocket = tmp;
        }

        public void run() {
            while (true) {
                try {
                    mmSocket.connect();
                } catch (IOException e) {
                    Log.e(TAG, "Socket's accept() method failed", e);
                    stopBluetooth();
                    break;
                }

                // A connection was accepted. Perform work associated with
                // the connection in a separate thread.
                manageMyConnectedSocket(mmSocket);
                break;
            }
        }

        // Closes the connect socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }

    private void manageMyConnectedSocket(BluetoothSocket socket) {
        getActivity().runOnUiThread(() -> {
            stopLoading();
            connected = true;
            accelerometerCircle.setVisibility(View.VISIBLE);
        });

        connectedThread = new ConnectedThread(socket);
        handler.post(updateRunnable);
    }

    Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            if (!connected) {
                return;
            }

            readFromBluetooth();
            if (handler != null) {
                handler.postDelayed(updateRunnable, INTERVAL);
            }
        }
    };


    public static final int MESSAGE_READ = 0;
    public static final int MESSAGE_WRITE = 1;
    public static final int MESSAGE_TOAST = 2;

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer; // mmBuffer store for the stream

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams; using temp objects because
            // member streams are final.
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating input stream", e);
            }
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating output stream", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            mmBuffer = new byte[1024];
            int numBytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                try {
                    // Read from the InputStream.
                    numBytes = mmInStream.read(mmBuffer);
                    // Send the obtained bytes to the UI activity.
                    Message readMsg = handler.obtainMessage(
                            MESSAGE_READ, numBytes, -1,
                            mmBuffer);
                    readMsg.sendToTarget();
                } catch (IOException e) {
                    Log.d(TAG, "Input stream was disconnected", e);
                    break;
                }
            }
        }

        // Call this from the main activity to send data to the remote device.
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);

                // Share the sent message with the UI activity.
                Message writtenMsg = handler.obtainMessage(
                        MESSAGE_WRITE, -1, -1, mmBuffer);
                writtenMsg.sendToTarget();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when sending data");

                // Send a failure message back to the activity.
                Message writeErrorMsg =
                        handler.obtainMessage(MESSAGE_TOAST);
                Bundle bundle = new Bundle();
                bundle.putString("toast",
                        "Couldn't send data to the other device");
                writeErrorMsg.setData(bundle);
                handler.sendMessage(writeErrorMsg);

                stopBluetooth();
            }
        }

        public void read() {
            try {
                DataInputStream dataInputStream = new DataInputStream(mmInStream);
                for (int i = 0; i < PACKAGE_SIZE * 2; i++) {
                    double value = dataInputStream.readDouble();
                    Log.i(TAG, isX ? "X: " : "Y: " + value);

                    addValue(value);
                    if (i == PACKAGE_SIZE) {
                        isX = false;
                    }
                }

                isX = true;

            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Error occurred when sending data");

                // Send a failure message back to the activity.
                Message writeErrorMsg =
                        handler.obtainMessage(MESSAGE_TOAST);
                Bundle bundle = new Bundle();
                bundle.putString("toast",
                        "Couldn't send data to the other device");
                writeErrorMsg.setData(bundle);
                handler.sendMessage(writeErrorMsg);

                stopBluetooth();
            }
        }

        // Call this method from the main activity to shut down the connection.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }

    private void stopBluetooth() {
        if (acceptThread != null) {
            acceptThread.cancel();
        }

        if (connectedThread != null) {
            connectedThread.cancel();
        }

        connected = false;

        if (accelerometerCircle != null) {
            accelerometerCircle.setVisibility(View.GONE);
        }

        if (handler != null) {
            handler.removeCallbacks(updateRunnable);
        }

        Log.i(TAG, "Bluetooth connection stopped");
    }
}