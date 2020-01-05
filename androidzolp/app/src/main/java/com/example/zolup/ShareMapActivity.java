package com.example.zolup;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

public class ShareMapActivity extends AppCompatActivity implements MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener, MapView.POIItemEventListener {

    private boolean find_check = false;
    private String my_id;
    private MapView mMapView;
    private ArrayList<MapInfo> mapInfos = new ArrayList<>();
    private ArrayList<MapInfo> searchmapInfos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharemap);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("졸업프로젝트");

        mMapView = findViewById(R.id.map_view);
        mMapView.setDaumMapApiKey("751d8c6a990081b07a441473207f841a");

        mMapView.setPOIItemEventListener(this);

        getSupportActionBar().setTitle("공유지도보기");
    }

    @Override
    public void onResume() {
        super.onResume();

        mapInfos.clear();
        mapInfos = (ArrayList<MapInfo>)getIntent().getSerializableExtra("mapinfos");
        my_id = getIntent().getStringExtra("id_info");

        setLocation();
    }

    //액션버튼 메뉴 액션바에 집어 넣기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("태그나 이름을 검색하세요");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //최종검색하는 태그나 이름으로 가능!
            @Override
            public boolean onQueryTextSubmit(String query) {
                //태그 검색해서 정보찾게 해주는 함수 만들기
                FindInfo(query);
                return false;
            }

            //실시간으로 업데이트 되는 부분
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_reset:
                setLocation();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //지도 전체의 정보를 띄움
    public void setLocation(){
        mMapView.removeAllPOIItems();
        find_check = false;

        for(int i = 0; i < mapInfos.size(); i++) {
            if (mapInfos.get(i).getCheck_share()) {
                MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(mapInfos.get(i).getLatitude(), mapInfos.get(i).getLongitude());

                MapPOIItem marker = new MapPOIItem();
                String id = mapInfos.get(i).getMy_id();

                marker.setMapPoint(mapPoint);
                marker.setItemName(mapInfos.get(i).getName());
                marker.setTag(i);

                if (id.equals(my_id)) {
                    marker.setMarkerType(MapPOIItem.MarkerType.RedPin);
                } else {
                    marker.setMarkerType(MapPOIItem.MarkerType.YellowPin);
                }

                marker.setShowCalloutBalloonOnTouch(false);
                mMapView.addPOIItem(marker);
            }
        }
    }

    //검색어가 생겼을 때의 함수
    public void FindInfo(String query){

        if(query.equals("")){
            setLocation();
            return;
        }

        searchmapInfos.clear();
        find_check = true;

        for(int i = 0; i < mapInfos.size(); i++) {
            if (mapInfos.get(i).getCheck_share()) {
                if (mapInfos.get(i).getName().contains(query)) {
                    searchmapInfos.add(mapInfos.get(i));
                    continue;
                }

                String[] content = mapInfos.get(i).getContent().split(" ");
                for (int j = 0; j < content.length; j++) {
                    if (content[j].contains(query)) {
                        searchmapInfos.add(mapInfos.get(i));
                        break;
                    }
                }
            }
        }
        makeFindLocation();
    }

    //검색한 정보들을 토대로 marker 띄우기
    public void makeFindLocation(){
        mMapView.removeAllPOIItems();

        for(int i = 0; i < searchmapInfos.size(); i++){
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(searchmapInfos.get(i).getLatitude(), searchmapInfos.get(i).getLongitude());

            MapPOIItem marker = new MapPOIItem();
            String id = searchmapInfos.get(i).getMy_id();
            String name = searchmapInfos.get(i).getName();

            marker.setItemName(name);
            marker.setMapPoint(mapPoint);
            marker.setTag(i);

            if(id.equals(my_id)){
                marker.setMarkerType(MapPOIItem.MarkerType.RedPin);
            }

            else{
                marker.setMarkerType(MapPOIItem.MarkerType.YellowPin);
            }

            marker.setShowCalloutBalloonOnTouch(false);
            mMapView.addPOIItem(marker);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        mMapView.setShowCurrentLocationMarker(false);
    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float accuracyInMeters) {
//        MapPoint.GeoCoordinate mapPointGeo = currentLocation.getMapPointGeoCoord();
//        Log.i(LOG_TAG, String.format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)", mapPointGeo.latitude, mapPointGeo.longitude, accuracyInMeters));
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    @Override
    public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) {
        mapReverseGeoCoder.toString();
        onFinishReverseGeoCoding(s);
    }

    @Override
    public void onPOIItemSelected(MapView mapView, final MapPOIItem mapPOIItem) {
        //마커 정보 띄워주기해야함.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String content = null;
        if(find_check){
            content = searchmapInfos.get(mapPOIItem.getTag()).getContent();
        }
        else{
            content = mapInfos.get(mapPOIItem.getTag()).getContent();
        }
        builder.setTitle(mapPOIItem.getItemName());
        builder.setMessage(content);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();
    }

    @Override
    public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {
        onFinishReverseGeoCoding("Fail");
    }

    private void onFinishReverseGeoCoding(String result) {
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
    }
}
