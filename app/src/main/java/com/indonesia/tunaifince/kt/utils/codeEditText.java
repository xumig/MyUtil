package com.indonesia.tunaifince.kt.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.indonesia.tunaifince.kt.R;


/**
 * 验证码输入框,重写EditText的绘制方法实现。
 *
 * @author RAE
 */
public class codeEditText extends androidx.appcompat.widget.AppCompatEditText {

    private int mTextColor;

    public interface OnTextFinishListener {

        void onTextFinish(CharSequence text, int length);
    }

    // 输入的最大长度
    private int mMaxLength = 4;
    // 边框宽度
    private int mStrokeWidth =35;
    // 边框高度
    private int mStrokeHeight =35;
    // 边框之间的距离
    private int mStrokePadding = 20;

    private final Rect mRect = new Rect();


    /**
     * 输入结束监听
     */
    private OnTextFinishListener mOnInputFinishListener;

    // 方框的背景
    private Drawable mStrokeDrawable;

    // 方框聚焦的背景
    private Drawable mStrokeFocusDrawable;

    /**
     * 构造方法
     */
    public codeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.codeEditText);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = typedArray.getIndex(i);
            if (index == R.styleable.codeEditText_height) {
                this.mStrokeHeight = (int) typedArray.getDimension(index, 60);
            } else if (index == R.styleable.codeEditText_width) {
                this.mStrokeWidth = (int) typedArray.getDimension(index, 60);

            } else if (index == R.styleable.codeEditText_padding) {
                this.mStrokePadding = (int) typedArray.getDimension(index, 20);

            } else if (index == R.styleable.codeEditText_background) {
                this.mStrokeDrawable = typedArray.getDrawable(index);

            } else if (index == R.styleable.codeEditText_focusBackground) {
                this.mStrokeFocusDrawable = typedArray.getDrawable(index);

            } else if (index == R.styleable.codeEditText_length) {
                this.mMaxLength = typedArray.getInteger(index, 4);
            }
        }
        typedArray.recycle();


        if (mStrokeDrawable == null) {
            throw new NullPointerException("stroke drawable not allowed to be null!");
        }

        setMaxLength(mMaxLength);
        setLongClickable(false);
        // 去掉背景颜色
        setBackgroundColor(Color.TRANSPARENT);
        // 不显示光标
        setCursorVisible(false);
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        return false;
    }

//    private int px(int size) {
//        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, getResources().getDisplayMetrics());
//    }

    /**
     * 设置最大长度
     */
    private void setMaxLength(int maxLength) {
        if (maxLength >= 0) {
            setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        } else {
            setFilters(new InputFilter[0]);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        // 判断高度是否小于推荐高度
        if (height < mStrokeHeight) {
            height = mStrokeHeight;
        }

        // 判断高度是否小于推荐宽度
        int recommendWidth = mStrokeWidth * mMaxLength + mStrokePadding * (mMaxLength - 1);
        if (width < recommendWidth) {
            width = recommendWidth;
        }

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, widthMode);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, heightMode);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        mTextColor = getCurrentTextColor();
        setTextColor(Color.TRANSPARENT);
        super.onDraw(canvas);
        setTextColor(mTextColor);
        // 重绘背景颜色
        drawStrokeBackground(canvas);
        // 重绘文本
        drawText(canvas);
    }


    /**
     * 重绘背景
     */
    private void drawStrokeBackground(Canvas canvas) {
        // 绘制方框背景颜色
        mRect.left = 0;
        mRect.top = 0;
        mRect.right = mStrokeWidth;
        mRect.bottom = mStrokeHeight;
        int count = canvas.getSaveCount();
        canvas.save();
        for (int i = 0; i < mMaxLength; i++) {
            mStrokeDrawable.setBounds(mRect);
            mStrokeDrawable.setState(new int[]{android.R.attr.state_enabled});
            mStrokeDrawable.draw(canvas);
            float dx = mRect.right + mStrokePadding;
            // 移动画布
            canvas.save();
            canvas.translate(dx, 0);
        }
        canvas.restoreToCount(count);
        canvas.translate(0, 0);

        // 绘制激活状态的边框
        // 当前激活的索引
        int activatedIndex = Math.max(0, getEditableText().length());
        mRect.left = mStrokeWidth * activatedIndex + mStrokePadding * activatedIndex;
        mRect.right = mRect.left + mStrokeWidth;
        mStrokeFocusDrawable.setState(new int[]{android.R.attr.state_focused});
        mStrokeFocusDrawable.setBounds(mRect);
        mStrokeFocusDrawable.draw(canvas);

    }


    /**
     * 重绘文本
     */
    private void drawText(Canvas canvas) {
        int count = canvas.getSaveCount();
        canvas.translate(0, 0);
        int length = getEditableText().length();
        for (int i = 0; i < length; i++) {
            String text = String.valueOf(getEditableText().charAt(i));
            TextPaint textPaint = getPaint();
            textPaint.setColor(mTextColor);
            // 获取文本大小
            textPaint.getTextBounds(text, 0, 1, mRect);
            // 计算(x,y) 坐标
            int x = mStrokeWidth / 2 + (mStrokeWidth + mStrokePadding) * i - (mRect.centerX());
            int y = canvas.getHeight() / 2 + mRect.height() / 2;
            canvas.drawText(text, x, y, textPaint);
        }
        canvas.restoreToCount(count);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start,
                                 int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        // 当前文本长度
        int textLength = getEditableText().length();

        if (textLength == mMaxLength) {
            hideSoftInput();
            if (mOnInputFinishListener != null) {
                mOnInputFinishListener.onTextFinish(getEditableText().toString(), mMaxLength);
            }
        }

    }


    public void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public void showSoftInput(Activity activity) {
        this.requestFocus();
        InputMethodManager imm = (InputMethodManager) this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != imm) {
            imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        }

    }

    public void clearText() {
        this.setText("");
        postInvalidate();
    }

    /**
     * 设置输入完成监听
     */
    public void setOnTextFinishListener(OnTextFinishListener onInputFinishListener) {
        this.mOnInputFinishListener = onInputFinishListener;
    }

}
