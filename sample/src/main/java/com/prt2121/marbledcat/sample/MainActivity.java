package com.prt2121.marbledcat.sample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.prt2121.marbledcat.RxLog;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

@RxLog
public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show();
      }
    });

    Observable<String> o1 =
        Observable.interval(2, TimeUnit.SECONDS).skipWhile(new Func1<Long, Boolean>() {
          @Override public Boolean call(Long a1) {
            return a1 < 3;
          }
        }).map(new Func1<Long, Integer>() {
          @Override public Integer call(Long b1) {
            return new BigDecimal(b1).intValueExact();
          }
        }).filter(new Func1<Integer, Boolean>() {
          @Override public Boolean call(Integer c1) {
            return c1 % 2 == 0;
          }
        }).takeUntil(new Func1<Integer, Boolean>() {
          @Override public Boolean call(Integer d1) {
            return d1 > 10;
          }
        }).map(new Func1<Integer, String>() {
          @Override public String call(Integer e1) {
            return new RomanNumeral(e1).toString();
          }
        });

    Observable.interval(2, TimeUnit.SECONDS)
        .skipWhile(new Func1<Long, Boolean>() {
          @Override public Boolean call(Long a2) {
            return a2 < 4;
          }
        })
        .map(new Func1<Long, Integer>() {
          @Override public Integer call(Long b2) {
            return new BigDecimal(b2).intValueExact();
          }
        })
        .filter(new Func1<Integer, Boolean>() {
          @Override public Boolean call(Integer c2) {
            return c2 % 2 == 1;
          }
        })
        .takeUntil(new Func1<Integer, Boolean>() {
          @Override public Boolean call(Integer d2) {
            return d2 > 10;
          }
        })
        .map(new Func1<Integer, String>() {
          @Override public String call(Integer e2) {
            return new RomanNumeral(e2).toString();
          }
        })
        .mergeWith(o1)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<String>() {
          @Override public void call(String l) {
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            Timber.d(throwable.getMessage());
          }
        }, new Action0() {
          @Override public void call() {
          }
        });
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
