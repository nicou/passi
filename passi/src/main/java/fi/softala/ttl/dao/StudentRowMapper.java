package fi.softala.ttl.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import fi.softala.ttl.model.Student;

public class StudentRowMapper implements RowMapper<Student> {

	@Override
	public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
		Student student = new Student();
		student.setStudentID(rs.getInt("opi_id"));
		student.setUsername(rs.getString("username"));
		student.setFirstname(rs.getString("opi_etunimi"));
		student.setLastname(rs.getString("opi_sukunimi"));
		student.setSchool(rs.getString("opi_koulu"));
		student.setEmail(rs.getString("opi_email"));
		return student;
	}
}