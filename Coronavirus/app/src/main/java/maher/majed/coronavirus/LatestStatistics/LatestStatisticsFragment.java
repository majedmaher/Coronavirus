package maher.majed.coronavirus.LatestStatistics;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import maher.majed.coronavirus.R;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LatestStatisticsFragment extends Fragment {

    RequestQueue queue;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_latest_statistics, container, false);

        queue = Volley.newRequestQueue(view.getContext());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        ListView listView = view.findViewById(R.id.listView);
        EditText searchEd = view.findViewById(R.id.searchEd);

        final String url = "https://corona.lmao.ninja/v2/countries?yesterday&sort";

        final List<Country> countryList = new ArrayList<Country>();

        final CountryAdapter countryAdapter = new CountryAdapter(view.getContext(),countryList);
        listView.setAdapter(countryAdapter);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        Country country = new Country();
                        JSONObject object = response.getJSONObject(i);
                        country.setCountry(object.getString("country"));

                        JSONObject countryInfo = object.getJSONObject("countryInfo");
                        country.setFlag(countryInfo.getString("flag"));

                        country.setCases(object.getInt("cases"));
                        country.setTodayCases(object.getInt("todayCases"));
                        country.setDeaths(object.getInt("deaths"));
                        country.setTodayDeaths(object.getInt("todayDeaths"));
                        country.setRecovered(object.getInt("recovered"));
                        country.setActive(object.getInt("active"));
                        country.setContinent(object.getString("continent"));

                        countryList.add(country);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    countryAdapter.notifyDataSetChanged();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(view.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);

        searchEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {

                countryList.clear();

                JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                if (s.toString().equalsIgnoreCase("")) {
                                    Country country = new Country();
                                    JSONObject object = response.getJSONObject(i);
                                    country.setCountry(object.getString("country"));

                                    JSONObject countryInfo = object.getJSONObject("countryInfo");
                                    country.setFlag(countryInfo.getString("flag"));

                                    country.setCases(object.getInt("cases"));
                                    country.setTodayCases(object.getInt("todayCases"));
                                    country.setDeaths(object.getInt("deaths"));
                                    country.setTodayDeaths(object.getInt("todayDeaths"));
                                    country.setRecovered(object.getInt("recovered"));
                                    country.setActive(object.getInt("active"));
                                    country.setContinent(object.getString("continent"));

                                    countryList.add(country);
                                }else {
                                    if (response.getJSONObject(i).getString("country").toLowerCase().contains(s.toString().toLowerCase()) || response.getJSONObject(i).getString("continent").toLowerCase().contains(s.toString().toLowerCase())) {
                                        Country country = new Country();
                                        JSONObject object = response.getJSONObject(i);
                                        country.setCountry(object.getString("country"));

                                        JSONObject countryInfo = object.getJSONObject("countryInfo");
                                        country.setFlag(countryInfo.getString("flag"));

                                        country.setCases(object.getInt("cases"));
                                        country.setTodayCases(object.getInt("todayCases"));
                                        country.setDeaths(object.getInt("deaths"));
                                        country.setTodayDeaths(object.getInt("todayDeaths"));
                                        country.setRecovered(object.getInt("recovered"));
                                        country.setActive(object.getInt("active"));
                                        country.setContinent(object.getString("continent"));

                                        countryList.add(country);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            countryAdapter.notifyDataSetChanged();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(view.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                queue.add(request);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

}