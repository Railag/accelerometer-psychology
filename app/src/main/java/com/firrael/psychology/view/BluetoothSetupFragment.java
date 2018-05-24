package com.firrael.psychology.view;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firrael.psychology.R;
import com.firrael.psychology.presenter.AccelerometerTestPresenter;
import com.firrael.psychology.view.adapter.BluetoothDeviceAdapter;
import com.firrael.psychology.view.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import nucleus.factory.RequiresPresenter;

/**
 * Created by railag on 26.02.2018.
 */

@RequiresPresenter(AccelerometerTestPresenter.class)
public class BluetoothSetupFragment extends BaseFragment<AccelerometerTestPresenter> {

    private BluetoothDeviceAdapter adapter;

    @BindView(R.id.list)
    RecyclerView list;

    public static BluetoothSetupFragment newInstance() {

        Bundle args = new Bundle();

        BluetoothSetupFragment fragment = new BluetoothSetupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.bluetoothSetupTitle);
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_bluetooth_setup;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getMainActivity().blueTop();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initView(View v) {
        adapter = new BluetoothDeviceAdapter();

        List<BluetoothDevice> pairedDevices = getMainActivity().getPairedDevices();
        BluetoothDeviceAdapter.OnDeviceClickListener listener = getMainActivity().getBluetoothListener();

        adapter.setDevices(new ArrayList<>(pairedDevices), listener);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        list.setLayoutManager(manager);

        list.setAdapter(adapter);

    }
}