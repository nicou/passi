package fi.softala.ttl.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import fi.softala.ttl.model.Instructor;

public class InstructorRowMapper implements RowMapper<Instructor> {

	@Override
	public Instructor mapRow(ResultSet rs, int rowNum) throws SQLException {
		Instructor instructor = new Instructor();
		instructor.setUserID(rs.getInt("user_id"));
		instructor.setFirstname(rs.getString("firstname"));
		instructor.setLastname(rs.getString("lastname"));
		instructor.setEmail(rs.getString("email"));
		instructor.setPhone(rs.getString("phone"));
		return instructor;
	}
}