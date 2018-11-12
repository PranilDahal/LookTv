package looktv.project.cs4540.looktv;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import looktv.project.cs4540.looktv.models.SearchResultModel;
import looktv.project.cs4540.looktv.models.StoredShowModel;

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

        public void bind(final int i) {

            showName.setText(allShows.get(i).getShowName());

            if (allShows.get(i).getAvgRating() == "null") {
                showRating.setText("Ratings Unavailable");
            } else {
                showRating.setText("Average Rating: " + allShows.get(i).getAvgRating());
            }

            if (allShows.get(i).getShowPoster() == "null") {
                poster.setImageResource(R.drawable.ic_looktv_home);
            } else {
                new LoadImageTask().execute(allShows.get(i).getShowPoster());
                poster.setOnLongClickListener(new View.OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View v) {

                        // Notification
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                        String title = allShows.get(i).getShowName();
                        String textContent = "LookTv: " + title + " has been added to your list.";
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "LookTV")
                                .setSmallIcon(R.drawable.ic_looktv_account)
                                .setContentTitle(title)
                                .setContentText(textContent)
                                .setPriority(NotificationCompat.PRIORITY_HIGH);

                        notificationManager.notify(Integer.parseInt(allShows.get(i).getShowId()), mBuilder.build());

                        StoredShowModel storedShow = new StoredShowModel(allShows.get(i).getShowId(), allShows.get(i).getShowName(), allShows.get(i).getShowPoster());

                        //push to database
                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                        DatabaseReference mDatabase = db.getReference("shows");
                        mDatabase.child(storedShow.getId()).setValue(storedShow);

                        return true;
                    }
                });
            }
        }


        private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(String... posterUrl) {
                Bitmap bitMapObject = null;
                for (String curr : posterUrl) {
                    bitMapObject = grabBitmapFromUrl(curr);
                }
                return bitMapObject;
            }

            @Override
            protected void onPostExecute(Bitmap bitMapObj) {
                poster.setImageBitmap(bitMapObj);
            }

        }

    }

    private Bitmap grabBitmapFromUrl(String url) {
        Bitmap map = null;
        InputStream is = null;
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = 1;

        try {
            is = getHttpConnection(url);
            map = BitmapFactory.
                    decodeStream(is, null, bitmapOptions);
            is.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return map;
    }

    private InputStream getHttpConnection(String urlString)
            throws IOException {
        InputStream stream = null;
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();

        try {
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = httpConnection.getInputStream();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stream;
    }

}
