package me.synology.ssray73.raymovie;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

    private final String LOG_TAG = MovieDetailActivityFragment.class.getSimpleName();
    private String movieId;
    private GridLayout mGridLayout;
    private LinearLayout lProgress;
    //    private JSONObject movieDetails;
    private MovieDetail movieDetail;


    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);


        lProgress = (LinearLayout) rootView.findViewById(R.id.detail_progress);
        lProgress.setVisibility(View.VISIBLE);
        mGridLayout = (GridLayout) rootView.findViewById(R.id.detail_grid);
        mGridLayout.setUseDefaultMargins(false);
        mGridLayout.setAlignmentMode(GridLayout.ALIGN_BOUNDS);

        return rootView;
    }


    public void fetchDetails() {
        FetchMovieDetailsTask movieTask = new FetchMovieDetailsTask();
        movieTask.execute(movieId);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "DETAIL save state");
        outState.putParcelable("moviedetail", movieDetail);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        ImageView imageView = (ImageView) getActivity().findViewById(R.id.detail_movie_poster);
        Log.d(LOG_TAG, "DETAIL image width: " + imageView.getWidth() + " height: " + imageView.getHeight());
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            Log.d(LOG_TAG, "DETAIL retore state");
            // Restore last state for checked position.
            movieDetail = savedInstanceState.getParcelable("moviedetail");
            drawLayout(movieDetail);
        }
        {
            Intent intent = getActivity().getIntent();
            if (intent != null) {
                if (intent.hasExtra(Intent.EXTRA_TEXT)) {
                    movieId = intent.getStringExtra(Intent.EXTRA_TEXT);
                }
            }
            fetchDetails();

        }
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        Intent intent = getActivity().getIntent();
//        if (intent != null) {
//            if (intent.hasExtra(Intent.EXTRA_TEXT)) {
//                movieId = intent.getStringExtra(Intent.EXTRA_TEXT);
//            }
//        }
//        fetchDetails();
//
//    }

    public void drawLayout(MovieDetail movieDetail) {
        if(movieDetail != null) {
            lProgress.setVisibility(View.VISIBLE);
            TextView movieTitle = (TextView) getActivity().findViewById(R.id.detail_movie_title);
            movieTitle.setText(movieDetail.getMovieTitle());

            ImageView imageView = (ImageView) getActivity().findViewById(R.id.detail_movie_poster);

//        GridLayout grid = (GridLayout) imageView.getParent();

            Log.d(LOG_TAG, "DETAILS URL: " + movieDetail.getPoster_url());
            Log.d(LOG_TAG, "DETAIL screen: " + getResources().getConfiguration().orientation);
            int orientation = getResources().getConfiguration().orientation;
            int poster_width = 450;
            switch (orientation) {
                case 1:
                    poster_width = 500;
                    break;
                case 2:
                    poster_width = 800;
                    break;
                default:
                    break;
            }
            int poster_height = (int) (poster_width * 1.5);

            Glide.with(this) //
                    .load(movieDetail.getPoster_url())
                    .animate(R.anim.abc_fade_in)
                    .override(poster_width, poster_height)
//                .fitCenter()
                    .centerCrop()
                    .into(imageView);
//        Log.d(LOG_TAG, "DETAIL grid 2 width: " + grid.getWidth() + " height: " + grid.getHeight());
            Log.d(LOG_TAG, "DETAIL image 2 width: " + imageView.getWidth() + " height: " + imageView.getHeight());
            TextView release = (TextView) getActivity().findViewById(R.id.detail_movie_release);
            release.setText(movieDetail.getRelease());

            TextView rate = (TextView) getActivity().findViewById(R.id.detail_movie_rate);
            rate.setText(movieDetail.getRate() + getActivity().getResources().getString(R.string.average_weight_total));

            TextView desc = (TextView) getActivity().findViewById(R.id.detail_movie_desc);
            String descText = getActivity().getResources().getString(R.string.empty_description);
            if (!movieDetail.getDesc().isEmpty()) {
                descText = movieDetail.getDesc();
            }
            desc.setText(descText);

            lProgress.setVisibility(View.GONE);
        }else {
            lProgress.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.fetch_data_fail), Toast.LENGTH_SHORT).show();
            NavUtils.navigateUpFromSameTask(getActivity());
            lProgress.setVisibility(View.GONE);
        }
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
            if (jsonStr != null) {
                JSONObject jsonObject = new JSONObject(jsonStr);

//String movieId, String movieTitle, String poster_url, String release, String rate, String desc)
                return new MovieDetail(jsonObject.getString(MOVIE_ID),
                        jsonObject.getString(MOVIE_TITLE),
                        getActivity().getResources().getString(R.string.poseter_base_url) + jsonObject.getString(MOVIE_POSTER_PATH),
                        jsonObject.getString(MOVIE_RELEASE_DATE),
                        jsonObject.getString(MOVIE_VOTE_AVERAGE),
                        jsonObject.getString(MOVIE_OVERVIEW));
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
//            lProgress.setVisibility(View.VISIBLE);
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


            String id = params[0];


            String API_KEY = getActivity().getResources().getString(R.string.api_key);

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

//                Log.v(LOG_TAG, "INDV JSON: " + movieJsonStr);

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
        protected void onPostExecute(MovieDetail detail) {

                movieDetail = detail;
                drawLayout(detail);

        }
    }
}
