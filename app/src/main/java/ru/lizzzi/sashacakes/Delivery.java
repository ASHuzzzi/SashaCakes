package ru.lizzzi.sashacakes;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment_menu must implement the
 * {@link Delivery.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Delivery#newInstance} factory method to
 * create an instance of this fragment_menu.
 */

public class Delivery extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment_menu initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Delivery() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment_menu using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment_menu Delivery.
     */
    // TODO: Rename and change types and number of parameters
    public static Delivery newInstance(String param1, String param2) {
        Delivery fragment = new Delivery();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_delivery, container, false);
        TextView textView1 = ((TextView) v.findViewById(R.id.textView1));
        TextView textView2 = ((TextView) v.findViewById(R.id.textView2));
        TextView textView3 = ((TextView) v.findViewById(R.id.textView3));
        TextView textView4 = ((TextView) v.findViewById(R.id.textView4));
        TextView textView5 = ((TextView) v.findViewById(R.id.textView5));
        TextView textView6 = ((TextView) v.findViewById(R.id.textView6));
        TextView textView7 = ((TextView) v.findViewById(R.id.textView7));

        textView1.setText("Самовывоз");
        textView2.setText("Из Токсово, 7 дней в неделю.");
        textView3.setText("Доставка");
        textView4.setText(Html.fromHtml("Ежедневно в Девяткино, Новое-Девяткино, Мурино (при условии заезда (<strong>НЕ</strong>) через КАД и Бугры), Кузьмолово, Мега-Парнас. Стоимость 150руб.!"));
        textView5.setText("По Санкт-Петербургу");
        textView6.setText(Html.fromHtml("От 300&#8381! (<strong>в будни</strong>). Цена зависит от адреса, веса, заказа и времени."));
        textView7.setText("В выходные дни я делаю Ваши тортики, в связи с этим развозить заказы самостоятельно на далекие расстояния просто нет возможности! Цена и возможность доставки в выходной день дальше Севера Спб по-согласованию!");

        textView1.setTextSize(24);
        textView3.setTextSize(24);
        textView5.setTextSize(24);

        textView2.setTextSize(16);
        textView4.setTextSize(16);
        textView6.setTextSize(16);
        textView7.setTextSize(16);

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
     * fragment_menu to allow an interaction in this fragment_menu to be communicated
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
        getActivity().setTitle(R.string.Delivery_title);
    }
}
