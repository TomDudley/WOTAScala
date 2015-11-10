package DatabaseConnector

import Entities.Product
import java.sql.SQLException
import java.sql.Connection
import scalafx.collections.ObservableBuffer

/**
 * @author tdudley
 */
class ProductSQL {
  
  val dbConnection = new DBConnector()
  
  def findAllProducts() : ObservableBuffer[Product] = 
  {
    var productArray : ObservableBuffer[Product] = ObservableBuffer[Product]()
    
    try{
      
      val connection : Connection = dbConnection connect()
      
      val statement = connection createStatement
      val resultSet = statement executeQuery("SELECT * FROM product")
      
      while(resultSet next())
      {
        productArray += new Product(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getFloat(4), resultSet.getString(5))
      } 
    } catch {
        case e : SQLException => e printStackTrace
    }
      dbConnection closeConnection
      
      productArray
    
  }
  
  def findByProductID(productID : Int) : Product =
  {
    var product = new Product(99999999, "", "", 0, "")
    
    try{
      
      val connection : Connection = dbConnection connect
      
      val statement = connection createStatement 
      val resultSet = statement executeQuery("SELECT * FROM product WHERE product_id =" + productID)
      
      while(resultSet.next())
      {
        product = new Product(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getFloat(4), resultSet.getString(5))
      }
    } catch {
      case e : SQLException => e printStackTrace
    }
    
    product
  }
  
  
}