package io.dogan.suarizatakip;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Test {

    public static final String URL = "http://95.0.0.91/Datamanager/service.asmx";
    public static final String NAMESPACE = "http://tempuri.org/";
    public static final String SOAP_ACTION = "http://tempuri.org/";
    private static final String METHOD_NAME = "DataManager_getDATAset";


    public static void main(String[] args){
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        PropertyInfo baglantiTuru = createPropertyInfo("baglantituru_ADO_POST_ODBC", "POST", String.class);
        request.addProperty(baglantiTuru);

        PropertyInfo veritabani = createPropertyInfo("veritabaniadi", "kbs", String.class);
        request.addProperty(veritabani);

        PropertyInfo tablo = createPropertyInfo("tablo", "Liste", String.class);
        request.addProperty(tablo);

        PropertyInfo kosul = createPropertyInfo("kosul", "select * from su_arizatakip", String.class);
        request.addProperty(kosul);

        PropertyInfo projeAdi = createPropertyInfo("ProjeAdi", "android", String.class);
        request.addProperty(projeAdi);

        PropertyInfo kullanici = createPropertyInfo("Kullanici", "android", String.class);
        request.addProperty(kullanici);

        // n galiba veritabanindan donulecek sonuc sayisini belirliyor...
        PropertyInfo n = createPropertyInfo("n", 2, long.class);
        request.addProperty(n);

        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        //Set envelope as dotNet
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            // Invoke web service
            androidHttpTransport.call(SOAP_ACTION + METHOD_NAME, envelope);
            // Get the response
            List<String> items = new ArrayList<>();
            fillResponseItems((SoapObject) envelope.getResponse(), items);

            for (String s : items) {
                System.out.println(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void fillResponseItems(SoapObject object, List<String> items) {
        StringBuilder s = new StringBuilder();

        PropertyInfo propertyInfo = new PropertyInfo();
        int propertyCount = object.getPropertyCount();
        for (int i = 0; i < propertyCount; i++) {
            object.getPropertyInfo(i, propertyInfo);
            String name = propertyInfo.getName();
            if ("schema".equals(name)) {
                // schema yi okumaya gerek yok
                continue;
            }

            Object property = object.getProperty(i);
            if (property instanceof SoapObject) {
                fillResponseItems((SoapObject) property, items);
            } else if (property instanceof SoapPrimitive) {
                SoapPrimitive primitive = (SoapPrimitive) property;
                s.append(name).append(": ").append(primitive).append("\n");
            }
        }

        if (s.length() != 0) {
            items.add(s.toString());
        }
    }

    private static PropertyInfo createPropertyInfo(String name, Object value, Class type) {
        PropertyInfo prop = new PropertyInfo();
        prop.setName(name);
        prop.setValue(value);
        prop.setType(type);
        return prop;
    }
}
