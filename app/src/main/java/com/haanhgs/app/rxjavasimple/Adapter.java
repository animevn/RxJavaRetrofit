package com.haanhgs.app.rxjavasimple;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<Android> list;

    public Adapter(List<Android> list) {
        this.list = list;
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

        private TextView tvName;
        private TextView tvVer;
        private TextView tvApi;

        private void initViews(View view){
            tvApi = view.findViewById(R.id.tvAPI);
            tvName = view.findViewById(R.id.tvName);
            tvVer = view.findViewById(R.id.tvVersion);
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);
        }

        public void bindHolder(Android android){
            tvApi.setText(android.getApi());
            tvName.setText(android.getName());
            tvVer.setText(android.getVer());
        }
    }
}
