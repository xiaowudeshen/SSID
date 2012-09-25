/*
This file is part of SSID.

SSID is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

SSID is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with SSID.  If not, see <http://www.gnu.org/licenses/>.
 */

import Tokenizer.*;
import java.util.*;

import pd.utils.*;
import pd.*;

public class Main {

	public static final String BASELINE = "baseline";

	// static final int KGRAM_SIZE = 3;

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) throws Exception {

		long start = System.nanoTime();

		if (args.length != 9) {
			throw new Exception(
					"Usage: jPDS <Assignment Id> <Compare Folder path> <Language> <minMatch> <N-Gram> <database host> <database name> <database username> <database password>");
		}

		String aId = args[0];
		String compareFolderPath = args[1];
		String language = args[2];
		int minMatch = Integer.parseInt(args[3]);
		if (minMatch <= 0) {
			throw new Exception("MinMatch must be > 0");
		}
		int nGramSize = Integer.parseInt(args[4]);
		if (nGramSize <= 0) {
			throw new Exception("N-Gram size must be > 0");
		}
		String dbAddr = args[5];
		String dbName = args[6];
		String dbUser = args[7];
		String dbPwd = args[8];

		MySQLDB.setProperties(dbAddr, dbName, dbUser, dbPwd);

		ArrayList<Submission> submissions = SubmissionRetriever
				.retrieveSubmissions(compareFolderPath);

		if (language.equals(CTokenizer.language)) {
			CTokenizer.getTokenizer().TokenizeSubmissions(submissions);
		} else if (language.equals(JavaTokenizer.language)) {
			JavaTokenizer.getTokenizer().TokenizeSubmissions(submissions);
		} else {
			throw new Exception("Invalid Keyword Selection");
		}

		try {

			NGramizer.getNGramizer().constructSubmissionsNGrams(submissions,
					nGramSize);

			ArrayList<Result> simResults = SimComparer.getComparer()
					.compareSubmissions(submissions, nGramSize, minMatch);

			System.out.println("Checking Duration = "
					+ new java.text.DecimalFormat("0.000").format((System
							.nanoTime() - start) / Math.pow(10, 9)) + "s");

			MySQLDB.getMySQLDB().insertIntoDB(aId, submissions, simResults);

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			for (StackTraceElement f : ex.getStackTrace()) {
				System.out.println(f.toString());
			}
			throw ex;
		}

	}
}