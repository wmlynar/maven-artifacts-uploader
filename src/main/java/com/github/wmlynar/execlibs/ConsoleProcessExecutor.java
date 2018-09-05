package com.github.wmlynar.execlibs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ConsoleProcessExecutor {

	private Process process;

	private Thread inputThread;
	private Thread errorThread;

	private boolean shutdown = false;

	public boolean start(String command) {

		try {
			process = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			System.err.println("Exception caught while starting command: " + e.getMessage() + "\n" + stackTraceToString(e));
			return false;
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
		inputThread = new Thread(new Runnable() {
			@Override
			public void run() {
				String str;
				try {
					while ((str = br.readLine()) != null && !shutdown) {
						System.out.println(str);
					}
				} catch (IOException e) {
					System.err.println("Exception caught while reading from input stream: " + e.getMessage() + "\n" + stackTraceToString(e));
				}
			}
		});

		BufferedReader er = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		errorThread = new Thread(new Runnable() {
			@Override
			public void run() {
				String str;
				try {
					while ((str = er.readLine()) != null && !shutdown) {
						System.err.println(str);
					}
				} catch (IOException e) {
					System.err.println("Exception caught while reading from error stream: " + e.getMessage() + "\n" + stackTraceToString(e));
				}
			}
		});
		inputThread.start();
		errorThread.start();

		return true;
	}

	public int waitFor() throws InterruptedException {
		return process.waitFor();
	}

	public void stop() {
		shutdown = true;
		inputThread.interrupt();
		errorThread.interrupt();
		process.destroy();
	}
	
	private static String stackTraceToString(Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}

}
