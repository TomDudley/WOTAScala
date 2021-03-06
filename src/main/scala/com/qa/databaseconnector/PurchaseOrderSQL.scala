package com.qa.databaseconnector

import com.qa.entities.PurchaseOrder
import java.sql.SQLException
import java.sql.Connection
import scalafx.collections.ObservableBuffer

/**
 * @author tdudley
 */
class PurchaseOrderSQL {
  
  val dbConnection = new DBConnector()
  
   /**
   * @return : ObservableBuffer of Purchase Orders
   * 
   * Finds all purchase orders within the SQL database 
   * and returns them as an ObservableBuffer
   */
  
  def findAllPurchaseOrders() : ObservableBuffer[PurchaseOrder] = 
  {
    val purchaseOrderArray : ObservableBuffer[PurchaseOrder] = ObservableBuffer[PurchaseOrder]()
    
    val resultSet = dbConnection findAllSQL("SELECT * FROM purchaseorder")
    
    def getRSData()
    {
     if (resultSet next())
     {
       purchaseOrderArray += new PurchaseOrder(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4))
       getRSData
     }
    }
    
    getRSData

    dbConnection closeConnection
    
    purchaseOrderArray
  }
  
  /**
   * @param : purchaseOrderID used to find the  
   * @return : PurchaseOrder 
   * 
   * returns the employee with the given username
   */
  
  def findByPurchaseOrderID(purchaseOrderID : Int) : PurchaseOrder = 
  {
    var purchaseOrder : PurchaseOrder = new PurchaseOrder(999999999, "", "", 0)
    
    val resultSet = dbConnection runSQLQuery("SELECT * FROM purchaseorder WHERE purchase_order_id = ", purchaseOrderID)
    
    def getRSData()
    {
     if (resultSet next())
     {
       purchaseOrder = new PurchaseOrder(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4))
       getRSData
     }
    }
    
    getRSData

    dbConnection closeConnection
    
    purchaseOrder
  }
  
  /*
   * Returns an ObservableBuffer of PurchaseOrder with the given employeeID
   * @Param employeeID, used to find selected orders with this employeeID
   */
  
  def findByEmployeeID(employeeID : Int) : ObservableBuffer[PurchaseOrder] = 
  {
    val purchaseOrderArray : ObservableBuffer[PurchaseOrder] = ObservableBuffer[PurchaseOrder]()
    
    val resultSet = dbConnection runSQLQuery("SELECT * FROM purchaseorder WHERE employee_id = ", employeeID)
    
    def getRSData()
    {
     if (resultSet next())
     {
       purchaseOrderArray += new PurchaseOrder(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4))
       getRSData
     }
    }
    
    getRSData
    
    dbConnection closeConnection()
    
    purchaseOrderArray
  }
  
  /**
   * Updates the customer order table with a new employee ID for the selected customer order id
   * @PARAM employeeID will be the new employee id assigned to this order
   * @PARAM purchaseOrderID is the order that will be updated by this method
   */
  
  def claimPurchaseOrder(employeeID : Int, purchaseOrderID : Int) : Unit = 
  {
    dbConnection runSQLUpdateInt("UPDATE purchaseorder SET employee_id= '", "' WHERE purchase_order_id= ", employeeID, purchaseOrderID)
   
    dbConnection closeConnection()
  }
  
    /**
   * Updates the purchase order table with a new employee ID for the selected purchase order id
   * @PARAM updatedStatus will be the new status assigned to this order
   * @PARAM purchaseOrderID is the order that will be updated by this method
   */
  
  def updateOrderStatus(purchaseOrderID : Int, updatedStatus : String) : Unit = 
  {
    dbConnection runSQLUpdateString("Update purchaseorder SET order_status= '", "' WHERE purchase_order_id= ", updatedStatus, purchaseOrderID)
    
    dbConnection closeConnection()
   
  }
  
  def addOrder(purchaseOrder : PurchaseOrder) : Unit = 
  {
    try
    {
      val connection : Connection = dbConnection connect
      val statement = connection createStatement 
      val resultSet = statement executeUpdate("INSERT INTO `purchaseorder` (`purchase_order_id`, `order_date`, `order_status`, `employee_id`) VALUES ('" + purchaseOrder.purchaseOrderID.value.toInt + "', '" + purchaseOrder.purchaseOrderDate.value + "', '" + purchaseOrder.orderStatus.value + "', '" + purchaseOrder.employeeID.value.toInt + "')")
    }
    catch
    {
      case e : SQLException => e printStackTrace
    }
    
    dbConnection closeConnection
  }
  
}