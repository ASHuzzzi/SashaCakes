package ru.lizzzi.sashacakes;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ru.lizzzi.sashacakes.R;
import ru.lizzzi.sashacakes.views.FastScrollRecyclerView;


public class RecyclerAdapterMenu extends RecyclerView.Adapter<RecyclerAdapterMenu.ViewHolder> implements FastScrollRecyclerView.SectionedAdapter {

    private ArrayList<String> mDataset;
    private ArrayList<String> mPriceset;

    @NonNull
    @Override
    public String getSectionName(int position) {
        return String.valueOf(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;
        private TextView mPrice;
        private ImageView mImageView;


        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.label);
            mImageView = (ImageView) v.findViewById(R.id.icon);
            mPrice = (TextView) v.findViewById(R.id.price);
        }
    }

    public RecyclerAdapterMenu(ArrayList<String> dataset, ArrayList<String> priceset) {
        mDataset = dataset;
        mPriceset = priceset;
    }

    // Создает новые views (вызывается layout manager-ом)
    @Override
    public RecyclerAdapterMenu.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Заменяет контент отдельного view (вызывается layout manager-ом)
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mDataset.get(position));
        holder.mPrice.setText(mPriceset.get(position));
        String s = mDataset.get(position);

        if (s.startsWith("Капкейк")) {
            holder.mImageView.setImageResource(R.drawable.cupcake_big);
        }
        if (s.startsWith("Торт")) {
            holder.mImageView.setImageResource(R.drawable.cake);
        }
        if (s.startsWith("Детский торт")) {
            holder.mImageView.setImageResource(R.drawable.childrens_cake);
        }
        if (s.startsWith("Торт для мужчин")) {
            holder.mImageView.setImageResource(R.drawable.cakeformen);
        }
        if (s.startsWith("Чизкейк")) {
            holder.mImageView.setImageResource(R.drawable.cheesecake);
        }
        if (s.startsWith("Ярусный торт")) {
            holder.mImageView.setImageResource(R.drawable.tieredcake);
        }
        if (s.startsWith("Медовик")) {
            holder.mImageView.setImageResource(R.drawable.honey_cake);
        }
        if (s.startsWith("Зефир")) {
            holder.mImageView.setImageResource(R.drawable.zephyr);
        }
        if (s.startsWith("Кейк-попсы")) {
            holder.mImageView.setImageResource(R.drawable.cakepops);
        }
        if (s.startsWith("Вафельные трубочки")) {
            holder.mImageView.setImageResource(R.drawable.waferrolls);
        }
        if (s.startsWith("Пирожное \"Картошка\"")) {
            holder.mImageView.setImageResource(R.drawable.potatocake);
        }
        if (s.startsWith("Торт \"Птичье молоко\"")) {
            holder.mImageView.setImageResource(R.drawable.cakepigeonsmilk);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}