package com.qingmei2.rximagepicker.ui.camera;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;

import com.qingmei2.rximagepicker.ui.BaseSystemPickerView;
import com.qingmei2.rximagepicker.ui.ICameraPickerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Observable;

public final class SystemCameraPickerView extends BaseSystemPickerView implements ICameraPickerView {

    private static final String TAG = SystemCameraPickerView.class.getSimpleName();
    private static Uri cameraPictureUrl;

    public static ICameraPickerView instance(FragmentManager fragmentManager) {
        SystemCameraPickerView fragment = (SystemCameraPickerView) fragmentManager.findFragmentByTag(TAG);
        if (fragment == null) {
            fragment = new SystemCameraPickerView();
            fragmentManager.beginTransaction()
                    .add(fragment, TAG)
                    .commit();
        }
        return fragment;
    }

    @Override
    public Observable<Uri> pickImage() {
        return getUriObserver();
    }

    @Override
    public void startRequest() {
        if (!checkPermission()) {
            return;
        }
        cameraPictureUrl = createImageUri();
        Intent pictureChooseIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        pictureChooseIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPictureUrl);

        startActivityForResult(pictureChooseIntent, CAMERA_REQUEST_CODE);
    }

    @Override
    public Uri getActivityResultUri(Intent data) {
        return cameraPictureUrl;
    }

    private Uri createImageUri() {
        ContentResolver contentResolver = getActivity().getContentResolver();
        ContentValues cv = new ContentValues();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        cv.put(MediaStore.Images.Media.TITLE, timeStamp);
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
    }
}