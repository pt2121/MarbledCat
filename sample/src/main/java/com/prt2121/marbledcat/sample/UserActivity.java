package com.prt2121.marbledcat.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.rxbinding.support.v4.widget.RxSwipeRefreshLayout;
import com.jakewharton.rxbinding.support.v7.widget.RecyclerViewScrollEvent;
import com.jakewharton.rxbinding.support.v7.widget.RxRecyclerView;
import com.jakewharton.rxbinding.support.v7.widget.RxToolbar;
import com.prt2121.marbledcat.RxLog;
import com.prt2121.marbledcat.sample.model.User;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

@RxLog
public class UserActivity extends AppCompatActivity {

  public static int VISIBLE_THRESHOLD = 5;

  @Bind(R.id.recyclerView) RecyclerView recyclerView;
  @Bind(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
  @Bind(R.id.toolbar) Toolbar toolbar;

  private OkHttpClient httpClient = new OkHttpClient();
  private List<User> users = new ArrayList<>();
  private UserListAdapter listAdapter = new UserListAdapter(users);
  private Void UNIT = null;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user);
    ButterKnife.bind(this);
    setSupportActionBar(toolbar);

    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    recyclerView.setAdapter(listAdapter);
    recyclerView.setLayoutManager(layoutManager);

    final Observable<Void> menuItemClicks =
        RxToolbar.itemClicks(toolbar).filter(new Func1<MenuItem, Boolean>() {
          @Override public Boolean call(MenuItem menuItem) {
            return menuItem.getItemId() == R.id.action_refresh;
          }
        }).map(new Func1<MenuItem, Void>() {
          @Override public Void call(MenuItem menuItem) {
            return null;
          }
        });

    Observable<Void> refreshes =
        RxSwipeRefreshLayout.refreshes(swipeRefreshLayout).mergeWith(menuItemClicks);

    Observable<String> requests = refreshes.startWith(UNIT).map(new Func1<Void, String>() {
      @Override public String call(Void click) {
        int randomOffset = (int) Math.floor(Math.random() * 500);
        return "https://api.github.com/users?since=" + randomOffset;
      }
    });

    requests.flatMap(new Func1<String, Observable<String>>() {
      @Override public Observable<String> call(String url) {
        return requestJson(url);
      }
    }).map(new Func1<String, ArrayList<User>>() {
      @Override public ArrayList<User> call(String json) {
        Type type = new TypeToken<ArrayList<User>>() {
        }.getType();
        return new GsonBuilder().create().fromJson(json, type);
      }
    }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ArrayList<User>>() {
      @Override public void call(ArrayList<User> userList) {
        swipeRefreshLayout.setRefreshing(false);
        users.clear();
        users.addAll(userList);
        listAdapter.notifyDataSetChanged();
      }
    }, new Action1<Throwable>() {
      @Override public void call(Throwable throwable) {
      }
    });

    RxRecyclerView.scrollEvents(recyclerView).map(new Func1<RecyclerViewScrollEvent, Boolean>() {
      @Override public Boolean call(RecyclerViewScrollEvent event) {
        return reachingLastItem(event);
      }
    }).distinctUntilChanged().filter(new Func1<Boolean, Boolean>() {
      @Override public Boolean call(Boolean bool) {
        return bool;
      }
    }).throttleLast(2, TimeUnit.SECONDS).map(new Func1<Boolean, String>() {
      @Override public String call(Boolean bool) {
        int randomOffset = (int) Math.floor(Math.random() * 500);
        return "https://api.github.com/users?since=" + randomOffset;
      }
    }).flatMap(new Func1<String, Observable<String>>() {
      @Override public Observable<String> call(String url) {
        return requestJson(url);
      }
    }).map(new Func1<String, ArrayList<User>>() {
      @Override public ArrayList<User> call(String json) {
        Type type = new TypeToken<ArrayList<User>>() {
        }.getType();
        return new GsonBuilder().create().fromJson(json, type);
      }
    }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ArrayList<User>>() {
      @Override public void call(ArrayList<User> userList) {
        users.addAll(userList);
        listAdapter.notifyDataSetChanged();
      }
    }, new Action1<Throwable>() {
      @Override public void call(Throwable throwable) {
      }
    });
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_user, menu);
    return true;
  }

  private Observable<String> requestJson(final String url) {
    return Observable.create(new Observable.OnSubscribe<String>() {
      @Override public void call(final Subscriber<? super String> subscriber) {
        Request request = new Request.Builder().url(url).build();
        httpClient.newCall(request).enqueue(new Callback() {
          @Override public void onFailure(Request request, IOException e) {
            if (!subscriber.isUnsubscribed()) {
              subscriber.onError(e);
            }
          }

          @Override public void onResponse(Response response) throws IOException {
            if (!subscriber.isUnsubscribed()) {
              subscriber.onNext(response.body().string());
              subscriber.onCompleted();
            }
          }
        });
      }
    });
  }

  // breached the VISIBLE_THRESHOLD and load more user.
  @NonNull private Boolean reachingLastItem(RecyclerViewScrollEvent event) {
    RecyclerView view = event.view();
    LinearLayoutManager manager = (LinearLayoutManager) view.getLayoutManager();
    int totalItemCount = manager.getItemCount();
    int visibleItemCount = manager.getChildCount();
    int firstVisibleItem = manager.findFirstVisibleItemPosition();
    return (totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_THRESHOLD);
  }
}
