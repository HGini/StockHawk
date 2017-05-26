package com.udacity.stockhawk.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Hemangini on 5/21/17.
 */

public class CustomRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new CustomRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}