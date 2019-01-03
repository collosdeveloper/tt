package com.knyaz.testtask.base.ui.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

import com.knyaz.testtask.R;

import java.util.regex.Pattern;

public class EllipsizingTextView extends android.support.v7.widget.AppCompatTextView {
    private final int DEFAULT_MAX_LINES = 3;
    private SpannableStringBuilder moreText;
    private SpannableStringBuilder lessText;

    private static final Pattern DEFAULT_END_PUNCTUATION = Pattern.compile("[\\.!?,;:\u2026]*$", Pattern.DOTALL);
    private EllipsizeStrategy mEllipsizeStrategy;
    private boolean isEllipsized;
    private boolean isStale;
    private boolean programmaticChange;
    private Spanned mFullText;
    private int mMaxLines;
    private float mLineSpacingMult = 1.0f;
    private float mLineAddVertPad = 0.0f;

    private Pattern mEndPunctPattern;

    public EllipsizingTextView(Context context) {
        this(context, null);
        initClickListener();
    }

    public EllipsizingTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
        initClickListener();
    }

    public EllipsizingTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs,
                new int[]{android.R.attr.maxLines}, defStyle, 0);
        setMaxLines(a.getInt(0, DEFAULT_MAX_LINES));
        a.recycle();
        setEndPunctuationPattern(DEFAULT_END_PUNCTUATION);
        initClickListener();
    }

    private void initClickListener() {
        setOnClickListener(v -> {
            if (mMaxLines == Integer.MAX_VALUE) {
                setMaxLines(DEFAULT_MAX_LINES);
                setEllipsize(TextUtils.TruncateAt.END);
            } else {
                setMaxLines(Integer.MAX_VALUE);
                setEllipsize(null);
            }
            resetText();
        });
    }

    private void initEdgeText() {
        if (moreText == null) {
            moreText = new SpannableStringBuilder()
                    .append(" ")
                    .append(new SpannableString(getContext().getString(R.string.more)));
            ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            moreText.setSpan(foregroundSpan, 0, moreText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (lessText == null) {
            lessText = new SpannableStringBuilder()
                    .append(" ")
                    .append(new SpannableString(getContext().getString(R.string.less)));
            ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            lessText.setSpan(foregroundSpan, 0, lessText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    public void setEndPunctuationPattern(Pattern pattern) {
        mEndPunctPattern = pattern;
    }

    public boolean isEllipsized() {
        return isEllipsized;
    }

    @SuppressLint("Override")
    public int getMaxLines() {
        return mMaxLines;
    }

    @Override
    public void setMaxLines(int maxLines) {
        super.setMaxLines(maxLines);
        mMaxLines = maxLines;
        isStale = true;
    }

    public boolean ellipsizingLastFullyVisibleLine() {
        return mMaxLines == Integer.MAX_VALUE;
    }

    @Override
    public void setLineSpacing(float add, float mult) {
        mLineAddVertPad = add;
        mLineSpacingMult = mult;
        super.setLineSpacing(add, mult);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        initEdgeText();
        if (!programmaticChange) {
            mFullText = new SpannableString(text);
            isStale = true;
        }
        super.setText(text, type);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (ellipsizingLastFullyVisibleLine()) isStale = true;
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        if (ellipsizingLastFullyVisibleLine()) isStale = true;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        if (isStale) resetText();
        super.onDraw(canvas);
    }

    private void resetText() {
        int maxLines = getMaxLines();
        CharSequence workingText = mFullText;
        boolean ellipsized = false;

        if (maxLines != -1) {
            if (mEllipsizeStrategy == null) setEllipsize(null);
            workingText = mEllipsizeStrategy.processText(mFullText);
            ellipsized = !mEllipsizeStrategy.isInLayout(mFullText);
        }

        if (!workingText.equals(getText())) {
            programmaticChange = true;
            try {
                setText(workingText);
            } finally {
                programmaticChange = false;
            }
        }

        isStale = false;
        if (ellipsized != isEllipsized) {
            isEllipsized = ellipsized;
        }
    }

    @Override
    public void setEllipsize(TextUtils.TruncateAt where) {
        initEdgeText();
        if (where == null) {
            mEllipsizeStrategy = new EllipsizeNoneStrategy();
            return;
        }
        switch (where) {
            case END:
                mEllipsizeStrategy = new EllipsizeEndStrategy();
                break;
            case MARQUEE:
                super.setEllipsize(where);
                isStale = false;
            default:
                mEllipsizeStrategy = new EllipsizeNoneStrategy();
                break;
        }
    }

    private abstract class EllipsizeStrategy {
        public CharSequence processText(CharSequence text) {
            return !isInLayout(text) ? createEllipsizedText(text) : text;
        }

        public boolean isInLayout(CharSequence text) {
            Layout layout = createWorkingLayout(text);
            return layout.getLineCount() <= DEFAULT_MAX_LINES;
        }

        protected Layout createWorkingLayout(CharSequence workingText) {
            return new StaticLayout(workingText, getPaint(),
                    getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                    Layout.Alignment.ALIGN_NORMAL, mLineSpacingMult,
                    mLineAddVertPad, false /* includepad */);
        }

        protected int getLinesCount() {
            if (ellipsizingLastFullyVisibleLine()) {
                int fullyVisibleLinesCount = getFullyVisibleLinesCount();
                return fullyVisibleLinesCount == -1 ? 1 : fullyVisibleLinesCount;
            } else {
                return mMaxLines;
            }
        }

        protected int getFullyVisibleLinesCount() {
            Layout layout = createWorkingLayout("");
            int height = getHeight() - getCompoundPaddingTop() - getCompoundPaddingBottom();
            int lineHeight = layout.getLineBottom(0);
            return height / lineHeight;
        }

        protected abstract CharSequence createEllipsizedText(CharSequence fullText);
    }

    private class EllipsizeNoneStrategy extends EllipsizeStrategy {
        @Override
        protected CharSequence createEllipsizedText(CharSequence fullText) {
            return new SpannableStringBuilder(fullText).append(lessText);
        }
    }

    private class EllipsizeEndStrategy extends EllipsizeStrategy {
        @Override
        protected CharSequence createEllipsizedText(CharSequence fullText) {
            Layout layout = createWorkingLayout(fullText);
            int cutOffIndex = layout.getLineEnd(mMaxLines - 1);
            int textLength = fullText.length();
            int cutOffLength = textLength - cutOffIndex;
            if (cutOffLength < moreText.length()) cutOffLength = moreText.length();
            String workingText = TextUtils.substring(fullText, 0, textLength - cutOffLength).trim();
            String strippedText = stripEndPunctuation(workingText);

            while (!isInLayout(strippedText + moreText)) {
                int lastSpace = workingText.lastIndexOf(' ');
                if (lastSpace == -1) break;
                workingText = workingText.substring(0, lastSpace).trim();
                strippedText = stripEndPunctuation(workingText);
            }
            workingText = strippedText + moreText;
            SpannableStringBuilder dest = new SpannableStringBuilder(strippedText).append(moreText);

            if (fullText instanceof Spanned) {
                TextUtils.copySpansFrom((Spanned) fullText, 0, workingText.length(), null, dest, 0);
            }
            return dest;
        }

        public String stripEndPunctuation(CharSequence workingText) {
            return mEndPunctPattern.matcher(workingText).replaceFirst("");
        }
    }
}