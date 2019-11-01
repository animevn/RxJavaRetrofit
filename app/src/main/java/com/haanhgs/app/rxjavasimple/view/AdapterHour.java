package com.haanhgs.app.rxjavasimple.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.haanhgs.app.rxjavasimple.R;
import com.haanhgs.app.rxjavasimple.model.ListHour;
import com.haanhgs.app.rxjavasimple.repo.Repo;

import java.util.List;
import java.util.Locale;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterHour extends RecyclerView.Adapter<AdapterHour.ViewHolder> {

    private List<ListHour> list;
    private Context context;

    public AdapterHour(Context context, List<ListHour> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home_recycler_hour, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindHolder(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvMax;
        private TextView tvMin;
        private TextView tvTime;
        private TextView tvHour;
        private ImageView ivIcon;

        private void initViews(View view){
            tvMax = view.findViewById(R.id.tvMax);
            tvTime = view.findViewById(R.id.tvTime);
            tvHour = view.findViewById(R.id.tvHour);
            tvMin = view.findViewById(R.id.tvMin);
            ivIcon = view.findViewById(R.id.ivIcon);
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);
        }

        public void bindHolder(ListHour listHour){
            tvTime.setText(Repo.convertEpocToDay(listHour.getDt()));
            tvHour.setText(Repo.convertEpocToDayHour(listHour.getDt()));
            tvMax.setText(String.format(Locale.getDefault(), "%.0f", listHour.getMain().getMaxC()));
            tvMin.setText(String.format(Locale.getDefault(), "%.0f", listHour.getMain().getMinC()));
            String img = listHour.getWeather().get(0).getIcon();
            int id = Repo.getId(context, img);
            Glide.with(context).load("").apply(RequestOptions.placeholderOf(id)).into(ivIcon);
        }
    }
}
