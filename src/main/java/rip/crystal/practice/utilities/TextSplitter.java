package rip.crystal.practice.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextSplitter {

	public static List<String> split(int length, List<String> lines, String linePrefix, String wordSuffix) {
		StringBuilder builder = new StringBuilder();

		for (String line : lines) {
			builder.append(line.trim());
			builder.append(" ");
		}

		return split(length, builder.substring(0, builder.length() - 1), linePrefix, wordSuffix);
	}

	public static List<String> split(int length, String text, String linePrefix, String wordSuffix) {
		if (text.length() <= length) {
			return Collections.singletonList(linePrefix + text);
		}

		List<String> lines = new ArrayList<>();
		String[] split = text.split(" ");
		StringBuilder builder = new StringBuilder(linePrefix);

		for (int i = 0; i < split.length; ++i) {
			if (builder.length() + split[i].length() >= length) {
				lines.add(builder.toString());
				builder = new StringBuilder(linePrefix);
			}

			builder.append(split[i]);
			builder.append(wordSuffix);

			if (i == split.length - 1) {
				builder.replace(builder.length() - wordSuffix.length(), builder.length(), "");
			}
		}

		if (builder.length() != 0) {
			lines.add(builder.toString());
		}

		return lines;
	}

}
