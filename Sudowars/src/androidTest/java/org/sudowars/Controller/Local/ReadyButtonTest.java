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

package org.sudowars.Controller.Local;

import android.test.AndroidTestCase;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class ReadyButtonTest extends AndroidTestCase {
    private boolean clicked;
    private Button button;
    private CheckBox checkbox;
    private ReadyButton readyButton;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        clicked = false;
        button = new Button(getContext());
        checkbox = new CheckBox(getContext());
        readyButton = new ReadyButton(button, checkbox);
    }

    public void testConfiguration() {
        assertEquals(readyButton.getButton(), button);
        assertEquals(readyButton.getCheckBox(), checkbox);
        assertEquals(checkbox.isClickable(), false);
    }

    public void testChecked() {
        readyButton.setChecked(true);
        assertEquals(readyButton.isChecked(), true);
        assertEquals(checkbox.isChecked(), true);

        readyButton.setChecked(false);
        assertEquals(readyButton.isChecked(), false);
        assertEquals(checkbox.isChecked(), false);
    }

    public void testClickable() {
        readyButton.setClickable(false);
        assertEquals(readyButton.isClickable(), false);
        assertEquals(button.isClickable(), false);

        readyButton.setClickable(true);
        assertEquals(readyButton.isClickable(), true);
        assertEquals(button.isClickable(), true);
    }

    public void testToggle() {
        readyButton.toggle();
        assertEquals(readyButton.isChecked(), true);
        assertEquals(checkbox.isChecked(), true);

        readyButton.toggle();
        assertEquals(readyButton.isChecked(), false);
        assertEquals(checkbox.isChecked(), false);
    }

    public void testEnabled() {
        readyButton.setEnabled(false);
        assertEquals(button.isEnabled(), false);
        assertEquals(button.isClickable(), true);
        assertEquals(checkbox.isEnabled(), false);

        readyButton.setEnabled(true);
        assertEquals(button.isEnabled(), true);
        assertEquals(button.isClickable(), true);
        assertEquals(checkbox.isEnabled(), true);
    }

    public void testOnClickListener() {
        assertEquals(readyButton.hasOnClickListener(), false);
        readyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked = true;
            }
        });
        assertEquals(readyButton.hasOnClickListener(), true);

        button.performClick();
        assertEquals(clicked, true);
        assertEquals(checkbox.isChecked(), true);
        button.performClick();
        assertEquals(checkbox.isChecked(), false);
    }
}
