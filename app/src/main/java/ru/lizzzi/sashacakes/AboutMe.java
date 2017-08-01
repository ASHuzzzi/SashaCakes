package ru.lizzzi.sashacakes;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AboutMe.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AboutMe#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutMe extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String APP_PREFERENCES = "mycontacts";
    public static final String APP_PREFERENCES_Name = "Name";
    public static final String APP_PREFERENCES_Telephone = "Telephone";
    public static final String APP_PREFERENCES_Email = "Email";
    public static final String APP_PREFERENCES_Street = "Street";
    public static final String APP_PREFERENCES_Home = "Home";
    private SharedPreferences mMyContacts;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    public AboutMe() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AboutMe.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutMe newInstance(String param1, String param2) {
        AboutMe fragment = new AboutMe();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMyContacts = this.getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_about_me, container, false);
        Button button_save_contacts = (Button) v.findViewById(R.id.button_save_contacts);
        final EditText mEditText_Name = (EditText) v.findViewById(R.id.editText_Name);
        final EditText mEditText_Telephone = (EditText) v.findViewById(R.id.editText_Telephone);
        final EditText mEditText_Email = (EditText) v.findViewById(R.id.editText_Email);
        final EditText mEditText_Street = (EditText) v.findViewById(R.id.editText_Street);
        final EditText mEditText_Home = (EditText) v.findViewById(R.id.editText_Home);


        if (mMyContacts.contains(APP_PREFERENCES_Name)){
            String str_Name = mMyContacts.getString(APP_PREFERENCES_Name, "");
            mEditText_Name.setText(str_Name);
        }
        if (mMyContacts.contains(APP_PREFERENCES_Telephone)){
            String str_Telephone = mMyContacts.getString(APP_PREFERENCES_Telephone, "");
            mEditText_Telephone.setText(str_Telephone);
        }
        if (mMyContacts.contains(APP_PREFERENCES_Email)){
            String str_Email = mMyContacts.getString(APP_PREFERENCES_Email, "");
            mEditText_Email.setText(str_Email);
        }
        if (mMyContacts.contains(APP_PREFERENCES_Street)){
            String str_Street = mMyContacts.getString(APP_PREFERENCES_Street, "");
            mEditText_Street.setText(str_Street);
        }
        if (mMyContacts.contains(APP_PREFERENCES_Home)){
            String str_Home = mMyContacts.getString(APP_PREFERENCES_Home, "");
            mEditText_Home.setText(str_Home);
        }


        button_save_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_Name = mEditText_Name.getText().toString();
                String str_Telephone = mEditText_Telephone.getText().toString();
                String str_Email = mEditText_Email.getText().toString();
                String str_Street = mEditText_Street.getText().toString();
                String str_Home = mEditText_Home.getText().toString();

                SharedPreferences.Editor editor = mMyContacts.edit();
                editor.putString(APP_PREFERENCES_Name, str_Name);
                editor.putString(APP_PREFERENCES_Telephone, str_Telephone);
                editor.putString(APP_PREFERENCES_Email, str_Email);
                editor.putString(APP_PREFERENCES_Street, str_Street);
                editor.putString(APP_PREFERENCES_Home, str_Home);
                editor.apply();

                if (str_Telephone.length() < 11) {
                    Toast toast = Toast.makeText(getContext(), "Укажите телефон. Иначе мы не сможем в Вами связаться!",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    mEditText_Telephone.requestFocus();
                } else {
                    Toast toast = Toast.makeText(getContext(), "Сохранено",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });


        return v;
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        getActivity().setTitle(R.string.AboutMe_title);
    }
}
