/*******************************************************************************
 * Copyright (c) 2011 - 2012 Adrian Vielsack, Christof Urbaczek, Florian Rosenthal, Michael Hoff, Moritz Lüdecke, Philip Flohr.
 * 
 * This file is part of Sudowars.
 * 
 * Sudowars is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Sudowars is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Sudowars.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 * 
 * Diese Datei ist Teil von Sudowars.
 * 
 * Sudowars ist Freie Software: Sie können es unter den Bedingungen
 * der GNU General Public License, wie von der Free Software Foundation,
 * Version 3 der Lizenz oder (nach Ihrer Option) jeder späteren
 * veröffentlichten Version, weiterverbreiten und/oder modifizieren.
 * 
 * Sudowars wird in der Hoffnung, dass es nützlich sein wird, aber
 * OHNE JEDE GEWÄHELEISTUNG, bereitgestellt; sogar ohne die implizite
 * Gewährleistung der MARKTFÄHIGKEIT oder EIGNUNG FÜR EINEN BESTIMMTEN ZWECK.
 * Siehe die GNU General Public License für weitere Details.
 * 
 * Sie sollten eine Kopie der GNU General Public License zusammen mit diesem
 * Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 * initial API and implementation:
 * Adrian Vielsack
 * Christof Urbaczek
 * Florian Rosenthal
 * Michael Hoff
 * Moritz Lüdecke
 * Philip Flohr 
 ******************************************************************************/
package org.sudowars.Model.SudokuManagement.Pool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;
import java.util.UUID;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import org.sudowars.DebugHelper;
import org.sudowars.DebugHelper.PackageName;
import org.sudowars.Model.Difficulty.Difficulty;
import org.sudowars.Model.Difficulty.DifficultyEasy;
import org.sudowars.Model.Difficulty.DifficultyHard;
import org.sudowars.Model.Difficulty.DifficultyMedium;
import org.sudowars.Model.Sudoku.Sudoku;
import org.sudowars.Model.Sudoku.Field.DataCell;
import org.sudowars.Model.Sudoku.Field.DataCellBuilder;
import org.sudowars.Model.Sudoku.Field.Field;
import org.sudowars.Model.Sudoku.Field.FieldBuilder;
import org.sudowars.Model.Sudoku.Field.FieldStructure;
import org.sudowars.Model.Sudoku.Field.SquareStructure;
import org.sudowars.Model.Sudoku.RuleManagement.DependencyManager;
import org.sudowars.Model.Sudoku.RuleManagement.RuleApplier.StandardRulesetFactory;
import org.sudowars.Model.SudokuManagement.Generator.Generator;
import org.sudowars.Model.SudokuManagement.Generator.GeneratorBase;
import org.sudowars.Model.SudokuManagement.Generator.Transformer;
import org.sudowars.Model.SudokuManagement.Generator.TransformerBase;

/**
 * This class is android specific the implementation of the sudoku pool, which is realized as a bound service.
 */
public class SudokuFilePool extends Service implements SudokuPool {

	private static final long serialVersionUID = -8387498223151949658L;
	private static final String SUDOKU_FILE_NAME = "%s.ser";
	
	private static final int MIN_SUDOKU_COUNT = 5;
	private static final int MAX_SUDOKU_COUNT = 30;
	private static final int WORKER_THREADS_PRIORITY = 1;
	private static final String[] usedDifficulties = new String[] {	new DifficultyEasy().toString(),
																	new DifficultyMedium().toString(),
																	new DifficultyHard().toString() };
	
	private static final SquareStructure[] sizes = new SquareStructure[] {new SquareStructure(9), new SquareStructure(16)};
	
	private final ThreadGroup threadGroup;
	private boolean hasChanged;
	private final File rootDirectory;
	private final File[] dirs;
	private final TransformerBase transformer;
	private final Random randomNumberGenerator;
	private final IBinder binder = new SudokuFilePoolBinder();
	
	private static boolean initializing = false;
	private static PoolInitializer initializer = null;
	//private static Thread initializingThread = null;

	/**
	 * Initializes a new instance of the {@link SudokuFilePool} class.
	 *
	 */
	public SudokuFilePool() {
		this.transformer = new Transformer();
		this.randomNumberGenerator = new Random();
		
		this.threadGroup = new ThreadGroup("Pool generator threads");
		this.threadGroup.setMaxPriority(WORKER_THREADS_PRIORITY);
		
		this.rootDirectory = new File(android.os.Environment.getDataDirectory()  + "/data/org.sudowars/files/sudokus");
		if (!this.rootDirectory.exists()) {
			this.rootDirectory.mkdirs();
		}
		
		this.dirs = new File[usedDifficulties.length * sizes.length];
		initializeDirectoryStructure();
			
		if (this.empty() && !getInitializing()) {
			setInitializing(true);
			DebugHelper.log(DebugHelper.PackageName.SudokuFilePool, "Start intizializing");
			initializer = new PoolInitializer();
			Thread t = new Thread(initializer);
			//initializingThread = t;
			t.setPriority(2);
			t.start();
		}
		
		DebugHelper.log(DebugHelper.PackageName.SudokuFilePool, this.rootDirectory.getAbsolutePath());
	}
	
	private static synchronized void setInitializing(boolean flag) {
		initializing = flag;
	}
	
	private static synchronized boolean getInitializing() {
		return initializing;
	}
	
	private class PoolInitializer implements Runnable {

		boolean milestone = false;
		
		@Override
		public void run() {
			initializeFilePool();
			setInitializing(false);
			DebugHelper.log(DebugHelper.PackageName.SudokuFilePool, "Pool initialized");
		}
		
		public synchronized void reachedMilestone() throws InterruptedException {
			while (!milestone) {
				this.wait();
			}
		}
		
		public synchronized void notifyMilestoneReached() {
			this.milestone = true;
			this.notifyAll();
		}
		
	}

