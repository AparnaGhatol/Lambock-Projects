package com.javabydeveloper.demo.template;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import com.javabydeveloper.base.BaseTest;
import com.javabydeveloper.model.User;
import com.javabydeveloper.util.UserType;

@SpringBootTest
@Sql(scripts = "/basic_mapping.sql") // to created DB tables and init sample DB data
@TestMethodOrder(value = OrderAnnotation.class) // to run tests in order
public class JdbcTemplateQueryForObjectTest extends BaseTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	//@Disabled
	@Test
	@DisplayName("users-count-test")
	@Order(1)
	void usersCountTest() {
		
		String sql = "SELECT count(*) FROM USER";
		
		int total = jdbcTemplate.queryForObject(sql, Integer.class);
		System.out.println(total);
		assertTrue(total == 12);
	}
	
	@Order(2)
	@DisplayName("find-by-id-test")
	@ParameterizedTest
	@ValueSource(longs = {11})
	void findByIdTest(Long id) {
		
		String sql = "SELECT * FROM USER where ID = ?";
		
		Optional<User> user = Optional.ofNullable(jdbcTemplate.queryForObject(sql, new Object[] {id},  new UserMapper()));
		assertTrue(user.get().getId().equals(id));
	}

	@Disabled("Run on Mysql only")
	@Order(3)
	@DisplayName("srong-password-test")
	@ParameterizedTest
	@ValueSource(strings = {"PeterM"})
	void userHasStrongPasswordTest(String name) {
		
		String sql = "SELECT IF((SELECT LENGTH(password) FROM USER where USERNAME=?) > 6, true, false)";
		
		boolean hasStrongPassword = jdbcTemplate.queryForObject(sql, new Object[] {name},  Boolean.class);
		
		assertTrue(hasStrongPassword);
	}
	
	
	@Test
	@DisplayName("queryForObject()1")
	@Order(4)
	void usersNameTest() {
		
		// test queryForObject(String sql, Class<T> requiredType)
		
		String sql = "SELECT USERNAME FROM USER WHERE ID=?";
		
		final String name = jdbcTemplate.queryForObject(sql, String.class, 10);
		System.out.println(name);
		assertNotNull(name);
	}
	
	@Test
	@DisplayName("queryForObject()2-1")
	@Order(5)
	void usernameCountTest() {
		
		// test queryForObject(String sql, Class<T> requiredType, Object... args)
		
		String sql = "SELECT count(*) FROM USER WHERE USERNAME LIKE ?";
		
		final int count = jdbcTemplate.queryForObject(sql, Integer.class, "Donald%");
		System.out.println(count);
		assertTrue(count == 1);
	}
	
	@Test
	@DisplayName("queryForObject()2-2")
	@Order(5)
	void usernameCountMutipleParametersTest() {
		
		// test queryForObject(String sql, Class<T> requiredType, Object... args)
		
		String sql = "SELECT count(*) FROM USER WHERE USERNAME LIKE ? OR ID = ?";
		
		final int count = jdbcTemplate.queryForObject(sql, Integer.class, "Donald%", 10);
		System.out.println(count);
		assertTrue(count == 1);
	}

	@Test
	@DisplayName("queryForObject()3")
	@Order(6)
	void usersCountMultipleArgsTest() {
		
		// queryForObject(String sql, Object[] args, int[] argTypes, Class<T> requiredType)
		
		String sql = "SELECT count(*) FROM USER WHERE USERNAME LIKE ? OR ID = ?";
		
		Object[] args = { "Peter%", 10 };
		int[] argTypes = { java.sql.Types.VARCHAR, java.sql.Types.INTEGER };
		
		final int count = jdbcTemplate.queryForObject(sql, args, argTypes, Integer.class);
		System.out.println(count);
		assertTrue(count == 4);
	}
	
	@Test
	@DisplayName("queryForObject()4")
	@Order(7)
	void userObjectMultipleArgsTest() {
		
		// queryForObject(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper)
		
		String sql = "SELECT * FROM USER WHERE USERNAME = ? AND ID = ?";
		
		Object[] args = { "DonaldT", 10};
		int[] argTypes = { java.sql.Types.VARCHAR, java.sql.Types.INTEGER };
		
		final User user = jdbcTemplate.queryForObject(sql, args, argTypes, new UserMapper());
		System.out.println(user);
		assertTrue("DonaldT".equals(user.getUserName()));
	}
	
	@Test
	@DisplayName("queryForObject()5")
	@Order(8)
	void userObjectStaticQuery() {
		
		// queryForObject(String sql, RowMapper<T> rowMapper)
		
		String sql = "SELECT * FROM USER WHERE ID = 10";
		
		final User user = jdbcTemplate.queryForObject(sql, new UserMapper());
		System.out.println(user);
		assertTrue("DonaldT".equals(user.getUserName()));
	}
	
	@Test
	@DisplayName("queryForObject()6")
	@Order(9)
	void userObjectWithArgs() {
		
		// queryForObject(String sql, RowMapper<T> rowMapper, Object... args)
		
		String sql = "SELECT * FROM USER WHERE USERNAME = ? AND USERTYPE = ?";
		
		final User user = jdbcTemplate.queryForObject(sql, new UserMapper(), "DonaldT", UserType.STUDENT.name());
		System.out.println(user);
		assertTrue("DonaldT".equals(user.getUserName()));
	}
	
	@Test
	@DisplayName("queryForObject()7")
	@Order(10)
	void userWithDataClassRowMapperWithArgs() {
		
		// queryForObject(String sql, RowMapper<T> rowMapper, Object... args)
		
		String sql = "SELECT * FROM USER WHERE USERNAME = ? AND USERTYPE = ?";
		
		final User user = jdbcTemplate.queryForObject(sql, new DataClassRowMapper<User>(), "DonaldT", UserType.STUDENT.name());
		System.out.println(user);
		assertTrue("DonaldT".equals(user.getUserName()));
	}
}
