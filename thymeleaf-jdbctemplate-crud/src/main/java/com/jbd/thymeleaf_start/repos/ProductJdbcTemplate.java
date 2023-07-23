package com.jbd.thymeleaf_start.repos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.jbd.thymeleaf_start.domain.Product;
import com.jbd.thymeleaf_start.model.ProductDTO;
import com.jbd.thymeleaf_start.util.ProductMappingUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ProductJdbcTemplate {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// count
	public int count() {
		return jdbcTemplate.queryForObject("SELECT count(*) FROM PRODUCT", Integer.class);
	}

	// save product
	public int save(Product product) {
		return jdbcTemplate.update(
				"INSERT INTO `PRODUCT` (`discount`,`name`,`price`,`sku`,`stock_status`) VALUES (?,?,?,?,?)",
				product.getDiscount(), product.getName(), product.getPrice(), product.getSku(), product.getStockStatus());
	}

	//update product
	public int update(Product product) {
		return jdbcTemplate.update("UPDATE PRODUCT SET discount = ?,"
				+ "name = ?,"
				+ "price = ?,"
				+ "sku = ?,"
				+ "stock_status = ?"
				+ " WHERE ID = ?", product.getDiscount(), product.getName(), product.getPrice(), product.getSku(), product.getStockStatus(), product.getId());
	}

	// delete product
	public int delete(Long id) {
		return jdbcTemplate.update("DELETE FROM PRODUCT WHERE ID = ?", id);
	}

	// retrieve all products
	public List<ProductDTO> findAll() {
		return jdbcTemplate.query("SELECT * FROM PRODUCT", (rs, rowNum) -> mapUserResult(rs));
	}

	// find product by id
	public Optional<Product> findById(Long id) {
		String sql = "SELECT * FROM PRODUCT WHERE ID = ?";
		try {
			return Optional
					.of(jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<Product>(Product.class), new Object[] { id }));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	// find product by name
	public Optional<ProductDTO> findByProductName(String name) {
		try {
			return jdbcTemplate.queryForObject("SELECT * FROM PRODUCT WHERE NAME = ?",
					(rs, rowNum) -> Optional.of(mapUserResult(rs)), new Object[] { name });
		} catch (EmptyResultDataAccessException e) {
			log.debug("No record found in database for "+name, e);
			return Optional.empty();
		}
	}

	//find product by name and stockSstatus
	public List<ProductDTO> findByProductNameAndStockStatus(String name, String stockStatus) {

		return jdbcTemplate.query("SELECT * FROM PRODUCT WHERE NAME = ? AND STOCK_STATUS = ?",
				(rs, rowNum) -> mapUserResult(rs), new Object[] { name, stockStatus });
	}

	// find sorted results
	public List<ProductDTO> findAll(Sort sort) {
		Order order = sort.toList().get(0);
		return jdbcTemplate.query(
				"SELECT * FROM PRODUCT ORDER BY " + order.getProperty() + " " + order.getDirection().name(),
				(rs, rowNum) -> mapUserResult(rs));
	}

	// get paginated results, default sorts by Id if order not provided
	public Page<ProductDTO> findAll(Pageable page) {

		Order order = !page.getSort().isEmpty() ? page.getSort().toList().get(0) : Order.by("ID");

		List<ProductDTO> users = jdbcTemplate.query("SELECT * FROM PRODUCT ORDER BY " + order.getProperty() + " "
				+ order.getDirection().name() + " LIMIT " + page.getPageSize() + " OFFSET " + page.getOffset(),
				(rs, rowNum) -> mapUserResult(rs));

		return new PageImpl<ProductDTO>(users, page, count());
	}


	// map results to Product
	private ProductDTO mapUserResult(final ResultSet rs) throws SQLException {
		Product product = new Product();
		product.setId(rs.getLong("ID"));
		product.setDiscount(rs.getInt("DISCOUNT"));
		product.setName(rs.getString("NAME"));
		product.setPrice(rs.getInt("PRICE"));
		product.setSku(rs.getString("SKU"));
		product.setStockStatus(rs.getString("STOCK_STATUS"));
		
		return ProductMappingUtils.mapToDTO(product, new ProductDTO());
	}

}
