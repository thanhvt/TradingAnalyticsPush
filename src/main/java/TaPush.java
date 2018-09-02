/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.turo.pushy.apns.ApnsClient;
import com.turo.pushy.apns.ApnsClientBuilder;
import com.turo.pushy.apns.PushNotificationResponse;
import com.turo.pushy.apns.auth.ApnsSigningKey;
import com.turo.pushy.apns.util.SimpleApnsPushNotification;
import com.turo.pushy.apns.util.TokenUtil;
import com.turo.pushy.apns.util.concurrent.PushNotificationFuture;
import java.io.File;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Thanh
 */
public class TaPush {

    /**
     * @param args the command line arguments
     */
    private static String SERVER_KEY = "AAAAArGXr1c:APA91bGJ4hHWw67-dB9iLb3v-TX_c33eFf5qhvcwzMG-iqRUFfvOsEWqlWtvEFPq6v_qBEEz7SMRCvc1ZLTSzKOrbsoySJ8n64ceCV_L6yv59D7zioUTtiidk3_LWpHFqWUB4QSwTpwg";
    private static String DEVICE_TOKEN = "123";
   
    private static String iDEVICE_TOKEN = "FB8E9605A540F831917BD934D4D0CA866AE82086BE38FE16159CEAEDC5B771AB";
    private static String iPATH_TO_P12_CERT = "AuthKey.p8";
    private static String iCERT_PASSWORD = "Admin@123";

