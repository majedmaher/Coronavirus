package maher.majed.coronavirus;


import android.os.Bundle;
import android.view.MenuItem;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import maher.majed.coronavirus.CoronaInformation.CoronaInformationFragment;
import maher.majed.coronavirus.LatestStatistics.LatestStatisticsFragment;
import maher.majed.coronavirus.LeatestInformation.LeatestInformationMessagesFragment;
import maher.majed.coronavirus.Videos.VideoFragment;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new CoronaInformationFragment()).commit();
        }
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new CoronaInformationFragment();
                            break;
                        case R.id.nav_video:
                            selectedFragment = new VideoFragment();
                            break;
                        case R.id.nav_search:
                            selectedFragment = new LatestStatisticsFragment();
                            break;
                        case R.id.nav_news:
                            selectedFragment = new LeatestInformationMessagesFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };

}