package com.bayu1993.swipeandloadmore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private LoadMoreListView ListItem;
    private SwipeRefreshLayout swipeMain;
    private LinkedList<String> list;
    private ArrayAdapter<String> adapter;
    private int MaxPage = 5;
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListItem = (LoadMoreListView)findViewById(R.id.list_item);
        swipeMain = (SwipeRefreshLayout)findViewById(R.id.swipe_main);
        list = new LinkedList<>();

        populateDefaultData();

        //listener untuk load more
        ListItem.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener(){
            @Override
            public void onLoadMore(){
                if (currentPage < MaxPage){
                    new FakeLoadMoreAsync().execute();
                }else {
                    ListItem.onLoadMoreComplete();
                }
            }
        });
        swipeMain.setOnRefreshListener(this);
    }
    private void populateDefaultData(){
        String[] androidVersion = new String[]{
                "Not Apple", "Not Blackberry",
                "Cupcake", "Donut", "Eclair",
                "Froyo", "Gingerbread", "Honeycomb",
                "Ice Cream Sandwich", "Jelly Bean",
                "Kitkat", "Lollipop", "Marshmallow",
                "Nougat","O"
        };

        for (int i =0; i < androidVersion.length; i++){
            list.add(androidVersion[i]);
        }
        adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_expandable_list_item_1,
                android.R.id.text1, list);
        ListItem.setAdapter(adapter);
    }
    @Override
    public void onRefresh(){
        new FakePullRefreshAsync().execute();
    }
    //Background proses palsu untuk melakukan proses delay dan append data di
    //bagian bawah list
    private class FakeLoadMoreAsync extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids){
            try{
                Thread.sleep(4000);
            }catch (Exception e){}
            return null;

    }
        @Override
        protected  void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
        populateLoadmoreData();
            currentPage +=1;
        }
    }
    private void populateLoadmoreData(){
        String loadmoreText = "Added on loadmore";
        for (int i = 0; i < 10; i++){
            list.addLast(loadmoreText);
        }
        adapter.notifyDataSetChanged();
        ListItem.onLoadMoreComplete();
        ListItem.setSelection(list.size() - 11);
    }

    // Background proses palsu untuk melakukan proses delay dan append
    // data di bagian atas list
    private class FakePullRefreshAsync extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids){
            try{
                Thread.sleep(4000);
            }catch (Exception e){}
            return null;
        }

         @Override
         protected void onPostExecute(Void aVoid){
         super.onPostExecute(aVoid);
             populatePullRefreshData();
         }
    }
    private void populatePullRefreshData(){
        String swipePullRefreshText = "Added after swipe layout";
        for (int i=0; i<5; i++){
            list.addFirst(swipePullRefreshText);
        }
        swipeMain.setRefreshing(false);
        adapter.notifyDataSetChanged();
        ListItem.setSelection(0);
    }

}
