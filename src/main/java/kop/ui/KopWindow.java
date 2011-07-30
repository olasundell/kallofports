package kop.ui;

import javax.swing.*;

/**
 * Base interface for all windows, used in the main class.
 */
public interface KopWindow {
	JPanel getContentPane();
	String getTitle();
}
