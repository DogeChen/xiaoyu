package com.ainemo.pad;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;

import com.ainemo.pad.Contact.Record.CallRecord;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Silver on 2017/7/8.
 */

public class RecordUtil {
    private static final String TAG = "RecordUtil";

    public static List<CallRecord> getCallLog(ContentResolver contentResolver){
        Cursor cursor;

        cursor = contentResolver.query(CallLog.Calls.CONTENT_URI,
                new String[]{
                        CallLog.Calls.CACHED_NAME,
                        CallLog.Calls.NUMBER,
                        CallLog.Calls.TYPE,
                        CallLog.Calls.DATE,
                        CallLog.Calls.DURATION,
                        CallLog.Calls.CACHED_PHOTO_URI,
                        CallLog.Calls.CACHED_PHOTO_ID
                }, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        List<CallRecord> list=new ArrayList<>();
        int i = 0;
        if (cursor != null && cursor.getCount() > 0) {
            for (cursor.moveToFirst(); !cursor.isAfterLast() && i < 200; cursor.moveToNext()) {
                String name = cursor.getString(0);
                String number = cursor.getString(1);
                int type = Integer.parseInt(cursor.getString(2));

//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date date = new Date(Long.parseLong(cursor.getString(3)));
//                String dateString = simpleDateFormat.format(date);
                long date=Long.parseLong(cursor.getString(3));
                int callDuration = Integer.parseInt(cursor.getString(4));
                String photoUri = cursor.getString(5);
                Integer contactId = cursor.getInt(6);
                Uri uriPhoto = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
                Log.d(TAG, "getCallLog: name="+name+" photoUri="+photoUri+" contactId="+contactId);
                CallRecord callRecord=new CallRecord(name,"",number,type,date,callDuration,uriPhoto.toString());
                list.add(callRecord);
            }
            cursor.close();
        }
        return list;
    }

    public static boolean writeCallLog(ContentResolver contentResolver, CallRecord callRecord) {
        ContentValues values = new ContentValues();
        values.put(CallLog.Calls.CACHED_NAME, callRecord.getName());
        values.put(CallLog.Calls.NUMBER, callRecord.getXiaoyuId());
        values.put(CallLog.Calls.TYPE, callRecord.getType());
        values.put(CallLog.Calls.DATE, callRecord.getDate());
        values.put(CallLog.Calls.DURATION, callRecord.getDuration());
        values.put(CallLog.Calls.CACHED_PHOTO_URI, callRecord.getImageUrl());
        if (null != contentResolver.insert(CallLog.Calls.CONTENT_URI, values)) {
            return true;
        } else {
            return false;
        }
    }
}
