package hanjo.simukgak;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class order_last extends AppCompatActivity implements order_ListViewAdapter2.ListBtnClickListener{

    private TextView total_price_View ;
    private int total_price_int;
    private ArrayList<ListViewItem> itemList;
    private order_ListViewAdapter2 adapter;
    private FileManager fileManager;
    private SharedPreferences preferences;
    private EditText address;
    private TextView saved_address;
    private CheckBox saved_address_Box;
    private CheckBox new_address_Box;
    private CheckBox cash_Box;
    private CheckBox card_Box;
    private CheckBox banking_Box;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_last);

        Intent intent = getIntent();
        itemList = (ArrayList<ListViewItem>) intent.getSerializableExtra("order");

        adapter = new order_ListViewAdapter2(this,R.layout.order_item2,this);
        adapter.setStoreName(intent.getStringExtra("StoreName"));

        adapter.setList(itemList);

        ListView list = (ListView) findViewById(R.id.k);
        list.setAdapter(adapter);

        fileManager = new FileManager(getApplicationContext(), "orderlist_info.txt");

         final TextView total_price_View =(TextView) findViewById(R.id.total_price);
         int total_price_int = 0;
        for (int i = 0; i < itemList.size(); i++) {
            total_price_int += (itemList.get(i).getcount() * itemList.get(i).getPrice());
        }

        total_price_View.setText("총 가격: " + total_price_int);

        banking_Box =(CheckBox)findViewById(R.id.banking_check);
        card_Box =(CheckBox)findViewById(R.id.card_check);
        cash_Box =(CheckBox)findViewById(R.id.cash_check);

        new_address_Box =(CheckBox)findViewById(R.id.new_address_check);
        saved_address_Box =(CheckBox)findViewById(R.id.save_address_check);

        address = (EditText)findViewById(R.id.real_address);
        saved_address =(TextView)findViewById(R.id.save_address);
        preferences=getSharedPreferences("shared", MODE_PRIVATE);

        saved_address.setText(" 저장된 주소: "+preferences.getString("key", null));
        if(!new_address_Box.isChecked())
        {
        address.setEnabled(false);
        }
    }
    public void save_address(View v)
    {
        if(new_address_Box.isChecked())
        {
            new_address_Box.post(new Runnable() {
                @Override
                public void run() {
                    new_address_Box.setChecked(false);
                    address.setEnabled(false);
                }
            });
        }
    }
    public void new_address(View v)
    {
        if(saved_address_Box.isChecked())
         {
        saved_address_Box.post(new Runnable() {
            @Override
            public void run() {
               saved_address_Box.setChecked(false);
            }
        });
         }

        if(new_address_Box.isChecked())
        {
            address.setEnabled(true);
        }
        else
        {
            address.setEnabled(false);
        }
    }
    public void cash(View v)
    {
        if(card_Box.isChecked())
        {
            card_Box.post(new Runnable() {
                @Override
                public void run() {
                    card_Box.setChecked(false);
                }
            });
        }
        else if(banking_Box.isChecked())
        {
            banking_Box.post(new Runnable() {
                @Override
                public void run() {
                    banking_Box.setChecked(false);

                }
            });
        }
    }
    public void card(View v)
    {
        if(cash_Box.isChecked())
        {
            cash_Box.post(new Runnable() {
                @Override
                public void run() {
                    cash_Box.setChecked(false);
                }
            });
        }
        else if(banking_Box.isChecked())
        {
            banking_Box.post(new Runnable() {
                @Override
                public void run() {
                    banking_Box.setChecked(false);

                }
            });
        }
    }
    public void banking(View v)
    {
        if(cash_Box.isChecked())
        {
            cash_Box.post(new Runnable() {
                @Override
                public void run() {
                    cash_Box.setChecked(false);
                }
            });
        }
        else if(card_Box.isChecked())
        {
            card_Box.post(new Runnable() {
                @Override
                public void run() {
                    card_Box.setChecked(false);

                }
            });
        }
    }
    @Override
public void onListBtnClick(int position, View v)
{
    switch(v.getId()) {
        case R.id.btn_minus:
            adapter.count_minus(position);
            adapter.notifyDataSetInvalidated();
            break;
        case R.id.btn_plus:
            adapter.count_plus(position);
            adapter.notifyDataSetInvalidated();
            break;
        default:
            break;
    }
    TextView t =(TextView)findViewById(R.id.total_price);
    t.setText("총 가격:"+ adapter.getTotal_price());

}
public void send_order(View v)
{
    adapter.setTotal_price();
    if(adapter.getTotal_price()>0)
    {

    // 현재시간을 msec 으로 구한다.
    long now = System.currentTimeMillis();
    // 현재시간을 date 변수에 저장한다.
    Date date = new Date(now);
    // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy.MM.dd");
    // nowDate 변수에 값을 저장한다.
    String formatDate = sdfNow.format(date);


    String data;
    data = adapter.getStoreName() + "," + formatDate + ",0";
    for(int j = 0; j < adapter.getCount(); j++) {
        data = data + "," + (adapter.getItem(j).getTitle());
        data = data + "," + adapter.getItem(j).getPrice();
        data = data + "," + adapter.getItem(j).getcount();
    }

        fileManager.writeFile(data);
        Toast.makeText(this, "주문이 완료되었습니다.", Toast.LENGTH_LONG).show();

        setResult(RESULT_OK);
        finish();
        return;
    }
    else
    {
        Toast.makeText(this, "수량이 0입니다. 수량을 조정하세요.", Toast.LENGTH_LONG).show();
    }
}
}
