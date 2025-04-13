package db;

import org.example.business.AcademicDTO;
import org.example.business.AccountDTO;
import org.example.db.AcademicDAO;
import org.example.db.AccountDAO;

import java.sql.SQLException;

public class TestContext {
  protected static final AccountDAO ACCOUNT_DAO = new AccountDAO();
  protected static final AcademicDAO ACADEMIC_DAO = new AcademicDAO();
  protected static final AccountDTO ACADEMIC_ACCOUNT = new AccountDTO("smith@uv.mx", "123Password");
  protected static final AcademicDTO ACADEMIC_DTO = new AcademicDTO.AcademicBuilder()
    .setID("00102")
    .setEmail(ACADEMIC_ACCOUNT.email())
    .setName("John")
    .setPaternalLastName("Doe")
    .setMaternalLastName("Smith")
    .setRole("PROFESSOR")
    .build();

  protected static void createAcademic() throws SQLException {
    ACCOUNT_DAO.create(ACADEMIC_ACCOUNT);
    ACADEMIC_DAO.create(ACADEMIC_DTO);
  }

  protected static void deleteAcademic() throws SQLException {
    ACCOUNT_DAO.delete(ACADEMIC_ACCOUNT.email());
    ACADEMIC_DAO.delete(ACADEMIC_DTO.getID());
  }
}
