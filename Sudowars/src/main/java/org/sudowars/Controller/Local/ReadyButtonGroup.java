/*
 * Copyright (c) 2011 - 2013 Adrian Vielsack, Christof Urbaczek, Florian Rosenthal, Michael Hoff, Moritz Lüdecke, Philip Flohr.
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

import android.view.View;
import android.view.View.OnClickListener;

/**
 * This class handles multiple {@class ReadyButton}
 */
public class ReadyButtonGroup {
    /**
     * the ready status
     */
    private boolean ready;

    /**
     * the initial local enabled status
     */
    private boolean initLocalEnabled;

    /**
     * the initial local clickable status
     */
    private boolean initLocalClickable;

    /**
     * the initial local checked status
     */
    private boolean initLocalChecked;

    /**
     * the initial remote enabled status
     */
    private boolean initRemoteEnabled;

    /**
     * the initial remote clickable status
     */
    private boolean initRemoteClickable;

    /**
     * the initial remote checked status
     */
    private boolean initRemoteChecked;

    /**
     * the local ready button
     */
    private ReadyButton local;

    /**
     * the remote ready button
     */
    private ReadyButton remote;

    /**
     * Listener used to dispatch ready events.
     */
    private OnReadyListener mOnReadyListener;

    /**
     * Listener used to dispatch local ready events.
     */
    private OnLocalReadyChangeListener mOnLocalReadyChangeListener;

    /**
     * Interface definition for a callback to be invoked when ready event is dispatched.
     */
    public interface OnReadyListener {
        /**
         * Called when all players are ready
         */
        void onReady();
    }

    /**
     * Interface definition for a callback to be invoked when local ready change event is dispatched.
     */
    public interface OnLocalReadyChangeListener {
        /**
         * Called when the local player changed the ready status
         */
        void onLocalReadyChange();
    }

    /**
     * Creates a new {@class ReadyButtonGroup}
     *
     * @param local the local ready button
     * @param remote the remote ready button
     */
    public ReadyButtonGroup(ReadyButton local, ReadyButton remote) {
        this.local = local;
        this.remote = remote;

        this.ready = false;
        this.initLocalEnabled = this.local.isEnabled();
        this.initLocalClickable = this.local.isClickable();
        this.initLocalChecked = this.local.isChecked();
        this.initRemoteEnabled = this.remote.isEnabled();
        this.initRemoteClickable = this.remote.isClickable();
        this.initRemoteChecked = this.remote.isChecked();

        this.local.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ready) {
                    mOnLocalReadyChangeListener.onLocalReadyChange();
                }
                onStatusChange();
            }
        });
        this.remote.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onStatusChange();
            }
        });
    }

    /**
     * Set the enabled state of the local ready button.
     *
     * @param enabled true if this view is enabled, false otherwise.
     */
    public void setLocalEnabled(Boolean enabled) {
        if (!ready) {
            local.setEnabled(enabled);
        }
    }

    /**
     * Enables or disables click events for the local ready button.
     *
     * @param clickable true to make the local ready button clickable, false otherwise
     */
    public void setLocalClickable(Boolean clickable) {
        if (!ready) {
            local.setClickable(clickable);
        }
    }

    /**
     * Changes the checked state of the local ready button. Has no effect if all players are ready.
     *
     * @param checked true to check the local ready button, false to uncheck it
     */
    public void setLocalChecked(Boolean checked) {
        if (!ready) {
            local.setChecked(checked);
        }
        onStatusChange();
    }

    /**
     * Changes the checked state of the remote ready button. Has no effect if all players are ready.
     *
     * @param checked true to check the remote ready button, false to uncheck it
     */
    public void setRemoteChecked(Boolean checked) {
        if (!ready) {
            remote.setChecked(checked);
        }
        onStatusChange();
    }

    /**
     * Returns if the local ready button is checked
     *
     * @return if the local ready button is checked
     */
    public boolean isLocalChecked() {
        return local.isChecked();
    }

    /**
     * Returns if the remote ready button is checked
     *
     * @return if the remote ready button is checked
     */
    public boolean isRemoteChecked() {
        return remote.isChecked();
    }

    public boolean isReady() {
        return ready;
    }

    /**
     * Register a callback to be invoked when both player are ready.
     *
     * @param l The callback that will run
     */
    public void setOnLocalReadyChangeListener(OnLocalReadyChangeListener l) {
        mOnLocalReadyChangeListener = l;
    }

    /**
     * Return whether this view has an attached OnReadyListener.
     *
     * @returns true if there is a listener, false if there is none.
     */
    public boolean hasOnLocalReadyChangeListener() {
        return (mOnLocalReadyChangeListener != null);
    }

    /**
     * Register a callback to be invoked when both player are ready.
     *
     * @param l The callback that will run
     */
    public void setOnReadyListener(OnReadyListener l) {
        mOnReadyListener = l;
    }

    /**
     * Return whether this view has an attached OnReadyListener.
     *
     * @returns true if there is a listener, false if there is none.
     */
    public boolean hasOnReadyListener() {
        return (mOnReadyListener != null);
    }

    /**
     * Calls the onReady event if all players are ready. Has no effect if all players are already ready.
     */
    private void onStatusChange() {
        if (!ready && local.isChecked() && remote.isChecked()) {
            ready = true;
            local.setClickable(false);

            if (mOnReadyListener != null) {
                mOnReadyListener.onReady();
            }
        }
    }

    /**
     * Set the ready status to false and reset all player ready buttons to the initial condition.
     */
    public void reset() {
        ready = false;

        local.setEnabled(initLocalEnabled);
        local.setClickable(initLocalClickable);
        local.setChecked(initLocalChecked);

        remote.setEnabled(initRemoteEnabled);
        remote.setClickable(initRemoteClickable);
        remote.setChecked(initRemoteChecked);
    }
}
