package ua.hanasaka.testtaskmolodykh;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Adapter for recyclerview using List<List<String>> data
 * @author  Oleksandr Molodykh
 */
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

    /**
     * alignment data and setting it to view
     * @param holder
     * @param position
     */
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

    /**
     * @return count of listData items (rows of quandl response)
     */
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
            //finding view elements
            date = (TextView) itemView.findViewById(R.id.date);
            content = (TextView) itemView.findViewById(R.id.content);
        }
    }
}
