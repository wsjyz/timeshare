package com.timeshare.dao.impl.assembly;

import com.timeshare.dao.BaseDAO;
import com.timeshare.dao.assembly.CollectionDAO;
import com.timeshare.dao.assembly.CollectionDAO;
import com.timeshare.domain.assembly.Collection;
import com.timeshare.domain.assembly.Collection;
import com.timeshare.utils.CommonStringUtils;
import com.timeshare.utils.Contants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/9/23.
 */
@Repository("CollectionDAO")
public class CollectionDAOImpl extends BaseDAO implements CollectionDAO {
    @Override
    public String saveCollection(Collection Collection) {
        StringBuilder sql = new StringBuilder("INSERT INTO t_collection (collection_id,user_id,assembly_id,create_time) VALUES (?, ?, ?, ?);");
        final String id = CommonStringUtils.genPK();
        int result = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1,id);
                ps.setString(2,Collection.getUserId());
                ps.setString(3,Collection.getAssemblyId());
                ps.setString(4,Collection.getCreateTime());
            }
        });
        if(result > 0){
            return id;
        }else{
            return Contants.FAILED;
        }
    }

    @Override
    public List<Collection> getCollectionByAssemblyId(String assemblyId) {
        StringBuilder sql = new StringBuilder("select * from t_collection where assembly_id=?");
        List<Collection> list= getJdbcTemplate().query(sql.toString(),new Object[]{assemblyId},new CollectionRowMapper());
        if (CollectionUtils.isEmpty(list)){
            return new ArrayList<Collection>();
        }
        return list;
    }


    class CollectionRowMapper implements RowMapper<Collection>{
        @Override
        public Collection mapRow(ResultSet rs, int i) throws SQLException {
            Collection Collection = new Collection();
            Collection.setCollectionId(rs.getString("Collection_id"));
            Collection.setAssemblyId(rs.getString("assembly_id"));
            Collection.setUserId(rs.getString("user_id"));
            Collection.setCreateTime(rs.getString("create_time"));
            return Collection;
        }
    }
}
