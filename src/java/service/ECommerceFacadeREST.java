package service;

import Entity.Itementity;
import Entity.Lineitementity;
import Entity.Salesrecordentity;
import static java.lang.System.out;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
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
            @QueryParam("storeID")int StoreId,
            @QueryParam("SKU") String SKU,
            @QueryParam("quantity")     int quantity,
            @QueryParam("countryID")    int countryId){
        String message = "";
        try{
            //initialize LineItemEntity object first
            Itementity item = new Itementity();
            
            item.setSku(SKU);
            Lineitementity  lineitem = new Lineitementity();
            
            item.reduceFromDatabase(quantity, SKU, StoreId);
            message = Integer.toString(item.getidfromsku(SKU));

//                
        }catch(Exception ex){
                return Response.status(Response.Status.OK)
                    .entity(message + "came here" + ex).build(); 
        }
          return Response.status(Response.Status.OK)
                    .entity(message).build(); 
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
    
    @PUT
    @Path("createECommerceLineItemRecord")
    @Produces(MediaType.APPLICATION_JSON) 
    public Response createECommerceLineItemRecord(@QueryParam("ItemIDs") int Id, @QueryParam("Quantity") int Quantity, @QueryParam("salesRecordID") int id) throws ClassNotFoundException, SQLException{ 
        //String success = ck.createECommerceLineItemRecord(ids, Quantity, id);
        try {
        Itementity item = new Itementity();
        String success =item.addToDatabase(Id, Quantity, id);
        
        return Response.status(Response.Status.OK).entity("" + success).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.OK).entity("" + e.getMessage()).build();
        }
        
    }
    
}
