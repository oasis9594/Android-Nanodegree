package com.example.dell.nanodegree;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

public class PopularMovies extends AppCompatActivity {

    RecyclerView popularMovieList;
    public static final String TAG="dell.nanodegree.TAG";
    ArrayList<MovieObject> movieObjects;
    PopularMovieAdapter movieAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);
        assert getSupportActionBar()!=null;
        getSupportActionBar().setTitle("Popular Movies");
        movieObjects=new ArrayList<>();
        popularMovieList=(RecyclerView) findViewById(R.id.popularMovieList);
        GridLayoutManager layoutManager=new GridLayoutManager(this, 2);
        popularMovieList.setLayoutManager(layoutManager);
        SharedPreferences shared=PreferenceManager.getDefaultSharedPreferences(this);
        String type=shared.getString(getResources().getString(R.string.movie_type), null);
        if(type==null)
            type="popular";
        getData(type);
        movieAdapter=new PopularMovieAdapter(this, movieObjects);
        popularMovieList.setAdapter(movieAdapter);
    }
    public void getData(String s)
    {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            FetchMovieTask movieTask=new FetchMovieTask();
            setActionBarTitle(s);
            movieTask.execute(s);
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection!!!!", Toast.LENGTH_SHORT).show();
        }
    }
    public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<MovieObject>>
    {

        @Override
        protected void onPostExecute(ArrayList<MovieObject> m) {
            if(m!=null)
            {
                movieObjects.clear();
                for(MovieObject o: m)
                {
                    movieObjects.add(o);
                    Log.v(TAG, o.getOriginal_title());
                }
                Log.v(TAG, ""+movieObjects.size());
                movieAdapter.notifyDataSetChanged();
            }
        }

        @Override
        protected ArrayList<MovieObject> doInBackground(String... params) {
            if(params.length==0)
                return null;
            HttpURLConnection urlConnection=null;
            BufferedReader reader=null;
            String movieJsonString=null;
            final String MOVIE_API_KEY=getResources().getString(R.string.movie_key);
            try{
                final String BASE_API_POPULAR="http://api.themoviedb.org/3/movie";
                final String QUERY_API_KEY="api_key";
                Uri builtUri = Uri.parse(BASE_API_POPULAR).buildUpon()
                        .appendPath(params[0])
                        .appendQueryParameter(QUERY_API_KEY, MOVIE_API_KEY).build();
                URL url = new URL(builtUri.toString());

                Log.v(TAG, "Built URI " + builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    Log.v(TAG, "inputstream null");
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    Log.v(TAG, "Stream empty while fetching data");
                    return null;
                }
                movieJsonString = buffer.toString();
                Log.v(TAG, movieJsonString);
            }catch (IOException e)
            {
                Log.e(TAG, "Error getting data from api: "+e.getMessage());
                return null;
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                return getMovieDataFromJson(movieJsonString);
            }catch (JSONException e)
            {
                Log.e(TAG, "Error parsing JSON: "+e.getMessage());
            }
            return null;
        }
    }
    public void setActionBarTitle(String s)
    {
        switch (s)
        {
            case "popular":
                getSupportActionBar().setTitle("Popular Movies");
                break;
            case "top_rated":
                getSupportActionBar().setTitle("Top Rated");
                break;
            case "upcoming":
                getSupportActionBar().setTitle("Upcoming Movies");
                break;
        }
    }
    ArrayList<MovieObject> getMovieDataFromJson(String movieJsonStr) throws JSONException
    {
        ArrayList<MovieObject> myMovies=new ArrayList<>();

        final String POSTER_BASE_URL="http://image.tmdb.org/t/p/w342";
        final String MOVIE_RESULTS="results";
        final String MOVIE_POSTER="poster_path";
        final String MOVIE_ORIGINAL_TITLE="original_title";
        final String MOVIE_OVERVIEW="overview";
        final String MOVIE_LANGUAGE="original_language";
        final String MOVIE_RATING="vote_average";
        final String MOVIE_POPULARITY="popularity";
        final String MOVIE_VOTE_COUNT="vote_count";
        final String MOVIE_ADULT="adult";
        final String MOVIE_DATE="release_date";


        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray=movieJson.getJSONArray(MOVIE_RESULTS);
        int i;
        for(i=0;i<movieArray.length();i++)
        {
            MovieObject obj=new MovieObject();
            String posterPath;
            JSONObject movie = movieArray.getJSONObject(i);

            posterPath = POSTER_BASE_URL+movie.getString(MOVIE_POSTER);

            String title=movie.getString(MOVIE_ORIGINAL_TITLE);
            String overview=movie.getString(MOVIE_OVERVIEW);
            String language=movie.getString(MOVIE_LANGUAGE);
            double rating=movie.getDouble(MOVIE_RATING);
            double popularity=movie.getDouble(MOVIE_POPULARITY);
            int votes=movie.getInt(MOVIE_VOTE_COUNT);
            boolean b=movie.getBoolean(MOVIE_ADULT);
            String d=movie.getString(MOVIE_DATE);

            obj.setPosterPath(posterPath);
            Log.v(TAG, posterPath);
            obj.setOriginal_title(title);
            obj.setAdult(b);
            obj.setOverview(overview);
            obj.setPopularity(popularity);
            obj.setRating(rating);
            obj.setLanguage(language);
            obj.setVote_count(votes);
            obj.setRelease_date(d);
            myMovies.add(obj);
        }
        return myMovies;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_type, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id == R.id.menu_popular) {
            SharedPreferences shared=PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor=shared.edit();
            editor.clear();
            editor.putString(getResources().getString(R.string.movie_type), "popular");
            editor.apply();
            getData("popular");
        }
        else if(id == R.id.menu_top_rated) {
            SharedPreferences shared=PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor=shared.edit();
            editor.clear();
            editor.putString(getResources().getString(R.string.movie_type), "top_rated");
            editor.apply();
            getData("top_rated");
        }
        else if(id == R.id.menu_upcoming) {
            SharedPreferences shared=PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor=shared.edit();
            editor.clear();
            editor.putString(getResources().getString(R.string.movie_type), "upcoming");
            editor.apply();
            getData("upcoming");
        }
        return super.onOptionsItemSelected(item);
    }
}
