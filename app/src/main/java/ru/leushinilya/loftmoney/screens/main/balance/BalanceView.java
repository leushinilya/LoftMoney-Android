package ru.leushinilya.loftmoney.screens.main.balance;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import ru.leushinilya.loftmoney.R;

public class BalanceView extends View {

    private float incomes = 500;
    private float expenses = 1000;

    public BalanceView(Context context) {
        super(context);
    }

    public BalanceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BalanceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BalanceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setIncomes(float incomes) {
        this.incomes = incomes;
        invalidate();
    }

    public void setExpenses(float expenses) {
        this.expenses = expenses;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paintIncomes = new Paint();
        Paint paintExpenses = new Paint();
        paintIncomes.setColor(getResources().getColor(R.color.apple_green));
        paintExpenses.setColor(getResources().getColor(R.color.dark_sky_blue));

        float expensesAngle = expenses / (expenses + incomes) * 360;
        float incomesAngle = incomes / (expenses + incomes) * 360;
        int space = 15;
        int radius = getWidth() - space;

        canvas.drawArc(0f,0f,radius, radius, 180 - expensesAngle/2, expensesAngle, true, paintExpenses);
        canvas.drawArc(space,0f,radius + space, radius, 360 - incomesAngle/2, incomesAngle, true, paintIncomes);


    }
}
