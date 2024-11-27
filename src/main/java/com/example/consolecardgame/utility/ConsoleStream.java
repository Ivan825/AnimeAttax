package com.example.consolecardgame.utility;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.OutputStream;

/**
 * ConsoleStream redirects data written to it to a JavaFX TextArea.
 */
public class ConsoleStream extends OutputStream {
    private final TextArea textArea;
    private final StringBuilder buffer = new StringBuilder();

    /**
     * Constructs a new ConsoleStream.
     *
     * @param textArea The TextArea to append text to.
     */
    public ConsoleStream(TextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) throws IOException {
        // Append the byte as a character to the buffer
        buffer.append((char) b);
        if (b == '\n') {
            flushBuffer();
        }
    }

    @Override
    public void flush() throws IOException {
        flushBuffer();
    }

    /**
     * Flushes the buffer and appends the content to the TextArea.
     */
    private void flushBuffer() {
        String text = buffer.toString();
        buffer.setLength(0); // Clear the buffer

        // Ensure that UI updates occur on the JavaFX Application Thread
        Platform.runLater(() -> textArea.appendText(text));
    }
}
