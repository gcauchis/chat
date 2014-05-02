/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gc.irc.client.swt.utils;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;

/**
 * The Class SWTUtils.
 *
 * @author gcauchis
 * @version 0.0.4
 */
public class SWTUtils {

    /**
     * Center.
     *
     * @param shell
     *            the shell
     */
    public static void center(final Shell shell) {
        final Rectangle bds = shell.getDisplay().getBounds();

        final Point p = shell.getSize();
        final int left = (bds.width - p.x) / 2;
        final int top = (bds.height - p.y) / 2;

        shell.setBounds(left, top, p.x, p.y);
    }

}
