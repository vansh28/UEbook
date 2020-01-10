package com.ue.uebook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.text.TextUtils;
import android.util.Patterns;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public  class Commonutils {public static final String TICKET_DETAILS = "ticketDetails";
    public static final String SCREEN_TITLE = "screenTitle";

    public static final String SELECTED_CIRCLE = "selectedCircle";
    public static final String SELECTED_SITE = "selectedSite";
    public static final String SELECTED_SSA = "selectedSSA";
    public static final String SELECTED_CIRCLE_ID = "selectedCircleId";
    public static final String SELECTED_SITE_ID = "selectedSiteId";
    public static final String SELECTED_SSA_ID = "selectedSSAId";
    public static final String USER_ROLE = "userRole";
    public static final String ADMIN = "admin";
    public static final String ENGINEER = "engineer";

    public static final String PENDING = "Pending";
    public static final String APPROVED = "Approved";
    public static final String CLOSED = "closed";
    public static final String ISSUE_OTHER = "Other";
    public static final String ISSUE_OTHER_ID = "6";
    public static final String TICKET_STATUS_RAISED = "0";
    public static final String TICKET_STATUS_ASSIGNED = "1";
    public static final String TICKET_STATUS_SERVING = "2";
    public static final String TICKET_STATUS_REQUEST_TO_CLOSE = "3";
    public static final String TICKET_STATUS_CLOSED = "4";

    public static final String HYPHEN = "-";
    public static final String INDIA_COUNTRY_CODE = "+91";

    public static final String RESPONSE_FAILED = "failed";
    public static final String RESPONSE_SUCCESS = "success";
    public static final String RESPONSE = "response";
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    // location updates interval - 10sec

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    public static final int REQUEST_GPS_CHECK_SETTINGS = 2;

    private static boolean isValidMobile(String phone) {
        return Patterns.PHONE.matcher(phone).matches();
    }
    public static boolean isValidEmail(CharSequence target) {

        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());

    }


    public static String validatePhoneNumber(String phNo) {
        if (TextUtils.isEmpty(phNo))

            return "Enter Mobile number";

        else if (!isValidMobile(phNo))

            return "Enter Valid Mobile number";
        else if (phNo.length() != 10)

            return "Enter Valid Mobile number";
        else
            return null;
    }


    @SuppressLint("NewApi")
    public static String getAddressFromLocation(final double latitude, final double longitude, Context context) {
        Geocoder geocoder;
        String address = "";
        List<Address> addresses;
        if (context != null) {
            try {
                geocoder = new Geocoder(Objects.requireNonNull(context), Locale.getDefault());
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                address = addresses.get(0).getFeatureName()+" - "+addresses.get(0).getSubLocality()+" , "+addresses.get(0).getLocality(); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
               if (addresses != null && addresses.size() > 0) {
                   String addresss = addresses.get(0).getAddressLine(0);
                   String city = addresses.get(0).getLocality();
                   String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String local =addresses.get(0).getSubLocality();
                    String knownName = addresses.get(0).getFeatureName();
                    String ff=addresses.get(0).getPremises();
//                  locationTxt.setText(address + " " + city + " " + country);
               }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return address;
    }
}