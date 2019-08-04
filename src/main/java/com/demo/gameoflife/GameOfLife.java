package com.demo.gameoflife;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameOfLife {
	JFrame jFrame;
	JLabel positionLabel;
	JButton resetButton;
	JButton countButton;
	JButton jButton;
	JButton nextButton;

	int gridSize = 4;
	boolean[][] liveCellArray;
	boolean[][] previouslyClickedArray = new boolean[gridSize][gridSize];

	JButton[][] actualButtonArray = new JButton[gridSize][gridSize];

	JButton[][] resetButtonArray;
	int count;

	private static final String INITIAL_TEXT = "No Cell is live";
	private static final String LIVE_CELL = " cells are alive";

	public static void main(String args[]) {
		GameOfLife gameOfLife = new GameOfLife();
	}

	private GameOfLife() {
		jFrame = new JFrame("My Frame");
		jFrame.setBounds(50, 50, 600, 400);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addComponent(jFrame);
	}

	private void addComponent(JFrame jFrame) {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
		mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 4));

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));

		JPanel labelPanel = new JPanel();
		positionLabel = new JLabel(INITIAL_TEXT, JLabel.CENTER);

		JPanel resetButtonPanel = new JPanel();
		resetButton = new JButton("Reset");

		JPanel countLiveCellsButtonPanel = new JPanel();
		countButton = new JButton("Count");

		JPanel nextButtonPanel = new JPanel();
		nextButton = new JButton("Next");

		// resetButton Listener
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				resetButton(resetButtonArray);
			}
		});

		// countButton Listener
		countButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				countButton();
			}
		});

		// nextButton Listener
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				nextGeneration(liveCellArray, resetButtonArray);
			}
		});

		labelPanel.add(positionLabel);
		leftPanel.add(labelPanel);
		resetButtonPanel.add(resetButton);
		leftPanel.add(resetButtonPanel);
		countLiveCellsButtonPanel.add(countButton);
		leftPanel.add(countLiveCellsButtonPanel);
		nextButtonPanel.add(nextButton);
		leftPanel.add(nextButtonPanel);
		mainPanel.add(leftPanel);

		// set GridPanel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(gridSize, gridSize));
		liveCellArray = new boolean[gridSize][gridSize];
		resetButtonArray = new JButton[gridSize][gridSize];

		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				jButton = new JButton("(" + i + ", " + j + ")");
				jButton.setBackground(Color.GRAY);
				jButton.setActionCommand(i + "," + j);
				final int k = i;
				final int l = j;

				actualButtonArray[k][l] = jButton;
				jButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						JButton jButton = (JButton) ae.getSource();
						resetButtonArray[k][l] = jButton;
						jButton.setBackground(Color.GREEN);
						liveCellArray[k][l] = true;
					}
				});
				buttonPanel.add(jButton);
				mainPanel.add(buttonPanel);
				jFrame.add(mainPanel);
				jFrame.setVisible(true);
			}
		}
	}

	// resetButton
	private void resetButton(JButton[][] resetButtonArray) {
		for (int i = 0; i < liveCellArray.length; i++) {
			for (int j = 0; j < liveCellArray.length; j++) {
				if (liveCellArray[i][j]) {
					resetButtonArray[i][j].setBackground(Color.GRAY);
					previouslyClickedArray[i][j] = false;
					liveCellArray[i][j] = false;
				}
			}
		}
		positionLabel.setText(INITIAL_TEXT);
		count = 0;
	}

	// countButton
	private void countButton() {
		for (int i = 0; i < liveCellArray.length; i++) {
			for (int j = 0; j < liveCellArray.length; j++) {
				if (liveCellArray[i][j] && isInPreviouslyClickedArray(liveCellArray[i][j], i, j)) {
					count++;
					positionLabel.setText(count + LIVE_CELL);
				}
			}
		}
	}

	// isInPreviouslyClickedArray
	private boolean isInPreviouslyClickedArray(boolean value, int row, int column) {
		boolean flag = false;
		for (int i = 0; i < previouslyClickedArray.length; i++) {
			for (int j = 0; j < previouslyClickedArray.length; j++) {
				if (i == row && j == column) {
					if (!previouslyClickedArray[i][j]) {
						previouslyClickedArray[i][j] = true;
						flag = true;
					}
				}
			}
		}
		return flag;
	}

	private void nextGeneration(boolean[][] liveCellArray, JButton[][] resetButtonArray) {
		for (int i = 0; i < liveCellArray.length; i++) {
			for (int j = 0; j < liveCellArray.length; j++) {
				if ((i == 0 && j == 0) || (i == 0 && j == gridSize - 1) || (i == gridSize - 1 && j == 0)
						|| (i == gridSize - 1 && j == gridSize - 1)) {
					if (liveCellArray[i][j]) {
						liveToNextGenerationForFourCorners(i, j);
					} else if (!liveCellArray[i][j]) {
						makeCellLiveForFourCorners(i, j);
					}
				} else if ((i == 0 && j != 0) || (i == 0 && j != gridSize - 1)) {
					checkCountOfLiveAndDeadCellsForFirstRow(i, j);
				} else if ((i == gridSize - 1 && j != 0) || (i == gridSize - 1 && j != 0)) {
					checkCountOfLiveAndDeadCellsForLastRow(i, j);
				} else if ((i != 0 && j == 0) || (i != gridSize - 1 && j == 0)) {
					checkCountOfLiveAndDeadCellsForFirstColumn(i, j);
				} else if ((i != 0 && j == gridSize - 1) || (i != gridSize - 1 && j == gridSize - 1)) {
					checkCountOfLiveAndDeadCellsForLastColumn(i, j);
				} else if (i > 0 && j > 0) {
					if (i == gridSize - 2) {
						checkCountOfLiveAndDeadCellsForSecondLastRow(i, j);
					} else {
						checkCountOfLiveAndDeadCellsForMiddleRows(i, j);
					}
				}
			}
		}
	}

	// make cell live or dead for last column
	private void checkCountOfLiveAndDeadCellsForLastColumn(int row, int column) {
		int liveCount = 0;
		int deadCount = 0;
		if (liveCellArray[row][column]) {
			liveCount++;
		} else {
			deadCount++;
		}

		if (liveCellArray[row - 1][column - 1]) {
			liveCount++;
		} else {
			deadCount++;
		}

		if (liveCellArray[row][column - 1]) {
			liveCount++;
		} else {
			deadCount++;
		}

		if (liveCellArray[row + 1][column - 1]) {
			liveCount++;
		} else {
			deadCount++;
		}

		if (liveCellArray[row + 1][column]) {
			liveCount++;
		} else {
			deadCount++;
		}

		makeCellLiveDeadForCornerRowsColumns(liveCount, row, column);
	}

	// make cell live or dead for first column
	private void checkCountOfLiveAndDeadCellsForFirstColumn(int row, int column) {
		int liveCount = 0;
		int deadCount = 0;
		if (liveCellArray[row - 1][column]) {
			liveCount++;
		} else {
			deadCount++;
		}

		if (liveCellArray[row - 1][column + 1]) {
			liveCount++;
		} else {
			deadCount++;
		}

		if (liveCellArray[row][column + 1]) {
			liveCount++;
		} else {
			deadCount++;
		}

		if (liveCellArray[row + 1][column + 1]) {
			liveCount++;
		} else {
			deadCount++;
		}

		if (liveCellArray[row + 1][column]) {
			liveCount++;
		} else {
			deadCount++;
		}
		makeCellLiveDeadForCornerRowsColumns(liveCount, row, column);
	}

	// make cell live dead for corner rows column
	private void makeCellLiveDeadForCornerRowsColumns(int liveCount, int row, int column) {
		if (liveCellArray[row][column]) {
			if (liveCount > 3) {
				actualButtonArray[row][column].setBackground(Color.RED);
				resetButtonArray[row][column] = actualButtonArray[row][column];
				liveCellArray[row][column] = false;
			} else if (liveCount < 2) {
				actualButtonArray[row][column].setBackground(Color.RED);
				resetButtonArray[row][column] = actualButtonArray[row][column];
				liveCellArray[row][column] = false;
			} else if (liveCount == 2 || liveCount == 3) {
				actualButtonArray[row][column].setBackground(Color.GREEN);
				resetButtonArray[row][column] = actualButtonArray[row][column];
				liveCellArray[row][column] = true;
			}
		} else if (!liveCellArray[row][column]) {
			if (liveCount == 3) {
				actualButtonArray[row][column].setBackground(Color.GREEN);
				resetButtonArray[row][column] = actualButtonArray[row][column];
				liveCellArray[row][column] = true;
			}
		}
	}

	// make cell live or dead for last row
	private void checkCountOfLiveAndDeadCellsForLastRow(int row, int column) {
		int liveCount = 0;
		int deadCount = 0;

		if (liveCellArray[row][column - 1]) {
			liveCount++;
		} else {
			deadCount++;
		}

		if (liveCellArray[row - 1][column - 1]) {
			liveCount++;
		} else {
			deadCount++;
		}

		if (liveCellArray[row - 1][column]) {
			liveCount++;
		} else {
			deadCount++;
		}

		if (liveCellArray[row - 1][column + 1]) {
			liveCount++;
		} else {
			deadCount++;
		}

		if (liveCellArray[row][column + 1]) {
			liveCount++;
		} else {
			deadCount++;
		}
		makeCellLiveDeadForCornerRowsColumns(liveCount, row, column);
	}

	// make cells live or dead for first row
	private void checkCountOfLiveAndDeadCellsForFirstRow(int row, int column) {
		int liveCount = 0;
		int deadCount = 0;

		if (liveCellArray[row][column - 1]) {
			liveCount++;
		} else {
			deadCount++;
		}

		if (liveCellArray[row + 1][column - 1]) {
			liveCount++;
		} else {
			deadCount++;
		}

		if (liveCellArray[row + 1][column]) {
			liveCount++;
		} else {
			deadCount++;
		}

		if (liveCellArray[row + 1][column + 1]) {
			liveCount++;
		} else {
			deadCount++;
		}

		if (liveCellArray[row][column + 1]) {
			liveCount++;
		} else {
			deadCount++;
		}
		makeCellLiveDeadForCornerRowsColumns(liveCount, row, column);
	}

	// make cell live or dead for second last row
	private void checkCountOfLiveAndDeadCellsForSecondLastRow(int row, int column){
			int liveCount = 0;
			int deadCount = 0;
			
			if(liveCellArray[row+1][column]){
				liveCount++;
			}else{
				deadCount++;
			}
			
			if(liveCellArray[row+1][column-1]){
				liveCount++;
			}else{
				deadCount++;
			}
			
			if(liveCellArray[row][column-1]){
				liveCount++;
			}else{
				deadCount++;
			}
			
			if(liveCellArray[row-1][column-1]){
				liveCount++;
			}else{
				deadCount++;
			}
			
			if(liveCellArray[row-1][column]){
				liveCount++;
			}else{
				deadCount++;
			}
			
			if(liveCellArray[row-1][column+1]){
				liveCount++;
			}else{
				deadCount++;
			}
			
			if(liveCellArray[row][column+1]){
				liveCount++;
			}else{
				deadCount++;
			}
			
			if(liveCellArray[row+1][column+1]){
				liveCount++;
			}else{
				deadCount++;
			}
			
			makeCellLiveDeadForCornerRowsColumns(liveCount, row, column);
		}

	private void checkCountOfLiveAndDeadCellsForMiddleRows(int row, int column) {
		int liveCount = 0;
		int deadCount = 0;

		if (liveCellArray[row][column - 1]) {
			liveCount++;
		} else {
			deadCount++;
		}

		if (liveCellArray[row - 1][column - 1]) {
			liveCount++;
		} else {
			deadCount++;
		}

		if (liveCellArray[row - 1][column]) {
			liveCount++;
		} else {
			deadCount++;
		}

		if (liveCellArray[row - 1][column + 1]) {
			liveCount++;
		} else {
			deadCount++;
		}

		if (liveCellArray[row][column + 1]) {
			liveCount++;
		} else {
			deadCount++;
		}

		if (liveCellArray[row + 1][column + 1]) {
			liveCount++;
		} else {
			deadCount++;
		}

		if (liveCellArray[row + 1][column]) {
			liveCount++;
		} else {
			deadCount++;
		}

		if (liveCellArray[row + 1][column - 1]) {
			liveCount++;
		} else {
			deadCount++;
		}
		makeCellLiveDeadForCornerRowsColumns(liveCount, row, column);
	}

	// Any Dead cell with exactly three live neighbours becomes live cell , as
	// if by reproduction,
	// if corner cell is dead then that cell becomes live if it has exactly
	// three live neighbours
	// because of reproduction. Cells are (0,0),(0,3),(3,0) and (3,3)
	private void makeCellLiveForFourCorners(int row, int column) {
		if (row == 0 && column == 0) {
			if (liveCellArray[row][column + 1] && liveCellArray[row + 1][column + 1]
					&& liveCellArray[row + 1][column]) {
				actualButtonArray[row][column].setBackground(Color.GREEN);
				resetButtonArray[row][column] = actualButtonArray[row][column];
				liveCellArray[row][column] = true;
			} else if (liveCellArray[row + 1][column] && liveCellArray[row + 1][column + 1]
					&& liveCellArray[row][column + 1]) {
				actualButtonArray[row][column].setBackground(Color.GREEN);
				resetButtonArray[row][column] = actualButtonArray[row][column];
				liveCellArray[row][column] = true;
			} else if(liveCellArray[row+1][column] && liveCellArray[row][column+1] && liveCellArray[row+1][column+1]){
				actualButtonArray[row][column].setBackground(Color.GREEN);
				resetButtonArray[row][column] = actualButtonArray[row][column];
				liveCellArray[row][column] = true;
			}
		} else if(row == 0 && column == gridSize -1){
			if(liveCellArray[row][column-1] && liveCellArray[row+1][column-1] && liveCellArray[row+1][column]){
				actualButtonArray[row][column].setBackground(Color.GREEN);
				resetButtonArray[row][column] = actualButtonArray[row][column];
				liveCellArray[row][column] = true;
			}else if(liveCellArray[row+1][column] && liveCellArray[row+1][column-1] && liveCellArray[row][column-1]){
				actualButtonArray[row][column].setBackground(Color.GREEN);
				resetButtonArray[row][column] = actualButtonArray[row][column];
				liveCellArray[row][column] = true;
			}else if(liveCellArray[row+1][column] && liveCellArray[row][column-1] && liveCellArray[row+1][column-1]){
				actualButtonArray[row][column].setBackground(Color.GREEN);
				resetButtonArray[row][column] = actualButtonArray[row][column];
				liveCellArray[row][column] = true;
			}
		} else if(row == gridSize - 1 && column == 0){
			if(liveCellArray[row][column+1] && liveCellArray[row-1][column+1] && liveCellArray[row-1][column]){
				actualButtonArray[row][column].setBackground(Color.GREEN);
				resetButtonArray[row][column] = actualButtonArray[row][column];
				liveCellArray[row][column] = true;
			}else if (liveCellArray[row-1][column] && liveCellArray[row-1][column+1] && liveCellArray[row][column+1]){
				actualButtonArray[row][column].setBackground(Color.GREEN);
				resetButtonArray[row][column] = actualButtonArray[row][column];
				liveCellArray[row][column] = true;
			}else if (liveCellArray[row-1][column] && liveCellArray[row][column+1] && liveCellArray[row-1][column+1]){
				actualButtonArray[row][column].setBackground(Color.GREEN);
				resetButtonArray[row][column] = actualButtonArray[row][column];
				liveCellArray[row][column] = true;
			}
		} else if(row == gridSize - 1 && column == gridSize - 1){
			if(liveCellArray[row-1][column] && liveCellArray[row-1][column-1] && liveCellArray[row][column-1]){
				actualButtonArray[row][column].setBackground(Color.GREEN);
				resetButtonArray[row][column] = actualButtonArray[row][column];
				liveCellArray[row][column] = true;
			}else if(liveCellArray[row][column-1] && liveCellArray[row-1][column-1] && liveCellArray[row-1][column]){
				actualButtonArray[row][column].setBackground(Color.GREEN);
				resetButtonArray[row][column] = actualButtonArray[row][column];
				liveCellArray[row][column] = true;
			}else if(liveCellArray[row-1][column] && liveCellArray[row][column-1] && liveCellArray[row-1][column-1]){
				actualButtonArray[row][column].setBackground(Color.GREEN);
				resetButtonArray[row][column] = actualButtonArray[row][column];
				liveCellArray[row][column] = true;
			}
		}
	}
	
	// Any live cell with fewer than two live neighbors dies as if caused by under population,
	// if corner cell is live and it has two or three live neighbors then it will live to next
	// generation and becomes dead if it has only one live neighbor
	private void liveToNextGenerationForFourCorners(int row, int column){
		if(row == 0 && column ==0){
			if ((liveCellArray[row][column+1] && liveCellArray[row+1][column+1]) || (liveCellArray[row][column+1] && liveCellArray[row+1][column+1]
					&& liveCellArray[row+1][column])){
				actualButtonArray[row][column].setBackground(Color.GREEN);
				resetButtonArray[row][column] = actualButtonArray[row][column];
				liveCellArray[row][column] = true;
			} else if ((liveCellArray[row+1][column] && liveCellArray[row+1][column+1]) || (liveCellArray[row+1][column] && liveCellArray[row+1][column+1]
					&& liveCellArray[row][column+1])){
				actualButtonArray[row][column].setBackground(Color.GREEN);
				resetButtonArray[row][column] = actualButtonArray[row][column];
				liveCellArray[row][column] = true;
			} else if ((liveCellArray[row+1][column] && liveCellArray[row][column+1]) || (liveCellArray[row+1][column] && liveCellArray[row][column+1]
					&& liveCellArray[row+1][column+1])){
				actualButtonArray[row][column].setBackground(Color.GREEN);
				resetButtonArray[row][column] = actualButtonArray[row][column];
				liveCellArray[row][column] = true;
			} else {
				if((liveCellArray[row][column] && liveCellArray[row][column+1]) && !(liveCellArray[row+1][column] && liveCellArray[row+1][column+1])){
					actualButtonArray[row][column].setBackground(Color.RED);
					resetButtonArray[row][column] = actualButtonArray[row][column];
					liveCellArray[row][column] = false;
				}else if((liveCellArray[row][column] && liveCellArray[row+1][column+1]) && !(liveCellArray[row+1][column] && liveCellArray[row][column+1])){
					actualButtonArray[row][column].setBackground(Color.RED);
					resetButtonArray[row][column] = actualButtonArray[row][column];
					liveCellArray[row][column] = false;
				}else if((liveCellArray[row][column] && liveCellArray[row+1][column]) && !(liveCellArray[row][column+1] && liveCellArray[row+1][column+1])){
					actualButtonArray[row][column].setBackground(Color.RED);
					resetButtonArray[row][column] = actualButtonArray[row][column];
					liveCellArray[row][column] = false;
				}
			}
		} else if(row == 0 && column == gridSize - 1){
			if((liveCellArray[row][column-1] && liveCellArray[row+1][column-1]) 
					|| (liveCellArray[row][column-1] && liveCellArray[row+1][column-1] && liveCellArray[row+1][column])){
				actualButtonArray[row][column].setBackground(Color.GREEN);
				resetButtonArray[row][column] = actualButtonArray[row][column];
				liveCellArray[row][column] = true;
			} else if((liveCellArray[row+1][column] && liveCellArray[row+1][column-1]) 
					|| (liveCellArray[row+1][column] && liveCellArray[row+1][column-1] && liveCellArray[row][column-1])){
				actualButtonArray[row][column].setBackground(Color.GREEN);
				resetButtonArray[row][column] = actualButtonArray[row][column];
				liveCellArray[row][column] = true;
			} else if((liveCellArray[row+1][column] && liveCellArray[row][column-1]) 
					|| (liveCellArray[row+1][column] && liveCellArray[row][column-1] && liveCellArray[row+1][column-1])){
				actualButtonArray[row][column].setBackground(Color.GREEN);
				resetButtonArray[row][column] = actualButtonArray[row][column];
				liveCellArray[row][column] = true;
			} else{
				if((liveCellArray[row][column] && liveCellArray[row][column-1]) && !(liveCellArray[row+1][column-1] && liveCellArray[row+1][column])){
					actualButtonArray[row][column].setBackground(Color.RED);
					resetButtonArray[row][column] = actualButtonArray[row][column];
					liveCellArray[row][column] = false;
				}else if((liveCellArray[row][column] && liveCellArray[row+1][column-1]) && !(liveCellArray[row+1][column] && liveCellArray[row][column-1])){
					actualButtonArray[row][column].setBackground(Color.RED);
					resetButtonArray[row][column] = actualButtonArray[row][column];
					liveCellArray[row][column] = false;
				}else if((liveCellArray[row][column] && liveCellArray[row+1][column]) && !(liveCellArray[row][column-1] && liveCellArray[row+1][column-1])){
					actualButtonArray[row][column].setBackground(Color.RED);
					resetButtonArray[row][column] = actualButtonArray[row][column];
					liveCellArray[row][column] = false;
				}
			}
		} else if (row == gridSize - 1 && column == 0){
			if((liveCellArray[row][column+1] && liveCellArray[row-1][column+1]) || 
					(liveCellArray[row][column+1] && liveCellArray[row-1][column+1] && liveCellArray[row-1][column])){
				actualButtonArray[row][column].setBackground(Color.GREEN);
				resetButtonArray[row][column] = actualButtonArray[row][column];
				liveCellArray[row][column] = true;
			}else if((liveCellArray[row-1][column] && liveCellArray[row-1][column+1]) || 
					(liveCellArray[row-1][column] && liveCellArray[row-1][column+1] && liveCellArray[row][column+1])){
				actualButtonArray[row][column].setBackground(Color.GREEN);
				resetButtonArray[row][column] = actualButtonArray[row][column];
				liveCellArray[row][column] = true;
			} else if((liveCellArray[row-1][column] && liveCellArray[row][column+1]) || 
					(liveCellArray[row-1][column] && liveCellArray[row][column+1] && liveCellArray[row-1][column+1])){
				actualButtonArray[row][column].setBackground(Color.GREEN);
				resetButtonArray[row][column] = actualButtonArray[row][column];
				liveCellArray[row][column] = true;
			}else{
				if((liveCellArray[row][column] && liveCellArray[row][column+1]) && !(liveCellArray[row-1][column] && liveCellArray[row-1][column+1])){
					resetButtonArray[row][column].setBackground(Color.RED);
				}else if((liveCellArray[row][column] && liveCellArray[row-1][column+1]) && !(liveCellArray[row][column+1] && liveCellArray[row-1][column])){
					resetButtonArray[row][column].setBackground(Color.RED);
				}else if((liveCellArray[row][column] && liveCellArray[row-1][column]) && !(liveCellArray[row][column+1] && liveCellArray[row-1][column+1])){
					resetButtonArray[row][column].setBackground(Color.RED);
				}
			}
		} else if(row == gridSize - 1 && column == gridSize - 1){
			if((liveCellArray[row-1][column] && liveCellArray[row-1][column-1]) 
					|| (liveCellArray[row-1][column] && liveCellArray[row-1][column-1] && liveCellArray[row][column-1])){
				actualButtonArray[row][column].setBackground(Color.GREEN);
				resetButtonArray[row][column] = actualButtonArray[row][column];
				liveCellArray[row][column] = true;
			} else if((liveCellArray[row][column-1] && liveCellArray[row-1][column-1]) 
					|| (liveCellArray[row][column-1] && liveCellArray[row-1][column-1] && liveCellArray[row-1][column])){
				actualButtonArray[row][column].setBackground(Color.GREEN);
				resetButtonArray[row][column] = actualButtonArray[row][column];
				liveCellArray[row][column] = true;
			} else if((liveCellArray[row-1][column] && liveCellArray[row][column-1]) 
					|| (liveCellArray[row-1][column] && liveCellArray[row][column-1] && liveCellArray[row-1][column-1])){
				actualButtonArray[row][column].setBackground(Color.GREEN);
				resetButtonArray[row][column] = actualButtonArray[row][column];
				liveCellArray[row][column] = true;
			}else {
				if((liveCellArray[row][column] && liveCellArray[row-1][column]) && !(liveCellArray[row-1][column-1] && liveCellArray[row][column-1])){
					resetButtonArray[row][column].setBackground(Color.RED);
				}else if((liveCellArray[row][column] && liveCellArray[row-1][column-1]) && !(liveCellArray[row-1][column] && liveCellArray[row][column-1])){
					resetButtonArray[row][column].setBackground(Color.RED);
				}else if((liveCellArray[row][column] && liveCellArray[row][column-1]) && !(liveCellArray[row-1][column] && liveCellArray[row-1][column-1])){
					resetButtonArray[row][column].setBackground(Color.RED);
				}
			}
		}
	}
}
