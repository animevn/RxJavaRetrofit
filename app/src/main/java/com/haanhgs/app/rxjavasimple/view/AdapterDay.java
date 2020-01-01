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
import com.haanhgs.app.rxjavasimple.model.weather.ListHour;
import com.haanhgs.app.rxjavasimple.repo.Repo;
import java.util.List;
import java.util.Locale;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterDay extends RecyclerView.Adapter<AdapterDay.ViewHolder> {

    private final Context context;
    private final List<ListHour> listDay;

    public AdapterDay(Context context, List<ListHour> listDay) {
        this.context = context;
        this.listDay = listDay;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ViewHolder(inflater.inflate(R.layout.recycler_day, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindHolder(listDay.get(position));
    }

    @Override
    public int getItemCount() {
        return listDay == null ? 0 : listDay.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvDay)
        TextView tvDay;
        @BindView(R.id.tvTemp)
        TextView tvTemp;
        @BindView(R.id.ivIconDay)
        ImageView ivIconDay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindHolder(ListHour listHour) {
            tvDay.setText(Repo.convertEpocToDay(listHour.getDt()));
            String max = String.format(Locale.getDefault(), "%.0f", listHour.getMain().getMaxC());
            String min = String.format(Locale.getDefault(), "%.0f", listHour.getMain().getMinC());
            tvTemp.setText(String.format("%s", min + " " + max));
            String img = listHour.getWeather().get(0).getIcon();
            int id = Repo.getId(context, img);
            Glide.with(context).load("").apply(RequestOptions.placeholderOf(id)).into(ivIconDay);
        }
    }
}
