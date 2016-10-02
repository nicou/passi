package fi.softala.ttl.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import fi.softala.ttl.model.Group;

public class GroupRowMapper implements RowMapper<Group> {

	@Override
	public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
		Group group = new Group();
		group.setGroupID(rs.getInt("group_id"));
		group.setGroupName(rs.getString("group_name"));
		return group;
	}
}
