package kop.ui;

import javax.swing.*;

/**
 * Base interface for all windows, used in the main class.
 */
public interface KopWindow {
	String MONEY_TEXT_FORMAT = "%,.2f";

	JPanel getContentPane();
	String getTitle();
}
