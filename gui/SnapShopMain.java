/*
 * TCSS 305 - Assignment 3: SnapShop
 */

package gui;

import java.awt.EventQueue;

public final class SnapShopMain {

    private SnapShopMain() {
        throw new IllegalStateException();
    }

    public static void main(final String[] theArgs) {
        EventQueue.invokeLater(() -> new SnapShopGUI().start());
    }
}
