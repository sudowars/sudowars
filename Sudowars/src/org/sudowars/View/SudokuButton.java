/*
 * Copyright (c) 2011 - 2012 Adrian Vielsack, Christof Urbaczek, Florian Rosenthal, Michael Hoff, Moritz Lüdecke, Philip Flohr.
 *
 * This file is part of Sudowars.
 *
 * Sudowars is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Sudowars is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sudowars.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 * initial API and implementation:
 * Adrian Vielsack
 * Christof Urbaczek
 * Florian Rosenthal
 * Michael Hoff
 * Moritz Lüdecke
 * Philip Flohr
 */
package org.sudowars.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class SudokuButton extends Button {
    /**
     * The scale factor of the text size
     */
    private static final float SCALE_FACTOR = (float) 0.4;

    public SudokuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        refreshTextSize();
    }

    protected void onSizeChanged(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        super.onSizeChanged(newWidth, newWidth, oldWidth, oldHeight);

        refreshTextSize();
    }

    /**
     * Scale the text to the button size
     */
    private void refreshTextSize() {
        float textSize = (float) (SCALE_FACTOR * Math.min(getWidth(), getHeight()));
        this.setTextSize(textSize);
    }
}