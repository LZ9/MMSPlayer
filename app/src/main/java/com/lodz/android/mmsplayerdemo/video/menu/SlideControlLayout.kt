package com.lodz.android.mmsplayerdemo.video.menu

import android.annotation.TargetApi
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.PointF
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.lodz.android.mmsplayerdemo.utils.ScreenUtils

/**
 * 滑动控制控件
 * Created by zhouL on 2018/10/22.
 */
class SlideControlLayout : FrameLayout {

    /** 默认竖屏屏幕宽度 */
    private var DEFAULT_SCREEN_WIDTH = 0
    /** 默认竖屏屏幕高度 */
    private var DEFAULT_SCREEN_HEIGHT = 0

    private val ADJUST_MODE_NONE = 0
    private val ADJUST_MODE_LEFT = 1
    private val ADJUST_MODE_RIGHT = 2
    private val ADJUST_MODE_HORIZONTAL = 3

    private val START_AREA_LEFT = 0
    private val START_AREA_MIDDLE = 1
    private val START_AREA_RIGHT = 2

    private val MOVE_ORENTATION_HORIZONTAL = 0
    private val MOVE_ORENTATION_VERTICAL = 1

    private val MIN_DISTANCE_TO_ADJUST = 30.0f

    private var mScreenWidth = 0
    private var mScreenHeight = 0

