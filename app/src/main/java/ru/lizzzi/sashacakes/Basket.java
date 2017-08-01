package ru.lizzzi.sashacakes;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

import ru.lizzzi.sashacakes.data.ItemConract;
import ru.lizzzi.sashacakes.data.ItemDbHelper;

@CoordinatorLayout.DefaultBehavior(BottomNavigationViewBehavior_NH.class)

public class Basket extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayList<Product> products = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public BottomNavigationView mBottomNav;

    private OnFragmentInteractionListener mListener;
    private String qs;
    private ItemDbHelper mDbHelper;

    private SharedPreferences mMyContacts;
    public static final String APP_PREFERENCES = "mycontacts";
    public static final String APP_PREFERENCES_Telephone = "Telephone";

    public Basket() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Basket.
     */
    // TODO: Rename and change types and number of parameters
    public static Basket newInstance(String param1, String param2) {
        Basket fragment = new Basket();
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
        final View v = inflater.inflate(R.layout.fragment_basket, container, false);

        mDbHelper = new ItemDbHelper(getContext());

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_basket);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        Button send_order = ((Button) v.findViewById(R.id.send_order));

        final TableLayout basket_menu = ((TableLayout) v.findViewById(R.id.basket_menu));
        final TextView basket_empty = (TextView) v.findViewById(R.id.basket_empty);
        basket_empty.setVisibility(View.INVISIBLE);

        fillData();
        Order_Sum(v);
        mAdapter = new RecyclerAdapterBasket(products, new ListenerActivity() {
            @Override
            public void Remove(String name, int position) {
                UFO(name, 0);
                products.remove(position);
                mAdapter.notifyItemRemoved(position);
                mAdapter.notifyItemRangeChanged(position, getItemCount());
                if (products.isEmpty()) {
                    basket_empty.setVisibility(View.VISIBLE);
                    basket_menu.setVisibility(View.INVISIBLE);
                }
                Order_Sum(v);
                BadgeShow ();
            }

            @Override
            public void UpdateFileOder(String name, int quantity) {
                UFO(name, quantity);
                Order_Sum(v);
                BadgeShow ();
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        if (products.isEmpty()) {
            basket_empty.setVisibility(View.VISIBLE);
            basket_menu.setVisibility(View.INVISIBLE);
        }


        send_order.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                String str_Telephone = mMyContacts.getString(APP_PREFERENCES_Telephone, "");
                if (str_Telephone.length() < 11){

                    ContextThemeWrapper ctw = new ContextThemeWrapper(getContext(), R.style.ThemeOverlay_AppCompat_Dark);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
                    alertDialogBuilder.setMessage("Для отправки заказа Вам необходимо заполнить телефон, чтобы мы могли связаться с вами.");
                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                    Fragment fragment = null;
                    Class fragmentClass = null;
                    fragmentClass = AboutMe.class;
                    FragmentManager fragmentManager = getFragmentManager();
                    try {
                        fragment = (Fragment) fragmentClass.newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.content_main, fragment);
                    ft.commit();
                }else {
                    // по хорошему тут надо формировать заказ а не тупо удалять значения >0, но пока этого хватает -)
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(ItemConract.ItemList.COLUMN_QUANTITY, 0);
                    db.update(ItemConract.ItemList.TABLE_NAME,
                            values,
                            ItemConract.ItemList.COLUMN_QUANTITY + "> 0 ", null);

                    products.clear();
                    mAdapter.notifyDataSetChanged();
                    BadgeShow ();
                    if (products.isEmpty()){
                        basket_empty.setVisibility(View.VISIBLE);
                        basket_menu.setVisibility(View.INVISIBLE);
                    }
                    Toast.makeText(getActivity(), "Заказ отправлен", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    // генерируем данные для адаптера
    void fillData() {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        products.clear();

        String[] columns = new  String[]{ItemConract.ItemList._ID, ItemConract.ItemList.COLUMN_NAME, ItemConract.ItemList.COLUMN_PRICE, ItemConract.ItemList.COLUMN_QUANTITY};
        Cursor cursor = db.query(ItemConract.ItemList.TABLE_NAME,
                columns,
                ItemConract.ItemList.COLUMN_QUANTITY + "> 0 ",
                null,
                null,
                null,
                null);
        if (cursor !=null && cursor.moveToFirst()){
            do {
                String name = cursor.getString(cursor.getColumnIndex(ItemConract.ItemList.COLUMN_NAME));
                String price = cursor.getString(cursor.getColumnIndex(ItemConract.ItemList.COLUMN_PRICE));
                Integer quantity = Integer.valueOf(cursor.getString(cursor.getColumnIndex(ItemConract.ItemList.COLUMN_QUANTITY)));
                int image = 0;
                if (name.equals("Капкейк")) {
                    image = R.drawable.cupcake_big;
                }
                if (name.equals("Торт")) {
                    image = R.drawable.cake;
                }
                if (name.equals("Детский торт")) {
                    image = R.drawable.childrens_cake;
                }
                if (name.equals("Чизкейк")) {
                    image = R.drawable.cheesecake;
                }
                if (name.equals("Торт \"Птичье молоко\"")) {
                    image = R.drawable.cakepigeonsmilk;
                }
                if (name.equals("Ярусный торт")) {
                    image = R.drawable.tieredcake;
                }
                if (name.equals("Медовик")) {
                    image = R.drawable.honey_cake;
                }
                if (name.equals("Зефир")) {
                    image = R.drawable.zephyr;
                }
                if (name.equals("Кейк-попсы")) {
                    image = R.drawable.cakepops;
                }
                if (name.equals("Вафельные трубочки")) {
                    image = R.drawable.waferrolls;
                }
                if (name.equals("Пирожное \"Картошка\"")) {
                    image = R.drawable.potatocake;
                }
                if (name.equals("Торт для мужчин")) {
                    image = R.drawable.cakeformen;
                }
                products.add(new Product(name, price, image, quantity));
            }while (cursor.moveToNext());
        }
        cursor.close();
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

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    void UFO (String name, int quantity){

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ItemConract.ItemList.COLUMN_QUANTITY, quantity);
        db.update(ItemConract.ItemList.TABLE_NAME,
                values,
                ItemConract.ItemList.COLUMN_NAME + "= ?", new String[]{name});
    }
    public int getItemCount(){
        return products.size();
    }

    void Order_Sum(View v){
        TextView sum_oder = ((TextView) v.findViewById(R.id.sum_order));
        int quantity;
        int quantity_sum = 0;
        int price;

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String[] columns = new  String[]{ItemConract.ItemList._ID, ItemConract.ItemList.COLUMN_PRICE, ItemConract.ItemList.COLUMN_QUANTITY};
        Cursor cursor = db.query(ItemConract.ItemList.TABLE_NAME,
                columns,
                ItemConract.ItemList.COLUMN_QUANTITY + "> 0 ",
                null,
                null,
                null,
                null);
        if (cursor !=null && cursor.moveToFirst()){
            do {
                price = Integer.valueOf(cursor.getString(cursor.getColumnIndex(ItemConract.ItemList.COLUMN_PRICE)));
                quantity = Integer.valueOf(cursor.getString(cursor.getColumnIndex(ItemConract.ItemList.COLUMN_QUANTITY)));
                quantity_sum = quantity_sum + (quantity * price);
            }while (cursor.moveToNext());
        }
        cursor.close();


        //Разделение по разрядом сумму заказа
        DecimalFormat convert_per = new DecimalFormat();
        convert_per.setGroupingSize(3);
        String str = convert_per.format(quantity_sum);
        sum_oder.setText(str);
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setTitle(R.string.Basket);
        mBottomNav = ((ru.lizzzi.sashacakes.BottomNavigationView) getActivity().findViewById(R.id.navigation));
        final CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mBottomNav.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior_NH());
    }

    @Override
    public void onPause(){
        super.onPause();

        mBottomNav = ((ru.lizzzi.sashacakes.BottomNavigationView) getActivity().findViewById(R.id.navigation));
        final CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mBottomNav.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());
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
    }
}