package pl.michalmaslak.samplemobileapp.models.models

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import pl.michalmaslak.samplemobileapp.models.WorkOrder

class WorkOrderTest {
   
    @Test
    @Throws(Exception::class)
    fun isWorkOrdersEqual_identicalProperties_returnTrue() {

        val workOrder1 = WorkOrder(1, "Title#1", "Description#1", "NEW","Creator#1",123456789, 123456789, "#Owner" )
        val workOrder2 = WorkOrder(1, "Title#1", "Description#1", "NEW","Creator#1",123456789, 123456789, "#Owner" )


        assertEquals(workOrder1, workOrder2)
        println("The work orders are equal!")
    }

    /*
        Compare work orders with 2 different pks
     */
    @Test
    @Throws(Exception::class)
    fun isWorkOrdersEqual_differentPks_returnFalse() {
        val workOrder1 = WorkOrder(1, "Title#1", "Description#1", "NEW","Creator#1",123456789, 123456789, "#Owner" )
        val workOrder2 = WorkOrder(2, "Title#1", "Description#1", "NEW","Creator#1",123456789, 123456789, "#Owner" )
        
        assertNotEquals(workOrder1, workOrder2)
        println("The work orders are not equal!")
    }

    /*
        Compare two work orders with different titles
     */
    @Test
    @Throws(Exception::class)
    fun isWorkOrdersEqual_differentTitle_returnFalse() {
        val workOrder1 = WorkOrder(1, "Title#1", "Description#1", "NEW","Creator#1",123456789, 123456789, "#Owner" )
        val workOrder2 = WorkOrder(1, "Title#2", "Description#1", "NEW","Creator#1",123456789, 123456789, "#Owner" )

        assertNotEquals(workOrder1, workOrder2)
        println("The work orders are not equal! They have different titles.")
    }

    /*
        Compare two work orders with different description
     */
    @Test
    @Throws(Exception::class)
    fun isWorkOrdersEqual_differentDescription_returnFalse() {
        val workOrder1 = WorkOrder(1, "Title#1", "Description#1", "NEW","Creator#1",123456789, 123456789, "#Owner" )
        val workOrder2 = WorkOrder(1, "Title#1", "Description#2", "NEW","Creator#1",123456789, 123456789, "#Owner" )

        assertNotEquals(workOrder1, workOrder2)
        println("The work orders are not equal! They have different description.")
    }

    /*
       Compare two work orders with different status
    */
    @Test
    @Throws(Exception::class)
    fun isWorkOrdersEqual_differentStatus_returnFalse() {
        val workOrder1 = WorkOrder(1, "Title#1", "Description#1", "NEW","Creator#1",123456789, 123456789, "#Owner" )
        val workOrder2 = WorkOrder(1, "Title#1", "Description#1", "IN_PROGRESS","Creator#1",123456789, 123456789, "#Owner" )

        assertNotEquals(workOrder1, workOrder2)
        println("The work orders are not equal! They have different status.")
    }

    /*
      Compare two work orders with different creators
   */
    @Test
    @Throws(Exception::class)
    fun isWorkOrdersEqual_differentCreatedBy_returnFalse() {
        val workOrder1 = WorkOrder(1, "Title#1", "Description#1", "NEW","Creator#1",123456789, 123456789, "#Owner" )
        val workOrder2 = WorkOrder(1, "Title#1", "Description#1", "NEW","Creator#2",123456789, 123456789, "#Owner" )

        assertNotEquals(workOrder1, workOrder2)
        println("The work orders are not equal! They have different creator.")
    }

    /*
     Compare two work orders with different create date
  */
    @Test
    @Throws(Exception::class)
    fun isWorkOrdersEqual_differentCreatedAt_returnFalse() {
        val workOrder1 = WorkOrder(1, "Title#1", "Description#1", "NEW","Creator#1",987654321, 123456789, "#Owner" )
        val workOrder2 = WorkOrder(1, "Title#1", "Description#1", "NEW","Creator#1",123456789, 123456789, "#Owner" )

        assertNotEquals(workOrder1, workOrder2)
        println("The work orders are not equal! They have different create date.")
    }

    /*
    Compare two work orders with different update date
 */
    @Test
    @Throws(Exception::class)
    fun isWorkOrdersEqual_differentUpdatedAt_returnFalse() {
        val workOrder1 = WorkOrder(1, "Title#1", "Description#1", "NEW","Creator#1",123456789, 987654321, "#Owner" )
        val workOrder2 = WorkOrder(1, "Title#1", "Description#1", "NEW","Creator#1",123456789, 123456789, "#Owner" )

        assertNotEquals(workOrder1, workOrder2)
        println("The work orders are not equal! They have different update date.")
    }

    /*
   Compare two work orders with different persons assigned
*/
    @Test
    @Throws(Exception::class)
    fun isWorkOrdersEqual_differentAssignedTo_returnFalse() {
        val workOrder1 = WorkOrder(1, "Title#1", "Description#1", "NEW","Creator#1",123456789, 123456789, "#Owner1" )
        val workOrder2 = WorkOrder(1, "Title#1", "Description#1", "NEW","Creator#1",123456789, 123456789, "#Owner2" )

        assertNotEquals(workOrder1, workOrder2)
        println("The work orders are not equal! They have different persons assigned.")
    }
}