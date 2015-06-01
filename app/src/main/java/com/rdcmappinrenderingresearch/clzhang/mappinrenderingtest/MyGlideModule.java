package com.rdcmappinrenderingresearch.clzhang.mappinrenderingtest;;

import android.content.Context;
import android.provider.ContactsContract;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;

public class MyGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }
//
    @Override
    public void registerComponents(Context context, Glide glide) {
//        glide.register(Model.class, ContactsContract.Data.class, new MyModelLoader());
    }
}
