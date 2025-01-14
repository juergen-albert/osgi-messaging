/*******************************************************************************
 * Copyright 2020-2023 Amit Kumar Mondal
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package in.bytehue.messaging.mqtt5.provider.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Table {

	private static final String HORIZONTAL_SEP = "-";

	private String joinSep;
	private String[] headers;
	private String verticalSep;
	private boolean rightAlign;

	private final List<String[]> rows;

	public Table() {
		rows = new ArrayList<>();
		setShowVerticalLines(false);
	}

	public void setRightAlign(final boolean rightAlign) {
		this.rightAlign = rightAlign;
	}

	public void setShowVerticalLines(final boolean showVerticalLines) {
		joinSep = showVerticalLines ? "+" : " ";
		verticalSep = showVerticalLines ? "|" : "";
	}

	public void setHeaders(final String... headers) {
		this.headers = headers;
	}

	public void addRow(final String... cells) {
		rows.add(cells);
	}

	public String print() {
		int[] maxWidths = headers != null ? Arrays.stream(headers).mapToInt(String::length).toArray() : null;

		for (final String[] cells : rows) {
			if (maxWidths == null) {
				maxWidths = new int[cells.length];
			}
			if (cells.length != maxWidths.length) {
				throw new IllegalArgumentException("Number of row-cells and headers should be consistent");
			}
			for (int i = 0; i < cells.length; i++) {
				maxWidths[i] = Math.max(maxWidths[i], Objects.toString(cells[i], "").length());
			}
		}

		final StringBuilder b = new StringBuilder();
		if (headers != null) {
			final StringBuilder b1 = printLine(maxWidths);
			final StringBuilder b2 = printRow(headers, maxWidths);
			final StringBuilder b3 = printLine(maxWidths);

			b.append(b1).append(b2).append(b3);
		}
		for (final String[] cells : rows) {
			final StringBuilder b4 = printRow(cells, maxWidths);
			b.append(b4);
		}
		if (headers != null && maxWidths != null) {
			final StringBuilder b5 = printLine(maxWidths);
			b.append(b5);
		}
		return b.toString();
	}

	private StringBuilder printLine(final int[] columnWidths) {
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i < columnWidths.length; i++) {
			final String line = String.join("",
					Collections.nCopies(columnWidths[i] + verticalSep.length() + 1, HORIZONTAL_SEP));
			builder.append(joinSep + line + (i == columnWidths.length - 1 ? joinSep : ""));
		}
		builder.append(System.lineSeparator());
		return builder;
	}

	private StringBuilder printRow(final String[] cells, final int[] maxWidths) {
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i < cells.length; i++) {
			final String s = cells[i];
			final String verStrTemp = i == cells.length - 1 ? verticalSep : "";
			if (rightAlign) {
				builder.append(String.format("%s %" + maxWidths[i] + "s %s", verticalSep, s, verStrTemp));
			} else {
				builder.append(String.format("%s %-" + maxWidths[i] + "s %s", verticalSep, s, verStrTemp));
			}
		}
		builder.append(System.lineSeparator());
		return builder;
	}

}
