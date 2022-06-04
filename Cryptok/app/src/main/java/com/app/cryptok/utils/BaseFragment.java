package com.app.cryptok.utils;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;



import java.io.Serializable;
import java.util.HashMap;


abstract public class BaseFragment extends Fragment {
    abstract protected int setLayout();

    abstract protected void onLaunch();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        itemView=inflater.inflate(setLayout(), container, false);
        return itemView;
    }

    View itemView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onLaunch();
    }

    public <T extends android.view.View> T find(int id) {
        return itemView.findViewById(id);
    }
    public void goTo(Class className,Boolean isFinish){
        Intent intent=new Intent(getActivity(),className);
        startActivity(intent);
        if (isFinish){
            getActivity().finish();
        }
    }
    public void goTo(Class className, Boolean isFinish, HashMap hashMap){
        Intent intent=new Intent(getActivity(),className);
        intent.putExtra(UrlContainer.TRANSFER_DATA,hashMap);
        startActivity(intent);
        if (isFinish){
            getActivity().finish();
        }
    }
    public void goTo(Class className, Boolean isFinish, Object serializableObject){
        Intent intent=new Intent(getActivity(),className);
        intent.putExtra(UrlContainer.TRANSFER_DATA, (Serializable) serializableObject);
        startActivity(intent);
        if (isFinish){
            getActivity().finish();
        }
    }
    /*@Override
    public void onStop() {
        super.onStop();
      try {
          removeRtcEventHandler(this);
          if (rtcEngine()!=null)
          rtcEngine().leaveChannel();

      }catch (Exception e){
          e.printStackTrace();
      }
    }
    protected void removeRtcEventHandler(EventHandler handler) {
        application().removeEventHandler(handler);
    }
    protected AgoraApplication application() {
        return (AgoraApplication) Objects.requireNonNull(getActivity()).getApplication();
    }

    protected EngineConfig config() {
        return application().engineConfig();
    }
    protected RtcEngine rtcEngine() {
        return application().rtcEngine();
    }

    @Override
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {

    }

    @Override
    public void onLeaveChannel(IRtcEngineEventHandler.RtcStats stats) {

    }

    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {

    }

    @Override
    public void onUserOffline(int uid, int reason) {

    }

    @Override
    public void onUserJoined(int uid, int elapsed) {

    }

    @Override
    public void onLastmileQuality(int quality) {

    }

    @Override
    public void onLastmileProbeResult(IRtcEngineEventHandler.LastmileProbeResult result) {

    }

    @Override
    public void onLocalVideoStats(IRtcEngineEventHandler.LocalVideoStats stats) {

    }

    @Override
    public void onRtcStats(IRtcEngineEventHandler.RtcStats stats) {

    }

    @Override
    public void onNetworkQuality(int uid, int txQuality, int rxQuality) {

    }

    @Override
    public void onRemoteVideoStats(IRtcEngineEventHandler.RemoteVideoStats stats) {

    }

    @Override
    public void onRemoteAudioStats(IRtcEngineEventHandler.RemoteAudioStats stats) {

    }*/
}
