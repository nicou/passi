package fi.softala.ttl.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import fi.softala.ttl.model.Instructor;

public class InstructorRowMapper implements RowMapper<Instructor> {

	@Override
	public Instructor mapRow(ResultSet rs, int rowNum) throws SQLException {
		Instructor instructor = new Instructor();
		instructor.setUsername(rs.getString("username"));
		instructor.setFirstname(rs.getString("ope_etu"));
		instructor.setLastname(rs.getString("ope_suku"));
		instructor.setEmail(rs.getString("ope_email"));
		instructor.setSchool(rs.getString("koulu"));
		return instructor;
	}
}