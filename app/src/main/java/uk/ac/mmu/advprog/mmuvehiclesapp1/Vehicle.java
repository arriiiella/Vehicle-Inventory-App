package uk.ac.mmu.advprog.mmuvehiclesapp1;

import java.io.Serializable;

/**
 * The purpose of this class is to retrieve and set the vehicle data
 * It does this by passing through the appropriate vehicle details to either
 * 'get' or 'set' them
 * @author Ariella Faber 17006468
 * @version 1.0
 */
public class Vehicle implements Serializable
{
    private int vehicle_id;
    private String make;
    private String model;
    private int year;
    private int price;
    private String license_number;
    private String colour;
    private int number_doors;
    private String transmission;
    private int mileage;
    private String fuel_type;
    private int engine_size;
    private String body_style;
    private String condition;
    private String notes;

    public Vehicle()
    {

    }

    /**
     * This method is the constructor which is used to insert or update a vehicle
     * Each vehicle detail is passed into the constructor and set to the global variables
     * of this class
     * @param Vehicle_Id
     * @param Make
     * @param Model
     * @param Year
     * @param Price
     * @param License_Number
     * @param Colour
     * @param Number_Doors
     * @param Transmission
     * @param Mileage
     * @param Fuel_Type
     * @param Engine_Size
     * @param Body_Style
     * @param Condition
     * @param Notes
     */
    public Vehicle(int Vehicle_Id, String Make, String Model, int Year, int Price, String License_Number,
                   String Colour, int Number_Doors, String Transmission, int Mileage, String Fuel_Type, int Engine_Size,
                   String Body_Style, String Condition, String Notes)
    {

        this.vehicle_id = Vehicle_Id;
        this.make = Make;
        this.model = Model;
        this.year = Year;
        this.price = Price;
        this.license_number = License_Number;
        this.colour = Colour;
        this.number_doors = Number_Doors;
        this.transmission = Transmission;
        this.mileage = Mileage;
        this.fuel_type = Fuel_Type;
        this.engine_size = Engine_Size;
        this.body_style = Body_Style;
        this.condition = Condition;
        this.notes = Notes;
    }

    /**
     * get the vehicle ID
     * @return vehicle id
     */
    public int getVehicle_id()
    {
        return vehicle_id;
    }

    /**
     * set the vehicle id
     * @param vehicle_id
     */
    public void setVehicle_id(int vehicle_id)
    {
        this.vehicle_id = vehicle_id;
    }

    /**
     * get the vehicle make
     * @return make
     */
    public String getMake()
    {
        return make;
    }

    /**
     * set the vehicle make
     * @param make
     */
    public void setMake(String make)
    {
        this.make = make;
    }

    /**
     * get the vehicle model
     * @return model
     */
    public String getModel()
    {
        return model;
    }

    /**
     * set the vehicle model
     * @param model
     */
    public void setModel(String model)
    {
        this.model = model;
    }

    /**
     * get the vehicle year
     * @return year
     */
    public int getYear()
    {
        return year;
    }

    /**
     * set the vehicle year
     * @param year
     */
    public void setYear(int year)
    {
        this.year = year;
    }

    /**
     * get the vehicle price
     * @return price
     */
    public int getPrice()
    {
        return price;
    }

    /**
     * set the vehicle price
     * @param price
     */
    public void setPrice(int price)
    {
        this.price = price;
    }

    /**
     * get the license number
     * @return license_number
     */
    public String getLicense_number()
    {
        return license_number;
    }

    /**
     * set the license number
     * @param license_number
     */
    public void setLicense_number(String license_number)
    {
        this.license_number = license_number;
    }

    /**
     * get the vehicle colour
     * @return colour
     */
    public String getColour()
    {
        return colour;
    }

    /**
     * set the vehicle colour
     * @param colour
     */
    public void setColour(String colour)
    {
        this.colour = colour;
    }

    /**
     * get the vehicle's number of doors
     * @return number_doors
     */
    public int getNumber_doors()
    {
        return number_doors;
    }

    /**
     * set the vehicle's number of doors
     * @param number_doors
     */
    public void setNumber_doors(int number_doors)
    {
        this.number_doors = number_doors;
    }

    /**
     * get the vehicle transmission
     * @return transmission
     */
    public String getTransmission()
    {
        return transmission;
    }

    /**
     * set the vehicle transmission
     * @param transmission
     */
    public void setTransmission(String transmission)
    {
        this.transmission = transmission;
    }

    /**
     * get the vehicle mileage
     * @return transmission
     */
    public int getMileage()
    {
        return mileage;
    }

    /**
     * set the vehicle mileage
     * @param mileage
     */
    public void setMileage(int mileage)
    {
        this.mileage = mileage;
    }

    /**
     * get the vehicle fuel type
     * @return fuel_type
     */
    public String getFuel_type()
    {
        return fuel_type;
    }

    /**
     * set the vehicle fuel type
     * @param fuel_type
     */
    public void setFuel_type(String fuel_type)
    {
        this.fuel_type = fuel_type;
    }

    /**
     * get the vehicle's engine size
     * @return engine_size
     */
    public int getEngine_size()
    {
        return engine_size;
    }

    /**
     * set the vehicle engine size
     * @param engine_size
     */
    public void setEngine_size(int engine_size)
    {
        this.engine_size = engine_size;
    }

    /**
     * get the vehicle's body style
     * @return body_style
     */
    public String getBody_style()
    {
        return body_style;
    }

    /**
     * set the vehicle body style
     * @param body_style
     */
    public void setBody_style(String body_style)
    {
        this.body_style = body_style;
    }

    /**
     * get the vehicle's condition
     * @return condition
     */
    public String getCondition()
    {
        return condition;
    }

    /**
     * set the vehicle condition
     * @param condition
     */
    public void setCondition(String condition)
    {
        this.condition = condition;
    }

    /**
     * get the vehicle notes
     * @return notes
     */
    public String getNotes()
    {
        return notes;
    }

    /**
     * set the vehicle notes
     * @param notes
     */
    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    /**
     * combines all the variables from this class into one string
     */
    @Override
    public String toString()
    {

        StringBuilder sb=new StringBuilder();
        sb.append("vehicle_id = ");
        sb.append(vehicle_id);
        sb.append("\n");
        sb.append("make = ");
        sb.append(make);
        sb.append("\n");
        sb.append("model = ");
        sb.append(model);
        sb.append("\n");
        sb.append("year = ");
        sb.append(year);
        sb.append("\n");
        sb.append("price = ");
        sb.append(price);
        sb.append("\n");
        sb.append("license_number = ");
        sb.append(license_number);
        sb.append("\n");
        sb.append("colour = ");
        sb.append(colour);
        sb.append("\n");
        sb.append("number_doors = ");
        sb.append(number_doors);
        sb.append("\n");
        sb.append("transmission = ");
        sb.append(transmission);
        sb.append("\n");
        sb.append("mileage = ");
        sb.append(mileage);
        sb.append("\n");
        sb.append("fuel_type = ");
        sb.append(fuel_type);
        sb.append("\n");
        sb.append("engine_size = ");
        sb.append(engine_size);
        sb.append("\n");
        sb.append("body_style = ");
        sb.append(body_style);
        sb.append("\n");
        sb.append("condition = ");
        sb.append(condition);
        sb.append("\n");
        sb.append("notes = ");
        sb.append(notes);
        sb.append("\n");

        return sb.toString();

    }
}
