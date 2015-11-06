package com.prt2121.marbledcat.sample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.prt2121.marbledcat.sample.model.User;
import java.util.List;

/**
 * Created by pt2121 on 10/29/15.
 */
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

  private List<User> users;

  public UserListAdapter(List<User> users) {
    this.users = users;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user, parent, false);
    return new ViewHolder(v);
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    holder.textView.setText(users.get(position).getLogin());
  }

  @Override public int getItemCount() {
    return users.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.textView) TextView textView;

    public ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }
}
