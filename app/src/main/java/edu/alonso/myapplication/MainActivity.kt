
/**
 * Created By Andy Alonso on 5/20/2025
 */

package edu.alonso.myapplication

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.ContextMenu
import android.view.GestureDetector
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlin.math.abs

// Defines a compile-time constant for the maximum number of dice.
const val MAX_DICE = 3

// MainActivity class sets up and manages the display and interaction for up to three dice, including an options menu.
class MainActivity : AppCompatActivity(), RollLengthDialogFragment.OnRollLengthSelectedListener {

    // Tracks how many dice are currently visible, initialized to the maximum.
    private var numVisibleDice = MAX_DICE
    // Will hold a list of Dice objects, each representing a single die's state and image.
    private lateinit var diceList: MutableList<Dice>
    // Will hold a list of ImageView UI elements that display the dice images.
    private lateinit var diceImageViewList: MutableList<ImageView>

    private lateinit var optionsMenu: Menu

    private var timer: CountDownTimer? = null

    private var timerLength = 2000L

    private var selectedDie = 0

    private var initTouchX = 0

    private lateinit var gestureDetector: GestureDetector



    // Initializes the activity, creates Dice objects, links them to ImageViews, and displays them.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // Essential call to the superclass for activity setup.
        setContentView(R.layout.activity_main) // Loads the UI layout from the activity_main.xml file.

        diceList = mutableListOf() // Creates an empty list to store Dice objects.
        for (i in 0 until MAX_DICE) { // Loops to create each Dice object.
            diceList.add(Dice(i + 1)) // Adds a new Dice (initially showing face i+1) to the list.
        }

        // Populates the list of ImageViews by finding them by their IDs in the layout.
        diceImageViewList = mutableListOf(
            findViewById(R.id.dice1), findViewById(R.id.dice2), findViewById(R.id.dice3))

        showDice() // Calls a function to update the displayed dice images.

        // Register context menus for all dice and tag each die
//        for (i in 0 until diceImageViewList.size) {
//            registerForContextMenu(diceImageViewList[i])
//            diceImageViewList[i].tag = i
//        }
//        // Moving finger left or right changes dice number
//        diceImageViewList[0].setOnTouchListener { v, event ->
//            var returnVal = true
//            when (event.action) {
//                MotionEvent.ACTION_DOWN -> {
//                    initTouchX = event.x.toInt()
//                }
//                MotionEvent.ACTION_MOVE -> {
//                    val x = event.x.toInt()
//
//                    // See if movement is at least 20 pixels
//                    if (abs(x - initTouchX) >= 20) {
//                        if (x > initTouchX) {
//                            diceList[0].number++
//                        } else {
//                            diceList[0].number--
//                        }
//                        showDice()
//                        initTouchX = x
//                    }
//                }
//                else -> returnVal = false
//            }
//            returnVal
//        }
        gestureDetector = GestureDetector(this,
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onDown(e: MotionEvent): Boolean {
                    return true
                }

                override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float,
                                     velocityY: Float): Boolean {
                    rollDice()
                    return true
                }
            }
        )
    }

    // Inflates and displays an options menu from an XML resource in the app bar.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar_menu, menu) // Loads menu items from appbar_menu.xml.
        optionsMenu = menu!!
        return super.onCreateOptionsMenu(menu) // Allows the system to handle further menu processing.
    }
    // Moving finger left or right changes dice number


    // Updates the ImageViews to show the current face of each visible die.
    private fun showDice() {
        for (i in 0 until numVisibleDice) { // Loops through each dice that should be visible.
            val diceDrawable = ContextCompat.getDrawable(this, diceList[i].imageId) // Gets the correct image for the current die face.
            diceImageViewList[i].setImageDrawable(diceDrawable) // Sets the ImageView to display the die's image.
            diceImageViewList[i].contentDescription = diceList[i].imageId.toString() // Sets an accessibility description (could be improved).
        }
    }
    // This method is called when a user selects an item from the options menu in the app bar.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        // Determines which menu item was clicked based on its ID and executes the corresponding action.
        return when (item.itemId) {
            // If the menu item with ID 'action_one' was selected:
            R.id.action_one -> {
                changeDiceVisibility(1) // Calls a function to make only one die visible.
                showDice()                        // Updates the display of the dice.
                true                              // Returns true to indicate that the event has been handled.
            }
            // If the menu item with ID 'action_two' was selected:
            R.id.action_two -> {
                changeDiceVisibility(2) // Calls a function to make two dice visible.
                showDice()                        // Updates the display of the dice.
                true                              // Returns true to indicate that the event has been handled.
            }
            // If the menu item with ID 'action_three' was selected:
            R.id.action_three -> {
                changeDiceVisibility(3) // Calls a function to make three dice visible.
                showDice()              // Updates the display of the dice.
                true                    // Returns true to indicate that the event has been handled.
            }
            R.id.action_stop -> {
                timer?.cancel()
                item.isVisible = false
                true
            }
            R.id.action_roll -> {
                rollDice()
                true
            }
            R.id.action_roll_length -> {
                val dialog = RollLengthDialogFragment()
                dialog.show(supportFragmentManager, "rollLengthDialog")
                true
            }
            // If the selected menu item's ID doesn't match any of the above cases:
            else -> super.onOptionsItemSelected(item) // Calls the superclass's implementation to handle any other menu items (e.g., system items).
        }
    }
    private fun rollDice() {
        optionsMenu.findItem(R.id.action_stop).isVisible = true
        timer?.cancel()

        // Start a timer that periodically changes each visible dice
        timer = object : CountDownTimer(timerLength, 100) {
            override fun onTick(millisUntilFinished: Long) {
                for (i in 0 until numVisibleDice) {
                    diceList[i].roll()
                }
                showDice()
            }

            override fun onFinish() {
                optionsMenu.findItem(R.id.action_stop).isVisible = false
            }
        }.start()
    }
    // This private function adjusts which dice ImageViews are visible or hidden on the screen.
    private fun changeDiceVisibility(numVisible: Int) {
        numVisibleDice = numVisible // Updates the global variable tracking the number of dice that should be shown.

        // This loop makes the specified number of dice ImageViews visible.
        for (i in 0 until numVisible) { // Iterates from 0 up to (but not including) 'numVisible'.
            diceImageViewList[i].visibility = View.VISIBLE // Sets the visibility of the i-th ImageView to VISIBLE.
        }

        // This loop hides any remaining dice ImageViews that exceed the 'numVisible' count.
        for (i in numVisible until MAX_DICE) { // Iterates from 'numVisible' up to (but not including) MAX_DICE.
            diceImageViewList[i].visibility = View.GONE // Sets the visibility of the i-th ImageView to GONE (it disappears and doesn't take up space).
        }
    }
    override fun onRollLengthClick(which: Int) {
        // Convert to milliseconds
        timerLength = 1000L * (which + 1)
    }
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        // Save which die is selected
        selectedDie = v?.tag as Int

        menuInflater.inflate(R.menu.context_menu, menu)
    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_one -> {
                diceList[selectedDie].number++
                showDice()
                true
            }
            R.id.subtract_one -> {
                diceList[selectedDie].number--
                showDice()
                true
            }
            R.id.roll -> {
                rollDice()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

}

