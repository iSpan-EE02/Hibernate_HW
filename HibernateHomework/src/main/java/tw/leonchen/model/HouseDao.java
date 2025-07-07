package tw.leonchen.model;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

public class HouseDao implements IHouseDao {
	
	private Session session;
	
	public HouseDao(Session session) {
		this.session = session;
	}

	@Override
	public House insert(House insertBean) throws SQLException {
		// TODO Auto-generated method stub
		if(insertBean !=null) {
			session.persist(insertBean);
			return insertBean;
		}
		return null;
	}

	@Override
	public House update(House updateBean) throws SQLException {
		// TODO Auto-generated method stub
		House resultBean = session.find(House.class, updateBean.getHouseid());
		if(resultBean!=null) {
			return session.merge(updateBean);
		}
		return null;
	}

	@Override
	public boolean deleteById(House deleteBean) throws SQLException {
		// TODO Auto-generated method stub
		House resultBean = session.get(House.class, deleteBean.getHouseid());
		
		if (resultBean!=null) {
			session.remove(resultBean);
			return true;
		}
		
		return false;
	}

	@Override
	public House queryById(Integer id) throws SQLException {
		// TODO Auto-generated method stub
		House resultBean = session.find(House.class,id);
		return resultBean;
	}

	@Override
	public List<House> queryAll() throws SQLException {
		// TODO Auto-generated method stub
		Query<House> query = session.createQuery("from House", House.class);
		return query.list();
	}

}
