package bizcom.bizcom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;

public class businessProfile extends AppCompatActivity {


    Button btn_ads;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_profile);
        btn_ads = (Button) findViewById(R.id.ads_button);

        btn_ads.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent ads_intent = new Intent(businessProfile.this, ads_activity.class);
                startActivity(ads_intent);
            }
        });
    }
}
