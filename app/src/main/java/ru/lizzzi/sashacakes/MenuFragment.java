package ru.lizzzi.sashacakes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ru.lizzzi.sashacakes.data.ItemConract;
import ru.lizzzi.sashacakes.data.ItemDbHelper;
import ru.lizzzi.sashacakes.views.FastScrollRecyclerView;

public class MenuFragment extends Fragment {


    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private FastScrollRecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ItemDbHelper mDbHelper;
    private ArrayList<String> Item_list_name = new ArrayList<>();
    private ArrayList<String> Item_list_price = new ArrayList<>();

    public MenuFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MenuFragment newInstance(int columnCount) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        mDbHelper = new ItemDbHelper(getContext());
        CreateItemList();
        mRecyclerView = (FastScrollRecyclerView) view.findViewById(R.id.recyclerview);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerAdapterMenu(Item_list_name, Item_list_price);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Fragment fragment = null;
                        Class fragmentClass;
                        fragmentClass = ItemFragment.class;
                        ItemFragment yfc = new ItemFragment();
                        Bundle bundle = new Bundle();
                        String str = Item_list_name.get(position);
                        bundle.putString("tag", str);
                        yfc.setArguments(bundle);


                        try {
                            fragment = (Fragment) fragmentClass.newInstance();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction ft = fragmentManager.beginTransaction();
                        ft.replace(R.id.content_main, yfc);
                        ft.addToBackStack(null);

                        ft.commit();

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name

    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        getActivity().setTitle(R.string.app_name);
    }

    private void CreateItemList(){
        // Подумать на досуге о упрощении запроса до одного
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Item_list_name.clear();
        Item_list_price.clear();

        String[] columns = new  String[]{ItemConract.ItemList._ID, ItemConract.ItemList.COLUMN_NAME};
        Cursor cursor = db.query(ItemConract.ItemList.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null);
        if (cursor !=null && cursor.moveToFirst()){
            do {
                String name = "";
                for (String cn : cursor.getColumnNames()) {
                    name = cursor.getString(cursor.getColumnIndex(cn));
                }
                Item_list_name.add(name);
            }while (cursor.moveToNext());
        }
        cursor.close();

        columns = new  String[]{ItemConract.ItemList._ID, ItemConract.ItemList.COLUMN_PRICE};
        cursor = db.query(ItemConract.ItemList.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null);
        if (cursor !=null && cursor.moveToFirst()){
            do {
                String name = "";
                for (String cn : cursor.getColumnNames()) {
                    name = cursor.getString(cursor.getColumnIndex(cn));
                }
                Item_list_price.add(name);
            }while (cursor.moveToNext());
        }
        cursor.close();
    }
}

