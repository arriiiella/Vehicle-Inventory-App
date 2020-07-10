package uk.ac.mmu.advprog.mmuvehiclesapp1;

import android.support.design.internal.ThemeEnforcement;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * The purpose of this class is to update an existing vehicle in the database.
 * This class does this by grabbing the information from the vehicle and populating
 * an a separate editText with each piece of information. The user can then override any
 * information then update the vehicle
 * @author Ariella Faber 17006468
 * @version 1.0
 */
public class UpdateVehicles extends AppCompatActivity
{

    EditText make, model, year, price, license_number, colour, doors, transmission, mileage, fuel_type, engine_size, body_style, condition, notes;
    FloatingActionButton btnUpdate;

    /**
     * This method displays the update activity on the creation of the page
     * It populates the edit Text with the vehicle's information then grabs the
     * data in each and sets them to appropriate variables created.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_vehicles);

        //select the edit text fields and attach to the appropriate variable
        make = findViewById(R.id.updateMake2);
        model = findViewById(R.id.updateModel2);
        year= findViewById(R.id.updateYear2);
        price = findViewById(R.id.updatePrice2);
        license_number = findViewById(R.id.updateLicense2);
        colour = findViewById(R.id.updateColour2);
        transmission = findViewById(R.id.updateTransmission2);
        mileage = findViewById(R.id.updateMileage2);
        fuel_type= findViewById((R.id.updateFuel2));
        engine_size = findViewById(R.id.updateEngine2);
        body_style = findViewById(R.id.updateBody2);
        doors = findViewById(R.id.updateDoors2);
        condition = findViewById(R.id.updateCondition2);
        notes= findViewById(R.id.updateNotes2);
        //select the update button and connect it to an appropriate variable
        btnUpdate = findViewById(R.id.btnUpdate);
        final HashMap<String,String> params = new HashMap<>();

        Bundle extras = getIntent().getExtras();

        final Vehicle theVehicle = (Vehicle) extras.get("updateVehicle");
        System.out.println("received from the intent: " + theVehicle.getMake() + " " + theVehicle.getModel());

        //Here we are grabbing the data in the edit text fields and attaching them to the variables
        make.setText(theVehicle.getMake());
        model.setText(theVehicle.getModel());
        year.setText(Integer.toString(theVehicle.getYear()));
        price.setText(Integer.toString(theVehicle.getPrice()));
        license_number.setText(theVehicle.getLicense_number());
        colour.setText(theVehicle.getColour());
        transmission.setText(theVehicle.getTransmission());
        mileage.setText(Integer.toString(theVehicle.getMileage()));
        fuel_type.setText(theVehicle.getFuel_type());
        engine_size.setText(Integer.toString(theVehicle.getEngine_size()));
        body_style.setText(theVehicle.getBody_style());
        doors.setText(Integer.toString(theVehicle.getNumber_doors()));
        condition.setText(theVehicle.getCondition());
        notes.setText(theVehicle.getNotes());

        //if the update button is clicked
         btnUpdate.setOnClickListener(new View.OnClickListener()
         {
            @Override
            public void onClick(View v)
            {
                Gson gson = new Gson();

                //Here we are grabbing the text written in the edit text fields and attaching them to new variables
                //we do this to pass the variables through to the JSON object
                //if the data is an integer, we will convert it to a string
                String Make = make.getText().toString();
                String Model = model.getText().toString();
                int Year = Integer.parseInt(year.getText().toString());
                int Price = Integer.parseInt(price.getText().toString());
                String License_Number = license_number.getText().toString();
                String Colour = colour.getText().toString();
                int Doors = Integer.parseInt(doors.getText().toString());
                String Transmission = transmission.getText().toString();
                int Mileage = Integer.parseInt(mileage.getText().toString());
                String FuelType = fuel_type.getText().toString();
                int Engine_Size = Integer.parseInt(engine_size.getText().toString());
                String Body_Style = body_style.getText().toString();
                String Condition = condition.getText().toString();
                String Notes = notes.getText().toString();

                //we will then create a new vehicle with the same vehicle id and pass in each variable to update the car as a new one
                //this acts a an override, replacing the old car details
                Vehicle vehicle = new Vehicle(theVehicle.getVehicle_id(),Make, Model, Year, Price, License_Number, Colour, Doors, Transmission, Mileage, FuelType, Engine_Size, Body_Style, Condition, Notes);

                //connecting to json in eclipse
                String vehicleJson = gson.toJson(vehicle);
                System.out.println(vehicleJson);
                params.put("json", vehicleJson);
                String URL = "http://10.0.2.2:8006/API";
                performPostCall(URL, params);
            }
         } );
    }

    /**
     * This method creates a URL, opens a connection, and sends a request to the API
     * It does this by sending the appropriate method request, in this case a PUT request
     * The method then checks if the connection was successful, if so a message is outputted
     * stating the success of the post call, else the appropriate error message is displayed
     * @param requestURL this is the url connection to connect to the eclipse servletAPI
     * @param postDataParams this stores the gson to json vehicle list format
     * @exception 'e' will print a stackTrace error
     * @return response
     */
    public String performPostCall(String requestURL, HashMap<String, String> postDataParams)
    {
        URL url;
        String response = "";

        try
        {
            url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(1500);
            conn.setConnectTimeout(1500);
            conn.setRequestMethod("PUT");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            writer.write(getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode = " + responseCode);

            if(responseCode == HttpsURLConnection.HTTP_OK)
            {
                Toast.makeText(this, "Vehicle Updated ", Toast.LENGTH_LONG).show();
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while((line = br.readLine())!=null)
                {
                    response+=line;
                }
            }
            else
            {
                Toast.makeText(this, "Vehicle Update Failed ", Toast.LENGTH_LONG).show(); response = "";
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        System.out.println("response = " + response);
        return response;
    }

    /**
     * This method will grab the data from the database and store it in a String variable
     * @param params this stores the gson to json vehicle list format
     * @return the result of the data retrieval converted to a string format
     * @throws UnsupportedEncodingException
     */
    private String getPostDataString(HashMap<String,String> params ) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry:params.entrySet())
        {
            if(first)
            {
                first = false;
            }
            else
            {
                result.append("&");
            }
            result.append(URLEncoder.encode(entry.getKey(),"UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(),"UTF-8"));
        }
        return result.toString();
    }

}