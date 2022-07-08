package com.java.busylooping.backget.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.java.busylooping.backget.CallBacks.AsyncResponse;
import com.java.busylooping.backget.models.GeneralModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class SearchInList<M, N, T> extends AsyncTask<M, N, ArrayList<Integer>> {
    private final AsyncResponse<M, N, ArrayList<Integer>> asyncResponse;
    private final WeakReference<Context> contextWeakReference;
    private final List<?> list;

    public SearchInList(AsyncResponse<M, N, ArrayList<Integer>> asyncResponse, Context contextWeakReference, List<?> list) {
        this.asyncResponse = asyncResponse;
        this.contextWeakReference = new WeakReference<>(contextWeakReference);
        this.list = list;
    }

    @Override
    protected ArrayList<Integer> doInBackground(M... ms) {
        Context context = contextWeakReference.get();
        ArrayList<Integer> indexList = new ArrayList<>();
        if (context != null && list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (isCancelled())
                    break;
                if (list.get(i) instanceof GeneralModel) {
                    GeneralModel generalModel = (GeneralModel) list.get(i);
                    for (M m : ms) {
                        if (m instanceof GeneralModel) {
                            GeneralModel modelToSearch = (GeneralModel) m;
                            if (generalModel.getImageId().equals(modelToSearch.getImageId())) {
                                indexList.add(i);
                                break;
                            }
                        } else if (m.equals(generalModel)) {
                            indexList.add(i);
                            break;
                        }
                    }
                } else {
                    for (M m : ms) {
                        if (m.equals(list.get(i))) {
                            indexList.add(i);
                            break;
                        }
                    }
                }
            }
        }
        return indexList;
    }

    @SafeVarargs
    @Override
    protected final void onProgressUpdate(N... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(ArrayList<Integer> t) {
        super.onPostExecute(t);
        if (t.size() == 0) {
            t.add(-1);
        }
        if (asyncResponse != null) {
            asyncResponse.onProcessFinished(t);
        }
    }
}