	private void initializeDirectoryStructure() {
		int i = 0;
		for (SquareStructure size : sizes) {
			for (String diff: usedDifficulties) {
				this.dirs[i] = new File(this.rootDirectory,  "/" + size.getWidth() + size.getHeight() + "/" + diff);
				if (!this.dirs[i].exists()) {
					this.dirs[i].mkdirs();
				}
				++i;
			}
		}
	}
	
	/**
	 * Starts the generation of a new sudoku with the given structure and difficulty:
	 * <br>First a solved sudoko is transformed and then passed to the generator, generating a new one.
	 *
	 * @param baseSudoku The {@link Sudoku} to be the base of the one to generate.
	 * @param difficulty The {@link Difficulty} of the sudoku to generate.
	 */
	private void startSudokuGeneration(Sudoku<DataCell> baseSudoku, Difficulty difficulty) {
		assert baseSudoku != null && difficulty != null;
		
		//if necessary more than one generating thread can be running concurrently
		GeneratorBase generator = new Generator(this);
		generator.setTargetSudokuProperties(new DifficultyHard(), baseSudoku.clone());
		//generator.setTargetSudokuProperties(difficulty, baseSudoku.clone());
		DebugHelper.log(DebugHelper.PackageName.SudokuFilePool, "starting generation thread for sudoku with difficulty " + difficulty);
		Thread worker = new Thread(this.threadGroup, generator);
		worker.setPriority(WORKER_THREADS_PRIORITY);
		worker.start();
	}
	
	/**
	 * Indicates whether the pool is empty, i.e. does not contain any sudokus.
	 *
	 * @return {@code true}, if pool is empty, otherwise {@code false}.
	 */
	@Override
	public boolean empty() {
		//return this.rootDirectory.list().length == 0;
		for (File dir : this.dirs) {
			if (dir.listFiles().length > 0) return false;
		}
		return true;
	}
	
	/**
	 * Indicates if the pool has changed, that is
	 * <li>if at least one sudoku has been extracted, or</li>
	 * <li>if at least one sudoku has been added to the pool.</li>
	 *
	 * @return <code>true</code> if pool has changed, otherwise <code>false</code>.
	 */
	@Override
	public boolean hasChanged() {
		return this.hasChanged;
	}

	/**
	 * Extracts an unsolved sudoku with the given structure and difficulty from the pool.
	 *
	 * @param structure The {@link FieldStructure} of the sudoku to extract.
	 * @param difficulty The {@link Difficulty} of the sudoku to extract.
	 *
	 * @return An unsolved {@link Sudoku} with the given structure and difficulty<br>
	 * or <code>null</code> if no sudoku could be found, or an error occurred during extraction.
	 *
	 * @throws IllegalArgumentException if at least one of the given params was <code>null</code> 
	 */
	@Override
	public Sudoku<DataCell> extractSudoku(FieldStructure structure, Difficulty difficulty) throws IllegalArgumentException{
		if (structure == null || difficulty == null) {
			throw new IllegalArgumentException("at least one of the given parameters was null.");
		}
		
		if (getInitializing()) {
			try {
				//initializer.join();
				initializer.reachedMilestone();
			} catch (InterruptedException e) {
				DebugHelper.log(PackageName.SudokuFilePool, "initializing interrupted, trying to continue");
			}
		}
		
		File[] foundSudokus = null;
		
		File sudokuFile = getDirectoryForSudokuType(structure, difficulty);
		if (sudokuFile != null) {
			foundSudokus = sudokuFile.listFiles();
		}
		else {
			foundSudokus = new File[0];
		}
		
		DebugHelper.log(PackageName.SudokuFilePool, "found sudokus: " + foundSudokus.length);
		
		if (foundSudokus.length > 0) {
			Sudoku<DataCell> result = pickSudokuRandomly(foundSudokus);
			if (result != null) {
				this.hasChanged = true;
				if (foundSudokus.length <= MIN_SUDOKU_COUNT && !initializing) startSudokuGeneration(result, difficulty);
			}
			return result;
		} else {
			initializeFilePool();
			return extractSudoku(structure, difficulty);
		}
		
	}
	
	private Sudoku<DataCell> pickSudokuRandomly(File[] foundSudokus) {
		assert foundSudokus != null && foundSudokus.length > 0;
		//pick sudoku randomly
		int index = this.randomNumberGenerator.nextInt(foundSudokus.length);
		Sudoku<DataCell> result;
		try {
			result = loadSudoku(foundSudokus[index]);
			foundSudokus[index].delete();
			this.hasChanged = true;
		} catch (IOException e) {
			result = null;
		}
		return result;
	}
	
	private File getDirectoryForSudokuType(FieldStructure structure, Difficulty diff) {
		assert structure != null && diff != null;
		
		File result = null;
		
		File temp = new File(this.rootDirectory, String.valueOf(structure.getWidth()) + String.valueOf(structure.getHeight()) + "/" + diff.toString());
		for (File dir : this.dirs) {
			if (dir.getAbsolutePath().equals(temp.getAbsolutePath())) {
				result = dir;
				break;
			}
		}
		return result;
	}
	
	private int getSudokuCount(FieldStructure structure, Difficulty diff) {
		assert structure != null && diff != null;
		
		int result = 0;
		File directory = getDirectoryForSudokuType(structure, diff);
		if (directory != null) {
			result = directory.listFiles().length;
		}
		return result;
	}
	
