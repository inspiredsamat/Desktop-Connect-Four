package four;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ConnectFour extends JFrame {
    private static final int CELL_SIZE = 20;
    private static final int ROWS = 6;
    private static final int COLUMNS = 7;

    private final Font buttonFont = new Font("Courier", Font.BOLD, 40);
    private final Font resetButtonFont = new Font("Courier", Font.BOLD, 20);
    private final List<JButton> buttons = new ArrayList<>();
    private final Cell[][] cells = new Cell[ROWS][COLUMNS];

    private Order playerOrder = Order.X;
    private boolean hasEnded = false;

    public ConnectFour() {
        initWindow();
        addComponents();
        setVisible(true);
    }

    public void initWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(COLUMNS * CELL_SIZE, ROWS * CELL_SIZE);
        setLayout(new BorderLayout());
        setTitle("Connect Four");
        setLocationRelativeTo(null);
    }

    public void addComponents() {
        JPanel buttonsPanel = new JPanel(new GridLayout(ROWS, COLUMNS));
        JPanel resetButtonPanel = new JPanel();
        List<String> rowNames = List.of("6", "5", "4", "3", "2", "1");
        List<String> columnNames = List.of("A", "B", "C", "D", "E", "F", "G");

        for (int i = 0; i < rowNames.size(); i++) {
            for (int j = 0; j < columnNames.size(); j++) {
                buttonsPanel.add(createButton(columnNames.get(j) + rowNames.get(i)));
                cells[i][j] = new Cell(i, j);
            }
        }

        add(buttonsPanel);

        resetButtonPanel.add(createResetButton());
        add(resetButtonPanel, BorderLayout.SOUTH);
    }

    private JButton createButton(final String name) {
        var button = new JButton();
        button.setName("Button" + name);
        button.setFocusPainted(false);
        button.setFont(buttonFont);
        button.setText(" ");
        button.setBackground(Color.GRAY);

        button.addActionListener(e -> {
            if (!hasEnded) {
                String columnName = getColumnName(button);
                int columnNumber = getColumnNumber(columnName);

                for (int i = buttons.size() - 1 - columnNumber; i >= 0; i = i - 7) {
                    JButton currentButton = buttons.get(i);
                    if (!(currentButton.getText().equals("X") || currentButton.getText().equals("O"))) {
                        cells[i / 7][Math.abs(columnNumber - 6)].setText(playerOrder == Order.X ? "X" : "O");
                        setText(currentButton);
                        check(currentButton.getText(), i / 7, Math.abs(columnNumber - 6));
                        return;
                    }
                }
            }
        });
        buttons.add(button);
        return button;
    }

    private JButton createResetButton() {
        var button = new JButton();
        button.setName("ButtonReset");
        button.setText("Reset");
        button.setFocusPainted(false);
        button.setBackground(Color.BLACK);
        button.setForeground(Color.RED);
        button.setFont(resetButtonFont);
        button.addActionListener(e -> resetBoard());

        return button;
    }

    private void check(String player, int row, int column) {
        checkVertical(player, column);
        checkHorizontal(player, row);
        checkRightDiagonal(player, row, column);
        checkLeftDiagonal(player, row, column);
    }

    private void checkLeftDiagonal(String player, int row, int column) {
        List<Cell> winCells = new ArrayList<>();
        int rowStart = Math.max(row + column - 6, 0);
        int columnStart = Math.min(row + column, 6);

        for (int i = rowStart, j = columnStart; i < 6 && j >= 0; i++, j--) {
            if (cells[i][j].getText().equals(player)) {
                winCells.add(cells[i][j]);
                if (winCells.size() > 3) {
                    markWinCells(winCells);
                }
            } else {
                winCells.clear();
            }
        }
    }

    private void checkRightDiagonal(String player, int row, int column) {
        List<Cell> winCells = new ArrayList<>();
        int rowStart = Math.max(row - column, 0);
        int columnStart = Math.max(column - row, 0);

        for (int i = rowStart, j = columnStart; i < 6 && j < 7; i++, j++) {
            if (cells[i][j].getText().equals(player)) {
                winCells.add(cells[i][j]);
                if (winCells.size() > 3) {
                    markWinCells(winCells);
                }
            } else {
                winCells.clear();
            }
        }
    }

    private void checkHorizontal(String player, int row) {
        List<Cell> winCells = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            if (cells[row][i].getText().equals(player)) {
                winCells.add(cells[row][i]);
                if (winCells.size() > 3) {
                    markWinCells(winCells);
                }
            } else {
                winCells.clear();
            }
        }
    }

    private void checkVertical(String player, int column) {
        List<Cell> winCells = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            if (cells[i][column].getText().equals(player)) {
                winCells.add(cells[i][column]);
                if (winCells.size() > 3) {
                    markWinCells(winCells);
                }
            } else {
                winCells.clear();
            }
        }
    }

    private void markWinCells(List<Cell> winCells) {
        for (Cell cell : winCells) {
            int i = cell.getRow() * 7 + cell.getColumn();
            buttons.get(i).setBackground(Color.BLUE);
            hasEnded = true;
        }
    }

    private void resetBoard() {
        for (JButton button : buttons) {
            button.setBackground(Color.GRAY);
            button.setText(" ");
        }

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                cells[i][j].setText(" ");
            }
        }

        playerOrder = Order.X;
        hasEnded = false;
    }

    private String getColumnName(JButton button) {
        String name = button.getName();
        return name.substring(6, 7);
    }

    private int getColumnNumber(String columnName) {
        if (columnName.equals("G"))
            return 0;
        if (columnName.equals("F"))
            return 1;
        if (columnName.equals("E"))
            return 2;
        if (columnName.equals("D"))
            return 3;
        if (columnName.equals("C"))
            return 4;
        if (columnName.equals("B"))
            return 5;
        return 6;
    }

    private void setText(JButton button) {
        switch (playerOrder) {
            case X -> {
                button.setText("X");
                playerOrder = Order.O;
            }
            case O -> {
                button.setText("O");
                playerOrder = Order.X;
            }
        }
    }
}