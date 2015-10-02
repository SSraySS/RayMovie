package me.synology.ssray73.raymovie.core;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

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

import me.synology.ssray73.raymovie.R;
import me.synology.ssray73.raymovie.object.MovieDetail;
import me.synology.ssray73.raymovie.object.MovieReview;
import me.synology.ssray73.raymovie.object.MovieTrailer;


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


    private String getConstantString(Integer key){
        return getActivity().getResources().getString(key);
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

            Glide.with(this)
                    .load(movieDetail.getPoster_url())
                    .animate(R.anim.abc_fade_in)
                    .override(poster_width, poster_height)
                    .centerCrop()
                    .into(imageView);

//        Log.d(LOG_TAG, "DETAIL grid 2 width: " + grid.getWidth() + " height: " + grid.getHeight());
//            Log.d(LOG_TAG, "DETAIL image 2 width: " + imageView.getWidth() + " height: " + imageView.getHeight());
            TextView release = (TextView) getActivity().findViewById(R.id.detail_movie_release);
            release.setText(movieDetail.getRelease());

            TextView rate = (TextView) getActivity().findViewById(R.id.detail_movie_rate);
            rate.setText(movieDetail.getRate() + getConstantString(R.string.average_weight_total));

            TextView desc = (TextView) getActivity().findViewById(R.id.detail_movie_desc);
            String descText = getConstantString(R.string.empty_description);
            if (!movieDetail.getDesc().isEmpty()) {
                descText = movieDetail.getDesc();
            }
            desc.setText(descText);


            ArrayList<MovieReview> reviews = movieDetail.getReviews();
            ArrayList<MovieTrailer> trailers = movieDetail.getTrailers();
            Log.d(LOG_TAG, "REPRINT TRAILERS: " + trailers.size());
            Log.d(LOG_TAG, "REPRINT REVIEWS: " + reviews.size());

            if(reviews.isEmpty()){
                //show no review message

            }else {
//                ListView reviewList = (ListView) getActivity().findViewById(R.id.detail_movie_review_list);
                for(MovieReview review : movieDetail.getReviews()){
                    Log.d(LOG_TAG, "REPRINT review - author" + review.getAuthor() );
                }

            }


            if (trailers.isEmpty()){
                //show no trailers message

            } else {
                LinearLayout trailerLayout =  (LinearLayout) getActivity().findViewById(R.id.detail_movie_trailer_list);
                for(MovieTrailer trailer : movieDetail.getTrailers()){
                    if(trailer.getType().equals("Trailer")){

                    }
                    Log.d(LOG_TAG, "REPRINT trailer - key" + trailer.getKey() + " || " + trailer.getType());
                    LayoutInflater inflater = (LayoutInflater) getActivity()
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    View trailerView = inflater.inflate(R.layout.trailer, this, false);

                }



            }






            lProgress.setVisibility(View.GONE);
        }else {
            lProgress.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), getConstantString(R.string.fetch_data_fail), Toast.LENGTH_SHORT).show();
            NavUtils.navigateUpFromSameTask(getActivity());
            lProgress.setVisibility(View.GONE);
        }
    }




    public class FetchMovieDetailsTask extends AsyncTask<String, Void, MovieDetail> {
        private final String LOG_TAG = FetchMovieDetailsTask.class.getSimpleName();


        final String MOVIE_ID =  getConstantString(R.string.movieapi_id);
        final String MOVIE_TITLE = getConstantString(R.string.movieapi_title);
        final String MOVIE_POSTER_PATH = getConstantString(R.string.movieapi_poster_path);
        final String MOVIE_OVERVIEW = getConstantString(R.string.movieapi_overview);
        final String MOVIE_RELEASE_DATE = getConstantString(R.string.movieapi_release_date);
        final String MOVIE_VOTE_AVERAGE = getConstantString(R.string.movieapi_vote_average);
        final String MOVIE_VOTE_COUNT = getConstantString(R.string.movieapi_vote_count);


        final String REVIEW_RESULT = getConstantString(R.string.movieapi_review_results);
        final String REVIEW_ID = getConstantString(R.string.movieapi_review_id);
        final String REVIEW_AUTHOR = getConstantString(R.string.movieapi_review_author);
        final String REVIEW_CONTENT = getConstantString(R.string.movieapi_review_content);
        final String REVIEW_URL = getConstantString(R.string.movieapi_review_url);

        final String TRAILER_RESULT = getConstantString(R.string.movieapi_trailer_results);
        final String TRAILER_ID = getConstantString(R.string.movieapi_trailer_id);
        final String TRAILER_ISO = getConstantString(R.string.movieapi_iso);
        final String TRAILER_KEY = getConstantString(R.string.movieapi_trailer_key);
        final String TRAILER_NAME = getConstantString(R.string.movieapi_trailer_name);
        final String TRAILER_SITE = getConstantString(R.string.movieapi_trailer_site);
        final String TRAILER_SIZE = getConstantString(R.string.movieapi_trailer_size);
        final String TRAILER_TYPE = getConstantString(R.string.movieapi_trailer_type);



        private String getConstantString(Integer key){
            return getActivity().getResources().getString(key);
        }

        private MovieDetail getMovieDetailFromJson(String detailStr, String reviewStr, String trailerStr) throws JSONException {
            if (detailStr != null) {
                JSONObject reviewObject = null;
                JSONObject trailerObject = null;
                Log.d(LOG_TAG, " Reviews str: " + reviewStr);
                ArrayList<MovieReview> reviews = new ArrayList<>();
                ArrayList<MovieTrailer> trailers = new ArrayList<>();


                if(reviewStr != null ){
                    reviewObject = new JSONObject(reviewStr);
                    JSONArray reviewArray = reviewObject.getJSONArray(REVIEW_RESULT);
                    for(int i = 0; i < reviewArray.length(); i++){
                        JSONObject idvReview = (JSONObject) reviewArray.get(i);
                        reviews.add(new MovieReview(idvReview.getString(REVIEW_ID),
                                idvReview.getString(REVIEW_AUTHOR),
                                idvReview.getString(REVIEW_CONTENT),
                                idvReview.getString(REVIEW_URL)));
                    }
                }

                if(trailerStr != null){
                    trailerObject = new JSONObject(trailerStr);
                    Log.d(LOG_TAG, " Trailer str: " + trailerStr);
                    trailerObject = new JSONObject(trailerStr);
                    JSONArray trailerArray = trailerObject.getJSONArray(TRAILER_RESULT);
                    for(int i = 0; i < trailerArray.length(); i++ ){
                        JSONObject idvTrailer = (JSONObject) trailerArray.get(i);
                        trailers.add(new MovieTrailer(idvTrailer.getString(TRAILER_ID),
                                idvTrailer.getString(TRAILER_ISO),
                                idvTrailer.getString(TRAILER_KEY),
                                idvTrailer.getString(TRAILER_NAME),
                                idvTrailer.getString(TRAILER_SITE),
                                idvTrailer.getString(TRAILER_SIZE),
                                idvTrailer.getString(TRAILER_TYPE)));
                    }

                }

                JSONObject movieObject = new JSONObject(detailStr);

//String movieId, String movieTitle, String poster_url, String release, String rate, String desc)
                return new MovieDetail(movieObject.getString(MOVIE_ID),
                        movieObject.getString(MOVIE_TITLE),
                        getConstantString(R.string.poseter_base_url) + movieObject.getString(MOVIE_POSTER_PATH),
                        movieObject.getString(MOVIE_RELEASE_DATE),
                        movieObject.getString(MOVIE_VOTE_AVERAGE),
                        movieObject.getString(MOVIE_OVERVIEW),
                        reviews, trailers);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
//            lProgress.setVisibility(View.VISIBLE);
        }


        public String urlGetContent(HttpURLConnection connection) throws IOException {
            BufferedReader reader = null;
            try{
                InputStream inputStream = connection.getInputStream();
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
                return buffer.toString();
            }finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, e.getMessage(), e);
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected MovieDetail doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            HttpURLConnection trailersUrlConnection = null;
            HttpURLConnection reviewUrlConnection = null;

            String movieJsonStr = null;
            String reviewJsonStr = null;
            String trailersJsonStr = null;

            final String TMDB_BASE_URL = getConstantString(R.string.movieapi_detail_baseurl);
            final String KEY_PARAM = getConstantString(R.string.movieapi_key);
            final String TRAILERS_PATH = getConstantString(R.string.movieapi_path_videos);
            final String REVIEWS_PATH = getConstantString(R.string.movieapi_path_reviews);

            String id = params[0];


            String API_KEY = getConstantString(R.string.api_key);

            Uri buildUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                    .appendPath(id)
                    .appendQueryParameter(KEY_PARAM, API_KEY).build();

            try {
                URL url = new URL(buildUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                movieJsonStr =  urlGetContent(urlConnection);

                Log.d(LOG_TAG, "DETAIL API: " + url.toString());
//                Log.d(LOG_TAG, "INDV JSON: " + movieJsonStr);


                //Get movie trailers
                Uri trailersUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                        .appendPath(id)
                        .appendPath(TRAILERS_PATH)
                        .appendQueryParameter(KEY_PARAM, API_KEY).build();

                URL trailerUrl = new URL(trailersUri.toString());
                trailersUrlConnection = (HttpURLConnection) trailerUrl.openConnection();
                trailersUrlConnection.setRequestMethod("GET");
                trailersUrlConnection.connect();

                trailersJsonStr =  urlGetContent(trailersUrlConnection);

                Log.d(LOG_TAG, "TRAILERS: " + trailerUrl.toString());
                Log.d(LOG_TAG, "TRAILERS: " + trailersJsonStr);


                //Get movie reviews
                Uri reviewUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                        .appendPath(id)
                        .appendPath(REVIEWS_PATH)
                        .appendQueryParameter(KEY_PARAM, API_KEY).build();

                URL reviewUrl = new URL(reviewUri.toString());
                reviewUrlConnection = (HttpURLConnection) reviewUrl.openConnection();
                reviewUrlConnection.setRequestMethod("GET");
                reviewUrlConnection.connect();
                reviewJsonStr = urlGetContent(reviewUrlConnection);
                Log.d(LOG_TAG, "REVIEWS: " + reviewUrl.toString());
                Log.d(LOG_TAG, "REVIEWS: " + reviewJsonStr);
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
                if(trailersUrlConnection != null){
                    trailersUrlConnection.disconnect();
                }
                if(reviewUrlConnection != null){
                    reviewUrlConnection.disconnect();
                }

            }
            try {
                return getMovieDetailFromJson(movieJsonStr, reviewJsonStr, trailersJsonStr );
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
