package com.prt2121.marbledcat.sample;

import android.app.Application;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import rx.Observable;
import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by pt2121 on 10/30/15.
 */
public class RxApp extends Application {

  private static RxApp instance;
  private OkHttpClient httpClient;
  private Observable<String> jsonObservable;

  @Override public void onCreate() {
    super.onCreate();
    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    }
    instance = this;
    httpClient = new OkHttpClient();
  }

  public static RxApp getApp() {
    return instance;
  }

  public Observable<String> requestJson(final String url) {
    if (jsonObservable == null) {
      jsonObservable = Observable.create(new Observable.OnSubscribe<String>() {
        @Override public void call(final Subscriber<? super String> subscriber) {
          Request request = new Request.Builder().url(url).build();
          Timber.d("requesting...");
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
      }).cache();
    }
    return jsonObservable;
  }
}
