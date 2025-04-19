package com.semiramide.timetracker.core.usecase.export;

import com.semiramide.timetracker.core.util.WorklogExportDTO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.semiramide.timetracker.core.util.ExcelExportUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ExcelExportIT {

  private static final String FILE_NAME = "export.xlsx";
  private String fileLocation;
  private File tempFile;

  @BeforeEach
  public void prepareExcelFile() throws IOException, IllegalAccessException {

    File currDir = new File(".");
    String path = currDir.getAbsolutePath();
    fileLocation = path.substring(0, path.length() - 1) + FILE_NAME;

    WorklogExportDTO row1 =
        WorklogExportDTO.builder()
            .employee("John Doe")
            .project("Project1")
            .type("work type 1")
            .task("task 1")
            .description("description 1")
            .time(5.25)
            .date(LocalDate.now())
            .build();

    WorklogExportDTO row2 =
        WorklogExportDTO.builder()
            .employee("Jane Doe")
            .project("Project1")
            .type("work type 2")
            .task("task 2")
            .description("description 2")
            .time(8.50)
            .date(LocalDate.now())
            .build();

    List<WorklogExportDTO> rowDTOList = new ArrayList<>();
    rowDTOList.add(row1);
    rowDTOList.add(row2);

    tempFile = ExcelExportUtil.writeExcel(rowDTOList);
  }

  @Test
  public void shouldParseAndReadExcelFile() throws IOException {
    Map<Integer, List<String>> data = ExcelExportUtil.readExcel(tempFile);

    Assertions.assertEquals("employee", data.get(0).get(0));
    Assertions.assertEquals("John Doe", data.get(1).get(0));
    Assertions.assertEquals("8.50", data.get(2).get(5));
    Assertions.assertEquals(LocalDate.now().toString(), data.get(2).get(6));
  }

  @AfterEach
  public void cleanUp() throws IOException {
    Files.deleteIfExists(Path.of(fileLocation));
  }
}
