package bizcom.bizcom;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.IOException;

public class HomeActivity extends AppCompatActivity {
    LinearLayout recomCat1, recomCat2, recomCat3;
    RecyclerView sponsored, recommended;
    Drawable back;
    ColorStateList col;

    public Button createImgButton(String imageName, String name){
/*
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginBottom="8dp"
        android:layout_marginEnd="8dp"
        android:layout_marginStart="8dp"
        android:layout_marginTop="8dp"
        android:background="@drawable/thumb_style"
        android:drawableTop="@mipmap/ic_launcher"
        android:text="Sign Up"
        android:textColor="@android:color/background_dark" />
*/

//        Button mybt = (Button) recomCat3.getChildAt(0);
//        Drawable back = mybt.getBackground();
//        ColorStateList col = mybt.getTextColors();


        Drawable icon = null;
        try {
            icon = Drawable.createFromStream(getAssets().open(imageName), null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Button bt = new Button(this);
        bt.setCompoundDrawables(null,null,null, icon);
        bt.setText(name);

        bt.setBackground(back);
        bt.setTextColor(col);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(8, 8, 8, 8);
        bt.setLayoutParams(params);

        return bt;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sponsored = findViewById(R.id.sponsoredItems);
        recommended = findViewById(R.id.recommendedItems);
        recomCat1 = findViewById(R.id.linear3);
        recomCat2 = findViewById(R.id.linear4);
        recomCat3 = findViewById(R.id.linear5);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        sponsored.setLayoutManager(layoutManager);
        recommended.setLayoutManager(layoutManager);
        Button mybt = (Button) recomCat2.getChildAt(0);
        mybt.setText("Maachis");

        back = mybt.getBackground();
        col = mybt.getTextColors();

        mybt.setBackground(back);
        mybt.setTextColor(col);

        recomCat3.addView(createImgButton("khaikkhaik.jpg","DynamicButton"));
        recomCat3.addView(createImgButton("khaikkhaik.jpg","DynamicButton2"));




    }

}
