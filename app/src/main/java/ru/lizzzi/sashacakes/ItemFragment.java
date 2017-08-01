package ru.lizzzi.sashacakes;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import ru.lizzzi.sashacakes.data.ItemConract;
import ru.lizzzi.sashacakes.data.ItemDbHelper;

public class ItemFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public Integer q = 0 ;
    private String qs;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ItemDbHelper mDbHelper;


    public ItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemFragment newInstance(String param1, String param2) {
        ItemFragment fragment = new ItemFragment();
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
        final View v = inflater.inflate(R.layout.fragment_item, container, false);

        final TextView name = ((TextView) v.findViewById(R.id.name_item));
        final ImageView imgpic = ((ImageView) v.findViewById(R.id.image_item));
        final TextView about = ((TextView) v.findViewById(R.id.about_item));
        final TextView price = ((TextView) v.findViewById(R.id.price_item));
        final TextView quantity = ((TextView) v.findViewById(R.id.quantity));

        ImageButton minus = ((ImageButton) v.findViewById(R.id.minus));
        ImageButton plus = ((ImageButton) v.findViewById(R.id.plus));
        ImageButton sendorder = ((ImageButton) v.findViewById(R.id.sendorder));

        qs = String.valueOf(q);
        quantity.setText(qs);

        final Bundle bundle = getArguments();
        final String ri = bundle.getString("tag");
        mDbHelper = new ItemDbHelper(getContext());
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] columns = new  String[]{ItemConract.ItemList.COLUMN_NAME, ItemConract.ItemList.COLUMN_ABOUT, ItemConract.ItemList.COLUMN_PRICE, ItemConract.ItemList.COLUMN_QUANTITY};
        Cursor cursor = db.query(ItemConract.ItemList.TABLE_NAME,
                columns,
                ItemConract.ItemList.COLUMN_NAME + "= '" + ri + "'",
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        getActivity().setTitle(cursor.getString(cursor.getColumnIndex(ItemConract.ItemList.COLUMN_NAME)));
        name.setText(cursor.getString(cursor.getColumnIndex(ItemConract.ItemList.COLUMN_NAME)));
        about.setText(cursor.getString(cursor.getColumnIndex(ItemConract.ItemList.COLUMN_ABOUT)));
        price.setText(cursor.getString(cursor.getColumnIndex(ItemConract.ItemList.COLUMN_PRICE)));
        quantity.setText(cursor.getString(cursor.getColumnIndex(ItemConract.ItemList.COLUMN_QUANTITY)));
        q = Integer.valueOf(cursor.getString(cursor.getColumnIndex(ItemConract.ItemList.COLUMN_QUANTITY)));


        //Toast.makeText(getContext(), name, Toast.LENGTH_SHORT).show();

        if (bundle != null) {
            if (ri.equals("Капкейк")) {
                imgpic.setImageResource(R.drawable.cupcake_big);
            }
            if (ri.equals("Торт")) {
                imgpic.setImageResource(R.drawable.cake);
            }
            if (ri.equals("Детский торт")) {
                imgpic.setImageResource(R.drawable.childrens_cake);
            }
            if (ri.equals("Чизкейк")) {
                imgpic.setImageResource(R.drawable.cheesecake);
            }
            if (ri.equals("Торт \"Птичье молоко\"")) {
                imgpic.setImageResource(R.drawable.cakepigeonsmilk);
            }
            if (ri.equals("Ярусный торт")) {
                imgpic.setImageResource(R.drawable.tieredcake);
            }
            if (ri.equals("Медовик")) {
                imgpic.setImageResource(R.drawable.honey_cake);
            }
            if (ri.equals("Зефир")) {
                imgpic.setImageResource(R.drawable.zephyr);
            }
            if (ri.equals("Кейк-попсы")) {
                imgpic.setImageResource(R.drawable.cakepops);
            }
            if (ri.equals("Вафельные трубочки")) {
                imgpic.setImageResource(R.drawable.waferrolls);
            }
            if (ri.equals("Пирожное \"Картошка\"")) {
                imgpic.setImageResource(R.drawable.potatocake);
            }
            if (ri.equals("Торт для мужчины")) {
                imgpic.setImageResource(R.drawable.cakeformen);
            }
        }

        minus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if ( q  > 0){
                    q = q - 1;
                }else {
                    q = 0;
                }
                String qs = String.valueOf(q);
                quantity.setText(qs);
            }
        });

        plus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                q = q + 1 ;
                String qs = String.valueOf(q);
                quantity.setText(qs);
            }
        });

        sendorder.setOnClickListener(new View.OnClickListener(){
          @Override
            public void onClick(View v){
              ContextThemeWrapper  ctw = new ContextThemeWrapper(getContext(), R.style.ThemeOverlay_AppCompat_Dark);
              AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
              if (q == 0){
                  alertDialogBuilder.setMessage("Вы не выбрали количество!");
              }else{

                  ContentValues values = new ContentValues();
                  values.put(ItemConract.ItemList.COLUMN_QUANTITY, q);
                  db.update(ItemConract.ItemList.TABLE_NAME,
                          values,
                          ItemConract.ItemList.COLUMN_NAME + "= ?", new String[]{ri});
                  BadgeShow();
                  alertDialogBuilder.setMessage("Заказ добавлен в корзину");
              }

              alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {
                  }
              });
              AlertDialog alertDialog = alertDialogBuilder.create();
              alertDialog.show();
          }
        });
        cursor.close();
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

    public void BadgeShow () {

        final SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int quantity = 0;
        Cursor cursor = db.query(ItemConract.ItemList.TABLE_NAME,
                new String[] {"SUM(" + ItemConract.ItemList.COLUMN_QUANTITY + ")"},
                null, null, null, null, null);
        if (cursor.moveToFirst()) {
            quantity = cursor.getInt(0);
        }

        ((ru.lizzzi.sashacakes.BottomNavigationView) getActivity().findViewById(R.id.navigation)).setBadgeViewNumberByPosition(2,quantity);
        cursor.close();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
