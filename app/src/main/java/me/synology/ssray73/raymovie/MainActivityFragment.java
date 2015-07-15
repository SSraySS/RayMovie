package me.synology.ssray73.raymovie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185/";

    GridviewThumbnailAdapter movieAdapter;
    private ProgressBar mProgressBar;
    private GridView mGridView;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.debug_refresh, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.v(LOG_TAG, "----------------------------Action menu");
        int id = item.getItemId();
        if (id == R.id.action_referesh){
            updateMovie();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String[] urls = {
                "http://image.tmdb.org/t/p/w185//uXZYawqUsChGSj54wcuBtEdUJbh.jpg",
                "http://image.tmdb.org/t/p/w185//5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg",
                "http://image.tmdb.org/t/p/w185//q0R4crx2SehcEEQEkYObktdeFy.jpg",
                "http://image.tmdb.org/t/p/w185//kqjL17yufvn9OVLyXYpvtyrFfak.jpg",
                "http://image.tmdb.org/t/p/w185//A7HtCxFe7Ms8H7e7o2zawppbuDT.jpg",
                "http://image.tmdb.org/t/p/w185//aBBQSC8ZECGn6Wh92gKDOakSC8p.jpg",
                "http://image.tmdb.org/t/p/w185//saF3HtAduvrP9ytXDxSnQJP3oqx.jpg",
                "http://image.tmdb.org/t/p/w185//qey0tdcOp9kCDdEZuJ87yE3crSe.jpg",
                "http://image.tmdb.org/t/p/w185//aMEsvTUklw0uZ3gk3Q6lAj6302a.jpg",
                "http://image.tmdb.org/t/p/w185//oAISjx6DvR2yUn9dxj00vP8OcJJ.jpg",
                "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
                "http://image.tmdb.org/t/p/w185//hGRfWcy1HRGbsjK6jF7NILmqmFT.jpg",
                "http://image.tmdb.org/t/p/w185//qrFwjJ5nvFnpBCmXLI4YoeHJNBH.jpg",
                "http://image.tmdb.org/t/p/w185//xxX0v4vyfVc3Z8DEsbLJODnMOfQ.jpg",
                "http://image.tmdb.org/t/p/w185//8uREeoTbutetwgjB2jGAotcehuG.jpg",
                "http://image.tmdb.org/t/p/w185//t90Y3G8UGQp0f0DrP60wRu9gfrH.jpg",
                "http://image.tmdb.org/t/p/w185//gCBw0AQDhlo0bNetkjsSRWzrxpW.jpg",
                "http://image.tmdb.org/t/p/w185//rDycdoAXtBb7hoWlBpZqbwk2F44.jpg",
                "http://image.tmdb.org/t/p/w185//2i0JH5WqYFqki7WDhUW56Sg0obh.jpg",
                "http://image.tmdb.org/t/p/w185//9gm3lL8JMTTmc3W4BmNMCuRLdL8.jpg"

        };

        String[] ids = {
                "135397",
                "87101",
                "211672",
                "76341",
                "214756",
                "262500",
                "198184",
                "254128",
                "76757",
                "207703",
                "157336",
                "177572",
                "122917",
                "287424",
                "228161",
                "99861",
                "238713",
                "150540",
                "150689",
                "118340"
        };

        ArrayList<String> movieUrls = new ArrayList<String>(Arrays.asList(urls));
        ArrayList<String> movieIds = new ArrayList<String>(Arrays.asList(ids));
        movieAdapter = new GridviewThumbnailAdapter(
                getActivity(), movieUrls, movieIds);


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        mGridView = (GridView) rootView.findViewById(R.id.gridview_movie);
        mGridView.setAdapter(movieAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), position + " - " + movieAdapter.getMovieId(position), Toast.LENGTH_SHORT).show();

                String movieId = movieAdapter.getMovieId(position);
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class).putExtra(Intent.EXTRA_TEXT, movieId);
                startActivity(intent);
            }
        });


        return rootView;
    }


    public void updateMovie(){
            FetchMovieTask movieTask = new FetchMovieTask();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sort_pref = prefs.getString("movie_sort_order", "popularity.desc");
            movieTask.execute(sort_pref);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovie();
    }

    public class FetchMovieTask extends AsyncTask<String, Void, JSONArray>{

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        final String MOVIE_RESULT = "results";
        final String MOVIE_ID = "id";
        final String MOVIE_TITLE = "original_title";
        final String MOVIE_POSTER_PATH = "poster_path";
        final String MOVIE_OVERVIEW = "overview";
        final String MOVIE_RELEASE_DATE = "release_date";
        final String MOVIE_VOTE_AVERAGE = "vote_average";
        final String MOVIE_VOTE_COUNT = "vote_count";


        private JSONArray getMovieDataFromJson(String movieJsonStr) throws JSONException {




            JSONObject moviesJson = new JSONObject(movieJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray(MOVIE_RESULT);

//            Log.d(LOG_TAG, "Movies list contains: " + moviesArray.length());

            for(int i = 0; i < moviesArray.length(); i ++){
                JSONObject movie = moviesArray.getJSONObject(i);
                movie.put(MOVIE_POSTER_PATH, POSTER_BASE_URL + movie.getString(MOVIE_POSTER_PATH));
//                Log.d(LOG_TAG, "ID: " + movie.getString(MOVIE_ID) + " path: " + "http://image.tmdb.org/t/p/w185/" + movie.getString(MOVIE_POSTER_PATH));


            }
            if (moviesArray.length() > 0){
                return moviesArray;
            }
            return null;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            mGridView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected JSONArray doInBackground(String... params) {

            if (params.length ==0 ){
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieJsonStr = null;



            final String TMDB_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
            final String SORT_PARAM = "sort_by";
            final String KEY_PARAM = "api_key";

//            String type = "popularity.desc"; //vote_average.desc

            String type = params[0];


            String API_KEY = "fe48c1c18c1a17e4ab15ed99b02fc412";

            Uri buildUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_PARAM, type)
                    .appendQueryParameter(KEY_PARAM, API_KEY).build();

            try {
                URL url = new URL(buildUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(inputStream == null){
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null){
                    buffer.append(line + "\n");
                }


                if(buffer.length()==0){
                    return null;
                }
                movieJsonStr = buffer.toString();

//                Log.v(LOG_TAG, "Json: " + movieJsonStr);

            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "Error: " + e);
                e.printStackTrace();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error: " + e);
                e.printStackTrace();
            } finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
                if(reader != null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, e.getMessage(), e);
                        e.printStackTrace();
                    }
                }
            }

            try {
                return getMovieDataFromJson(movieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            if(jsonArray != null){
                try {
                    ArrayList<String> urls = new  ArrayList<String>();
                    ArrayList<String> movieIds = new ArrayList<String>();
                    for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject movieJson = (JSONObject) jsonArray.get(i);
                        urls.add(movieJson.getString(MOVIE_POSTER_PATH));
                        movieIds.add(movieJson.getString(MOVIE_ID));
                    }
//                    movieAdapter = new GridviewThumbnailAdapter(
//                            getActivity(), urls);
                    movieAdapter.update(urls, movieIds);
                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                }

            }
            mGridView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }

}

