package com.dpdtech.application.network.models.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class LoginResponse implements Parcelable {
    public boolean status;
    public int userID;
    public int roleID;
    public String validIPs;
    public String message;
    public int isAdmin;
    public int thirdPartyChainID;
    public int automateLicensePayment;
    public int chainID;
    public String country;
    public ArrayList<AllowedStore> allowedStores;
    public String industry;
    public String industrySubType;
    public ArrayList<StoreList> storeList;
    public int isDeviceRegistered;
    public int licenseNumber;
    public ArrayList<Object> clientInfo;
    public int gcmUserID;
    public String lastSerialNumber;
    public String fingerprint;
    public Object accessTokenAgri;
    public WhiteLabel whiteLabel;
    public int flapBarrierStore;
    public ArrayList<FlapBarrierCounter> flapBarrierCounters;

    protected LoginResponse(Parcel in) {
        status = in.readByte() != 0;
        userID = in.readInt();
        roleID = in.readInt();
        validIPs = in.readString();
        message = in.readString();
        isAdmin = in.readInt();
        thirdPartyChainID = in.readInt();
        automateLicensePayment = in.readInt();
        chainID = in.readInt();
        country = in.readString();
        allowedStores = in.createTypedArrayList(AllowedStore.CREATOR);
        industry = in.readString();
        industrySubType = in.readString();
        storeList = in.createTypedArrayList(StoreList.CREATOR);
        isDeviceRegistered = in.readInt();
        licenseNumber = in.readInt();
        clientInfo = (ArrayList<Object>) in.readSerializable();
        gcmUserID = in.readInt();
        lastSerialNumber = in.readString();
        fingerprint = in.readString();
        whiteLabel = in.readParcelable(WhiteLabel.class.getClassLoader());
        flapBarrierStore = in.readInt();
        flapBarrierCounters = in.createTypedArrayList(FlapBarrierCounter.CREATOR);
    }

    public static final Creator<LoginResponse> CREATOR = new Creator<LoginResponse>() {
        @Override
        public LoginResponse createFromParcel(Parcel in) {
            return new LoginResponse(in);
        }

        @Override
        public LoginResponse[] newArray(int size) {
            return new LoginResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (status ? 1 : 0));
        dest.writeInt(userID);
        dest.writeInt(roleID);
        dest.writeString(validIPs);
        dest.writeString(message);
        dest.writeInt(isAdmin);
        dest.writeInt(thirdPartyChainID);
        dest.writeInt(automateLicensePayment);
        dest.writeInt(chainID);
        dest.writeString(country);
        dest.writeTypedList(allowedStores);
        dest.writeString(industry);
        dest.writeString(industrySubType);
        dest.writeTypedList(storeList);
        dest.writeInt(isDeviceRegistered);
        dest.writeInt(licenseNumber);
        dest.writeSerializable(clientInfo);
        dest.writeInt(gcmUserID);
        dest.writeString(lastSerialNumber);
        dest.writeString(fingerprint);
        dest.writeParcelable(whiteLabel, flags);
        dest.writeInt(flapBarrierStore);
        dest.writeTypedList(flapBarrierCounters);
    }

    // Parcelable implementation for inner classes
    public static class AllowedStore implements Parcelable {
        public int storeID;

        protected AllowedStore(Parcel in) {
            storeID = in.readInt();
        }

        public static final Creator<AllowedStore> CREATOR = new Creator<AllowedStore>() {
            @Override
            public AllowedStore createFromParcel(Parcel in) {
                return new AllowedStore(in);
            }

            @Override
            public AllowedStore[] newArray(int size) {
                return new AllowedStore[size];
            }
        };

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(storeID);
        }

        @Override
        public int describeContents() {
            return 0;
        }
    }

    public static class StoreList implements Parcelable {
        public int storeID;
        public String storeName;

        protected StoreList(Parcel in) {
            storeID = in.readInt();
            storeName = in.readString();
        }

        public static final Creator<StoreList> CREATOR = new Creator<StoreList>() {
            @Override
            public StoreList createFromParcel(Parcel in) {
                return new StoreList(in);
            }

            @Override
            public StoreList[] newArray(int size) {
                return new StoreList[size];
            }
        };

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(storeID);
            dest.writeString(storeName);
        }

        @Override
        public int describeContents() {
            return 0;
        }
    }

    public static class WhiteLabel implements Parcelable {
        public String brandName;

        protected WhiteLabel(Parcel in) {
            brandName = in.readString();
        }

        public static final Creator<WhiteLabel> CREATOR = new Creator<WhiteLabel>() {
            @Override
            public WhiteLabel createFromParcel(Parcel in) {
                return new WhiteLabel(in);
            }

            @Override
            public WhiteLabel[] newArray(int size) {
                return new WhiteLabel[size];
            }
        };

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(brandName);
        }

        @Override
        public int describeContents() {
            return 0;
        }
    }

    public static class FlapBarrierCounter implements Parcelable {
        public int counterID;
        public String counterName;

        protected FlapBarrierCounter(Parcel in) {
            counterID = in.readInt();
            counterName = in.readString();
        }

        public static final Creator<FlapBarrierCounter> CREATOR = new Creator<FlapBarrierCounter>() {
            @Override
            public FlapBarrierCounter createFromParcel(Parcel in) {
                return new FlapBarrierCounter(in);
            }

            @Override
            public FlapBarrierCounter[] newArray(int size) {
                return new FlapBarrierCounter[size];
            }
        };

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(counterID);
            dest.writeString(counterName);
        }

        @Override
        public int describeContents() {
            return 0;
        }
    }
}