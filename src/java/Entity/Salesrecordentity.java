/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import DatabaseConnection.DBConn;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author ChenYi
 */
public class Salesrecordentity {
    
    private long id;
    private double amountdue;
    private double amountpaid;
    private double amountpaidusingpoints;
    private Date createddate;
    private String currency;
    private int loyaltypointsdeducted;
    private String posname;
    private String receiptno;
    private String servedbystaff;
    private long member_id;
    private long store_id;
    
    public Salesrecordentity(){}
    
    public Salesrecordentity(long id, double amountdue, double amountpaid, double amountpaidusingpoints, Date createddate, String currency, int loyaltypointsdeducted, String posname, String receiptno, String servedbystaff, long member_id, long store_id) {
        this.id = id;
        this.amountdue = amountdue;
        this.amountpaid = amountpaid;
        this.amountpaidusingpoints = amountpaidusingpoints;
        this.createddate = createddate;
        this.currency = currency;
        this.loyaltypointsdeducted = loyaltypointsdeducted;
        this.posname = posname;
        this.receiptno = receiptno;
        this.servedbystaff = servedbystaff;
        this.member_id = member_id;
        this.store_id = store_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAmountdue() {
        return amountdue;
    }

    public void setAmountdue(double amountdue) {
        this.amountdue = amountdue;
    }

    public double getAmountpaid() {
        return amountpaid;
    }

    public void setAmountpaid(double amountpaid) {
        this.amountpaid = amountpaid;
    }

    public double getAmountpaidusingpoints() {
        return amountpaidusingpoints;
    }

    public void setAmountpaidusingpoints(double amountpaidusingpoints) {
        this.amountpaidusingpoints = amountpaidusingpoints;
    }

    public Date getCreateddate() {
        return createddate;
    }

    public void setCreateddate(Date createddate) {
        this.createddate = createddate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getLoyaltypointsdeducted() {
        return loyaltypointsdeducted;
    }

    public void setLoyaltypointsdeducted(int loyaltypointsdeducted) {
        this.loyaltypointsdeducted = loyaltypointsdeducted;
    }

    public String getPosname() {
        return posname;
    }

    public void setPosname(String posname) {
        this.posname = posname;
    }

    public String getReceiptno() {
        return receiptno;
    }

    public void setReceiptno(String receiptno) {
        this.receiptno = receiptno;
    }

    public String getServedbystaff() {
        return servedbystaff;
    }

    public void setServedbystaff(String servedbystaff) {
        this.servedbystaff = servedbystaff;
    }

    public long getMember_id() {
        return member_id;
    }

    public void setMember_id(long member_id) {
        this.member_id = member_id;
    }

    public long getStore_id() {
        return store_id;
    }

    public void setStore_id(long store_id) {
        this.store_id = store_id;
    }
    
    public void createTransactionRecord(String memberId,double amountpaid) throws ClassNotFoundException, SQLException{
        Connection conn = DBConn.getConnection();
        
        /*increment max value for receiptNp
        https://stackoverflow.com/questions/5360117/insert-and-set-value-with-max1-problems
        */
        String query = "SELECT CAST(max(CAST(receiptno AS signed) + 1) as char(100)) FROM salesrecordentity";
        PreparedStatement ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        rs.next();
        String receiptno = rs.getString(1);
        
        
        String stmt ="INSERT INTO salesrecordentity (AMOUNTDUE,AMOUNTPAID,AMOUNTPAIDUSINGPOINTS,CREATEDDATE,CURRENCY,LOYALTYPOINTSDEDUCTED,POSNAME,RECEIPTNO,SERVEDBYSTAFF,MEMBER_ID,STORE_ID)" +
        "VALUES(?,?,0,?,'SGD',0,'Counter 1',?,'Cashier 1',?,?)";
        ps = conn.prepareStatement(stmt,Statement.RETURN_GENERATED_KEYS);
        ps.setDouble(1, this.amountdue);                        //amountdue
        ps.setDouble(2, this.amountpaid);                       //amountpaid
        ps.setTimestamp(3, new java.sql.Timestamp(System.currentTimeMillis()));//CREATEDATE 
        ps.setString(4, receiptno);
        ps.setLong(5, Long.parseLong(memberId));                                //memberId
        ps.setInt(6, 59);                                       //STORE_ID
 
        ps.executeUpdate();
        rs = ps.getGeneratedKeys();
        rs.next();
        this.id = rs.getLong(1); //get id gg
        
        rs.close();
        ps.close();
    }
    
}
