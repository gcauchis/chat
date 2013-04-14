package com.gc.irc.client.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

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
        final Shell shell = new Shell(display);

        final Text helloWorldTest = new Text(shell, SWT.NONE);
        helloWorldTest.setText("Hello World SWT");
        helloWorldTest.pack();

        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();

    }

}
