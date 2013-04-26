package com.gc.irc.client.swt;

import org.eclipse.swt.widgets.Display;

import com.gc.irc.client.swt.ui.ClientUI;

/**
 * The Class Main.
 */
public class Main {

    /**
     * The main method.
     * 
     * @param args
     *            the arguments
     */
    public static void main(final String[] args) {
        final Display display = new Display();
        new ClientUI(display);
        display.dispose();
    }

}
