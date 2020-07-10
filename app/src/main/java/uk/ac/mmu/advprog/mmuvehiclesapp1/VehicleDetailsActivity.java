package uk.ac.mmu.advprog.mmuvehiclesapp1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * The purpose of this class is to display the vehicle's details when a vehicle on the main page is clicked.
 * This class does this by grabbing the information from the vehicle that has been selected and populating
 * separate labels with each detail.
 * @author Ariella Faber 17006468
 * @version 1.0
 */
public class VehicleDetailsActivity extends AppCompatActivity
{
    /**
     * This method displays the details activity on the creation of the page
     * It populates labels with the vehicle selected's information
     * @param savedInstanceState
     * @exception MalformedURLException to display the appropriate error based on the url status
     * @exception IOException to display the appropriate error if the url input is incorrect
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details);

        Bundle extras = getIntent().getExtras();

        Vehicle theVehicle = (Vehicle) extras.get("vehicle");
        System.out.println("received from the intent: " + theVehicle.getMake() + " " + theVehicle.getModel());

        //the textviews and image are set to an appropriate variable
        ImageView carPicture = findViewById(R.id.carImage);
        TextView TheMake = findViewById(R.id.viewMake);
        TextView TheModel = findViewById(R.id.viewModel);
        TextView TheYear = findViewById(R.id.viewYear);
        TextView TheLicense = findViewById(R.id.viewLicense);
        TextView ThePrice = findViewById(R.id.viewPrice);
        TextView TheColour = findViewById(R.id.viewColour);
        TextView TheTransmission = findViewById(R.id.viewTransmission);
        TextView TheMileage = findViewById(R.id.viewMileage);
        TextView TheFuelType = findViewById((R.id.viewFuelType));
        TextView TheEngineSize = findViewById(R.id.viewEngineSize);
        TextView TheDoors = findViewById(R.id.viewDoors);
        TextView TheCondition = findViewById(R.id.viewCondition);
        TextView TheNotes = findViewById(R.id.viewNotes);

        //these variables' values are then declared by retrieving the appropriate car's details
        //the var details are grabbed using the get statements from the vehicle class
        TheMake.setText(theVehicle.getMake());
        TheModel.setText(theVehicle.getModel());
        TheYear.setText(Integer.toString(theVehicle.getYear()));
        TheLicense.setText(theVehicle.getLicense_number());
        ThePrice.setText(Integer.toString(theVehicle.getPrice()));
        TheColour.setText(theVehicle.getColour());
        TheTransmission.setText(theVehicle.getTransmission());
        TheMileage.setText(Integer.toString(theVehicle.getMileage()));
        TheFuelType.setText(theVehicle.getFuel_type());
        TheEngineSize.setText(Integer.toString(theVehicle.getEngine_size()));
        TheDoors.setText(Integer.toString(theVehicle.getNumber_doors()));
        TheCondition.setText(theVehicle.getCondition());
        TheNotes.setText(theVehicle.getNotes());

        //this code is used to retrieve the appropriate car logo for the image to be replaced with
        String ImageName = theVehicle.getMake();
        String str = ImageName;
        String NewS = str.replaceAll("","-");
        ImageName = NewS;
        String imageURL = "http://www.carlogos.org/logo/" +ImageName +"-logo.png";

        try
        {
            URL url = new URL(imageURL);
            System.out.println(url);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            carPicture.setImageBitmap(bmp);
        }

        catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}