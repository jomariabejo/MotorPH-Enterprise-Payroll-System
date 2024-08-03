package com.jomariabejo.motorph.utility;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class NetworkUtils {
    public static String getLocalIPAddress() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "Unable to determine IP address";
        }
    }

    public static Instant getCurrentInstant() {
        return LocalDateTime.now(ZoneId.of("Asia/Manila")).toInstant(ZoneId.of("Asia/Manila").getRules().getOffset(LocalDateTime.now()));
    }

    public static void main(String[] args) {
        System.out.println(TimestampUtils.getCurrentTimestamp());
    }
}
