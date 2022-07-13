package com.example.permissionjsl.permission;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Nihal Srivastava on 6/07/22.
 */

public abstract class PermissionHandler {

    //If all granted
    public abstract void onGranted();

    //If some of permission denied from List
    public void onDenied(Context context, ArrayList<String> deniedPermissions) {
        if (JioPermitManager.loggingEnabled) {
            StringBuilder builder = new StringBuilder();
            builder.append("Denied:");
            for (String permission : deniedPermissions) {
                builder.append(" ");
                builder.append(permission);
            }
            JioPermitManager.log(builder.toString());
        }
        Toast.makeText(context, "Permission Denied.", Toast.LENGTH_SHORT).show();
    }

    //If user blocked any permission
    public boolean onBlocked(Context context, ArrayList<String> blockedList) {
        if (JioPermitManager.loggingEnabled) {
            StringBuilder builder = new StringBuilder();
            builder.append("Don't ask again:");
            for (String permission : blockedList) {
                builder.append(" ");
                builder.append(permission);
            }
            JioPermitManager.log(builder.toString());
        }
        return false;
    }

    //For time being blocked permission
    public void onJustBlocked(Context context, ArrayList<String> justBlockedList,
                              ArrayList<String> deniedPermissions) {
        if (JioPermitManager.loggingEnabled) {
            StringBuilder builder = new StringBuilder();
            builder.append("For time being not to ask again:");
            for (String permission : justBlockedList) {
                builder.append(" ");
                builder.append(permission);
            }
            JioPermitManager.log(builder.toString());
        }
        onDenied(context, deniedPermissions);
    }

}
