package fi.softala.ttl.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import fi.softala.ttl.model.Member;

public class MemberRowMapper implements RowMapper<Member> {

	@Override
	public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
		Member member = new Member();
		member.setUserID(rs.getInt("user_id"));
		member.setFirstname(rs.getString("firstname"));
		member.setLastname(rs.getString("lastname"));
		member.setEmail(rs.getString("email"));
		member.setPhone(rs.getString("phone"));
		return member;
	}
}