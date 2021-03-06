package com.qa.gui

import scalafx.Includes._
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.Scene.sfxScene2jfx
import scalafx.stage.Stage.sfxStage2jfx
import scalafx.scene.layout.{HBox, VBox}
import scalafx.application.JFXApp.PrimaryStage
import scalafx.application.JFXApp
import scalafx.scene.control.{TabPane, Tab}
import scalafx.geometry.Insets
import scalafx.scene.paint.Color._
import scalafx.scene.paint._

/**
 * @author tdudley
 * 
 * This class will start the GUI and hold the tabs 
 * The tabs will contain the other pages 
 * 
 * 
 */
class GUIMain(employeeID : Int) extends JFXApp()
{  
  /**
   * @return : TabPane 
   * 
   * Creates the tab panes that contain the three main gui classes
   */
  
  def createTabs() : TabPane = 
  {
    new TabPane {
      tabs = List(
      
       new Tab {
         text = "Customer Orders"
         closable = false
         
         val custOrderGUI : CustomerOrderGUI = new CustomerOrderGUI(employeeID, stage)
         
         content = new VBox
         {
           padding = Insets(10, 100, 100, 10)
           
           children = Seq(
             custOrderGUI createCustomerOrderTable,
             custOrderGUI createGridPane
             
           )
         }
       },
       
       new Tab{
         text = "Purchase Orders"
         closable = false
         
         val purchaseOrderGUI : PurchaseOrderGUI = new PurchaseOrderGUI(employeeID, stage)
         
         content = new VBox
         {
           padding = Insets(10, 100, 100, 10)
           
           children = Seq(
             purchaseOrderGUI createPurchaseOrderTable,
             purchaseOrderGUI createGridPane
             
           )
         }
         
       },
       
       new Tab{
         text = "Inventory"
         
         closable = false
         
         val productGUI : ProductGUI = new ProductGUI(stage)
         
         content = new VBox
         {
           padding = Insets(10, 100, 100, 10)
           
           children = Seq(
               productGUI createProductTable,
               productGUI createBorderPane
           
           )    
         }
       }
      
      )
    }
  }
 
  /*
   * Creates the scene to hold the tabs
   */
  def createScene() : Scene =
  {
    val scene2 = new Scene
    {  
      content = new HBox
      {   
        children = Seq(
        
            createTabs()
        )
      }
    }
    
    scene2
  }

  /**
   * When called, sets this scene to the stage and shows 
   * the scene  
   */
  def showGUI(): Unit = 
  {
 
    stage = new PrimaryStage()
    
    stage setScene(createScene())
    
    stage show
    
    stage title = "Warehouse Order Tracking Application"
    stage width = 580
    stage height = 600

  }
  
}
