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