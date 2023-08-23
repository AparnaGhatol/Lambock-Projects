package com.javabydeveloper.demo.template;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import com.javabydeveloper.base.BaseTest;
import com.javabydeveloper.util.UserType;

@SpringBootTest
@Sql(scripts = "/basic_mapping.sql") // to created DB tables and init sample DB data
@TestMethodOrder(value = OrderAnnotation.class) // to run tests in order
public class JdbcTemplateQueryForListTest extends BaseTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Order(1)
	@Test
	@DisplayName("find-by-id-test")
	void findWeakUsersTest() {

		String sql = "SELECT USERNAME FROM USER where LENGTH(USERNAME) < 7";

		List<String> users = jdbcTemplate.queryForList(sql, String.class);
		assertTrue(users.size() == 5);
	}

	@Order(2)
	@Test
	@DisplayName("find-users-test")
	void findUsersTest() {

		String sql = "SELECT ID, USERNAME, PASSWORD, IF(PASSWORD REGEXP '[0-9]', 'true', 'false') as HAS_NUMERIC_PASSWORD FROM USER";

		List<Map<String, Object>> users = jdbcTemplate.queryForList(sql);

		System.out.println("======== queryForList(String sql) ==========");
		users.forEach(rowMap -> {
			Long id = (Long) rowMap.get("ID");
			String userName = (String) rowMap.get("USERNAME");
			String password = (String) rowMap.get("PASSWORD");
			boolean hasNumericPass = Boolean.valueOf(rowMap.get("HAS_NUMERIC_PASSWORD").toString());

			System.out.println(id + "-" + userName + "-" +password+ "-"+ hasNumericPass);
		});
	}

	@Order(3)
	@DisplayName("find-users-age-test")
	@ParameterizedTest
	@EnumSource(names = { "EMPLOYEE" })
	void findUsersAgeTest(UserType userType) {

		// TIMESTAMPDIFF(YEAR, DATEOFBIRTH, CURDATE()) - gets dates differnce in years
		String sql = "SELECT USERNAME, TIMESTAMPDIFF(YEAR, DATEOFBIRTH, CURDATE()) as AGE FROM USER WHERE USERTYPE=?";

		List<Map<String, Object>> users = jdbcTemplate.queryForList(sql, new Object[] { userType }, new int[] { Types.VARCHAR });

		System.out.println("======== queryForList(String sql, Object[] args, int[] argTypes) ==========");
		users.forEach(rowMap -> {
			String userName = (String) rowMap.get("USERNAME");
			int age = ((Long) rowMap.get("AGE")).intValue();

			System.out.println(userName + "-" + age);
		});
	}

	@Order(4)
	@DisplayName("queryFotList with params 1")
	@ParameterizedTest
	@EnumSource(names = { "EMPLOYEE" })
	void queryForListWithParameters1(UserType userType) {

		// TIMESTAMPDIFF(YEAR, DATEOFBIRTH, CURDATE()) - gets dates differnce in years
		String sql = "SELECT USERNAME, TIMESTAMPDIFF(YEAR, DATEOFBIRTH, CURDATE()) as AGE FROM USER WHERE USERTYPE=?";

		List<Map<String, Object>> users = jdbcTemplate.queryForList(sql, userType.name());

		System.out.println("======== queryForList(String sql, Object... args) ==========");
		users.forEach(rowMap -> {
			String userName = (String) rowMap.get("USERNAME");
			int age = ((Long) rowMap.get("AGE")).intValue();
			System.out.println(userName + "-" + age);
		});
	}

	@Order(5)
	@DisplayName("queryFotList with params 2")
	@ParameterizedTest
	@EnumSource(names = { "EMPLOYEE" })
	void queryForListWithParameters2(UserType userType) {

		// find usernames by usertype and username
		String sql = "SELECT USERNAME FROM USER WHERE USERTYPE=? AND USERNAME LIKE ?";

		final Object[] args = new Object[] { userType, "K%" };
		final int[] types = new int[] { Types.VARCHAR, Types.VARCHAR };

		List<String> users = jdbcTemplate.queryForList(sql, args, types, String.class);

		System.out.println("======== queryForList(String sql, Object[] args, int[] argTypes, Class<T> elementType) ==========");
		users.forEach(user -> {
			System.out.println(user);
		});
	}
	
	@Order(6)
	@DisplayName("queryFotList with params 3")
	@ParameterizedTest
	@EnumSource(names = { "EMPLOYEE" })
	void queryForListWithParameters3(UserType userType) {

		// find usernames by usertype and username
		String sql = "SELECT USERNAME FROM USER WHERE USERTYPE=? AND USERNAME LIKE ?";

		List<String> users = jdbcTemplate.queryForList(sql, String.class, userType.name(), "K%");

		System.out.println("======== queryForList(String sql, Class<T> elementType,  @Nullable Object... args) ==========");
		users.forEach(user -> {
			System.out.println(user);
		});
	}

	@Order(7)
	@DisplayName("queryFotList with params 4")
	@ParameterizedTest
	@EnumSource(names = { "EMPLOYEE" })
	void queryForListWithParameters4(UserType userType) {

		// find usernames where usertype and
		String sql = "SELECT USERNAME FROM USER WHERE USERTYPE=? AND USERNAME LIKE ?";

		final Object[] args = new Object[] { userType.name(), "K%" };

		List<String> users = jdbcTemplate.queryForList(sql, args, String.class);

		System.out.println("======== queryForList(String sql, Object[] args, Class<T> elementType) ==========");
		users.forEach(user -> {
			System.out.println(user);
		});
	}
}
