package org.lightips.sample.contacts.fragment;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import org.lightips.sample.contacts.Main;
import org.lightips.sample.contacts.R;
import org.lightips.sample.contacts.service.MyService;

public class ServiceFragment extends Fragment implements View.OnClickListener {
    protected static final String TAG = ServiceFragment.class.getSimpleName();
    private static final String ARG_SECTION_NUMBER = "section_number";

    private OnFragmentInteractionListener mListener;
    private MyService myService;
//    private int progress = 0;
    private ProgressBar mProgressBar;
    private ServiceConnection conn;

    public static ServiceFragment newInstance(int sectionNumber) {
        ServiceFragment fragment = new ServiceFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    public ServiceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_service, container, false);
        mProgressBar = (ProgressBar)view.findViewById(R.id.progressBar);

        Intent i= new Intent(view.getContext(), MyService.class);
        if(!isMyServiceRunning(MyService.class)){
            conn = new ServiceConnection() {

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }

                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    //返回一个MsgService对象
                    myService = ((MyService.MsgBinder)service).getService();

                }
            };

            view.getContext().bindService(i, conn, Context.BIND_AUTO_CREATE);
        }
        else{
            conn = new ServiceConnection() {

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }

                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    //返回一个MsgService对象
                    myService = ((MyService.MsgBinder)service).getService();

                }
            };
            view.getContext().bindService(i, conn, 0);
            listenProgress();
        }
        Button btnService = (Button)view.findViewById(R.id.btn_start_service);
        btnService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myService.startDownLoad();
                listenProgress();
            }
        });

        Button btnStop = (Button)view.findViewById(R.id.btn_stop_service);
        btnStop.setOnClickListener(this);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((Main) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_pause_service:
                this.onPauseBtn(v);
                break;
            case R.id.btn_resume_service:
                this.onResumeBtn(v);
                break;
            case R.id.btn_stop_service:
                this.onStopBtn(v);
                break;
        }
    }

    private void onPauseBtn(View v){

    }

    private void onResumeBtn(View v){

    }

    private void onStopBtn(View v){
        mProgressBar.setProgress(0);
        myService.resumeDownload();
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public void listenProgress(){
        new Thread(new Runnable() {

            @Override
            public void run() {
                while(myService.getProgress() <= MyService.MAX_PROGRESS){
                    mProgressBar.setProgress(myService.getProgress());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    @Override
    public void onDestroyView() {
        this.getActivity().unbindService(conn);
        super.onDestroyView();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) this.getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
