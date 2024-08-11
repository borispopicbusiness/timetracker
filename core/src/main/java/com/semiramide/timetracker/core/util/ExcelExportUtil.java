package com.semiramide.timetracker.core.util;

import java.io.*;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelExportUtil {

  public static Map<Integer, List<String>> readExcel(File file) throws IOException {

    Map<Integer, List<String>> data = new HashMap<>();

    Workbook workbook = new XSSFWorkbook(file.getAbsolutePath());
    Sheet sheet = workbook.getSheetAt(0);
    int i = 0;
    for (Row row : sheet) {
      data.put(i, new ArrayList<>());
      for (Cell cell : row) {
        switch (cell.getCellType()) {
          case STRING:
            data.get(i).add(cell.getRichStringCellValue().getString());
            break;
          case NUMERIC:
            if (DateUtil.isCellDateFormatted(cell)) {
              data.get(i)
                  .add(
                      cell.getDateCellValue()
                              .toInstant()
                              .atZone(ZoneId.systemDefault())
                              .toLocalDate()
                          + "");
            } else {
              data.get(i).add((int) cell.getNumericCellValue() + "");
            }
            break;
          case BOOLEAN:
            data.get(i).add(cell.getBooleanCellValue() + "");
            break;
          case FORMULA:
            data.get(i).add(cell.getCellFormula() + "");
            break;
          default:
            data.get(i).add(" ");
        }
      }
      i++;
    }
    workbook.close();
    return data;
  }

  public static <T> File writeExcel(List<T> rowDTOList) throws IOException, IllegalAccessException {

    XSSFWorkbook workbook = new XSSFWorkbook();

    Sheet sheet = workbook.createSheet("Worklogs");

    Class<?> entityClass = rowDTOList.get(0).getClass();

    int columnCount = entityClass.getDeclaredFields().length;

    for (int i = 0; i < columnCount; i++) {
      sheet.setColumnWidth(i, 8000);
    }

    Row header = sheet.createRow(0);

    CellStyle headerStyle = workbook.createCellStyle();

    headerStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    XSSFFont font = workbook.createFont();
    font.setFontName("Arial");
    font.setFontHeightInPoints((short) 16);
    font.setBold(true);
    font.setColor(IndexedColors.WHITE.getIndex());
    headerStyle.setFont(font);
    headerStyle.setAlignment(HorizontalAlignment.RIGHT);

    int i = 0;
    while (i < columnCount) {
      String columnName = entityClass.getDeclaredFields()[i].getName();
      Cell headerCell = header.createCell(i);
      headerCell.setCellValue(columnName);
      headerCell.setCellStyle(headerStyle);
      i++;
    }

    Object[] objects = new Object[rowDTOList.size()];
    rowDTOList.toArray(objects);

    int rowCount = 0;
    while (rowCount < rowDTOList.size()) {
      Row row = sheet.createRow(rowCount + 1);

      i = 0;
      while (i < columnCount) {
        Cell cell = row.createCell(i);
        CellStyle style = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        Field[] fields = entityClass.getDeclaredFields();
        fields[i].setAccessible(true);
        Object value = fields[i].get(objects[rowCount]);
        Class<?> type = fields[i].getType();
        switch (type.getSimpleName()) {
          case "String":
            style.setWrapText(true);
            cell.setCellValue((String) value);
            break;
          case "LocalDate":
            style.setDataFormat(createHelper.createDataFormat().getFormat("dd.mm.yyyy"));
            cell.setCellValue((LocalDate) value);
            break;
          case "double":
            DecimalFormat df = new DecimalFormat("#.00");
            cell.setCellValue(df.format(value));
            break;
          case "Intiger":
            cell.setCellValue((Integer) value);
            break;
          case "int":
            cell.setCellValue((int) value);
            break;
          default:
            break;
        }
        style.setAlignment(HorizontalAlignment.RIGHT);
        cell.setCellStyle(style);
        i++;
      }
      rowCount++;
    }

    File currDir = new File(".");
    String path = currDir.getAbsolutePath();
    String fileLocation = path.substring(0, path.length() - 1) + "export.xlsx";

    FileOutputStream outputStream = new FileOutputStream(fileLocation);
    workbook.write(outputStream);
    outputStream.close();

    return new File(fileLocation);
  }

  public static byte[] convertFileToByteArray(File file) throws IOException {
    InputStream inputStream = new FileInputStream(file);
    byte[] bytes = inputStream.readAllBytes();
    inputStream.close();
    return bytes;
  }
}
