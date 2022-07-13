package com.example.permissionjsl.permission;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Nihal Srivastava on 6/07/22.
 */
public class JioPermitManager {

    static boolean loggingEnabled = true;


    public static void disableLogging() {
        loggingEnabled = false;
    }

    static void log(String message) {
        if (loggingEnabled) Log.d("Permissions", message);
    }


    // Check/Request a permission and call the callback methods of permission handler accordingly.

    public static void check(Context context, String permission, String rationale, PermissionHandler handler) {
        check(context, new String[]{permission}, rationale, null, handler);
    }

    //Check/Request a permission and call the callback methods of permission handler accordingly.

    public static void check(Context context, String permission, int rationaleId,
                             PermissionHandler handler) {
        String rationale = null;
        try {
            rationale = context.getString(rationaleId);
        } catch (Exception ignored) {
        }
        check(context, new String[]{permission}, rationale, null, handler);
    }

    //Check/Request permissions and call the callback methods of permission handler accordingly.

    public static void check(final Context context, String[] permissions, String rationale, Options options, final PermissionHandler handler) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            handler.onGranted();
            log("Android version < 23");
        } else {
            Set<String> permissionsSet = new LinkedHashSet<>();
            Collections.addAll(permissionsSet, permissions);
            boolean allPermissionProvided = true;
            for (String aPermission : permissionsSet) {
                if (context.checkSelfPermission(aPermission) != PackageManager.PERMISSION_GRANTED) {
                    allPermissionProvided = false;
                    break;
                }
            }

            if (allPermissionProvided) {
                handler.onGranted();
                log("Permission(s) " + (PermissionsActivity.permissionHandler == null ?
                        "already granted." : "just granted from settings."));
                PermissionsActivity.permissionHandler = null;

            } else {
                PermissionsActivity.permissionHandler = handler;
                ArrayList<String> permissionsList = new ArrayList<>(permissionsSet);

                Intent intent = new Intent(context, PermissionsActivity.class)
                        .putExtra(PermissionsActivity.EXTRA_PERMISSIONS, permissionsList)
                        .putExtra(PermissionsActivity.EXTRA_RATIONALE, rationale)
                        .putExtra(PermissionsActivity.EXTRA_OPTIONS, options);
                if (options != null && options.createNewTask) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(intent);
            }
        }
    }

    //Check/Request permissions and call the callback methods of permission handler accordingly.

    public static void check(final Context context, String[] permissions, int rationaleId,
                             Options options, final PermissionHandler handler) {
        String rationale = null;
        try {
            rationale = context.getString(rationaleId);
        } catch (Exception ignored) {
        }
        check(context, permissions, rationale, options, handler);
    }

    //Options to customize while requesting permissions.
    public static class Options implements Serializable {

        String settingsText = "Settings";
        String rationaleDialogTitle = "Permissions Required";
        String settingsDialogTitle = "Permissions Required";
        String settingsDialogMessage = "Required permission(s) have been set" +
                " not to ask again! Please provide them from settings.";
        boolean sendBlockedToSettings = true;
        boolean createNewTask = false;


        // Sets the button text for "settings" while asking user to go to settings.
        public Options setSettingsText(String settingsText) {
            this.settingsText = settingsText;
            return this;
        }

        //Sets the "Create new Task" flag in Intent, for when we're calling this library from within a Service or other
        public Options setCreateNewTask(boolean createNewTask) {
            this.createNewTask = createNewTask;
            return this;
        }

        //Set title for rational dialog
        public Options setRationaleDialogTitle(String rationaleDialogTitle) {
            this.rationaleDialogTitle = rationaleDialogTitle;
            return this;
        }

        //Set Title
        public Options setSettingsDialogTitle(String settingsDialogTitle) {
            this.settingsDialogTitle = settingsDialogTitle;
            return this;
        }

        //Settings Dialog
        public Options setSettingsDialogMessage(String settingsDialogMessage) {
            this.settingsDialogMessage = settingsDialogMessage;
            return this;
        }

        // Block Settings
        public Options sendDontAskAgainToSettings(boolean send) {
            sendBlockedToSettings = send;
            return this;
        }
    }

}
