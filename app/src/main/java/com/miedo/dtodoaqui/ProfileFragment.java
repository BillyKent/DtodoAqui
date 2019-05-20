package com.miedo.dtodoaqui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Button bt_establecimientos;

    Button bt_registro;

    Button bt_login;

    Button bt_myprofile;

    Button bt_logout;

    LinearLayout progressLayout;

    //private UserViewModel viewModel;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // Inflate the layout for this fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        /*progressLayout = view.findViewById(R.id.progressLayout);

        bt_establecimientos = view.findViewById(R.id.bt_establishments);
        bt_establecimientos.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.establishments_dest));

        bt_registro = view.findViewById(R.id.bt_register);
        bt_registro.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.registeruser_dest));

        bt_login = view.findViewById(R.id.bt_login);
        bt_login.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.loginuser_dest));

        bt_myprofile = view.findViewById(R.id.bt_update);
        bt_myprofile.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.updateuser_dest));

        bt_logout = view.findViewById(R.id.bt_logout);
        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.Logout();
                bt_registro.setVisibility(View.VISIBLE);
                bt_login.setVisibility(View.VISIBLE);
                bt_establecimientos.setVisibility(View.GONE);
                bt_myprofile.setVisibility(View.GONE);
                bt_logout.setVisibility(View.GONE);
            }
        });

        //ViewModel

        viewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        viewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user == null){
                    //Login fallido
                    Log.i("Profile","Login fallido");
                }else {

                    if (user.getId().equals("-")) {
                        //Cierre sesión

                    } else {
                        //Login exitoso
                        Log.i("Profile Fragment", "Login exitoso");

                    }
                }
            }
        });

        // Session

        User user = SessionManager.getInstance().getCurrentSession();
        if(!user.getUsername().equals(""))
            viewModel.SincUser(user.getUsername(),user.getPassword());
        else
            ShowLogin();*/
        return view;
    }

    private void ShowLogin(){
        bt_registro.setVisibility(View.VISIBLE);
        bt_login.setVisibility(View.VISIBLE);
        bt_establecimientos.setVisibility(View.GONE);
        bt_myprofile.setVisibility(View.GONE);
        bt_logout.setVisibility(View.GONE);

        progressLayout.setVisibility(View.GONE);
    }

    private void ShowLogged(){
        bt_registro.setVisibility(View.GONE);
        bt_login.setVisibility(View.GONE);
        bt_establecimientos.setVisibility(View.VISIBLE);
        bt_myprofile.setVisibility(View.VISIBLE);
        bt_logout.setVisibility(View.VISIBLE);

        progressLayout.setVisibility(View.GONE);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        void onFragmentInteraction(Uri uri);
    }
}
