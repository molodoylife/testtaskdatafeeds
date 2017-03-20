package ua.hanasaka.testtaskmolodykh;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class DatafeedAdapter extends RecyclerView.Adapter<DatafeedAdapter.ViewHolder> {

    private List<List<String>> listData;

    public DatafeedAdapter(List<List<String>> listData) {
        this.listData = listData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        List<String> regularRow = listData.get(position);
        String date = regularRow.get(0);
        StringBuilder sbContent = new StringBuilder();
        sbContent.append("o=").append(regularRow.get(1));
        sbContent.append("h=").append(regularRow.get(2));
        sbContent.append("l=").append(regularRow.get(3));
        sbContent.append("c=").append(regularRow.get(3));
        holder.date.setText(date);
        holder.content.setText(sbContent.toString());
    }

    @Override
    public int getItemCount() {
        if (listData == null)
            return 0;
        return listData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
            content = (TextView) itemView.findViewById(R.id.content);
        }
    }
}