    public static void main(String[] args) {
        // TODO code application logic here

        String title = "Hello Trader Tuyên. Thay device token để push";
        String message = "This is auto message from Trader Pro Server. Your key already added";

//        title = "BNB |2| ADA";
//        message = "<b> *** ADA *** </b>&nbsp;<b>PRI OP: </b>0.00014540<br/><b>PRI HT: </b>0.00014880&nbsp;<b>PRI 1H: </b>0.00014490<br/><b>PRI 5P: </b>0.00014740&nbsp;<b>PRI 30P: </b>0.00014570<br/><b>PRI 4H: </b>0.00014170&nbsp;<b>PRI 12H: </b>0.00014520<br/><b>VOL HT: </b>8.6542&nbsp;<b>VOL 2H: </b>3.0818<br/><b>VOL 1H: </b>8.2202&nbsp;<b>VOL TB: </b>5.5173<br/><b>Number of Buyer/Seller: </b>881/326<br/><b>Number of Taker/Maker: </b>1152/274<br/> <br/><b></b>";
//
//        title = "BNB ADA BUYYY";
//        message = "PRICE: ~" + "0.00002322";
//
//        title = "BNB TakeProfit ADA";
//        message = "<b> Sell " + "XMR" + ": </b>" + "0.13040304" + "&nbsp;"
//                + "<b> Buy: </b>" + "0.01540900" + "<br/>"
//                + "<b> ===> PROFIT: " + "5.23" + "%</b>";

        Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        int min = rightNow.get(Calendar.MINUTE);
        int nam = rightNow.get(Calendar.YEAR);
        int isecond = rightNow.get(Calendar.SECOND);
        int mlsec = rightNow.get(Calendar.MILLISECOND);
        int thang = rightNow.get(Calendar.MONTH) + 1;
        int ngay = rightNow.get(Calendar.DAY_OF_MONTH);
        String strTime = hour + ":" + min + ":" + isecond + " - " + ngay + "." + thang;
        try {
            title = "PC " + title + " *** " + strTime;
            androidPush(title, message);
            iOSPush(title, message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void androidPush(String title, String message) {

        System.out.println("Mess: " + message);
        Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        int min = rightNow.get(Calendar.MINUTE);
        int nam = rightNow.get(Calendar.YEAR);
        int isecond = rightNow.get(Calendar.SECOND);
        int mlsec = rightNow.get(Calendar.MILLISECOND);
        int thang = rightNow.get(Calendar.MONTH) + 1;
        int ngay = rightNow.get(Calendar.DAY_OF_MONTH);
        String strTime = hour + ":" + min + ":" + isecond + " - " + ngay + "." + thang;
        try {
            title = "HM " + title + " *** " + strTime;
            String pushMessage = "{\"data\":{\"title\":\""
                    + title
                    + "\",\"message\":\""
                    + message
                    + "\"},\"to\":\""
                    + DEVICE_TOKEN
                    + "\"}";
            pushMessage = "{\n"
                    + "   \"to\" : \"" + DEVICE_TOKEN + "\",\n"
                    + "   \"data\" : {\n"
                    + "     \"title\" : \"" + title + "\",\n"
                    + "     \"message\" : \"" + message + "\",\n"
                    + "   }\n"
                    + " }";
            JSONObject json = new JSONObject(pushMessage);
            pushMessage = json.toString();
            System.out.println(pushMessage);
            // Create connection to send FCM Message request.
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "key=" + SERVER_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Send FCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(pushMessage.getBytes());
            System.out.println(conn.getResponseCode());
            System.out.println(conn.getResponseMessage());
        } catch (Exception e) {

        }

    }

    public static void iOSPush(String title, String message) {
        try {
            ApnsClient service = new ApnsClientBuilder()
                    .setApnsServer(ApnsClientBuilder.DEVELOPMENT_APNS_HOST)
                    .setSigningKey(ApnsSigningKey.loadFromPkcs8File(new File(iPATH_TO_P12_CERT),
                            "Q8QE9HFRAE", "8H4ZHZAL2H"))
                    .build();
            // final ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
            String mBuild = "";
            if (message.contains("VOL HT")) {
                // "title":"HM BNB |2| AE *** 23:36:57 - 15.8"
                String strCoin = title.substring(title.lastIndexOf("|") + 2, title.indexOf("***") - 1);
                strCoin = strCoin.trim();
                String strTime = title.substring(title.indexOf("***") + 4, title.indexOf(" - ")).trim();
                String strCase = title.contains("|") ? title.substring(title.indexOf("|") + 1, title.lastIndexOf("|")).trim() : "";

                String strExchange = title.contains("BNB") ? "Binance" : "Bittrex";
                String strGia = message.contains("PRI HT: ") ? message.substring(message.indexOf("PRI HT: ") + 12, message.indexOf("PRI HT: ") + 22) : "";
                String strVolHT = message.contains("VOL HT: ") ? message.substring(message.indexOf("VOL HT: ") + 12, message.indexOf("VOL HT: ") + 17) : "";
                String strVolTB = message.contains("VOL TB: ") ? message.substring(message.indexOf("VOL TB: ") + 12, message.indexOf("VOL TB: ") + 17) : "";
                String strBuySell = "";
                if (message.contains("Buyer/Seller")) {
                    strBuySell = message.substring(message.indexOf("Buyer/Seller") + 18, message.lastIndexOf("Number") - 8);
                }
                String strTM = "";
                if (message.contains("Taker/Maker")) {
                    strTM = message.substring(message.indexOf("Taker/Maker") + 17, message.lastIndexOf("<br/>") - 6);
                }
                /*
                Mess: <b> *** AE *** </b>&nbsp;<b>PRI OP: </b>0.00014540<br/><b>PRI HT: </b>0.00014880&nbsp;<b>PRI 1H: </b>0.00014490<br/><b>PRI 5P: </b>0.00014740&nbsp;<b>PRI 30P: </b>0.00014570<br/><b>PRI 4H: </b>0.00014170&nbsp;<b>PRI 12H: </b>0.00014520<br/><b>VOL HT: </b>8.6542&nbsp;<b>VOL 2H: </b>3.0818<br/><b>VOL 1H: </b>8.2202&nbsp;<b>VOL TB: </b>5.5173<br/><b>Number of Buyer/Seller: </b>881/326<br/><b>Number of Taker/Maker: </b>1152/274<br/> <br/><b></b>
                 */
                String mmessage = message.replace("</b>", " ");
                mmessage = mmessage.replace("<br/><b>", "|");
                mmessage = mmessage.replace("&nbsp", "");
                mmessage = mmessage.replace("<br/>", "");
                mmessage = mmessage.replace(";", "");
                mmessage = mmessage.replace("<b>", "|");
                mmessage = mmessage.replace("|", " | ");

                mBuild = "{\n"
                        + "  \"aps\": {\n"
                        + "    \"alert\": {\n"
                        + "      \"subtitle\": \"" + title + "\",\n"
                        + "      \"body\": \"" + mmessage + "\",\n"
                        + "      \"title\": \"" + "Boss $$$ " + strCoin + "\"\n"
                        + "    },\n"
                        + "    \"category\": \"VERSION_NORMAL\"\n"
                        + "  },\n"
                        + "  \"Info\": {\n"
                        + "    \"TIME_PUSH\": \"" + strTime + "\",\n"
                        + "    \"VOL_TB\": \"" + strVolTB + "\",\n"
                        + "    \"TAKER_MAKER\": \"" + strTM + "\",\n"
                        + "    \"VOL_HT\": \"" + strVolHT + "\",\n"
                        + "    \"EXCHANGE\": \"" + strExchange + "\",\n"
                        + "    \"BUYSELL\": \"" + strBuySell + "\",\n"
                        + "    \"PRI_HT\": \"" + strGia + "\",\n"
                        + "    \"COIN\": \"" + strCoin + "\",\n"
                        + "    \"CASE\": \"" + strCase + "\"\n"
                        + "  }\n"
                        + "}";
//                payloadBuilder.setCategoryName("VERSION_NORMAL");
//                payloadBuilder.setAlertTitle("Boss $$$ " + strCoin);
//                payloadBuilder.setAlertSubtitle(title);
//                payloadBuilder.setAlertBody(mmessage);
//                payloadBuilder.addCustomProperty("COIN", strCoin);
//                payloadBuilder.addCustomProperty("TIME_PUSH", strTime);
//                payloadBuilder.addCustomProperty("CASE", strCase);
//                payloadBuilder.addCustomProperty("EXCHANGE", strExchange);
//                payloadBuilder.addCustomProperty("PRI_HT", strGia);
//                payloadBuilder.addCustomProperty("VOL_HT", strVolHT);
//                payloadBuilder.addCustomProperty("VOL_TB", strVolTB);
//                payloadBuilder.addCustomProperty("BUYSELL", strBuySell);
//                payloadBuilder.addCustomProperty("TAKER_MAKER", strTM);
            } else if (title.contains("BUYYY")) {
                String strTime = title.substring(title.indexOf("***") + 4, title.indexOf(" - ")).trim();
                String strCoin = title.substring(title.lastIndexOf("BNB") + 4, title.indexOf("BUYYY") - 1);
                strCoin = strCoin.trim();
                String strGiaMua = message.substring(message.indexOf("PRICE: ") + 8);
                mBuild = "{\n"
                        + "  \"aps\": {\n"
                        + "    \"alert\": {\n"
                        + "      \"subtitle\": \"" + title + "\",\n"
                        + "      \"body\": \"" + message + "\",\n"
                        + "      \"title\": \"" + "BUYYY NOW" + "\"\n"
                        + "    },\n"
                        + "    \"category\": \"VERSION_VBUY\"\n"
                        + "  },\n"
                        + "  \"Info\": {\n"
                        + "    \"TIME_PUSH\": \"" + strTime + "\",\n"
                        + "    \"EXCHANGE\": \"" + "Binance" + "\",\n"
                        + "    \"PRI_BUY\": \"" + strGiaMua + "\",\n"
                        + "    \"COIN\": \"" + strCoin + "\"\n"
                        + "  }\n"
                        + "}";
            } else if (title.contains("StopLoss") || title.contains("TakeProfit")) {
                String strTime = "";
                String strCoin = "";
                String strGiaMua = "GIA_MUA";
                String strGiaBan = "GIA_BAN";
                String strTimeBan = "TIME_BAN";
                String strProfit = "PROFIT";

                if (title.contains("***")) {
                    strTime = title.substring(title.indexOf("***") + 4, title.indexOf(" - ")).trim();
                }
                if (title.contains("TakeProfit")) {
                    strCoin = title.substring(title.lastIndexOf("TakeProfit") + 10, title.indexOf("***") - 1);
                    strCoin = strCoin.trim();
                } else if (title.contains("StopLoss")) {
                    strCoin = title.substring(title.lastIndexOf("StopLoss") + 8, title.indexOf("***") - 1);
                    strCoin = strCoin.trim();
                }

                strGiaBan = message.substring(message.indexOf(":") + 6, message.indexOf(":") + 16);
                strGiaBan = strGiaBan.trim();
                strTimeBan = title.substring(title.indexOf("***") + 4, title.indexOf(" - ")).trim();
                strProfit = "+" + message.substring(message.lastIndexOf(":") + 1, message.lastIndexOf("</b>")).trim();
                strGiaMua = message.substring(message.indexOf("Buy:") + 9, message.indexOf("Buy") + 19).trim();
                /*
                 title = "BNB TakeProfit ADA";
                 message = "<b> Sell " + "XMR" + ": </b>" + "0.13040304" + "&nbsp;"
                        + "<b> Buy: </b>" + "0.01540900" + "<br/>"
                        + "<b> ===> PROFIT: " + "5.23" + "%</b>";
                 */
                String mmessage = message.replace("</b>", " ");
                mmessage = mmessage.replace("&nbsp;", "");
                mmessage = mmessage.replace("<br/>", "");
                mmessage = mmessage.replace("<b>", "|");
                if (title.contains("TakeProfit")) {
                    mBuild = "{\n"
                            + "  \"aps\": {\n"
                            + "    \"alert\": {\n"
                            + "      \"subtitle\": \"" + title + "\",\n"
                            + "      \"body\": \"" + mmessage + "\",\n"
                            + "      \"title\": \"" + "Take profit NOWWW" + "\"\n"
                            + "    },\n"
                            + "    \"category\": \"VERSION_VSELL\"\n"
                            + "  },\n"
                            + "  \"Info\": {\n"
                            + "    \"TIME_PUSH\": \"" + strTime + "\",\n"
                            + "    \"PRI_BUY\": \"" + strGiaMua + "\",\n"
                            + "    \"TIME_SELL\": \"" + strTimeBan + "\",\n"
                            + "    \"PROFIT\": \"" + strProfit + "\",\n"
                            + "    \"PRI_SELL\": \"" + strGiaBan + "\",\n"
                            + "    \"COIN\": \"" + strCoin + "\"\n"
                            + "  }\n"
                            + "}";
                } else if (title.contains("StopLoss")) {
                    mBuild = "{\n"
                            + "  \"aps\": {\n"
                            + "    \"alert\": {\n"
                            + "      \"subtitle\": \"" + title + "\",\n"
                            + "      \"body\": \"" + mmessage + "\",\n"
                            + "      \"title\": \"" + "Stop loss NOWWW" + "\"\n"
                            + "    },\n"
                            + "    \"category\": \"VERSION_VSELL\"\n"
                            + "  },\n"
                            + "  \"Info\": {\n"
                            + "    \"TIME_PUSH\": \"" + strTime + "\",\n"
                            + "    \"PRI_BUY\": \"" + strGiaMua + "\",\n"
                            + "    \"TIME_SELL\": \"" + strTimeBan + "\",\n"
                            + "    \"PROFIT\": \"" + strProfit + "\",\n"
                            + "    \"PRI_SELL\": \"" + strGiaBan + "\",\n"
                            + "    \"COIN\": \"" + strCoin + "\"\n"
                            + "  }\n"
                            + "}";
                }
//                payloadBuilder.setCategoryName("VERSION_MUA");
//                payloadBuilder.setAlertTitle(title);
//                payloadBuilder.setAlertSubtitle(title);
//                payloadBuilder.setAlertBody(mmessage);
//                payloadBuilder.addCustomProperty("COIN", strCoin);
//                payloadBuilder.addCustomProperty("TIME_PUSH", strTime);
//                payloadBuilder.addCustomProperty("GIA_MUA", strGiaMua);
//                payloadBuilder.addCustomProperty("GIA_BAN", "GIA_BAN");
//                payloadBuilder.addCustomProperty("TIME_BAN", "TIME_BAN");
//                payloadBuilder.addCustomProperty("PROFIT", "PROFIT");
            } else {
//                payloadBuilder.setAlertTitle(title);
//                payloadBuilder.setAlertSubtitle(title);
//                payloadBuilder.setAlertBody("Hello World");
                mBuild = "{\n"
                        + "  \"aps\": {\n"
                        + "    \"alert\": {\n"
                        + "      \"subtitle\": \"" + title + "\",\n"
                        + "      \"body\": \"" + message + "\",\n"
                        + "      \"title\": \"" + title + "\"\n"
                        + "    },\n"
                        + "    \"category\": \"NOTIFICATION_NORMAL\"\n"
                        + "  }\n"
                        + "}";

            }

            final String token = TokenUtil.sanitizeTokenString(iDEVICE_TOKEN);

            SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(token, "com.vnittech.vn.TradeAuto", mBuild);
            PushNotificationFuture sendNotificationFuture = service.sendNotification(pushNotification);
            try {
                final PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse
                        = (PushNotificationResponse<SimpleApnsPushNotification>) sendNotificationFuture.get();

                if (pushNotificationResponse.isAccepted()) {
                    System.out.println("Push notification accepted by APNs gateway. " + pushNotificationResponse.getRejectionReason());
                } else {
                    System.out.println("Notification rejected by the APNs gateway: "
                            + pushNotificationResponse.getRejectionReason());

                    if (pushNotificationResponse.getTokenInvalidationTimestamp() != null) {
                        System.out.println("\t…and the token is invalid as of "
                                + pushNotificationResponse.getTokenInvalidationTimestamp());
                    }
                }
            } catch (final ExecutionException e) {
                System.err.println("Failed to send push notification.");
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.err.println("Failed to send push notification." + e.getMessage());
            e.printStackTrace();
        }
    }

}
