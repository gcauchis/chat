/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gc.irc.client.swt.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.gc.irc.client.swt.api.AbstractUI;
import com.gc.irc.client.swt.utils.SWTUtils;

/**
 * The Class ClientUI.
 *
 * @version 0.0.4
 * @author x472511
 */
public class ClientUI extends AbstractUI {

    /**
     * Instantiates a new client ui.
     *
     * @param display
     *            the display
     */
    public ClientUI(final Display display) {
        super(display);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.client.swt.api.AbstractUI#initShell(org.eclipse.swt.widgets
     * .Shell)
     */
    /** {@inheritDoc} */
    @Override
    protected void initShell(final Shell shell) {
        shell.setText("IRC Client SWT");

        final Text helloWorldTest = new Text(shell, SWT.NONE);
        helloWorldTest.setText("Hello World SWT");
        helloWorldTest.pack();

        shell.pack();
        SWTUtils.center(shell);
    }

}
