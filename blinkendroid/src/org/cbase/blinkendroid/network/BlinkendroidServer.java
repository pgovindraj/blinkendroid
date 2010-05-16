/*
 * Copyright 2010 the original author or authors.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.cbase.blinkendroid.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.cbase.blinkendroid.Constants;

import android.util.Log;

public class BlinkendroidServer extends Thread{
	private boolean running=false;
	private BlinkendroidProtocol blinkendroidProtocol;
	private int port=4444;
	public BlinkendroidServer(int port) {
		this.port=4444;
	}


	@Override
	public void run() {
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			Log.e(Constants.LOG_TAG, "Could not create Socket",e);
			return;
		}
		running=true;
		Log.i(Constants.LOG_TAG,"BlinkendroidServer Thread started");
		while(running){
			try {
				Socket clientSocket = serverSocket.accept();
				blinkendroidProtocol	=	new BlinkendroidProtocol(clientSocket,true);
				if(null!=blinkendroidProtocol)
				    blinkendroidProtocol.startTimerThread();
			} catch (IOException e) {
				Log.e(Constants.LOG_TAG, "BlinkendroidServer Could not accept",e);
			}
		}
		try {
			serverSocket.close();
		} catch (IOException e) {
			Log.e(Constants.LOG_TAG, "Could not close",e);
		}
		Log.i(Constants.LOG_TAG,"BlinkendroidServer Thread closed");
	}

	public void end(){
        	if(null!=blinkendroidProtocol)
        	    blinkendroidProtocol.stopTimerThread();
        	running=false;
        	Log.i(Constants.LOG_TAG, "BlinkendroidServer Thread ended");
        	interrupt();
	}
	
	public boolean isRunning() {
	    return running;
	}


	public BlinkendroidProtocol getProtocol() {
		return blinkendroidProtocol;
	}
}