	private static void saveSudoku(File sudokuFile, Sudoku<DataCell> sudoku) throws IOException {
		assert sudokuFile != null && sudokuFile.getName().length() > 0 && !sudokuFile.exists() && sudokuFile.canWrite();
		assert sudoku != null;
		
		FileOutputStream fileOutputStream = null;
		ObjectOutputStream outputStream = null;
		try {
			fileOutputStream = new FileOutputStream(sudokuFile);
			outputStream = new ObjectOutputStream(fileOutputStream);
			outputStream.writeObject(sudoku);
		} catch (IOException e) {
			throw new IOException("Error while saving sudoku: " + e.getMessage());
		}
		finally {
			outputStream.close();
			fileOutputStream.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	private static Sudoku<DataCell> loadSudoku(File sudokuFile) throws IOException {
		assert sudokuFile != null && sudokuFile.length() > 0 && sudokuFile.exists() && sudokuFile.canRead();
		
		Sudoku<DataCell> result = null;
		
		FileInputStream fileInputStream = null;
		ObjectInputStream inputStream = null;
		try {
			fileInputStream = new FileInputStream(sudokuFile);
			inputStream = new ObjectInputStream(fileInputStream);
			result = (Sudoku<DataCell>) inputStream.readObject();
		} catch (IOException e) {
			result = null;
		} catch (ClassNotFoundException ex) {
			result = null;
		} finally {
			inputStream.close();
			fileInputStream.close();
		}
		return result;
	}
	
	private File generateSudokuFile(FieldStructure structure, Difficulty difficulty) {
		assert structure != null && difficulty != null;
		
		UUID id = UUID.randomUUID();
		
		File baseDirectory = getDirectoryForSudokuType(structure, difficulty);
		assert baseDirectory != null;
		
		return new File (baseDirectory, String.format(SUDOKU_FILE_NAME, id.toString()));
	}
	
	/**
	 * Adds a sudoku with a specified difficulty to the pool.
	 * @param sudoku the {@link Sudoku} to add to the pool.
	 * @param difficulty {@link Difficulty} of the sudoku to add
	 * @param transform indicates if given sudoku shall be transformed first
	 * @return {@code true}, if the sudoku was successfully added to the pool, otherwise {@code false}
	 * @throws IllegalArgumentException if at least one of the given params is {@code null}
	 * @see TransformerBase#transformSudoku(Sudoku)
	 */
	public synchronized boolean addSudoku(Sudoku<DataCell> sudoku, Difficulty difficulty, boolean transform) throws IllegalArgumentException {
		if (sudoku == null || difficulty == null) {
			throw new IllegalArgumentException("at least one of the given parameters was null.");
		}
		DebugHelper.log(DebugHelper.PackageName.SudokuFilePool, "trying to save generated sudoku with difficulty " + difficulty);
		boolean result = false;
		if (getSudokuCount(sudoku.getField().getStructure(), difficulty) < MAX_SUDOKU_COUNT) {
			try {
				saveSudoku(generateSudokuFile(sudoku.getField().getStructure(), difficulty), (transform ? this.transformer.transformSudoku(sudoku.clone()) : sudoku));
				this.hasChanged = true;
				result = true;
				DebugHelper.log(DebugHelper.PackageName.SudokuFilePool, "saved generated sudoku with difficulty " + difficulty);
			} catch (IOException e) {
				DebugHelper.log(DebugHelper.PackageName.SudokuFilePool,"error while saving sudoku" + e.getMessage());
			}
		} else {
			DebugHelper.log(DebugHelper.PackageName.SudokuFilePool, "discarding generated sudoku with difficulty " + difficulty);
		}
		return result;
	} 

	
	private static Sudoku<DataCell> decodeSudoku(String sudokuCode, String initialCode) {
		
		assert sudokuCode.length() == initialCode.length();
		assert !sudokuCode.contains(".");
		
		FieldBuilder<DataCell> fb = new FieldBuilder<DataCell>();
		FieldStructure structure = null;
		DependencyManager dependencyManager = null;
		
		switch (sudokuCode.length()) {
			case  81 : 
					structure = new SquareStructure(9); 
					dependencyManager = StandardRulesetFactory.getInstance().build9x9Ruleset();
					break;
			case 256 : 
					structure = new SquareStructure(16);
					dependencyManager = StandardRulesetFactory.getInstance().build16x16Ruleset();
					break;
		}
		
		assert structure != null;
		
		Field<DataCell> field = fb.build(structure, new DataCellBuilder());
				
		for (int i = 0; i < sudokuCode.length(); i++) {
			
			Character c = sudokuCode.charAt(i);
					
			int value;
			switch (c) {
				/*case '1' : value =  1; break;
				case '2' : value =  2; break;
				case '3' : value =  3; break;
				case '4' : value =  4; break;
				case '5' : value =  5; break;
				case '6' : value =  6; break;
				case '7' : value =  7; break;
				case '8' : value =  8; break;
				case '9' : value =  9; break;*/
				case '0' : value = 10; break;
				case 'a' : value = 11; break;
				case 'b' : value = 12; break;
				case 'c' : value = 13; break;
				case 'd' : value = 14; break;
				case 'e' : value = 15; break;
				case 'f' : value = 16; break;
				default:
					value = Integer.parseInt(c.toString());
			}
			if (initialCode.charAt(i) == '.') {
				field.getCell(i).setInitial(false);
			} else {
				field.getCell(i).setInitial(true);
			}
			field.getCell(i).setValue(value);
			
		}
		Sudoku<DataCell> sudoku = new Sudoku<DataCell>(field, dependencyManager);
		
		return sudoku;
		
	}
	

	private void initializeFilePool() {
		Difficulty hdiff = new DifficultyHard();
		Difficulty mdiff = new DifficultyMedium();
		Difficulty ediff = new DifficultyEasy();
		
		for (int i = 0; i < 10; i++) {
			String[] split = null;
			if (initial9x9Hard.length > i) {
				split = initial9x9Hard[i].split(" ");
				addSudoku(decodeSudoku(split[0], split[1]), hdiff, true);
			}
			
			if (initial9x9Medium.length > i) {
				split = initial9x9Medium[i].split(" ");
				addSudoku(decodeSudoku(split[0], split[1]), mdiff, true);
			}
			
			if (initial9x9Easy.length > i) {
				split = initial9x9Easy[i].split(" ");
				addSudoku(decodeSudoku(split[0], split[1]), ediff, true);
			}
			
			if (initial16x16Hard.length > i) {
				split = initial16x16Hard[i].split(" ");
				addSudoku(decodeSudoku(split[0], split[1]), hdiff, true);
			}
			
			if (initial16x16Medium.length > i) {
				split = initial16x16Medium[i].split(" ");
				addSudoku(decodeSudoku(split[0], split[1]), mdiff, true);
			}
			
			if (initial16x16Easy.length > i) {
				split = initial16x16Easy[i].split(" ");
				addSudoku(decodeSudoku(split[0], split[1]), ediff, true);
			}

			if (i == 1) {
				initializer.notifyMilestoneReached();
			}
		}
	}
	
	private static String[] initial9x9Hard = new String[] {
				"496512783382796415517348269174639852839275146265184397653421978748963521921857634 49......33...96.........2...7.63..5......51...6..8..9.6..4.19.87....3521.2.8..6..",
				"981637452627594831435281769268459317573126948194873625359762184742318596816945273 ..1.3......7.94.31.....1.6.2..4.9...5..1...48.....36.5.....2...7.2....96.1.94.2.3",
				"418276593327594168659831427894612735731945682562387941183459276976128354245763819 4.8..6.9.3.7..41.86.98....78...1...57...456....23.....1............2.3....5.63.1.",
				"861495723724138956395672814537216498286549137149783265672954381918367542453821679 ...4.....7.....9.6.....28.....21.....865..1..1....3.6.672.54..1.......42..38.....",
				"245763981976128435183459627731945268562387194894612573659831742418276359327594816 .4...3.819..1...35..3..9....3..4.2..56.3....48.4.........8.......8.7635.32...48..",
				"563241987291587643478693512625814379389725164714369825946152738157438296832976451 ...24...7....8.643..8......6....43..3....516.7..3.9..5...1.....1.7.3.29...2.7....",
				"381495726679182534542736189498621375137954862265378491723549618956813247814267953 ...4....667..8..3....7...8....62..7....9.4.....5.....1723......9..8...4.81.2..953",
				"138459276967128354254763819526387941713945682849612735481276593372594168695831427 1.....27....1....4.....3.1.52.3..94..1.9...82..96.....4.1.7...3.7.5.4.6.69..3....",
				"192785643874396512365142987983527164526418379417963825751834296238679451649251738 .9278....8.....5......429...83.....4.....8...4..96..2...183.....38.7..5.64..5..3.",
				"364785912251396784798142635416527893937418256582963147145679328629834571873251469 .6..85.1..5....7....8.4.6..4165.7........8..6.8.9...4..45.7...8..9.3.5...7.2..46."
		};
	
	private static String[] initial9x9Medium = new String[] {
				"245763891183459267976128345894612753562387914731945628327594186659831472418276539 .4....8..1..4.92.79.6.......9....7.3..23...1..3..45.......9...665...1.7.4..2.....",
				"364857912798421635251963784582639147937184256416275893873512469629348571145796328 3..8..91.....2.6..25...3.84...63.1..9.71.....4.6..5..3...5.246.62.3.8..1..5.....8",
				"926438751378152649541976238739814526614725983285369417897241365152693874463587192 .2.4....137.1....95.1..623.....1.5.66.4..59..2..36..1...724.......6..8.4......19.",
				"274138965935672841681495732419783256357216489826549173762954318543821697198367524 ..4.38.6.9...72.41.81..57..4197..2.6.5..1...9...5......6...4...5.38...9...8......",
				"724318956395762814861945723286459137537126498149873265672594381453281679918637542 7.4.189.6...7..8.......5...2..4....7.3..2....1.9..32..6...94.81..328.6....8.3....",
				"873521649629384751145769238582693417416257983937148526364875192251936874798412365 .7.....4.6..3.47....5.6...8.82.9...7.1..5.9....71...26..48.51..2...3.874.98..23..",
				"738251649296834751451679238379418526164527983825963417643785192512396874987142365 .382........8....145..........41.5.61...2...3..59.34.7..378..9.512..6.749...42.6.",
				"837125649692483751154967238973841526528396417461752983346578192215639874789214365 83.............7..15..6..3.9.3.4......8..64.7....52.....657.19.2.5..9.......14.65",
				"861459723395627814724183956286594137537261498149738265453812679672945381918376542 ....59.....5.2...4...183...2...941.7...26..9.1.......5.53..2.79.......8...83.6...",
				"543281679198637542762594381419873265357126498826459137935762814274318956681945723 .4....67..9..375..76...4...4.........5...6.9.8.6...1.7.....2..4274.18.56..1.45..."
		};
	
	private static String[] initial9x9Easy = new String[] {
				"659138472418672539327495186562783914731549628894216753183954267976821345245367891 .5..3.4..418.7........95.86.6.783.14.31....2..9.2...5318.9.....9...21..524.3.....",
				"291875463478936152563412897625148739714693285389257614832769541157384926946521378 .9.8....3..8....52..3..2.9.6.51..7...14..3...3.925.6...3.7.9.4.1..384..69.652....",
				"543128976762459183198763245826945731357612894419387562935276418681594327274831659 ..312.9..7....91..1....324.82694.7...5...2.944...8.......2.6.1.6815...2.....31.5.",
				"157834692832679154946251837389527461625418973714963528563142789478396215291785346 1.....6.2.3.6...5..4.2518.......7..162.4...737.49.352.....427....83..2...9.7.5.4.",
				"418726593659381427327954168562837941894162735731495682183549276976218354245673819 4..7.6.93.5..81....279..1..562.37.........7.....4.5.8.1.3.492..97..1..54....7.81.",
				"918376524672945318453812697286594173537261489149738256861459732724183965395627841 ..83.652.6..945.1......26.7..65......37...48914..3..56......7..7..183..5.9.6....1",
				"514697238387215649962843751641572983793481526258936417879124365125369874436758192 51...72..3.72....9......75.....729.3.9.48..26..8.36.1..7....3.5...36.8....6..8192",
				"524763819697128354318459276489612735256387941173945682965831427841276593732594168 5...6.8196.7.2.354.18.5...6...61.7.5.....794.1..9..6..9.58......41.7.....3....1.8",
				"618495273953672184247138596491783625375216948862549317726954831534821769189367452 .1.....739....2....47....964...8...5..5.169.886..4..1.726.54..1.3.8.1..9....67..2",
				"453821796672954813918367425537216984286549371149783652861495237395672148724138569 ..38...9.6.29.48......6.4..5.7.1.9.4.8.5......49.83.5286..95..73.56.2....24......"
		};
	
	private static String[] initial16x16Hard = new String[] {
				"edc46f3519b27a08862f719e03a45dcbb09ac2d4e785316f3715b0a8c6dfe492f301dc2abe684759547b360f912ace8dc2a89e57340dfb16d9e6148bfc57203a0152a94d7fce86b37c695312804bdfae4efd08b62a31957cab83f7ecd596124028304d615bf9ace71f4e2ac96870b3d56ab785f34dec092195dceb70a21368f4 .d...f3519..7..8.62.7..e03.....b...ac....785316..7.5.0........9....1dc.a.e....5...7...0.9.2a.e..c2..9..7...df....9.614...c5.2....1..a94d...e86b...6..3.2.0.b.fa.4efd......3..5.c....f7.cd59..2..28.0.d61.bf...e.1...2.c....0b3......8.....ec..2195dc..7.a2...8..",
				"87a0b92156f3c4de2e49d6fc8b0a1573f316875e4c2d9a0bb5dca340e7192f68dce821a9f3607b45a2035c7fb148e69d94756e8badc2013f6fb104d379e5a82c386bcfe7da9452100124956dcf7e83bac9573a12608bfde4edfa40b8253169c77acefb9514d630825b3d780692ac4ef11092edc4385fb7a6468f123a0eb7dc59 ....b..1.......e..4...fc8b0....3.316875..c.d9a.b.5d..3.0e71..f...ce8...9f.6.7..5..0.5...b...e....47.6e.b......3f........7...a82c.8.b.f.7.a..521.0...95..cf..8...c..7.a...0.bfd.4.d.a4...2.31.......ef.9..4..3....b3...0..2....f1......c..8.fb7a64...12.a0e...c.9",
				"e83b5fdca07461294f5ab168329d7e0cd6704392b1ceaf85c21970ea856fb43d67bc3d19fa50824ef1026c4e79385adb39ad0285e41bfc675e84fab7cd26391010ce9bf3d782465abad826509c43e7f193671ec45f0ad8b224f5a87d6eb1c09375e3c42f18d90ba60c9685314ba72def8b2fd9a603ec1574ad41e70b26f593c8 e8..5fd.a0.46.......b1..32.d.e....70.392b.ce.f.5c....0e...6....d.....d...a...2..f.0.6..e7.3....b39.d0...e.1.f.6...84f.b......9.0..ce9.f.d782....b.......9c43e...93.71..45f...8...4..a....eb...9..5e...2..8..0.a6.c..8.314b.7...f.b2..9.603........4.e7.b...593..",
				"961207a4c5fdeb835af81cbe2439d067db43568fa70ec921c7e0293d8b164af5e824a5f093d16c7bb5ad9378e6c4f2107fc641eb50283d9a0391d2c67fab54e8a46578d239bf1e0c2d8bf05a41ec97361e7fc4930265b8ad3c09eb61da87254f893c6f25be70a1d4f2deba47185306c960ba8d19fc42735e41573e0c6d9a8fb2 .612.....5f.eb..5a...cb...39.0..d.4.......0ec.21c7e.29..8.1..a..e.2.a....3.1.c..b.a...78e...f210.f..41.b50....9.0..1d......b5..8..6.78...9..1..c2d8....a....97..1.7f.493.26.b8a....9eb.......5..89..6.25.e..a..4.2deba.7..5.0...6...8d.9.c.2.3....5..e0c..9..f..",
				"bf8269ad0ec37514537ef42c1d98ab06c60915384a7bed2fd1a4b70e2f56c398452fd87a6b1e90c3a8bd0652943cf7e137964ec150afb8d20e1c3bf9d827564ae4587abfc26d19309d3a5280e1b46cf712f0ec467389da5b7c6b9d13f50a428e29c1a0e786f534bd60d72394bce18fa5fa45816b39d20e7c8be3cfd5a7402169 b.8.6.ad0.c..51.537.f4...d.8...6.60..53....b.d2.d1a4..0..f...39..52fd...6b.e90c.a..d.6..943.f7.....64e.1.0a...d..e...b..d82.....e4.8.a.fc2...93.9.....8.e...6..7.2.....67.8...5.7..b9...f.0.428...c.....8.f5..bd...7...4....8..5.a...16...d....c...3..d..7.02..9",
				"78a029b13f56cd4e5bdc43a091e726f8e249f6dca08b17533f16578ed24c90abcde8a12906f374b52a037c5f84b1e96d49758e6b2cad031ff6b1d4035e79a28c836befc749da51201024659de7cf8b3adefab04813256c979c571a32b860fed40192cde4f538ba76b53d0876ca924fe1a7ce9bf56d143802648f321a7b0ed5c9 .8a..9b1....cd.e.bd...a.91e7..f.e249.......b..5.....5...d.4.9..b.de.a...0..3.4b.2a...c.f84.1e....97.8...2...........d4.3...9a.8....be.c.49d....01.24....e7cf..3ad.fab..8132.6c....5....2.8...e....92.de.f.....76b..d0.76...24fe1.7..9...6..4..026....21..b0.d..9",
				"753e4f2c98d1ab068bf296adc3e07514ad147b0e56f2c3980c6951387ba4ed2f245f8d7a1eb690c310ecb3f9278d564a9376e4c1af05b8d2ba8d60523c49f7e139da2580b41e6cf7f120ce468937da5b5e48a7bf6d2c193067cbd9130a5f428ec2910ae7f56834bdd6073294e1cb8fa54fa5186bd2930e7ce8b3fcd5407a2169 7.......98..ab.6..f.96..c.e0..14..1.7b.e....c.9.0.6.5.38.....d...45f..7.1.b.....1...b..9.7.....a9.76e.c1af0.b..2ba.d60.23..9f.e1..da....b.1..c..f1.0..4.89.7..5b.e.8a...6.2....067.b...30.....8.c.9....7f.............9.e.cb8f.5..a....bd.93.e.ce.b3.c....7.216.",
				"5b3d67809ca24ef110924edc3f58b7a6468fa12307bedc597ace5fb916d43082c95723a16b80fde4386b7cfed49a5210edfa840b213569c70124d956ce7f83ba6fb1304d75e9a82ca203f5c7b841e69d9475b6e8a2cd013fdce8921af0637b452e49cd6f8a0b1573f316e8754d2c9a0bb5dc0a34e9172f6887a01b9253f6c4de .b.d67809ca2.ef110924ed..f58..a64.8.a1...7.....9....5........0.....7.3.1...0..e....b7c.e..9..21..d..8..b213.6.c...24.9.6c.7f..b..f.1304d..e9..2..203..c7b..1e.9d.4.5....a..d....dce.9...f06.7....e49c.6...0b..7...1..8..4.2c..0b.5...a..e9..2..88.....9.5..6c4..",
				"e4dcf536b1928a708f621e97a034bd5cba0924dc8e75f136357108abdc6f24e95b476f03291adec8f130ca2d6be89745d69e4b815fc7a023c82ae759034d6bf102159d4ac7fe368ba3b87cef9d5602144def86b032a1c59779c63215480befda2083d164f5b97cae1ef4a9c2768053bd67ab53f8e4dc19029c5db07e1a23486f ..d..5.6..928a......1.97...4..5.ba....d...75f..63....8a.dc6f2.e.5.47.f032.1.......30ca..6....7..d6..4..15.c......8.ae7.....d.bf..2..9d4..7fe36.ba..8.....d..021...ef.6..32....9..9.....54..b.......3..64.5.....e.e....c.7680..bd6..b.3.8e....9.29....07e.a2..8..",
				"a7802b91f536ce4dd5bc4a301e9728f64e29fd6c08ab135713f6587e24dc9ba0ecd8a2196f0375b4749586ebca2d0f1302a375cf4b81ed69bf61d043e759ac82683becf79d4a502159c713a286b0f4de2104695d7cef8a3bfdeab4083215679cca7e9fb5d16432083b5d0786a9c241ef9012ced453f8b67a864f312ab07ed9c5 a..02.91.....e.d.5..4..0.e...8f..e.9fd...8..1.571...5....4dc9b.....8......0.75b4....8.e......f13..a.7.cf.b..e..9b.61...3...9a...683b....9d..5.2159.713.2.6b.f.....0.69......8a.bfdea..0.3...6...ca7e...5d.6.3..8.....7..a9c..1.f....c.d453.8..7a..4..12a.07..9.."
		};
	
	private static String[] initial16x16Medium = new String[] {
				"24e9f6dca80b7153bd5c43a09e1762f8f136578ed42c09ab8a7029b135f6dc4edec8a1290f6347b597458e6b2acd301fa0237c5f8b419e6d6bf1d40357e92a8c368befc74d9a1520c5971a32b680efd4efdab0481235c6970214659dec7fb83a7cae9bf561d4830253bd0876c9a2f4e11902cde4f358ab76486f321a70be5dc9 .4..f.d......15...5....09..76..8..3.57....2c....8....9b..5f6d.4..e.8..29.f.347.5...58e6........fa0.3..5f...1.e6d..f1d.0.5......c.68b..c7.d9a1.20....1a..b..0...4..d.b0..1235c697..1...9d.c.....a7.a.9b.56.d4.3025.b......9a..4.......de..358.b7648.f32..7......9",
				"486fa21307bec5d97cae5bf916d4083219024dec3f587ab653bd68709ca2ef41368b7fced49a2150efda804b21359c670214d596ce7f3b8ac5972a316b80def46bf1340d75e982aca023fc57b84169ed9745be68a2cd130fdec8912af063b47524e9c6df8a0b5713f136e7854d2ca09bbd5c03a4e917f6288a7019b253f64dce 486.a...07....d97.ae..f.....0..2.90.4dec3f587ab......87......f4.368b.fce.4.a.....fd....b2..59.6...14.59.c...3.8.c.972a31..8.de.4..f.3.0..5.....ca.2.fc5..8..6..d97....68.2..1.0.dec8.12a.06.b.....e.c6df8.0.5....13...8..d.c.09...5.03....1...2.....19b.5..64...",
				"4edc6f5319b20a78f86271e903a4cd5b5371b08ac6df94e2ab09c24de785613fb54736f0912a8ecd1f30dca2be6857496d9e14b8fc57302a8c2a9e75340d1bf62015a9d47fceb683d4ef086b2a31759c97c65321804bafde3ab8f7ced596421002834d165bf9eca7e1f42a9c6870d3b576ab853f4dec2901c95deb07a213f864 4e..6f....b20a7.......e..3..cd5.53...08ac6.f94e...09..4d..8.61....4.36...1.a..c.1f30d...be.857..6d....b8..57...a8c2.9...3.0.1b.62.1.a9..7fc.b68.d4....6b...17.9....653.1..4....e3....7c....6..1..2..4d16...9..a..1.42a.c.870..b.76....3.......0...5.e.0..21..86.",
				"41573c0e69ad2bf860ba891df42ce537893c652fb70e4d1af2deb74a15389c602d8bfa504ec163791e7fc3940652da8b3c09e16bd87af452a46572d83bf9c0e1e824a0f59d13b7c67fc64be15280a9d3b5ad9873ec46012f0391d6c27abf8e45db435f86a0e7129c5af81ebc2394760dc7e02d39816b5fa4961204a7cfd538be ..5.3c0e..a...f860ba..........3....c6.2..7.e.d...2de..4a1..8..6.2d.b.a.0.ec163.9..7..3.4065.da8...0.e..b.8..f....4...2.83...c.e1..2....5.d13.7....c64..1...0.9...5.d9.7.e...0....3.1....7a.f....d.4....6a0.71.9c5.f..e...3..7.0.c7..2..9..6b5.a49........fd53.b.",
				"e8b35fdc0a7421964fa5b168239d0ec7c29170ea586f34dbd60743921bce8f5a67cb3d19af5042e839da02854e1b6c7ff1206c4e9738dab55e48fab7dc261903245fa87de6b1903c93761ec4f50ab82d10ec9bf37d8256a4ba8d2650c943f71e0c698531b4a7edf28bf2d9a630ec7541753ec42f81d9ab60ad14e70b62f5c389 ..b35f....7.2..6..a5...8...d0e....9170ea..6.......0...9..bc.8f..67c..d19...0.2...9da...5.e1..c..f12.6.....3...b.5.....b.dc....03245..8..e6b1903c93..1ec.f..ab82.1.e...f.7..256..b....6.0c..3...e0..98.31b.a7ed..8.....a6.0..75...5.ec.2....9ab..a.1.e......5..89",
				"8b2f9a6d03ec715475e342fc18d9a0b6ad4170be26f5c9380c9653184ba7e2df24f587da6eb19c03bad865029c43fe719367ec415f0abd8210cebf39d782546a5e84ab7fcd26139067bcd193fa50482ef102c4e67938d5ab39ad2850e41b6fc7c2190ea7856f3b4dd6703924b1ce8af54f5a168b329d07ece83bfdc5a0742619 8b..9.6.0..c715....3.......9...6a........6.5c9...c..5....ba.e2d.24...7.a..b19...b.d865.29c43fe719367e.41.f0....21.c.b....7..546a..8..b7f.d...390.7b.d19.f...4...f10.c..67......b3..d2.5..4.b.f.7..1..ea7..6.....d..0.92..1c..af5.f5a168.3..d...c.8..fd.5....2.19",
				"96124a70c5dfb38edb43f865a7e0912cc7e0d3928b61a5f45af8ebc12493076de8240f5a931dcb76b5ad8739e64c201f7fc6be145082da9303916c2d7fba48e53c0916beda785f42a4652d8739fbec011e7f394c02568dab2d8ba50f41ce7639f2de74ab183569c0893c52f6be0714da60ba91d8fc243e574157c0e36da9f2b8 96.24.7.c5.f..8...4..8..a7...12.c7e0d.92...1a.f...f8..c1....076.e....f.....d.b76..ad8...e64c2...7...b..45082....0.9.6...7...4..53c0.16b....8...2a.6.....39...c.1...f3..c0.5.8...2d8...0f41.e76.9..........3......9....f..e.714..6...9.d..........15..0.36.a.f...",
				"e83b5fdc7a041269c21970ea685f43bdd6704392cb1ef8a54f5ab168932de07c67bc3d195fa0248ef1026c4e3798ad5b39ad02851e4bc6f75e84fab72cd6913010ce9bf38d72654a93671ec405fa8bd2bad8265049c37fe124f5a87db6e109c38b2fd9a6e03c5714ad41e70bf2653c9875e3c42fd189ba060c968531a4b7de2f .83b...c7a.4...9......e....f..bd.6..4.9.c.1.f.a....ab.68932d...c67...d1.5..02.8e..02...e...8.d.b3.ad0..51..b.6...e.4.a.........0..c...f38..2654.9.........f...d....82650..c3..e.24f...7d.6.109c.8....9.6.0.c5.1.ad.1e7..f.6.3c98......2..1.....60..6...1a....e.."	
			};
	
	private static String[] initial16x16Easy = new String[] {
				"ed4c563f192b70a8b0a94cd2e758361f37518ba0c6fde94286f2e791034a5cdb54b7f30691a2c8edf310ad2cbe864579d96eb184fc75230ac28a795e34d0f1b60125da497fec8b63ab38cfe7d56914207c96251380b4dafe4edf60b82a13975c2803146d5b9faec71fe492ca6807bd356a7b38f54dce029195cd0e7ba2316f84 ed...63.1.2b.0...0...cd.....36..3.518ba0..fd.94....2e.9.0...5c.b...7...691a2..e...........8..5.9....b1.4.c75230....a795e3....1b..125d...7.e.8b63ab3.cfe7d5..1.2...9.25.380.4...e...f60b....397..280.1.6.5.9..ec..fe492.a680..d356....8f.4d..........0.7ba231.f8.",
				"893c5f260be741da4157ce03a6d92fb860ba9d182fc4e357f2de7a4b318596c03c091b6e7da8f5421e7f349c5026d8ab2d8ba05fc41e6739a46528d7f39bce01039162cdb7fa84e57fc6b1e48502ad93e82405fa193dbc76b5ad83794e6c021fdb43f685ea70192cc7e0d93268b15af45af8ecb19243706d961247a0dc5f3b8e .9..5f260b.7.1d.4.57.e..a6d.2.....b..d18....e.5.f2.e7..b3..5..c..c0..........5421.7...9c.026d.a..d8..0.f..1..7.9.4.528.7..9.c..1039.62c.b7f.8.e..f..b.e....2ad9.e.2.05..1..db.7..5.d83794e6c0.1fd.4.....e...192c.7.0d.326.b.....5a.8e..1....7..d9612......5f3b..",
				"8bf2d9a630ce7514753ec42f819dab06ad14e70b625fc3980c698531b47aed2f10ec9bf37d28564a93761ec4f5a0b8d2245fa87de61b90c3ba8d2650c934f7e15e48fab7dc621930f1206c4e9783da5b39da02854eb16cf767cb3d19af05428ed60743921bec8fa5c29170ea58f634bd4fa5b16823d90e7ce8b35fdc0a472169 ...2.9a.3.c.7...753e..2f.19..b.6.d1.e70b62....9.0..9....b...ed2.1.e.9.f3..28..4a..761.c.f5a0b.......a8.de6.b90c3b.8d2.5.....f..15...f...dc...930.12.6c.e.7..da...9d..2.54e.16c.7.7c.3d1...0.4...d6.74.92.b....a5..9.70.a.8f.3....fa.b1....d..e...8.35...0.47.169",
				"9cd5e7b0a2136f8467ba8f534dec0291203846d15bf9aec71e4f2ca96870bd354dfe0b862a31975c796c5132804bdafe0251a49d7fce8b63a38bfe7cd59614205b74306f912ac8edf103d2cabe684579c8a295e7340df1b6d6e9184bfc57230a3517ba08c6dfe9428f26791e03a45cdbba90cd24e785361fe4cd63f519b270a8 .c...7b..21.6.8...ba8....d..0.91...8.6..5b.9a.c71.4f2ca9..70...5..fe....2a31975....c..3.80..daf..251a..d7fc..b..a38.fe7cd...14.05b7..0...1.ac8e.f.0.....b..8.57...a.95e734..f....6e.184b.c5.2...3....a0...dfe.4.8...........5c.bba9...24e.85.6...4c.6.f519.2..a.",
				"4715ce03a69d2fb86a0b9d182f4ce3578c935f260b7e41dafe2d7a4b315896c01fe7349c5062d8ab39c01b6e7d8af542a54628d7f3b9ce012bd8a05fc4e16739bd5a83794ec6021f76fcb1e48520ad93e48205fa19d3bc76013962cdb7af84e5d3b4f685ea07192cc07ed932681b5af458afecb19234706d926147a0dcf53b8e ..15ce..a....f.8.....d1....c....8.9.5f2..b.....a.e..7....15..6c01fe734.c5062d8.b..c01b6e7d..f5....46.8d7f..9.e0.2.d8...fc4e..7.9.d5a83794ec.0.1f.6...1e4.520ad9.e4.2...a....b..6..3.62cd.7af8.e5...4f......71..cc07ed9..6..b5a..5....cb...3.......6..7..dc......",
				"5af8bec12934706dc7e03d92861b5af4db438f65ae07192c9612a470cdf53b8ee824f05a91d3bc767fc6eb145820ad93b5ad7839e4c6021f0391c62d7baf84e51e7f934c0562d8ab2d8b5a0f4ce16739a465d2873fb9ce013c0961bed78af542893c25f6b07e41da60ba19d8f24ce357f2de47ab135896c041570ce36a9d2fb8 ..f..ec.293470..c..0...2......f4db43.....e..192..6.2.4...d...b..e824.0.a9.....7.7fc6.b145.2..d....ad783.e.......0....6....a..4.51.7f.3....62.8.b2..b..0f..e16739.4.5d.8...b..e01.c...1b.d78.f54..9.c25f6b07.4.da60b.1.d.f.4..3.7f2d..7.b13.8...0..57.ce3.a.d..b.",
				"921647a05dfc38ebc0e7d932b6185f4a58faecb1493276d0d34bf6857e0a12c9e42805fa31d9b76cbda5837964ce01f276cfb1e40825a93d019362cdfba78e542b8da05f1ce463971f7e349c2560dab8a56428d79fb3c01e390c1b6ea78df425fed27a4b83519c066ab09d18c24fe5738c395f26e07b4da14751ce03da962b8f 9.164.a05..c...b..e..9.......f..58f.e.b....2.....34bf...7....2.9..2805f..1..b.6...a.8.....ce........b1.4.825a9.d.193.2...ba.8e5.2.8d.0...c...39...7e.49.25.0da.8a.6.2.d.9fb3c..e390c1b..a7....25fe.2.a..8.51.c.66.b09d18...fe5738c.9...6...b...1..51c..3d.96.b8f",
				"a1d4e70b62f5c389735ec42f81d9ab6006c98531b4a7edf28fb2d9a630ec754197361ec4f50ab82db8ad2650c943f71e254fa87de6b1903c1e0c9bf37d8256a46c7b3d19af5042e83d9a02854e1b6c7ff2106c4e9738dab554e8fab7dc261903c92170ea586f34db4af5b168239d0ec7d06743921bce8f5aeb835fdc0a742196 ..d.e.0b6..5c38.7....4.f81d9ab...6c.....b4.7...28.b..9a630e..5...7.61...f5.a...db.a.2...c94.f7....4f.87..6.190.c.e0..bf......6a..c.b.d.9af...2...d9a.2..4.1.6c..f.1..c...7.8.a.554.8..b..c2.1...c..1...a.86f3.db4a.5b..82.9d0ec7d.6.4....bc.8.5ae...5fdc0..4..9."
			};
	
	
	/**
	 * Return the communication channel to the service. 
	 * May return null if clients can not bind to the service. 
	 * The returned IBinder is usually for a complex interface that has been described using aidl. 
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return this.binder;
	}

	/**
	 * Class for a remotable object, the core part of a lightweight remote procedure call mechanism defined by IBinder. 
	 */
	public class SudokuFilePoolBinder extends Binder {
		
		private SudokuFilePool pool = null;
		
		/**
		 * Gets the service provided by this instance.
		 * @return Reference to a {@link SudokuFilePool} which is used a a service.
		 */
		public synchronized SudokuFilePool getService() {
			if (this.pool == null) {
				this.pool = new SudokuFilePool();
			}
			return this.pool;
		}
	}
}


