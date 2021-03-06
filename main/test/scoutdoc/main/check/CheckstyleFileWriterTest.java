/*******************************************************************************
 * Copyright (c) 2012 BSI Business Systems Integration AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BSI Business Systems Integration AG - initial API and implementation
 ******************************************************************************/

package scoutdoc.main.check;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import scoutdoc.main.TU;
import scoutdoc.main.structure.PageUtility;

/**
 * Tests for {@link CheckstyleFileWriter}
 */
public class CheckstyleFileWriterTest {
  private static String FILE_1 = "<not initialized 1>";
  private static String FILE_2 = "<not initialized 2>";
  private static final String MESSAGE_1 = "This is the message";
  private static final String MESSAGE_2 = "This is message 2";
  private static final String SOURCE = "xxx";

  private static final String LN = System.getProperty("line.separator");
  private static final String XML_VERSION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
  private static final String CHECKSTYLE_OPEN = "<checkstyle version=\"5.5\">";
  private static final String CHECKSTYLE_CLOSE = "</checkstyle>";
  private static final String FILE_CLOSE = "</file>";

  @BeforeClass
  public static void beforeClass() {
    TU.init();
    FILE_1 = PageUtility.toFile(TU.PAGE_1).getAbsolutePath();
    FILE_2 = PageUtility.toFile(TU.PAGE_2).getAbsolutePath();
  }

  @Test
  public void testWriteEmpty() {
    String expected = XML_VERSION + LN + CHECKSTYLE_OPEN + LN + CHECKSTYLE_CLOSE + LN;
    List<Check> list = Collections.emptyList();

    runAndAssertEquals(expected, list);
  }

  @Test
  public void testWriteOneCheck() {
    String expected = XML_VERSION + LN;
    expected += CHECKSTYLE_OPEN + LN;
    expected += createFile(FILE_1) + LN;
    expected += createError("2", "10", "warning", MESSAGE_1, "") + LN;
    expected += FILE_CLOSE + LN;
    expected += CHECKSTYLE_CLOSE + LN;

    Check check = createCheck1();

    List<Check> list = Collections.singletonList(check);

    runAndAssertEquals(expected, list);
  }

  @Test
  public void testWriteTwoChecks() {
    String expected = XML_VERSION + LN;
    expected += CHECKSTYLE_OPEN + LN;
    expected += createFile(FILE_1) + LN;
    expected += createError("2", "10", "warning", MESSAGE_1, "") + LN;
    expected += createError("20", "5", "warning", MESSAGE_2, SOURCE) + LN;
    expected += FILE_CLOSE + LN;
    expected += CHECKSTYLE_CLOSE + LN;

    List<Check> list = Arrays.asList(createCheck1(), createCheck2());

    runAndAssertEquals(expected, list);
  }

  @Test
  public void testWriteCheckWithTwoFiles() {
    String start = XML_VERSION + LN;
    start += CHECKSTYLE_OPEN + LN;

    String contentFile1 = createFile(FILE_1) + LN;
    contentFile1 += createError("2", "10", "warning", MESSAGE_1, "") + LN;
    contentFile1 += FILE_CLOSE + LN;

    String contentFile2 = createFile(FILE_2) + LN;
    contentFile2 += createError("20", "5", "warning", MESSAGE_2, SOURCE) + LN;
    contentFile2 += FILE_CLOSE + LN;

    String end = FILE_CLOSE + LN;
    end += CHECKSTYLE_CLOSE + LN;

    List<Check> list = Arrays.asList(createCheck1(), createCheck3());

    StringWriter sw = runWrite(list);
    String actual = sw.toString();

    Assert.assertTrue("startsWith " + start, actual.startsWith(start));
    Assert.assertTrue("endsWith " + end, actual.endsWith(end));
    Assert.assertTrue("contains " + contentFile1, actual.contains(contentFile1));
    Assert.assertTrue("contains " + contentFile2, actual.contains(contentFile2));
  }

  private Check createCheck1() {
    Check check = new Check();
    check.setLine(2);
    check.setColumn(10);
    check.setPage(TU.PAGE_1);
    check.setMessage(MESSAGE_1);
    check.setSeverity(Severity.warning);
    return check;
  }

  private Check createCheck2() {
    Check check = new Check();
    check.setLine(20);
    check.setColumn(5);
    check.setPage(TU.PAGE_1);
    check.setMessage(MESSAGE_2);
    check.setSeverity(Severity.warning);
    check.setSource(SOURCE);
    return check;
  }

  private Check createCheck3() {
    Check check = new Check();
    check.setLine(20);
    check.setColumn(5);
    check.setPage(TU.PAGE_2);
    check.setMessage(MESSAGE_2);
    check.setSeverity(Severity.warning);
    check.setSource(SOURCE);
    return check;
  }

  private String createFile(String file) {
    return "<file name=\"" + file + "\">";
  }

  private String createError(String line, String column, String severity, String message, String source) {
    return "<error line=\""
        + line
        + "\" column=\""
        + column
        + "\" severity=\""
        + severity
        + "\" message=\""
        + message
        + "\" source=\""
        + source + "\"/>";
  }

  private StringWriter runWrite(List<Check> list) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    CheckstyleFileWriter.write(list, pw);
    return sw;
  }

  private void runAndAssertEquals(String expected, List<Check> list) {
    StringWriter sw = runWrite(list);
    Assert.assertEquals(expected, sw.toString());
  }

}
