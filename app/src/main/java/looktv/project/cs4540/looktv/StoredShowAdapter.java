package looktv.project.cs4540.looktv;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.renderscript.Sampler;
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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;

import looktv.project.cs4540.looktv.models.StoredShowModel;
import looktv.project.cs4540.looktv.utils.NetworkUtils;

public class StoredShowAdapter extends RecyclerView.Adapter<StoredShowAdapter.TVShowHolder> {

    ArrayList<StoredShowModel> allStoredShows;
    Context context;

    public StoredShowAdapter(ArrayList<StoredShowModel> allStoredShows, Context context) {
        this.allStoredShows = allStoredShows;
        this.context = context;
    }

    @NonNull
    @Override
    public TVShowHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.stored_show, parent, shouldAttachToParentImmediately);
        StoredShowAdapter.TVShowHolder viewHolder = new StoredShowAdapter.TVShowHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TVShowHolder tvShowHolder, int i) {
        tvShowHolder.bind(i);
    }

    @Override
    public int getItemCount() {
        if (allStoredShows != null) {
            return allStoredShows.size();
        } else {
            return 0;
        }
    }

    public class TVShowHolder extends RecyclerView.ViewHolder {

        ImageView poster;
        TextView showName;

        public TVShowHolder(@NonNull View itemView) {
            super(itemView);
            poster = (ImageView) itemView.findViewById(R.id.stored_show_poster);
//            showName = (TextView) itemView.findViewById(R.id.stored_show_name);
        }

        public void bind(final int i) {
            final StoredShowModel model = allStoredShows.get(i);
//            showName.setText(model.getName());

            if (model.getUrl() == "null") {
                poster.setImageResource(R.drawable.ic_looktv_home);
            } else {

                Picasso.get()
                        .load(model.getUrl())
                        .placeholder(R.drawable.ic_looktv_home)
                        .error(R.drawable.ic_looktv_notificataion)
                        .into(poster);

                poster.setOnLongClickListener(new View.OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View v) {

                        // Notification
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                        String title = model.getName();
                        String textContent = "LookTv: " + title + " has been removed from your list.";
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "LookTV")
                                .setSmallIcon(R.drawable.ic_looktv_account)
                                .setContentTitle(title)
                                .setContentText(textContent)
                                .setPriority(NotificationCompat.PRIORITY_HIGH);

                        notificationManager.notify(Integer.parseInt(allStoredShows.get(i).getId()), mBuilder.build());

                        StoredShowModel storedShow = new StoredShowModel(model.getId(), model.getName(), model.getUrl());

                        //push to database
                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                        DatabaseReference mDatabase = db.getReference("shows");
                        mDatabase.child(storedShow.getId()).removeValue();

                        StoredShowModel index = null;
                        for (StoredShowModel x : allStoredShows) {
                            if (x.getId() == storedShow.getId()) {
                                index = x;
                            }
                        }

                        allStoredShows.remove(index);
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), allStoredShows.size());
                        notifyDataSetChanged();
                        return true;
                    }
                });
            }
        }
    }
}

