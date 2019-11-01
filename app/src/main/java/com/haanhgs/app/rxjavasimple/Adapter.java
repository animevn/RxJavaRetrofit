package com.haanhgs.app.rxjavasimple;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.haanhgs.app.rxjavasimple.model.ListHour;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<ListHour> list;
    private Context context;

    public Adapter(Context context, List<ListHour> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_main_recycler_item, parent, false);
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
        private ImageView ivIcon;

        private void initViews(View view){
            tvMax = view.findViewById(R.id.tvMax);
            tvTime = view.findViewById(R.id.tvTime);
            tvMin = view.findViewById(R.id.tvMin);
            ivIcon = view.findViewById(R.id.ivIcon);
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);
        }

        public void bindHolder(ListHour listHour){
            tvTime.setText(Repo.convertEpocToDate(listHour.getDt()));
            tvMax.setText(String.valueOf(listHour.getMain().getTempMax()));
            tvMin.setText(String.valueOf(listHour.getMain().getTempMin()));
            String img = listHour.getWeather().get(0).getIcon();
            ivIcon.setImageResource(Repo.getId(context, img));
        }
    }
}
