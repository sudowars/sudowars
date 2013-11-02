/*
 * Copyright (c) 2011 - 2012 Adrian Vielsack, Christof Urbaczek, Florian Rosenthal, Michael Hoff, Moritz Lüdecke, Philip Flohr.
 *
 * This file is part of Sudowars.
 * Based on an official Android sample app
 * http://developer.android.com/training/implementing-navigation/lateral.html
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

public class ReadyButtonGroupTest extends AndroidTestCase {
    private boolean localReady;
    private boolean globalReady;
    private Button localButton;
    private Button remoteButton;
    private CheckBox localCheckbox;
    private CheckBox remoteCheckbox;
    private ReadyButton localReadyButton;
    private ReadyButton remoteReadyButton;
    private ReadyButtonGroup readyButtonGroup;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        localReady = false;
        globalReady = false;

        localButton = new Button(getContext());
        localCheckbox = new CheckBox(getContext());
        localReadyButton = new ReadyButton(localButton, localCheckbox);
        localReadyButton.setEnabled(true);

        remoteButton = new Button(getContext());
        remoteCheckbox = new CheckBox(getContext());
        remoteReadyButton = new ReadyButton(remoteButton, remoteCheckbox);

        readyButtonGroup = new ReadyButtonGroup(localReadyButton, remoteReadyButton);
    }

    public void testConfiguration() {
        assertEquals(readyButtonGroup.isReady(), false);
        assertEquals(readyButtonGroup.isLocalChecked(), false);
        assertEquals(readyButtonGroup.isRemoteChecked(), false);
        assertEquals(readyButtonGroup.hasOnReadyListener(), false);
        assertEquals(readyButtonGroup.hasOnLocalReadyChangeListener(), false);

        assertEquals(localButton.isClickable(), true);
        assertEquals(localButton.isEnabled(), true);
        assertEquals(remoteButton.isClickable(), false);
        assertEquals(remoteButton.isEnabled(), false);
    }

    public void testLocalChecked() {
        assertEquals(localButton.isClickable(), true);

        readyButtonGroup.setLocalChecked(true);
        assertEquals(readyButtonGroup.isLocalChecked(), true);
        assertEquals(readyButtonGroup.isLocalChecked(), localCheckbox.isChecked());
        assertEquals(localButton.isClickable(), true);
        assertEquals(readyButtonGroup.isRemoteChecked(), false);
        assertEquals(readyButtonGroup.isRemoteChecked(), remoteCheckbox.isChecked());
        assertEquals(readyButtonGroup.isReady(), false);

        readyButtonGroup.setLocalChecked(false);
        assertEquals(readyButtonGroup.isLocalChecked(), false);
        assertEquals(readyButtonGroup.isLocalChecked(), localCheckbox.isChecked());
        assertEquals(localButton.isClickable(), true);
        assertEquals(readyButtonGroup.isRemoteChecked(), false);
        assertEquals(readyButtonGroup.isRemoteChecked(), remoteCheckbox.isChecked());
        assertEquals(readyButtonGroup.isReady(), false);

        readyButtonGroup.setLocalChecked(true);
        readyButtonGroup.setRemoteChecked(true);
        assertEquals(localButton.isClickable(), false);
        assertEquals(localButton.isEnabled(), true);
        assertEquals(readyButtonGroup.isLocalChecked(), true);

        readyButtonGroup.setLocalEnabled(false);
        assertEquals(localButton.isClickable(), false);
        assertEquals(localButton.isEnabled(), true);
        assertEquals(readyButtonGroup.isLocalChecked(), true);

        readyButtonGroup.setLocalClickable(true);
        assertEquals(localButton.isClickable(), false);
        assertEquals(localButton.isEnabled(), true);
        assertEquals(readyButtonGroup.isLocalChecked(), true);

        readyButtonGroup.setLocalChecked(false);
        assertEquals(localButton.isClickable(), false);
        assertEquals(localButton.isEnabled(), true);
        assertEquals(readyButtonGroup.isLocalChecked(), true);
    }

    public void testRemoteChecked() {
        assertEquals(remoteButton.isClickable(), false);

        readyButtonGroup.setRemoteChecked(true);
        assertEquals(readyButtonGroup.isRemoteChecked(), true);
        assertEquals(readyButtonGroup.isRemoteChecked(), remoteCheckbox.isChecked());
        assertEquals(remoteButton.isClickable(), false);
        assertEquals(readyButtonGroup.isLocalChecked(), false);
        assertEquals(readyButtonGroup.isLocalChecked(), localCheckbox.isChecked());
        assertEquals(readyButtonGroup.isReady(), false);

        readyButtonGroup.setRemoteChecked(false);
        assertEquals(readyButtonGroup.isRemoteChecked(), false);
        assertEquals(readyButtonGroup.isRemoteChecked(), remoteCheckbox.isChecked());
        assertEquals(remoteButton.isClickable(), false);
        assertEquals(readyButtonGroup.isLocalChecked(), false);
        assertEquals(readyButtonGroup.isLocalChecked(), localCheckbox.isChecked());
        assertEquals(readyButtonGroup.isReady(), false);

        readyButtonGroup.setLocalChecked(true);
        readyButtonGroup.setRemoteChecked(true);
        assertEquals(remoteButton.isClickable(), false);
        assertEquals(remoteButton.isEnabled(), false);
        assertEquals(readyButtonGroup.isRemoteChecked(), true);

        readyButtonGroup.setRemoteChecked(false);
        assertEquals(remoteButton.isClickable(), false);
        assertEquals(remoteButton.isEnabled(), false);
        assertEquals(readyButtonGroup.isRemoteChecked(), true);
    }

    public void testReady() {
        assertEquals(readyButtonGroup.isReady(), false);

        readyButtonGroup.setLocalChecked(true);
        assertEquals(readyButtonGroup.isReady(), false);
        assertEquals(localButton.isClickable(), true);
        assertEquals(remoteButton.isClickable(), false);
        readyButtonGroup.setLocalChecked(false);
        assertEquals(readyButtonGroup.isReady(), false);
        assertEquals(localButton.isClickable(), true);
        assertEquals(remoteButton.isClickable(), false);

        readyButtonGroup.setRemoteChecked(true);
        assertEquals(readyButtonGroup.isReady(), false);
        assertEquals(localButton.isClickable(), true);
        assertEquals(remoteButton.isClickable(), false);
        readyButtonGroup.setRemoteChecked(false);
        assertEquals(readyButtonGroup.isReady(), false);
        assertEquals(localButton.isClickable(), true);
        assertEquals(remoteButton.isClickable(), false);

        readyButtonGroup.setLocalChecked(true);
        readyButtonGroup.setRemoteChecked(true);
        assertEquals(readyButtonGroup.isReady(), true);
        assertEquals(readyButtonGroup.isLocalChecked(), true);
        assertEquals(readyButtonGroup.isRemoteChecked(), true);
        assertEquals(localButton.isClickable(), false);
        assertEquals(remoteButton.isClickable(), false);

        readyButtonGroup.setLocalChecked(false);
        assertEquals(readyButtonGroup.isReady(), true);
        assertEquals(readyButtonGroup.isLocalChecked(), true);
        assertEquals(readyButtonGroup.isRemoteChecked(), true);
        assertEquals(localButton.isClickable(), false);
        assertEquals(remoteButton.isClickable(), false);

        readyButtonGroup.setRemoteChecked(false);
        assertEquals(readyButtonGroup.isReady(), true);
        assertEquals(readyButtonGroup.isLocalChecked(), true);
        assertEquals(readyButtonGroup.isRemoteChecked(), true);
        assertEquals(localButton.isClickable(), false);
        assertEquals(remoteButton.isClickable(), false);
    }

    public void testReset() {
        readyButtonGroup.setLocalChecked(true);
        readyButtonGroup.setRemoteChecked(true);
        readyButtonGroup.reset();
        assertEquals(readyButtonGroup.isReady(), false);
        assertEquals(readyButtonGroup.isLocalChecked(), false);
        assertEquals(readyButtonGroup.isRemoteChecked(), false);
        assertEquals(localReadyButton.isEnabled(), false);
        assertEquals(remoteReadyButton.isEnabled(), false);

    }

    public void testLocalEnabled() {
        readyButtonGroup.setLocalEnabled(false);
        assertEquals(localButton.isEnabled(), false);
        assertEquals(localButton.isClickable(), true);

        readyButtonGroup.setLocalEnabled(true);
        assertEquals(localButton.isEnabled(), true);
        assertEquals(localButton.isClickable(), true);

        readyButtonGroup.setLocalChecked(true);
        readyButtonGroup.setRemoteChecked(true);
        assertEquals(localButton.isEnabled(), true);
        assertEquals(localButton.isClickable(), false);

        readyButtonGroup.setLocalEnabled(false);
        assertEquals(localButton.isEnabled(), true);
        assertEquals(localButton.isClickable(), false);

        readyButtonGroup.setLocalEnabled(true);
        assertEquals(localButton.isEnabled(), true);
        assertEquals(localButton.isClickable(), false);
    }

    public void testOnReadyListener() {
        assertEquals(readyButtonGroup.hasOnReadyListener(), false);
        readyButtonGroup.setOnReadyListener(new ReadyButtonGroup.OnReadyListener() {
            @Override
            public void onReady() {
                globalReady = !globalReady;
            }
        });
        assertEquals(readyButtonGroup.hasOnReadyListener(), true);

        readyButtonGroup.setLocalChecked(true);
        assertEquals(globalReady, false);
        readyButtonGroup.setRemoteChecked(true);
        assertEquals(globalReady, true);

        readyButtonGroup.setLocalChecked(false);
        assertEquals(globalReady, true);
        readyButtonGroup.setLocalChecked(true);
        assertEquals(globalReady, true);
    }

    public void testOnLocalReadyChangeListener() {
        assertEquals(readyButtonGroup.hasOnLocalReadyChangeListener(), false);
        readyButtonGroup.setOnLocalReadyChangeListener(new ReadyButtonGroup.OnLocalReadyChangeListener() {
            @Override
            public void onLocalReadyChange() {
                localReady = !localReady;
            }
        });
        assertEquals(readyButtonGroup.hasOnLocalReadyChangeListener(), true);

        localButton.performClick();
        assertEquals(localReady, true);
        localButton.performClick();
        assertEquals(localReady, false);
    }
}