    private var mAdjustMode = ADJUST_MODE_NONE
    private var mLastPoint = PointF()
    private var mStartPoint = PointF()
    private var isInAdjustingMode = false
    /** 是否启用 */
    var isCanUse = false
    /** 监听器 */
    private var mListener: Listener? = null


    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context, attrs, defStyleAttr, defStyleRes
    )

    init {
        initScreenSize()
    }

    private fun initScreenSize() {
        if (!isInEditMode) {
            DEFAULT_SCREEN_WIDTH = ScreenUtils.getScreenWidth(context)
            DEFAULT_SCREEN_HEIGHT = ScreenUtils.getScreenHeight(context)
        }
    }

    /** 设置屏幕宽度和高度，[isFullScreen]是否全屏展示，[screenOrientation]Activity默认的展示方位 */
    fun setScreenSize(isFullScreen: Boolean, screenOrientation: Int) {
        if (screenOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            mScreenWidth = if (isFullScreen) DEFAULT_SCREEN_WIDTH else DEFAULT_SCREEN_HEIGHT
            mScreenHeight = if (isFullScreen) DEFAULT_SCREEN_HEIGHT else DEFAULT_SCREEN_WIDTH
        } else if (screenOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            mScreenWidth = if (isFullScreen) DEFAULT_SCREEN_HEIGHT else DEFAULT_SCREEN_WIDTH
            mScreenHeight = if (isFullScreen) DEFAULT_SCREEN_WIDTH else DEFAULT_SCREEN_HEIGHT
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (isCanUse && event != null) {
            return when (event.action) {
                MotionEvent.ACTION_DOWN -> handleActionDown(event)
                MotionEvent.ACTION_MOVE -> handleActionMove(event)
                MotionEvent.ACTION_UP -> handleActionUp(event)
                else -> super.dispatchTouchEvent(event)
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun handleActionDown(event: MotionEvent): Boolean {
        mAdjustMode = ADJUST_MODE_NONE
        mStartPoint.set(event.rawX, event.rawY)
        mLastPoint.set(event.rawX, event.rawY)
        return true
    }

    private fun handleActionMove(event: MotionEvent): Boolean {
        if (mListener == null) {
            return true
        }

        if (mAdjustMode == ADJUST_MODE_NONE) {
            mAdjustMode = getAdjustMode(event)
        }

        var deltaX = event.rawX - mLastPoint.x
        var deltaY = mLastPoint.y - event.rawY
        val enoughDistanceToAdjust = Math.abs(deltaX) > MIN_DISTANCE_TO_ADJUST
                || Math.abs(deltaY) > MIN_DISTANCE_TO_ADJUST
        deltaX = deltaX / mScreenWidth
        deltaY = deltaY / mScreenHeight

        if (mAdjustMode == ADJUST_MODE_LEFT) {
            if (enoughDistanceToAdjust) {
                if (!isInAdjustingMode) {
                    isInAdjustingMode = true
                    mListener!!.onStartSlideLeftZone()
                }
            }

            if (isInAdjustingMode) {
                mLastPoint.set(event.rawX, event.rawY)
                mListener!!.onSlidingLeftZone(deltaY)
            }
        }

        if (mAdjustMode == ADJUST_MODE_RIGHT) {
            if (enoughDistanceToAdjust) {
                if (!isInAdjustingMode) {
                    isInAdjustingMode = true
                    mListener!!.onStartSlideRightZone()
                }
            }

            if (isInAdjustingMode) {
                mLastPoint.set(event.rawX, event.rawY)
                mListener!!.onSlidingRightZone(deltaY)
            }
        }

        if (mAdjustMode == ADJUST_MODE_HORIZONTAL) {
            if (enoughDistanceToAdjust) {
                if (!isInAdjustingMode) {
                    isInAdjustingMode = true
                    mListener!!.onStartSlideHorizontal()
                }
            }

            if (isInAdjustingMode) {
                mLastPoint.set(event.rawX, event.rawY)
                mListener!!.onSlidingHorizontal(deltaX)
            }
        }
        return true
    }

    private fun handleActionUp(event: MotionEvent): Boolean {
        if (mListener == null) {
            isInAdjustingMode = false
            return true
        }

        if (!isInAdjustingMode) {
            mListener!!.onClick(this)
            return true
        }

        isInAdjustingMode = false
        when (mAdjustMode) {
            ADJUST_MODE_NONE -> mListener!!.onClick(this)
            ADJUST_MODE_LEFT -> mListener!!.onEndSlideLeftZone()
            ADJUST_MODE_RIGHT -> mListener!!.onEndSlideRightZone()
            ADJUST_MODE_HORIZONTAL -> mListener!!.onEndSlideHorizontal()
        }
        return true
    }

    private fun getAdjustMode(event: MotionEvent): Int {
        val deltaX = event.rawX - mStartPoint.x
        val deltaY = mStartPoint.y - event.rawY
        val enoughDistanceToAdjust = Math.abs(deltaX) > MIN_DISTANCE_TO_ADJUST
                || Math.abs(deltaY) > MIN_DISTANCE_TO_ADJUST
        if (!enoughDistanceToAdjust) {
            return ADJUST_MODE_NONE
        }

        val moveOrentation = getMoveOrentation(deltaX, deltaY)
        if (MOVE_ORENTATION_HORIZONTAL == moveOrentation) {
            return ADJUST_MODE_HORIZONTAL
        }

        val startArea = getStartArea(event)
        return when (startArea) {
            START_AREA_LEFT -> ADJUST_MODE_LEFT
            START_AREA_MIDDLE -> ADJUST_MODE_NONE
            START_AREA_RIGHT -> ADJUST_MODE_RIGHT
            else -> ADJUST_MODE_NONE
        }
    }

    private fun getMoveOrentation(deltaX: Float, deltaY: Float) =
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            MOVE_ORENTATION_HORIZONTAL
        } else {
            MOVE_ORENTATION_VERTICAL
        }

    private fun getStartArea(event: MotionEvent): Int {
        val x = event.rawX
        return if (x >= 0 && x < mScreenWidth / 3) {
            START_AREA_LEFT
        } else if (x >= mScreenWidth * 2 / 3 && x < mScreenWidth) {
            START_AREA_RIGHT
        } else {
            START_AREA_MIDDLE
        }
    }

    /** 设置监听器 */
    fun setListener(listener: Listener?) {
        mListener = listener
    }

    interface Listener {
        /** 点击 */
        fun onClick(view: View)

        /** 左侧区域滑动开始 */
        fun onStartSlideLeftZone()

        /** 左侧区域正在滑动 */
        fun onSlidingLeftZone(delta: Float)

        /** 左侧区域滑动结束 */
        fun onEndSlideLeftZone()

        /** 右侧区域滑动开始 */
        fun onStartSlideRightZone()

        /** 右侧区域正在滑动 */
        fun onSlidingRightZone(delta: Float)

        /** 右侧区域滑动结束 */
        fun onEndSlideRightZone()

        /** 横向滑动开始 */
        fun onStartSlideHorizontal()

        /** 正在横向滑动 */
        fun onSlidingHorizontal(delta: Float)

        /** 横向滑动结束 */
        fun onEndSlideHorizontal()
    }
}