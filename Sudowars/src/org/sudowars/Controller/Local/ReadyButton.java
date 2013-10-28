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

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

public class ReadyButton {
    /**
     * the toggle button
     */
    private Button button;

    /**
     * the checkbox
     */
    private CheckBox checkbox;

    /**
     * Creates a new {@class ReadyButton}
     *
     * @param button the button
     * @param checkbox the checkbox
     */
    public ReadyButton(Button button, CheckBox checkbox) {
        this.button = button;
        this.checkbox = checkbox;

        this.checkbox.setClickable(false);

        this.button.setOnClickListener(
                new OnClickListener() {
                    public void onClick(View v) {
                        ReadyButton.this.checkbox.toggle();
                    }
                });
    }

    /**
     * Returns the button
     *
     * @return the button
     */
    public Button getButton() {
        return this.button;
    }

    /**
     * Returns the checkbox
     *
     * @return the checkbox
     */
    public CheckBox getCheckBox() {
        return this.checkbox;
    }

    /**
     * Returns if the button is checked
     *
     * @return if the button is checked
     */
    public boolean isChecked() {
        return this.checkbox.isChecked();
    }

    /**
     * Changes the checked state of this button.
     *
     * @param checked true to check the button, false to uncheck it
     */
    public void setChecked(Boolean checked) {
        this.checkbox.setChecked(checked);
    }

    /**
     * Change the checked state of the view to the inverse of its current state
     */
    public void toggle() {
        this.checkbox.toggle();
    }

    /**
     * Enables or disables click events for this view. When a view is clickable it will change its state to "pressed"
     * on every click. Subclasses should set the view clickable to visually react to user's clicks.
     *
     * @param clickable true to make the view clickable, false otherwise
     */
    public void setClickable(Boolean clickable) {
        this.button.setClickable(clickable);
    }

    /**
     * Indicates whether this view reacts to click events or not.
     *
     * @return true if the view is clickable, false otherwise
     */
    public boolean isClickable() {
        return this.button.isClickable();
    }

    /**
     * Set the enabled state of this view. The interpretation of the enabled state varies by subclass.
     *
     * @param enabled true if this view is enabled, false otherwise.
     */
    public void setEnabled(Boolean enabled) {
        this.button.setEnabled(enabled);
        this.checkbox.setEnabled(enabled);
    }

    /**
     * Returns the enabled status for this view. The interpretation of the enabled state varies by subclass.
     *
     * @return true if this view is enabled, false otherwise.
     */
    public boolean isEnabled() {
        return this.checkbox.isEnabled();
    }

    /**
     * Register a callback to be invoked when this view is clicked. If this view is not
     * clickable, it becomes clickable.
     *
     * @param l The callback that will run
     *
     * @see #setClickable(boolean)
     */
    public void setOnClickListener(OnClickListener l) {
        if (!isClickable()) {
            setClickable(true);
        }

        this.button.setOnClickListener(l);
    }
}