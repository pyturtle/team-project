package view.ui_elements;

import javax.swing.*;
import java.awt.*;

public class Message {

    public static JPanel createMessage(JPanel content, boolean isUser) {
        JPanel bubble = createMessageBox(content, isUser);
        JPanel row = wrapInRow(bubble, isUser);
        return row;
    }

    public static JPanel createTextBox(String messageText) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(false);


        String wrappedHtml =
                "<html><div style='width:200px;'>" +
                        messageText +
                        "</div></html>";

        JLabel message = new JLabel(wrappedHtml, SwingConstants.LEFT);
        message.setForeground(Color.WHITE);
        message.setFont(message.getFont().deriveFont(13f));

        panel.add(message);
        return panel;
    }

    private static JPanel createMessageBox(JPanel messageBoxContent, boolean isUser) {
        JPanel messageBox = new JPanel();
        messageBox.setLayout(new BoxLayout(messageBox, BoxLayout.Y_AXIS));


        Color userBubbleColor = new Color(70, 130, 180);
        Color responseBubbleColor = new Color(60, 60, 60);

        messageBox.setBackground(isUser ? userBubbleColor : responseBubbleColor);


        messageBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        messageBox.add(messageBoxContent);


        int bubbleMaxWidth = 240;

        messageBoxContent.revalidate();
        Dimension preferred = messageBox.getPreferredSize();


        preferred.width = Math.min(preferred.width, bubbleMaxWidth);

        messageBox.setMaximumSize(new Dimension(preferred.width, preferred.height));

        return messageBox;
    }

    public static void scrollToBottom(JScrollPane scroll) {
        SwingUtilities.invokeLater(() -> {
            JScrollBar bar = scroll.getVerticalScrollBar();
            bar.setValue(bar.getMaximum());
        });
    }

    private static JPanel wrapInRow(JPanel messageBubble, boolean isUser) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setOpaque(false);

        row.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        if (isUser) {
            row.add(Box.createHorizontalGlue());
            row.add(messageBubble);
        } else {
            row.add(messageBubble);
            row.add(Box.createHorizontalGlue());
        }

        return row;
    }
}
