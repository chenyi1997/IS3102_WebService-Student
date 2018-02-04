package service;

import Entity.Itementity;
import Entity.Lineitementity;
import Entity.Salesrecordentity;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("commerce")
public class ECommerceFacadeREST {

    @Context
    private UriInfo context;

    public ECommerceFacadeREST() {
    }

    @GET
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
    
    
    @PUT
    @Path("updateQuantityItemRecord")
    @Produces("application/json")
    public Response updateQuantityFromItemRecord(
            @QueryParam("salesRecordID")long salesRecordId,
            @QueryParam("itemEntityID") long itemEntityId,
            @QueryParam("quantity")     int quantity,
            @QueryParam("countryID")    long countryId){
        
        try{
            //initialize LineItemEntity object first
            Itementity item = new Itementity(itemEntityId);
            Lineitementity  lineitem = new Lineitementity();
            
            if(item.reduceFromDatabase(quantity)){
            
            }else{
                return Response.status(Response.Status.BAD_REQUEST)
                     .entity("Unable to deduct from database").build();
            }
           
                //retrieve the PK from the database after adding it 
                lineitem.setId(item.addToDatabase(quantity));
                
                //Bind it with the salesrecordentity
                lineitem.addToSalesRecord(salesRecordId);
                
                //if got id 
                if(lineitem.getId()>0){
                    return Response.status(Response.Status.OK)
                            .entity("Success! ").build();
                }else{
                    return Response.status(Response.Status.CONFLICT)
                            .entity(String.valueOf(lineitem.getId())).build();
                }
        }catch(ClassNotFoundException | SQLException ex){
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ex.toString()).build(); 
        }
    }
    
       
    /*
    * after submit payment details, create a transaction record
    * update the salesrecordentity in DB
    */
    
    
    @PUT
    @Path("createECommerceTransactionRecord")
    @Produces("application/json")
    public Response createECommerceTransactionRecord(
           String memberId,
            @QueryParam("finalPrice")double finalPrice,
            @QueryParam("countryId") long countryId){
        try{
            Salesrecordentity salesRecord = new Salesrecordentity();
            
            //return amountdue,amountpaid
            salesRecord.setAmountdue(finalPrice);
            salesRecord.setAmountpaid(finalPrice);
           
            //create Transaction record using memberID
            salesRecord.createTransactionRecord(memberId,finalPrice);
            
            return Response.ok(
                    String.valueOf(salesRecord.getId())).build();
        
        }catch(Exception ex){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ex.getMessage().toString()).build();
        }
    }
    
}
