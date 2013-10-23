/*******************************************************************************
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
 * 
 * 
 * Diese Datei ist Teil von Sudowars.
 * 
 * Sudowars ist Freie Software: Sie können es unter den Bedingungen
 * der GNU General Public License, wie von der Free Software Foundation,
 * Version 3 der Lizenz oder (nach Ihrer Option) jeder späteren
 * veröffentlichten Version, weiterverbreiten und/oder modifizieren.
 * 
 * Sudowars wird in der Hoffnung, dass es nützlich sein wird, aber
 * OHNE JEDE GEWÄHELEISTUNG, bereitgestellt; sogar ohne die implizite
 * Gewährleistung der MARKTFÄHIGKEIT oder EIGNUNG FÜR EINEN BESTIMMTEN ZWECK.
 * Siehe die GNU General Public License für weitere Details.
 * 
 * Sie sollten eine Kopie der GNU General Public License zusammen mit diesem
 * Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 * initial API and implementation:
 * Adrian Vielsack
 * Christof Urbaczek
 * Florian Rosenthal
 * Michael Hoff
 * Moritz Lüdecke
 * Philip Flohr 
 ******************************************************************************/
package org.sudowars.View;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Button;

import org.sudowars.R;

public class SudokuImageButton extends Button {
    /**
     * The scale factor of the text size
     */
    private static final float SCALE_FACTOR = (float) 0.6;

    /**
     * The button icon
     */
    private Drawable drawable;

    public SudokuImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (this.getId() == R.id.key_clear) {
            this.drawable = getResources().getDrawable(R.drawable.ic_input_delete);
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        refreshImageSize();
    }

    protected void onSizeChanged(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        super.onSizeChanged(newWidth, newWidth, oldWidth, oldHeight);

        refreshImageSize();
    }

    public void setTextColor(int color) {
        super.setTextColor(color);

        drawable.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
    }

    /**
     * Scale the text to the button size
     */
    private void refreshImageSize() {
        if (this.drawable != null) {
            int imageSize = (int) (SCALE_FACTOR * Math.min(getWidth(), getHeight()));
            imageSize = Math.min(imageSize, this.drawable.getIntrinsicWidth());
            int spaceSize = (getWidth() - imageSize) / 2;
            this.drawable.setBounds(spaceSize, 0, imageSize + spaceSize, imageSize);
            setCompoundDrawables(this.drawable, null, null, null);


            /*
            ScaleDrawable scaleDrawable = new ScaleDrawable(drawable, 0, imageSize, imageSize);
            scaleDrawable.setBounds(spaceSize, 0, imageSize + spaceSize, imageSize);
            setCompoundDrawables(scaleDrawable.getDrawable(), null, null, null);
            */

            /*
            this.drawable.setBounds(0, 0,
                    (int) (0.5 * this.drawable.getIntrinsicWidth()),
                    (int) (0.5 * this.drawable.getIntrinsicHeight()));
            ScaleDrawable sd = new ScaleDrawable(this.drawable, 0, imageSize, imageSize);
            setCompoundDrawables(sd.getDrawable(), null, null, null);
            */

            //FIXME: Workaround: SudokuImageButton must have the same size as SudokuButtons
            this.setHeight(0);
            this.setWidth(0);

        }
    }
}