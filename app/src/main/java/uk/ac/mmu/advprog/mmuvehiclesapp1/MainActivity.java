package uk.ac.mmu.advprog.mmuvehiclesapp1;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * The purpose of this class is to display the main page of the application.
 * From this activity, depending on the user's choice, the other features and pages of
 * the app will be initiated from here.
 * The class does this by having listeners listening for button clicks.
 * When a certain button is clicked, the appropriate code to complete that task is started.
 * @author Ariella Faber 17006468
 * @version 1.0
 */

public class MainActivity extends AppCompatActivity
{

    String[] vehicleNames;
    ArrayList<Vehicle> allVehicles = new ArrayList();
    private static final String TAG = "vehicle_list";
    private SwipeMenuListView vehicleList;
    final HashMap<String, String> params = new HashMap<>();
    Boolean deleted = false;
    SwipeRefreshLayout refreshLayout;
    FloatingActionButton InsertVehicle;


    /**
     * This method displays the main activity on the creation of the page
     * It displays and creates the list of vehicles, along with the insert vehicle button and swipe menu
     * with edit and delete buttons.
     * These button listeners will include the code to redirect the user to the appropriate activities.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //references
        vehicleList = findViewById(R.id.listView);
        InsertVehicle=findViewById(R.id.btnAdd);
        refreshLayout = findViewById(R.id.refresh);

        //if the vehicle list is refreshed
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                httpCall();
                //this stops the refresh being in a continuous loop after the httpCall is complete
                refreshLayout.setRefreshing(false);
            }
        });

        httpCall();

        //if the insert vehicle button is clicked
        InsertVehicle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //create a new intent with the insert vehicle class
                Intent intent = new Intent(getApplicationContext(),InsertVehicle.class);
                //start the activity
                startActivity(intent);
            }
        });

        //if a vehicle in the list is clicked
        vehicleList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                //a pop up is displayed with the car make and model
                Toast.makeText(MainActivity.this, "you pressed " + allVehicles.get(i).getMake() + " " + allVehicles.get(i).getModel() , Toast.LENGTH_SHORT).show();


                // declare a new intent and give it the context and
                // specify which activity you want to open/start
                Intent intent = new Intent(getApplicationContext(), VehicleDetailsActivity.class);

                intent.putExtra("vehicle",allVehicles.get(i));

                // launch the activity
                startActivity(intent);
            }
        });

        //this is the code for the vehicle swipe menu
        //inspiration for this feature was provided by https://github.com/baoyongzhang/SwipeMenuListView
        SwipeMenuCreator creator = new SwipeMenuCreator()
        {
            @Override
            public void create(SwipeMenu menu)
            {
                //initiate the edit item menu item
                SwipeMenuItem editItem = new SwipeMenuItem(getApplicationContext());
                //item background colour
                editItem.setBackground(new ColorDrawable(Color.rgb(0x00, 0x66, 0xff)));
                //item width
                editItem.setWidth(170);
                //icon
                editItem.setIcon(R.drawable.editpen);
                //add to menu
                menu.addMenuItem(editItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                //item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                //item width
                deleteItem.setWidth(170);
                //icon
                deleteItem.setIcon(R.drawable.delete);
                //add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        vehicleList.setMenuCreator(creator);

        //if a vehicle is swiped and a button on the swipe menu is clicked
        vehicleList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int i)
            {
                switch (i)
                {
                    //if the edit button is clicked
                    case 0:
                        //message stating the vehicle make and model appears before being redirected to the update page
                        Toast.makeText(MainActivity.this, "you wish to edit " + allVehicles.get(position).getMake() + " " + allVehicles.get(position).getModel() , Toast.LENGTH_SHORT).show();
                        //create new intent with the UpdateVehicles class
                        Intent intent = new Intent(getApplicationContext(), UpdateVehicles.class);

                        intent.putExtra("updateVehicle",allVehicles.get(position));

                        // launch the activity
                        startActivity(intent);
                        break;
                    //if the delete button is clicked
                    case 1:
                        params.put("vehicle_id",""+allVehicles.get(position).getVehicle_id());
                        String url = "http://10.0.2.2:8006/API?vehicle_id=" + allVehicles.get(position).getVehicle_id();
                        performPostCall(url,params);
                        break;
                }
                //if false close the menu
                //if true : do not close the menu
                return false;
            }
        });
    }

    /**
     * This method connects Android Studio to the ServletAPI in Eclipse to allow the
     * application to connect to the vehicles database
     * @exception IOException to catch an input error with the url
     * @exception JSONException to catch a JSON format error with the vehicle information
     */
    private void httpCall()
    {
        //Making the http call
        HttpURLConnection urlConnection;
        InputStream in = null;
        try
        {
            // the url we will try to connect with
            URL url = new URL("http://10.0.2.2:8006/API");
            // open connection to the URL
            urlConnection = (HttpURLConnection) url.openConnection();
            // get server response in an input stream
            in = new BufferedInputStream(urlConnection.getInputStream());
        }
        //if error found, it will be caught here and the appropriate error message displayed
        catch (IOException e)
        {
            e.printStackTrace();
        }

        // change the input stream to a string
        String response = convertStreamToString(in);
        // print the response to log cat
        System.out.println("Server response = " + response);

        try
        {
            // declare a new json array and pass it the string response from the server
            // this will convert the string into a JSON array which we can then iterate
            // over using a loop
            JSONArray jsonArray = new JSONArray(response);
            // instantiate the vehicleNames array and set the size
            // to the amount of vehicle object returned by the server
            vehicleNames = new String[jsonArray.length()];

            // use a for loop to iterate over the JSON array
            for (int i=0; i < jsonArray.length(); i++)
            {
                // the following lines of code will get each vehicle detail from the
                // current JSON object and store it in a string variable
                int vehicle_id = Integer.parseInt(jsonArray.getJSONObject(i).get("vehicle_id").toString());
                String make = jsonArray.getJSONObject(i).get("make").toString();
                String model = jsonArray.getJSONObject(i).get("model").toString();
                int year = Integer.parseInt(jsonArray.getJSONObject(i).get("year").toString());
                int price = Integer.parseInt(jsonArray.getJSONObject(i).get("price").toString());
                String license = jsonArray.getJSONObject(i).get("license_number").toString();
                String colour = jsonArray.getJSONObject(i).get("colour").toString();
                int doors = Integer.parseInt(jsonArray.getJSONObject(i).get("number_doors").toString());
                String transmission = jsonArray.getJSONObject(i).get("transmission").toString();
                int mileage = Integer.parseInt(jsonArray.getJSONObject(i).get("mileage").toString());
                String fuel_type = jsonArray.getJSONObject(i).get("fuel_type").toString();
                int engine_size = Integer.parseInt(jsonArray.getJSONObject(i).get("engine_size").toString());
                String body_style = jsonArray.getJSONObject(i).get("body_style").toString();
                String condition = jsonArray.getJSONObject(i).get("condition").toString();
                String notes = jsonArray.getJSONObject(i).get("notes").toString();
                // print the name to log cat
                System.out.println("make = " + make + " " + "model = " + model);
                //add params below
                Vehicle v = new Vehicle(vehicle_id,make,model,year,price,license,colour,doors,transmission,mileage,fuel_type,engine_size,body_style,condition,notes);
                allVehicles.add(v);
                // add the name of the vehicle to the vehicleNames array
                vehicleNames [i] = make + " " + model +  "("+year+")  \n "+license;
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        // an array adapter to tell the context, the layout,and the data
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, vehicleNames);
        //set the adapter to the listview
        vehicleList.setAdapter(arrayAdapter);
    }

    /**
     * This method converts the string respones in the httpCall method to a string
     * @param is
     * @return s
     */
    public String convertStreamToString(InputStream is)
    {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    /**
     * This method will grab the data from the database and store it in a String variable
     * @param params this stores the gson to json vehicle list format
     * @return the result of the data retrieval converted to a string format
     * @throws UnsupportedEncodingException
     */
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }

    /**
     * This method creates a URL, and opens a connection, and sends a request to the API
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
            //making the http call
            url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            //set the read and connect times to 1500 milliseconds
            conn.setReadTimeout(1500);
            conn.setConnectTimeout(1500);
            //http request
            conn.setRequestMethod("DELETE");
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

            //if the request is successful therefore vehicle has been deleted successfully
            if(responseCode == HttpsURLConnection.HTTP_OK)
            {
                //notify user that vehicle was deleted successfully
                Toast.makeText(this, "Vehicle Deleted ", Toast.LENGTH_LONG).show();
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while((line = br.readLine())!=null)
                {
                    response+=line;
                }
            }
            //else the vehicle was not able to be deleted
            else
            {
                //notify user that vehicle was not deleted
                Toast.makeText(this, "Vehicle Delete Failed ", Toast.LENGTH_LONG).show(); response = "";
            }

        }
        catch(Exception e)
        {
            //print the appropriate error message if unsuccessful
            e.printStackTrace();
        }
        System.out.println("response = " + response);
        return response;
    }

}