package com.gfso.client.oauthclientapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.gfso.client.oauthclientapplication.fragment.FilterDrawerFragment;
import com.gfso.client.oauthclientapplication.fragment.recycleview.SearchHotKeysAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.drawer_content)
    FrameLayout mDrawerContent;
//    @BindView(R.id.search_delete_btn)
//    Button deleteBtn;
    @BindView(R.id.search_history_keys)
    ListView searchkeyListView;
    @BindView(R.id.search_hot_keys)
    RecyclerView hotkeyRecyclerView;

    SearchHotKeysAdapter hotKeyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        initDrawer();
        mDrawerLayout.openDrawer(mDrawerContent);

        initHotKey();

        List<String> hotKeyList = new ArrayList<>();
        hotKeyList.add("手机");
        hotKeyList.add("电脑");
        hotKeyList.add("显示器");
        hotKeyList.add("手机1");
        hotKeyList.add("电脑1");
        hotKeyList.add("显示器1");
        hotKeyList.add("手机2");
        hotKeyList.add("电脑2");
        hotKeyList.add("显示器2");
        hotKeyAdapter = new SearchHotKeysAdapter(this, hotKeyList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);//new LinearLayoutManager(this);
        hotkeyRecyclerView.setLayoutManager(mLayoutManager);
        hotkeyRecyclerView.setAdapter(hotKeyAdapter);
        hotKeyAdapter.setOnItemClickListener((value)->{
            Toast.makeText(SearchActivity.this, value, Toast.LENGTH_LONG).show();
        });

        String[] history = new String[]{"电脑","手机","水果","零食","电脑1","手机1","水果1","零食1","电脑2","手机2","水果2","零食2"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, history);
        searchkeyListView.setAdapter(adapter);
        searchkeyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub
                String value=adapter.getItem(position);
                Toast.makeText(getApplicationContext(),value,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initHotKey() {
        List<String> hotKeyList = new ArrayList<>();
        hotKeyList.add("手机");
        hotKeyList.add("电脑");
        hotKeyList.add("显示器");
        hotKeyList.add("手机1");
        hotKeyList.add("电脑1");
        hotKeyList.add("显示器1");
        hotKeyList.add("手机2");
        hotKeyList.add("电脑2");
        hotKeyList.add("显示器2");
        hotKeyAdapter = new SearchHotKeysAdapter(this, hotKeyList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);//new LinearLayoutManager(this);
        hotkeyRecyclerView.setHasFixedSize(true);
        hotkeyRecyclerView.setLayoutManager(mLayoutManager);
        hotkeyRecyclerView.setAdapter(hotKeyAdapter);
        hotKeyAdapter.setOnItemClickListener((value)->{
            Toast.makeText(SearchActivity.this, value, Toast.LENGTH_LONG).show();
        });
    }

    private void initDrawer() {
        Fragment fragment = new FilterDrawerFragment();
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("departmentName","");
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.drawer_content, fragment).commit();
    }
}
