package tw.leonchen.model;

import java.sql.SQLException;
import java.util.List;

public interface IHouseService {
	public House insert(House insertBean) throws SQLException ;
	public House update(House updateBean) throws SQLException ;
	public boolean deleteById(House deleteBean) throws SQLException ;
	public House queryById(Integer id) throws SQLException;
	public List<House> queryAll() throws SQLException;
}
