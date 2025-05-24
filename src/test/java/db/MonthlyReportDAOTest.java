package db;

import org.example.business.dao.MonthlyReportDAO;
import org.example.business.dao.filter.FilterMonthlyReport;
import org.example.business.dto.MonthlyReportDTO;
import org.example.business.dto.PracticeDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class MonthlyReportDAOTest {
  public static final MonthlyReportDAO MONTHLY_REPORT_DAO = new MonthlyReportDAO();
  private static final int MONTHLY_REPORT_MONTH = 1;
  private static final int MONTHLY_REPORT_YEAR = 2026;

  public static MonthlyReportDTO createOneTestMonthlyReport() throws SQLException {
    PracticeDTO practice = PracticeDAOTest.createOneTestPractice();

    MonthlyReportDTO dataObject = new MonthlyReportDTO.MonthlyReportBuilder()
      .setIDStudent(practice.getIDStudent())
      .setIDProject(practice.getIDProject())
      .setReport("I did a lot of work this month")
      .setWorkedHours(40)
      .setMonth(MONTHLY_REPORT_MONTH)
      .setYear(MONTHLY_REPORT_YEAR)
      .build();

    MONTHLY_REPORT_DAO.createOne(dataObject);
    return dataObject;
  }

  public static void deleteOneTestMonthlyReport() throws SQLException {
    PracticeDAOTest.deleteOneTestPractice();
  }

  @AfterEach
  public void tearDown() throws SQLException {
    deleteOneTestMonthlyReport();
  }

  @Test
  public void testCreateOneMonthlyReport() {
    assertDoesNotThrow(() -> {
      MonthlyReportDTO monthlyReport = createOneTestMonthlyReport();

      MonthlyReportDTO createdMonthlyReport = MONTHLY_REPORT_DAO.getOne(
        new FilterMonthlyReport(
          monthlyReport.getIDStudent(),
          monthlyReport.getIDProject(),
          MONTHLY_REPORT_MONTH,
          MONTHLY_REPORT_YEAR
        )
      );

      Assertions.assertEquals(monthlyReport, createdMonthlyReport);
    });
  }

  @Test
  public void testGetAllMonthlyReports() {
    assertDoesNotThrow(() -> {
      createOneTestMonthlyReport();

      List<MonthlyReportDTO> monthlyReportList = MONTHLY_REPORT_DAO.getAll();

      Assertions.assertNotNull(monthlyReportList);
      Assertions.assertFalse(monthlyReportList.isEmpty());
    });
  }

  @Test
  public void testGetOneMonthlyReport() {
    assertDoesNotThrow(() -> {
      MonthlyReportDTO monthlyReport = createOneTestMonthlyReport();

      MonthlyReportDTO createdMonthlyReport = MONTHLY_REPORT_DAO.getOne(
        new FilterMonthlyReport(
          monthlyReport.getIDStudent(),
          monthlyReport.getIDProject(),
          MONTHLY_REPORT_MONTH,
          MONTHLY_REPORT_YEAR
        )
      );

      Assertions.assertEquals(monthlyReport, createdMonthlyReport);
    });
  }

  @Test
  public void testUpdateOneMonthlyReport() {
    assertDoesNotThrow(() -> {
      MonthlyReportDTO createdMonthlyReport = createOneTestMonthlyReport();

      MonthlyReportDTO updatedMonthlyReport = new MonthlyReportDTO.MonthlyReportBuilder()
        .setIDStudent(createdMonthlyReport.getIDStudent())
        .setReport("I didn't do a lot of work this month")
        .setWorkedHours(20)
        .setMonth(MONTHLY_REPORT_MONTH)
        .setYear(MONTHLY_REPORT_YEAR)
        .setIDProject(createdMonthlyReport.getIDProject())
        .build();
      MONTHLY_REPORT_DAO.updateOne(updatedMonthlyReport);

      Assertions.assertEquals(updatedMonthlyReport, MONTHLY_REPORT_DAO.getOne(
        new FilterMonthlyReport(
          StudentDAOTest.STUDENT_DTO.getID(),
          ProjectDAOTest.PROJECT_DTO.getID(),
          MONTHLY_REPORT_MONTH,
          MONTHLY_REPORT_YEAR
        )
      ));
    });
  }

  @Test
  public void testDeleteOneMonthlyReport() {
    assertDoesNotThrow(() -> {
      createOneTestMonthlyReport();
      MonthlyReportDTO createdMonthlyReport = MONTHLY_REPORT_DAO.getOne(
        new FilterMonthlyReport(
          StudentDAOTest.STUDENT_DTO.getID(),
          ProjectDAOTest.PROJECT_DTO.getID(),
          MONTHLY_REPORT_MONTH,
          MONTHLY_REPORT_YEAR
        )
      );
      Assertions.assertNotNull(createdMonthlyReport);

      deleteOneTestMonthlyReport();
      MonthlyReportDTO deletedMonthlyReport = MONTHLY_REPORT_DAO.getOne(
        new FilterMonthlyReport(
          StudentDAOTest.STUDENT_DTO.getID(),
          ProjectDAOTest.PROJECT_DTO.getID(),
          MONTHLY_REPORT_MONTH,
          MONTHLY_REPORT_YEAR
        )
      );

      Assertions.assertNull(deletedMonthlyReport);
    });
  }
}
