package com.example.zolup;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class PrivateMapActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private DatePickerDialog datePickerDialog;
    private Calendar calendar;

    private String my_id;

    private ImageView imageView;
    private ListView listView;

    static private String docu_id;

    static private ArrayList<MapInfo> mapInfos = new ArrayList<>();
    static private ArrayList<MapInfo> private_map = new ArrayList<>();
    static private PrivateListAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privatemap);
        imageView = findViewById(R.id.map_view);
        listView = findViewById(R.id.list_view);
        getSupportActionBar().setTitle("개인기록확인");

    }

    @Override
    protected void onResume(){
        super.onResume();

        mapInfos.clear();
        mapInfos = (ArrayList<MapInfo>)getIntent().getSerializableExtra("mapinfos");
        my_id = getIntent().getStringExtra("id_info");

        imageView.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.INVISIBLE);

        viewCalendar();
        datePickerDialog.show(getSupportFragmentManager(), "date");
    }

    //선택가능한 날짜 띄우기
    private void viewCalendar(){
        calendar = Calendar.getInstance();

        datePickerDialog = DatePickerDialog.newInstance(PrivateMapActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setThemeDark(false);
        datePickerDialog.showYearPickerFirst(false);
        datePickerDialog.setAccentColor(Color.parseColor("#0072BA"));
        datePickerDialog.setTitle("날짜 선택");

        ArrayList<Calendar> setdays = new ArrayList<>();

        for(int i = 0; i < mapInfos.size(); i++){
            if(my_id.equals(mapInfos.get(i).getMy_id())) {
                Calendar days = Calendar.getInstance();
                days.setTime(mapInfos.get(i).getDate());
                setdays.add(days);
            }
        }

        Calendar setselectabledays[] = new Calendar[setdays.size()];

        for(int i = 0; i < setdays.size(); i++){
            setselectabledays[i] = Calendar.getInstance();
            setselectabledays[i] = setdays.get(i);
        }

        datePickerDialog.setSelectableDays(setselectabledays);
    }

    //날짜 선택 후 데이터 셋 만들기
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String datename = year + "년" + (monthOfYear + 1) + "월" + dayOfMonth + "일";
        getSupportActionBar().setTitle("개인기록확인(" + datename + ")");
        private_map.clear();

        String datestr = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(datestr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < mapInfos.size(); i++){
            if(simpleDateFormat.format(mapInfos.get(i).getDate()).equals(simpleDateFormat.format(date)) && my_id.equals(mapInfos.get(i).getMy_id())){
                private_map.add(mapInfos.get(i));
            }
        }

        setStaypoint();
    }

    //날짜에 맞는 점들을 불러와서 정보 확인
    private void setStaypoint(){
        imageView.setVisibility(View.VISIBLE);
        listView.setVisibility(View.VISIBLE);

        listView = findViewById(R.id.list_view);
        myAdapter = new PrivateListAdapter(this, private_map);

        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                imageView.setImageBitmap(setMapimage(private_map.get(position).getLatitude(), private_map.get(position).getLongitude()));

                docu_id = private_map.get(position).getDocu_id();

                CustomDialog customDialog = new CustomDialog(PrivateMapActivity.this);
                customDialog.callFunction(private_map.get(position).getName(), private_map.get(position).getContent(), position, private_map.get(position).getCheck_share());
            }
        });
    }

    public Bitmap setMapimage(double lat, double lon) {
        String url = "https://naveropenapi.apigw.ntruss.com/map-static/v2/raster?w=300&h=300&center="+lon + "," + lat +"&level=16&format=jpg&markers=type:d|size:small|pos:"+lon+"%20"+lat+"|color:red&X-NCP-APIGW-API-KEY-ID=rj9vi23d12&X-NCP-APIGW-API-KEY=YD575NdfLNjT75Pwz2Fk4Q5fiZ6owmZ59ATShQow";
        Bitmap bmp = null;
        try {
            bmp = new JSONTaskGet().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return bmp;
    }

    public class JSONTaskGet extends AsyncTask<String, String, Bitmap> {
        @Override
        protected Bitmap doInBackground(String urls[]) {
            try {
                HttpURLConnection con = null;
                Bitmap bitmap = null;

                try {
                    URL url = new URL(urls[0]);//url을 가져온다.
                    con = (HttpURLConnection) url.openConnection();
                    con.connect();//연결 수행

                    //입력 스트림 생성
                    InputStream stream = con.getInputStream();
                    bitmap = BitmapFactory.decodeStream(stream);

                    return bitmap;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    //종료가 되면 disconnect메소드를 호출한다.
                    if (con != null) {
                        con.disconnect();
                    }
                }//finally 부분
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    static public void updateList(String place_name, String place_context, int position, boolean check_share){

        for(int i = 0; i < mapInfos.size(); i++){
            if(docu_id.equals(mapInfos.get(i).getDocu_id())){
                mapInfos.get(i).setName(place_name);
                mapInfos.get(i).setContent(place_context);
                mapInfos.get(i).setCheck_share(check_share);
                break;
            }
        }

        //디비 update요청하고!
        private_map.get(position).setName(place_name);
        private_map.get(position).setContent(place_context);
        private_map.get(position).setCheck_share(check_share);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("staypoints")
                .document(private_map.get(position).getDocu_id())
                .update("name", place_name, "content", place_context, "isShared", check_share);



        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
