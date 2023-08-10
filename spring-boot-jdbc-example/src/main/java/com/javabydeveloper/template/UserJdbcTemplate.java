package com.javabydeveloper.template;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.javabydeveloper.model.User;
import com.javabydeveloper.util.UserType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class UserJdbcTemplate {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private SimpleJdbcCall simpleJdbcCall;

	public int count() {
		return jdbcTemplate.queryForObject("SELECT count(*) FROM USER", Integer.class);
	}

	public int save(User user) {
		return jdbcTemplate.update(
				"INSERT INTO `USER` (USERNAME, PASSWORD, CREATEDTIME, UPDATEDTIME, USERTYPE, DATEOFBIRTH)"
						+ " VALUES(?,?,?,?,?,?)",
				user.getUserName(), user.getPassword(), user.getCreatedTime(), user.getUpdatedTime(),
				user.getUserType().toString(), user.getDateofBirth());
	}

	public int update(User user) {
		
		return jdbcTemplate.update("UPDATE USER SET PASSWORD = ? WHERE ID = ?", user.getPassword(), user.getId());
	}

	public int delete(User user) {
		return jdbcTemplate.update("DELETE FROM USER WHERE ID = ?", user.getId());
	}

	public List<User> findAll() {
		return jdbcTemplate.query("SELECT * FROM USER", (rs, rowNum) -> mapUserResult(rs));
	}

	public Optional<User> findById(Long id) {
		String sql = "SELECT * FROM USER WHERE ID = ?";
		try {
			return Optional
					.of(jdbcTemplate.queryForObject(sql, new Object[] { id }, new BeanPropertyRowMapper<User>()));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	public Optional<User> findByUserName(String name) {
		try {
			return jdbcTemplate.queryForObject("SELECT * FROM USER WHERE USERNAME = ?", new Object[] { name },
					(rs, rowNum) -> Optional.of(mapUserResult(rs)));
		} catch (EmptyResultDataAccessException e) {
			log.debug("No record found in database for "+name, e);
			return Optional.empty();
		}
	}

	public List<User> findByUserNameAndUserType(String name, String userType) {

		return jdbcTemplate.query("SELECT * FROM USER WHERE USERNAME = ? AND USERTYPE = ?",
				new Object[] { name, userType }, (rs, rowNum) -> mapUserResult(rs));
	}

	public List<User> findAll(Sort sort) {
		Order order = sort.toList().get(0);
		return jdbcTemplate.query(
				"SELECT * FROM USER ORDER BY " + order.getProperty() + " " + order.getDirection().name(),
				(rs, rowNum) -> mapUserResult(rs));
	}

	// defaults sorts by Id if order not provided
	public Page<User> findAll(Pageable page) {

		Order order = !page.getSort().isEmpty() ? page.getSort().toList().get(0) : Order.by("ID");

		List<User> users = jdbcTemplate.query("SELECT * FROM USER ORDER BY " + order.getProperty() + " "
				+ order.getDirection().name() + " LIMIT " + page.getPageSize() + " OFFSET " + page.getOffset(),
				(rs, rowNum) -> mapUserResult(rs));

		return new PageImpl<User>(users, page, count());
	}

	public Map<String, Object> searchUserByName(String searchTerm) {

		List<SqlParameter> parameters = Arrays.asList(new SqlParameter(Types.NVARCHAR));
		
		return jdbcTemplate.call(new CallableStatementCreator() {

			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {

				CallableStatement cs = con.prepareCall("{call search_users_proc(?)}");
				cs.setString(1, searchTerm);
				return cs;
			}
		}, parameters);
	}

	/* Calling Stored Procedure using SimpleJdbcCall */
	public Map<String, Object> searchUsersWithCount(String by, String direction, int size, int offset) {

		simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
				.withProcedureName("count_search_users_proc")
				.declareParameters(
						new SqlParameter("order_by", Types.NVARCHAR),
						new SqlParameter("direction", Types.VARCHAR),
						new SqlParameter("limit_", Types.INTEGER),
						new SqlParameter("offset_", Types.INTEGER),
						new SqlOutParameter("total", Types.INTEGER));
		
		
		Map<String, Object> out = simpleJdbcCall.execute(
				new MapSqlParameterSource("order_by", by)
				.addValue("direction", direction)
				.addValue("limit_", size)
				.addValue("offset_", offset));
     
		return out;
	}
	
	/* Example to get Multiple Results sets on stored procedure call */
	public Map<String, Object> searchWeakUsers(String userType) {

		simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
				.withProcedureName("search_weak_users_proc")
				.declareParameters(
						new SqlParameter("user_type", Types.VARCHAR),
						new SqlOutParameter("type_total", Types.INTEGER));
		
		
		Map<String, Object> out = simpleJdbcCall.execute(
				new MapSqlParameterSource("user_type", userType));
     
		return out;
	}
	
	/* Example to call Stored Function  */
	public boolean hasUserStrongPassword(Long id) {

		simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
				.withFunctionName("user_has_strongpwd_fn")
				.declareParameters(new SqlParameter("u_id", Types.BIGINT));
		
		boolean out = simpleJdbcCall.executeFunction(Boolean.class,
				new MapSqlParameterSource("u_id", id));
     
		return out;
	}
	
	

	private User mapUserResult(final ResultSet rs) throws SQLException {
		User user = new User();
		user.setId(rs.getLong("ID"));
		user.setUserName(rs.getString("USERNAME"));
		user.setPassword(rs.getString("PASSWORD"));
		user.setCreatedTime(rs.getDate("CREATEDTIME"));
		user.setUpdatedTime(rs.getDate("UPDATEDTIME"));
		user.setUserType(UserType.valueOf(rs.getString("USERTYPE")));
		user.setDateofBirth(rs.getDate("DATEOFBIRTH"));

		return user;
	}

}
