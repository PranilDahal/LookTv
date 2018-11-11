package looktv.project.cs4540.looktv;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import looktv.project.cs4540.looktv.models.SearchResultModel;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.TVShowHolder> {

    Context context;
    ArrayList<SearchResultModel> allShows;

    public SearchResultAdapter(Context context, ArrayList<SearchResultModel> shows) {
        this.context = context;
        this.allShows = shows;
    }

    @NonNull
    @Override
    public SearchResultAdapter.TVShowHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.search_result, parent, shouldAttachToParentImmediately);
        TVShowHolder viewHolder = new TVShowHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultAdapter.TVShowHolder tvShowHolder, int i) {
        tvShowHolder.bind(i);
    }

    @Override
    public int getItemCount() {
        return allShows.size();
    }

    public class TVShowHolder extends RecyclerView.ViewHolder {

        ImageView poster;
        TextView showName;
        TextView showRating;

        public TVShowHolder(@NonNull View itemView) {
            super(itemView);
            poster = (ImageView) itemView.findViewById(R.id.show_poster);
            showName = (TextView) itemView.findViewById(R.id.show_name);
            showRating = (TextView) itemView.findViewById(R.id.show_rating);
        }

        public void bind(int i) {

            showName.setText(allShows.get(i).getShowName());

            if (allShows.get(i).getAvgRating() == "null") {
                showRating.setText("Ratings Unavailable");
            } else {
                showRating.setText("Average Rating: "+allShows.get(i).getAvgRating());
            }
        }
    }
}
