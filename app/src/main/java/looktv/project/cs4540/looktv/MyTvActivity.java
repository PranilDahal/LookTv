package looktv.project.cs4540.looktv;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import looktv.project.cs4540.looktv.models.StoredShowModel;

public class MyTvActivity extends AppCompatActivity {

    private StoredShowAdapter storedShowAdapter;
    private RecyclerView showRecyclerView;
    private ArrayList<StoredShowModel> showList = new ArrayList<StoredShowModel>();
    private ProgressBar progressBar;
    private DatabaseReference mShowReference;
    private ValueEventListener showListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mytv_activity);

        showRecyclerView = (RecyclerView) findViewById(R.id.stored_show_recycler);
        progressBar = (ProgressBar) findViewById(R.id.progress2);

        storedShowAdapter = new StoredShowAdapter(showList, this);
        showRecyclerView.setAdapter(storedShowAdapter);
        showRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager llm = new LinearLayoutManager(this);
        showRecyclerView.setLayoutManager(llm);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_mytv);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.tv_app_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // start dowloading info from firebase
        new DownloadTask().execute("start");

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        menu.findItem(R.id.app_search_bar).setVisible(false);
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_browse:
                    showHome();
                    return true;
                case R.id.navigation_mytv:
                    return true;
            }
            return false;
        }
    };

    private void showHome() {
        Intent homeIntent = new Intent(this, MainActivity.class);
        startActivity(homeIntent);
        this.overridePendingTransition(0, 0);
    }

    private void downloadStoredShows() {

        mShowReference = FirebaseDatabase.getInstance().getReference().child("shows");

        mShowReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                progressBar.setVisibility(View.VISIBLE);

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    StoredShowModel show = snapshot.getValue(StoredShowModel.class);
                    System.out.println(show.toString());
                    showList.add(show);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            storedShowAdapter.notifyDataSetChanged();
                        }
                    });

                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.print("\n SOMEHTING BAD HAPPENED HERE \n");
            }
        });

    }

    class DownloadTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... params) {
            downloadStoredShows();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}
