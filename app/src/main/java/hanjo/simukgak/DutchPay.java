package hanjo.simukgak;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class DutchPay extends AppCompatActivity implements DutchListViewAdapter.ListBtnClickListener {

    private FileManager fileManager;

    final DutchListViewAdapter adapter = new DutchListViewAdapter(this, R.layout.dutch_listview_item, this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dutch_pay);
        final ListView listview ;

        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);

        LinearLayout layout =(LinearLayout)findViewById(R.id.back);
        layout.setBackgroundResource(R.drawable.bg2);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

            }
        });

        ArrayList<String> fileValues;
        String[] values;

        fileManager = new FileManager(getApplicationContext(), "dutch_info.txt");
        fileValues = fileManager.readFile();
        for(int i = 0; i < fileValues.size(); i++)
        {
            values = fileValues.get(i).split(",");
            adapter.addItem(values[0], values[1], values[2], values[3]);
        }
        adapter.sortItemByDate();

        TextView totalTText = (TextView)findViewById(R.id.totalText);
        Button sortByDate = (Button)findViewById(R.id.sortByDateButton) ;
        Button sortByName = (Button)findViewById(R.id.sortByNameButton) ;

        totalTText.setText("받아야 할 돈: " + adapter.getTotalPrice() + "원");

        sortByDate.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.sortItemByDate();
            }
        });

        sortByName.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.sortItemByName();
            }
        });

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        fileManager.resetData();
        String data;
        for(int i = 0; i<adapter.getCount(); i++)
        {
            data = adapter.getItem(i).getTitle() + "," + Integer.toString(adapter.getItem(i).getPrice()) + "," + adapter.getItem(i).getName() + "," + adapter.getItem(i).getDate();
            fileManager.writeFile(data);
        }
    }

    @Override
    public void onListBtnClick(int position, View v) {
        switch(v.getId()) {
            case R.id.deleteButton:
                itemDelete(position);
                break;
            case R.id.askButton:
                noticeDutch(position);
                break;
            default:
                break;
        }
    }

    public void itemDelete(final int position) {
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(DutchPay.this);
        alert_confirm.setMessage("내역을 삭제하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nameTo = adapter.getItem(position).getName();
                        SocketWrapper.object().sendDutchDismiss(nameTo);
                        //adapter.deleteItem(position);
                        int n = adapter.deleteCommonItem(position);
                        TextView totalTText = (TextView)findViewById(R.id.totalText);
                        totalTText.setText("받아야 할 돈: " + adapter.getTotalPrice() + "원");
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), Integer.toString(n) + "개의 항목이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 'No'
                        return;
                    }
                });
        AlertDialog alert = alert_confirm.create();
        alert.show();
    }

    public void noticeDutch(final int position) {
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(DutchPay.this);
        alert_confirm.setMessage(adapter.getItem(position).getName() + "에게 " + adapter.getTotalPrice(position) + "원을 요청하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nameTo = adapter.getItem(position).getName();
                        String price = adapter.getTotalPrice(position);
                        Toast.makeText(getApplicationContext(), nameTo + ": " + price, Toast.LENGTH_SHORT).show();
                        SocketWrapper.object().sendDutch(nameTo, price);
                    }
                }).setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog alert = alert_confirm.create();
        alert.show();
    }
}