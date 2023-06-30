package com.example.qizil.Data;



import com.example.qizil.Model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DataSource {
    private Connection connection;

    private PreparedStatement queryCustomer;
    private PreparedStatement insertCustomer;
    private  PreparedStatement increaseAnbar;
    private  PreparedStatement decreaseAnbar;
    private PreparedStatement insertSale;
    private PreparedStatement insertPayment;

    private PreparedStatement queryCustomerInfo;
    private PreparedStatement querySaleCustomerinfo;
    private PreparedStatement queryPaymentCustomerinfo;

    private PreparedStatement querySatisNr;

    private PreparedStatement deleteSale;
    private PreparedStatement deletePayment;
    private PreparedStatement updateSatisQram;
    private PreparedStatement editSale;


    private static final String DB_NAME="/Users/muhammadfeyzullayev/Documents/Portfolio/Qizil/qizil.db";
    private static final String DB_PATH="jdbc:sqlite:";
    private static final String CONNECTION_STRING=DB_PATH + DB_NAME;


    private static final String TABLE_LOGIN="login";
    private static final String COLUMN_USERNR="userNr";
    private static final String COLUMN_USER="user";
    private static final String COLUMN_PASSWORD="password";



    private static final String TABLE_ANBAR="Anbar";
    private static final String COLUMN_ANBAR_NR="nr";
    private static final String COLUMN_ANBAR_EYAR="eyar";
    private static final String COLUMN_ANBAR_QRAM="qram";



    private static final String TABLE_CUSTOMERS="Customers";
    private static final String COLUMN_CUSTOMERS_NR="nr";
    private static final String COLUMN_CUSTOMERS_NAME="name";
    private static final String COLUMN_CUSTOMERS_BORC_585="borc_585";
    private static final String COLUMN_CUSTOMERS_BORC_750="borc_750";

    private static final String TABLE_SALE ="Sale";
    private static final String COLUMN_SALE_NR="nr";
    private static final String COLUMN_SALE_DATE="date";
    private static final String COLUMN_SALE_CUSTOMER_NR="customer_nr";
    private static final String COLUMN_SALE_CUSTOMER_NAME="customer_name";

    private static final String COLUMN_SALE_EYAR="eyar";
    private static final String COLUMN_SALE_QRAM="qram";
    private static final String TABLE_PAYMENT ="Payment";
    private static final String COLUMN_PAYMENT_NR="nr";
    private static final String COLUMN_PAYMENT_DATE="date";
    private static final String COLUMN_PAYMENT_CUSTOMER_NR="customer_nr";
    private static final String COLUMN_PAYMENT_CUSTOMER_NAME="customer_name";
    private static final String COLUMN_PAYMENT_EYAR="eyar";
    private static final String COLUMN_PAYMENT_QRAM="qram";

    private static final String QUERY_CUSTOMER_NAME ="SELECT " + COLUMN_CUSTOMERS_NR + " FROM "
            + TABLE_CUSTOMERS + " WHERE "  + COLUMN_CUSTOMERS_NAME + " = ?";

    private static final String INSERT_CUSTOMER="INSERT INTO " + TABLE_CUSTOMERS + '(' + COLUMN_CUSTOMERS_NAME + ", " +
            COLUMN_CUSTOMERS_BORC_585 + ", " + COLUMN_CUSTOMERS_BORC_750 + ')' + " VALUES(?,?,?)";

    private static final String INCREASE_ANBAR="UPDATE " + TABLE_ANBAR + " SET " + COLUMN_ANBAR_QRAM + " = " +COLUMN_ANBAR_QRAM
            +"+ ?"+
            " WHERE " + COLUMN_ANBAR_EYAR + " = ?";
    private static final String DECREASE_ANBAR="UPDATE " + TABLE_ANBAR + " SET " + COLUMN_ANBAR_QRAM + " = " +COLUMN_ANBAR_QRAM
            +"- ?"+
            " WHERE " + COLUMN_ANBAR_EYAR + " = ?";

    private static final String INSERT_SALE="INSERT INTO " + TABLE_SALE + '(' + COLUMN_SALE_DATE + ", " + COLUMN_SALE_CUSTOMER_NR
            +", " + COLUMN_SALE_CUSTOMER_NAME + ", " + COLUMN_SALE_EYAR + ", " + COLUMN_SALE_QRAM + ')' +
            "  VALUES(?,?,?,?,?)";
    private static final String INSERT_PAYMENT="INSERT INTO " + TABLE_PAYMENT + '(' + COLUMN_PAYMENT_DATE + ", " + COLUMN_PAYMENT_CUSTOMER_NR
            +", " + COLUMN_PAYMENT_CUSTOMER_NAME + ", " + COLUMN_PAYMENT_EYAR + ", " + COLUMN_PAYMENT_QRAM + ')' +
            "  VALUES(?,?,?,?,?)";

    private static final String QUERY_ANBAR="SELECT * FROM " + TABLE_ANBAR;

    private static final String QUERY_CUSTOMER="SELECT * FROM " + TABLE_CUSTOMERS;

    private static final String QUERY_CUSTOMER_INFO="SELECT " + COLUMN_CUSTOMERS_NAME +", " + COLUMN_CUSTOMERS_BORC_585+", "+
            COLUMN_CUSTOMERS_BORC_750 + " FROM " + TABLE_CUSTOMERS + " WHERE " + COLUMN_CUSTOMERS_NR + " = ?" ;

    private static final String QUERY_SALE_FORCUSTOMER="SELECT " + COLUMN_SALE_DATE +", " + COLUMN_SALE_CUSTOMER_NAME + ", " + COLUMN_SALE_EYAR
            +", " + COLUMN_SALE_QRAM + " FROM " + TABLE_SALE + " WHERE " + COLUMN_SALE_CUSTOMER_NR + " =?";
    private static final String QUERY_PAYMENT_FORCUSTOMER="SELECT " + COLUMN_PAYMENT_DATE +", " + COLUMN_PAYMENT_CUSTOMER_NAME + ", " + COLUMN_PAYMENT_EYAR
            +", " + COLUMN_PAYMENT_QRAM + " FROM " + TABLE_PAYMENT + " WHERE " + COLUMN_PAYMENT_CUSTOMER_NR + " =?";

    private static final String QUERY_ALLSALES="SELECT * FROM " + TABLE_SALE;
    private static final String QUERY_ALLPAYMENTS="SELECT * FROM " + TABLE_PAYMENT;

    private static final String QUERY_ONESALE_CUSTOMER = "SELECT " + COLUMN_SALE_NR + " FROM " + TABLE_SALE + " WHERE " + COLUMN_SALE_CUSTOMER_NR + " = ? AND " + COLUMN_SALE_EYAR + " = ? ORDER BY " + COLUMN_SALE_NR + " DESC LIMIT 1";

    private static final String UPDATE_ONESALE_CUSTOMER="UPDATE " + TABLE_SALE + " SET " + COLUMN_SALE_QRAM + " = " +COLUMN_SALE_QRAM +" - ?" + " WHERE " + COLUMN_SALE_NR + " =?";
    private static final String DELETE_SALE = "DELETE FROM " + TABLE_SALE + " WHERE " + COLUMN_SALE_NR + " = ?";
    private static final String DELETE_PAYMENT = "DELETE FROM " + TABLE_PAYMENT + " WHERE " + COLUMN_PAYMENT_NR + " = ?";


    private static final String EDIT_SALE="UPDATE " + TABLE_SALE + " SET " +  COLUMN_SALE_EYAR + " = ?" + ", " + COLUMN_SALE_QRAM + " = ?" + " WHERE " + COLUMN_SALE_NR + " = ?";

    private static final String QUERY_LOGIN= "SELECT * FROM " + TABLE_LOGIN;


    public void open(){
        try {
            connection= DriverManager.getConnection(CONNECTION_STRING);
            queryCustomer=connection.prepareStatement(QUERY_CUSTOMER_NAME);
            insertCustomer=connection.prepareStatement(INSERT_CUSTOMER);
            increaseAnbar=connection.prepareStatement(INCREASE_ANBAR);
            decreaseAnbar=connection.prepareStatement(DECREASE_ANBAR);
            insertSale=connection.prepareStatement(INSERT_SALE);
            insertPayment=connection.prepareStatement(INSERT_PAYMENT);
            queryCustomerInfo = connection.prepareStatement(QUERY_CUSTOMER_INFO);
            querySaleCustomerinfo=connection.prepareStatement(QUERY_SALE_FORCUSTOMER);
            queryPaymentCustomerinfo=connection.prepareStatement(QUERY_PAYMENT_FORCUSTOMER);
            querySatisNr = connection.prepareStatement(QUERY_ONESALE_CUSTOMER);
            updateSatisQram = connection.prepareStatement(UPDATE_ONESALE_CUSTOMER);
            deleteSale = connection.prepareStatement(DELETE_SALE);
            editSale = connection.prepareStatement(EDIT_SALE);
            deletePayment= connection.prepareStatement(DELETE_PAYMENT);

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }
    public void close(){
        try {
            if (connection!=null){
                connection.close();
            }
            if (queryCustomer!=null){
                queryCustomer.close();
            }
            if (insertCustomer!=null){
                insertCustomer.close();
            }
            if (increaseAnbar!=null){
                increaseAnbar.close();
            }
            if (decreaseAnbar!=null){
                decreaseAnbar.close();
            }
            if (insertSale!=null){
                insertSale.close();
            }
            if (insertPayment!=null){
                insertPayment.close();
            }
            if (queryCustomerInfo!=null){
                queryCustomerInfo.close();
            }if (querySaleCustomerinfo!=null){
                querySaleCustomerinfo.close();
            }if (queryPaymentCustomerinfo!=null){
                queryPaymentCustomerinfo.close();
            }
            if (querySatisNr!=null){
                querySatisNr.close();
            }
            if (updateSatisQram!=null){
                updateSatisQram.close();
            }
            if (deleteSale != null) {
                deleteSale.close();
            }if (editSale != null){
                editSale.close();
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }


    }

    public void deleteSale(int saleNr) {
        try {
            deleteSale.setInt(1, saleNr);
            int nrAffectedRows = deleteSale.executeUpdate();
            if (nrAffectedRows == 1) {
                System.out.println("Sale deleted successfully.");
            } else {
                System.out.println("Failed to delete sale.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void deletePayment(int paymentNr) {
        try {
            deletePayment.setInt(1,paymentNr);
            int nrAffectedRows = deleteSale.executeUpdate();
            if (nrAffectedRows == 1) {
                System.out.println("Sale deleted successfully.");
            } else {
                System.out.println("Failed to delete sale.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



    public void insertCustomer(String name,double borc_585,double borc_750){
        if (findCustomer(name)==0){
            try {
                connection.setAutoCommit(false);
                insertCustomer.setString(1,name);
                insertCustomer.setDouble(2,borc_585);
                insertCustomer.setDouble(3,borc_750);
                int nrAffectedRows=insertCustomer.executeUpdate();
                if (nrAffectedRows==1){
                    connection.commit();
                    System.out.println("commited");
                }else {
                    throw  new SQLException("Couldn't insert customer " + name);
                }
            }catch (SQLException e){
                System.out.println(e.getMessage());
                try {
                    connection.rollback();
                    System.out.println("connection rolled back");
                }catch (SQLException f){
                    System.out.println("failed to roll back " + f.getMessage() );
                }
            }
            finally {
                try {
                    System.out.println("saving changes and setting to true ");
                    connection.setAutoCommit(true);
                }catch (SQLException h){
                    System.out.println("failed to setting auto commit to true " + h.getMessage());
                }
            }
        }else{
            System.out.println("customer exists");
        }


    }

    public int findCustomer(String name){
        try {
            queryCustomer.setString(1,name);
            ResultSet resultSet = queryCustomer.executeQuery();
            if (resultSet.next()){
                return  resultSet.getInt(1);
            }else{
                return 0;
            }
        }catch (SQLException e){

            System.out.println("Someting went wrong" + e.getMessage());
            return 0;
        }
    }

    public void increaseCustomerDebt(String name,String eyar,double gram){
        try {
            int id = findCustomer(name);
            if (id!=0){
                String borc_eyar;
                if (eyar.equals("585")){
                    borc_eyar=COLUMN_CUSTOMERS_BORC_585;
                }else {
                    borc_eyar = COLUMN_CUSTOMERS_BORC_750;
                }
                String query = "UPDATE " + TABLE_CUSTOMERS + " SET " + borc_eyar + " = " + borc_eyar +
                        "+ ? " + " WHERE " + COLUMN_CUSTOMERS_NR + " = ?";

                PreparedStatement statement = connection.prepareStatement(query);
                statement.setDouble(1,gram);
                statement.setInt(2,id);
                statement.executeUpdate();
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void decreaseCustomerDebt(String name,String eyar,double gram){
        try {
            int id = findCustomer(name);
            if (id!=0){
                String borc_eyar;
                if (eyar.equals("585")){
                    borc_eyar=COLUMN_CUSTOMERS_BORC_585;
                }else {
                    borc_eyar = COLUMN_CUSTOMERS_BORC_750;
                }
                String query = "UPDATE " + TABLE_CUSTOMERS + " SET " + borc_eyar + " = " + borc_eyar +
                        "- ? " + " WHERE " + COLUMN_CUSTOMERS_NR + " = ?";

                PreparedStatement statement = connection.prepareStatement(query);
                statement.setDouble(1,gram);
                statement.setInt(2,id);
                statement.executeUpdate();
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }



    public void increaseAnbar(String eyar,double gram){
        try {
            connection.setAutoCommit(false);
            increaseAnbar.setDouble(1,gram);
            increaseAnbar.setString(2,eyar);
            int nrAffectedrows=increaseAnbar.executeUpdate();
            if (nrAffectedrows==1){
                connection.commit();
                System.out.println("commmited");
            }else{
                throw new SQLException("couldn't increase Anbar");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
            try {
                connection.rollback();
                System.out.println("connection rolled back");
            }catch (SQLException f){
                System.out.println("failed to roll back " + f.getMessage() );
            }
        }
        finally {
            try {
                System.out.println("saving changes and setting to true ");
                connection.setAutoCommit(true);
            }catch (SQLException h){
                System.out.println("failed to setting auto commit to true " + h.getMessage());
            }
        }
    }
    public void decreaseAnbar(String eyar,double gram) {
        try {
            connection.setAutoCommit(false);
            decreaseAnbar.setDouble(1,gram);
            decreaseAnbar.setString(2,eyar);
            int nrAffectedrows=decreaseAnbar.executeUpdate();
            if (nrAffectedrows==1){
                connection.commit();
                System.out.println("commmited");
            }else{
                throw new SQLException("couldn't increase Anbar");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
            try {
                connection.rollback();
                System.out.println("connection rolled back");
            }catch (SQLException f){
                System.out.println("failed to roll back " + f.getMessage() );
            }
        }
        finally {
            try {
                System.out.println("saving changes and setting to true ");
                connection.setAutoCommit(true);
            }catch (SQLException h){
                System.out.println("failed to setting auto commit to true " + h.getMessage());
            }
        }
    }


    public void editSatis(int saleNr,String eyar,double gram) {
        try {
            connection.setAutoCommit(false);
            editSale.setString(1, eyar);
            editSale.setDouble(2, gram);
            editSale.setInt(3, saleNr);
            int nrAffectedRows = editSale.executeUpdate();
            if (nrAffectedRows == 1) {
                connection.commit();
                System.out.println("commited");
            } else {
                throw new SQLException("Error inserting Sale");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            try {
                connection.rollback();
                System.out.println("connection rolled back");
            } catch (SQLException f) {
                System.out.println("failed to roll back " + f.getMessage());
            }
        } finally {
            try {
                System.out.println("saving changes and setting to true ");
                connection.setAutoCommit(true);
            } catch (SQLException h) {
                System.out.println("failed to setting auto commit to true " + h.getMessage());

            }
        }
    }


    public void insertSatis(String name,String eyar,double gram){
        try {
            int id= findCustomer(name);
            if (id!=0){
                connection.setAutoCommit(false);
                LocalDate date= LocalDate.now();
                insertSale.setString(1,date.toString());
                insertSale.setInt(2,id);
                insertSale.setString(3,name);
                insertSale.setString(4,eyar);
                insertSale.setDouble(5,gram);
                int nrAffectedRows=insertSale.executeUpdate();
                if (nrAffectedRows==1){
                    increaseCustomerDebt(name,eyar,gram);
                    decreaseAnbar(eyar,gram);
                    connection.commit();
                    System.out.println("commited");
                }else {
                    throw new SQLException("Error inserting Sale");
                }
            }else{
                System.out.println("customer not found");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
            try {
                connection.rollback();
                System.out.println("connection rolled back");
            } catch (SQLException f) {
                System.out.println("failed to roll back " + f.getMessage());
            }
        }finally {
            try {
                System.out.println("saving changes and setting to true ");
                connection.setAutoCommit(true);
            } catch (SQLException h) {
                System.out.println("failed to setting auto commit to true " + h.getMessage());
            }
        }
    }

    public void insertPayment(String name,String eyar,double gram){
        try {
            int id= findCustomer(name);
            if (id!=0){
                connection.setAutoCommit(false);
                LocalDate date= LocalDate.now();
                insertPayment.setString(1,date.toString());
                insertPayment.setInt(2,id);
                insertPayment.setString(3,name);
                insertPayment.setString(4,eyar);
                insertPayment.setDouble(5,gram);
                int nrAffectedRows=insertPayment.executeUpdate();
                if (nrAffectedRows==1){
                    decreaseCustomerDebt(name,eyar,gram);
                    connection.commit();
                    System.out.println("commited");
                }else {
                    throw new SQLException("Error inserting Sale");
                }
            }else{
                System.out.println("customer not found");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
            try {
                connection.rollback();
                System.out.println("connection rolled back");
            } catch (SQLException f) {
                System.out.println("failed to roll back " + f.getMessage());
            }
        }finally {
            try {
                System.out.println("saving changes and setting to true ");
                connection.setAutoCommit(true);
            } catch (SQLException h) {
                System.out.println("failed to setting auto commit to true " + h.getMessage());
            }
        }
    }

    public List<Anbar> queryAnbar(){
        try (Statement statement = connection.createStatement();
        ResultSet resultSet= statement.executeQuery(QUERY_ANBAR)){
            List<Anbar> anbarList = new ArrayList<>();
            while (resultSet.next()){
                Anbar anbar = new Anbar();
                anbar.setEyar(resultSet.getString(1));
                anbar.setGram(resultSet.getDouble(2));
                anbarList.add(anbar);
            }
            return anbarList;
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Customer> queryAllCustomers(){
        try(Statement statement= connection.createStatement();
        ResultSet resultSet = statement.executeQuery(QUERY_CUSTOMER)){
            List<Customer> customers= new ArrayList<>();
            while (resultSet.next()){
                Customer customer = new Customer();
                customer.setName(resultSet.getString(2));
                customer.setBorc_585(resultSet.getDouble(3));
                customer.setBorc_750(resultSet.getDouble(4));
                customers.add(customer);
            }
            return customers;

        }catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }








    public List<Satis> getSatisCustomer(String customerName){
        try {
            int id = findCustomer(customerName);
            if (id!=0){
                querySaleCustomerinfo.setInt(1,id);
                List<Satis>satisList = new ArrayList<>();
                ResultSet resultSet = querySaleCustomerinfo.executeQuery();
                while (resultSet.next()){
                    Satis satis =  new Satis();
                    satis.setDate(resultSet.getString(1));
                    satis.setCustomerName(resultSet.getString(2));
                    satis.setEyar(resultSet.getString(3));
                    satis.setGram(resultSet.getDouble(4));
                    satisList.add(satis);
                }
                return satisList;
            }
        }catch (SQLException e){
            e.getMessage();
            return null;
        }
        return null;
    }


    public List<Payment> getPaymentCustomer(String name){
        try {
            int id = findCustomer(name);
            if (id!=0){
                queryPaymentCustomerinfo.setInt(1,id);
                List<Payment>paymentList = new ArrayList<>();
                ResultSet resultSet = queryPaymentCustomerinfo.executeQuery();
                while (resultSet.next()){
                    Payment payment=  new Payment();
                    payment.setDate(resultSet.getString(1));
                    payment.setCustomerName(resultSet.getString(2));
                    payment.setEyar(resultSet.getString(3));
                    payment.setGram(resultSet.getDouble(4));
                    paymentList.add(payment);
                }
                return paymentList;
            }
        }catch (SQLException e){
            e.getMessage();
            return null;
        }
        return null;
    }


    public List<Satis> queryAllSales(){
        try (Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(QUERY_ALLSALES)){
            List<Satis> satisList = new ArrayList<>();
            while (resultSet.next()) {
                Satis satis = new Satis();
                satis.setSystemNr(resultSet.getInt(1));
                satis.setDate(resultSet.getString(2));
                satis.setCustomerNr(resultSet.getInt(3));
                satis.setCustomerName(resultSet.getString(4));
                satis.setEyar(resultSet.getString(5));
                satis.setGram(resultSet.getDouble(6));
                satisList.add(satis);
            }
            return satisList;
        }catch (SQLException e){
            e.getMessage();
        }return null;
    }
    public List<Payment> queryAllPayments(){
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(QUERY_ALLPAYMENTS)){
            List<Payment> paymentList = new ArrayList<>();
            while (resultSet.next()) {
                Payment payment = new Payment();
                payment.setSystemNr(resultSet.getInt(1));
                payment.setDate(resultSet.getString(2));
                payment.setCustomerNr(resultSet.getInt(3));
                payment.setCustomerName(resultSet.getString(4));
                payment.setEyar(resultSet.getString(5));
                payment.setGram(resultSet.getDouble(6));
                paymentList.add(payment);
            }
            return paymentList;
        }catch (SQLException e){
            e.getMessage();
        }return null;
    }

    public List<Admin> queryUsers(){
        try (Statement statement = connection.createStatement();
        ResultSet resultSet= statement.executeQuery(QUERY_LOGIN)){
            List<Admin> admins = new ArrayList<>();
            while (resultSet.next()){
                Admin admin = new Admin();
                admin.setUserNr(resultSet.getInt(1));
                admin.setUserName(resultSet.getString(2));
                admin.setPassword(resultSet.getString(3));
                admins.add(admin);
            }
            return admins;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




}




