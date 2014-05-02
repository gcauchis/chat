package com.gc.irc.client.swt;

import org.eclipse.swt.widgets.Display;

import com.gc.irc.client.swt.ui.LoginUI;

/**
 * The Class Main.
 *
 * @author gcauchis
 * @version 0.0.4
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
        // new ClientUI(display);
        new LoginUI(display);
        display.dispose();
    }

}
