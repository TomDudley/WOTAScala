package com.qa.gui

import com.qa.databaseconnector.{PurchaseOrderLineSQL, PurchaseOrderSQL, ProductSQL}
import com.qa.entities.{PurchaseOrderLine, PurchaseOrder, Product}
import scalafx.Includes._
import scalafx.scene.Node
import scalafx.scene.control.{Label, ComboBox,TableView, TextField, Button, TableColumn}
import scalafx.event.{ActionEvent, EventHandler}
import scalafx.collections.ObservableBuffer
import scalafx.stage.Popup
import scalafx.scene.layout.{StackPane, BorderPane, GridPane}
import scalafx.scene.paint.Color
import scalafx.geometry.{Pos, Orientation, Insets}
import scalafx.scene.shape.{Rectangle}
import scalafx.stage.Popup
import scalafx.collections.ObservableBuffer
import scalafx.application.JFXApp.PrimaryStage

import java.text.SimpleDateFormat
import java.util.Date


/**
 * @author tdudley
 * 
 * @Param : employeeID is the employee who is currently logged in 
 * @Param : stage is what the pop up that is being created within the class
 *          will be added to.
 *          
 * @description : this class produces a pop up window that allows the creation of 
 *  a new purchase order. This is made up from the purchase order lines that the 
 *  user specifies the product id and the quantity required.
 * 
 */
class PurchaseOrderForm(employeeID : Int, stage : PrimaryStage) 
{
  val productIDTextField = new TextField

  val productQuantityTextField = new TextField
  
  var tempPurchaseOrderLine : ObservableBuffer[PurchaseOrderLine] = ObservableBuffer[PurchaseOrderLine]()
  
  val table = new TableView[PurchaseOrderLine](tempPurchaseOrderLine)
  
  /**
   * these methods are the column names for the table
   */
  
  val orderIDCol = new TableColumn[PurchaseOrderLine, Int]
  {
    text = "Purchase Order Line ID"
    cellValueFactory = {_.value.purchaseOrderLineID}
    prefWidth = 140
  }
  
  val quantityCol = new TableColumn[PurchaseOrderLine, Int]
  {
    text = "Quantity"
    cellValueFactory = {_.value.quantity}
    prefWidth = 120
  }  
  
  val purchaseOrderIDCol =  new TableColumn[PurchaseOrderLine, Int]
  {
    text = "Purchase Order ID"
    cellValueFactory = {_.value.purchaseOrderID}
    prefWidth = 150
  }
    
  val productIDCol = new TableColumn[PurchaseOrderLine, Int]
  {
    text = "Product ID"
    cellValueFactory = {_.value.productID}
    prefWidth = 120
  }
  
  /**
   * Method that updates the contents of the table by passing in 
   * the updated tempPurchaseOrderLine
   * 
   */
  
  def updateTable(table : TableView[PurchaseOrderLine]) : Unit = 
  {  
    table.items.update(tempPurchaseOrderLine)
  }
  
  /**
   * @return : Button 
   * 
   *  
   * Creates the button that is used for the creating the purchase order
   * also clears the purchase order line table.
   */
  
  def createUpdateButton() : Button = 
  {
    val button = new Button
    {
       text = "Add Purchase Order"
       style = "-fx-font-size: 10pt"
                
       onAction = (ae: ActionEvent) =>
       {
         createPurchaseOrder()
         
         tempPurchaseOrderLine.clear()
         
         updateTable(table)
       }
    }

    button
  }
  
  /**
   * @return : Button 
   * 
   *  
   * Creates the button that is used for the creating the purchase order line
   * also updates the purchase order line table.
   */
  
  def createAddOrderLineButton() : Button = 
  {
    val button = new Button
    {
       text = "Add Purchase Order Line"
       style = "-fx-font-size: 10pt"
                
       onAction = (ae: ActionEvent) =>
       {
         createPurchaseOrderLine()
         
         updateTable(table)
       }
    }

    button
  }
  
  /**
   * @return : Node 
   * 
   * Adds the columns to the previously defined table
   * and then returns the table
   */
  
  def createPurchaseOrderLineTable() : Node = 
  {
    table.columns += (orderIDCol, quantityCol, purchaseOrderIDCol, productIDCol)
    
    table
  }
  
