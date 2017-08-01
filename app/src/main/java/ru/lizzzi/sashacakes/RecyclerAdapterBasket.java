package ru.lizzzi.sashacakes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class RecyclerAdapterBasket extends RecyclerView.Adapter<RecyclerAdapterBasket.ViewHolder>{

    private final ArrayList<Product> products;
    private Product p;
    private Integer temp = 0;
    private ListenerActivity mlistener;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTextViewName;
        private TextView mTextViewPrice;
        private TextView mTextViewQuantity;
        private ImageView mImageView;
        private ImageButton minus_item;
        private ImageButton plus_item;
        private ImageButton delete_item;

        public ViewHolder(View v) {
            super(v);
            mTextViewName = (TextView) v.findViewById(R.id.item_name);
            mTextViewPrice = (TextView) v.findViewById(R.id.item_price);
            mTextViewQuantity = (TextView) v.findViewById(R.id.item_quantity);

            mImageView = (ImageView) v.findViewById(R.id.item_image);

            minus_item = (ImageButton) v.findViewById(R.id.minus_item);
            plus_item = (ImageButton) v.findViewById(R.id.plus_item);
            delete_item = (ImageButton) v.findViewById(R.id.delete_item);

        }
    }

    // Конструктор
    public RecyclerAdapterBasket(ArrayList<Product> products, ListenerActivity listener) {
        this.products = products;
        mlistener = listener;
    }

    // Создает новые views (вызывается layout manager-ом)
    @Override
    public RecyclerAdapterBasket.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_basket, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Заменяет контент отдельного view (вызывается layout manager-ом)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.mTextViewName.setText(products.get(position).name);
        holder.mTextViewPrice.setText(products.get(position).price);
        holder.mTextViewQuantity.setText(String.valueOf(products.get(position).quantity));
        holder.mImageView.setImageResource(products.get(position).image);
        holder.minus_item.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                p = products.get(position);
                temp = p.quantity;
                if (temp > 1) {
                    temp = temp - 1;
                } else {
                    temp = 1;
                }
                p.quantity = temp;
                holder.mTextViewQuantity.setText(String.valueOf(products.get(position).quantity));
                mlistener.UpdateFileOder(p.name, p.quantity);
            }
        });
        holder.plus_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p = products.get(position);
                temp = p.quantity;
                temp = temp + 1;
                p.quantity = temp;
                holder.mTextViewQuantity.setText(String.valueOf(products.get(position).quantity));
                mlistener.UpdateFileOder(p.name, p.quantity);
            }
        });
        holder.delete_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p = products.get(position);
                if(mlistener!= null){
                    mlistener.Remove(p.name, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}