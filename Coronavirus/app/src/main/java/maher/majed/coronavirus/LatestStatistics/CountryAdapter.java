package maher.majed.coronavirus.LatestStatistics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import maher.majed.coronavirus.R;

public class CountryAdapter extends BaseAdapter {

    private Context context;
    List<Country> countryList;
    public CountryAdapter(Context context, List<Country> countryList){
        this.context = context;
        this.countryList = countryList;
    }

    @Override
    public int getCount() {
        return countryList.size();
    }

    @Override
    public Object getItem(int position) {
        return countryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.country_item, null);
            ViewHolder vh = new ViewHolder();
            vh.flagIm = convertView.findViewById(R.id.flagIm);
            vh.countryTv = convertView.findViewById(R.id.countryTv);
            vh.continentTv = convertView.findViewById(R.id.continentTv);
            vh.casesTv = convertView.findViewById(R.id.casesTv);
            vh.todayCasesTv = convertView.findViewById(R.id.todayCasesTv);
            vh.deathsTv = convertView.findViewById(R.id.deathsTv);
            vh.todayDeathsTv = convertView.findViewById(R.id.todayDeathsTv);
            vh.recoveredTv = convertView.findViewById(R.id.recoveredTv);
            vh.activeTv = convertView.findViewById(R.id.activeTv);

            convertView.setTag(vh);
        }

        ViewHolder vh = (ViewHolder) convertView.getTag();
        Glide.with(context).load(countryList.get(position).getFlag()).into(vh.flagIm);
        vh.countryTv.setText("Country : " + countryList.get(position).getCountry());
        vh.continentTv.setText("Continent : " + countryList.get(position).getContinent());
        vh.casesTv.setText("Cases : " + countryList.get(position).getCases());
        vh.todayCasesTv.setText("Today Cases : " + countryList.get(position).getTodayCases());
        vh.deathsTv.setText("Deaths : " + countryList.get(position).getDeaths());
        vh.todayDeathsTv.setText("Today Deaths : " + countryList.get(position).getTodayDeaths());
        vh.recoveredTv.setText("Recovered : " + countryList.get(position).getRecovered());
        vh.activeTv.setText("Active : " + countryList.get(position).getActive());


        return convertView;

    }
    class ViewHolder{
        ImageView flagIm;
        TextView countryTv;
        TextView continentTv;
        TextView casesTv;
        TextView todayCasesTv;
        TextView deathsTv;
        TextView todayDeathsTv;
        TextView recoveredTv;
        TextView activeTv;

    }

}
