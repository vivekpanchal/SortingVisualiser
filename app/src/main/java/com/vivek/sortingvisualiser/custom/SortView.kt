package com.vivek.sortingvisualiser.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.random.Random

private const val TAG = "SortView"

private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    // Paint styles used for rendering are initialized here. This
    // is a performance optimization, since onDraw() is called
    // for every screen refresh.
    style = Paint.Style.FILL_AND_STROKE
    textAlign = Paint.Align.CENTER
    textSize = 40.0f
    color = Color.BLACK
    typeface = Typeface.create("", Typeface.BOLD)
}

private var viewWidth = 400
private var viewHeight = 400

private var currentIndex = 0
private var swapIndex1 = 0;
private var swapIndex2 = 0;
private var lastSortedElement = 0;
private var array = IntArray(10) { Random.nextInt(1, 10) }

class SortView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        for ((index, value) in array.withIndex()) {
            canvas?.let {
                drawElement(it, index)
            }
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h
    }

    private fun drawElement(canvas: Canvas, index: Int) {
        val rectangleToDraw = getRect(index)
        val rectangleColor = getColor(index)
        canvas.drawRect(rectangleToDraw!!, rectangleColor)
    }

    public fun swap() {
        Thread(Runnable {
            var numSwaps = 0
            var isSorted = false
            var lastUnsorted = array.size - 1
            Log.d(TAG, "swap: array before sorting ${array.contentToString()}")
            while (!isSorted) {
                isSorted = true
                for (i in 0 until lastUnsorted) {
                    if (array[i] > array[i + 1]) {
                        swapIndex1 = array[i]
                        swapIndex2 = array[i + 1]
                        swapValues(array, i, i + 1)
                        isSorted = false
                        numSwaps++
                        invalidate()
                        Log.d(TAG, "swap: array inside sorting ${array.contentToString()}")
                        Thread.sleep(1000)
                    }
                }
                lastUnsorted--
                lastSortedElement = array[array.lastIndex]
            }
            Log.d(TAG, "swap: array after sorting in $numSwaps = ${array.contentToString()}")
        }).start();
    }

    public fun shuffle() {
        array = IntArray(10) { Random.nextInt(1, 10) }
        swapIndex1 = 0
        swapIndex2 = 0
        lastSortedElement = 0
        invalidate()
    }


    private fun swapValues(array: IntArray, a: Int, b: Int) {
        val temp = array[b]
        array[b] = array[a]
        array[a] = temp
        swapIndex1 = a
        swapIndex2 = b
    }


    private fun getRect(index: Int): Rect? {
        val barWidth: Int = (viewWidth - array.size) / array.size
        val rect = Rect()
        rect.left = index * barWidth + index
        rect.top = viewHeight - (array[index] * (viewHeight / array.size))
        rect.bottom = viewHeight
        rect.right = index * barWidth + barWidth + index
        return rect
    }

    private fun getColor(index: Int): Paint {
        return when {
            array[index] == swapIndex1 -> {
                val color = Paint()
                color.color = Color.BLACK
                color
            }
            array[index] == swapIndex2 -> {
                val color = Paint()
                color.color = Color.BLACK
                color
            }
            array[index] == lastSortedElement -> {
                val color = Paint()
                color.color = Color.GREEN
                color
            }
            else -> {
                val color = Paint()
                color.color = Color.BLUE
                color
            }
        }
    }
}