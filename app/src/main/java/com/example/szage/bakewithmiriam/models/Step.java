package com.example.szage.bakewithmiriam.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Step class contains preparation step of the recipe,
 * it's short-, long description, and two (types of) video URLs and their getter methods.
 */

public class Step implements Parcelable{

    private String mShortDescription;
    private String mLongDescription;
    private String mVideoUrl;
    private String mThumbnailURL;


    public Step(String shortDescription, String longDescription, String videoUrl, String thumbnailURL) {
        mShortDescription = shortDescription;
        mLongDescription = longDescription;
        mVideoUrl = videoUrl;
        mThumbnailURL = thumbnailURL;
    }

    protected Step(Parcel in) {
        mShortDescription = in.readString();
        mLongDescription = in.readString();
        mVideoUrl = in.readString();
        mThumbnailURL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mShortDescription);
        dest.writeString(mLongDescription);
        dest.writeString(mVideoUrl);
        dest.writeString(mThumbnailURL);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    public String getShortDescription() {
        return mShortDescription;
    }

    public String getLongDescription() {
        return mLongDescription;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public String getThumbnailURL() {
        return mThumbnailURL;
    }
}
