package uk.ac.mmu.advprog.mmuvehiclesapp1;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
 * The purpose of this class is to insert vehicles into the database
 * This class does this by grabbing the information from each editText
 * and storing it in the appropriate detail variable, the passing these
 * through the constructor to create a new vehicle.
 * @author Ariella Faber 17006468
 * @version 1.0
 */
public class InsertVehicle extends AppCompatActivity
{

    EditText make, model, year, price, license_number, colour, doors, transmission, mileage, fuel_type, engine_size, body_style, condition, notes;
    FloatingActionButton insertVehicle;

    /**
     * This method displays the insert activity on the creation of the page
     * It sets the edit Text fields to the appropriate variable then grabs the
     * data in each and sets them to the variables.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_vehicle);

        //select the edit text fields and attach to the appropriate variable
        make = findViewById(R.id.addMake);
        model = findViewById(R.id.addModel);
        year = findViewById(R.id.addYear);
        price = findViewById(R.id.addPrice);
        license_number = findViewById(R.id.addLicense);
        colour = findViewById(R.id.addColour);
        doors = findViewById(R.id.addDoors);
        transmission = findViewById((R.id.addTransmission));
        mileage = findViewById(R.id.addMileage);
        fuel_type = findViewById(R.id.addFuel);
        engine_size = findViewById(R.id.addEngine);
        body_style = findViewById(R.id.addBody);
        condition = findViewById(R.id.addCondition);
        notes = findViewById(R.id.addNotes);
        //select the insert button and connect it to an appropriate variable
        insertVehicle = findViewById(R.id.btnAddVehicle);
        final HashMap<String,String> params = new HashMap<>();

        //onclick listener for the insert vehicle button
        insertVehicle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();

                //Here we are grabbing the text written in the edit text fields and attaching them to the variables
                //we do this to pass the variables through to the JSON object
                String Make = make.getText().toString();
                String Model = model.getText().toString();
                int Year = Integer.parseInt(year.getText().toString());
                int Price = Integer.parseInt(price.getText().toString());
                String License_Number = license_number.getText().toString();
                String Colour = colour.getText().toString();
                int number_doors= Integer.parseInt(doors.getText().toString());
                String Transmission = transmission.getText().toString();
                int Mileage = Integer.parseInt(mileage.getText().toString());
                String Fuel_Type = fuel_type.getText().toString();
                int Engine_Size = Integer.parseInt(engine_size.getText().toString());
                String Body_Style = body_style.getText().toString();
                String Condition = condition.getText().toString();
                String Notes = notes.getText().toString();

                //We are declaring a new car using the vehicle constructor from the vehicle class
                // The parameters are the variables created with the users input data attached to each one
                Vehicle vehicle = new Vehicle(0,Make, Model, Year, Price, License_Number, Colour, number_doors, Transmission, Mileage, Fuel_Type, Engine_Size, Body_Style, Condition, Notes);

                //connecting to json in eclipse
                String vehicleJson = gson.toJson(vehicle);
                System.out.println(vehicleJson);
                params.put("Json", vehicleJson);
                String URL = "http://10.0.2.2:8006/API";
                performPostCall(URL, params);
            }
        });

    }

    /**
     * This method creates a URL, and opens a connection, and sends a request to the API
     * It does this by sending the appropriate method request, in this case a POST request
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
            //here we are calling the post method as this is what we use to create a new vehicle
            conn.setRequestMethod("POST");
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
                Toast.makeText(this, "Vehicle Inserted ", Toast.LENGTH_LONG).show();
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while((line = br.readLine())!=null)
                {
                    response+=line;
                }
            }
            else
            {
                Toast.makeText(this, "Vehicle Inserted Failed ", Toast.LENGTH_LONG).show(); response = "";
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
