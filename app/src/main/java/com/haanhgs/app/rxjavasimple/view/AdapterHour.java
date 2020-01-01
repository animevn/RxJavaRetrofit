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

public class AdapterHour extends RecyclerView.Adapter<AdapterHour.ViewHolder> {

    private final List<ListHour> list;
    private final Context context;

    public AdapterHour(Context context, List<ListHour> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ViewHolder(inflater.inflate(R.layout.recycler_hour, parent, false));
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

        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.tvHour)
        TextView tvHour;
        @BindView(R.id.tvMin)
        TextView tvMin;
        @BindView(R.id.tvMax)
        TextView tvMax;
        @BindView(R.id.ivIcon)
        ImageView ivIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindHolder(ListHour listHour) {
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
