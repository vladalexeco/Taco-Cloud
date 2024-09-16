package tacos.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import tacos.Ingredient;

@Repository
public class JdbcIngredientRepository implements IngredientRepository {
	
	private JdbcTemplate jdbcTemplate;
	
	public JdbcIngredientRepository(JdbcTemplate jdbcTemplate) {
		
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Iterable<Ingredient> findAll() {
		
		return jdbcTemplate.query(
				"select id, name, type from Ingredient", 
				this::mapToRowIngredient
		);
	}

	@Override
	public Optional<Ingredient> findById(String id) {
		
		List<Ingredient> ingredients = jdbcTemplate.query(
				"select id, name, type from Ingredient where id=?", 
				this::mapToRowIngredient,
				id
		);
		
		return ingredients.size() == 0 ? Optional.empty() : Optional.of(ingredients.get(0));
	}

	@Override
	public Ingredient save(Ingredient ingredient) {
		
		jdbcTemplate.update(
			"insert into Ingredient (id, name, type) values (?, ?, ?)",
			ingredient.getId(),
			ingredient.getName(),
			ingredient.getType().toString()
		);
		
		return ingredient;
	}
	
	private Ingredient mapToRowIngredient(ResultSet row, int rowNum) throws SQLException {
		return new Ingredient(
				row.getString("id"),
				row.getString("name"),
				Ingredient.Type.valueOf(row.getString("type"))
		);
	}
}
