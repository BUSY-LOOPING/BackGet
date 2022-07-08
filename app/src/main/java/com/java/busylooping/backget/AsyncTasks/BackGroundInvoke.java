package com.java.busylooping.backget.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

public class BackGroundInvoke<T, M, N> extends AsyncTask<T, M, N> {

    //    private final Object objectToInvokeOn;
//    private final Method method;
    private final WeakReference<Context> contextWeakReference;
    private Command command;

    public interface CommandInt {
        void execute(Object[] data);
    }

    public static class Command implements CommandInt {

        @Override
        public void execute(Object[] data) {

        }
    }

    public BackGroundInvoke(Command command, Context context) {
        this.contextWeakReference = new WeakReference<>(context);
//        this.objectToInvokeOn = objectToInvokeOn;
//        this.method = method;
        this.command = command;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @SafeVarargs
    @Override
    protected final N doInBackground(T... ts) {
        command.execute(ts);
        return null;
    }

    @Override
    protected void onPostExecute(N n) {
        super.onPostExecute(n);
    }
}
