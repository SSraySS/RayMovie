package me.synology.ssray73.raymovie;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    private String movieId;
    private GridLayout mGridLayout;
    private JSONObject movieDetails;

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);


//        Intent intent = getActivity().getIntent();
//        if(intent != null){
//            if (intent.hasExtra(Intent.EXTRA_TEXT)) {
//                movieId = intent.getStringExtra(Intent.EXTRA_TEXT);
//                TextView titleTextView = (TextView) rootView.findViewById(R.id.detail_movie_title);
//                titleTextView.setText(posterUrl);
//
//                ImageView posterImageView = (ImageView) rootView.findViewById(R.id.detail_movie_poster);
//                if(posterImageView == null) {
//                    posterImageView = new ImageView(getActivity());
//                }
//                Picasso.with(getActivity()) //
//                        .load(posterUrl)
//                        .into(posterImageView);
//            }
//        }

        return rootView;
    }


    public void fetchDetails() {
        FetchMovieDetailsTask movieTask = new FetchMovieDetailsTask();
        movieTask.execute(movieId);
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra(Intent.EXTRA_TEXT)) {
                movieId = intent.getStringExtra(Intent.EXTRA_TEXT);
            }
        }
        fetchDetails();


    }

    public class FetchMovieDetailsTask extends AsyncTask<String, Void, MovieDetail> {
        private final String LOG_TAG = FetchMovieDetailsTask.class.getSimpleName();


        final String MOVIE_ID = "id";
        final String MOVIE_TITLE = "original_title";
        final String MOVIE_POSTER_PATH = "poster_path";
        final String MOVIE_OVERVIEW = "overview";
        final String MOVIE_RELEASE_DATE = "release_date";
        final String MOVIE_VOTE_AVERAGE = "vote_average";
        final String MOVIE_VOTE_COUNT = "vote_count";


        private MovieDetail getMovieDetailFromJson(String jsonStr) throws JSONException {

            JSONObject jsonObject = new JSONObject(jsonStr);
//String movieId, String movieTitle, String poster_url, String release, String rate, String desc)
            return new MovieDetail(jsonObject.getString(MOVIE_ID),
                    jsonObject.getString(MOVIE_TITLE),
                    jsonObject.getString(MOVIE_POSTER_PATH),
                    jsonObject.getString(MOVIE_RELEASE_DATE),
                    jsonObject.getString(MOVIE_VOTE_AVERAGE),
                    jsonObject.getString(MOVIE_OVERVIEW));
        }



        @Override
        protected MovieDetail doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieJsonStr = null;


            final String TMDB_BASE_URL = "http://api.themoviedb.org/3/movie/";
            final String KEY_PARAM = "api_key";

//            String type = "popularity.desc"; //vote_average.desc

            String id = params[0];


            String API_KEY = "fe48c1c18c1a17e4ab15ed99b02fc412";

            Uri buildUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                    .appendPath(id)
                    .appendQueryParameter(KEY_PARAM, API_KEY).build();

            try {
                URL url = new URL(buildUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                Log.d(LOG_TAG, "DETAIL API: " + url.toString());

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }


                if (buffer.length() == 0) {
                    return null;
                }
                movieJsonStr = buffer.toString();

                Log.v(LOG_TAG, "INDV JSON: " + movieJsonStr);

            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "Error: " + e);
                e.printStackTrace();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error: " + e);
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, e.getMessage(), e);
                        e.printStackTrace();
                    }
                }
            }

            try {
                return getMovieDetailFromJson(movieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(MovieDetail movieDetail) {
            TextView movieTitle = (TextView) getActivity().findViewById(R.id.detail_movie_title);
            movieTitle.setText(movieDetail.getMovieTitle());

            ImageView imageView = (ImageView) getActivity().findViewById(R.id.detail_movie_poster);


            Log.d(LOG_TAG, "DETAILS URL: " + movieDetail.getPoster_url());
            Glide.with(getActivity()) //
                    .load(movieDetail.getPoster_url())
                    .into(imageView);

            TextView release = (TextView) getActivity().findViewById(R.id.detail_movie_release);
            release.setText(movieDetail.getRelease());

            TextView rate = (TextView) getActivity().findViewById(R.id.detail_movie_rate);
            rate.setText(movieDetail.getRate());

            TextView desc = (TextView) getActivity().findViewById(R.id.detail_movie_desc);
            desc.setText(movieDetail.getDesc());


            super.onPostExecute(movieDetail);
        }
    }
}
