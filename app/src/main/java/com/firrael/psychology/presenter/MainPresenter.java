package com.firrael.psychology.presenter;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.firrael.psychology.App;
import com.firrael.psychology.RConnectorService;
import com.firrael.psychology.MainActivity;

import icepick.State;

import static com.firrael.psychology.Requests.REQUEST_SAVE_IMAGE;

/**
 * Created by Railag on 02.06.2016.
 */
public class MainPresenter extends BasePresenter<MainActivity> {

    @State
    Bitmap image;

    @State
    long userId;

    @State
    long groupId;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        RConnectorService service = App.restService();

       /* restartableLatestCache(REQUEST_SAVE_IMAGE,
                () -> service.saveImage(image)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                MainActivity::onSuccessSaveImage,
                MainActivity::onErrorSaveImage);

        restartableLatestCache(REQUEST_PN_ADD_USER,
                () -> service.addUser(userId, groupId)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                MainActivity::onSuccessAddUser,
                MainActivity::onErrorAddUser);*/
    }

    public void saveImage(Bitmap image) {
        this.image = image;
        start(REQUEST_SAVE_IMAGE);
    }
}
