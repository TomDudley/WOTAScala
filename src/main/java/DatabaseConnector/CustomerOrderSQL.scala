package DatabaseConnector

import Entities.CustomerOrder
import java.sql.SQLException
import java.sql.Connection
import scalafx.collections.ObservableBuffer

/**
 * @author tdudley
 */
class CustomerOrderSQL {
  
  val dbConnection = new DBConnector()
      
  /*
   * Finds all customer orders within the SQL database 
   * and returns them as an ObservableBuffer
   */
  
  def findAllCustomerOrders() : ObservableBuffer[CustomerOrder] = 
  {
    val customerOrderArray : ObservableBuffer[CustomerOrder] = ObservableBuffer[CustomerOrder]()
    
    try{
      
      val connection : Connection = dbConnection connect()
      
      val statement = connection createStatement()
      val resultSet = statement executeQuery("SELECT customerorder_id, employee_id, order_date, order_status FROM customerorder")
    
      while(resultSet next())
      {     
        customerOrderArray += new CustomerOrder(resultSet.getInt(1), resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4))
      }
    } catch {
      case e : SQLException => e printStackTrace
    }
    
    
    dbConnection closeConnection()
    
    customerOrderArray
  }
  
  def findByCustomerID(customerID : Int) : CustomerOrder = 
  {
    var customerOrder : CustomerOrder = new CustomerOrder(999999999, 0, "", "")
    
    try{
      
      val connection : Connection = dbConnection connect()
      
      val statement = connection createStatement()
      val resultSet = statement executeQuery("SELECT customerorder_id, employee_id, order_date, order_status FROM customerorder WHERE customerorder_id = " + customerID)
      
      while(resultSet next())
      {
        customerOrder = new CustomerOrder(resultSet.getInt(1), resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4))
      }
      
     
      
    } catch {
      case e : SQLException => e printStackTrace
      
    }
    
    dbConnection closeConnection() 
    customerOrder
  }
  
  /*
   * Returns an ObservableBuffer of CustomerOrder with the given employeeID
   * @Param employeeID, used to find selected orders with this employeeID
   */
  
  def findByEmployeeID(employeeID : Int) : ObservableBuffer[CustomerOrder] = 
  {
    val customerOrderArray : ObservableBuffer[CustomerOrder] = ObservableBuffer[CustomerOrder]()
    
    try{
      
      val connection : Connection = dbConnection connect()
      
      val statement = connection createStatement()
      val resultSet = statement executeQuery("SELECT customerorder_id, employee_id, order_date, order_status FROM customerorder WHERE employee_id = " + employeeID)
    
      while(resultSet next())
      {     
        customerOrderArray += new CustomerOrder(resultSet.getInt(1), resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4))
      }
    } catch {
      case e : SQLException => e printStackTrace
    }
    
    
    dbConnection closeConnection()
    
    customerOrderArray
  }
  
  def claimCustomerOrder(employeeID : Int, customerOrderID : Int) : Unit = 
  {
    try
    {
      val connection : Connection = dbConnection connect()
      val statement = connection createStatement()
      val resultSet = statement executeUpdate("UPDATE customerorder SET employee_id= '" + employeeID + "' WHERE customerorder_id= " + customerOrderID)

    }
    catch {
      case e : SQLException => e printStackTrace
    }
    
    dbConnection closeConnection()
  }
  
  def updateOrderStatus(customerOrderID : Int, updatedStatus : String) : Unit = 
  {
    try
    {
      val connection : Connection = dbConnection connect()
      val statement = connection createStatement()
      val resultSet = statement executeQuery("UPDATE customerorder SET order_status= '" + updatedStatus + "' WHERE order_id= " + customerOrderID)
    }
    catch {
      case e : SQLException => e printStackTrace
    }
    
    dbConnection closeConnection()
   
  }
  
}