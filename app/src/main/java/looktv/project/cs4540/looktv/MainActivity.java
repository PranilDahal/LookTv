package looktv.project.cs4540.looktv;

import android.os.AsyncTask;
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
import android.widget.SearchView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import looktv.project.cs4540.looktv.models.SearchResultModel;
import looktv.project.cs4540.looktv.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    private static String searchResults = "";
    private RecyclerView showRecyclerView;
    private SearchResultAdapter showAdapter;
    private ArrayList<SearchResultModel> showList = new ArrayList<SearchResultModel>();

    private ProgressBar progressBar;
    private SearchView searchView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_browse:
                    showHome();
                    return true;
                case R.id.navigation_mytv:
                    showMytv();
                    return true;
                case R.id.navigation_notifications:
                    showNotifs();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.app_search_bar);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                new SearchTask().execute(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showRecyclerView = (RecyclerView) findViewById(R.id.search_page_recycler);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        showAdapter = new SearchResultAdapter(this, showList);
        showRecyclerView.setAdapter(showAdapter);
        showRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager llm = new LinearLayoutManager(this);
        showRecyclerView.setLayoutManager(llm);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    void showHome() {

    }

    void showMytv() {

    }

    void showNotifs() {

    }

    class SearchTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                searchResults = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildURL(params[0]));
                Log.d("MyCode", searchResults);
                populateRecyclerView(searchResults);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
        }
    }

    public void populateRecyclerView(String searchResults) {
        showList = NetworkUtils.parseShows(searchResults);
        showAdapter.allShows = showList;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showAdapter.notifyDataSetChanged();
            }
        });
    }

}