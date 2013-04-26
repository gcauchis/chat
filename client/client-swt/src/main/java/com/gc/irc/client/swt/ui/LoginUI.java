/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gc.irc.client.swt.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.gc.irc.client.swt.api.AbstractUI;
import com.gc.irc.client.swt.utils.SWTUtils;

/**
 * The Class LoginUI.
 * 
 * @author gcauchis
 */
public class LoginUI extends AbstractUI {

    /**
     * Instantiates a new login ui.
     * 
     * @param display
     *            the display
     */
    public LoginUI(final Display display) {
        super(display);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.client.swt.api.AbstractUI#initShell(org.eclipse.swt.widgets
     * .Shell)
     */
    @Override
    protected void initShell(final Shell shell) {
        shell.setText("Login");
        final GridLayout gridLayout = new GridLayout(2, true);
        shell.setLayout(gridLayout);

        final Label labelServerMessage = new Label(shell, SWT.CENTER);
        labelServerMessage.setText("message from server");
        GridData gridData = new GridData();
        gridData.horizontalSpan = 2;
        gridData.horizontalAlignment = GridData.CENTER;
        labelServerMessage.setLayoutData(gridData);

        final Label labelLogin = new Label(shell, SWT.CENTER);
        labelLogin.setText("Login :");
        gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING, SWT.FILL, false, false);
        labelLogin.setLayoutData(gridData);

        final Text textLogin = new Text(shell, SWT.SINGLE);
        gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
        textLogin.setLayoutData(gridData);

        final Label labelPassword = new Label(shell, SWT.CENTER);
        labelPassword.setText("Password :");
        gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING, SWT.FILL, false, false);
        labelPassword.setLayoutData(gridData);

        final Text textPassword = new Text(shell, SWT.PASSWORD);
        gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
        textPassword.setLayoutData(gridData);

        final Button buttonValidation = new Button(shell, SWT.PUSH);
        buttonValidation.setText("Login");
        gridData.horizontalSpan = 2;
        gridData.horizontalAlignment = GridData.HORIZONTAL_ALIGN_END;
        buttonValidation.setLayoutData(gridData);

        shell.pack();
        SWTUtils.center(shell);

    }
}
