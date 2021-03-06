package hanjo.simukgak;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class Mypage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        LinearLayout layout =(LinearLayout)findViewById(R.id.back);
        layout.setBackgroundResource(R.drawable.bg2);
    }


    public void onClickOrderlist(View view)
    {
        startActivity(new Intent(getApplicationContext(), Orderlist.class));
    }
    public void onClickAddress(View view)
    {
        startActivity(new Intent(getApplicationContext(), AddressChange.class));
    }
    public void onClickManage(View view)
    {
        startActivity(new Intent(getApplicationContext(), ManageDiet.class));
    }
}