  /**
   * This method allows the creation of a purchase order line
   * This gets the list of all the current purchase order lines 
   * and purchase orders from within the SQL database.
   * <p>
   * This creates a new temporary purchase order line with a order id, a calculated
   * order line id, the selected product id and quantity
   * This is then added to an Observable Buffer 
   */
  
  def createPurchaseOrderLine() : Unit = 
  {
    val purchaseOrderLineSQL : PurchaseOrderLineSQL = new PurchaseOrderLineSQL
    
    val orderLine = purchaseOrderLineSQL.findAllPurchaseOrderLines()
    
    val purchaseOrderSQL : PurchaseOrderSQL = new PurchaseOrderSQL
    
    val orders = purchaseOrderSQL.findAllPurchaseOrders()
    
    val tempOrderLineNum = (orderLine.length + 1)+ tempPurchaseOrderLine.length    
    
    val purchaseOrderLine = new PurchaseOrderLine(tempOrderLineNum, productQuantityTextField.text.getValue.toInt, orders.length + 1, productIDTextField.text.getValue.toInt)
  
    tempPurchaseOrderLine += purchaseOrderLine
  }
  
  /**
   * This method allows the creation of a purchase order
   * This gets the list of all the current purchase order lines 
   * and purchase orders from within the SQL database.
   * <p>
   * This creates a new temporary purchase order with a new order id, a calculated
   * order line id, the selected product id and quantity
   * This is then added to an Observable Buffer 
   */
  
  def createPurchaseOrder() : Unit = 
  {
    val purchaseOrderSQL : PurchaseOrderSQL = new PurchaseOrderSQL
    val purchaseOrderLineSQL : PurchaseOrderLineSQL = new PurchaseOrderLineSQL
    
    purchaseOrderSQL.addOrder(new PurchaseOrder(tempPurchaseOrderLine(0).purchaseOrderID.value.toInt, getDate, "Order Sent", employeeID))
       
    def forLoop(n : Int) : Unit = 
    {
      if(n >= tempPurchaseOrderLine.length - 1)
      {
        purchaseOrderLineSQL.addOrderLine(tempPurchaseOrderLine(n))
      }
      else
      {
        purchaseOrderLineSQL.addOrderLine(tempPurchaseOrderLine(n))
        forLoop(n + 1)
      }
    }
    
    forLoop(0)
  }
  
  /**
   * @return : String
   * 
   * This method gets the current date and returns this as a string
   * 
   */
  
  def getDate() : String = 
  {
    val date = new Date
    val sdf = new SimpleDateFormat("dd/MM/yyyy")
    val dates = sdf.format(date)
      
    dates
  }
  
  /**
   * @return : Popup
   * 
   * This method creates the pop up for where all of this class will be displayed
   * This contains 
   * 
   */
  
  def createAlertPopup() = new Popup
  {
    inner =>
    content.add(new StackPane 
    {
       
     children = List(
        new Rectangle 
        {
          width = 600
          height = 700
          //arcWidth = 20
          //arcHeight = 20
          fill = Color.White
          stroke = Color.WhiteSmoke
          strokeWidth = 4
       
        },
        
        new BorderPane 
        {
          top = new GridPane
          {
            padding = Insets(20, 20, 20, 20)
            add(
                new Label
                {
                  text = "Product ID: "
                  style = "-fx-font-size: 10pt"
                }, 1, 1)
                
            add(productIDTextField, 3, 1)
               
            add(
              new Label
              {
                text = "Product Quantity: "
                style = "-fx-font-size: 10pt"
             }, 1, 3)
            
             add(productQuantityTextField, 3, 3)
             
             add(createAddOrderLineButton, 3, 4)
              
          }
          center = createPurchaseOrderLineTable
                 
          bottom = new GridPane
          {
            add(createUpdateButton, 3, 2 )
            add(
                new Button("Close") 
                {
                  alignmentInParent = Pos.Center
                  margin = Insets(10, 0, 10, 0)
      
                  onAction = 
                  {
                   e: ActionEvent => inner.hide
                  }
            
                }, 3, 3)
          }
        }
    )
  }.delegate
  )
  }
  
  /**
   * Calls the method to create the pop up
   * and shows the pop.
   */
  def showPopUp() : Unit =
  {
    val popup = createAlertPopup()
    popup show(stage, 600, 200)
  } 
}