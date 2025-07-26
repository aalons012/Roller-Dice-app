package edu.alonso.myapplication

/**
 * Created By Andy Alonso on 5/20/2025
 */

// Defines a compile-time constant for the largest possible number on a dice.
// 'const val' means this value is inlined at compile time and must be a top-level property or a member of an object.
const val LARGEST_NUM = 6

// Defines a compile-time constant for the smallest possible number on a dice.
const val SMALLEST_NUM = 1

// Declares a class named 'Dice'.
// It has a primary constructor that takes an integer 'diceNumber' as an argument.
class Dice(diceNumber: Int) {
    // Declares a mutable property 'imageId' of type Int, initialized to 0.
    // This will store the resource ID of the drawable image for the current dice face.
    var imageId = 0

    // Declares a mutable property 'number' of type Int, initialized with SMALLEST_NUM (which is 1).
    // This property represents the current face value of the dice.
    var number = SMALLEST_NUM
        // Defines a custom setter for the 'number' property.
        // This code block is executed whenever a new value is assigned to 'number'.
        set(value) {
            // Checks if the new 'value' is within the valid range for a dice (SMALLEST_NUM to LARGEST_NUM).
            if (value in SMALLEST_NUM..LARGEST_NUM) {
                // 'field' is a special identifier within a custom setter/getter that refers to the backing field of the property.
                // If the value is valid, it updates the backing field of 'number' to this new 'value'.
                field = value
                // Updates the 'imageId' based on the new 'value' of the dice.
                // A 'when' expression is used here as a more concise way to handle multiple if-else-if conditions.
                imageId = when (value) {
                    1 -> R.drawable.dice_1 // If value is 1, set imageId to the drawable resource for dice_1.
                    2 -> R.drawable.dice_2 // If value is 2, set imageId to the drawable resource for dice_2.
                    3 -> R.drawable.dice_3 // If value is 3, set imageId to the drawable resource for dice_3.
                    4 -> R.drawable.dice_4 // If value is 4, set imageId to the drawable resource for dice_4.
                    5 -> R.drawable.dice_5 // If value is 5, set imageId to the drawable resource for dice_5.
                    else -> R.drawable.dice_6 // For any other valid value (which can only be 6 in this case),
                    // set imageId to the drawable resource for dice_6.
                }
            }
        }

    // This is an initializer block. The code inside 'init' is executed when an instance of the 'Dice' class is created,
    // after the primary constructor has been called.
    init {
        // Sets the 'number' property of the Dice instance to the 'diceNumber' value passed to the constructor.
        // This will trigger the custom setter defined above, which also sets the initial 'imageId'.
        number = diceNumber
    }

    // Declares a public function named 'roll'.
    // This function simulates rolling the dice to get a new random number.
    fun roll() {
        // Assigns a new random number to the 'number' property.
        // (SMALLEST_NUM..LARGEST_NUM) creates an integer range from 1 to 6 (inclusive).
        // .random() picks a random integer from that range.
        // Assigning to 'number' will again trigger its custom setter, updating both 'field' (the number) and 'imageId'.
        number = (SMALLEST_NUM..LARGEST_NUM).random()
    }
}
