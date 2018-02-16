package com.example.dm2.webservices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.samples.amazon.search.messages.Request;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.HttpsTransportSE;
import org.ksoap2.transport.Transport;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private String TAG="Response";
    private Button btnCalcular;
    private EditText edKm;
    private TextView yar;
    private String km;
    private SoapPrimitive resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCalcular=(Button) findViewById(R.id.btncalcular);
        edKm=(EditText)findViewById(R.id.txtkm);
        yar=(TextView) findViewById(R.id.txtyardas);

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                km=edKm.getText().toString();
                AsyncCallWS task=new AsyncCallWS();
                task.execute();
            }
        });
    }

    public class AsyncCallWS extends AsyncTask<Void,Void,Void>{

        protected void onPreExecute(){
            Log.i(TAG,"metodo onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG,"metodo onPreExecute");
            calcular();
            Log.i(TAG,"metodo onPreExecute");
            return null;
        }

        private void calcular(){

            String SOAP_ACTION="http://www.webservicex.net/length.asmx/ChangeLengthUnit";
            String METHOD_NAME="ChangeLengthUnit";
            String NAMESPACE="http://www.webservicex.net";
            String URL="http://www.webservicex.net/length.asmx";
            try{
                SoapObject request=new SoapObject(NAMESPACE, METHOD_NAME);

                //parametros
                PropertyInfo parametroValorKm= new PropertyInfo();
                PropertyInfo parametrokm= new PropertyInfo();
                PropertyInfo parametroYardas= new PropertyInfo();

                //valores de los parametros
                parametroValorKm.setName("LengthValue");
                parametroValorKm.setValue(edKm.getText().toString());

                parametrokm.setName("fromLengthUnit");
                parametrokm.setValue("Kilometers");

                parametroYardas.setName("toLengthUnit");
                parametroYardas.setValue("Yards");

                //añadimos los parametros que pide la pagina en el orden que lo pide
                request.addProperty(parametroValorKm);
                request.addProperty(parametrokm);
                request.addProperty(parametroYardas);


                SoapSerializationEnvelope  soapEnvelope= new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet=true;
                soapEnvelope.setOutputSoapObject(request);
                HttpTransportSE transport= new HttpTransportSE(URL);

                transport.call(SOAP_ACTION,soapEnvelope);
                resultado=(SoapPrimitive)soapEnvelope.getResponse();

                Toast.makeText(MainActivity.this, resultado.toString(), Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Log.i(TAG,"ERROR: "+e.getMessage());
            }
        }
    }


}
